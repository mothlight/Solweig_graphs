package au.edu.monash.mes.envimet;

import java.util.ArrayList;
import java.util.TreeMap;

public class RetrieveDataFromATMXY
{
	
	public final static String SW_DIRECT_RADIATION = "Direct Sw Radiation (W/m²)";
	public final static String SW_DIFFUSE_RADIATION = "Diffuse Sw Radiation (W/m²)";
	public final static String T_SURFACE_K = "Pot. Temperature (K)";
	public final static String LONGWAVE_RAD_FROM_ENVIRONMENT = "Lw Radiation Environment (W/m²)";
	public final static String WIND_SPEED_M_S = "Wind Speed (m/s)";
	public final static String SPEC_HUMIDTY = "Spec. Humidity (g/kg)";
	public final static String REL_HUMIDTY = "Relative Humidity (%)";
	
	public final static String ABSOLUTE_LAD = "Absolute LAD (m²/m³)";
	public final static String REFLECTED_SW_RADIATION = "Reflected Sw Radiation (W/m²)";
	public final static String TEMPERATURE_FLUX = "Temperature Flux (K*m/s))";
	public final static String VAPOUR_FLUX = "Vapour Flux (g/kg*m/s))";
	public final static String WATER_ON_LEAFES = "Water on Leafes (g/m²)";
	public final static String STOMATA_RESISTANCE = "Stomata Resistance (m/s)";
	public final static String LOCAL_MASS_BUDGET_PM10 = "Local mass budget  PM10 (µg/s*m³)";
	
	
	
	public final static String SOIL_HEAT_FLUX = "Soil_heat_Flux";
	public final static String SENSIBLE_HEAT_FLUX = "Sensible_heat_flux";
	public final static String VARIABLE_TIME = "variable_time";
	public final static String BLACK_BODY_RADIATION = "black_body_radiation";
	public final static String SURFACE_ALBEDO = "Surface_Albedo";
	
//	public final String SW_DOWN = "swDown";
//	public final String LW_DOWN = "lwDown";
	
	public final String SW_IN = "swIn";
	public final String LW_IN = "lwIn";
	public final String SW_OUT = "swOut";
	public final String LW_OUT = "lwOut";
	public final String NET_RADIATION = "netRadiation";
	public final String AVAILABLE_ENERGY = "availableEnergy";
	public final String LATENT_HEAT_FLUX = "Latent_heat_flux";
	public final String DAILY_ENERGY_BALANCE = "Daily_energy_balance";
	
	public final static String BUILDING_HEIGHT = "building_height";
	public final static String DEPOSITION_SPEED = "deposition_speed";
	public final static String EXCHANGE_COEFF_HEAT = "exchange_coeff_heat";
	public final static String LAMBERT_FACTOR = "lambert_factor";
	public final static String LONGWAVE_RADIATION_BUDGET = "longwave_radiation_budget";
	public final static String MASS_BOIMET = "mass_boimet";
	public final static String MASS_DEPOSED = "mass_deposed";
	public final static String Q_AIR_BIOMET = "q_air_biomet";
	public final static String Q_SURFACE = "q_surface";
	public final static String RECEPTORS = "receptors";
	public final static String SHADOW_FLAG = "shadow_flag";
	public final static String SKY_VIEW_FAKTOR = "sky_view_faktor";
	public final static String SURFACE_EXPOSITION = "surface_exposition";
	public final static String SURFACE_INCLINATION = "surface_inclination";
	public final static String T_AIR_BOIMET = "t_air_boimet";
	public final static String T_SURFACE_CHANGE = "t_surface_change";
	public final static String T_SURFACE_DIFF = "t_surface_diff";
	public final static String TMRT_BOIMET = "tmrt_boimet";
	public final static String UV_ABOVE_SURFACE = "uv_above_surface";
	public final static String WALKABILITY_VALUE = "walkability_value";
	public final static String WATER_FLUX = "water_flux";
	public final static String WIND_SPEED_BOIMET = "wind_speed_boimet";
	public final static String Z_BOIMET = "z_boimet";
	public final static String Z_NODE_BOIMET = "z_node_boimet";
	public final static String Z_TOPO = "z_topo";
	
	

	int runID = 30; // 5, 6, 7, 8, 10, 11

	ENVICommon common = new ENVICommon();
	CreateGraphFromData createGraphFromData = new CreateGraphFromData();
	public final String MONASH_IMAGE = "/home/nice/Documents/MonashMasters/Research Dissertation/MonashImages/MonashResize-850.png";
	
