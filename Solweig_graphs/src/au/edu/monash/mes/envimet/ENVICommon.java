package au.edu.monash.mes.envimet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.jgnuplot.Splot;

public class ENVICommon
{

	String ediFileType = ".EDI";
	String edtFileType = ".EDT";
	String cfFileType = ".cf";
	String inFileType = ".in";

	public static final int z_Topo = 1;
	public static final int T_Surface_K = 2;
	public static final int T_Surface_Diff = 3;
	public static final int T_Surface_Change = 4;
	public static final int Q_Surface = 5;
	public static final int uv_above_Surface = 6;
	public static final int Sensible_heat_flux = 7;
	public static final int Exchange_Coeff_Heat = 8;
	public static final int Latent_heat_flux = 9;
	public static final int Soil_heat_Flux = 10;
	public static final int Sw_Direct_Radiation = 11;
	public static final int Sw_Diffuse_Radiation = 12;
	public static final int Lambert_Factor = 13;
	public static final int Longwave_Radiation_Budget = 14;
	public static final int Longwave_Rad_from_vegetation = 15;
	public static final int Longwave_Rad_from_environment = 16;
	public static final int Water_Flux = 17;
	public static final int Sky_View_Faktor = 18;
	public static final int Building_Height = 19;
	public static final int Surface_Albedo = 20;
	public static final int Deposition_Speed = 21;
	public static final int Mass_Deposed = 22;

	public static final String RUN_DATE = "run_date";
	public static final String RUN_DESC = "run_desc";
	public static final String RUN_NAME = "run_name";
	public static final String ENVI_VERSION = "envi_version";
	public static final String ENVI_VERSION3 = "3";
	public static final String ENVI_VERSION4 = "4";
	public static final String DATA_DIR = "data_dir";

	public static final String RECEPTOR_ID = "receptor_id";
	public static final String RECEPTOR_X = "x";
	public static final String RECEPTOR_Y = "y";
	public static final String RECEPTOR_FILENAME = "filename";
	public static final String RECEPTOR_PATH = "path";

	/*
	 *
	 * <td>1</td><td>z Topo (m)</td></tr> <td>2</td><td>T Surface (K)</td></tr>
	 * <td>3</td><td>T Surface Diff. (K)</td></tr> <td>4</td><td>T Surface
	 * Change (K/h)</td></tr> <td>5</td><td>q Surface (g/kg)</td></tr>
	 * <td>6</td><td>uv above Surface (m/s)</td></tr> <td>7</td><td>Sensible
	 * heat flux (W/m&sup2;)</td></tr> <td>8</td><td>Exchange Coeff. Heat
	 * (m&sup2;/s)</td></tr> <td>9</td><td>Latent heat flux
	 * (W/m&sup2;)</td></tr> <td>10</td><td>Soil heat Flux (W/m&sup2;)</td></tr>
	 * <td>11</td><td>Sw Direct Radiation (W/m&sup2;)</td></tr>
	 * <td>12</td><td>Sw Diffuse Radiation (W/m&sup2;)</td></tr>
	 * <td>13</td><td>Lambert Factor</td></tr> <td>14</td><td>Longwave Radiation
	 * Budget (W/m&sup2;)</td></tr> <td>15</td><td>Longwave Rad. from vegetation
	 * (W/m&sup2;)</td></tr> <td>16</td><td>Longwave Rad. from environment
	 * (W/m&sup2;)</td></tr> <td>17</td><td>Water Flux (g/(m&sup2;s))</td></tr>
	 * <td>18</td><td>Sky-View-Faktor</td></tr> <td>19</td><td>Building Height
	 * (m)</td></tr> <td>20</td><td>Surface Albedo</td></tr>
	 * <td>21</td><td>Deposition Speed (mm/s)</td></tr> <td>22</td><td>Mass
	 * Deposed (&micro;g/m&sup2;)</td></tr>
	 */

	public void writeFile(String text, String filename)
	{
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object

		try
		{
			out = new FileOutputStream(filename);
			p = new PrintStream(out);
			p.println(text);
			p.close();
		} catch (Exception e)
		{
			System.err.println("Error writing to file");
		}

	}

	public boolean createDirectory(String directory)
	{
//	    // Create one directory
//	    boolean success = (new File(directory)).mkdir();
//	    if (success) {
//	      System.out.println("Directory: " + directory + " created");
//	    }


	    // Create multiple directories
	    boolean success = (new File(directory)).mkdirs();
	    if (success) {
	      System.out.println("Directories: " + directory + " created");
	    }

	    return success;


	}

