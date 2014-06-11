package com.danmorgan.fuelcalculatoruk;

import java.text.DecimalFormat;

import com.danmorgan.fuelcalculatoruk.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Journey_Calculator extends Activity {
	private AdView adView;
	private EditText distance, mpg, price;
	private ImageButton buttonCalc;
	private final double litreGallon = 4.54609;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journey__calculator);
		
		/* Setting the text boxes and buttons to variables */ 
		distance = (EditText)findViewById(R.id.distance);
		mpg = (EditText)findViewById(R.id.mpg);
		price = (EditText)findViewById(R.id.price);
		buttonCalc = (ImageButton) findViewById(R.id.btn_calc);
		
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
		
		/* Calls initButton, which when clicked will carry out calculation */
		initButton();
		
	}
	
	private void initButton() {
        buttonCalc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	/* After button clicked, hides the keyboards */
            	InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE); 
            	inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
            	
            	/* Seting vairables as the strings from textbox */
                String sDistance = distance.getText().toString();
                String sMpg = mpg.getText().toString();
                String sPrice = price.getText().toString();
                
                /* Allows vairables to be set to 2 decimal places i.e. price */
                DecimalFormat twoDigit = new DecimalFormat("#,##0.00");
                
                /* Getting vale from Strings */
                String distanceLabel = String.valueOf(sDistance);
                String mpgLabel = String.valueOf(sMpg);
                TextView lblResult = (TextView) findViewById(R.id.lbl_result);	
                
                
                /* Validations - ensure text boxes arent blank */
                if(sDistance.equals("")){
                    Toast t =Toast.makeText(Journey_Calculator.this, "Please enter a distance to continue", Toast.LENGTH_LONG);
                    t.show(); 
                } else if (sMpg.equals("")){
                    Toast t =Toast.makeText(Journey_Calculator.this, "Please enter the fuel consumption (MPG) to continue", Toast.LENGTH_LONG);
                    t.show(); 
                } else if (sPrice.equals("")) {
                	 Toast t =Toast.makeText(Journey_Calculator.this, "Please enter the price per litre to continue", Toast.LENGTH_LONG);
                     t.show();
                } else {
	                setMainInvisible();  
	                setVisible();
	                
	                /* Once the text boxes have at minimum 1 entry */
		            if(sMpg.length()>=1 && sDistance.length()>=1){	  
		            	/* Passing the string values to the method */
				        Double journey = journeyCalc(sDistance, sMpg, sPrice);              
				        Double PPL = journey / pricePerLitre(sDistance);	               	                	               
				        String pplAnswer = String.valueOf(twoDigit.format(PPL));	 
				        String journeyAnswer = String.valueOf(twoDigit.format(journey));
				        
				        /* Answer in a paragraph - using html formatting for appearance purposes */
				        String finalAnswer = "On the journey of " + distanceLabel + " miles and a fuel consumption of " +
				           		"<font color='white'> <b>" + mpgLabel + " MPG,</b></font>" + " the journey " +
				        		"will approxmiately cost" + "<font color='white'> <b>" +"£" + journeyAnswer + "</b></font> in fuel," +
				           		" which is" + "<font color='white'> <b>" + "£"+ pplAnswer + "</b></font>" + " each mile." ;
				        lblResult.setText(Html.fromHtml(finalAnswer));  
		            }
                }	          
            }
        });
    }

	/* Method to calculate journey costs - parsing vairables as doubles and returning a double */
	private double journeyCalc(String a, String b, String c){
        double dblDist = Double.parseDouble(a);
        double dblMpg = Double.parseDouble(b);
        double dblPrice = Double.parseDouble(c);
        double dblPricePerGallon = dblPrice * litreGallon;
        double gallonsUsed = dblDist / dblMpg;    	
		return (gallonsUsed * dblPricePerGallon)/100; 
	}
	
	/* Parsing in value of price per litre and returning a double */
	private double pricePerLitre(String a){
		double dblDist = Double.parseDouble(a);
		return dblDist;
	}
	
	/* Hiding the widgets used for input */
	private void setMainInvisible(){
		TextView journeyIntro = (TextView) findViewById(R.id.journeyIntro);
		journeyIntro.setVisibility(View.GONE);
		TextView distLabel = (TextView) findViewById(R.id.distance_label);
		distLabel.setVisibility(View.GONE);
		TextView mpgLabel = (TextView) findViewById(R.id.mpg_label);
		mpgLabel.setVisibility(View.GONE);
		TextView priceLabel = (TextView) findViewById(R.id.price_label);
		priceLabel.setVisibility(View.GONE);
		distance.setVisibility(View.GONE);
		mpg.setVisibility(View.GONE);
		price.setVisibility(View.GONE);  
		buttonCalc.setVisibility(View.GONE);
	}
	
	/* Making new buttons visible after calculation */
	private void setVisible(){
		ImageButton btn_recalc = (ImageButton) findViewById(R.id.btn_recalc);
		btn_recalc.setVisibility(View.VISIBLE);
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
		btn_home.setVisibility(View.VISIBLE);

	}
	
	public void Recalculate(View view) {
        Intent recalc = new Intent(Journey_Calculator.this, Journey_Calculator.class);
        startActivity(recalc);
    }
	
	public void openHome(View view) {
        Intent home = new Intent(Journey_Calculator.this, StartActivity.class);
        startActivity(home);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.journey__calculator, menu);
		return true;
	}

}