	public final String MONASH_BUILDING_IMAGE = "/home/nice/Documents/MonashMasters/Research Dissertation/MonashImages/MonashCampus-BuildingModel-850.png";
	public final String MONASH_SOIL_IMAGE = "/home/nice/Documents/MonashMasters/Research Dissertation/MonashImages/MonashCampus-SoilModel-850.png";
	public final String MONASH_VEGETATION_IMAGE = "/home/nice/Documents/MonashMasters/Research Dissertation/MonashImages/MonashCampus-VegetationModel-850.png";
	public String runDesc;

	public static void main(String[] args)
	{
		RetrieveDataFromATMXY retrieveDataFromATMXY = new RetrieveDataFromATMXY();
		//testGraphsFromEDI.process();
		//testGraphsFromEDI.processATM();
		
		double x = 23;
		double y = 29;		
		
		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
		
//		double x = 37;
//		double y = 81;		
//		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);
////		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);
//		
//		
//		x = 63;
//		y = 78;		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
//		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);
//
//		
//		x = 85;
//		y = 59;		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
//		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);
//
//		
//		x = 33;
//		y = 48;		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
//		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);
//
//		
//		x = 77;
//		y = 18;		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
//		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);
//
//		
//		x = 68;
//		y = 38;		
//		retrieveDataFromATMXY.processATMEnergyGraphs(x,y);
////		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y-1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+0);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+0);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x-1,y+1);		
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+0,y+1);
////		retrieveDataFromATMXY.processATMEnergyGraphs(x+1,y+1);

	}
	
	public void processATMEnergyGraphs(double x, double y)
	{
		String lineSeparator = System.getProperty("line.separator");

		
		int cutPlace = 1;		
		TreeMap<String, String> runMeta = common.getMetaDataForRun(runID);

		System.out.println(runMeta.toString());
		String dataDir = runMeta.get(common.DATA_DIR);
		dataDir = dataDir + "../atmosphere/";
		runMeta.put(common.DATA_DIR, dataDir);
		dataDir = runMeta.get(common.DATA_DIR);
		runDesc = runMeta.get(common.RUN_DESC);
		String enviVersion = runMeta.get(common.ENVI_VERSION);
		String runName = runMeta.get(common.RUN_NAME);

		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphtest";
		
		//String filename = "MonashCampusValidationNW_AT_14.00.00 25.03.2011";
		//String directory = "/mnt/Samsung2TB/EnvimetRuns-5days/Monash/MonashCampusValidationNW/output/atmosphere/";
		String ediFileType = ".EDI";
		String edtFileType = ".EDT";
		//String potTemp = "Pot. Temperature (K)";
		String cutItem = ReadEDTFile.Z;
		
		ArrayList<String> set = new ArrayList<String>();
		String[] files = common.getDirectoryList(dataDir);
		for (int i = 0; i < files.length; i++)
		{
			String file = files[i];

			String trimmedFilename = file.replaceFirst(ediFileType, "");
			trimmedFilename = trimmedFilename.replaceFirst(edtFileType, "");
			//System.out.println("trimmedFilename=" + trimmedFilename);
			//System.out.println("trimmedFilename=" + trimmedFilename.subSequence(trimmedFilename.length() - 20,trimmedFilename.length()));
			set.add(trimmedFilename);
			//otherwise doubled filenames with both EDI and EDT
			i++;
		}
		
		//String variableName = potTemp;
		//processDirectory(dataDir, runMeta);
		
		ReadEDIFile readEDI = new ReadEDIFile(dataDir + files[0] );

		ArrayList<String> fileVariables = readEDI.getFileVariables();		

		System.out.println("fileVariables=" + fileVariables.toString());
		ArrayList<String> variableNames = new ArrayList<String>(); 
		variableNames.add(SW_DIRECT_RADIATION);
		variableNames.add(SW_DIFFUSE_RADIATION);
		variableNames.add(T_SURFACE_K);
		variableNames.add(WIND_SPEED_M_S); //ws
		variableNames.add(SPEC_HUMIDTY); //q-spec
		variableNames.add(REL_HUMIDTY); //q-rel
		
		ArrayList<ArrayList<ArrayList>> allData = new ArrayList<ArrayList<ArrayList>>();
		
		for (String dataFilename : set)
		{
			//ArrayList<ArrayList> data = getATMCutSpecVarSpecXY(dataDir + dataFilename + edtFileType, readEDI, variableNames, cutItem, cutPlace, x, y);
			ArrayList<ArrayList> data = getATMCutSpecVarSpecXY(dataDir + dataFilename + edtFileType, readEDI, variableNames, cutItem, cutPlace, x, y);
			//System.out.println("data=" + data.toString());
			allData.add(data);
//			plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
			
			//break;
		}
		String outputFilename = null;
		StringBuffer outputStr = new StringBuffer();
		
		int count = 0;
		for (String dataFilename : set)
		{
			ArrayList<ArrayList> data = allData.get(count);			
			System.out.println(dataFilename + " " + data.toString());
			//  MonashCampusValidationNW_AT_06.00.00 22.03.2011 [[[252.5, 252.5, 0, 1.0]], [[252.5, 252.5, 0, 1.0]], [[252.5, 252.5, 296.5711, 1.0]]]
			
			
			//MonashCampusValidationNW_AT_00.00.00 23.03.2011.EDI
			String date = dataFilename.substring(dataFilename.length() - 10, dataFilename.length() - 0); 
			String time = dataFilename.substring(dataFilename.length() - 19, dataFilename.length() - 11);	
			
			String outputDate = date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2) 
								+ "-" + time.substring(0, 2) + ":" 
								//+ time.substring(3, 5)
								;
			
			ArrayList inner = data.get(0);
			//System.out.println("inner=" + inner);
			ArrayList Diffuse_Sw_RadiationList = (ArrayList)inner.get(0);
			//System.out.println("Diffuse_Sw_RadiationList(0)=" + Diffuse_Sw_RadiationList.get(0));
			
			String Diffuse_Sw_Radiation = (String)Diffuse_Sw_RadiationList.get(2); 
			
			ArrayList inner1 = data.get(1);
			ArrayList Direct_Sw_RadiationList = (ArrayList)inner1.get(0);
			String Direct_Sw_Radiation = (String)Direct_Sw_RadiationList.get(2); 
			ArrayList inner2 = data.get(2);
			ArrayList Pot_Temperature_KList = (ArrayList)inner2.get(0);
			String Pot_Temperature_K = (String)Pot_Temperature_KList.get(2);
			
			ArrayList inner3 = data.get(3);
			ArrayList windSpeedList = (ArrayList)inner3.get(0);
			String windSpeed = (String)windSpeedList.get(2); 
			
			ArrayList inner4 = data.get(4);
			ArrayList specHumList = (ArrayList)inner4.get(0);
			String specHum = (String)specHumList.get(2); 
			
			ArrayList inner5 = data.get(5);
			ArrayList relHumList = (ArrayList)inner5.get(0);
			String relHum = (String)relHumList.get(2); 
			
			//System.out.println("");
			String values = "\t" + Diffuse_Sw_Radiation + "\t" + Direct_Sw_Radiation + "\t" + Pot_Temperature_K
								+ "\t" + windSpeed
								+ "\t" + specHum
								+ "\t" + relHum	;
			
			System.out.println(outputDate + values);
			outputStr.append(outputDate + "00" + values + lineSeparator);
			outputStr.append(outputDate + "05" + values + lineSeparator);
			outputStr.append(outputDate + "10" + values + lineSeparator);
			outputStr.append(outputDate + "15" + values + lineSeparator);
			outputStr.append(outputDate + "20" + values + lineSeparator);
			outputStr.append(outputDate + "25" + values + lineSeparator);
			outputStr.append(outputDate + "30" + values + lineSeparator);
			outputStr.append(outputDate + "35" + values + lineSeparator);
			outputStr.append(outputDate + "40" + values + lineSeparator);
			outputStr.append(outputDate + "45" + values + lineSeparator);
			outputStr.append(outputDate + "50" + values + lineSeparator);
			outputStr.append(outputDate + "55" + values + lineSeparator);
			
			if (count < 1)
			{
				// x252.5y252.5z1.0.dat
				outputFilename = "x" + Diffuse_Sw_RadiationList.get(0) + 
								"y" + Diffuse_Sw_RadiationList.get(1) + 
								"z" + Diffuse_Sw_RadiationList.get(3) +
								".dat";
				//System.out.println("outputFilename=" + outputFilename);
			}
			
			count++;
			
			//break;
		}
		
		String testDir = "/home/nice/Documents/MonashMasters/Research Dissertation/MonashObservations/FullResults/";
		common.writeFile(outputStr.toString(), testDir + outputFilename);
		
	}
	
	public ArrayList<ArrayList> getATMCutSpecVarSpecXY(String fileAndDir, ReadEDIFile readEDI, ArrayList<String> variableNames, String cutItem, int cutPlace, double x, double y)
	{
		
		ReadEDTFile readEDTFile = new ReadEDTFile();
		readEDTFile.readEDTFileSpecificVariablesSpecificPoint(fileAndDir, readEDI, variableNames, cutItem, cutPlace, -99999f, x, y);
		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		System.out.println(edtData.toString());
		ArrayList<ArrayList> data = new ArrayList();
		for (String variableName : variableNames)
		{
			data.add(edtData.get(variableName));
		}		
		return data;
		
	}	

}
