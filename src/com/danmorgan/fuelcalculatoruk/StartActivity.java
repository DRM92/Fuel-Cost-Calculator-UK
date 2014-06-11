package com.danmorgan.fuelcalculatoruk;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.danmorgan.fuelcalculatoruk.R;
import com.google.android.gms.ads.*;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.*;

public class StartActivity extends Activity {
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		/* Mobile ads setup */
		// Create the adView.
	    adView = new AdView(this);
	    adView.setAdUnitId("ca-app-pub-5201917292617779/8634852091");
	    adView.setAdSize(AdSize.BANNER);
	   
		//Set the view to RelativeLayout
	    RelativeLayout ll = (RelativeLayout) findViewById(R.id.adView);
		ll.addView(adView);
		
		//Developer phone to ignore miss-clicks of Ads
		AdRequest adRequest = new AdRequest.Builder().
				addTestDevice("").build();
		adView.loadAd(adRequest);
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	public void mpgPage(View view) {
        Intent mpgPage = new Intent(StartActivity.this, Mpg_Calculation.class);
        startActivity(mpgPage);
    }
		
	public void historyPage(View view) {
        Intent logPage = new Intent(StartActivity.this, DataListView.class);
        startActivity(logPage);
    }
	
	public void journeyPage(View view) {
        Intent journeyPage = new Intent(StartActivity.this, Journey_Calculator.class);
        startActivity(journeyPage);
    }

}
