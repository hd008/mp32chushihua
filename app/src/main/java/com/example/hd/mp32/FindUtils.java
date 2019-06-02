package com.example.hd.mp32;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindUtils {


    public static List<DownSong> list;

    public static DownSong song;

    private static DatabaseHelper dbHelper;


    public static List<DownSong> getfindmusic(Context context,String name) {

        list = new ArrayList<>();




            OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                    .build();
            RequestBody body = new FormBody.Builder().add("input",name)
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

                     JSONArray a=jsonObject.getJSONArray("data");//1.data [{"xxx":"xxx"...},{},{}]
                    //System.out.println(a.get(1));//{"xxx":"xxx"...}



                    for(int i=0;i<10;i++){

                        JSONObject music = jsonObject.parseObject(a.get(i).toString());//2.{"xxx":"xxx"...}
                        //System.out.println("author"+music.get("author"));

                        song = new DownSong();
                        song.song=music.getString("title");
                        song.author=music.getString("author");
                        song.down_url=music.getString("url");
                        song.lrc=music.getString("lrc");
                       // System.out.println(song.author);
//                        System.out.println(song.down_url);
//                        System.out.println(song.lrc);
                        list.add(song);
                    }

                }
            });




        return list;
    }

    public static void uptosql(Context context,String name,String author,String path) {



        dbHelper = DatabaseHelper.getInstance(context);//数据库传递 context


        //Int size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
        //查询数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String song =name;
//判断是否重复
        Cursor cursor2 = db.rawQuery("select * from music where song=?", new String[]{song});
        if(!cursor2.moveToNext()) {
            String singer = "";
            db.execSQL("insert into music(song,singer,path,duration,size) values(?,?,?,?,?)", new Object[]{song, singer,path,207073,3313702});
            //放入数据库  但时常和大小未知
            //  /storage/emulated/0/hd_music/Marian Hill-Good.mp3
            //   duration 200620
            //  size  3210388
        }

    }


}
