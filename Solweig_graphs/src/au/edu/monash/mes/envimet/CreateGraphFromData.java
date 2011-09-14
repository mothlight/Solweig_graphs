package au.edu.monash.mes.envimet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jgnuplot.Axes;
import org.jgnuplot.Graph;
import org.jgnuplot.LineType;
import org.jgnuplot.Splot;
import org.jgnuplot.PointType;
import org.jgnuplot.Style;
import org.jgnuplot.Terminal;

public class CreateGraphFromData
{

	// public static String directory =
	// "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI31ValidationRuns/output/surface/";
	// public static String filename = "BareFtCtMel_FX_13.00.00 22.01.2011";
//	String ediFileType = ".EDI";
//	String edtFileType = ".EDT";
	//public static String envi_directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI31ValidationRuns/output/surface/";
	//public static String envi_directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVImet40runs/surface/";
	//public static String envi_directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40Validations/BareConcrete-Dres/BareConcrete/surface/";
	//public static String envi_directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40ValidationRuns/surface/";
	public static String envi_directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40ValidationRuns/BareFlatAsphaltMel/surface/";
	//public static String outputDirectory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40ValidationRuns/BareFlatAsphaltMel/graphs";
//	public static String outputDirectory = "/tmp/envimet";
	public TreeMap<String, Integer> getVariableTypeMapping()
	{
		return variableTypeMapping;
	}

	public void setVariableTypeMapping(TreeMap<String, Integer> variableTypeMapping)
	{
		this.variableTypeMapping = variableTypeMapping;
	}

	public Set<String> getVariableTypeMappingKeys()
	{
		return variableTypeMappingKeys;
	}

	public void setVariableTypeMappingKeys(Set<String> variableTypeMappingKeys)
	{
		this.variableTypeMappingKeys = variableTypeMappingKeys;
	}

	public TreeMap<String, Integer> variableTypeMapping;
	public Set<String> variableTypeMappingKeys;
	
	ENVICommon common = new ENVICommon();

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		CreateGraphFromData create = new CreateGraphFromData();
		// create.processFile(CreateGraphFromData.directory,
		// CreateGraphFromData.filename);
		
		
		//create.processDirectory(envi_directory);
		//create.processDBData(5, ENVICommon.T_Surface_K);
		create.processAllDBDataForRunAsManyGraphs(6);
		
		
	}

	public CreateGraphFromData()
	{		
		super();
		variableTypeMapping = common.getDataVariableTypes();
		variableTypeMappingKeys = variableTypeMapping.keySet();
	}
	
	public void processAllDBDataForRunAsManyGraphs(int runID)
	{
		ArrayList<Integer> variableTypes = common.getDistinctVariableTypesForRun(runID);
		
		for (Integer variableType : variableTypes)
		{			
			processDBDataAllVariables(runID, variableType);			
		}
	}
	
	public void processDBDataAllVariables(int runID, int variable)
	{
		TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, variable);
		generateDataFileFromDataPoints(data, variable, runID);
		
	}	
	
//	public void processDBData(int runID, int variable)
//	{
//		TreeMap<String, ArrayList<DataPoint>> data = common.getVariableDataFromDB(runID, variable);
//		//generateDataFileFromDataPoints(data);
//		generateSingleDataFileFromDataPoints(data);
//	}
	


