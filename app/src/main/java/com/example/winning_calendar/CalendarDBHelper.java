package com.example.winning_calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CalendarDBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "calendar.db";
    private static final String TABLE_NAME = "todo";

    final static int blueColor = 0xFF1E90FF;
    final static int yellowColor = 0xFFFFD700;
    final static int redColor = 0xFFFF6347;
    final static int greenColor = 0xFF32CD32;
    final static int orangeColor = 0xFFFFA500;

    public CalendarDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version){
        super(context, DB_NAME, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todo (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title TEXT, content TEXT,color TEXT, startAttr INTEGER, endAttr INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS todo;");
        onCreate(db);
    }

    public ArrayList<DateEvent> Loading() {
        ArrayList<DateEvent> todoEvents = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM todo ORDER BY _id", null);
        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String color = cursor.getString(cursor.getColumnIndex("color"));
                @SuppressLint("Range") String start = cursor.getString(cursor.getColumnIndex("startAttr"));
                @SuppressLint("Range") String end = cursor.getString(cursor.getColumnIndex("endAttr"));

                DateAttr startAttr = new DateAttr(Long.parseLong(start));
                DateAttr endAttr = new DateAttr(Long.parseLong(end));

                int c = 0;
                switch (color){
                    case "blueColor":
                        c = blueColor;
                        break;
                    case "yellowColor":
                        c = yellowColor;
                        break;
                    case "redColor":
                        c = redColor;
                        break;
                    case "greenColor":
                        c = greenColor;
                        break;
                    case "orangeColor":
                        c = orangeColor;
                        break;
                    default:
                        break;
                }

                DateEvent e2 = new DateEvent(title, content, c, startAttr, endAttr);

                todoEvents.add(e2);
            }

        }
        cursor.close();
        return todoEvents;
    }


    public void InsertTodo(String title, String content,String color, long startAttr, long endAttr){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO todo (title, content, color, startAttr, endAttr) VALUES('" + title + "','" + content + "','" +
                color + "','" + startAttr + "','" + endAttr + "');");
        db.close();
    }


    public void deleteTodo(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM todo WHERE title = '" + title + "'");
        db.close();
    }
}
