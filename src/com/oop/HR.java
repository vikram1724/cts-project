package com.oop;

	public class HR extends Employee{
		
		Integer Bonus = 20000;
		Integer total = Bonus + BaseSalary;
		public static void main(String[] args) {
			
			HR Anisha = new HR();
		  Anisha.TotalSalary(Anisha.BaseSalary, Anisha.Bonus);
		  
		}

}
