package com.example.hd.mp32.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
        private static DatabaseHelper instance;

        //带全部参数的构造函数，此构造函数必不可少
    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        String sql = "create table music(song varchar(20),singer varchar(20),path varchar(20),duration int(20),size float)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
//
    public static DatabaseHelper getInstance(Context context) {
            if (instance == null) {
                instance = new DatabaseHelper(context, "music.db", null, 2);
            }
            return instance;//保持该数据库

        }

}