package com.learn.oop.collection;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayListDemo {
	public static void main(String[] args) {
		
		ArrayList<String> ParticipantList = new ArrayList<>();
		 ParticipantList.add("Vikram");
		 ParticipantList.add("Asif");
		 ParticipantList.add("Akhil");
		 ParticipantList .add("John");
		 ParticipantList .add("Sisio");
		 System.out.println( ParticipantList);
		 
		 Iterator itr = ParticipantList.iterator();
		 
		 while(itr.hasNext()) {
			 System.out.println( itr.next());
		 }
		 
	}

}
