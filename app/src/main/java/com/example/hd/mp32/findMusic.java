package com.example.hd.mp32;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class findMusic extends AppCompatActivity {
    private EditText name;
    private Button find_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);

        name = findViewById(R.id.find_name);
        find_btn = findViewById(R.id.find_btn);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = name.getText().toString();
                if(!TextUtils.isEmpty(name_)){

                    http3();
                Toast.makeText(findMusic.this,name_,Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(findMusic.this, "歌名不能为空", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    public void http3(){
        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .build();
        RequestBody body = new FormBody.Builder().add("input","123")
                .add("filter","name")
                .add("type","netease")
                .add("page","1").build();
//        Musicpost musicpost = new Musicpost("good","name","netease",1);
//        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
//        Gson gson = new Gson();
//        //使用Gson将对象转换为json字符串
//        String json = gson.toJson(musicpost);
//        System.out.println(json);
//
//        //MediaType  设置Content-Type 标头中包含的媒体类型值
//        RequestBody requestBody = FormBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
//                , json);

        Request request = new Request.Builder()
                .url("http://music.bload.cn/")//请求的url
                .addHeader(    "X-Requested-With","XMLHttpRequest")
                .post(body)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               // System.out.println(response.body().string());
                String json = response.body().string();

                //JSONObject 表示形式 {"key" : "value"}
                //
                //JSONArray 表示形式  [{"key" : "value"},{"key" : "value"},{"key" : "value"}]，JSONArray里面包含多个JSONObject
                //
                //访问时通过 JSONObject对象去访问，使用 jsonObject.getXXX("key")得到相应的值
                //
                //一般解析JSON都使用这两个。
                JSONObject jsonObject = JSONObject.parseObject(json);//{"data":[{"type":"netease","link":"http:\/\/music.163.com\/#\/so
                //System.out.println(jsonObject);
                String data=jsonObject.get("data").toString();//获得data [{"xxx":"xxx"...},{},{}] 转为string

                JSONArray array = JSONArray.parseArray(data);//转为数组
                //System.out.println(array.get(0));//{"xxx":"xxx"...}

                for(int i=0;i<10;i++){

                    JSONObject music = jsonObject.parseObject(array.get(i).toString());
                    System.out.println("author"+music.get("author"));
                }

            }
        });

    }

//    class Musicpost {
//        private String input;
//        private String filter;
//        private String type;
//        private  int page;
//
//        public Musicpost(String input, String filter, String type, int page) {
//            this.input = input;
//            this.filter = filter;
//            this.type = type;
//            this.page = page;
//        }
//
//        public String getInput() {
//            return input;
//        }
//
//        public void setInput(String input) {
//            this.input = input;
//        }
//
//        public String getFilter() {
//            return filter;
//        }
//
//        public void setFilter(String filter) {
//            this.filter = filter;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public int getPage() {
//            return page;
//        }
//
//        public void setPage(int page) {
//            this.page = page;
//        }
//    }




}
