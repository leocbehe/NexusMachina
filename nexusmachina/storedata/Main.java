package nexusmachina.storedata;

import java.io.*;
import java.lang.StringBuilder;
import org.json.JSONObject;
import org.json.JSONArray;

class Main{

	public static int PATTERN_SIZE = 100;
	public static String inputFileName;
	public static String outputFileName;

	public static void main(String[] args){
		if(args.length < 2){
			System.out.println(" > No memory filename given, output not stored to memory.");
			return;
		}
		inputFileName = args[0];
		outputFileName = args[1];
		File textf = new File(inputFileName);
		File memoryf = new File(outputFileName);
		System.out.println(" > Reading relations file...");
		JSONObject inputData = readFile(textf);
		System.out.println(" > Reading memory file...");
		JSONObject outputData = readFile(memoryf);
		System.out.println(" > Modifying memory...");
		writeToMemory(inputData, outputData);
	}



	public static JSONObject readFile(File f){
		BufferedReader br;
		StringBuilder sb = new StringBuilder();
		try{
			br = new BufferedReader(new FileReader(f));
			String line;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			br.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return new JSONObject(sb.toString());
	}



	public static void writeToMemory(JSONObject inputData, JSONObject outputData){
		JSONArray arr;
		JSONObject positions;
		JSONObject outputPositions;
		for(String key : inputData.keySet()){
			for(int i = 0; i < PATTERN_SIZE*2; i++){
				arr = inputData.getJSONArray(key);
				positions = arr.getJSONObject(i);
				outputPositions = outputData.getJSONArray(key).getJSONObject(i);
				for(String positionKey : positions.keySet()){
					outputData.getJSONArray(key).getJSONObject(i).put(positionKey, outputPositions.getInt(positionKey)+positions.getInt(positionKey));
				}
			}
		}
		
		System.out.println(" > Writing memory changes to output...");
		writeToFile(outputData);
	}
	
	
	
	public static void writeToFile(JSONObject outputData){
		FileWriter fw;
		try{
			fw = new FileWriter(outputFileName);
			outputData.write(fw, 1, 1);
			fw.flush();
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
}
