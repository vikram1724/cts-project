package com.learn.oop.exception;

public class Eligibity {
	
	public static void Eligible(Integer Exp) {
		
		if(Exp < 10) {
			
			throw new ArithmeticException("This person is not eligible to be a senor System Architect");
		}else {
			System.out.println("This person is eligible");
		}
	}

}