//	public void processDirectory(String directory)
//	{
//		TreeSet<String> set = new TreeSet<String>();
//
//		String[] files = common.getDirectoryList(directory);
//		for (int i = 0; i < files.length; i++)
//		{
//			String file = files[i];
//
//			String trimmedFilename = file.replaceFirst(common.ediFileType, "");
//			trimmedFilename = trimmedFilename.replaceFirst(common.edtFileType, "");
//			System.out.println("trimmedFilename=" + trimmedFilename);
//			System.out.println("trimmedFilename="
//					+ trimmedFilename.subSequence(
//							trimmedFilename.length() - 20,
//							trimmedFilename.length()));
//			set.add(trimmedFilename);
//
//		}
//
//		for (String filename : set)
//		{
//			processFile(directory, filename);
//
//		}
//	}

	public void processFile(String directory, String filename, int runID)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);		
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		
		// String dataVariable = "T Surface (K)";

		ReadEDIFile readEDI = new ReadEDIFile(directory + filename
				+ common.ediFileType);

		ArrayList<String> fileVariables = readEDI.getFileVariables();

		System.out.println("fileVariables=" + fileVariables.toString());

		ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
				+ common.edtFileType, readEDI);

		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		// for (int i = 0; i < fileVariables.size(); i++)
		// {
		// String variable = fileVariables.get(i);
		// ArrayList variableData = edtData.get(variable);
		// System.out.println(variable + "=" + variableData);
		// }

		for (String dataVariable : fileVariables)
		{

			ArrayList surfaceTemp = edtData.get(dataVariable);
			StringBuffer outputStr = new StringBuffer();
			for (int i = 0; i < surfaceTemp.size(); i++)
			{
				ArrayList temps = (ArrayList) surfaceTemp.get(i);
				outputStr.append(temps.get(0) + " " + temps.get(1) + " "
						+ temps.get(2) + '\n');
				System.out.println(temps.get(0) + " " + temps.get(1) + " "
						+ temps.get(2));
			}
			
			String trimmedDataVariable = dataVariable.replaceAll("/", "_");

			String fullFilename = trimmedDataVariable + "_" + filename;

			String dataFileName = outputDirectory + File.separator
					+ File.separator + trimmedDataVariable + ".dat";
			common.writeFile(outputStr.toString(), dataFileName);

			String outputFile = outputDirectory + File.separator + fullFilename
					+ ".png";
			String xLabel = "Data from " + filename + " of " + dataVariable;
			plotData(dataFileName, outputFile, xLabel, outputDirectory);

		}

	}
	
	public void generateSingleDataFileFromDataPoints(TreeMap<String, ArrayList<DataPoint>> data, int runID)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);		
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		
		StringBuffer outputStr = new StringBuffer();
		String filename = null;
		
		Set<String> keys = data.keySet();
		System.out.println(keys.toString());
		String xLabel = null;
		String dataFileName = null;
		String outputFile = null;
		int count = 0;
		
		for (String key : keys)
		{
			ArrayList<DataPoint> dataPoints = data.get(key);
			
			for (DataPoint dataPoint : dataPoints)
			{
				outputStr.append(dataPoint.getX() + " " + dataPoint.getY() + " " + dataPoint.getValue() + '\n');
				if (count == 0)
				{					
					filename = dataPoint.getOutputFile();
				}
			}
			
			if (count == 0)
			{
				dataFileName = outputDirectory + File.separator + filename + ".dat";
			}
			

			if (count == 0)
			{
				outputFile = outputDirectory + File.separator + filename + ".png";
				xLabel = "Data from " + filename;
			}
			
			count ++;
		}	
		common.writeFile(outputStr.toString(), dataFileName);
		plotData(dataFileName, outputFile, xLabel, outputDirectory);
	}	
	
	public void generateDataFileFromDataPoints(TreeMap<String, ArrayList<DataPoint>> data, int variable, int runID)
	{
		
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);		
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		
		String variableName = "" + variable;
		
		for (String key : variableTypeMappingKeys)
		{
			if (variableTypeMapping.get(key).equals(variable))
			{
				variableName = key;
			}
		}
		
		
		StringBuffer outputStr = new StringBuffer();
		String filename = null;
		
		Set<String> keys = data.keySet();
		System.out.println(keys.toString());
		
		for (String key : keys)
		{
			ArrayList<DataPoint> dataPoints = data.get(key);
			
			for (DataPoint dataPoint : dataPoints)
			{
				outputStr.append(dataPoint.getX() + " " + dataPoint.getY() + " " + dataPoint.getValue() + '\n');
				String outputFilename = dataPoint.getOutputFile();
								
				String year = outputFilename.substring(outputFilename.length()-4, outputFilename.length());
				String month = outputFilename.substring(outputFilename.length()-7, outputFilename.length()-5);
				String day = outputFilename.substring(outputFilename.length()-10, outputFilename.length()-8);
				String hour = outputFilename.substring(outputFilename.length()-16, outputFilename.length()-14);
				String minute = outputFilename.substring(outputFilename.length()-19, outputFilename.length()-17);
				
				String oldFilename = outputFilename.substring(0, outputFilename.length()-20);
				
				System.out.println(oldFilename + "_"+ year + "-" + month + "-" + day + "_" + hour + ":" + minute);
				
				//filename = oldFilename + "_"+ year + "-" + month + "-" + day + "_" + hour + ":" + minute + "_" + variableName;
				filename = common.removeIllegalCharacters(variableName) + "_"+ year + "-" + month + "-" + day + "_" + hour + ":" + minute + "_" +  oldFilename  ;
			}
			
			String dataFileName = outputDirectory + File.separator + filename + ".dat";
			common.writeFile(outputStr.toString(), dataFileName);

			String outputFile = outputDirectory + File.separator + filename
						+ ".png";
			String xLabel = "Data from " + filename;
			plotData(dataFileName, outputFile, xLabel, outputDirectory);
		}		
	}

