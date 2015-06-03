package com.mmga.volleytest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GSONRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.XMLRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends Activity {

    private ImageView imageView;

    private NetworkImageView networkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        networkImageView = (NetworkImageView) findViewById(R.id.network_image);
//        useStringRequest();
//        useJsonRequest();
//        useImageRequest();
        useImageLoader();
        useNetworkImageView();
//        useXmlRequest();
        useGsonRequest();
    }

    private void useJsonRequest() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(">>>>>>>>>>>>>>>", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(">>>>>>>>>>>", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void useStringRequest() {
        //获取到一个RequestQueue对象
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
//        创建一个StringRequest对象.需要传入三个参数，
//        第一个参数就是目标服务器的URL地址，
//        第二个参数是服务器响应成功的回调，
//        第三个参数是服务器响应失败的回调。
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(">>>>>>>>>", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(">>>>>>>>>>", error.getMessage(), error);
            }
        });
//        将这个StringRequest对象添加到RequestQueue里面
        mQueue.add(stringRequest);
    }

    public void useImageRequest() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
//        第一个参数就是图片的URL地址
//        第二个参数是图片请求成功的回调，这里我们把返回的Bitmap参数设置到ImageView中
//        第三第四个参数分别用于指定允许图片最大的宽度和高度,0为不限制
//        第五个参数用于指定图片的颜色属性，其中ARGB_8888可以展示最好的颜色属性每个图片像素占据4个字节的大小，
//              RGB_565则表示每个图片像素占据2个字节大小
//        第六个参数是图片请求失败的回调
        ImageRequest imageRequest = new ImageRequest("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",
        new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                imageView.setImageBitmap(response);
            }
        },0,0, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "读取错误", Toast.LENGTH_SHORT).show();
                    }
                });
        mQueue.add(imageRequest);
    }


    public void useImageLoader() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
//        第一个参数就是RequestQueue对象，第二个参数是一个ImageCache对象
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache()
        );
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.abc_btn_default_mtrl_shape, R.drawable.abc_btn_check_material);
        imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener,200,200);


    }

    public void useNetworkImageView() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", imageLoader);
    }

    private void useXmlRequest() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        XMLRequest xmlRequest = new XMLRequest("http://flash.weather.com.cn/wmaps/xml/china.xml",
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)) {
                                            String pName = response.getAttributeValue(0);
                                            Log.d(">>>>>>>", "pName is " + pName);
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }


                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(">>>>>>>>>>>", error.getMessage(), error);
            }
        });
        mQueue.add(xmlRequest);
    }

    private void useGsonRequest() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        GSONRequest<Weather> gsonRequest = new GSONRequest<Weather>("http://www.weather.com.cn/data/sk/101010100.html",
                Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                WeatherInfo weatherInfo = weather.getWeatherinfo();
                Log.d(">>>>>>>", "city is " + weatherInfo.getCity());
                Log.d(">>>>>>>", "temp is " + weatherInfo.getTemp());
                Log.d(">>>>>>>", "time is " + weatherInfo.getTime());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(">>>>>>>>", error.getMessage(), error);
            }
        });
        mQueue.add(gsonRequest);
    }

}
