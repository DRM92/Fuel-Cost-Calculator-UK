package com.danmorgan.fuelcalculatoruk;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Mpg_Calculation extends Activity {
	private AdView adView;
	private EditText distance, litres, price;
	private ImageButton buttonCalc;
	private final double gallonLitre = 0.219969;
	private final double litreGallon = 4.54609;
    private String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mpg__calculation);	
		
		/* Setting the text boxes and buttons to variables */ 
		distance = (EditText)findViewById(R.id.distance);
		litres = (EditText)findViewById(R.id.litres);
		price = (EditText)findViewById(R.id.price);
		buttonCalc = (ImageButton) findViewById(R.id.btn_calc);	
		
		/* Calls initButton, which when clicked will carry out calculation */
		initButton();
		
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
	
	
	private void initButton() {
        buttonCalc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	/* After button clicked, hides the keyboards */
            	InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE); 
            	inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                           InputMethodManager.HIDE_NOT_ALWAYS);
            	
            	/* Instance of the database in order to use its methods */
            	DatabaseHandler db = new DatabaseHandler(Mpg_Calculation.this);
            	
            	/*Retrieving strings from text boxes */
            	String sDistance = distance.getText().toString();
                String sLitres = litres.getText().toString();
                String sPrice = price.getText().toString();
                
                /* One decimal place for MPG and two for price */
                DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
                DecimalFormat twoDigit = new DecimalFormat("#,##0.00");
                
                String distanceLabel = String.valueOf(sDistance);
                TextView lblResult = (TextView) findViewById(R.id.lbl_result);	
                
                /* Validations - ensure text boxes arent blank */
                if(sDistance.equals("")){
                    Toast t =Toast.makeText(Mpg_Calculation.this, "Please enter a distance to continue", Toast.LENGTH_LONG);
                    t.show(); 
                } else if (sLitres.equals("")){
                    Toast t =Toast.makeText(Mpg_Calculation.this, "Please enter the number of litres to continue", Toast.LENGTH_LONG);
                    t.show(); 
                } else {       
                    setMainInvisible();  
                    setVisible();
                    
                    /* Price is optional - therefore calculation can be carried out without price */
	                if(sLitres.length()>=1 && sDistance.length()>=1){
		                if(sPrice.equals("")){	                
			                Double MPG = mpgCalc(sDistance, sLitres);              
			                String mpgAnswer = String.valueOf(oneDigit.format(MPG));
			                String finalAnswer = "On the journey of " + distanceLabel + " miles, you achieved " +
			                		"<font color='white'> <b>" + mpgAnswer + " MPG.</b></font>";
			                lblResult.setText(Html.fromHtml(finalAnswer));
			                String pplAnswer = "N/A";
			                
			                /* Adding the calculation result into the database */
			                db.addJourneyData(new Journey(1,timeStamp, distanceLabel, mpgAnswer, pplAnswer));
		                
			                /* Else statement which will run if all fields are filled out */
		                } else {
			                Double MPG = mpgCalc(sDistance, sLitres);              
			                Double PPL = priceJourney(sDistance, sLitres, sPrice);	               
			                String mpgAnswer = String.valueOf(oneDigit.format(MPG));	                	               
			                String pplAnswer = String.valueOf(twoDigit.format(PPL));	                
			                String finalAnswer = "On the journey of " + distanceLabel + " miles, you achieved " +
			                		"<font color='white'> <b>" + mpgAnswer + " MPG.</b></font>" + "<br /><br />Therefore, this journey " +
			                				"has cost you" + "<font color='white'> <b>" +"£" + pplAnswer + "</b></font>" +" in fuel." ;
			                lblResult.setText(Html.fromHtml(finalAnswer)); 
			                
			                /* Adding the calculation result into the database */
			                db.addJourneyData(new Journey(1,timeStamp, distanceLabel, mpgAnswer, pplAnswer));
		                }
	                }
                }
            }
        });
    }
	
	/* MPG calculation which takes in the distance and litres as Strings */
    private double mpgCalc(String a, String b) {        
        double dblDist = Double.parseDouble(a);
        double dblLitres = Double.parseDouble(b);
        return dblDist / gallonToLitres(dblLitres);
    }
    
    /* Calculates the cost of the journey taking in all strings */
    public double priceJourney(String a, String b, String c){
        double dblDist = Double.parseDouble(a);
        double dblLitres = Double.parseDouble(b);
        double dbPrice = Double.parseDouble(c);        
        double dbPricePerGallon = dbPrice * litreGallon;
        double gallonsUsed = dblDist / (dblDist/gallonToLitres(dblLitres));    	
		return (gallonsUsed * dbPricePerGallon)/100;    	
    }
    
    /* Conversion from gallons to litres (required for mpg) */
	public double gallonToLitres(double dbLitres){		
		dbLitres = gallonLitre * dbLitres;		
		return dbLitres;		
	}
    
	/* Hiding the widgets used for input */
    private void setMainInvisible(){
    	TextView mpgIntro = (TextView) findViewById(R.id.mpgIntro);
    	mpgIntro.setVisibility(View.GONE);
    	TextView distLabel = (TextView) findViewById(R.id.distance_label);
    	distLabel.setVisibility(View.GONE);
    	TextView litresLabel = (TextView) findViewById(R.id.litres_label);
    	litresLabel.setVisibility(View.GONE);
    	TextView priceLabel = (TextView) findViewById(R.id.price_label);
    	priceLabel.setVisibility(View.GONE);
    	distance.setVisibility(View.GONE);
    	litres.setVisibility(View.GONE);
    	price.setVisibility(View.GONE);  
    	buttonCalc.setVisibility(View.GONE);
    }
    
    /* Making new buttons visible after calculation */
    private void setVisible(){
    	ImageButton btn_history = (ImageButton) findViewById(R.id.btn_history);
    	btn_history.setVisibility(View.VISIBLE);
    	ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
    	btn_home.setVisibility(View.VISIBLE);
    	ImageButton btn_recalc = (ImageButton) findViewById(R.id.btn_recalc);
    	btn_recalc.setVisibility(View.VISIBLE);
    }
    
	public void Recalculate(View view) {
        Intent recalc = new Intent(Mpg_Calculation.this, Mpg_Calculation.class);
        startActivity(recalc);
    }
	
	public void openHome(View view) {
        Intent home = new Intent(Mpg_Calculation.this, StartActivity.class);
        startActivity(home);
    }
	
	public void openHistory(View view) {
        Intent logPage = new Intent(Mpg_Calculation.this, DataListView.class);
        startActivity(logPage);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mpg__calculation, menu);
		return true;
	}
}
