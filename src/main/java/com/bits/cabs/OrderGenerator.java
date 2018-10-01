package com.bits.cabs;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OrderGenerator {

	public static void main(String args[]) {

		Random random = new Random();

		try {
			FileWriter fw = new FileWriter("/home/dips07/eclipse-workspace/storm/src/main/resources/orders.txt");

			List<String> contactNumbers = new ArrayList<String>();
			for (int i = 1; i <= 10; i++) {
				String number = "";
				for (int j = 1; j <= 10; j++) {
					number += (int) (Math.random() * 10);
				}
				System.out.println(number);
				contactNumbers.add(number);
			}
			
			List<String> outlets = new ArrayList<String>();
			for(int i=1;i<=10;i++) {
				String outlet = "";
				for(int j=1;j<=7;j++) {
					int val = (int)(Math.random() *10);
					if(j<3)
						outlet+=(char)(65+val);
					else
						outlet+=val;
				}
				outlets.add(outlet);
			}
			
			List<String> flavors = new ArrayList<String>();
			flavors.add("Mango");
			flavors.add("Vanilla");
			flavors.add("Chocolate");
			flavors.add("Butter-Scotch");
			flavors.add("Strawberry");
			flavors.add("Blueberry");
			flavors.add("Maple-Walnut");
			flavors.add("Cotton-Candy");
			flavors.add("Peppermint");
			flavors.add("Tutti-Frutti");

			for(int count =1;count<=1000;count++) {
				int orderId = count;
				String time = String.valueOf(System.currentTimeMillis());
				String customerId = contactNumbers.get(random.nextInt(10));
				String outletId = outlets.get(random.nextInt(10));
				String flavor = flavors.get(random.nextInt(10));
				
				fw.write(orderId + " " + customerId+" "+ outletId + " " +time + " " + flavor + "\n");
				
			}
			Thread.sleep(1000);
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
