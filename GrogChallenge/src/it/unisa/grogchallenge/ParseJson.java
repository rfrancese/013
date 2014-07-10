package it.unisa.grogchallenge;
import java.util.ArrayList;
import java.util.HashMap;


public class ParseJson {
	/*
	 * Questa classe 
	 * 	
	 */
	public HashMap<String, String> parse(String str) {
		HashMap<String, String> toReturn = new HashMap<String, String>();
		ArrayList<String> tmp = new ArrayList<String>();
		if(str.equals("") || str == null)return null;
		int i = 0;
		
		
		while(str.charAt(i) != '}'){
			
			if(str.charAt(i) != '{' && str.charAt(i) != '"' && str.charAt(i) != ':' && str.charAt(i) != ','){
				String s = "";
				int j=i;
				while(str.charAt(j) != '"'){
					s = s +  str.charAt(j);
					j++;
				}
				i=j;
					tmp.add(s);
				
			}
			i++;
		}
		
		/*
		for (String string : tmp) {
		System.out.println(string);
		}*/
		
		for (int j = 0; j < tmp.size(); j = j + 2) {
			toReturn.put(tmp.get(j),tmp.get(j+1));
		}
		
		
		
		return toReturn;
	}
	
}
