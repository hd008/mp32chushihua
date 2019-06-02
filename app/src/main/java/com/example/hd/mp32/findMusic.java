package com.example.hd.mp32;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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


public class findMusic extends AppCompatActivity  {
    private EditText name;
    private Button find_btn;

    ListView mylist;
    List<DownSong> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);

        name = findViewById(R.id.find_name);
        find_btn = findViewById(R.id.find_btn);

        mylist = findViewById(R.id.findlist);
        list = new ArrayList<>();

        list = FindUtils.getfindmusic(findMusic.this);
        FindAdapter findAdapter= new FindAdapter(this,list);
        mylist.setAdapter(findAdapter);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = name.getText().toString();
                if(!TextUtils.isEmpty(name_)){
                    click();
                    //http3();
                    Toast.makeText(findMusic.this,name_,Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(findMusic.this, "歌名不能为空", Toast.LENGTH_SHORT).show();

                }

            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击列 响应
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p = list.get(position).down_url;//获得歌曲的下载地址
                System.out.println(p);
            }
        });

    }
    public void click(){//点击fid 响应
        list = FindUtils.getfindmusic(findMusic.this);
        FindAdapter findAdapter= new FindAdapter(findMusic.this,list);
        mylist.setAdapter(findAdapter);


    }
    class FindAdapter extends BaseAdapter{

        Context context;
        List<DownSong> list;

        public FindAdapter(findMusic findMusic, List<DownSong> list) {
            this.context = findMusic;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            Findholder findholder;

            if (view == null) {
                findholder = new Findholder();
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.findtext, null);

                findholder.f_position = view.findViewById(R.id.f_position);
                findholder.f_song = view.findViewById(R.id.f_song);
                findholder.f_author = view.findViewById(R.id.f_author);

                view.setTag(findholder);//在list中itemView相同的情况下，我们只进行了一次的控件资源绑定
                //在第一次创建itemView的时候，完成对控件的绑定，同时吧控件作为一个object--holder，把它通过setTag()存到itemView中，
                //再第二次使用的时候就可以通过getTag()把holder取出来直接使用
                //文艺式：这种写法非常好，即利用了listView的缓存机制，又避免了重复的findViewById
                //
                //1、创建内部类 2、判断convertView是否为空 3通过setTag方法将viewHolder与convertView建立关系绑定

            } else {
                findholder = (Findholder) view.getTag();
            }
            //开头的序号
            findholder.f_position.setText(position + 1 + "");
            findholder.f_song.setText(list.get(position).song.toString());
            findholder.f_author.setText(list.get(position).author.toString());


            return view;
        }

        class Findholder {
            TextView f_position, f_song, f_author;
        }
    }

//public void http3(){
//    OkHttpClient okHttpClient  = new OkHttpClient.Builder()
//            .build();
//    RequestBody body = new FormBody.Builder().add("input","123")
//            .add("filter","name")
//            .add("type","netease")
//            .add("page","1").build();
////        Musicpost musicpost = new Musicpost("good","name","netease",1);
////        //使用Gson 添加 依赖 compile 'com.google.code.gson:gson:2.8.1'
////        Gson gson = new Gson();
////        //使用Gson将对象转换为json字符串
////        String json = gson.toJson(musicpost);
////        System.out.println(json);
////
////        //MediaType  设置Content-Type 标头中包含的媒体类型值
////        RequestBody requestBody = FormBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")
////                , json);
//
//    Request request = new Request.Builder()
//            .url("http://music.bload.cn/")//请求的url
//            .addHeader(    "X-Requested-With","XMLHttpRequest")
//            .post(body)
//            .build();
//
//    //创建/Call
//    Call call = okHttpClient.newCall(request);
//    //加入队列 异步操作
//    call.enqueue(new Callback() {
//        //请求错误回调方法
//        @Override
//        public void onFailure(Call call, IOException e) {
//            System.out.println("连接失败");
//        }
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            // System.out.println(response.body().string());
//            String json = response.body().string();
//
//            //JSONObject 表示形式 {"key" : "value"}
//            //
//            //JSONArray 表示形式  [{"key" : "value"},{"key" : "value"},{"key" : "value"}]，JSONArray里面包含多个JSONObject
//            //
//            //访问时通过 JSONObject对象去访问，使用 jsonObject.getXXX("key")得到相应的值
//            //
//            //一般解析JSON都使用这两个。
//            JSONObject jsonObject = JSONObject.parseObject(json);//{"data":[{"type":"netease","link":"http:\/\/music.163.com\/#\/so
//
//            //System.out.println(jsonObject);
//
////                     String a=jsonObject.getJSONArray("data").get(1).toString();
////                    System.out.println(a);
//
//            String data=jsonObject.get("data").toString();//获得data [{"xxx":"xxx"...},{},{}] 转为string
//
//            JSONArray array = JSONArray.parseArray(data);//转为数组
//            System.out.println(array.get(0));//{"xxx":"xxx"...}
//
////                    for(int i=0;i<10;i++){
////
////                        JSONObject music = jsonObject.parseObject(array.get(i).toString());
////                        System.out.println("author"+music.get("author"));
////
////                        song = new DownSong();
////                        song.author=music.get("author").toString();
////                        song.down_url=music.getString("")
////                    }
//
//        }
//    });
//
//}





}
