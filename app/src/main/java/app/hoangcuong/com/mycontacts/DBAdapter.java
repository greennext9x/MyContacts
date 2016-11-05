package app.hoangcuong.com.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jarvis on 10/24/2016.
 */

public class DBAdapter {
    static final String KEY_ID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_PHONE = "phone";
    static final String KEY_EMAIL = "email";
    static final String KEY_ADDRESS = "address";
    static final String KEY_NOTE = "note";
    static final String KEY_IMAGE = "image";
    static final String KEY_TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyContact.sqlite";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS contacts(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " name TEXT, phone TEXT, email TEXT, address TEXT, note TEXT, image BLOB);";
    Context context;
    DataBaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DataBaseHelper(context);

    }
    private static class DataBaseHelper extends SQLiteOpenHelper{
        DataBaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(DATABASE_CREATE);
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(KEY_TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //opens the database
    public DBAdapter open() throws SQLiteException{
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //Closes the database
    public void close(){
        DBHelper.close();
    }
    //---insert a contact into the database---
    public long insertContact(String name, String phone, String email, String address, String note, byte[] image){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_ADDRESS, address);
        contentValues.put(KEY_NOTE, note);
        contentValues.put(KEY_IMAGE, image);
        return db.insert(DATABASE_TABLE, null,contentValues);
    }
    //---deletes a particular contact---
    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
    }
    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME,KEY_PHONE,
                KEY_EMAIL,KEY_ADDRESS,KEY_NOTE,KEY_IMAGE}, null, null, null, null, null);
    }
    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ID,
                                KEY_NAME, KEY_EMAIL}, KEY_ID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean upcdateContat(long rowId, String name, String phone,String email, String address, String note, byte[] image)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_PHONE, phone);
        args.put(KEY_EMAIL, email);
        args.put(KEY_ADDRESS, address);
        args.put(KEY_NOTE, note);
        args.put(KEY_IMAGE, image);
        return db.update(DATABASE_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
    }
}
