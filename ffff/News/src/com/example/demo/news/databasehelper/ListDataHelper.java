package com.example.demo.news.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 123456 on 2015/8/28.
 */
public class ListDataHelper extends SQLiteOpenHelper {

//准备存储各个栏目的json数据。
    public ListDataHelper(Context context) {
        super(context, "listData", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE listData(" +
                "json TEXT DEFAULT NULL," +
                " name TEXT DEFAULT NULL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
