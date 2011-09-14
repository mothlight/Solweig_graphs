package au.edu.monash.mes.envimet;
/* ====================================================================
Kerry Nice
Monash University
 ==================================================================== */

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

/*
 The data files (.EDT) are simple binary files of type "float" with a single precision.

 The file are written with the following loop logic

 For z:= 0 to ZZ
 For y:=1 to YY
 For x:=1 to XX
 {
 Write Value[x][y][z]
 }

 with XX,YY and ZZ being the dimensions of the model domain. 
 */

public class ReadEDTFile
{
	public static final String X = "x";
	public static final String Y = "y";
	public static final String Z = "z";

	private TreeMap data;

	public TreeMap getData()
	{
		return data;
	}

	public void setData(TreeMap data)
	{
		this.data = data;
	}
	
	public ReadEDTFile()
	{
		super();		
	}	

	public ReadEDTFile(String filename, ReadEDIFile readEDIFile)
	{
		super();
		readEDTFile(filename, readEDIFile);
	}
	
	public void readEDTFileSpecificVariablesSpecificPoint(String filename, ReadEDIFile readEDIFile, ArrayList<String> variablesToObtain
			, String cut, int cutValue, float ignoreValue, double xGridPoint, double yGridPoint)
	{

		//MonashCampusValidationNW_AT_00.00.00 23.03.2011.EDI
		String date = filename.substring(filename.length() -14, filename.length() -4); 
		String time = filename.substring(filename.length() -23, filename.length() - 15);		
		System.out.println("");
		System.out.println("date=" + date + " time=" + time);
		
		double startingX = 2.5;
		double startingY = 2.5;
		double startingZ = 0.0;
		
		double currentGridX = startingX;
		double currentGridY = startingY;
		double currentGridZ = startingZ;
		
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		ArrayList<String> gridSpacingX = readEDIFile.getGridSpacingX();
		ArrayList<String> gridSpacingY = readEDIFile.getGridSpacingY();
		ArrayList<String> gridSpacingZ = readEDIFile.getGridSpacingZ();

		data = new TreeMap<String, ArrayList>();
		ArrayList<String> fileVariables = readEDIFile.getFileVariables();

		try
		{			
			DecimalFormat df4 = new DecimalFormat( "#.####" );
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			int numOfVariablesInFile = readEDIFile.getNumOfVariablesInFile();
			
			for (int i = 0; i < numOfVariablesInFile; i++)
			{
				ArrayList variableData = new ArrayList<Float>();
				String variableName = fileVariables.get(i);
				//if (variableName.equals(variableToObtain))
				if (variablesToObtain.contains(variableName))
				{
					//System.out.println(variableName + " found");			

					for (int z = 0; z < readEDIFile.getNumOfZGrids(); z++)
					{					
						for (int y = 0; y < readEDIFile.getNumOfYGrids(); y++)
						{						
							for (int x = 0; x < readEDIFile.getNumOfXGrids(); x++)
							{
								Float floatData = dis.readFloat();		
								float floatSwapped = ReadEDTFile.swap(floatData); 
								String formatedFloat = df4.format(floatSwapped);
								
								ArrayList xyPointData = null;
								if (cut == null)
								{
									xyPointData = new ArrayList();									
								}
								else 
								{
									if (cut.equals(X))
									{										
										if (cutValue == x)
										{
											xyPointData = new ArrayList();											
										}
									}
									else if (cut.equals(Y))
									{
										if (cutValue == y)
										{
											xyPointData = new ArrayList();											
										}										
									}
									if (cut.equals(Z))
									{
										if (cutValue == z)
										{
											//System.out.println (variableName + " x="+ currentGridX + " y=" + currentGridY  + " z=" + currentGridZ +  " value=" + formatedFloat);
											if (x == xGridPoint && y == yGridPoint)
											{
												xyPointData = new ArrayList();
											}
										}
									}
								}
								
								if (xyPointData != null)
								{
									xyPointData.add(currentGridX);
									xyPointData.add(currentGridY);
									xyPointData.add(formatedFloat);
									xyPointData.add(currentGridZ);
									if (floatSwapped != ignoreValue)
									{
										variableData.add(xyPointData);
									}
									System.out.println (variableName + " x="+ currentGridX + " y=" + currentGridY  + " z=" + currentGridZ +  " value=" + formatedFloat);
								}
								
								
								String nextGridXStr = (String)gridSpacingX.get(x);
								Float nextGridX = new Float(nextGridXStr).floatValue();							
								currentGridX = currentGridX + nextGridX;
							}
							String nextGridYStr = (String)gridSpacingY.get(y);
							Float nextGridY = new Float(nextGridYStr).floatValue();
							currentGridY = currentGridY + nextGridY;
						
							currentGridX = startingX;
						}
						String nextGridZStr = (String)gridSpacingZ.get(z);
						Float nextGridZ = new Float(nextGridZStr).floatValue();
						currentGridZ = currentGridZ + nextGridZ;
					
						currentGridY = startingY;
					
					}	
					currentGridZ = startingZ;
					data.put(variableName, variableData);
					//System.out.println("End of " + variableName);
				}
				else
				{
					int numberInVariable = readEDIFile.getNumOfZGrids() * readEDIFile.getNumOfYGrids() * readEDIFile.getNumOfXGrids();
					for (int useUp=0;useUp<numberInVariable;useUp++)
					{
						dis.readFloat();
					}			
				}	
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}	
	
	public void readEDTFileSingleVariable(String filename, ReadEDIFile readEDIFile, String variableToObtain, String cut, int cutValue, float ignoreValue)
	{
		double startingX = 2.5;
		double startingY = 2.5;
		double startingZ = 0.0;
		
		double currentGridX = startingX;
		double currentGridY = startingY;
		double currentGridZ = startingZ;
		
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		ArrayList<String> gridSpacingX = readEDIFile.getGridSpacingX();
		ArrayList<String> gridSpacingY = readEDIFile.getGridSpacingY();
		ArrayList<String> gridSpacingZ = readEDIFile.getGridSpacingZ();

		data = new TreeMap<String, ArrayList>();
		ArrayList<String> fileVariables = readEDIFile.getFileVariables();

		try
		{			
			DecimalFormat df4 = new DecimalFormat( "#.####" );
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			int numOfVariablesInFile = readEDIFile.getNumOfVariablesInFile();
			
			for (int i = 0; i < numOfVariablesInFile; i++)
			{
				ArrayList variableData = new ArrayList<Float>();
				String variableName = fileVariables.get(i);
				if (variableName.equals(variableToObtain))
				{
					System.out.println(variableToObtain + " found");			

					for (int z = 0; z < readEDIFile.getNumOfZGrids(); z++)
					{
					
						for (int y = 0; y < readEDIFile.getNumOfYGrids(); y++)
						{						
							for (int x = 0; x < readEDIFile.getNumOfXGrids(); x++)
							{
								Float floatData = dis.readFloat();		
								float floatSwapped = ReadEDTFile.swap(floatData); 
								String formatedFloat = df4.format(floatSwapped);
								
								
								ArrayList xyPointData = null;
								if (cut == null)
								{
									xyPointData = new ArrayList();									
								}
								else 
								{
									if (cut.equals(X))
									{										
										if (cutValue == x)
										{
											xyPointData = new ArrayList();											
										}
									}
									else if (cut.equals(Y))
									{
										if (cutValue == y)
										{
											xyPointData = new ArrayList();											
										}
									}
									if (cut.equals(Z))
									{
										if (cutValue == z)
										{
											xyPointData = new ArrayList();											
										}										
									}
								}
								
								if (xyPointData != null)
								{
									xyPointData.add(currentGridX);
									xyPointData.add(currentGridY);
									xyPointData.add(formatedFloat);
									xyPointData.add(currentGridZ);
									if (floatSwapped != ignoreValue)
									{
										variableData.add(xyPointData);
									}
									System.out.println (variableName + " x="+ currentGridX + " y=" + currentGridY  + " z=" + currentGridZ +  " value=" + formatedFloat);
								}
								
								
								String nextGridXStr = (String)gridSpacingX.get(x);
								Float nextGridX = new Float(nextGridXStr).floatValue();							
								currentGridX = currentGridX + nextGridX;
							}
							String nextGridYStr = (String)gridSpacingY.get(y);
							Float nextGridY = new Float(nextGridYStr).floatValue();
							currentGridY = currentGridY + nextGridY;
						
							currentGridX = startingX;
						}
						String nextGridZStr = (String)gridSpacingZ.get(z);
						Float nextGridZ = new Float(nextGridZStr).floatValue();
						currentGridZ = currentGridZ + nextGridZ;
					
						currentGridY = startingY;
					
					}				
					data.put(variableName, variableData);
					//System.out.println("End of " + variableName);
				}
				else
				{
					int numberInVariable = readEDIFile.getNumOfZGrids() * readEDIFile.getNumOfYGrids() * readEDIFile.getNumOfXGrids();
					for (int useUp=0;useUp<numberInVariable;useUp++)
					{
						dis.readFloat();
					}			
				}	
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}	

	public void readEDTFile(String filename, ReadEDIFile readEDIFile)
	{
		double startingX = 2.5;
		double startingY = 2.5;
		double startingZ = 0.0;
		
		double currentGridX = startingX;
		double currentGridY = startingY;
		double currentGridZ = startingZ;
		
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		
		ArrayList<String> gridSpacingX = readEDIFile.getGridSpacingX();
		ArrayList<String> gridSpacingY = readEDIFile.getGridSpacingY();
		ArrayList<String> gridSpacingZ = readEDIFile.getGridSpacingZ();

		data = new TreeMap<String, ArrayList>();
		ArrayList<String> fileVariables = readEDIFile.getFileVariables();

		try
		{			
			DecimalFormat df4 = new DecimalFormat( "#.####" );
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			
			for (int i = 0; i < readEDIFile.getNumOfVariablesInFile(); i++)
			{
				ArrayList variableData = new ArrayList<Float>();
				String variableName = fileVariables.get(i);

				for (int z = 0; z < readEDIFile.getNumOfZGrids(); z++)
				{
					
					for (int y = 0; y < readEDIFile.getNumOfYGrids(); y++)
					{						
						for (int x = 0; x < readEDIFile.getNumOfXGrids(); x++)
						{
							Float floatData = dis.readFloat();		
							float floatSwapped = ReadEDTFile.swap(floatData); 
							String formatedFloat = df4.format(floatSwapped);	
							ArrayList xyPointData = new ArrayList();
							xyPointData.add(currentGridX);
							xyPointData.add(currentGridY);
							xyPointData.add(formatedFloat);
							xyPointData.add(currentGridZ);
							
							variableData.add(xyPointData);
							//System.out.println ("x="+ currentGridX + " y=" + currentGridY + " value=" + formatedFloat);
							String nextGridXStr = (String)gridSpacingX.get(x);
							Float nextGridX = new Float(nextGridXStr).floatValue();							
							currentGridX = currentGridX + nextGridX;
						}
						String nextGridYStr = (String)gridSpacingY.get(y);
						Float nextGridY = new Float(nextGridYStr).floatValue();
						currentGridY = currentGridY + nextGridY;
						
						currentGridX = startingX;
					}
					String nextGridZStr = (String)gridSpacingZ.get(z);
					Float nextGridZ = new Float(nextGridZStr).floatValue();
					currentGridZ = currentGridZ + nextGridZ;
					
					currentGridY = startingY;
					
				}				
				data.put(variableName, variableData);
				//System.out.println("End of " + variableName);
			}

			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	  /**
	   * Byte swap a single float value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static float swap (float value)
	  {
	    int intValue = Float.floatToIntBits (value);
	    intValue = swap (intValue);
	    return Float.intBitsToFloat (intValue);
	  }
	  
	  /**
	   * Byte swap a single int value.
	   * 
	   * @param value  Value to byte swap.
	   * @return       Byte swapped representation.
	   */
	  public static int swap (int value)
	  {
	    int b1 = (value >>  0) & 0xff;
	    int b2 = (value >>  8) & 0xff;
	    int b3 = (value >> 16) & 0xff;
	    int b4 = (value >> 24) & 0xff;

	    return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	  }	  


}
