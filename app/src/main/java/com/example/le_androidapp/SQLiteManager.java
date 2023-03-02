package com.example.le_androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;

    private static final String DB_NAME = "UsersDB";
    private static final int DB_VER = 1;
    private static final String TABLE_NAME = "AppUsers";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String USERNAME_FIELD = "username";
    private static final String BADCOUNT_FIELD = "badCount";
    private static final String SELECTED_FIELD = "selected";

    private final Context fContext;

    public SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VER);
        fContext = context;
    }

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Database Creation
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ").append(TABLE_NAME).append("(")
                .append(COUNTER).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD).append(" INT, ")
                .append(USERNAME_FIELD).append(" TEXT, ")
                .append(BADCOUNT_FIELD).append(" INT, ")
                .append(SELECTED_FIELD).append(" INT)");

        sqLiteDatabase.execSQL(sql.toString());

        // Database Value Initialization
        final int STARTING_COUNT = 0;
        ContentValues values = new ContentValues();
        Resources resources =   fContext.getResources();
        String[] initArray = resources.getStringArray(R.array.name_array);
        int id_count = 0;
        int select = 1;
        for (String item : initArray) {
            values.put(ID_FIELD, id_count);
            id_count++;
            values.put(USERNAME_FIELD, item);
            values.put(BADCOUNT_FIELD, STARTING_COUNT);
            values.put(SELECTED_FIELD, select);
            select--;
            sqLiteDatabase.insert(TABLE_NAME, null, values);
        }
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
        contentValues.put(SELECTED_FIELD, appUser.getSelected());

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
                    int selected = result.getInt(4);

                    AppUser appUser = new AppUser(id, username, badCount, selected);
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
        contentValues.put(SELECTED_FIELD, appUser.getSelected());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(appUser.getId())});
    }

}
