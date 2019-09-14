package com.sogou.solopiapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static void getCases(final NetCallback<List<Case>> netCallback){
        Call<List<Case>> call = retrofit.create(NetService.class).getCases("SogouBrowser");
        call.enqueue(new Callback<List<Case>>() {
            @Override
            public void onResponse(Call<List<Case>> call, Response<List<Case>> response) {
                List<Case> cases = response.body();
                Log.e("getCases", String.valueOf(cases.size()));
                if (netCallback != null){
                    netCallback.onSuccess(cases);
                }
            }

            @Override
            public void onFailure(Call<List<Case>> call, Throwable t) {
                Log.e("getCases", t.toString());
                if (netCallback != null){
                    netCallback.onFailed();
                }
            }
        });
    }

    public static void upload(Context context){

        AssetManager manager = context.getAssets();
        Map<String, RequestBody> map = new HashMap<>();
        try{
            InputStream in1 = manager.open("net_font.json");
            InputStream in2 = manager.open("net_preload.json");
            InputStream in3 = manager.open("reading_mode.json");
            InputStream in4 = manager.open("net_font_a.json");
            InputStream in5 = manager.open("net_preload_a.json");
            InputStream in6 = manager.open("reading_mode_a.json");

            RequestBody body1 = RequestBody.create(MediaType.parse("text/*"), readStream(in1));
            RequestBody body2 = RequestBody.create(MediaType.parse("text/*"), readStream(in2));
            RequestBody body3 = RequestBody.create(MediaType.parse("text/*"), readStream(in3));
            RequestBody body4 = RequestBody.create(MediaType.parse("text/*"), readStream(in4));
            RequestBody body5 = RequestBody.create(MediaType.parse("text/*"), readStream(in5));
            RequestBody body6 = RequestBody.create(MediaType.parse("text/*"), readStream(in6));

            List<TestCase> cases = new ArrayList<>();
            cases.add(new TestCase("net_font.json", "feed", "SogouBrowser", "sogou.mobile.explore"));
            cases.add(new TestCase("net_preload.json", "account", "SogouBrowser", "sogou.mobile.explore"));
            cases.add(new TestCase("reading_mode.json", "browser", "SogouBrowser", "sogou.mobile.explore"));
            cases.add(new TestCase("net_font_a.json", "browser", "SogouBrowser", "sogou.mobile.explore"));
            cases.add(new TestCase("net_preload_a.json", "setting", "Streamline", "sogou.mobile.explore.streamline"));
            cases.add(new TestCase("reading_mode_a.json", "browser", "Streamline", "sogou.mobile.explore.streamline"));

            RequestBody body7 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(cases));

            //这里的key必须这么写，否则服务端无法识别
            map.put("file\"; filename=\""+ "net_font.json", body1);
            map.put("file\"; filename=\""+ "net_preload.json", body2);
            map.put("file\"; filename=\""+ "reading_mode.json", body3);
            map.put("file\"; filename=\""+ "net_font_a.json", body4);
            map.put("file\"; filename=\""+ "net_preload_a.json", body5);
            map.put("file\"; filename=\""+ "reading_mode_a.json", body6);
            map.put("extras", body7);
        }catch (Exception e){

        }

        Call<ResponseBody> call = retrofit.create(NetService.class).uploadFiles(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("upload", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("upload", t.toString());
            }
        });
    }

    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static void download(String rname, final String name){
        Call<ResponseBody> call = retrofit.create(NetService.class).downloadCase(rname);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.e("download", response.toString()+ "--" +response.body().contentLength());
                new Thread(){
                    @Override
                    public void run() {
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "seasonfif/"+name);
                        writeFileFromIS(file, response.body().byteStream(), response.body().contentLength());
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("download", t.toString());
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
