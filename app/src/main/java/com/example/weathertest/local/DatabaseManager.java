package com.example.weathertest.local;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weathertest.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void openDb(){
        db = helper.getWritableDatabase();
    }

    public void insertIntoDb(String city){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CITY, city);
        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public List<String> getFromDb(){
        List<String> tempList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, null, null, null, null,null,null);

        while(cursor.moveToNext()){
            @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex(Constants.CITY));
            tempList.add(city);
        }

        cursor.close();
        return tempList;
    }

    public void closeDb(){
        helper.close();
    }
}
