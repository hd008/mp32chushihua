package com.example.hd.mp32;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hd.mp32.dao.DatabaseHelper;


public class start extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取权限
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
        setContentView(R.layout.activity_start);

        //依靠DatabaseHelper的构造函数创建数据库
        //DatabaseHelper dbHelper = new DatabaseHelper(this, "music_db",null,1);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper =  DatabaseHelper.getInstance(this);//用getinstance（自己创建的） 初始化数据库 创表
        SQLiteDatabase db = dbHelper.getWritableDatabase();

//调用本地媒体接口
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        //MediaStore.Audio.Media._ID：歌曲ID
        //Int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        //
        //MediaStore.Audio.Media.TITLE：歌曲的名称
        //String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        //
        //MediaStore.Audio.Media.ALBUM ：歌曲的专辑名
        //String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        //
        //
        //MediaStore.Audio.Media.ARTIST：歌曲的歌手名
        //String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        //
        //MediaStore.Audio.Media.DATA：歌曲文件的路径
        //String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        //
        //MediaStore.Audio.Media.DURATION：歌曲的总播放时长
        //Int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        //
        //MediaStore.Audio.Media.SIZE： 歌曲文件的大小
        //Int size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//判断是否重复
                Cursor cursor1 = db.rawQuery("select * from music where song=?", new String[]{song});
                if(!cursor1.moveToNext()) {
                    String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                    if(size> 1000 * 800) {
                        db.execSQL("insert into music(song,singer,path,duration,size) values(?,?,?,?,?)", new Object[]{song, singer, path, duration, size,});
                    }
                }
            }
        }

        cursor.close();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
