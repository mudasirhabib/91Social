package com.social91.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social91.dto.ChainAssembly;
import com.social91.dto.CycleComponents;
import com.social91.dto.CyclePriceList;
import com.social91.dto.Frame;
import com.social91.dto.HandleBarBrakes;
import com.social91.dto.Seating;
import com.social91.dto.Wheels;
@SuppressWarnings("deprecation")
public class CyclePrice {
	public static JSONArray cyclePriceList = new JSONArray();
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {


		 //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader readerPrice = new FileReader("priceList.json");FileReader readerInput = new FileReader("input.json"))
        {
        	JSONParser parser = new JSONParser();

            Object objPrice = jsonParser.parse(readerPrice);
            Object objInput = jsonParser.parse(readerInput);
            JSONArray priceList = (JSONArray) objPrice;
            JSONArray inputList = (JSONArray) objInput;
            
            //Iterate over cycle list
         Iterator<JSONObject> it =  inputList.iterator();
         ExecutorService executor = Executors.newFixedThreadPool(10);
         
        while(it.hasNext()) {
        	executor.execute(new Task(it.next()));
        }
        try {
            if (!executor.awaitTermination(8000, TimeUnit.MILLISECONDS)) {
            	executor.shutdownNow();
            } 
        } catch (InterruptedException e) {
        	executor.shutdownNow();
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
      //WRITE OUTPUT TO JSON FILE
    	try (FileWriter file = new FileWriter("output.json")) {
            file.write(cyclePriceList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 static class Task implements Runnable{
	 JSONObject cycle;
	 
	 Task(JSONObject  cycle){
		 this.cycle = cycle;
		 new Thread(this);
	 }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		parseCycleObject(cycle);
	}
	
 }
    private static void parseCycleObject(JSONObject cycleParts) 
    {
    	 CycleComponents cycle;
         ObjectMapper mapper = new ObjectMapper();
         CyclePriceList price;
         
         try {
			cycle = mapper.readValue(cycleParts.toString(), CycleComponents.class);
			 String dateOfPricing = cycle.getDateOfPricing();
			 int framePrice = getFramePrice(cycle.getFrame(), dateOfPricing);
			 int handleBarBrakesPrice = getHandleBarBrakesPrice(cycle.getHandleBarBrakes(), dateOfPricing);
			 int seatingPrice = getSeatingPrice(cycle.getSeating(), dateOfPricing);  
			 int wheelsPrice = getWheelsPrice(cycle.getWheels(), dateOfPricing); 
			 int chainAssemblyPrice = getChainAssemblyPrice(cycle.getChainAssembly(), dateOfPricing);
			 int totalPrice = framePrice + handleBarBrakesPrice + seatingPrice + wheelsPrice + chainAssemblyPrice ;
			 System.out.println("Cycle Id : "+cycle.getCycleId()+"\nFrame Price : "+framePrice+"\nHandle Price : "+handleBarBrakesPrice+" \nSeating Price : "+seatingPrice+"\nWheels Price : "+wheelsPrice+"\nChain Price : "+chainAssemblyPrice+"\nTotal Price : "+totalPrice);
			 System.out.println("***********************************");
			 
			 JSONObject cyclePriceObject = new JSONObject();
			 cyclePriceObject.put("cycleId", cycle.getCycleId());
			 cyclePriceObject.put("framePrice", framePrice);
			 cyclePriceObject.put("handleBarBrakesPrice",handleBarBrakesPrice);
			 cyclePriceObject.put("seatingPrice", seatingPrice);
			 cyclePriceObject.put("wheelsPrice", wheelsPrice);
			 cyclePriceObject.put("chainAssemblyPrice", chainAssemblyPrice);	
			 cyclePriceObject.put("totalPrice", totalPrice);
			 
		    JSONObject cyclePrice = new JSONObject(); 
		    cyclePrice.put("cyclePriceList", cyclePriceObject);
		    
		  //Add  list
	    	//JSONArray cyclePriceList = new JSONArray();
	    	cyclePriceList.add(cyclePrice);
			 
         } catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
    private static int getFramePrice(Frame frame, String dateOfPricing) {
    	
    	JSONParser parser = new JSONParser();
    	FileReader framePrice;
    	int topTubePrice = 0;
		 int downTubePrice = 0;
		 int seatTubePrice = 0;
		 int seatStayPrice = 0;
		 int chainStayPrice = 0;
		try {
			framePrice = new FileReader("framePrice.json");
			Object objInput1 = parser.parse(framePrice);
		   	JSONArray jsonObject1 = (JSONArray) objInput1;
		   	JSONObject companyList1 = (JSONObject)jsonObject1.get(0);
		   	Map<String, String> m = (Map)companyList1.get(dateOfPricing.toLowerCase());
		   	 String topTube = frame.getTopTube();
			 String downTube = frame.getDownTube();
			 String seatTube = frame.getSeatTube();
			 String seatStay = frame.getSeatStay();
			 String chainStay = frame.getChainStay();
 
			  topTubePrice = Integer.parseInt(m.get(topTube));
			  downTubePrice = Integer.parseInt(m.get(downTube));
			  seatTubePrice = Integer.parseInt(m.get(seatTube));
			  seatStayPrice = Integer.parseInt(m.get(seatStay));
			  chainStayPrice = Integer.parseInt(m.get(chainStay));
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return (topTubePrice + downTubePrice + seatTubePrice + seatStayPrice + chainStayPrice);
    }
    private static int getHandleBarBrakesPrice(HandleBarBrakes handleBarBrakes, String dateOfPricing) {
    	JSONParser parser = new JSONParser();
    	FileReader framePrice;
    	int handleBarGripPrice = 0;
		 int headTubePrice = 0;
		 int shockAbsorberPrice = 0;
		 int frontBrakePrice = 0;
		 int forkPrice = 0;
		try {
			framePrice = new FileReader("handleBarBrakesPrice.json");
			Object objInput1 = parser.parse(framePrice);
		   	JSONArray jsonObject1 = (JSONArray) objInput1;
		   	JSONObject companyList1 = (JSONObject)jsonObject1.get(0);
		   	
		   	Map<String, String> m = (Map)companyList1.get(dateOfPricing.toLowerCase());

		   	 String handleBarGrip = handleBarBrakes.getHandleBarGrip();
			 String headTube = handleBarBrakes.getHeadTube();
			 String shockAbsorber = handleBarBrakes.getShockAbsorber();
			 String frontBrake = handleBarBrakes.getFrontBrake();
			 String fork = handleBarBrakes.getFork();
			 
			 handleBarGripPrice = Integer.parseInt(m.get(handleBarGrip));
			 headTubePrice = Integer.parseInt(m.get(headTube));
			 shockAbsorberPrice = Integer.parseInt(m.get(shockAbsorber));
			 frontBrakePrice = Integer.parseInt(m.get(frontBrake));
			 forkPrice = Integer.parseInt(m.get(fork));
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return (handleBarGripPrice + headTubePrice + shockAbsorberPrice + frontBrakePrice + forkPrice);
    }
    private static int getSeatingPrice(Seating seat, String dateOfPricing) {
    	JSONParser parser = new JSONParser();
    	FileReader framePrice;
    	int saddlePrice = 0;
		 int seatPostPrice = 0;
		try {
			framePrice = new FileReader("seatingPrice.json");
			Object objInput1 = parser.parse(framePrice);
		   	JSONArray jsonObject1 = (JSONArray) objInput1;
		   	JSONObject companyList1 = (JSONObject)jsonObject1.get(0);
		   	
		   	Map<String, String> m = (Map)companyList1.get(dateOfPricing.toLowerCase());
		   	
		   	 String saddle = seat.getSaddle();
			 String seatPost = seat.getSeatPost();

			 
			 saddlePrice = Integer.parseInt(m.get(saddle));
			 seatPostPrice = Integer.parseInt(m.get(seatPost));
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return (saddlePrice + seatPostPrice );
   
    }
    private static int getWheelsPrice(Wheels wheels, String dateOfPricing) {

    	    JSONParser parser = new JSONParser();
        	FileReader framePrice;
        	int spokePrice = 0;
    		 int hubTubePrice = 0;
    		 int rimPrice = 0;
    		 int tirePrice = 0;
    		 int valvePrice = 0;
    		try {
    			framePrice = new FileReader("wheelsPrice.json");
    			Object objInput1 = parser.parse(framePrice);
    		   	JSONArray jsonObject1 = (JSONArray) objInput1;
    		   	JSONObject companyList1 = (JSONObject)jsonObject1.get(0);
    		   	
    		   	Map<String, String> m = (Map)companyList1.get(dateOfPricing.toLowerCase());

    		   	 String spoke = wheels.getSpoke();
    			 String hub = wheels.getHub();
    			 String rim = wheels.getRim();
    			 String tire = wheels.getTire();
    			 String valve = wheels.getValve();
    			 
    			 spokePrice = Integer.parseInt(m.get(spoke));
    			 hubTubePrice = Integer.parseInt(m.get(hub));
    			 rimPrice = Integer.parseInt(m.get(rim));
    			 tirePrice = Integer.parseInt(m.get(tire));
    			 valvePrice = Integer.parseInt(m.get(valve));
    			
    		
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	return (spokePrice + hubTubePrice + rimPrice + tirePrice + valvePrice);
 
    }
    private static int getChainAssemblyPrice(ChainAssembly chainParts, String dateOfPricing) {
    
        JSONParser parser = new JSONParser();
    	FileReader framePrice;
    	int chainPrice = 0;
		 int chainRingsPrice = 0;
		try {
			framePrice = new FileReader("chainAssemblyPrice.json");
			Object objInput1 = parser.parse(framePrice);
		   	JSONArray jsonObject1 = (JSONArray) objInput1;
		   	JSONObject companyList1 = (JSONObject)jsonObject1.get(0);
		   	
		   	Map<String, String> m = (Map)companyList1.get(dateOfPricing.toLowerCase());

		   	 String chain = chainParts.getChain();
			 String chainRings = chainParts.getChainRings();

			 
			 chainPrice = Integer.parseInt(m.get(chain));
			 chainRingsPrice = Integer.parseInt(m.get(chainRings));
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return (chainPrice + chainRingsPrice );
    }
}
