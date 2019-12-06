package com.example.queuedisplay.database;

public class queueTable {
    public static final String KEY_TABLE_NAME = "tickets";
   // public static final String PK_ID = "id"; Duplicate ?
    public static final String KEY_STATUS = "status";
    public static final String KEY_EMAIL = "email";
    public static final String PK_TICKET_ID = "ticket_id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CREATED_DATE = "created_date";

    public static final String DATABASE_CREATE_STUDENTS = "create table if not exists " + KEY_TABLE_NAME +
            "(" + PK_TICKET_ID + " integer primary key not null" +
            ", " + KEY_STATUS + " text " +
            ", " + KEY_EMAIL + " text " +
            ", " + KEY_MESSAGE + " text " +
            ", " + KEY_CREATED_DATE + " text)";
}
