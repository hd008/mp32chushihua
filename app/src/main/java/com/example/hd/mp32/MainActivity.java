package com.example.hd.mp32;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;

import com.example.hd.mp32.bean.Song;
import com.example.hd.mp32.dao.DatabaseHelper;
import com.example.hd.mp32.util.Utils;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    public MediaPlayer mediaPlayer = new MediaPlayer();

    ListView mylist;
    List<Song> list;
    int count;

    private DatabaseHelper dbHelper;

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
        setContentView(R.layout.activity_main);




        mylist = (ListView) findViewById(R.id.mylist);

        list = new ArrayList<>();

        list = Utils.getmusic(this);

        count=list.size();

        MyAdapter myAdapter = new MyAdapter(this, list);//list 音乐信息

        System.out.println(list);

        mylist.setAdapter(myAdapter);

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String p = list.get(i).path;//获得歌曲的地址
                play(p);
                String author = list.get(i).singer;
                String name = list.get(i).song;
                Intent intent = new Intent(MainActivity.this,play.class);
                intent.putExtra("name",name);
                intent.putExtra("singer",author);
                startActivity(intent);
            }
        });



        Button more=findViewById(R.id.more);
        Button next=(Button)findViewById(R.id.btnnext);

        more.setOnClickListener(this);
        next.setOnClickListener(this);

    }


    class MyAdapter extends BaseAdapter {

        Context context;
        List<Song> list;

        public MyAdapter(MainActivity mainActivity, List<Song> list) {
            this.context = mainActivity;
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Myholder myholder;

            if (view == null) {
                myholder = new Myholder();
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.text, null);

                myholder.t_position = view.findViewById(R.id.t_postion);
                myholder.t_song = view.findViewById(R.id.t_song);
                myholder.t_singer = view.findViewById(R.id.t_singer);
                myholder.t_duration = view.findViewById(R.id.t_duration);

                view.setTag(myholder);//在list中itemView相同的情况下，我们只进行了一次的控件资源绑定
                //在第一次创建itemView的时候，完成对控件的绑定，同时吧控件作为一个object--holder，把它通过setTag()存到itemView中，
                //再第二次使用的时候就可以通过getTag()把holder取出来直接使用
                //文艺式：这种写法非常好，即利用了listView的缓存机制，又避免了重复的findViewById
                //
                //1、创建内部类 2、判断convertView是否为空 3通过setTag方法将viewHolder与convertView建立关系绑定

            } else {
                myholder = (Myholder) view.getTag();
            }
            //开头的序号
            myholder.t_position.setText(i + 1 + "");

            myholder.t_song.setText(list.get(i).song.toString());
            myholder.t_singer.setText(list.get(i).singer.toString());
            String time = Utils.formatTime(list.get(i).duration);

            myholder.t_duration.setText(time);




            return view;
        }


        class Myholder {
            TextView t_position, t_song, t_singer, t_duration;
        }


    }

    //点击文件即播放
    public void play(String path) {


        try {
            //        重置音频文件，防止多次点击会报错
            mediaPlayer.reset();
//        调用方法传进播放地址
            mediaPlayer.setDataSource(path);
           // 异步准备资源，防止卡顿
            mediaPlayer.prepareAsync();
//            调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onClick(View v) {

        dbHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //根据响应Click的按钮id进行选择操作
        switch(v.getId()){

            case R.id.btnnext:
                Random random = new Random();
                int i =random.nextInt(count);//随机数 少于list.size
                String p = list.get(i).path;//获得歌曲的地址
                play(p);
                String author = list.get(i).singer;
                String name = list.get(i).song;
                Intent intent1 = new Intent(MainActivity.this,play.class);
                intent1.putExtra("name",name);
                intent1.putExtra("singer",author);
                startActivity(intent1);
                break;
            case R.id.more:
                Intent intent2 =new Intent(this,findMusic.class);
                startActivity(intent2);

            default:
                break;
        }
    }

}
