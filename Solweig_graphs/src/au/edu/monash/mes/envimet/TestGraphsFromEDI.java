package au.edu.monash.mes.envimet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.jgnuplot.Graph;
import org.jgnuplot.Splot;
import org.jgnuplot.Terminal;

public class TestGraphsFromEDI
{
	public final static String SW_DIRECT_RADIATION = "Direct Sw Radiation (W/m²)";
	public final static String SW_DIFFUSE_RADIATION = "Diffuse Sw Radiation (W/m²)";
	public final static String T_SURFACE_K = "Pot. Temperature (K)";
	public final static String LONGWAVE_RAD_FROM_ENVIRONMENT = "Lw Radiation Environment (W/m²)";
	
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
	
	
	double x = 50;
	double y = 50;
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
		TestGraphsFromEDI testGraphsFromEDI = new TestGraphsFromEDI();
		//testGraphsFromEDI.process();
		
		
		String filename = "MonashCampusObservations_AT_14.00.00 08.04.2011";
		testGraphsFromEDI.processATM(filename);
		//testGraphsFromEDI.processATMEnergyGraphs();
	}
	
	public void processATMEnergyGraphs()
	{
		int cutPlace = 10;		
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
		variableNames.add(LONGWAVE_RAD_FROM_ENVIRONMENT);
		
		variableNames.add(ABSOLUTE_LAD);
		variableNames.add(REFLECTED_SW_RADIATION);
		variableNames.add(TEMPERATURE_FLUX);
		variableNames.add(VAPOUR_FLUX);
		variableNames.add(WATER_ON_LEAFES);
		variableNames.add(STOMATA_RESISTANCE);
		variableNames.add(LOCAL_MASS_BUDGET_PM10);
		

		ArrayList<ArrayList<ArrayList>> allData = new ArrayList<ArrayList<ArrayList>>();
		
		for (String dataFilename : set)
		{
			//ArrayList<ArrayList> data = getATMCutSpecVarSpecXY(dataDir + dataFilename + edtFileType, readEDI, variableNames, cutItem, cutPlace, x, y);
			ArrayList<ArrayList> data = getATMCutSpecVarSpecXY(dataDir + dataFilename + edtFileType, readEDI, fileVariables, cutItem, cutPlace, x, y);
			//System.out.println("data=" + data.toString());
			allData.add(data);
//			plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
			
			//break;
		}
		int count = 0;
		for (String dataFilename : set)
		{
			ArrayList<ArrayList> data = allData.get(count);			
			System.out.println(dataFilename + " " + data.toString());
			count++;
			
			//break;
		}
		
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
	
	public TreeMap processHourlyData(ArrayList<ArrayList> data)
	{
		TreeMap hourlyData = new TreeMap();
		
		
//		double swDirectRadiation = rs.getDouble(SW_DIRECT_RADIATION);
//		double swDiffuseRadiation = rs.getDouble(SW_DIFFUSE_RADIATION);
//		double tSurfaceK = rs.getDouble(T_SURFACE_K);
//		double surfaceAlbedo = rs.getDouble(SURFACE_ALBEDO);
//		double longwaveRadFromEnvironment = rs
//				.getDouble(LONGWAVE_RAD_FROM_ENVIRONMENT);
//		double soilHeatFlux = rs.getDouble(SOIL_HEAT_FLUX);
//		double sensibleHeatFlux = rs.getDouble(SENSIBLE_HEAT_FLUX);
//		double latentHeatFlux = rs.getDouble(LATENT_HEAT_FLUX);
//		String variableTime = rs.getString(VARIABLE_TIME);
//		
//		DateFormat formatterFromDB = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//		java.util.Date variableTimeDate = formatterFromDB.parse(variableTime);				
//
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
//		//String variableTimeStr = formatter.format(variableTime);
//		String variableTimeStr = formatter.format(variableTimeDate);
//
//		hourlyData.put(SW_DIRECT_RADIATION, "" + swDirectRadiation);
//		hourlyData.put(SW_DIFFUSE_RADIATION, "" + swDiffuseRadiation);
//		hourlyData.put(T_SURFACE_K, "" + tSurfaceK);
//		hourlyData.put(SURFACE_ALBEDO, "" + surfaceAlbedo);
//		hourlyData.put(LONGWAVE_RAD_FROM_ENVIRONMENT, "" + longwaveRadFromEnvironment);
//		hourlyData.put(LATENT_HEAT_FLUX, "" + Math.round(latentHeatFlux));
//		hourlyData.put(SOIL_HEAT_FLUX, "" + Math.round(soilHeatFlux));
//		hourlyData.put(SENSIBLE_HEAT_FLUX, "" + Math.round(sensibleHeatFlux));
//		hourlyData.put(VARIABLE_TIME, variableTimeStr);
//		
//		double swIn = swDirectRadiation + swDiffuseRadiation;
//		double swOut = (surfaceAlbedo) * (swIn);
//		//double lwIn = longwaveRadFromEnvironment;
//		double lwIn = rs.getDouble(REC_QLW_SKY);
//		
//		double lwOut = 0.0000000567 * Math.pow(tSurfaceK, 4);
//		double netRadiation = (swIn - swOut) + (lwIn - lwOut);
//		double availableEnergy = netRadiation - soilHeatFlux;
//		
//		double dailyEnergyBalance = netRadiation - soilHeatFlux - sensibleHeatFlux - latentHeatFlux;
//
//		hourlyData.put(NET_RADIATION, "" + Math.round(netRadiation));
//		hourlyData.put(AVAILABLE_ENERGY, "" + Math.round(availableEnergy));
//		
//		hourlyData.put(SW_IN, "" + Math.round(swIn));
//		hourlyData.put(SW_OUT, "" + Math.round(swOut));
//		hourlyData.put(LW_IN, "" + Math.round(lwIn));
//		hourlyData.put(LW_OUT, "" + Math.round(lwOut));
//		hourlyData.put(DAILY_ENERGY_BALANCE, "" + Math.round(dailyEnergyBalance));
//		
//		hourlyData.put(REC_Z, "" + rs.getDouble(REC_Z));
//		hourlyData.put(REC_DT0_DT, "" + rs.getDouble(REC_DT0_DT));
//		hourlyData.put(REC_Q0, "" + rs.getDouble(REC_Q0));
//		hourlyData.put(REC_UV1, "" + rs.getDouble(REC_UV1));
//		hourlyData.put(REC_W1, "" + rs.getDouble(REC_W1));
//		hourlyData.put(REC_T1, "" + rs.getDouble(REC_T1));
//		hourlyData.put(REC_KM, "" + rs.getDouble(REC_KM));
//		hourlyData.put(REC_KH, "" + rs.getDouble(REC_KH));
//		hourlyData.put(REC_QLW_BUDG, "" + rs.getDouble(REC_QLW_BUDG));
//		hourlyData.put(REC_QLW_BUDG, "" + rs.getDouble(REC_QLW_BUDG));
//		hourlyData.put(REC_QLW_SKY, "" + rs.getDouble(REC_QLW_SKY));
//		hourlyData.put(REC_QLW_LEAFS, "" + rs.getDouble(REC_QLW_LEAFS));
//		
//		returnData.add(hourlyData);
//		System.out.println(hourlyData.toString());
//		System.out.println();
		
		
		return hourlyData;
	}
	

	public void processATM(String filename)
	{
		TreeMap<String, String> runMeta = common.getMetaDataForRun(runID);

		System.out.println(runMeta.toString());
		String dataDir = runMeta.get(common.DATA_DIR);
		dataDir = dataDir + "../atmosphere/";
		runMeta.put(common.DATA_DIR, dataDir);
		dataDir = runMeta.get(common.DATA_DIR);
		runDesc = runMeta.get(common.RUN_DESC);
		String enviVersion = runMeta.get(common.ENVI_VERSION);
		String runName = runMeta.get(common.RUN_NAME);

		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../5TempGraphsD";
		
		//String filename = "MonashCampusValidationNW_AT_14.00.00 25.03.2011";
		//String directory = "/mnt/Samsung2TB/EnvimetRuns-5days/Monash/MonashCampusValidationNW/output/atmosphere/";
		String ediFileType = ".EDI";
		String edtFileType = ".EDT";
		String potTemp = "Pot. Temperature (K)";
		String cutItem = ReadEDTFile.Z;
		
		String variableName = potTemp;
		//processDirectory(dataDir, runMeta);
		
		ReadEDIFile readEDI = new ReadEDIFile(dataDir + filename + ediFileType);

		ArrayList<String> fileVariables = readEDI.getFileVariables();
		

		//System.out.println("fileVariables=" + fileVariables.toString());
		
		int cutPlace = 0;		
		ArrayList data = getATMCut(dataDir + filename + edtFileType, readEDI, variableName, cutItem, cutPlace);
		plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
		
		cutPlace = 1;		
		data = getATMCut(dataDir + filename + edtFileType, readEDI, variableName, cutItem, cutPlace);
		plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
		
		cutPlace = 2;		
		data = getATMCut(dataDir + filename + edtFileType, readEDI, variableName, cutItem, cutPlace);
		plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
		
		cutPlace = 7;		
		data = getATMCut(dataDir + filename + edtFileType, readEDI, variableName, cutItem, cutPlace);
		plotATMData(data, filename, variableName, graphDir, cutItem, cutPlace);
		
	}
	
	public ArrayList getATMCut(String fileAndDir, ReadEDIFile readEDI, String variableName, String cutItem, int cutPlace)
	{
		ReadEDTFile readEDTFile = new ReadEDTFile();
		readEDTFile.readEDTFileSingleVariable(fileAndDir, readEDI, variableName, cutItem, cutPlace, 290f);
		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		System.out.println(edtData.toString());
		ArrayList data = (ArrayList)edtData.get(variableName);
		return data;
		
	}
	
	
	public void plotATMData(
			ArrayList data, String outputFilename, String variableName, String outputDirectory, String cutItem, int cutPlace)
	{
		StringBuffer outputStr = new StringBuffer();
		String filename = null;
		String cutType = "";
		String xLabel = "";
		String yLabel = "";
		if (cutItem.equals( ReadEDTFile.X))
		{
			cutType = " yz cut "; 
			xLabel = "y (m)";
			yLabel = "z (m)";
		}
		else if (cutItem.equals( ReadEDTFile.Y))
		{
			cutType = " xz cut ";
			xLabel = "x (m)";
			yLabel = "z (m)";
		}
		if (cutItem.equals( ReadEDTFile.Z))
		{
			cutType = "xy cut";
			xLabel = "x (m)";
			yLabel = "y (m)";
		}
		
		for (int i = 0;i<data.size();i++)
		{
			ArrayList dataItem = (ArrayList)data.get(i);
			
			if ( dataItem.get(2).equals("0"))
				continue;
			
			Double cutX =0.0;
			Double cutY =0.0;
			
			if (cutItem.equals( ReadEDTFile.X))
			{
				cutX = (Double)dataItem.get(1);
				cutY = (Double)dataItem.get(3);				 
			}
			else if (cutItem.equals( ReadEDTFile.Y))
			{
				cutX = (Double)dataItem.get(0);
				cutY = (Double)dataItem.get(3);				
			}
			if (cutItem.equals( ReadEDTFile.Z))
			{
				cutX = (Double)dataItem.get(0);
				cutY = (Double)dataItem.get(1);				
			}
			
			outputStr.append(cutX + " " + cutY + " " + dataItem.get(2) + '\n');

			String year = outputFilename.substring(outputFilename.length() - 4,
					outputFilename.length());
			String month = outputFilename.substring(
					outputFilename.length() - 7, outputFilename.length() - 5);
			String day = outputFilename.substring(outputFilename.length() - 10,
					outputFilename.length() - 8);
			String hour = outputFilename.substring(
					outputFilename.length() - 16, outputFilename.length() - 14);
			String minute = outputFilename.substring(
					outputFilename.length() - 19, outputFilename.length() - 17);

			String oldFilename = outputFilename.substring(0,
					outputFilename.length() - 20);

			//System.out.println(oldFilename + "_" + year + "-" + month + "-"
			//		+ day + "_" + hour + ":" + minute);

			filename = common.removeIllegalCharacters(variableName) + "_"
					+ year + "-" + month + "-" + day + "_" + hour + ":"
					+ minute + "_" + oldFilename;
		}

		String dataFileName = outputDirectory + File.separator + filename
				+ ".dat";
		common.writeFile(outputStr.toString(), dataFileName);

		String outputFile = outputDirectory + File.separator + filename + "_" + cutType + "_" + cutPlace;
		String label = "Data from " + filename + " - " + runDesc + " " + cutType + " at " + cutPlace;
		
		//plotData(dataFileName, outputFile, xLabel, outputDirectory);
		
			Splot.setGnuplotExecutable("gnuplot");

			Splot.setPlotDirectory(outputDirectory);
			Graph aGraph = null;

			Splot aPlot = new Splot();

			aPlot.setGrid();
			aPlot.setKey(null);
			aPlot.setParametric();
			
			aPlot.addExtra("set title  \"" + label + "\"");

			aPlot.addExtra("unset hidden3d");
			aPlot.addExtra("set ticslevel 0.5");
			aPlot.addExtra("set view 60,30");
			aPlot.addExtra("set autoscale");
			aPlot.addExtra("set parametric");
			aPlot.addExtra("set style data points");
			
			//aPlot.addExtra("set key box");

			aPlot.addExtra("set dgrid3d 100,100,1");
			aPlot.addExtra("set xlabel \"" + xLabel + "\"");
			aPlot.addExtra("set ylabel \"" + yLabel + "\"");
			//aPlot.addExtra("set label \"" + label + "\"  ");
			aPlot.addExtra("set style data lines");

			aPlot.setDataFileName(dataFileName);

			aPlot.setOutput(Terminal.PNG, outputFile + ".png", " 2048,1200  small ");
			common.runPlotCmd(outputDirectory, aPlot);

			//aPlot.addExtra("set label \"" + label + "\"  at 0,520");
			aPlot.addExtra("set size square");
			aPlot.addExtra("set pm3d map");
			aPlot.addExtra("set palette rgbformulae 22,13,10");
			aPlot.addExtra("set autoscale");
			//aPlot.addExtra("set no key");
			aPlot.setOutput(Terminal.PNG, outputFile + "_c" + ".png", " 2048,1200  small ");
			common.runPlotCmd(outputDirectory, aPlot);

			
			aPlot.addExtra("set palette gray");
			aPlot.addExtra("set samples 100; set isosamples 100");

			aPlot.setOutput(Terminal.PNG, outputFile + "_c_g" + ".png", " 2048,1200  small ");
			common.runPlotCmd(outputDirectory, aPlot);

			
			String imageFilename = outputFile + "_contour" + ".png";
			String imageBlendFilename = outputFile + "_blend" + ".png";
			String imageBlendTransFilename = outputFile + "_blendT" + ".png";
			String imageBlendDissolveFilename = outputFile + "_dis" + ".png";
			String imageBlendDiffFilename = outputFile + "_diff" + ".png";
			String imageTransparent = outputFile + "_trans" + ".png";
			
			aPlot = new Splot();
			aPlot.setDataFileName(dataFileName);
			aPlot.addExtra("set grid; unset key; set parametric; unset polar; unset hidden3d; set ticslevel 0.5; set view 60,30");
			aPlot.addExtra("set autoscale; set parametric; set style data points; set dgrid3d 10,10,1; set style data lines; set size square");
			aPlot.addExtra("set pm3d map; set palette rgbformulae 22,13,10; unset logscale; set contour both; set cntr levels 100; unset clabel; unset surface");
			aPlot.setOutput(Terminal.PNG, imageFilename, "  2048,1200  small ");
			aPlot.addExtra("set xlabel \"" + xLabel + "\"");
			aPlot.addExtra("set ylabel \"" + yLabel + "\"");
			//aPlot.addExtra("set label \"" + label + "\"  at 0,520");
			aPlot.addExtra("set title  \"" + label + "\"");
			
			aPlot.setWithLinePalette(true);
			
			
			common.runPlotCmd(outputDirectory, aPlot);
		
		
	}	


	public void process()
	{
		TreeMap<String, String> runMeta = common.getMetaDataForRun(runID);
//		TreeMap<String, String> runMeta = new TreeMap<String, String>();
//		if (true)
//		{
//			runMeta.put(common.RUN_DESC, "Monash campus validation");
//			runMeta.put(common.RUN_NAME, "MonashCampusValidation");
//			runMeta.put(common.ENVI_VERSION, "3");
//			runMeta.put(common.DATA_DIR, "/mnt/Samsung2TB/EnvimetRuns/MonashCampusValidation/output/surface/");
//		}
		System.out.println(runMeta.toString());
		String dataDir = runMeta.get(common.DATA_DIR);
		dataDir = dataDir + "../surface_1day/";
		runMeta.put(common.DATA_DIR, dataDir);
		dataDir = runMeta.get(common.DATA_DIR);
		runDesc = runMeta.get(common.RUN_DESC);
		String enviVersion = runMeta.get(common.ENVI_VERSION);
		String runName = runMeta.get(common.RUN_NAME);

		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphs";
		processDirectory(dataDir, runMeta);
		//
		// String filename =
		// "/home/nice/.wine/drive_c/ENVImetprojects/Monash/Test/surface/MySim_FX_09.00.00 23.06.2010";
		// // String filename =
		// //
		// "/home/nice/.wine/drive_c/ENVImetprojects/Monash/Run002/surface/As_is_FX_23.00.00 1.1.2010";
		// String ediFileType = ".EDI";
		// String edtFileType = ".EDT";
		//
		// filename =
		// "/home/nice/Documents/MonashMasters/Research Dissertation/ENVImet40runs/surface/4.0Test_FX_08.00.04 23.06.2010";
		//
		// ReadEDIFile readEDI = new ReadEDIFile(filename + ediFileType);
		//
		// ReadEDTFile readEDTFile = new ReadEDTFile(filename + edtFileType,
		// readEDI);
		//
		// OutputExcel outputExcel = new OutputExcel();
		// boolean diffTabs = false;
		// boolean hideXY = false;
		// outputExcel.outputEDIEDTSpreadsheet(readEDI, readEDTFile, filename,
		// diffTabs, hideXY);
	}

	public void processDirectory(String directory, TreeMap<String, String> runMeta)
	{
		TreeSet<String> set = new TreeSet<String>();

		String ediFileType = ".EDI";
		String edtFileType = ".EDT";
		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphs";

		String[] files = common.getDirectoryList(directory);
		for (int i = 0; i < files.length; i++)
		{
			String file = files[i];

			String trimmedFilename = file.replaceFirst(ediFileType, "");
			trimmedFilename = trimmedFilename.replaceFirst(edtFileType, "");
			System.out.println("trimmedFilename=" + trimmedFilename);
			System.out.println("trimmedFilename="
					+ trimmedFilename.subSequence(
							trimmedFilename.length() - 20,
							trimmedFilename.length()));
			set.add(trimmedFilename);

		}

		ReadEDIFile[] readEDIArray = new ReadEDIFile[set.size()];
		ReadEDTFile[] readEDTArray = new ReadEDTFile[set.size()];
		int count = 0;
		for (String filename : set)
		{
			ReadEDIFile readEDI = new ReadEDIFile(directory + filename
					+ ediFileType);

			ArrayList<String> fileVariables = readEDI.getFileVariables();

			System.out.println("fileVariables=" + fileVariables.toString());

			ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
					+ edtFileType, readEDI);

			TreeMap<String, ArrayList> edtData = readEDTFile.getData();
			for (int i = 0; i < fileVariables.size(); i++)
			{
				String variable = fileVariables.get(i);
				ArrayList variableData = edtData.get(variable);
				//System.out.println(variable + "=" + variableData);
				if (variable.equals("T Surface (K)"))
				{
					generateDataFileFromDataPoints(variableData, filename, variable, graphDir);
				}
			}			
			
			// trim ignored fields
			// ArrayList<String> newFileVariables = trimVariablesOfIgnored(
			// readEDI.getFileVariables(), ignoredFields);
			// System.out.println("newFileVariables=" +
			// newFileVariables.toString());
			// readEDI.setFileVariables(newFileVariables);
			// readEDTFile = trimEDTOfIgnored(readEDTFile, ignoredFields);
			// System.out.println("readEDTFile.getData()=" +
			// readEDTFile.getData().toString());

			// store results
			readEDIArray[count] = readEDI;
			readEDTArray[count] = readEDTFile;
			count++;

			// System.out.println("fileVariables=" +
			// readEDI.getFileVariables().toString());
			// System.out.println("edtData="+edtData.toString());

			// System.exit(1);

		}
//		runPlotCmd(graphDir);
		// OutputExcel outputExcel = new OutputExcel();
		// boolean diffTabs = false;
		// boolean hideXY = false;
		//
		// if (summarize)
		// {
		// outputExcel.outputEDIEDTSpreadsheetSummary(readEDIArray,
		// readEDTArray, diffTabs, hideXY);
		// } else
		// {
		// outputExcel.outputEDIEDTSpreadsheet(readEDIArray, readEDTArray,
		// diffTabs, hideXY);
		// }
	}




	public void generateDataFileFromDataPoints(
			ArrayList data, String outputFilename, String variableName, String outputDirectory)
	{

		// TreeMap<String, String> metaData = common.getMetaDataForRun(runID);
		// String outputDirectory = metaData.get(ENVICommon.DATA_DIR)
		// + "../graphs";
		// common.createDirectory(outputDirectory);
		//
		// String variableName = "" + variable;
		//
		// for (String key : variableTypeMappingKeys)
		// {
		// if (variableTypeMapping.get(key).equals(variable))
		// {
		// variableName = key;
		// }
		// }
		//
		StringBuffer outputStr = new StringBuffer();
		String filename = null;
		//
		// Set<String> keys = data.keySet();
		// System.out.println(keys.toString());

		// for (String key : keys)
		// {
		// ArrayList<DataPoint> dataPoints = data.get(key);

		
		// Sensible heat flux (W/m²)=[[2.5, 2.5, -60.6894], [4.5, 2.5
		
		for (int i = 0;i<data.size();i++)
		{
			ArrayList dataItem = (ArrayList)data.get(i);
			
			if ( dataItem.get(2).equals("0"))
				continue;
			
			outputStr.append(dataItem.get(0) + " " + dataItem.get(1) + " "
					+ dataItem.get(2) + '\n');
//			String outputFilename = dataPoint.getOutputFile();

//			String year = outputFilename.substring(outputFilename.length() - 4,
//					outputFilename.length());
//			String month = outputFilename.substring(
//					outputFilename.length() - 7, outputFilename.length() - 5);
//			String day = outputFilename.substring(outputFilename.length() - 10,
//					outputFilename.length() - 8);
//			String hour = outputFilename.substring(
//					outputFilename.length() - 16, outputFilename.length() - 14);
//			String minute = outputFilename.substring(
//					outputFilename.length() - 19, outputFilename.length() - 17);
//
//			String oldFilename = outputFilename.substring(0,
//					outputFilename.length() - 20);
//
//			System.out.println(oldFilename + "_" + year + "-" + month + "-"
//					+ day + "_" + hour + ":" + minute);
//
//			filename = common.removeIllegalCharacters(variableName) + "_"
//					+ year + "-" + month + "-" + day + "_" + hour + ":"
//					+ minute + "_" + oldFilename;
			filename = getTrimmedFilename(outputFilename, variableName);
		}

		String dataFileName = outputDirectory + File.separator + filename
				+ ".dat";
		common.writeFile(outputStr.toString(), dataFileName);

		String outputFile = outputDirectory + File.separator + filename;
		String xLabel = "Data from " + filename + " - " + runDesc;
		plotData(dataFileName, outputFile, xLabel, outputDirectory);
		
		
		
		// }
	}
	
	public String getTrimmedFilename(String outputFilename, String variableName)
	{
		String year = outputFilename.substring(outputFilename.length() - 4,
				outputFilename.length());
		String month = outputFilename.substring(
				outputFilename.length() - 7, outputFilename.length() - 5);
		String day = outputFilename.substring(outputFilename.length() - 10,
				outputFilename.length() - 8);
		String hour = outputFilename.substring(
				outputFilename.length() - 16, outputFilename.length() - 14);
		String minute = outputFilename.substring(
				outputFilename.length() - 19, outputFilename.length() - 17);

		String oldFilename = outputFilename.substring(0,
				outputFilename.length() - 20);

		System.out.println(oldFilename + "_" + year + "-" + month + "-"
				+ day + "_" + hour + ":" + minute);

		return common.removeIllegalCharacters(variableName) + "_"
				+ year + "-" + month + "-" + day + "_" + hour + ":"
				+ minute + "_" + oldFilename;
	}
	
	public void plotData(String dataFileName, String outputFile, String label, String outputDirectory)
	{
		Splot.setGnuplotExecutable("gnuplot");

		Splot.setPlotDirectory(outputDirectory);
		Graph aGraph = null;

		Splot aPlot = new Splot();

		aPlot.setGrid();
		aPlot.setKey(null);
		aPlot.setParametric();

		aPlot.addExtra("unset hidden3d");
		aPlot.addExtra("set ticslevel 0.5");
		aPlot.addExtra("set view 60,30");
		aPlot.addExtra("set autoscale");
		aPlot.addExtra("set parametric");
		aPlot.addExtra("set style data points");
		
		aPlot.addExtra("set key box");

		aPlot.addExtra("set dgrid3d 10,10,1");
		aPlot.addExtra("set xlabel \"x (m)\"");
		aPlot.addExtra("set ylabel \"y (m)\"");
		aPlot.addExtra("set label \"" + label + "\"  ");
		aPlot.addExtra("set style data lines");

		aPlot.setDataFileName(dataFileName);

		aPlot.setOutput(Terminal.PNG, outputFile + ".png", " 1024,600  small ");
		common.runPlotCmd(outputDirectory, aPlot);
//		try
//		{
//			String plotName = aPlot.plot();
//			System.out.println("Plotting cmd=" + plotName);
//			common.actualPlotCmd(plotName, outputDirectory);
//			
//			//String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
//			//Process aProcess = Runtime.getRuntime().exec(commands);
//			
//
//		} catch (Exception e)
//		{
//			System.err.println(e);
//			System.exit(1);
//		}
		aPlot.addExtra("set label \"" + label + "\"  at 0,520");
		aPlot.addExtra("set size square");
		aPlot.addExtra("set pm3d map");
		aPlot.addExtra("set palette rgbformulae 22,13,10");
		aPlot.addExtra("set autoscale");
		//aPlot.addExtra("set no key");
		aPlot.setOutput(Terminal.PNG, outputFile + "_c" + ".png", " 1024,600  small ");
		common.runPlotCmd(outputDirectory, aPlot);
//		try
//		{
//			String plotName = aPlot.plot();
//			System.out.println("Plotting cmd=" + plotName);
//			common.actualPlotCmd(plotName, outputDirectory);
//			
//			//String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
//			//Process aProcess = Runtime.getRuntime().exec(commands);
//			
//
//		} catch (Exception e)
//		{
//			System.err.println(e);
//			System.exit(1);
//		}
		
		aPlot.addExtra("set palette gray");
		aPlot.addExtra("set samples 100; set isosamples 100");

		aPlot.setOutput(Terminal.PNG, outputFile + "_c_g" + ".png", " 1024,600  small ");
		common.runPlotCmd(outputDirectory, aPlot);
//		try
//		{
//			String plotName = aPlot.plot();
//			System.out.println("Plotting cmd=" + plotName);
//			common.actualPlotCmd(plotName, outputDirectory);
//			
//			//String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
//			//Process aProcess = Runtime.getRuntime().exec(commands);
//			
//
//		} catch (Exception e)
//		{
//			System.err.println(e);
//			System.exit(1);
//		}
		
		String imageFilename = outputFile + "_contour" + ".png";
		String imageBlendFilename = outputFile + "_blend" + ".png";
		String imageBlendTransFilename = outputFile + "_blendT" + ".png";
		String imageBlendDissolveFilename = outputFile + "_dis" + ".png";
		String imageBlendDiffFilename = outputFile + "_diff" + ".png";
		String imageTransparent = outputFile + "_trans" + ".png";
		
		aPlot = new Splot();
		aPlot.setDataFileName(dataFileName);
		aPlot.addExtra("set grid; unset key; set parametric; unset polar; unset hidden3d; set ticslevel 0.5; set view 60,30");
		aPlot.addExtra("set autoscale; set parametric; set style data points; set dgrid3d 10,10,1; set style data lines; set size square");
		aPlot.addExtra("set pm3d map; set palette rgbformulae 22,13,10; unset logscale; set contour both; set cntr levels 100; unset clabel; unset surface");
		aPlot.setOutput(Terminal.PNG, imageFilename, "  2048,1200  small ");
		aPlot.addExtra("set xlabel \"x (m)\"");
		aPlot.addExtra("set ylabel \"y (m)\"");
		aPlot.addExtra("set label \"" + label + "\"  at 0,520");
		
		//aPlot.setPm3d(true);
		aPlot.setWithLinePalette(true);
		
		
		common.runPlotCmd(outputDirectory, aPlot);
		
//		try
//		{
//			String plotName = aPlot.plot();
//			System.out.println("Plotting cmd=" + plotName);
//			common.actualPlotCmd(plotName, outputDirectory);			
//			//String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
//			//Process aProcess = Runtime.getRuntime().exec(commands);
//			
//
//		} catch (Exception e)
//		{
//			System.err.println(e);
//			System.exit(1);
//		}
		
		common.imageMakeTransparent(imageFilename, imageTransparent);
		common.imageCompositeBlendCmd(MONASH_IMAGE, imageFilename, imageBlendFilename, new Integer(25));
		// composite -gravity center -blend 25 MonashResize-850_u.png  T\ Surface\ \(K\)_2011-03-22_00\:13_MonashCampusValidation_FX_c.png blend_big_u.png
		common.imageCompositeBlendCmd(MONASH_IMAGE, imageTransparent, imageBlendTransFilename, new Integer(25));
		
		//composite -gravity center -dissolve 55 MonashResize-850.png  T\ Surface\ \(K\)_2011-03-22_00\:13_MonashCampusValidation_FX_cT.png blend_bigT.png
		common.imageCompositeDissolveCmd(MONASH_IMAGE, imageTransparent, imageBlendDissolveFilename, new Integer(55));
		//composite -gravity center -compose difference MonashResize-850.png  T\ Surface\ \(K\)_2011-03-22_00\:13_MonashCampusValidation_FX_cT.png blend_bigT.png
		common.imageCompositeDifferenceCmd(MONASH_IMAGE, imageTransparent, imageBlendDiffFilename);
		
		String imageBlendFilenameSoil = outputFile + "_Soilblend" + ".png";
		String imageBlendTransFilenameSoil = outputFile + "_SoilblendT" + ".png";
		String imageBlendDissolveFilenameSoil = outputFile + "_Soildis" + ".png";
		String imageBlendDiffFilenameSoil = outputFile + "_Soildiff" + ".png";
		
		common.imageCompositeBlendCmd(MONASH_SOIL_IMAGE, imageFilename, imageBlendFilenameSoil, new Integer(25));
		common.imageCompositeBlendCmd(MONASH_SOIL_IMAGE, imageTransparent, imageBlendTransFilenameSoil, new Integer(25));
		common.imageCompositeDissolveCmd(MONASH_SOIL_IMAGE, imageTransparent, imageBlendDissolveFilenameSoil, new Integer(55));
		common.imageCompositeDifferenceCmd(MONASH_SOIL_IMAGE, imageTransparent, imageBlendDiffFilenameSoil);
		
		String imageBlendFilenameVeg = outputFile + "_Vegblend" + ".png";
		String imageBlendTransFilenameVeg = outputFile + "_VegblendT" + ".png";
		String imageBlendDissolveFilenameVeg = outputFile + "_Vegdis" + ".png";
		String imageBlendDiffFilenameVeg = outputFile + "_Vegdiff" + ".png";
		
		common.imageCompositeBlendCmd(MONASH_VEGETATION_IMAGE, imageFilename, imageBlendFilenameVeg, new Integer(25));
		common.imageCompositeBlendCmd(MONASH_VEGETATION_IMAGE, imageTransparent, imageBlendTransFilenameVeg, new Integer(25));
		common.imageCompositeDissolveCmd(MONASH_VEGETATION_IMAGE, imageTransparent, imageBlendDissolveFilenameVeg, new Integer(55));
		common.imageCompositeDifferenceCmd(MONASH_VEGETATION_IMAGE, imageTransparent, imageBlendDiffFilenameVeg);
		
		String imageBlendFilenameBuilding = outputFile + "_Bublend" + ".png";
		String imageBlendTransFilenameBuilding = outputFile + "_BublendT" + ".png";
		String imageBlendDissolveFilenameBuilding = outputFile + "_Budis" + ".png";
		String imageBlendDiffFilenameBuilding = outputFile + "_Budiff" + ".png";
		
		common.imageCompositeBlendCmd(MONASH_BUILDING_IMAGE, imageFilename, imageBlendFilenameBuilding, new Integer(25));
		common.imageCompositeBlendCmd(MONASH_BUILDING_IMAGE, imageTransparent, imageBlendTransFilenameBuilding, new Integer(25));
		common.imageCompositeDissolveCmd(MONASH_BUILDING_IMAGE, imageTransparent, imageBlendDissolveFilenameBuilding, new Integer(55));
		common.imageCompositeDifferenceCmd(MONASH_BUILDING_IMAGE, imageTransparent, imageBlendDiffFilenameBuilding);
		

	}	
	
	
	

	
	
	

	


}
