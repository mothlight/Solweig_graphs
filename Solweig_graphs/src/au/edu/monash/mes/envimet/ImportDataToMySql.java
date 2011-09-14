package au.edu.monash.mes.envimet;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class ImportDataToMySql
{
	ENVICommon common = new ENVICommon();

	public ImportDataToMySql()
	{
		super();
	}

	public static void main(String[] args)
	{
		ImportDataToMySql importData = new ImportDataToMySql();
		
		int runID = 1;
		//importData.testDatabase();
		importData.processDirectory(runID);	

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

		}

		for (String filename : set)
		{
			processFile(directory, filename, runID);

		}
	}
	
	public TreeMap<String, Integer> getDataVariableTypes()
	{
		TreeMap<String, Integer> variables = new TreeMap<String, Integer>();
		String query = "select variable_id, variable_name from run_variables ";

		Connection conn = common.getMySqlConnection();		
		try
		{
			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				Integer variableID = rs.getInt("variable_id");
				String variableName = rs.getString("variable_name");
				
				variables.put(variableName, variableID);
			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//System.out.println(variables.toString());
		return variables;
	}
	
	public TreeMap<String, Integer> addDataVariableToDB(String dataVariable)
	{
		
		String query = " insert into run_variables (variable_name, variable_id) values ('" + dataVariable +
				"', null)  ";
		
		executeInsert(query);
		System.out.println(query);
		
		return getDataVariableTypes();
		
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
		TreeMap<String, Integer> dbVariableTypes = getDataVariableTypes();
		float z = 0.0f;
		ReadEDIFile readEDI = new ReadEDIFile(directory + filename
				+ common.ediFileType);
		ArrayList<String> fileVariables = readEDI.getFileVariables();
		ReadEDTFile readEDTFile = new ReadEDTFile(directory + filename
				+ common.edtFileType, readEDI);
		TreeMap<String, ArrayList> edtData = readEDTFile.getData();
		
		String insertStatementString = "insert into run_data (run_id, output_file, variable_type, x, y, z, value, variable_time) values" +
		"(" +
		//runID +
		 "?" +
		" , '" +
		//filename
		"?"
		+ "' , " 
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
		+ " null)";		
		
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
			System.out.println("Starting " + dataVariable + " for " + filename);
			Integer dataVariableID = null;
			
			if (dbVariableTypes.containsKey(dataVariable))
			{
				dataVariableID = dbVariableTypes.get(dataVariable);
			}
			else
			{
				dbVariableTypes = addDataVariableToDB(dataVariable);	
				dataVariableID = dbVariableTypes.get(dataVariable);
			}

			ArrayList dataForVariable = edtData.get(dataVariable);

			for (int i = 0; i < dataForVariable.size(); i++)
			{
				ArrayList temps = (ArrayList) dataForVariable.get(i);
				
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
				
//				String insertStatementString = "insert into run_data (run_id, output_file, variable_type, x, y, z, value, variable_time) values" +
//				"(" +
//				//runID +
//				 "?" +
//				" , '" +
//				//filename
//				"?"
//				+ "' , " 
//				//+ dataVariableID
//				+ "?"
//				+ " , " 
//				//+ temps.get(0)
//				+ "?"
//				+ "," 
//				//+ temps.get(1)
//				+ "?"
//				+ " , "
//				//+ z
//				+ "?"
//				+ ", "
//				//+ temps.get(2)
//				+ "?"
//				+ ", " 
//				+ " null)";		
				
					
				try
				{
//					PreparedStatement stat = conn.prepareStatement(insertStatementString);
					stat.setInt(1, runID);
					stat.setString(2, filename);
					stat.setInt(3, dataVariableID);
					
					stat.setFloat(4, (Float)temps.get(0));
					stat.setFloat(5, (Float)temps.get(1));
					stat.setFloat(6, z);
					stat.setFloat(7, (Float)temps.get(2));
					//System.out.println(insert);
					boolean success = stat.execute();					
					

				} catch (Exception e)
				{
					e.printStackTrace();
				}					

				//executeInsert(insertString);
			}
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
				//System.out.println(rs.getString("run_id") + '\t' + rs.getString("data_dir") +  '\t');

			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return dataDir;
	}	

	public void testDatabase()
	{
		String query = "select run_id,data_dir from runs";
		Connection conn = common.getMySqlConnection();
		try
		{
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				System.out.println(rs.getString("run_id") + '\t'						
						+ rs.getString("data_dir") +  '\t'
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
