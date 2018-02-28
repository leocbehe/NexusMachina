package nexusmachina.readfiletext;

import java.io.*;
import java.nio.charset.*;
import java.lang.System;
import org.json.JSONObject;
import org.json.JSONArray;


class Main {

	//CLASS VARIABLES
	static JSONObject input;
	static InputStream is;
	static InputStreamReader reader;
	static File textf;
	static int[] allFileChars;


	//MAIN LOGIC
	public static void main(String[] args){
		System.out.println();
		System.out.println("     ###################################################");
		System.out.println("     ##                                               ##");
		System.out.println("     ##                 NEXUS/MACHINA                 ##");
		System.out.println("     ##                                               ##");
		System.out.println("     ###################################################");
		System.out.println();
		textf = new File(args[0]);
		if(textf.length()>Integer.MAX_VALUE){
			System.err.println(" ! Files larger than 2GB not yet supported.");
			return;
		}
		
		if(setInputStream(textf) == null){
		 	System.err.println(" ! Failed to open file.");
		 	return;
		}
		System.out.println(" > Reading input file stream...");
		allFileChars = new int[(int)textf.length()];
		reader = new InputStreamReader(is, Charset.forName("UTF-8"));
		readFileChars(reader);
		System.out.println(" > Writing to output.");
		writeOutput();
	}
	//END MAIN LOGIC
	

	/**
	* Initializes the static InputStream object using the filename provided
	* in args[].
	*/
	public static InputStream setInputStream(File f){
		try{
			is = new FileInputStream(f);
		}catch(IOException e){
			System.err.println(e);
			return null;
		}
		return is;
	}
	
	

	public static void readFileChars(InputStreamReader rd){
		try{			
			int charCount = 0;
			int fileChar = allFileChars[charCount] = rd.read();
			while(fileChar!=-1){
				if(fileChar>0xff){
					/**
					* Check for common characters out of the 0x00-0xff code
					* point range which can be replaced by a similar character
					* with a code point < 0xff.
					*/
					if(fileChar==0x201c || fileChar==0x201d){
						fileChar = 0x22;
					}else if(fileChar==0x2014){
						fileChar = 0x2d;
					}else{
						System.out.println(fileChar);
						throw new IOException(" ! Code point value out of 8-bit range.");
					}
				}
				fileChar = allFileChars[charCount++] = rd.read();
			}
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	

	public static JSONObject parseInput(String filename){
		StringBuilder jsonStr = new StringBuilder();
		try{
			String line;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while((line = br.readLine()) != null){
				jsonStr.append(line);
			}
		}catch(IOException e){
			System.err.println(e.getLocalizedMessage());
		}
		return new JSONObject(jsonStr.toString());
	}
	
	
	
	public static void printAllChars(){
		long start = System.currentTimeMillis();
		for(int x = 0; allFileChars[x] > 0; x++){
			char cc = (char)allFileChars[x];
			System.out.print(x+": "+cc+"  ");
		}
		System.out.println(" > Execution time: "+(System.currentTimeMillis() - start)/1000.0 + " seconds.");
	}
	
	
	
	public static void writeOutput(){
		JSONArray patternsArray = new JSONArray();
		for(int x = 0; x < allFileChars.length; x++){
			if(allFileChars[x]==0)
				break;
			patternsArray.put(x, allFileChars[x]);
		}
		JSONObject outputObj = new JSONObject();	
		outputObj.put("readfiletext", patternsArray);
		try{
			FileWriter fw = new FileWriter("./io/output-readfiletext.json", false);
			fw.write(outputObj.toString());
			fw.close();
		}catch(IOException e){
			System.err.println(e.getLocalizedMessage());
		}		
	}

}
