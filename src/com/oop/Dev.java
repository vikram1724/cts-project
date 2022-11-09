package com.oop;

class Employees {
	
	Integer BaseSalary = 50000;
	
}
public class Dev extends Employees{
	
	Integer Bonus = 25000;
	Integer total = Bonus + BaseSalary;
	
	public static void main(String[] args) {
		
		Dev Vikram = new Dev();
		System.out.println("Total salary of Vikram is" + Vikram.total)
	}
	
	
}



