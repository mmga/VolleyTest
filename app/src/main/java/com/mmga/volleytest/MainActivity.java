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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class MainActivity extends Activity {

    private ImageView imageView;

    private NetworkImageView networkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        networkImageView = (NetworkImageView) findViewById(R.id.network_image);
//        getHttpConnection();
//        jsonRequest();
//        useImageRequest();
        useImageLoader();
        useNetworkImageView();
    }

    private void jsonRequest() {
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


    public void getHttpConnection() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(">>>>>>>>>", response);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(">>>>>>>>>>", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);

    }

    public void useImageRequest() {
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
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

}
