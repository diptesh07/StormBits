package com.bits.checkin;

import java.io.FileWriter;

public class WriteToFile {

	public static void main(String args[]) {
		writeToFile("Hello World");
	}

	public static void writeToFile(String txt) {
		try {
			FileWriter fw = new FileWriter("/home/dips07/eclipse-workspace/storm/src/main/resources/newFile.txt",true);
			fw.write(txt);
			fw.write("\n");
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Success...");
	}
}
