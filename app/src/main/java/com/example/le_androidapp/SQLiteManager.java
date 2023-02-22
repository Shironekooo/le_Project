package com.example.le_androidapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DB_NAME = "UsersDB";
    private static final int DB_VER = 1;
    private static final String TABLE_NAME = "AppUsers";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String USERNAME_FIELD = "username";
    private static final String BADCOUNT_FIELD = "badCount";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(USERNAME_FIELD)
                .append(" TEXT, ")
                .append(BADCOUNT_FIELD)
                .append(" INT, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public void addUserToDb(AppUser appUser) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, appUser.getId());
        contentValues.put(USERNAME_FIELD, appUser.getUsername());
        contentValues.put(BADCOUNT_FIELD, appUser.getBadCount());
        contentValues.put(DELETED_FIELD, getStringFromDate(appUser.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateUserListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(1);
                    String username = result.getString(2);
                    int badCount = result.getInt(3);
                    String stringDeleted = result.getString(4);
                    Date deleted = getDateFromString(stringDeleted);

                    AppUser appUser = new AppUser(id, username, badCount, deleted);
                    AppUser.userArrayList.add(appUser);
                }
            }
        }
    }

    public void updateUserInDb(AppUser appUser) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, appUser.getId());
        contentValues.put(USERNAME_FIELD, appUser.getUsername());
        contentValues.put(BADCOUNT_FIELD, appUser.getBadCount());
        contentValues.put(DELETED_FIELD, getStringFromDate(appUser.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(appUser.getId())});
    }

    private String getStringFromDate(Date date) {
        if (date == null) return null;
        return dateFormat.format(date);
    }

    private Date getDateFromString(String string){
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
