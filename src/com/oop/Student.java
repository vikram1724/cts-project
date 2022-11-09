package com.oop;

public class Student {
	Integer id;
	String name;
	Address address;
	
	void addStudent(Integer id, String name, Address address) {
		
		this.id = id;
		this.name = name;
		this.address = address;
	}
	void displayStudent() {
		
		System.out.println(name + " " + address.city + " " + address.country);
		
	}

	public static void main(String[] args) {
		Address johnAddress = new Address("Banglore", "Karnataka", "India");
		
		
		Student john = new Student();
		john.addStudent(101, "john philip", johnAddress);
		john.displayStudent();
	}
 	
}
