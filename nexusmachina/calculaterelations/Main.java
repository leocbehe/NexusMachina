package nexusmachina.calculaterelations;

import java.io.*;
import org.json.JSONObject;
import org.json.JSONArray;

class Main {

	public static final short PATTERN_SIZE = 100;
	public static JSONObject input;
	public static JSONObject outputObj;
	public static JSONArray characters;
	public static int[][][] patterns;
	public static long startTime;

	public static void main(String[] args){
		startTime = System.currentTimeMillis();
		patterns = new int[0xff][PATTERN_SIZE*2][0xff];
		if(readCharacters()==null)
			return;
		System.out.println(" > Calculating relations...");
		calculateRelations();
		System.out.println(" > Writing relations to file...");
		writePatterns();
		System.out.println(" > Runtime: "+(System.currentTimeMillis()-startTime)/1000.0+" seconds.");
	}



	public static JSONArray readCharacters(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("./io/output-readfiletext.json"));
			StringBuilder jsonString = new StringBuilder();
			String line;
			while((line = br.readLine()) != null){
				jsonString.append(line);
			}
			input = new JSONObject(jsonString.toString());
			characters = (JSONArray)input.get("readfiletext");
		}catch(IOException e){
			System.err.println(e.getMessage());
			return null;
		}
		return characters;
	}



	public static void calculateRelations(){
		for(int i = PATTERN_SIZE; i < characters.length()-PATTERN_SIZE; i++){
			int charVal = characters.getInt(i);
			for(int z = i-PATTERN_SIZE; z < i+PATTERN_SIZE; z++){
				if(i-z==0){
					continue;
				}else{
					int relatedCharVal = characters.getInt(z);
					patterns[charVal][z+PATTERN_SIZE-i][relatedCharVal] = patterns[charVal][z+PATTERN_SIZE-i][relatedCharVal] + 1; 
				}
			}			
		}
	}
	
	
	
	public static void writePatterns(){
		JSONObject outputObj = new JSONObject();
		JSONArray positions;
		JSONObject occurences;
		Character ch;
		
		for(int a = 0; a < 0xff; a++){
			if((a>=0x80 && a<=0x9f) || (a<=0x1f)){
				continue;
			}else if(a % 16 == 0){
				double f = a;
				double percent = (f/256) * 100;
				System.out.println(" > "+percent+"% complete... ");
			}
						
			positions = new JSONArray();
			for(int b = 0; b < PATTERN_SIZE*2; b++){
				occurences = new JSONObject();
				for(int c = 0; c < 0xff; c++){
					if((c>=0x80 && c<=0x9f) || (c<=0x1f))
						continue;
					ch = (char)c;
					occurences.put(ch.toString(), patterns[a][b][c]);
				}
				positions.put(b, occurences);
			}
			Character rootChar = (char)a;
			outputObj.put(rootChar.toString(), positions);
		}
		System.out.println(" > 100% complete. ");
		
		//write output obj to file
		System.out.println(" > Writing to file...");
		try{
			FileWriter fw = new FileWriter("./io/output-calculaterelations.json");
			outputObj.write(fw, 1, 1);
			fw.close();
		}catch(IOException e){
			System.err.println(e.getLocalizedMessage());
		}
	}
	


}
