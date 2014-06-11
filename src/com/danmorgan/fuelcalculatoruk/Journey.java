package com.danmorgan.fuelcalculatoruk;

/* Creating a structure - Can create Journey objects rather than string objects */

public class Journey {
	private String id;
	private String distance;
	private String mpg;
	private String cost;
	private int pkey;
	
	public Journey() {
		
	}
	
	public Journey(int i, String id, String distance, String mpg, String cost){
		this.pkey=i;
		this.id=id;
		this.distance=distance;
		this.mpg=mpg;
		this.cost=cost;
	}
	
	/* Get and set methods to be used in database handler */
	
	public void setPkey(int pkey){
		this.pkey=pkey;
	}
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setDistance(String distance){
		this.distance=distance;
	}
	
	public void setMpg(String mpg){
		this.mpg=mpg;
	}
	
	public void setCost(String cost){
		this.cost=cost;
	}
	
	public int getpKey(){
		return pkey;
	}
	
	public String getId(){
		return id;
	}
	
	public String getDistance(){
		return distance;
	}
	
	public String getMpg(){
		return mpg;
	}
	
	public String getCost(){
		return cost;
	}
	
	@Override
	public String toString() {
		return "Journey [id = " + id + ", distance = " + distance + " "
				+ "mpg = " + mpg + "cost = " + cost + "]";		
	}
	
	
	
	
	
	
	
	
	
}
