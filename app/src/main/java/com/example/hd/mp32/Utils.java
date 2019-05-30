package com.example.hd.mp32;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Song> list;

    public static Song song;
    private static DatabaseHelper dbHelper;


    public static List<Song> getmusic(Context context) {



        dbHelper = DatabaseHelper.getInstance(context);//数据库传递 context

        list = new ArrayList<>();

 //Int size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
        //查询数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from music order by song", null);
        while (cursor.moveToNext()) {

            song = new Song();
            song.song= cursor.getString(cursor.getColumnIndex("song"));
            song.singer= cursor.getString(cursor.getColumnIndex("singer"));
            song.path= cursor.getString(cursor.getColumnIndex("path"));
            song.duration= cursor.getInt(cursor.getColumnIndex("duration"));
            song.size= cursor.getLong(cursor.getColumnIndex("size"));

            //                把歌曲名字和歌手切割开
            if (song.size > 1000 * 800) {
                if (song.song.contains("-")) {
                    String[] str = song.song.split("-");
                    song.singer = str[0];
                    song.song = str[1];
                }
                list.add(song);
            }



        }




        cursor.close();

        return list;


    }


    //    转换歌曲时间的格式
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
            return tt;
        } else {
            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
            return tt;
        }
    }
}