//	public void writeFile(String text, String filename)
//	{
//		FileOutputStream out; // declare a file output object
//		PrintStream p; // declare a print stream object
//
//		try
//		{
//			out = new FileOutputStream(filename);
//			p = new PrintStream(out);
//			p.println(text);
//			p.close();
//		} catch (Exception e)
//		{
//			System.err.println("Error writing to file");
//		}
//
//	}

	public void plotData(String dataFileName, String outputFile, String xLabel, String outputDirectory)
	{
		/*
		 * set title "Simple demo of scatter data conversion to grid data" unset
		 * hidden3d set ticslevel 0.5 set view 60,30 set autoscale set
		 * parametric set style data points set xlabel
		 * "data style point - no dgrid" set key box splot "concrete.dat"
		 * 
		 * pause -1 "Hit return to continue (1)"
		 * 
		 * set dgrid3d 10,10,1 set xlabel " data style lines, dgrid3d 10,10,1"
		 * set style data lines #splot "hemisphr.dat" splot "concrete.dat"
		 */

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
		// aPlot.addExtra("set xlabel \"data style point - no dgrid\"");
		aPlot.addExtra("set key box");

		aPlot.addExtra("set dgrid3d 10,10,1");
		aPlot.addExtra("set xlabel \" " + xLabel + "\"");
		aPlot.addExtra("set style data lines");

		aPlot.setDataFileName(dataFileName);

		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");
		try
		{
			aPlot.plot();
			
			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);
			

		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}
		
		aPlot.addExtra("set size square");
		aPlot.addExtra("set pm3d map");
		aPlot.addExtra("set palette rgbformulae 22,13,10");
		aPlot.addExtra("set autoscale");
		aPlot.setOutput(Terminal.PNG, outputFile + "_c", " 1024,600  small ");
		try
		{
			aPlot.plot();
			
			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);
			

		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}
		
		aPlot.addExtra("set palette gray");
		aPlot.addExtra("set samples 100; set isosamples 100");

		aPlot.setOutput(Terminal.PNG, outputFile + "_c_g", " 1024,600  small ");
		try
		{
			aPlot.plot();
			
			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);
			

		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}

	}

	// public void gnuplot()
	// {
	// Splot.setGnuplotExecutable("gnuplot");
	// Splot.setPlotDirectory(outputDirectory);
	//
	// Splot aPlot = new Splot();
	// aPlot.setKey("left box");
	// aPlot.setSamples("50");
	// aPlot.setRanges("[-10:10]");
	// aPlot.pushGraph(new Graph("sin(x)"));
	// aPlot.pushGraph(new Graph("atan(x)"));
	// aPlot.pushGraph(new Graph("cos(atan(x))"));
	//
	// aPlot.setOutput(Terminal.PNG, "/tmp/simple-01.png");
	// try
	// {
	// aPlot.plot();
	// } catch (Exception e)
	// {
	// // System.err.println(e);
	// e.printStackTrace();
	// System.exit(1);
	// }
	//
	// }



}
