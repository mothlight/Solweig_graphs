package au.edu.monash.mes.envimet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;


public class TestRunningGraphs
{

	ENVICommon common = new ENVICommon();
	ImportDataToDB importDataToDB = new ImportDataToDB();
		
	double x = 54.5;
	double y = 48.5;
	int runID = 29;  //5, 6, 7, 8, 10, 11
	
	public static void main(String[] args)
	{
		TestRunningGraphs processRunData = new TestRunningGraphs();
		
		
		TreeMap<String, String> runMeta = processRunData.common.getMetaDataForRun(processRunData.runID);
		System.out.println(runMeta.toString());
		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphs/";
//		try
//		{
//			Process aProcess = Runtime.getRuntime().exec("gwenview " + graphDir);
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//processRunData.testProcess(processRunData.runID);
		processRunData.processGraphs(processRunData.runID);
		System.out.println("Graphs in " + graphDir);

	}
	
	public void testProcess(int runID)
	{
		ArrayList<String> dataToPlot;
		
		CreateDailyEnergyBalanceGraph create = new CreateDailyEnergyBalanceGraph();
		ArrayList<TreeMap<String, String>> data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.generateDataFileFromDataPoints(data, runID);
		
//		data = create.getReceptorFilesDataset(runID);
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID);	
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_Q0);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_BUDG);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_LEAFS);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SKY);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SURF);		
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID, dataToPlot, "recQ");		
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_T1);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_DT0_DT);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);				
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID, dataToPlot, "recT");			
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_AIR_BIOMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_SURFACE);	
//		create.setDataToPlot(dataToPlot);
//		data = create.getDataset(runID, x, y);
//		System.out.println (data.toString());
//		create.plotDataFromDatafile(data, runID, "ediQ");
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_AIR_BOIMET);	
//		create.setDataToPlot(dataToPlot);
//		data = create.getDataset(runID, x, y);
//		System.out.println (data.toString());
//		create.plotDataFromDatafile(data, runID, "ediT");
	}
	
	public void processGraphs(int runID)
	{
		processIndVariableGraphs(runID);
		
		ArrayList<TreeMap<String, String>> data ;
		ArrayList<String> dataToPlot;
		
		CreateDailyEnergyBalanceGraph create = new CreateDailyEnergyBalanceGraph();

		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.generateDataFileFromDataPoints(data, runID);
		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_AIR_BIOMET);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.Q_SURFACE);	
//		create.setDataToPlot(dataToPlot);
//		data = create.getDataset(runID, x, y);
//		System.out.println (data.toString());
//		create.plotDataFromDatafile(data, runID, "ediQ");		
		
//		data = create.getReceptorFilesDataset(runID);
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID);	
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_Q0);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_BUDG);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_LEAFS);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SKY);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_QLW_SURF);		
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID, dataToPlot, "recQ");		
		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_T1);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.REC_DT0_DT);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);				
//		System.out.println (data.toString());
//		create.graphReceptorFileData(data, runID, dataToPlot, "recT");			
//		
//		dataToPlot = new ArrayList<String>();
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_SURFACE_K);
//		dataToPlot.add(CreateDailyEnergyBalanceGraph.T_AIR_BOIMET);	
//		create.setDataToPlot(dataToPlot);
//		data = create.getDataset(runID, x, y);
//		System.out.println (data.toString());
//		create.plotDataFromDatafile(data, runID, "ediT");
		
		dataToPlot = new ArrayList<String>();
		dataToPlot.add(CreateDailyEnergyBalanceGraph.SW_DIFFUSE_RADIATION);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.SW_DIRECT_RADIATION);
		dataToPlot.add(CreateDailyEnergyBalanceGraph.LONGWAVE_RADIATION_BUDGET);	
		dataToPlot.add(CreateDailyEnergyBalanceGraph.LONGWAVE_RAD_FROM_ENVIRONMENT);		
		dataToPlot.add(CreateDailyEnergyBalanceGraph.BLACK_BODY_RADIATION);
		create.setDataToPlot(dataToPlot);
		data = create.getDataset(runID, x, y);
		System.out.println (data.toString());
		create.plotDataFromDatafile(data, runID, "ediSW");
						
	}	
	
	public void processIndVariableGraphs(int runID)
	{
		
		Integer T_Surface_KID = 2;		
		Integer q_surfaceID = 5;
		Integer t_surface_changeID = 4;
		Integer t_surface_diffID = 3;
		Integer uv_above_surfaceID = 6;
		Integer	Sw_Direct_RadiationID = 11;
		Integer	Sw_Diffuse_RadiationID = 12;
		Integer	Longwave_Rad_from_environmentID  = 16;
		Integer	Soil_heat_FluxID  = 10;
		Integer	Sensible_heat_fluxID  = 7;
		Integer	Latent_heat_fluxID  = 9;		
		Integer	longwave_radiation_budgetID = 14;
		Integer	Longwave_Rad_from_vegetationID  = 15;
		
		ArrayList<Integer> variablesToProcess = new ArrayList<Integer>();
		variablesToProcess.add(T_Surface_KID);
//		variablesToProcess.add(q_surfaceID);
//		variablesToProcess.add(t_surface_changeID);
//		variablesToProcess.add(t_surface_diffID);
//		variablesToProcess.add(uv_above_surfaceID);
//		variablesToProcess.add(Sw_Direct_RadiationID);
//		variablesToProcess.add(Sw_Diffuse_RadiationID);
//		variablesToProcess.add(Longwave_Rad_from_environmentID);
//		variablesToProcess.add(Soil_heat_FluxID);
//		variablesToProcess.add(Sensible_heat_fluxID);
//		variablesToProcess.add(Latent_heat_fluxID);
//		variablesToProcess.add(longwave_radiation_budgetID);
//		variablesToProcess.add(Longwave_Rad_from_vegetationID);		
		
		
		CreateGraphFromData create = new CreateGraphFromData();		
//		ArrayList<Integer> variableTypes = common.getDistinctVariableTypesForRun(runID);
//		System.out.println(variableTypes.toString());
//		
//		TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, 2);
//		create.generateDataFileFromDataPoints(data, 2);
//		
//		System.exit(0);
		
		for (Integer variableType : variablesToProcess)
		{
			System.out.println("Process graphs for " + variableType);
			TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, variableType);
			create.generateDataFileFromDataPoints(data, variableType, runID);
		}
	}	

}
