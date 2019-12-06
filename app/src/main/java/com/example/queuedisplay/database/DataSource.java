package com.example.queuedisplay.database;

import java.text.SimpleDateFormat;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Date;

public class DataSource {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDBHelper;

    //Name of tables
    static final String QUEUE_TABLE = queueTable.KEY_TABLE_NAME;

    public DataSource(Context context)
    {
        this.mContext = context;
        mDBHelper = new DBHelper(mContext);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void open()
    {
        mDatabase= mDBHelper.getWritableDatabase();
    }
    public void close()
    {
        mDBHelper.close();
    }


    //Insert queue
    public boolean insertTicket(String email, String ticketId, String status, String message, String createdDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        //String currentDateAndTime = sdf.format(new Date());

        ContentValues contentValues = new ContentValues();
        contentValues.put(queueTable.KEY_EMAIL, email);
        contentValues.put(queueTable.PK_TICKET_ID, ticketId); // pk - check the other ticketId
        contentValues.put(queueTable.KEY_STATUS, status);
        contentValues.put(queueTable.KEY_MESSAGE, message);
       contentValues.put(queueTable.KEY_CREATED_DATE, createdDate);
       // contentValues.put(queueTable.KEY_CREATED_DATE, currentDateAndTime); //Date created is current date
        mDatabase.insertWithOnConflict(QUEUE_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    //Delete all tickets
    public Integer deleteAllTickets() {

        return mDatabase.delete(QUEUE_TABLE,"1", null);
    }

    //Get all tickets
    public Cursor getAllTickets(String email)
    {
        //Missing ticket id
        return mDatabase.query(QUEUE_TABLE, new String[] {queueTable.KEY_EMAIL, queueTable.KEY_STATUS,queueTable.PK_TICKET_ID
                        ,queueTable.KEY_MESSAGE,queueTable.KEY_CREATED_DATE},null
                , null, null, null, null);
    }
//queueTable.KEY_EMAIL + "=" + email
    //Get message
    public Cursor getMessage(String ticketId) throws SQLException
    {
        Cursor mCursor =
                mDatabase.query(true, queueTable.KEY_TABLE_NAME, new String[] {queueTable.KEY_MESSAGE},
                        queueTable.PK_TICKET_ID + "=" + ticketId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    //Update ticket
    public boolean updateTicket(String email, String ticketId,String status, String message)
    {
        ContentValues args = new ContentValues();
        args.put(queueTable.KEY_EMAIL, email);
        args.put(queueTable.PK_TICKET_ID, ticketId);
        args.put(queueTable.KEY_STATUS, status);
        args.put(queueTable.KEY_MESSAGE, message);
        return mDatabase.update(QUEUE_TABLE, args, queueTable.PK_TICKET_ID + "=" + ticketId, null) > 0;
    }


}
