package com.example.queuedisplay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyQueueTickets.db";
    public static final String TICKETS_TABLE_NAME = "tickets";
    public static final String TICKETS_COLUMN_ID = "id";
    public static final String TICKETS_COLUMN_STATUS = "status";
    public static final String TICKETS_COLUMN_EMAIL = "email";
    public static final String TICKETS_COLUMN_TICKET_ID = "ticket_id";
    public static final String TICKETS_COLUMN_MESSAGE = "message";
    public static final String TICKETS_COLUMN_CREATED_DATE = "created_date";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TICKETS_TABLE_NAME +
                        " (" + TICKETS_COLUMN_TICKET_ID + " integer primary key" +
                        ", " + TICKETS_COLUMN_STATUS + " text " +
                        ", " + TICKETS_COLUMN_EMAIL + " text " +
                        ", " + TICKETS_COLUMN_MESSAGE + " text " +
                        ", " + TICKETS_COLUMN_CREATED_DATE + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TICKETS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTicket(String email, String ticketId, String status, String message, String createdDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TICKETS_COLUMN_EMAIL, email);
        contentValues.put(TICKETS_COLUMN_TICKET_ID, ticketId);
        contentValues.put(TICKETS_COLUMN_STATUS, status);
        contentValues.put(TICKETS_COLUMN_MESSAGE, message);
        contentValues.put(TICKETS_COLUMN_CREATED_DATE, createdDate);
        db.insertWithOnConflict(TICKETS_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public Integer deleteAllTickets() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TICKETS_TABLE_NAME,"1", null);
    }

    public ArrayList<String> getAllTickets() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TICKETS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TICKETS_COLUMN_STATUS)));
            res.moveToNext();
        }
        return array_list;
    }
}
