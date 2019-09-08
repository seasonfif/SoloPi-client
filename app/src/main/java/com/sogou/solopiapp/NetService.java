package com.sogou.solopiapp;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface NetService {

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> map);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url);

}
