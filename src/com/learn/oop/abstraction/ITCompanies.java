package com.learn.oop.abstraction;

public class VikramTech implements ITCompanies {
	
	public void working() {
		
		System.out.println("Vikram tech is working on a java project");
	}
	public static void main(String[] args) {
		
		VikramTech SMS = new VikramTech();
		SMS.working();
		
	}
	
}