package com.oop;

public class TV {
	String Brand;
	String ModelName;
	Integer screenSize;
	Integer price;
	String color;
	String DisplayType;	
	
	
	
	void insertData(String B, String Mn, Integer ss, Integer P, String cc, String dt) {
		
		 Brand = B;
		 ModelName = Mn;
		 screenSize = ss;
		 price = P;
		 color = cc;
		 DisplayType = dt;
		
	}
	
	
	

	public static void main(String[] args) {
		
		TV lgOled = new TV();
		TV SamsungFrame = new TV();
		
		lgOled.Brand = "LG";
		lgOled.ModelName = "4K oled 75 tv";
		lgOled.screenSize = 75; 
		lgOled.price = 100000 ;
		lgOled.color = "black";
		lgOled.DisplayType = "OLED";
		
		
		SamsungFram.insertData("Samsung", "65 oled sreen",)
		System.out.println(lgOled.Brand + "  " + lgOled.ModelName);
		
		
	}
	
	
	
	
	
	
	
	
}

