package com.hp.ali.ecomerceapp.communication;

import android.app.Activity;

import java.io.IOException;

import retrofit2.Callback;
import retrofit2.Response;

public abstract class ActivityBlockingCallBack<T> implements Callback<T> {
    // private static final Logger logger = Logger.getLogger(ActivityBlockingCallBack.class);
    private Activity activity;
//    private CustomProgressBar pd;
//
//    private boolean interactive = true;
//    private boolean finishActivityOnError;
//    private SimpleDialog simpleDialog;
    public ActivityBlockingCallBack(Activity activity) {
        this.activity = activity;
        initModalDialog();
    }



    public ActivityBlockingCallBack(Activity activity, boolean interactive, boolean finishActivityOnError) {
        this.activity = activity;
//        this.interactive = interactive;
//        this.finishActivityOnError = finishActivityOnError;
        initModalDialog();
    }

    private void initModalDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
//                    pd = CustomProgressBar.getInstance();
//                    pd.showProgress(activity, false);
                } catch (Exception ex) {

                }
            }
        });
    }

//    public boolean isInteractive() {
//        return interactive;
//    }
//
//    public void setInteractive(boolean interactive) {
//        this.interactive = interactive;
//    }
//
//    public boolean isFinishActivityOnError() {
//        return finishActivityOnError;
//    }
//
//    public void setFinishActivityOnError(boolean finishActivityOnError) {
//        this.finishActivityOnError = finishActivityOnError;
//    }

//    @Override
//    public final void onResponse(Call<T> call, Response<T> response) {
//        try {
//            if (response.isSuccessful()) {
//                handleSuccess(response);
//            } else {
//                try {
//                    handleServerError(processResponseForServerError(response));
//                } catch (Exception e) {
////                    logger.e("onResponse: handleServerError :: failed > " + e.toString(), e);
//                    handleFailure(e);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
////            if (pd != null && pd.isShowing()) {
////                activity.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        try {
////                            pd.hideProgress();
////                        } catch (Exception ex) {
////                            Log.e(getClass().getName(), ex.toString(), ex);
////                        }
////                    }
////                });
////            }
//        }
//    }

//    @Override
//    public final void onFailure(Call<T> call, @NotNull Throwable t) {
//        try {
//            handleFailure(t);
//        } finally {
//            try {
//                pd.hideProgress();
//            } catch (Exception ex) {
//                Log.e(getClass().getName(), ex.toString(), ex);
//            }
//        }
//
//       // throw new RuntimeException(t);
//    }

    public void handleSuccess() {
        handleSuccess();
    }

    public abstract void handleSuccess(Response<T> response) throws IOException;

//    public void handleServerError(ServerError serverError) {
//        String errorMessage = serverError != null ? serverError.getError() : null;
//        errorMessage = errorMessage == null ? serverError.getMessage() : null;
//        Log.e("handleServerError > ", errorMessage);
//        if (interactive) {
//            Toast.makeText(activity,  AppUtils.getErrorMessage(activity,500), Toast.LENGTH_LONG).show();
//
//        }
//    }

//    public void handleFailure(Throwable t) {
//        Log.e("handleFailure > " + t, t.getMessage());
//        if (interactive && !AppUtils.isInternetAvailable(activity)) {
//            Intent intent=  new Intent(activity,NoItemInternetImage.class);
//            activity.startActivity(intent);
////            simpleDialog=new SimpleDialog(activity, activity.getString(R.string.title_internet),
////                    activity.getString(R.string.msg_internet), activity.getString(R.string.button_cancel),
////                    activity.getString(R.string.button_ok), new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    simpleDialog.dismiss();
////                }
////            });
////            simpleDialog.show();
//
//
//        } else if (interactive) {
//            Intent intent=  new Intent(activity,NoItemInternetImage.class);
//            activity.startActivity(intent);
////            simpleDialog=new SimpleDialog(activity, activity.getString(R.string.server_error), activity.getString(R.string.default_error), activity.getString(R.string.button_cancel), activity.getString(R.string.button_ok), new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    simpleDialog.dismiss();
////                }
////            });
////            simpleDialog.show();
//
//        }
//
//    }




//    private ServerError processResponseForServerError(Response<T> response) throws IOException {
//        ServerError serverError = new ServerError();
//        String jsonString = response.errorBody().string();
//        //logger.d("processResponseForServerError > " + jsonString);
//        serverError = new Gson().fromJson(jsonString, ServerError.class);
//        return serverError;
//    }

}
