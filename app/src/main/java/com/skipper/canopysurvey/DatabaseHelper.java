package com.skipper.canopysurvey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skipper on 14/05/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "treecover";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_IMAGE = "image";
    public static final String COLUMN_NAME_COVER = "cover";
    public static final String COLUMN_NAME_LAT = "latitude";
    public static final String COLUMN_NAME_LON = "longitude";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseHelper.TABLE_NAME + " ("+
            DatabaseHelper.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            DatabaseHelper.COLUMN_NAME_IMAGE + " BLOB,"+
            DatabaseHelper.COLUMN_NAME_COVER + " REAL," +
            DatabaseHelper.COLUMN_NAME_LAT + " REAL," +
            DatabaseHelper.COLUMN_NAME_LON + " REAL)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseHelper.TABLE_NAME;



        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "treedb";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }


        public void addRecord(CoverRecord coverRecord) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_IMAGE, coverRecord.get_image());
            values.put(COLUMN_NAME_COVER, coverRecord.get_cover());
            values.put(COLUMN_NAME_LAT, coverRecord.get_lat());
            values.put(COLUMN_NAME_LON, coverRecord.get_lng());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }

        public CoverRecord getRecord(int id) {

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_NAME, new String[]{
                            COLUMN_NAME_ID, COLUMN_NAME_IMAGE, COLUMN_NAME_COVER, COLUMN_NAME_LAT, COLUMN_NAME_LON}, COLUMN_NAME_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                CoverRecord coverRecord = new CoverRecord(Integer.parseInt(cursor.getString(0)), cursor.getBlob(1), cursor.getFloat(2), cursor.getDouble(3), cursor.getDouble(4));
                cursor.close();
                return coverRecord;
            } else {

                return null;
            }

        }

        public List<CoverRecord> getList(){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            List<CoverRecord> list = new ArrayList<>();
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    CoverRecord record = new CoverRecord(cursor.getInt(0), cursor.getBlob(1), cursor.getFloat(2), cursor.getDouble(3), cursor.getDouble(4));
                    list.add(record);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return list;
        }

        public int getRecordCount() {
            String countQuery = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        public void deleteRecord(int id){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?",
                    new String[]{ String.valueOf(id)});
            db.close();
        }
    }


