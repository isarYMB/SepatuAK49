package com.hp.ali.ecomerceapp.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.SignInButton;
import com.hp.ali.ecomerceapp.Koneksi.DbContract;
import com.hp.ali.ecomerceapp.Koneksi.SessionManager;
import com.hp.ali.ecomerceapp.Koneksi.VolleyConnection;
import com.hp.ali.ecomerceapp.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.ali.ecomerceapp.adapter.CityAdapter;
import com.hp.ali.ecomerceapp.adapter.ExpedisiAdapter;
import com.hp.ali.ecomerceapp.adapter.ProvinceAdapter;
import com.hp.ali.ecomerceapp.api.ApiService;
import com.hp.ali.ecomerceapp.api.ApiUrl;
import com.hp.ali.ecomerceapp.login.InputOTPActivity;
import com.hp.ali.ecomerceapp.login.LogInActivity;
import com.hp.ali.ecomerceapp.login.RegisterActivity;
import com.hp.ali.ecomerceapp.model.city.ItemCity;
import com.hp.ali.ecomerceapp.model.cost.ItemCost;
import com.hp.ali.ecomerceapp.model.expedisi.ItemExpedisi;
import com.hp.ali.ecomerceapp.model.province.ItemProvince;
import com.hp.ali.ecomerceapp.model.province.Result;
import com.google.gson.Gson;
import com.hp.ali.ecomerceapp.models.lottiedialogfragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static maes.tech.intentanim.CustomIntent.customType;

