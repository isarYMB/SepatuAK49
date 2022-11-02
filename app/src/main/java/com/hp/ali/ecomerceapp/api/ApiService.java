package com.hp.ali.ecomerceapp.api;

/**
 * Created by Robby Dianputra on 10/31/2017.
 */

import com.hp.ali.ecomerceapp.model.city.ItemCity;
import com.hp.ali.ecomerceapp.model.cost.ItemCost;
import com.hp.ali.ecomerceapp.model.province.ItemProvince;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Province
    @GET("province")
    @Headers("key:769116a1c62f16076ed1f5b8cfec0acd")
    Call<ItemProvince> getProvince ();

    // City
    @GET("city")
    @Headers("key:769116a1c62f16076ed1f5b8cfec0acd")
    Call<ItemCity> getCity (@Query("province") String province);

    // Cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost (@Field("key") String Token,
                            @Field("origin") String origin,
                            @Field("destination") String destination,
                            @Field("weight") String weight,
                            @Field("courier") String courier);

}
