package com.sogou.solopiapp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetManager {


    private static Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.231:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void upload(){

        String path = Environment.getExternalStorageDirectory() + File.separator + "coverage.ec";
        File file = new File(path);
        RequestBody fileOne = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody fileTwo = RequestBody.create(MediaType.parse("image/*"),
                new File(Environment.getExternalStorageDirectory()
                + File.separator + "cleandata.txt"));
        Map<String, RequestBody> map = new HashMap<>();
        //这里的key必须这么写，否则服务端无法识别
        map.put("file\"; filename=\""+ file.getName(), fileOne);
        map.put("file\"; filename=\""+ "2.txt", fileTwo);
        map.put("folderName", RequestBody.create(MediaType.parse("text/plain"), "feed"));

        Call<ResponseBody> call = retrofit.create(NetService.class).uploadFiles(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("onResponse", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onResponse", t.toString());
            }
        });
    }

    public static void download(){
        String url = "http://192.168.31.231:3000/uploads/feed/2.txt";
        Call<ResponseBody> call = retrofit.create(NetService.class).download(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.e("onResponse", response.toString()+ "--" +response.body().contentLength());
                new Thread(){
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "seasonfif/demo.txt");
                        writeFileFromIS(file, response.body().byteStream(), response.body().contentLength());
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    //将输入流写入文件
    private static void writeFileFromIS(File file, InputStream is, long totalLength) {

        //创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte data[] = new byte[512];
            int len;
            while ((len = is.read(data, 0, 512)) != -1) {
                os.write(data, 0, len);
                currentLength += len;
                //计算当前下载进度
                Log.e("progress:", (int) (100 * currentLength / totalLength) +"");
            }

            Log.e("progress:","finish");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
