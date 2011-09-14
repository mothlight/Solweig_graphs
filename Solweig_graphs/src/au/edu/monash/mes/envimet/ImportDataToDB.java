package au.edu.monash.mes.envimet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class ImportDataToDB
{
	ENVICommon common = new ENVICommon();

	public ImportDataToDB()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		int runID;
		ImportDataToDB importData = new ImportDataToDB();
		
		//String query = "select run_id,data_dir from runs";
		
//		runID = 8;
//		System.out.println("Starting runID = " + runID);
//		importData.processDirectory(runID);

		
		//importData.updateCFandIN(8);
		

	}
	
	public int insertNewRun(String dataDirectory, int enviVersion)
	{
		// insert into runs (run_id, run_date,run_desc,run_name,envi_version,data_dir)
		// values (8,'2011-02-06','Bare flat concrete Melbourne, normal conditions,2 day run with receptors, v4','BareFlatConcreteMelRec-2days',4,'/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40ValidationRuns/BareFlatConcreteMel-2days/surface/')

		File dataDirectoryFile = new File(dataDirectory);
		long dataFileLong = dataDirectoryFile.lastModified();
		java.sql.Date runDate = new java.sql.Date(dataFileLong);
		System.out.println(runDate.toString());

		
		String modelDescription = null;
		String runName = null;
		int runID = 0;
		
		String cfFile = getConfigFileLocation(dataDirectory, common.cfFileType);
		String inFile = getConfigFileLocation(dataDirectory, common.inFileType);
		
		File inFileFile = new File(inFile);
		File cfFileFile = new File(cfFile);
		FileInputStream fisCfFile = null;
		FileInputStream fisInFile = null;
		try
		{
			fisInFile = new FileInputStream(inFileFile);
			BufferedReader inFileReader = new BufferedReader(new InputStreamReader(fisInFile));

			String line = inFileReader.readLine();
			line = inFileReader.readLine();
			modelDescription = inFileReader.readLine();
			System.out.println(modelDescription);
			
			fisCfFile = new FileInputStream(cfFileFile);
			BufferedReader cfFileReader = new BufferedReader(new InputStreamReader(fisCfFile));

			line = cfFileReader.readLine();
			line = cfFileReader.readLine();
			line = cfFileReader.readLine();
			line = cfFileReader.readLine();
			line = cfFileReader.readLine();
			line = cfFileReader.readLine();
			
			
			StringTokenizer st = new StringTokenizer(line,"=");
			String value = st.nextToken();
			runName = st.nextToken();
			
			System.out.println(runName);			
						
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
//		String receptorsFilePath = "../receptors/xx/";
		
		String query = "insert into runs (run_date,run_desc,run_name,envi_version,data_dir) values (?,?,?,?,?)";	

		Connection conn = null;
		
		PreparedStatement pstmt = null;
		try
		{
			conn = common.getMySqlConnection();		

			pstmt = conn.prepareStatement(query);

			pstmt.setDate(1, (java.sql.Date) runDate);
			pstmt.setString(2, modelDescription);
			pstmt.setString(3, runName);
			pstmt.setInt(4, enviVersion);
			pstmt.setString(5, dataDirectory);
						
			pstmt.execute();
			
			query = "select run_id from runs where data_dir ='" + dataDirectory + "'";
			Statement statement = conn.prepareStatement(query);
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				runID = rs.getInt("run_id");
			}
			rs.close();
			pstmt.close();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			conn.close();
		} catch (SQLException e)
		{			
			e.printStackTrace();
		}
				
		updateCFandIN(dataDirectory, runID);
		
		return runID;
	}
	
	public void updateCFandIN(int runID)
	{
		TreeMap<String, String> runMeta = common.getMetaDataForRun(runID);
		String dataDir = runMeta.get(ENVICommon.DATA_DIR);
		updateCFandIN(dataDir, runID);		
	}
	
	public String getConfigFileLocation(String directory, String fileExtension)
	{
		 
		String configFile = null;
				
		//   	/home/nice/Documents/MonashMasters/Research Dissertation/ENVI31ValidationRuns/output/surface/
		// assumption is that input files will be in ../../input/
		
		directory = directory + "../../input/";
				
		String[] files = common.getDirectoryList(directory);
		for (int i = 0; i < files.length; i++)
		{
			String file = files[i];

			System.out.println("file=" + file);
			if (file.endsWith(fileExtension))
			{
				configFile = directory + file;
			}

		}
		
		System.out.println(fileExtension + " file=" + configFile);
		return configFile;
	}
	
	public void updateCFandIN(String cfFile, String inFile, int runID)
	{		
		String query = "update runs set run_cf = ?, run_in = ? where run_id = ?";
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		try
		{
			conn = common.getMySqlConnection();		

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(3, runID);
		
			File cfFileBlob = new File (cfFile);
			FileInputStream isCF = new FileInputStream (cfFileBlob);
			pstmt.setBinaryStream (1, isCF, (int) cfFileBlob.length() );
		
			File inFileBlob = new File (inFile);
			FileInputStream isIN = new FileInputStream (inFileBlob);
			pstmt.setBinaryStream (2, isIN, (int) inFileBlob.length() );
				
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			conn.close();
		} catch (SQLException e)
		{			
			e.printStackTrace();
		}
		
	}	
	
	public void updateCFandIN(String directory, int runID)
	{
		String cfFile = null;
		String inFile = null;
		
		//   	/home/nice/Documents/MonashMasters/Research Dissertation/ENVI31ValidationRuns/output/surface/
		// assumption is that input files will be in ../../input/
		
		directory = directory + "../../input/";
		//directory = directory + "../input/";
		
		String[] files = common.getDirectoryList(directory);
		for (int i = 0; i < files.length; i++)
		{
			String file = files[i];

			System.out.println("file=" + file);
			if (file.endsWith(common.cfFileType))
			{
				cfFile = directory + file;
			}
			if (file.endsWith(common.inFileType))
			{
				inFile = directory + file;
			}
		}
		
		System.out.println("cfFile=" + cfFile);
		System.out.println("inFile=" + inFile);
		String query = "update runs set run_cf = ?, run_in = ? where run_id = ?";
		Connection conn = null;
		
		PreparedStatement pstmt = null;
		try
		{
			conn = common.getMySqlConnection();		

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(3, runID);
		
			File cfFileBlob = new File (cfFile);
			FileInputStream isCF = new FileInputStream (cfFileBlob);
			pstmt.setBinaryStream (1, isCF, (int) cfFileBlob.length() );
		
			File inFileBlob = new File (inFile);
			FileInputStream isIN = new FileInputStream (inFileBlob);
			pstmt.setBinaryStream (2, isIN, (int) inFileBlob.length() );
				
			pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			conn.close();
		} catch (SQLException e)
		{			
			e.printStackTrace();
		}
		
	}
	
	
	
	public void processDirectory(int runID)
	{
		String query = "select run_id,data_dir from runs where run_id=" +
				runID;
		String directory = getRunDataDir(query);		
		
		TreeSet<String> set = new TreeSet<String>();

		String[] files = common.getDirectoryList(directory);
		for (int i = 0; i < files.length; i++)
		{
			String file = files[i];

			String trimmedFilename = file.replaceFirst(common.ediFileType, "");
			trimmedFilename = trimmedFilename.replaceFirst(common.edtFileType, "");
			System.out.println("trimmedFilename=" + trimmedFilename);
			System.out.println("trimmedFilename="
					+ trimmedFilename.subSequence(
							trimmedFilename.length() - 20,
							trimmedFilename.length()));
			set.add(trimmedFilename);
			
			System.out.println ("parsedDateTime="  + parseDateTime(trimmedFilename));

		}

		for (String filename : set)
		{
			processFile(directory, filename, runID);

		}
	}
	
	public String parseDateTime(String trimmedFilename)
	{
		trimmedFilename = trimmedFilename.substring(
				trimmedFilename.length() - 20,
				trimmedFilename.length());
		String parsedStr;
		//trimmedFilename=_00.00.04 27.01.2011
		// to 'YYYY-MM-DD HH:MM:SS'
		parsedStr = trimmedFilename			
					.substring(16, 20)
					+ "-"
					+ trimmedFilename.substring(13, 15)
					+ "-"
					+ trimmedFilename.substring(10, 12)
					+ " "
					+ trimmedFilename.substring(1, 3)
					+ ":"
					+ trimmedFilename.substring(4, 6)
					+ ":"
					+ trimmedFilename.substring(7, 9);
		
		
		
		return parsedStr;
	}
	
	public Timestamp parseDateTimeToTimestamp(String trimmedFilename)
	{
		trimmedFilename = trimmedFilename.substring(
				trimmedFilename.length() - 20,
				trimmedFilename.length());
		String parsedStr;
		//trimmedFilename=_00.00.04 27.01.2011
		// to 'YYYY-MM-DD HH:MM:SS'
		parsedStr = trimmedFilename.substring(16, 20)  //YYYY
					+ "-"
					+ trimmedFilename.substring(13, 15) //MM
					+ "-"
					+ trimmedFilename.substring(10, 12) // DD
					+ " "
					+ trimmedFilename.substring(1, 3) //HH
					+ ":"
					+ trimmedFilename.substring(4, 6) //MM
					+ ":"
					+ trimmedFilename.substring(7, 9); //SS
		
		//System.out.println("parsedStr=" + parsedStr);
		
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =	null;
		try
		{
			date = sdf.parse(parsedStr);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		return new Timestamp(cal.getTimeInMillis());
	}
	
//	public TreeMap<String, Integer> getDataVariableTypes()
//	{
//		TreeMap<String, Integer> variables = new TreeMap<String, Integer>();
//		String query = "select variable_id, variable_name from run_variables ";
//
//		Connection conn = common.getMySqlConnection();		
//		try
//		{
//			Statement stat = conn.createStatement();
//
//			ResultSet rs = stat.executeQuery(query);
//			while (rs.next())
//			{
//				Integer variableID = rs.getInt("variable_id");
//				String variableName = rs.getString("variable_name");
//				
//				variables.put(variableName, variableID);
//			}
//			rs.close();
//			conn.close();
//
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		System.out.println(variables.toString());
//		return variables;
//	}
	
	public TreeMap<String, Integer> addDataVariableToDB(String dataVariable)
	{
		
		String query = " insert into run_variables (variable_name, variable_id) values ('" + dataVariable +
				"', null)  ";
		
		Connection conn = common.getMySqlConnection();		
		try
		{
			Statement stat = conn.createStatement();
			System.out.println(query);
			stat.execute(query);					
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return common.getDataVariableTypes();
		
	}
	
	public boolean executeInsert(String insert)
	{
		boolean success = false;
		
		Connection conn = common.getMySqlConnection();		
		try
		{
			Statement stat = conn.createStatement();
			//System.out.println(insert);
			success = stat.execute(insert);					
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}		
		
		return success;
	}

	public void processFile(String directory, String filename, int runID)
	{
		Connection conn = common.getMySqlConnection();	
		TreeMap<String, Integer> dbVariableTypes = common.getDataVariableTypes();
		
		// String dataVariable = "T Surface (K)";
		float z = 0.0f;

		ReadEDIFile readEDI = new ReadEDIFile(directory + filename
				+ common.ediFileType);

		ArrayList<String> fileVariables = readEDI.getFileVariables();

		//System.out.println("fileVariables=" + fileVariables.toString());

		ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
				+ common.edtFileType, readEDI);

		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		// for (int i = 0; i < fileVariables.size(); i++)
		// {
		// String variable = fileVariables.get(i);
		// ArrayList variableData = edtData.get(variable);
		// System.out.println(variable + "=" + variableData);
		// }
		
		
		
		String insertStatementString = "insert into run_data (run_id, output_file, variable_type, x, y, z, value, variable_time) values" +
		"(" +
		//runID +
		 "?" +
		" , " +
		//filename
		"?"
		+ " , " 
		//+ dataVariableID
		+ "?"
		+ " , " 
		//+ temps.get(0)
		+ "?"
		+ "," 
		//+ temps.get(1)
		+ "?"
		+ " , "
		//+ z
		+ "?"
		+ ", "
		//+ temps.get(2)
		+ "?"
		+ ", " 
		+ " ?)";		
		System.out.println(insertStatementString);
		//System.exit(1);
		
		PreparedStatement stat = null;
		try
		{
			stat = conn.prepareStatement(insertStatementString);
		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		

		for (String dataVariable : fileVariables)
		{
			System.out.println("starting to process " + dataVariable);
			Integer dataVariableID = null;
			
			if (dbVariableTypes.containsKey(dataVariable))
			{
				dataVariableID = dbVariableTypes.get(dataVariable);
			}
			else
			{
				dbVariableTypes = addDataVariableToDB(dataVariable);				
			}

			ArrayList dataForVariable = edtData.get(dataVariable);
			//StringBuffer outputStr = new StringBuffer();
			for (int i = 0; i < dataForVariable.size(); i++)
			{
				ArrayList temps = (ArrayList) dataForVariable.get(i);
				//outputStr.append(temps.get(0) + " " + temps.get(1) + " "
				//		+ temps.get(2) + '\n');
				//System.out.println(temps.get(0) + " " + temps.get(1) + " "
				//		+ temps.get(2));
				
//				String insertString = "insert into run_data (run_id, output_file, variable_type, x, y, z, value, variable_time) values" +
//						"(" +
//						runID +
//						" , '" +
//						filename
//						+ "' , " 
//						+ dataVariableID
//						+ " , " 
//						+ temps.get(0)
//						+ "," 
//						+ temps.get(1)
//						+ " , "
//						+ z
//						+ ", "
//						+ temps.get(2)
//						+ ", " 
//						+ " null)";
//				//System.out.println(insertString);
//				executeInsert(insertString);
				
				try
				{
//					PreparedStatement stat = conn.prepareStatement(insertStatementString);
					stat.setInt(1, runID);
					stat.setString(2, filename);
					stat.setInt(3, dataVariableID);
					
					stat.setDouble(4, (Double)temps.get(0));
					stat.setDouble(5, (Double)temps.get(1));
					stat.setDouble(6, z);
					Double valueDbt = new Double((String)temps.get(2)).doubleValue();
					stat.setDouble(7, valueDbt );
					Timestamp timestamp = parseDateTimeToTimestamp(filename);					
					stat.setTimestamp(8, timestamp);
					//System.out.println(insert);
					boolean success = stat.execute();	
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}					
			}
			
//			String trimmedDataVariable = dataVariable.replaceAll("/", "_");
//
//			String fullFilename = trimmedDataVariable + "_" + filename;
//
//			String dataFileName = outputDirectory + File.separator
//					+ File.separator + trimmedDataVariable + ".dat";
//			common.writeFile(outputStr.toString(), dataFileName);
//
//			String outputFile = outputDirectory + File.separator + fullFilename
//					+ ".png";
//			String xLabel = "Data from " + filename + " of " + dataVariable;
//			plotData(dataFileName, outputFile, xLabel);

		}
		
		try
		{
			conn.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

//	public Vector<TreeMap<String, String>> getTableData(String queryString)
//	{
//		Vector<String> columnNamesLocal = new Vector<String>();
//		Vector<TreeMap<String, String>> storedObject = new Vector<TreeMap<String, String>>();
//
//		try
//		{
//
//			Connection connection = this.getConnection();
//
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(queryString);
//			ResultSetMetaData metaData = resultSet.getMetaData();
//			int count = 0;
//			while (resultSet.next())
//			{
//				TreeMap<String, String> treeMap = new TreeMap<String, String>();
//				for (int i = 1; i <= metaData.getColumnCount(); i++)
//				{
//					treeMap.put(metaData.getColumnName(i),
//							resultSet.getString(i));
//					if (count == 0)
//					{
//						columnNamesLocal.add(metaData.getColumnName(i));
//					}
//				}
//				storedObject.add(treeMap);
//
//				count++;
//			}
//			statement.close();
//			connection.close();
//			columnNames = columnNamesLocal;
//
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return storedObject;
//
//	}
	
	public String getRunDataDir(String query)
	{
		String dataDir = null;

		Connection conn = common.getMySqlConnection();		
		try
		{


			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				dataDir = rs.getString("data_dir");
				System.out.println(rs.getString("run_id") + '\t'
						
						+ rs.getString("data_dir") +  '\t'
						//'\t' + rs.getString("album")
//						+ '\t' + rs.getString("year") + '\t'
//						+ rs.getString("directory") + '\t'
//						+ rs.getString("filename")
						);

			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return dataDir;
	}	

	public void testDatabase(String query)
	{
		//String query = "select * from songs_save";

		Connection conn = common.getMySqlConnection();
//		SQLite.Database db = null;
		try
		{


			Statement stat = conn.createStatement();
			// stat.executeUpdate("drop table if exists people;");
			// stat.executeUpdate("create table people (name, occupation);");

			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				// artist, title, album, year, directory, filename
				System.out.println(rs.getString("run_id") + '\t'
						
						+ rs.getString("data_dir") +  '\t'
						//'\t' + rs.getString("album")
//						+ '\t' + rs.getString("year") + '\t'
//						+ rs.getString("directory") + '\t'
//						+ rs.getString("filename")
						);

			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}



}
