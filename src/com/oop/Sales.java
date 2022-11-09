package com.oop;

public class Sales extends Employee{
	
	Integer Bonus = 10000;
	Integer insentive ;
	
	void working() {
		System.out.println("Employee is a part of sales team and they are working");
	}
	public static void main(String[] args) {
		
		Sales Anish = new Sales();
		Anish.insentive =2000;
		Anish.TotalSalary(Anish.BaseSalary, Anish.Bonus, Anish.insentive);
		Anish.working();
	}
	
	

}
