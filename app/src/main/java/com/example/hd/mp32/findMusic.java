package com.example.hd.mp32;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


import com.example.hd.mp32.bean.DownSong;
import com.example.hd.mp32.util.FindUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;


public class findMusic extends AppCompatActivity  {
    private EditText name;
    private Button find_btn;

    ListView mylist;
    List<DownSong> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);

        name = findViewById(R.id.find_name);
        find_btn = findViewById(R.id.find_btn);

        mylist = findViewById(R.id.findlist);
        list = new ArrayList<>();



        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String name_ = name.getText().toString();
                    if (!name_.isEmpty()) {
                        list = FindUtils.getfindmusic(findMusic.this, name_);
                        FindAdapter findAdapter = new FindAdapter(findMusic.this, list);
                        mylist.setAdapter(findAdapter);
                       // findAdapter.notifyDataSetChanged();
                        //click(name_);
                        //http3();
                        Toast.makeText(findMusic.this, name_, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(findMusic.this, "歌名不能为空", Toast.LENGTH_SHORT).show();

                    }


            }
        });
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击列 响应
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String down_url = list.get(position).down_url;//获得歌曲的下载地址
                System.out.println(down_url);
                String name=list.get(position).song;
                String author=list.get(position).author;
                downloadFile3(name,author,down_url);

                String lrc=list.get(position).lrc;
                System.out.println(lrc);
                downloadFile3_lrc(name,author,lrc);
            }
        });

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




    private void downloadFile3(String name,String author,String down_url){
        //下载路径
         String url =  down_url;
         long startTime = System.currentTimeMillis();
        Log.i("DOWNLOAD","startTime="+startTime);

        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                e.printStackTrace();
                Log.i("DOWNLOAD","download failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    String mSDCardPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/hd_music";

                    File destDir = new File(mSDCardPath);
                    if(!destDir.exists()){
                        destDir.mkdirs();
                    }
                    System.out.println("路径"+mSDCardPath);
                    File dest = new File(mSDCardPath,author+"-"+name+".mp3");

                    System.out.println(dest.getAbsolutePath());
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    if(dest.exists()){
                    Log.i("DOWNLOAD","download success");
                    Log.i("DOWNLOAD","totalTime="+ (System.currentTimeMillis() - startTime));
                    FindUtils.uptosql(findMusic.this,name,author,dest.getAbsolutePath());
                        Intent intent = new Intent(findMusic.this,MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD","download failed");
                } finally {
                    if(bufferedSink != null){
                        bufferedSink.close();
                    }

                }
            }
        });
    }
    private void downloadFile3_lrc(String name,String author,String content){
        String mSDCardPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/hd_music";

        File destDir = new File(mSDCardPath);
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        System.out.println("路径"+mSDCardPath);
        File dest = new File(mSDCardPath,author+"-"+name+".lrc");

        System.out.println(dest.getAbsolutePath());

        try {
            dest.createNewFile();
            FileOutputStream out=new FileOutputStream(dest,true);
            out.write(content.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
