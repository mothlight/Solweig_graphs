package au.edu.monash.mes.envimet;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProcessEDIEDTDirectory
{
	// public static String directory =
	// "/home/nice/Documents/MonashMasters/Research Dissertation/ENVImet40runs/surface/";

	//public static String directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40Validations/BareConcrete-Dres/BareConcrete/surface/";
	public static String directory = "/home/nice/Documents/MonashMasters/Research Dissertation/ENVI31ValidationRuns/output/surface/";
	
	public static boolean summarize = false;
	public static boolean DEBUG = true;
	public boolean RUN_TWICE = true;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String[] ignoredFields =
		{	
			"z Topo (m)"
			, "Surface inclination (°)"
			, "Shadow Flag"	
			, "Surface exposition (°)"
			//, "T Surface (K)"
			//, "T Surface Diff. (K)"
			, "T Surface Change (K/h)"
			, "q Surface (g/kg)"
			, "uv above Surface (m/s)"
			//, "Sensible heat flux (W/m2)"
			, "Exchange Coeff. Heat (m2/s)"
			, "Latent heat flux (W/m2)"
			, "Soil heat Flux (W/m2)"
			, "Sw Direct Radiation (W/m2)"
			, "Sw Diffuse Radiation (W/m2)"
			, "Lambert Factor"
			//, "Longwave Radiation Budget (W/m2)"
			//, "Longwave Rad. from vegetation (W/m2)"
			//, "Longwave Rad. from environment (W/m2)"
			//, "Water Flux (g/(m2s))"
			, "Sky-View-Faktor"
			, "Building Height (m)"
			//, "Surface Albedo"
			, "Deposition Speed (mm/s)"
			, "Mass Deposed (ug/m2)"
			, "z node Biomet"
			, "z Biomet (m)"
			, "T Air Biomet (K)"
			, "q Air Biomet (g/kg)"
			, "TMRT Biomet (K)"
			, "Wind Speed Biomet (m/s)"
			, "Mass Biomet (ug/m3)"
			, "Walkability Value"
			, "Receptors"
		};
		ProcessEDIEDTDirectory processEDIEDTDirectory = new ProcessEDIEDTDirectory();
//		processEDIEDTDirectory.processDirectory(directory, summarize,
//				ignoredFields);
	}

//	public void processDirectory(String directory, boolean summarize,
//			String[] ignoredFields)
//	{
//		TreeSet<String> set = new TreeSet<String>();
//
//		String ediFileType = ".EDI";
//		String edtFileType = ".EDT";
//
//		String[] files = getDirectoryList(directory);
//		for (int i = 0; i < files.length; i++)
//		{
//			String file = files[i];
//
//			String trimmedFilename = file.replaceFirst(ediFileType, "");
//			trimmedFilename = trimmedFilename.replaceFirst(edtFileType, "");
//			System.out.println("trimmedFilename=" + trimmedFilename);
//			System.out.println("trimmedFilename=" + trimmedFilename.subSequence(trimmedFilename.length()-20, trimmedFilename.length()));
//			set.add(trimmedFilename);
//			if (DEBUG)
//			{
//				if (RUN_TWICE)
//				{
//					RUN_TWICE = false;					
//				}
//				else
//				{
//					break;
//				}
//				
//			}
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
//			//System.out.println("fileVariables=" + fileVariables.toString());
//
//			ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
//					+ edtFileType, readEDI);
//
//			// TreeMap<String, ArrayList> edtData = readEDTFile.getData();
//			// for (int i=0;i<fileVariables.size();i++)
//			// {
//			// String variable = fileVariables.get(i);
//			// ArrayList variableData = edtData.get(variable);
//			// System.out.println(variable + "=" + variableData);
//			// }
//
//			// trim ignored fields
//			ArrayList<String> newFileVariables = trimVariablesOfIgnored(readEDI.getFileVariables(), ignoredFields);
//			//System.out.println("newFileVariables=" + newFileVariables.toString());
//			readEDI.setFileVariables(newFileVariables);
//			readEDTFile = trimEDTOfIgnored(readEDTFile, ignoredFields);			
//			//System.out.println("readEDTFile.getData()=" + readEDTFile.getData().toString());
//			
//
//			//store results
//			readEDIArray[count] = readEDI;
//			readEDTArray[count] = readEDTFile;
//			count++;
//
//			//System.out.println("fileVariables=" + readEDI.getFileVariables().toString());
//			// System.out.println("edtData="+edtData.toString());
//
//			//System.exit(1);
//
//		}
//		OutputExcel outputExcel = new OutputExcel();
//		boolean diffTabs = false;
//		boolean hideXY = false;
//
//		if (summarize)
//		{
//			outputExcel.outputEDIEDTSpreadsheetSummary(readEDIArray,
//					readEDTArray, diffTabs, hideXY);
//		} else
//		{
//			outputExcel.outputEDIEDTSpreadsheet(readEDIArray, readEDTArray,
//					diffTabs, hideXY);
//		}
//	}

	ReadEDTFile trimEDTOfIgnored(ReadEDTFile readEDTFile, String[] ignoredFields)
	{
		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		for (int i = 0; i < ignoredFields.length; i++)
		{
			edtData.remove(ignoredFields[i]);
		}
		readEDTFile.setData(edtData);

		return readEDTFile;
	}

	ArrayList<String> trimVariablesOfIgnored(ArrayList<String> fileVariables,
			String[] ignoredFields)
	{

		ArrayList<String> returnVariables = new ArrayList<String>();

		for (int j=0;j<fileVariables.size();j++)
		{
			boolean dropVariable = false;
			String variable = fileVariables.get(j);
			for (int i = 0; i < ignoredFields.length; i++)
			{
				//System.out.println("variable=|" +  variable + "| ignoredFields[i]=|" + ignoredFields[i] + "|");
				
				if (variable.equals(ignoredFields[i]))
				{
					// don't keep it
					dropVariable = true;
					break;
				} 

			}
			if (dropVariable)
			{
				
			}
			else
			{
				returnVariables.add(variable);
			}
			dropVariable = false;
		}
		return returnVariables;
	}

	private String[] getDirectoryList(String directory)
	{
		File dir = new File(directory);
		String[] children = dir.list();
		if (children == null)
		{
			// Either dir does not exist or is not a directory
		} else
		{
			for (int i = 0; i < children.length; i++)
			{
				// Get filename of file or directory
				String filename = children[i];
			}
		}
		FilenameFilter filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				boolean accept = true;
				if (name.contains("##check"))
				{
					accept = false;
				} else if (name.startsWith("."))
				{
					accept = false;
				}
				return accept;
			}
		};
		children = dir.list(filter);
		return children;

	}

}
