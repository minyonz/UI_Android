package com.kh.ui_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tbl_student (";
        sql += "      sno char(8) primary key,";
        sql += "      sname char(10) not null,";
        sql += "      syear int(1) not null,";
        sql += "      gender char(3) not null,";
        sql += "      major char(3) not null,";
        sql += "      score int(3) default 0 not null)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tbl_student");
        // 다시 만들기
        onCreate(db);
    }
}
