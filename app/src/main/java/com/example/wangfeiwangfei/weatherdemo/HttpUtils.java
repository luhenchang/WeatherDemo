package com.example.wangfeiwangfei.weatherdemo;

import android.content.IntentSender;
import android.os.Handler;
import android.util.Log;
import com.google.gson.Gson;
import okhttp3.*;
import okio.BufferedSink;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.net.Uri.encode;

public class HttpUtils{
    private static WhearBean whearBean1=new WhearBean();
    private static Handler mHandler;
    HttpUtils(){
        mHandler=new Handler();
    }
    public void doGet(final sendDataListenner sendDataListenner){
        /*
        需要公司账号注册。后期需要续费获取。
        阿里云墨迹天气获取接口和开发文档：
        https://market.aliyun.com/products/56928004/cmapi014123.html?spm=5176.730005.productlist.d_cmapi014123.20093524AErp7Q&innerSource=search_空气质量#sku=yuncode812300000
        String host = "https://saweather.market.alicloudapi.com";
	    String path = "/spot-to-weather";
	    String method = "GET";
	    String appcode = "你自己的AppCode";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("area", "泰山");
	    querys.put("need3HourForcast", "0");
	    querys.put("needAlarm", "0");
	    querys.put("needHourData", "0");
	    querys.put("needIndex", "0");
	    querys.put("needMoreDay", "0");
        * */

        Map<String, Object> querys = new HashMap<String, Object>();
        querys.put("area", "天津高西青区");
        querys.put("need3HourForcast", "0");
        querys.put("needAlarm", "0");
        querys.put("needHourData", "0");
        querys.put("needIndex", "0");
        querys.put("needMoreDay", "0");
        //String urls="http://saweather.market.alicloudapi.com/spot-to-weather?area=天津市滨江道&need3HourForcast=0&needAlarm=0&needHourData=0&needIndex=0&needMoreDay=0";
        String urls=appendParamers("http://saweather.market.alicloudapi.com/spot-to-weather",querys);
       OkHttpClient mOkhttClient = new OkHttpClient().newBuilder().
                connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                /*.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())*/.build();
        HashMap<String,String>headermap=new HashMap<>();
        String appcode = "1519afd312744f3eb67c33963be6fd84";
        headermap.put("Authorization","APPCODE " + appcode);
                Headers headers=Headers.of(headermap);


        final Request request = new Request.Builder()
                .get()
                .url(urls)
                .addHeader("Authorization","APPCODE " + appcode)
                .build();
        mOkhttClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson=new Gson();
                     whearBean1= gson.fromJson(response.body().string(),WhearBean.class);
                   // Log.e("result??",response.body().string().toString());
                    Log.e("天津市的天气状况",whearBean1.getShowapi_res_body().getCityInfo().getC1()+whearBean1.getShowapi_res_body().getF1().getDay_weather()
                    +whearBean1.getShowapi_res_body().getF1().getNight_air_temperature()+"度---"+whearBean1.getShowapi_res_body().getF1().getDay_air_temperature()+"度"
                    +",白天风度："+whearBean1.getShowapi_res_body().getF1().getDay_wind_power());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            sendDataListenner.sendDataListenner("天气预告："+whearBean1.getShowapi_res_body().getTime()+whearBean1.getShowapi_res_body().getF1().getDay_weather()
                                    +whearBean1.getShowapi_res_body().getF1().getNight_air_temperature()+"度---"+whearBean1.getShowapi_res_body().getF1().getDay_air_temperature()+"度"
                                    +",白天风度："+whearBean1.getShowapi_res_body().getF1().getDay_wind_power());
                        }
                    });
                } else {

                }
            }
        });
    }
    private static RequestBody appendBody(Map<String, String> paramers) {
        FormBody.Builder body = new FormBody.Builder();
        if (paramers == null || paramers.isEmpty()) {
            return body.build();
        }
        for (Map.Entry<String, String> entry : paramers.entrySet()) {
            body.add(entry.getKey(), entry.getValue().toString());
        }
        return body.build();
    }
    static String appendParamers(String url, Map<String, Object> paramers) {
        if (paramers == null && paramers.isEmpty()) {
            return url;
        }
        StringBuilder urlBuider = new StringBuilder(url);

        if (urlBuider.indexOf("?") <= 0) {
            urlBuider.append("?");
        } else {
            if (!urlBuider.toString().endsWith("?")) {
                urlBuider.append("&");
            }
        }
        int i = 0;
        for (Map.Entry<String, Object> entry : paramers.entrySet()) {
            i++;
            if (paramers.size() > i) {
                urlBuider.append(entry.getKey()).append("=").append(encode(entry.getValue().toString())).append("&");
            } else if (paramers.size() == i) {
                urlBuider.append(entry.getKey()).append("=").append(encode(entry.getValue().toString()));
            }
        }
        return urlBuider.toString();
    }
}