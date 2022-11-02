package com.hp.ali.ecomerceapp.communication.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebAPIResponse<T> {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("response")
    @Expose
    public T response;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("detail")
    @Expose
    public String detail;

    @SerializedName("error")
    @Expose
    public boolean error;

    @SerializedName("data")
    @Expose
    public T data;

}