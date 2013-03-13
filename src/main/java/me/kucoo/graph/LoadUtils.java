package me.kucoo.graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LoadUtils {

	public static String[] loadNames(String fileName)  {
		BufferedReader bf = null;
		Set<String> set = new HashSet<String>();
		
		try {
			FileReader reader = new FileReader(fileName);
			bf = new BufferedReader(reader);
			
			String line;
			
			while ((line = bf.readLine()) != null) {
				if (!line.startsWith("/m/"))
					continue;
				String[] parts = line.split("\t");
				set.add(parts[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set.toArray(new String[0]);

	}
	
	
	public static FactRecord[] loadFactRecords(String fileName)  {
		BufferedReader bf = null;
		Set<FactRecord> set = new HashSet<FactRecord>();
		
		try {
			FileReader reader = new FileReader(fileName);
			bf = new BufferedReader(reader);
			
			String line;
			
			while ((line = bf.readLine()) != null) {
				//System.out.println("line : " + line);
				if (!line.startsWith("/m/"))
					continue;
				
				String[] parts = line.split("\t");
				FactRecord fr = new FactRecord(parts[0], parts[1]);
				set.add(fr);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set.toArray(new FactRecord[0]);

	}
	
	
	public static FactRecord[] loadFactRecords(String fileName, int n)  {
		int count = 0;
		BufferedReader bf = null;
		Set<FactRecord> set = new HashSet<FactRecord>();
		
		try {
			FileReader reader = new FileReader(fileName);
			bf = new BufferedReader(reader);
			
			String line;
			
			while ((line = bf.readLine()) != null && count++ < n) {
				//System.out.println("line : " + line);
				if (!line.startsWith("/m/"))
					continue;
				
				String[] parts = line.split("\t");
				FactRecord fr = new FactRecord(parts[0], parts[1]);
				set.add(fr);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set.toArray(new FactRecord[0]);

	}
}
