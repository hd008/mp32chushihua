package com.example.hd.mp32.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hd.mp32.bean.Song;
import com.example.hd.mp32.dao.DatabaseHelper;

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
                    System.out.println("str"+str.toString());
                    song.singer = str[0];

                    if(str[1].contains(".")){
                        System.out.println("str1"+str[1].toString());
                        String[] str2=str[1].split("\\.");
                        song.song=str2[0];
                    }
                   else {song.song = str[1];}


                }
                //System.out.println(song.toString());
               // System.out.println(song.path);
               // System.out.println(song.duration);
               // System.out.println(song.size);
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
