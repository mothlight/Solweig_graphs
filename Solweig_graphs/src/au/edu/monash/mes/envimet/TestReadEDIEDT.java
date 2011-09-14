package au.edu.monash.mes.envimet;
/* ====================================================================
Kerry Nice
Monash University
 ==================================================================== */

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestReadEDIEDT {
	
	
	//double x = 54.5;
	//double y = 48.5;
	int runID = 28; // 5, 6, 7, 8, 10, 11
	
	String filename = "MonashCampusValidationNW_AT_14.00.00 25.03.2011";
	String directory = "/mnt/Samsung2TB/EnvimetRuns-5days/Monash/MonashCampusValidationNW/output/atmosphere/";
	String ediFileType = ".EDI";
	String edtFileType = ".EDT";

	ENVICommon common = new ENVICommon();
	public String runDesc;
	String potTemp = "Pot. Temperature (K)";
	
	public static void main(String[] args)
	{
		TestReadEDIEDT testReadEDIEDT = new TestReadEDIEDT();
		//testGraphsFromEDI.process();
		testReadEDIEDT.processATM();
	}
	

	public void processATM()
	{
//		TreeMap<String, String> runMeta = common.getMetaDataForRun(runID);
//
//		System.out.println(runMeta.toString());
//		String dataDir = runMeta.get(common.DATA_DIR);
//		dataDir = dataDir + "../atmosphere/";
//		runMeta.put(common.DATA_DIR, dataDir);
//		dataDir = runMeta.get(common.DATA_DIR);
//		runDesc = runMeta.get(common.RUN_DESC);
//		String enviVersion = runMeta.get(common.ENVI_VERSION);
//		String runName = runMeta.get(common.RUN_NAME);
//
//		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphs";
//		processDirectory(dataDir, runMeta);
		
		ReadEDIFile readEDI = new ReadEDIFile(directory + filename + ediFileType);

		ArrayList<String> fileVariables = readEDI.getFileVariables();

		System.out.println("fileVariables=" + fileVariables.toString());

		ReadEDTFile readEDTFile = new ReadEDTFile();
		readEDTFile.readEDTFileSingleVariable(directory + filename + edtFileType, readEDI, potTemp, ReadEDTFile.X, 1, -9999F);
//
//		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
//		for (int i = 0; i < fileVariables.size(); i++)
//		{
//			String variable = fileVariables.get(i);
//			ArrayList variableData = edtData.get(variable);
//			//System.out.println(variable + "=" + variableData);
////			if (variable.equals("T Surface (K)"))
////			{
////				generateDataFileFromDataPoints(variableData, filename, variable, graphDir);
////			}
//		}	
		
	}
	
//	public void processDirectory(String directory, TreeMap<String, String> runMeta)
//	{
//		TreeSet<String> set = new TreeSet<String>();
//
//		String ediFileType = ".EDI";
//		String edtFileType = ".EDT";
//		String graphDir = runMeta.get(ENVICommon.DATA_DIR) + "../graphs";
//
//		String[] files = common.getDirectoryList(directory);
//		for (int i = 0; i < files.length; i++)
//		{
//			String file = files[i];
//
//			String trimmedFilename = file.replaceFirst(ediFileType, "");
//			trimmedFilename = trimmedFilename.replaceFirst(edtFileType, "");
//			System.out.println("trimmedFilename=" + trimmedFilename);
//			System.out.println("trimmedFilename="
//					+ trimmedFilename.subSequence(
//							trimmedFilename.length() - 20,
//							trimmedFilename.length()));
//			set.add(trimmedFilename);
//
//		}
//
//		ReadEDIFile[] readEDIArray = new ReadEDIFile[set.size()];
//		ReadEDTFile[] readEDTArray = new ReadEDTFile[set.size()];
//		int count = 0;
//		for (String filename : set)
//		{
//			ReadEDIFile readEDI = new ReadEDIFile(directory + filename
//					+ ediFileType);
//
//			ArrayList<String> fileVariables = readEDI.getFileVariables();
//
//			System.out.println("fileVariables=" + fileVariables.toString());
//
//			ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
//					+ edtFileType, readEDI);
//
//			TreeMap<String, ArrayList> edtData = readEDTFile.getData();
//			for (int i = 0; i < fileVariables.size(); i++)
//			{
//				String variable = fileVariables.get(i);
//				ArrayList variableData = edtData.get(variable);
//				//System.out.println(variable + "=" + variableData);
//				if (variable.equals("T Surface (K)"))
//				{
//					generateDataFileFromDataPoints(variableData, filename, variable, graphDir);
//				}
//			}			
//			
//
//
//			// store results
//			readEDIArray[count] = readEDI;
//			readEDTArray[count] = readEDTFile;
//			count++;
//
//
//		}	
	

//	public static void main(String[] args) {
//		
//		String filename = "/home/nice/.wine/drive_c/ENVImetprojects/Monash/Test/surface/MySim_FX_09.00.00 23.06.2010";
//		//String filename = "/home/nice/.wine/drive_c/ENVImetprojects/Monash/Run002/surface/As_is_FX_23.00.00 1.1.2010";
//		String ediFileType = ".EDI";
//		String edtFileType = ".EDT";
//		
//		filename = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVImet40runs/surface/4.0Test_FX_08.00.04 23.06.2010";
//		
//		ReadEDIFile readEDI = new ReadEDIFile(filename + ediFileType);
//		
//		//ArrayList<String> fileVariables = readEDI.getFileVariables();
//		
////		System.out.println("File contents of " +  filename + ediFileType);
////						
////		System.out.println("TitleOfSim=" + readEDI.getTitleOfSim());
////		
////		System.out.println("Title=" + readEDI.getTitle());
////		System.out.println("Time=" + readEDI.getTime());
////		System.out.println("Date=" + readEDI.getDay());
////		System.out.println("Stage=" + readEDI.getStage());
////		
////		System.out.println("NumOfXGrids=" + readEDI.getNumOfXGrids());
////		System.out.println("NumOfYGrids=" + readEDI.getNumOfYGrids());
////		System.out.println("NumOfZGrids=" + readEDI.getNumOfZGrids());
////		System.out.println("NumOfVariablesInFile=" + readEDI.getNumOfVariablesInFile());
////		System.out.println("Variables=" + fileVariables);
////		
////		System.out.println("GridSpacingX=" + readEDI.getGridSpacingX());
////		System.out.println("GridSpacingY=" + readEDI.getGridSpacingY());
////		System.out.println("GridSpacingZ=" + readEDI.getGridSpacingZ());
//		
//		ReadEDTFile readEDTFile = new ReadEDTFile(filename + edtFileType, readEDI);
//		
////		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
////		System.out.println("File contents of " +  filename + edtFileType);
//		
////		for (int i=0;i<fileVariables.size();i++)
////		{
////			String variable = fileVariables.get(i);			
////			ArrayList variableData = edtData.get(variable);			
//////			System.out.println(variable + "=" + variableData);
////		}
//		
//		OutputExcel outputExcel = new OutputExcel();
//		boolean diffTabs = false;
//		boolean hideXY = false;
//		outputExcel.outputEDIEDTSpreadsheet(readEDI, readEDTFile, filename, diffTabs, hideXY);
//		
//	}

}
