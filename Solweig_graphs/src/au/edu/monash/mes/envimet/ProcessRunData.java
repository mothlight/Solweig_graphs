package au.edu.monash.mes.envimet;

import java.util.ArrayList;
import java.util.TreeMap;

public class ProcessRunData
{
	
	ENVICommon common = new ENVICommon();
	ImportDataToDB importDataToDB = new ImportDataToDB();
		
	double x = 54.5;
	double y = 48.5;
	public final boolean DONT_IMPORT = true;
	

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String dataDirectory;
		int enviVersion;
		ProcessRunData processRunData;
		int runID;
		
		

		//System.out.println(args[0]);
		
		
		dataDirectory = "/mnt/Samsung2TB/EnvimetRuns-5days/Monash/MonashCampusObservations/output/surface/";
		//dataDirectory = args[0] ;
		
		// /mnt/Samsung2TB/EnvimetRuns-5days/Monash/MonashCampusValidationSW/output/surface
		
		enviVersion = 3;
		processRunData = new ProcessRunData();
		runID = processRunData.process(dataDirectory, enviVersion);		
//		processRunData.processGraphs(runID);
		

		
		
		
		//processRunData.processIndVariableGraphs(runID);
		
		
		
//		ArrayList<String> dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SW_DIRECT_RADIATION);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SOIL_HEAT_FLUX);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.BUILDING_HEIGHT);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.DEPOSITION_SPEED);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SOIL_HEAT_FLUX);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.EXCHANGE_COEFF_HEAT);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.LAMBERT_FACTOR);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.LONGWAVE_RADIATION_BUDGET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.MASS_BOIMET);  //v4 only		
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.MASS_DEPOSED);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_AIR_BIOMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_SURFACE);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.RECEPTORS);		
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SHADOW_FLAG);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SKY_VIEW_FAKTOR);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SURFACE_EXPOSITION);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.SURFACE_INCLINATION);			
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_AIR_BOIMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_CHANGE);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_DIFF);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.TMRT_BOIMET);	
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.UV_ABOVE_SURFACE);			
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.WALKABILITY_VALUE);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.WATER_FLUX);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.WIND_SPEED_BOIMET);		
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Z_BOIMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Z_NODE_BOIMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Z_TOPO);		
		
//		processRunData.processGenericGraphs(8, dataToPlot);
		


	}
	
//	public void parseFilename()
//	{
//		String filename = "BareFtCtMelRec-40-2days_FX_23.00.04 26.01.2011";
//		String year = filename.substring(filename.length()-4, filename.length());
//		String month = filename.substring(filename.length()-7, filename.length()-5);
//		String day = filename.substring(filename.length()-10, filename.length()-8);
//		String hour = filename.substring(filename.length()-16, filename.length()-14);
//		String minute = filename.substring(filename.length()-19, filename.length()-17);
//		String oldFilename = filename.substring(0, filename.length()-20);
//		
//		System.out.println( oldFilename + "_"+ year + "-" + month + "-" + day + "_" + hour + ":" + minute);
//		
//	}
	
	public void processIndVariableGraphs(int runID)
	{
		CreateGraphFromData create = new CreateGraphFromData();		
		ArrayList<Integer> variableTypes = common.getDistinctVariableTypesForRun(runID);
		System.out.println(variableTypes.toString());
		
//		TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, 2);
//		create.generateDataFileFromDataPoints(data, 2);
//		
//		System.exit(0);
		
		for (Integer variableType : variableTypes)
		{
			TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, variableType);
			create.generateDataFileFromDataPoints(data, variableType, runID);
		}
	}
	
	public void processGenericGraphs(int runID, ArrayList<String> dataToPlot)
	{
		CreateDailyEnergyBalanceGraph create = new CreateDailyEnergyBalanceGraph();
		create.setDataToPlot(dataToPlot);
		ArrayList<TreeMap<String, String>> data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.plotDataFromDatafile(data, runID, "test");
	}
	
	public void processGraphs(int runID)
	{
		ArrayList<TreeMap<String, String>> data ;
		ArrayList<String> dataToPlot;
		
		CreateDailyEnergyBalanceGraph create = new CreateDailyEnergyBalanceGraph();

		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.generateDataFileFromDataPoints(data, runID);
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_AIR_BIOMET);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_SURFACE);	
		create.setDataToPlot(dataToPlot);
		data = null;
		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.plotDataFromDatafile(data, runID, "ediQ");		
		
		data = null;
		data = create.getReceptorFilesDataset(runID);
		System.out.println (data.toString());
		create.graphReceptorFileData(data, runID);	
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_Q0);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_BUDG);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_LEAFS);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SKY);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SURF);		
		System.out.println (data.toString());
		create.graphReceptorFileData(data, runID, dataToPlot, "recQ");		
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_T1);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_DT0_DT);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);				
		System.out.println (data.toString());
		create.graphReceptorFileData(data, runID, dataToPlot, "recT");			
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_AIR_BOIMET);	
		create.setDataToPlot(dataToPlot);
		data = null;
		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.plotDataFromDatafile(data, runID, "ediT");
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.SW_DIFFUSE_RADIATION);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.SW_DIRECT_RADIATION);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.LONGWAVE_RADIATION_BUDGET);	
		dataToPlot.add(CreateDailyEnergyBalanceGraph.LONGWAVE_RAD_FROM_ENVIRONMENT);		
		dataToPlot.add(CreateDailyEnergyBalanceGraph.BLACK_BODY_RADIATION);
		create.setDataToPlot(dataToPlot);
		data = null;
		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.plotDataFromDatafile(data, runID, "ediSW");
		
	
				
	}
	
	public int process(String dataDirectory, int enviVersion)
	{
		int runID = importDataToDB.insertNewRun(dataDirectory, enviVersion);
		System.out.println("Starting runID = " + runID);
		
		if (!DONT_IMPORT)
		{
			importDataToDB.processDirectory(runID);		
			ParseAndStoreReceptorFile receptorParse = new ParseAndStoreReceptorFile();
			receptorParse.insertReceptorMetaData(runID);
			receptorParse.readReceptorFile(runID);		
		}
		
		return runID;
	}

}
