package com.danmorgan.fuelcalculatoruk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.danmorgan.fuelcalculatoruk.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DataListView extends ListActivity {
	
    private ArrayList<String> journey = new ArrayList<String>();
    private String tableName = DatabaseHandler.TABLE_JOURNEY;
    private SQLiteDatabase newDB;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_list_view);
		
		/* Calls the methods which display the database in a list view */
        openDatabase();        
        displayResultList();
	}
	
	

	public void displayResultList() {	
		
		/* Title of the listview */
        TextView titleView = new TextView(this);
        getListView().addHeaderView(titleView);
        
        /* Setting the adapter - Using custom adapter to change appearance of text */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DataListView.this, R.layout.custom_text, journey);
        getListView().setAdapter(adapter);
         
        /**
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, journey));
        getListView().setTextFilterEnabled(true);
		**/
	}

	public void openDatabase() {		
		try {
            DatabaseHandler dbHelper = new DatabaseHandler(this.getApplicationContext());
            newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery("SELECT key, id, mpg, distance, cost FROM " +
                    tableName + " ORDER BY key DESC", null);
 
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                    	int pkey = c.getInt(c.getColumnIndex("key"));
                        String mpg = c.getString(c.getColumnIndex("mpg"));
                        String distance = c.getString(c.getColumnIndex("distance"));
                        String cost = c.getString(c.getColumnIndex("cost"));
                        String id = c.getString(c.getColumnIndex("id"));
                        
                        journey.add("\n" + "   ("+ pkey + ")  " +id + "\n" + "   Distance travelled =  " + distance +  
                        		" Miles" + "\n   Fuel consumption = " + mpg +" MPG " + "\n   Journey Cost = £" + cost + "\n");
                    }while (c.moveToNext());
                } 
            }           
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {
            //if (newDB != null) 
               // newDB.execSQL("DELETE FROM " + tableName);
                //newDB.close();
        } 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.data_list_view, menu);
		return true;
	}

}
