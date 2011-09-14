package au.edu.monash.mes.envimet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ParseAndStoreReceptorFile
{

	ENVICommon common = new ENVICommon();

	public static void main(String[] args)
	{
		int runID = 8;

		ParseAndStoreReceptorFile parseAndStoreReceptorFile = new ParseAndStoreReceptorFile();
		parseAndStoreReceptorFile.readReceptorFile(runID);

	}
	
	public void insertReceptorMetaData(int runID)
	{
		// {filename=FLX BareFtCtMelRec-40-2days xx.1DT, path=/home/nice/Documents/MonashMasters/Research Dissertation/ENVI40ValidationRuns/BareFlatConcreteMelRec-2days/receptors/xx/, receptor_id=1, x=20, y=21}
		
		TreeMap<String, String> runMetaData = common.getMetaDataForRun(runID);
		String runName = runMetaData.get(ENVICommon.RUN_NAME);
		String dataDirectory = runMetaData.get(ENVICommon.DATA_DIR);
		String enviVersion = runMetaData.get(ENVICommon.ENVI_VERSION);
		
		String receptorDataDirectory = dataDirectory + "../receptors/xx/";
		String receptorFilename = "FLX " + runName + " xx.1DT";
		String x = "20";
		String y = "20";
		
		String query = "insert into receptor_files (run_id,x,y,filename,path) values (?,?,?,?,?)";	

		Connection conn = null;
		
		PreparedStatement pstmt = null;
		try
		{
			conn = common.getMySqlConnection();		

			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, runID);			
			pstmt.setString(2, x);
			pstmt.setString(3, y);
			pstmt.setString(4, receptorFilename);
			pstmt.setString(5, receptorDataDirectory);
						
			pstmt.execute();			
			pstmt.close();
			
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
		
		
		
		TreeMap<String, String> receptorMetadata = common.getReceptorMetaDataFromDB(runID);
		
		
		System.out.println(receptorMetadata.toString());

		String path = receptorMetadata.get(ENVICommon.RECEPTOR_PATH);
		String filename = receptorMetadata.get(ENVICommon.RECEPTOR_FILENAME);
		String receptorID = receptorMetadata.get(ENVICommon.RECEPTOR_ID);
		
		
		
	}

	public void readReceptorFile(int runID)
	{
		TreeMap<String, String> metadata = common
				.getReceptorMetaDataFromDB(runID);

		String path = metadata.get(ENVICommon.RECEPTOR_PATH);
		String filename = metadata.get(ENVICommon.RECEPTOR_FILENAME);
		String receptorID = metadata.get(ENVICommon.RECEPTOR_ID);

		File file = new File(path + filename);
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			BufferedReader d = new BufferedReader(new InputStreamReader(fis));

			String line = d.readLine();
			line = d.readLine();
			ArrayList<String> variables = parseVariableLine(line);
			System.out.println(variables.toString());

			while (d.ready())
			{
				line = d.readLine();

				StringTokenizer st = new StringTokenizer(line);
				int count = 0;
				String day = null;
				String time = null;
				while (st.hasMoreTokens())
				{
					String value = st.nextToken();
					//System.out.println(variables.get(count) + "=" + value);
					
					if (count == 0)
					{
						day = value;
					}
					if (count == 1)
					{
						time = value;
					}
					if (count > 1)
					{
						insertValue(runID, receptorID, variables.get(count), value, day, time );
					}

					count++;
				}

			}

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public boolean insertValue(int runID, String receptorID, String variable, String value, String day, String time)
	{
		boolean success = false;
		
		String query = "insert into receptor_data (run_id, receptor_id, variable,value, variable_time) " +
				"values (" + runID +
				", " +
				receptorID +
				", '" +
				variable +
				"', " + value +
				", '" + day + " " + time +
				"') ";
		
		System.out.println(query);
		
		Connection conn = common.getMySqlConnection();		
		try
		{
			Statement stat = conn.createStatement();

			success = stat.execute(query);

			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return success;
	}

	public ArrayList<String> parseVariableLine(String line)
	{
		ArrayList<String> variables = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(line.substring(1));
		while (st.hasMoreTokens())
		{
			variables.add(st.nextToken());
		}

		return variables;
	}

}