	@SuppressWarnings("unchecked")
	public String[] getDirectoryList(String directory)
	{
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
				else if (name.contains("##Check"))
				{
					accept = false;
				}
				return accept;
			}
		};


		File dir = new File(directory);

		File files[] = dir.listFiles(filter);
		Arrays.sort( files, new Comparator<File>()
		{
//		     public int compare(final Object o1, final Object o2) {
//		       return new Long(((File)o1).lastModified()).compareTo
//		             (new Long(((File) o2).lastModified()));
//		      }

			@Override
			public int compare(File o1, File o2)
			{
				return new Long((o1).lastModified()).compareTo
	             (new Long(( o2).lastModified()));
			}
		});

		String[] fileNames = new String[files.length];
		int count = 0;
		for (File file : files)
		{
			fileNames[count] = file.getName();
			//System.out.println(fileNames[count]);
			count ++;
		}


//		//String[] children = dir.list();
//		if (files == null)
//		{
//			// Either dir does not exist or is not a directory
//		} else
//		{
//			for (int i = 0; i < children.length; i++)
//			{
//				// Get filename of file or directory
//				String filename = children[i];
//			}
//		}
//
//		children = dir.list(filter);
//		return children;
		return fileNames;

	}

	public ENVICommon()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Connection getMySqlConnection()
	{

		String USERNAME = "envimet";
		String PASSWORD = "envimet";
		String URL = "jdbc:mysql://localhost/envimet";

		Connection conn = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return conn;
	}

	public Connection getSqliteConnection()
	{
		Connection conn = null;
		// SQLite.Database db = null;
		try
		{
			Class.forName("SQLite.JDBCDriver").newInstance();
			conn = DriverManager
					.getConnection("jdbc:sqlite://home/nice/Documents/MonashMasters/Research Dissertation/Envimet-data.sqlite3");
			// java.lang.reflect.Method m = conn.getClass().getMethod(
			// "getSQLiteDatabase", null);
			// db = (SQLite.Database) m.invoke(conn, null);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return conn;
	}

	public ArrayList<String> getOutputFiles(int runID, int variable)
	{
		ArrayList<String> list = new ArrayList<String>();
		String query = "select distinct output_file from run_data where run_id = "
				+ "?" +
				// + runID +
				" and variable_type = " + "?"
		// + variable
		;
		System.out.println(query);

		Connection conn = getMySqlConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, runID);
			ps.setInt(2, variable);
			// Statement stat = conn.createStatement();

			// ResultSet rs = stat.executeQuery(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				String outputFile = rs.getString("output_file");
				list.add(outputFile);
				 System.out.println("add output_file " + outputFile);
				 System.out.println("list=" + list.toString());

			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return list;
	}

	public ArrayList<Integer> getDistinctVariableTypesForRun(int runID)
	{
		ArrayList<Integer> runVariableTypes = new ArrayList<Integer>();
		Connection conn = getMySqlConnection();
		String query = "select distinct variable_type from run_data where run_id = "
				+ runID;

		try
		{

			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(query);

			while (rs.next())
			{
				Integer variableType = rs.getInt("variable_type");
				runVariableTypes.add(variableType);
			}
			rs.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			conn.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return runVariableTypes;
	}

	public TreeMap<String, ArrayList<DataPoint>> getVariableDataFromDB(
			int runID, int variable)
	{

		ArrayList<String> listOfOutputFiles = getOutputFiles(runID, variable);

		Connection conn = getMySqlConnection();

		TreeMap<String, ArrayList<DataPoint>> allData = new TreeMap<String, ArrayList<DataPoint>>();

		for (String outputFileName : listOfOutputFiles)
		{
			String query = "select x,y,z,value,output_file from run_data where run_id = "
					+ runID
					+ " and variable_type = "
					+ variable
					+ " and output_file ='" + outputFileName + "' ";

			System.out.println(query);
			// System.exit(1);

			try
			{
				ArrayList<DataPoint> variables = new ArrayList<DataPoint>();
				;
				Statement stat = conn.createStatement();

				ResultSet rs = stat.executeQuery(query);

				while (rs.next())
				{
					double x = rs.getDouble("x");
					double y = rs.getDouble("y");
					double z = rs.getDouble("z");
					double value = rs.getDouble("value");
					String outputFile = rs.getString("output_file");
					// System.out.println("outputFile="+ outputFile);

					DataPoint dataPoint = new DataPoint(x, y, z, value,
							outputFile);
					variables.add(dataPoint);
					//
					// System.out.println(dataPoint.toString());
					// System.out.println("variables.size()="+
					// variables.size());
					// System.out.println("allData.size()="+ allData.size());
				}
				rs.close();
				allData.put(outputFileName, variables);

			} catch (Exception e)
			{
				e.printStackTrace();
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

		// System.out.println(variables.toString());
		// System.out.println("allData.size()="+ allData.size());
		return allData;
	}
	public TreeMap<String, Integer> getDataVariableTypes()
	{
		TreeMap<String, Integer> variables = new TreeMap<String, Integer>();
		String query = "select variable_id, variable_name from run_variables ";

		Connection conn = getMySqlConnection();
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

		System.out.println(variables.toString());
		return variables;
	}

	public TreeMap<String, String> getMetaDataForRun(int runID)
	{
		TreeMap<String, String> metaData = new TreeMap<String, String>();
		String query = "select run_date,run_desc,run_name,envi_version,data_dir from runs where run_id = " +runID;

		Connection conn = getMySqlConnection();
		try
		{
			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				metaData.put(RUN_DESC, rs.getString(RUN_DESC));
				metaData.put(RUN_NAME, rs.getString(RUN_NAME));
				metaData.put(ENVI_VERSION, rs.getString(ENVI_VERSION));
				metaData.put(DATA_DIR, rs.getString(DATA_DIR));
			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return metaData;
	}

	public TreeMap<String, String> getReceptorMetaDataFromDB(int runID)
	{
		TreeMap<String, String> metaData = new TreeMap<String, String>();
		String query = "select receptor_id,x,y,filename,path from receptor_files where run_id = " +runID;

		Connection conn = getMySqlConnection();
		try
		{
			Statement stat = conn.createStatement();

			ResultSet rs = stat.executeQuery(query);
			while (rs.next())
			{
				metaData.put(RECEPTOR_ID, rs.getString(RECEPTOR_ID));
				metaData.put(RECEPTOR_X, ""+rs.getInt(RECEPTOR_X));
				metaData.put(RECEPTOR_Y, ""+rs.getInt(RECEPTOR_Y));
				metaData.put(RECEPTOR_FILENAME, rs.getString(RECEPTOR_FILENAME));
				metaData.put(RECEPTOR_PATH, rs.getString(RECEPTOR_PATH));

			}
			rs.close();
			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return metaData;
	}

	public String removeIllegalCharacters(String str)
	{
		return str.replaceAll("/", "_");
	}

    public void actualPlotCmd(String plotname, String outputDirectory)
    {
    	try
		{
			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin_indiv.sh", plotname, outputDirectory};
			Process aProcess = Runtime.getRuntime().exec(commands);
			aProcess.waitFor();

		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}
    }
	public void runPlotCmd(String outputDirectory, Splot aPlot)
	{
		try
		{
			String plotName = aPlot.plot();
			//System.out.println("Plotting cmd=" + plotName);
			actualPlotCmd(plotName, outputDirectory);

		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}
	}

	public void imageMakeTransparent(String inImage, String outImage)
	{
		// convert startImage.png -transparent white EndImageT.png

		IMOperation op = new IMOperation();
		op.transparent("white");
		ConvertCmd convert = new ConvertCmd();
	    op.addImage();  // read and crop first image
	    op.addImage();  // read and crop second image

		try
		{
			convert.run(op, inImage, outImage);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IM4JavaException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void imageCompositeBlendCmd(String baseImage, String overlayImage, String outputImage, Integer blend)
	{
		IMOperation op = new IMOperation();
		op.blend(blend);
		op.gravity("center");
		CompositeCmd composite = new CompositeCmd();
	    op.addImage();  // read and crop first image
	    op.addImage();  // read and crop second image
	    op.addImage();
		try
		{
			composite.run(op,baseImage,overlayImage,outputImage);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IM4JavaException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void imageCompositeDifferenceCmd(String baseImage, String overlayImage, String outputImage)
	{
		IMOperation op = new IMOperation();
		op.compose("difference");
//		op.dissolve(percent);

//		op.blend(blend);
		op.gravity("center");
		CompositeCmd composite = new CompositeCmd();
	    op.addImage();  // read and crop first image
	    op.addImage();  // read and crop second image
	    op.addImage();
		try
		{
			composite.run(op,baseImage,overlayImage,outputImage);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IM4JavaException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void imageCompositeDissolveCmd(String baseImage, String overlayImage, String outputImage, Integer percent)
	{
		IMOperation op = new IMOperation();
		op.dissolve(percent);

//		op.blend(blend);
		op.gravity("center");
		CompositeCmd composite = new CompositeCmd();
	    op.addImage();  // read and crop first image
	    op.addImage();  // read and crop second image
	    op.addImage();
		try
		{
			composite.run(op,baseImage,overlayImage,outputImage);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IM4JavaException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

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

		for (int j = 0; j < fileVariables.size(); j++)
		{
			boolean dropVariable = false;
			String variable = fileVariables.get(j);
			for (int i = 0; i < ignoredFields.length; i++)
			{
				// System.out.println("variable=|" + variable +
				// "| ignoredFields[i]=|" + ignoredFields[i] + "|");

				if (variable.equals(ignoredFields[i]))
				{
					// don't keep it
					dropVariable = true;
					break;
				}

			}
			if (dropVariable)
			{

			} else
			{
				returnVariables.add(variable);
			}
			dropVariable = false;
		}
		return returnVariables;
	}


}