public class EditAlamat extends AppCompatActivity {
    ImageView img_back;
    private EditText etToCity, etToProvince,etKodepos,etAlamatLengkap;
    private AlertDialog.Builder alert;
    private AlertDialog ad;
    private EditText searchList;
    private ListView mListView;
    private ProvinceAdapter adapter_province;
    private List<Result> ListProvince = new ArrayList<Result>();
    private CityAdapter adapter_city;
    private List<com.hp.ali.ecomerceapp.model.city.Result> ListCity = new ArrayList<com.hp.ali.ecomerceapp.model.city.Result>();
    private ProgressDialog progressDialog;
    Button updateAlamat;
    SessionManager sessionManager;
    int infoID;
    String TAG,TAG2;
    String PROVINSI,KOTA;
    lottiedialogfragment lottiedialogfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alamat);
        img_back = findViewById(R.id.img_back);
        etToProvince = (EditText) findViewById(R.id.edt_provinsi);
        etToCity = (EditText) findViewById(R.id.edt_kota);
        etKodepos = (EditText) findViewById(R.id.edt_post_code);
        etAlamatLengkap = (EditText) findViewById(R.id.edt_alamatLengkap);
        updateAlamat = findViewById(R.id.update_alamat);
        sessionManager = new SessionManager(this);
        lottiedialogfragment = new lottiedialogfragment(this);

        HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
        infoID = id.get(sessionManager.SP_ID);

        updateAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String provinsi = etToProvince.getText().toString();
                String kota = etToCity.getText().toString();
                String kodePost = etKodepos.getText().toString();
                String alamatLengkap = etAlamatLengkap.getText().toString();

                if (kota.equals(KOTA)){
                    TAG2 = TAG;
                }else {
                    TAG2 = etToCity.getTag().toString();
                }

                if (provinsi.equals("") || kota.equals("") || kodePost.equals("")
                        || alamatLengkap.equals(""))
                {
                    Toast.makeText(EditAlamat.this, "Harap megisi semua data", Toast.LENGTH_SHORT).show();
                }else {
                    updateAlamat(provinsi,kota,kodePost,alamatLengkap,TAG2);

                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etToProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProvince(etToProvince, etToCity);

            }
        });

        etToCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (etToProvince.getTag().equals("")) {
                        etToProvince.setError("Please chooise your to province");
                    } else {
                        popUpCity(etToCity, etToProvince);
                    }
                } catch (NullPointerException e) {
                    etToProvince.setError("Please chooise your to province");
                }
            }
        });

        getInfoAkun(infoID);
    }

    private void getInfoAkun(int infoID) {
        if (checkNetworkConnection()){
            lottiedialogfragment.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GETINFOAKUN_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String data = jsonObject.getString("data");
                                if (resp.equals("true")) {
                                    JSONObject jsonObject2 = new JSONObject(data);
                                    String provinsi = jsonObject2.getString("PROVINSI");
                                    String kabupaten = jsonObject2.getString("KOTA");
                                    TAG = jsonObject2.getString("TAG_KOTA");
                                    String kodePos = jsonObject2.getString("KODE_POS");
                                    String alamatDetail = jsonObject2.getString("ALAMAT_LENGKAP");

                                    if (!provinsi.equals("Belum di atur") && !kabupaten.equals("Belum di atur") &&!kodePos.equals("Belum di atur")&&!alamatDetail.equals("Belum di atur")){
                                        PROVINSI = provinsi;
                                        KOTA = kabupaten;
                                        etToProvince.setText(PROVINSI);
                                        etToCity.setText(KOTA);
                                        etKodepos.setText(kodePos);
                                        etAlamatLengkap.setText(alamatDetail);
                                    }

                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("ID", String.valueOf(infoID));
                    return params;
                }
            };

            VolleyConnection.getInstance(EditAlamat.this).addToRequestQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottiedialogfragment.cancel();
                }
            }, 2000);

        }else {
            Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void popUpProvince(final EditText etProvince, final EditText etCity ) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(EditAlamat.this);
        alert.setTitle("Daftar Provinsi");
        alert.setMessage("Pilih Provinsi Kamu Saat Ini");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new EditAlamat.MyTextWatcherProvince(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListProvince.clear();
        adapter_province = new ProvinceAdapter(EditAlamat.this, ListProvince);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                Result cn = (Result) o;

                etProvince.setError(null);
                etProvince.setText(cn.getProvince());
                etProvince.setTag(cn.getProvinceId());

                etCity.setText("");
                etCity.setTag("");

                ad.dismiss();
            }
        });

        getProvince();

    }

    public void popUpCity(final EditText etCity, final EditText etProvince) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(EditAlamat.this);
        alert.setTitle("Daftar Kota/Kabupaten");
        alert.setMessage("Pilih Kota/Kabupaten Kamu Saat Ini");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new EditAlamat.MyTextWatcherCity(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListCity.clear();
        adapter_city = new CityAdapter(EditAlamat.this, ListCity);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                com.hp.ali.ecomerceapp.model.city.Result cn = (com.hp.ali.ecomerceapp.model.city.Result) o;

                etCity.setError(null);
                etCity.setText(cn.getCityName());
                etCity.setTag(cn.getCityId());

                ad.dismiss();
            }
        });
        getCity(etProvince.getTag().toString());
    }
    public void getProvince() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ItemProvince> call = service.getProvince();
        call.enqueue(new Callback<ItemProvince>() {
            @Override
            public void onResponse(Call<ItemProvince> call, Response<ItemProvince> response) {
                lottiedialogfragment.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {
                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        Result itemProvince = new Result(
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince()
                        );

                        ListProvince.add(itemProvince);
                        mListView.setAdapter(adapter_province);
                    }
                    adapter_province.setList(ListProvince);
                    adapter_province.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(EditAlamat.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemProvince> call, Throwable t) {
                lottiedialogfragment.dismiss();
                Toast.makeText(EditAlamat.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCity(String id_province) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemCity> call = service.getCity(id_province);

        call.enqueue(new Callback<ItemCity>() {
            @Override
            public void onResponse(Call<ItemCity> call, Response<ItemCity> response) {

                lottiedialogfragment.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        com.hp.ali.ecomerceapp.model.city.Result itemProvince = new com.hp.ali.ecomerceapp.model.city.Result(
                                response.body().getRajaongkir().getResults().get(a).getCityId(),
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince(),
                                response.body().getRajaongkir().getResults().get(a).getType(),
                                response.body().getRajaongkir().getResults().get(a).getCityName(),
                                response.body().getRajaongkir().getResults().get(a).getPostalCode()
                        );

                        ListCity.add(itemProvince);
                        mListView.setAdapter(adapter_city);
                    }

                    adapter_city.setList(ListCity);
                    adapter_city.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(EditAlamat.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCity> call, Throwable t) {
                lottiedialogfragment.dismiss();
                Toast.makeText(EditAlamat.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class MyTextWatcherProvince implements TextWatcher {

        private View view;

        private MyTextWatcherProvince(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_province.filter(editable.toString());
                    break;
            }
        }
    }

    private class MyTextWatcherCity implements TextWatcher {

        private View view;

        private MyTextWatcherCity(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_city.filter(editable.toString());
                    break;
            }
        }
    }

    public void updateAlamat (final String provinsi, final String kota,final String kodePos, final String alamatLengkap,final String tagKota){
        if (checkNetworkConnection()){
            lottiedialogfragment.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_UPDATEALAMAT_URL,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                if (resp.equals("true")) {
                                    if (kota.equals(KOTA)){
                                        sessionManager.saveSPString(SessionManager.SP_TAG, TAG);
                                    }else {
                                        sessionManager.saveSPString(SessionManager.SP_TAG, etToCity.getTag().toString());
                                    }

                                    sessionManager.saveSPString(SessionManager.SP_ALAMAT, String.valueOf(etAlamatLengkap.getText()));
                                    Toast.makeText(getApplicationContext(), "Alamat berhasil disimpan", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    HashMap<Integer, Integer> id = sessionManager.getuserKonsul();
                    int sid = id.get(sessionManager.SP_ID);
                    params.put("ID", String.valueOf(sid));
                    params.put("PROVINSI", provinsi);
                    params.put("KOTA", kota);
                    params.put("TAG_KOTA", tagKota);
                    params.put("ALAMAT_LENGKAP", alamatLengkap);
                    params.put("KODE_POS", kodePos);
                    return params;
                }
            };

            VolleyConnection.getInstance(EditAlamat.this).addToRequestQue(stringRequest);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lottiedialogfragment.cancel();
                    onBackPressed();
                    finish();
                }
            }, 4000);

        }else {
            Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}