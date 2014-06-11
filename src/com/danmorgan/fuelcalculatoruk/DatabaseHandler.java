package com.danmorgan.fuelcalculatoruk;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version - Will change when upgraded/deleted etc
    private static final int DATABASE_VERSION = 100;
 
    // Database Name
    private static final String DATABASE_NAME = "journeyManager";
 
    // Journey table name
    public static final String TABLE_JOURNEY = "journey";
 
    // Journey Table Columns names
    private static final String KEY_P = "key";
    private static final String KEY_ID = "id";
    private static final String KEY_DIST = "distance";
    private static final String KEY_MPG = "mpg";
    private static final String KEY_COST = "cost";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOURNEY_TABLE = "CREATE TABLE " + TABLE_JOURNEY + "("
                + KEY_P + " INTEGER PRIMARY KEY," + KEY_ID + " TEXT," + KEY_DIST + " TEXT,"
                + KEY_MPG + " TEXT," + KEY_COST + " TEXT )";
        db.execSQL(CREATE_JOURNEY_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEY);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addJourneyData(Journey journey) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        /*All columns apart from primary key */
        ContentValues values = new ContentValues();
        values.put(KEY_ID, journey.getId());
        values.put(KEY_DIST, journey.getDistance()); 
        values.put(KEY_MPG, journey.getMpg()); 
        values.put(KEY_COST, journey.getCost()); 
 
        // Inserting Row
        db.insert(TABLE_JOURNEY, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single journey
    Journey getJourney(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_JOURNEY, new String[] { KEY_P + KEY_ID + 
                KEY_DIST, KEY_MPG, KEY_COST }, KEY_P + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Journey journey = new Journey();
        journey.setPkey(Integer.parseInt(cursor.getString(0)));
        journey.setId(String.valueOf(cursor.getString(1)));
        journey.setMpg(String.valueOf(cursor.getString(2)));
        journey.setDistance(String.valueOf(cursor.getString(3)));
        journey.setCost(String.valueOf(cursor.getString(4)));
        return journey;
    }
     
    // Getting All Contacts
    public List<Journey> getAllJourneys() {
        List<Journey> journeyList = new ArrayList<Journey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNEY + " ORDER BY " + KEY_P + " DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Journey journey = new Journey();
                journey.setPkey(Integer.parseInt(cursor.getString(0)));
                journey.setId(String.valueOf(cursor.getString(1)));
                journey.setMpg(String.valueOf(cursor.getString(2)));
                journey.setDistance(String.valueOf(cursor.getString(3)));
                journey.setCost(String.valueOf(cursor.getString(4)));
                // Adding contact to list
                journeyList.add(journey);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return journeyList;
    } 
}
