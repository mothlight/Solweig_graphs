package au.edu.monash.mes.envimet;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import org.jgnuplot.Axes;
import org.jgnuplot.Graph;
import org.jgnuplot.LineType;
import org.jgnuplot.Plot;
import org.jgnuplot.PointType;
import org.jgnuplot.Style;
import org.jgnuplot.Terminal;

public class CreateDailyEnergyBalanceGraph
{
	ENVICommon common = new ENVICommon();
	//int runID = 10;
	double x = 54.5;
	double y = 48.5;
	public final String TMP_OUTPUT_DIRECTORY = "/tmp/envimet";

	public final static String SW_DIRECT_RADIATION = "Sw_Direct_Radiation";
	public final static String SW_DIFFUSE_RADIATION = "Sw_Diffuse_Radiation";
	public final static String T_SURFACE_K = "T_Surface_K";
	public final static String SURFACE_ALBEDO = "Surface_Albedo";
	public final static String LONGWAVE_RAD_FROM_ENVIRONMENT = "Longwave_Rad_from_environment";
	public final static String SOIL_HEAT_FLUX = "Soil_heat_Flux";
	public final static String SENSIBLE_HEAT_FLUX = "Sensible_heat_flux";
	public final static String VARIABLE_TIME = "variable_time";
	public final static String BLACK_BODY_RADIATION = "black_body_radiation";

//	public final String SW_DOWN = "swDown";
//	public final String LW_DOWN = "lwDown";
	
	public final String SW_IN = "swIn";
	public final String LW_IN = "lwIn";
	public final String SW_OUT = "swOut";
	public final String LW_OUT = "lwOut";
	public final String NET_RADIATION = "netRadiation";
	public final String AVAILABLE_ENERGY = "availableEnergy";
	public final String LATENT_HEAT_FLUX = "Latent_heat_flux";
	public final String DAILY_ENERGY_BALANCE = "Daily_energy_balance";
	
	public final static String BUILDING_HEIGHT = "building_height";
	public final static String DEPOSITION_SPEED = "deposition_speed";
	public final static String EXCHANGE_COEFF_HEAT = "exchange_coeff_heat";
	public final static String LAMBERT_FACTOR = "lambert_factor";
	public final static String LONGWAVE_RADIATION_BUDGET = "longwave_radiation_budget";
	public final static String MASS_BOIMET = "mass_boimet";
	public final static String MASS_DEPOSED = "mass_deposed";
	public final static String Q_AIR_BIOMET = "q_air_biomet";
	public final static String Q_SURFACE = "q_surface";
	public final static String RECEPTORS = "receptors";
	public final static String SHADOW_FLAG = "shadow_flag";
	public final static String SKY_VIEW_FAKTOR = "sky_view_faktor";
	public final static String SURFACE_EXPOSITION = "surface_exposition";
	public final static String SURFACE_INCLINATION = "surface_inclination";
	public final static String T_AIR_BOIMET = "t_air_boimet";
	public final static String T_SURFACE_CHANGE = "t_surface_change";
	public final static String T_SURFACE_DIFF = "t_surface_diff";
	public final static String TMRT_BOIMET = "tmrt_boimet";
	public final static String UV_ABOVE_SURFACE = "uv_above_surface";
	public final static String WALKABILITY_VALUE = "walkability_value";
	public final static String WATER_FLUX = "water_flux";
	public final static String WIND_SPEED_BOIMET = "wind_speed_boimet";
	public final static String Z_BOIMET = "z_boimet";
	public final static String Z_NODE_BOIMET = "z_node_boimet";
	public final static String Z_TOPO = "z_topo";
	
	public final static String REC_Z = "z";
	public final static String REC_DT0_DT = "dT0_dt";
	public final static String REC_Q0 = "q0";
	public final static String REC_UV1 = "uv1";
	public final static String REC_W1 = "w1";
	public final static String REC_T1 = "T1";
	public final static String REC_KM = "Km";
	public final static String REC_KH = "Kh";	
	public final static String REC_QLW_BUDG = "Qlw_Budg";
	public final static String REC_QLW_SURF = "Qlw_surf";
	public final static String REC_QLW_SKY = "Qlw_sky";
	public final static String REC_QLW_LEAFS = "Qlw_leafs";

	
	
	private ArrayList<String> dataToPlot = new ArrayList<String>();

	public ArrayList<String> getDataToPlot()
	{
		return dataToPlot;
	}

	public void setDataToPlot(ArrayList<String> dataToPlot)
	{
		this.dataToPlot = dataToPlot;
	}

	public CreateDailyEnergyBalanceGraph()
	{
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		CreateDailyEnergyBalanceGraph create = new CreateDailyEnergyBalanceGraph();
//		ArrayList<TreeMap<String, String>> data = null;
//		data = create.getDataset(create.runID, create.x, create.y);
//		System.out.println (data.toString());
//		create.generateDataFileFromDataPoints(data);
	}
	
	public void plotDataFromDatafile(
			ArrayList<TreeMap<String, String>> data, int runID, String prefix)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String filename = metaData.get(ENVICommon.RUN_NAME);
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		String dataFileName = outputDirectory + File.separator + filename + "_" + prefix + ".dat";

		if (data != null)
		{

			StringBuffer outputFileStr = new StringBuffer();
			outputFileStr.append("# Time" + "\t"); 
						
			for (String dataItem : dataToPlot)
			{
				outputFileStr.append(dataItem + '\t');
			}
			
			outputFileStr.append('\n');

			for (TreeMap<String, String> dataItem : data)
			{
				String variableTime = dataItem.get(VARIABLE_TIME);
				outputFileStr.append(variableTime + '\t' );
				for (String dataItem2 : dataToPlot)
				{
					String item = dataItem.get(dataItem2);
					outputFileStr.append(item + '\t'  );
				}
				outputFileStr.append('\n');
			}

			common.writeFile(outputFileStr.toString(), dataFileName);
		}

		String outputFile = outputDirectory + File.separator + filename + prefix + ".png";

		Plot.setGnuplotExecutable("/usr/bin/gnuplot");
		Plot.setPlotDirectory(outputDirectory);

		Plot aPlot = new Plot();
		aPlot.setDataFileName(dataFileName);
		
		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");
		//size 1024,600  small 
		
		aPlot.setGrid();
		aPlot.setKey("right box");
		aPlot.setXLabel("Date/Time");
		aPlot.setYLabel("W/m2");		
		aPlot.setTitle(metaData.get(ENVICommon.RUN_DESC));
		aPlot.setTimeFormat("%Y-%m-%d-%H:%M");
		aPlot.addExtra("set format x '%H'");
		aPlot.addExtra("set xdata time");
		
		int count = 2;
		for (String dataItem : dataToPlot)
		{
			aPlot.pushGraph(new Graph(dataFileName, "1:" + count, Axes.NOT_SPECIFIED, dataItem, Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
			count ++;
		}
		
		try
		{
			System.out.println("Creating plot in " + outputDirectory);						
			String execStr = aPlot.plot();
			
			String[] commands = new String[]{"gnuplot", "\"" + outputDirectory + "\"" + "/*.txt"};
			Process aProcess = Runtime.getRuntime().exec(commands);
			System.out.println("Created file " + outputFile);
			
			commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			aProcess = Runtime.getRuntime().exec(commands);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}	
	
	public String getReceptorSqlQuery(int runID)
	{		
		return
			" select rda.variable_time "
			+ ",rda.value as Sw_Direct_Radiation "
			+ ",rdb.value as Sw_Diffuse_Radiation "
			+ ",rdc.value as T_Surface_K "
			+ ",( select value as Surface_Albedo from run_data where variable_type in (" 
			+ "20) and run_id =" 
			+ runID 
			+ " limit 1 ) Surface_Albedo " 
			+ ",rde.value as Longwave_Rad_from_environment "
			+ ",rdf.value as Soil_heat_Flux "
			+ ",rdg.value as Sensible_heat_flux "
			+ ",rdh.value as Latent_heat_flux "
			+ ",rdi.value as z "
			+ ",rdj.value as dT0_dt "
			+ ",rdk.value as q0 "
			+ ",rdl.value as uv1 "
			+ ",rdm.value as w1 "
			+ ",rdn.value as T1 "
			+ ",rdo.value as Km "
			+ ",rdp.value as Kh "
			+ ",rdq.value as Qlw_Budg "
			+ ",rdr.value as Qlw_surf "
			+ ",rds.value as Qlw_sky "
			+ ",rdt.value as Qlw_leafs "	
			+ " from receptor_data rda  "
			+ " left outer join receptor_data rdb on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time = rdb.variable_time and rdb.variable in ('Qkw.dif') and rdb.run_id = rda.run_id " 
			+ " left outer join receptor_data rdc on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time = rdc.variable_time and rdc.variable in ('T0'        ) and rdc.run_id = rda.run_id " 
			+ " left outer join receptor_data rde on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time = rde.variable_time and rde.variable in ('Qlw.envir'  ) and rde.run_id = rda.run_id " 
			+ " left outer join receptor_data rdf on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time = rdf.variable_time and rdf.variable in ('G' ) and rdf.run_id = rda.run_id  "
			+ " left outer join receptor_data rdg on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdg.variable_time and rdg.variable in ('H') and  rdg.run_id = rda.run_id  "
			+ " left outer join receptor_data rdi on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdi.variable_time and rdi.variable in ('z') and  rdi.run_id = rda.run_id  "
			+ " left outer join receptor_data rdj on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdj.variable_time and rdj.variable in ('dT0/dt') and  rdj.run_id = rda.run_id " 
			+ " left outer join receptor_data rdk on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdk.variable_time and rdk.variable in ('q0') and  rdk.run_id = rda.run_id  "
			+ " left outer join receptor_data rdl on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdl.variable_time and rdl.variable in ('uv1') and  rdl.run_id = rda.run_id " 
			+ " left outer join receptor_data rdm on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdm.variable_time and rdm.variable in ('w1') and  rdm.run_id = rda.run_id  "
			+ " left outer join receptor_data rdn on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdn.variable_time and rdn.variable in ('T1') and rdn.run_id = rda.run_id  "
			+ " left outer join receptor_data rdo on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdo.variable_time and rdo.variable in ('Km') and  rdo.run_id = rda.run_id  "
			+ " left outer join receptor_data rdp on  rda.run_id = " 
			+ runID
			+ " and rda.variable_time =rdp.variable_time and rdp.variable in ('Kh') and  rdp.run_id = rda.run_id  "
			+ " left outer join receptor_data rdq on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdq.variable_time and rdq.variable in ('Qlw.Budg') and  rdq.run_id = rda.run_id " 
			+ " left outer join receptor_data rdr on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdr.variable_time and rdr.variable in ('Qlw.surf') and  rdr.run_id = rda.run_id  "
			+ " left outer join receptor_data rds on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rds.variable_time and rds.variable in ('Qlw.sky') and  rds.run_id = rda.run_id  "
			+ " left outer join receptor_data rdt on  rda.run_id = " 
			+ runID 
			+ " and rda.variable_time =rdt.variable_time and rdt.variable in ('Qlw.leafs') and  rdt.run_id = rda.run_id  "
			+ " right outer join receptor_data rdh on  rdh.run_id = " 
			+ runID 
			+ " and rdh.variable_time = rda.variable_time and rdh.variable in ('LE') and rdh.run_id = rda.run_id  "
			+ " where rda.variable in ('Qkw.dir') ";
       			
	}
	
	public ArrayList<TreeMap<String, String>> getReceptorFilesDataset(int runID)
	{
		ArrayList<TreeMap<String, String>> returnData = new ArrayList<TreeMap<String, String>>();
		
		//Date Time modtime(min) z T0 dT0/dt q0 uv1 w1 T1 H LE G Km Kh Qkw.dir Qkw.dif Qlw.Budg Qlw.surf Qlw.sky Qlw.leafs Qlw.envir
		
		String query = getReceptorSqlQuery(runID);

		Connection conn = common.getMySqlConnection();
		try
		{
			Statement stat = conn.createStatement();
			System.out.println(query);
			ResultSet rs = stat.executeQuery(query);

			while (rs.next())
			{
				TreeMap<String, String> hourlyData = new TreeMap<String, String>();

				double swDirectRadiation = rs.getDouble(SW_DIRECT_RADIATION);
				double swDiffuseRadiation = rs.getDouble(SW_DIFFUSE_RADIATION);
				double tSurfaceK = rs.getDouble(T_SURFACE_K);
				double surfaceAlbedo = rs.getDouble(SURFACE_ALBEDO);
				double longwaveRadFromEnvironment = rs
						.getDouble(LONGWAVE_RAD_FROM_ENVIRONMENT);
				double soilHeatFlux = rs.getDouble(SOIL_HEAT_FLUX);
				double sensibleHeatFlux = rs.getDouble(SENSIBLE_HEAT_FLUX);
				double latentHeatFlux = rs.getDouble(LATENT_HEAT_FLUX);
				String variableTime = rs.getString(VARIABLE_TIME);
				
				DateFormat formatterFromDB = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				java.util.Date variableTimeDate = formatterFromDB.parse(variableTime);				

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
				//String variableTimeStr = formatter.format(variableTime);
				String variableTimeStr = formatter.format(variableTimeDate);

				hourlyData.put(SW_DIRECT_RADIATION, "" + swDirectRadiation);
				hourlyData.put(SW_DIFFUSE_RADIATION, "" + swDiffuseRadiation);
				hourlyData.put(T_SURFACE_K, "" + tSurfaceK);
				hourlyData.put(SURFACE_ALBEDO, "" + surfaceAlbedo);
				hourlyData.put(LONGWAVE_RAD_FROM_ENVIRONMENT, "" + longwaveRadFromEnvironment);
				hourlyData.put(LATENT_HEAT_FLUX, "" + Math.round(latentHeatFlux));
				hourlyData.put(SOIL_HEAT_FLUX, "" + Math.round(soilHeatFlux));
				hourlyData.put(SENSIBLE_HEAT_FLUX, "" + Math.round(sensibleHeatFlux));
				hourlyData.put(VARIABLE_TIME, variableTimeStr);
				
				double swIn = swDirectRadiation + swDiffuseRadiation;
				double swOut = (surfaceAlbedo) * (swIn);
				//double lwIn = longwaveRadFromEnvironment;
				double lwIn = rs.getDouble(REC_QLW_SKY);
				
				double lwOut = 0.0000000567 * Math.pow(tSurfaceK, 4);
				double netRadiation = (swIn - swOut) + (lwIn - lwOut);
				double availableEnergy = netRadiation - soilHeatFlux;
				
				double dailyEnergyBalance = netRadiation - soilHeatFlux - sensibleHeatFlux - latentHeatFlux;

				hourlyData.put(NET_RADIATION, "" + Math.round(netRadiation));
				hourlyData.put(AVAILABLE_ENERGY, "" + Math.round(availableEnergy));
				
				hourlyData.put(SW_IN, "" + Math.round(swIn));
				hourlyData.put(SW_OUT, "" + Math.round(swOut));
				hourlyData.put(LW_IN, "" + Math.round(lwIn));
				hourlyData.put(LW_OUT, "" + Math.round(lwOut));
				hourlyData.put(DAILY_ENERGY_BALANCE, "" + Math.round(dailyEnergyBalance));
				
				hourlyData.put(REC_Z, "" + rs.getDouble(REC_Z));
				hourlyData.put(REC_DT0_DT, "" + rs.getDouble(REC_DT0_DT));
				hourlyData.put(REC_Q0, "" + rs.getDouble(REC_Q0));
				hourlyData.put(REC_UV1, "" + rs.getDouble(REC_UV1));
				hourlyData.put(REC_W1, "" + rs.getDouble(REC_W1));
				hourlyData.put(REC_T1, "" + rs.getDouble(REC_T1));
				hourlyData.put(REC_KM, "" + rs.getDouble(REC_KM));
				hourlyData.put(REC_KH, "" + rs.getDouble(REC_KH));
				hourlyData.put(REC_QLW_BUDG, "" + rs.getDouble(REC_QLW_BUDG));
				hourlyData.put(REC_QLW_BUDG, "" + rs.getDouble(REC_QLW_BUDG));
				hourlyData.put(REC_QLW_SKY, "" + rs.getDouble(REC_QLW_SKY));
				hourlyData.put(REC_QLW_LEAFS, "" + rs.getDouble(REC_QLW_LEAFS));
				
				returnData.add(hourlyData);
				System.out.println(hourlyData.toString());
				System.out.println();
			}

			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return returnData;		
	}
	
	public void graphReceptorFileData(
			ArrayList<TreeMap<String, String>> data, int runID, ArrayList<String> dataToPlot, String prefix )
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String filename = metaData.get(ENVICommon.RUN_NAME);
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		String dataFileName = outputDirectory + File.separator + filename
				+ "_REC_" + prefix + ".dat";

		if (data != null)
		{

			StringBuffer outputFileStr = new StringBuffer();
			outputFileStr.append("# Time" + "\t"); 
			for (String dataName : dataToPlot)
			{
				outputFileStr.append(dataName + "\t"); 
			}
							
//							+  '\t' + '\t' +
//							"swIn" +
//							"\t"  +
//							"lwIn" +
//							"\t"  +
//							"lwOut" +
//							"\t"  +
//							"net_radiation" +
//							"\t"  +
//							"soil_heat" +
//							"\t" +
//							"availableEnergy" +
//							"\t" +
//							"latentHeat" +
//							"\t"  +
//							"sensible_heat" +
//							"\t" + 

							
			outputFileStr.append('\n');

			for (TreeMap<String, String> dataItem : data)
			{
				String swIn = dataItem.get(SW_IN);
				String lwIn = dataItem.get(LW_IN);
				String netRadiation = dataItem.get(NET_RADIATION);
				String availableEnergy = dataItem.get(AVAILABLE_ENERGY);
				String soilHeat = dataItem.get(SOIL_HEAT_FLUX);
				String sensibleHeat = dataItem.get(SENSIBLE_HEAT_FLUX);
				String variableTime = dataItem.get(VARIABLE_TIME);
				String latentHeat = dataItem.get(LATENT_HEAT_FLUX); 
				String dailyEnergyBalance = dataItem.get(DAILY_ENERGY_BALANCE);				
				String lwOut = dataItem.get(LW_OUT);

				outputFileStr.append(variableTime + '\t' );
				for (String dataType : dataToPlot)
				{
					outputFileStr.append(dataItem.get(dataType)+ '\t');
				}
				
//						+ swIn + '\t' 
//						+ lwIn + '\t' 
//						+ lwOut + '\t'
//						+ netRadiation + '\t'+ '\t' 
//						+ soilHeat + '\t' + '\t'
//						+ availableEnergy + '\t' + '\t'
//						+ latentHeat + '\t' + '\t'
//						+ sensibleHeat + '\t' + '\t'
//						+ dailyEnergyBalance
						
						
				outputFileStr.append( '\n');
			}

			common.writeFile(outputFileStr.toString(), dataFileName);
		}

		String outputFile = outputDirectory + File.separator + filename
				+ "_REC_" + prefix +
						".png";

		Plot.setGnuplotExecutable("/usr/bin/gnuplot");
		Plot.setPlotDirectory(outputDirectory);
		//Graph aGraph = null;
		Plot aPlot = new Plot();
		aPlot.setDataFileName(dataFileName);
		
		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");

		aPlot.setGrid();
		aPlot.setKey("right box");
		aPlot.setXLabel("Date/Time");
		aPlot.setYLabel("W/m2");		
		aPlot.setTitle("Receptor data " + metaData.get(ENVICommon.RUN_DESC));
		aPlot.setTimeFormat("%Y-%m-%d-%H:%M");
		aPlot.addExtra("set format x '%H'");
		aPlot.addExtra("set xdata time");
		
		int count = 2;
		for (String dataType : dataToPlot)
		{
			aPlot.pushGraph(new Graph(dataFileName, "1:" + count, Axes.NOT_SPECIFIED, dataType, Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
			count ++;
		}
				
//		aPlot.pushGraph(new Graph(dataFileName, "1:2", Axes.NOT_SPECIFIED, "sw in(Qkw.dir + Qkw.dif)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:3", Axes.NOT_SPECIFIED, "lw in(Qlw.envir)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
//		aPlot.pushGraph(new Graph(dataFileName, "1:4", Axes.NOT_SPECIFIED, "lw out(5.6E8*T0^4)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:5", Axes.NOT_SPECIFIED, "net radiation", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:6", Axes.NOT_SPECIFIED, "soil heat(G)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:7", Axes.NOT_SPECIFIED, "available energy(netradiation-G)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:8", Axes.NOT_SPECIFIED, "latent heat(LE)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:9", Axes.NOT_SPECIFIED, "sensible heat(H)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
//		aPlot.pushGraph(new Graph(dataFileName, "1:10", Axes.NOT_SPECIFIED, "Rn-G-H-LE", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
		
		try
		{
			System.out.println("Creating plot in " + outputDirectory);						
			String execStr = aPlot.plot();
			
			String[] commands = new String[]{"gnuplot", "\"" + outputDirectory + "\"" + "/*.txt"};
			Process aProcess = Runtime.getRuntime().exec(commands);
			System.out.println("Created file " + outputFile);
			
			commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			aProcess = Runtime.getRuntime().exec(commands);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}		
	
	public void graphReceptorFileData(
			ArrayList<TreeMap<String, String>> data, int runID)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String filename = metaData.get(ENVICommon.RUN_NAME);
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		String dataFileName = outputDirectory + File.separator + filename
				+ "_REC.dat";

		if (data != null)
		{

			StringBuffer outputFileStr = new StringBuffer();
			outputFileStr
					.append("# Time" +
							"\t" +  '\t' + '\t' +
							"swIn" +
							"\t"  +
							"lwIn" +
							"\t"  +
							"lwOut" +
							"\t"  +
							"net_radiation" +
							"\t"  +
							"soil_heat" +
							"\t" +
							"availableEnergy" +
							"\t" +
							"latentHeat" +
							"\t"  +
							"sensible_heat" +
							"\t" + 
							"Rn-G-H-LE" 
							+ '\n');

			for (TreeMap<String, String> dataItem : data)
			{
				String swIn = dataItem.get(SW_IN);
				String lwIn = dataItem.get(LW_IN);
				String netRadiation = dataItem.get(NET_RADIATION);
				String availableEnergy = dataItem.get(AVAILABLE_ENERGY);
				String soilHeat = dataItem.get(SOIL_HEAT_FLUX);
				String sensibleHeat = dataItem.get(SENSIBLE_HEAT_FLUX);
				String variableTime = dataItem.get(VARIABLE_TIME);
				String latentHeat = dataItem.get(LATENT_HEAT_FLUX); 
				String dailyEnergyBalance = dataItem.get(DAILY_ENERGY_BALANCE);				
				String lwOut = dataItem.get(LW_OUT);

				outputFileStr.append(variableTime + '\t'  
						+ swIn + '\t' 
						+ lwIn + '\t' 
						+ lwOut + '\t'
						+ netRadiation + '\t'+ '\t' 
						+ soilHeat + '\t' + '\t'
						+ availableEnergy + '\t' + '\t'
						+ latentHeat + '\t' + '\t'
						+ sensibleHeat + '\t' + '\t'
						+ dailyEnergyBalance
						+ '\n');
			}

			common.writeFile(outputFileStr.toString(), dataFileName);
		}

		String outputFile = outputDirectory + File.separator + filename
				+ "_REC.png";

		Plot.setGnuplotExecutable("/usr/bin/gnuplot");
		Plot.setPlotDirectory(outputDirectory);
		//Graph aGraph = null;
		Plot aPlot = new Plot();
		aPlot.setDataFileName(dataFileName);
		
		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");

		aPlot.setGrid();
		aPlot.setKey("right box");
		aPlot.setXLabel("Date/Time");
		aPlot.setYLabel("W/m2");		
		aPlot.setTitle("Daily energy balance (receptor) " + metaData.get(ENVICommon.RUN_DESC));
		aPlot.setTimeFormat("%Y-%m-%d-%H:%M");
		aPlot.addExtra("set format x '%H'");
		aPlot.addExtra("set xdata time");
				
		aPlot.pushGraph(new Graph(dataFileName, "1:2", Axes.NOT_SPECIFIED, "sw in(Qkw.dir + Qkw.dif)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:3", Axes.NOT_SPECIFIED, "lw in(Qlw.sky)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
		aPlot.pushGraph(new Graph(dataFileName, "1:4", Axes.NOT_SPECIFIED, "lw out(5.6E8*T0^4)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:5", Axes.NOT_SPECIFIED, "net radiation", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:6", Axes.NOT_SPECIFIED, "soil heat(G)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:7", Axes.NOT_SPECIFIED, "available energy(netradiation-G)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:8", Axes.NOT_SPECIFIED, "latent heat(LE)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:9", Axes.NOT_SPECIFIED, "sensible heat(H)", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:10", Axes.NOT_SPECIFIED, "Rn-G-H-LE", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
		
//		aPlot.addExtra("plot " + "\"" 
//				+ dataFileName + "\" using 1:2 title \"sw in\" with linespoints , \""
//				+ dataFileName + "\" using 1:3 title \"lw in\" with linespoints , \""
//				+ dataFileName + "\" using 1:4 title \"lw out\" with linespoints , \""
//				+ dataFileName + "\" using 1:5 title \"net radiation\" with linespoints, \""
//				+ dataFileName + "\" using 1:6 title \"soil heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:7 title \"available energy\" with linespoints , \""
//				+ dataFileName + "\" using 1:8 title \"latent heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:9 title \"sensible heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:10 title \"Rn-G-H-LE\" with linespoints"
//				);
		
		try
		{
			System.out.println("Creating plot in " + outputDirectory);						
			String execStr = aPlot.plot();
			
			String[] commands = new String[]{"gnuplot", "\"" + outputDirectory + "\"" + "/*.txt"};
			Process aProcess = Runtime.getRuntime().exec(commands);
			System.out.println("Created file " + outputFile);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}	
	
	public String getAdditionalSelect(String newJoinId, String newJoinFieldName)
	{
		return
		" , rd" +
				newJoinId +
				".value as " +
				newJoinFieldName +
				" ";
	}
	
	public String getAdditionalJoin(String newJoinId, String baseJoinID, int runID)
	{
		return 
		" left outer join run_data rd" +
				newJoinId +
				" on rd" +
				baseJoinID +
				".run_id = " +
				runID +
				" and rd" +
				baseJoinID +
				".x=" +
				x +
				" and rd" +
				baseJoinID +
				".y=" +
				y +
				" and rd" +
				baseJoinID +
				".variable_time = rd" +
				newJoinId +
				".variable_time and rd" +
				newJoinId +
				".variable_type in (" +
				newJoinId +
				") and  rd" +
				newJoinId +
				".run_id = rd" +
				baseJoinID +
				".run_id and rd" +
				newJoinId +
				".x=rd" +
				baseJoinID +
				".x and rd" +
				newJoinId +
				".y=rd" +
				baseJoinID +
				".y ";
	}
	
	public String getQueryStr (int runID, double x, double y)
	{
		String Sw_Direct_RadiationID = "";
		String Sw_Diffuse_RadiationID = "";
		String T_Surface_KID = "2";
		String Surface_AlbedoID  = "20";
		String Longwave_Rad_from_environmentID  = "";
		
		String Soil_heat_FluxID  = "";
		String Sensible_heat_fluxID  = "";
		String Latent_heat_fluxID  = "";
		
		String building_heightID = "19";
		String deposition_speedID = "21";
		String exchange_coeff_heatID = "27";
		String lambert_factorID = "13";
		String longwave_radiation_budgetID = "32";
		String mass_boimetID = "43";
		String mass_deposedID = "36";
		String q_air_biometID = "40";
		String q_surfaceID = "5";
		String receptorsID = "45";
		String shadow_flagID = "25";
		String sky_view_faktorID = "18";
		String surface_expositionID = "24";
		String surface_inclinationID = "23";
		String t_air_boimetID = "39";
		String t_surface_changeID = "4";
		String t_surface_diffID = "3";
		String tmrt_boimetID = "41";
		String uv_above_surfaceID = "6";
		String walkability_valueID = "44";
		String water_fluxID = "35";
		String wind_speed_boimetID = "42";
		String z_boimetID = "38";
		String z_node_boimetID = "37";
		String z_topoID = "1";
		String Longwave_Rad_from_vegetationID  = "15";
		
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String enviVersion = metaData.get(ENVICommon.ENVI_VERSION);
		if (enviVersion.equals(ENVICommon.ENVI_VERSION3))
		{
			Sw_Direct_RadiationID = "11";
			Sw_Diffuse_RadiationID = "12";
			
			//T_Surface_KID = "2";
			//Surface_AlbedoID  = "20";
			Longwave_Rad_from_environmentID  = "16";
			Soil_heat_FluxID  = "10";
			Sensible_heat_fluxID  = "7";
			Latent_heat_fluxID  = "9";		
			
			exchange_coeff_heatID = "8";
			
			longwave_radiation_budgetID = "14";
			mass_deposedID = "22";
			water_fluxID = "17";
			Longwave_Rad_from_vegetationID  = "15";
			
		}
		if (enviVersion.equals(ENVICommon.ENVI_VERSION4))
		{
			Sw_Direct_RadiationID = "30";
			Sw_Diffuse_RadiationID = "31";
			
			//T_Surface_KID = "2";
			//Surface_AlbedoID  = "20";
			Longwave_Rad_from_environmentID  = "34";
			Soil_heat_FluxID  = "29";
			Sensible_heat_fluxID  = "26";
			Latent_heat_fluxID  = "28";
			
			
		}
		
		
		StringBuffer queryBase = new StringBuffer(" select " );
		
		StringBuffer select = new StringBuffer(" rd" +
				Surface_AlbedoID +
				".variable_time" +
				",rd" +
				Surface_AlbedoID +
				".value as " +
				SURFACE_ALBEDO +				
				" , rd" +
				Sw_Diffuse_RadiationID +
				".value as " +
				SW_DIFFUSE_RADIATION +
				", rd" +
				T_Surface_KID +
				".value as " +
				T_SURFACE_K +
				", rd" +
				Sw_Direct_RadiationID +
				".value as " +
				SW_DIRECT_RADIATION +
				" ");
		
		StringBuffer from = new StringBuffer(" from run_data rd" +
				Surface_AlbedoID +
				" "  );
		
		StringBuffer join = new StringBuffer( "	left outer join run_data rd" +
				Sw_Diffuse_RadiationID +
				" on  rd" +
				Surface_AlbedoID +
				".run_id = " +
				runID +
				" and rd" +
				Surface_AlbedoID +
				".x=" +
				x +
				" and rd" +
				Surface_AlbedoID +
				".y=" +
				y +
				" and rd" +
				Surface_AlbedoID +
				".variable_time = rd" +
				Sw_Diffuse_RadiationID +
				".variable_time and rd" +
				Sw_Diffuse_RadiationID +
				".variable_type in (" +
				Sw_Diffuse_RadiationID +
				") "
				+ "	and rd" +
				Sw_Diffuse_RadiationID +
						".run_id = rd" +
						Surface_AlbedoID +
						".run_id and rd" +
						Sw_Diffuse_RadiationID +
						".x=rd" +
						Surface_AlbedoID +
						".x and rd" +
						Sw_Diffuse_RadiationID +
						".y=rd" +
						Surface_AlbedoID +
						".y "
				+ "	left outer join run_data rd" +
				T_Surface_KID +
						" on  rd" +
						Surface_AlbedoID +
						".run_id = " +
						runID +
						" and rd" +
						Surface_AlbedoID +
						".x=" +
						x +
						" and rd" +
						Surface_AlbedoID +
						".y=" +
						y +
						" and rd" +
						Surface_AlbedoID +
						".variable_time = rd" +
						T_Surface_KID +
						".variable_time and rd" +
						T_Surface_KID +
						".variable_type in (" +
						T_Surface_KID +
						")  " 
						+ "		and  rd" +
						T_Surface_KID +
						".run_id = rd" +
						Surface_AlbedoID +
						".run_id and rd" +
						T_Surface_KID +
						".x=rd" +
						Surface_AlbedoID +
						".x and rd" +
						T_Surface_KID +
						".y=rd" +
						Surface_AlbedoID +
						".y ");
		
		StringBuffer queryEnd = new StringBuffer( "	right outer join run_data rd" +
				Sw_Direct_RadiationID +
				" on  rd" +
				Sw_Direct_RadiationID +
				".run_id = " +
				runID +
				" and rd" +
				Sw_Direct_RadiationID +
				".x=" +
				x +
				" and rd" +
				Sw_Direct_RadiationID +
				".y=" +
				y +
				" and rd" +
				Sw_Direct_RadiationID +
				".variable_time = rd" +
				Surface_AlbedoID +
				".variable_time and rd" +
				Sw_Direct_RadiationID +
				".variable_type in (" +
				Sw_Direct_RadiationID +
				") " 
				+ 	" and rd" +
				Sw_Direct_RadiationID +
						".run_id = rd" +
						Surface_AlbedoID +
						".run_id and rd" +
						Sw_Direct_RadiationID +
						".x=rd" +
						Surface_AlbedoID +
						".x and rd" +
						Sw_Direct_RadiationID +
						".y=rd" +
						Surface_AlbedoID +
						".y "
				+ " where rd" +
						Surface_AlbedoID +
						".variable_type in (" +
						Surface_AlbedoID +
						") " +
						" order by rd" +
						Surface_AlbedoID +
						".variable_time ");
		
		String newJoinId = Sensible_heat_fluxID;
		String newJoinFieldName = SENSIBLE_HEAT_FLUX;
		String baseJoinID = Surface_AlbedoID;
		
		select.append(getAdditionalSelect(newJoinId, newJoinFieldName));
		join.append(getAdditionalJoin(newJoinId, baseJoinID, runID));
		
		select.append(getAdditionalSelect(Longwave_Rad_from_environmentID, LONGWAVE_RAD_FROM_ENVIRONMENT));
		join.append(getAdditionalJoin(Longwave_Rad_from_environmentID, baseJoinID, runID));
		
		select.append(getAdditionalSelect(Soil_heat_FluxID, SOIL_HEAT_FLUX));
		join.append(getAdditionalJoin(Soil_heat_FluxID, baseJoinID, runID));
		
		select.append(getAdditionalSelect(Latent_heat_fluxID, LATENT_HEAT_FLUX));
		join.append(getAdditionalJoin(Latent_heat_fluxID, baseJoinID, runID));
		
		select.append(getAdditionalSelect(longwave_radiation_budgetID, LONGWAVE_RADIATION_BUDGET));
		join.append(getAdditionalJoin(longwave_radiation_budgetID, baseJoinID, runID));		
		
		
//		select.append(" , rd" +
//				newJoinId +
//				".value as " +
//				newJoinFieldName +
//				" ");
		
//		join.append(" left outer join run_data rd" +
//				newJoinId +
//				" on rd" +
//				baseJoinID +
//				".run_id = " +
//				runID +
//				" and rd" +
//				baseJoinID +
//				".x=" +
//				x +
//				" and rd" +
//				baseJoinID +
//				".y=" +
//				y +
//				" and rd" +
//				baseJoinID +
//				".variable_time = rd" +
//				newJoinId +
//				".variable_time and rd" +
//				newJoinId +
//				".variable_type in (" +
//				newJoinId +
//				") and  rd" +
//				newJoinId +
//				".run_id = rd" +
//				baseJoinID +
//				".run_id and rd" +
//				newJoinId +
//				".x=rd" +
//				baseJoinID +
//				".x and rd" +
//				newJoinId +
//				".y=rd" +
//				baseJoinID +
//				".y ");
		
		

		
		if (dataToPlot.contains(BUILDING_HEIGHT))
		{
			select.append(getAdditionalSelect(building_heightID, BUILDING_HEIGHT));
			join.append(getAdditionalJoin(building_heightID, baseJoinID, runID));
		}
			
		if (dataToPlot.contains(DEPOSITION_SPEED))
		{
			select.append(getAdditionalSelect(deposition_speedID, DEPOSITION_SPEED));
			join.append(getAdditionalJoin(deposition_speedID, baseJoinID, runID));
		}
		
		if (dataToPlot.contains(EXCHANGE_COEFF_HEAT))
		{
			select.append(getAdditionalSelect(exchange_coeff_heatID, EXCHANGE_COEFF_HEAT));
			join.append(getAdditionalJoin(exchange_coeff_heatID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(LAMBERT_FACTOR))
		{
			select.append(getAdditionalSelect(lambert_factorID, LAMBERT_FACTOR));
			join.append(getAdditionalJoin(lambert_factorID, baseJoinID, runID));
		}		
		
//		if (dataToPlot.contains(LONGWAVE_RADIATION_BUDGET))
//		{
//			select.append(getAdditionalSelect(longwave_radiation_budgetID, LONGWAVE_RADIATION_BUDGET));
//			join.append(getAdditionalJoin(longwave_radiation_budgetID, baseJoinID, runID));
//		}		
		
		if (dataToPlot.contains(MASS_BOIMET) && enviVersion.equals(ENVICommon.ENVI_VERSION4))  //V4 only
		{
			select.append(getAdditionalSelect(mass_boimetID, MASS_BOIMET));
			join.append(getAdditionalJoin(mass_boimetID, baseJoinID, runID));	
		}		
				
		if (dataToPlot.contains(MASS_DEPOSED))
		{
			select.append(getAdditionalSelect(mass_deposedID, MASS_DEPOSED));
			join.append(getAdditionalJoin(mass_deposedID, baseJoinID, runID));		
		}
				
		if (dataToPlot.contains(Q_AIR_BIOMET))
		{
			select.append(getAdditionalSelect(q_air_biometID, Q_AIR_BIOMET));
			join.append(getAdditionalJoin(q_air_biometID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(Q_SURFACE))
		{
			select.append(getAdditionalSelect(q_surfaceID, Q_SURFACE));
			join.append(getAdditionalJoin(q_surfaceID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(RECEPTORS))
		{
			select.append(getAdditionalSelect(receptorsID, RECEPTORS));
			join.append(getAdditionalJoin(receptorsID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(SHADOW_FLAG))
		{
			select.append(getAdditionalSelect(shadow_flagID, SHADOW_FLAG));
			join.append(getAdditionalJoin(shadow_flagID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(SKY_VIEW_FAKTOR))
		{
			select.append(getAdditionalSelect(sky_view_faktorID, SKY_VIEW_FAKTOR));
			join.append(getAdditionalJoin(sky_view_faktorID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(SURFACE_EXPOSITION))
		{
			select.append(getAdditionalSelect(surface_expositionID, SURFACE_EXPOSITION));
			join.append(getAdditionalJoin(surface_expositionID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(SURFACE_INCLINATION))
		{
			select.append(getAdditionalSelect(surface_inclinationID, SURFACE_INCLINATION));
			join.append(getAdditionalJoin(surface_inclinationID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(T_AIR_BOIMET))
		{
			select.append(getAdditionalSelect(t_air_boimetID, T_AIR_BOIMET));
			join.append(getAdditionalJoin(t_air_boimetID, baseJoinID, runID));
		}		
				
		if (dataToPlot.contains(T_SURFACE_CHANGE))
		{
			select.append(getAdditionalSelect(t_surface_changeID, T_SURFACE_CHANGE));
			join.append(getAdditionalJoin(t_surface_changeID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(T_SURFACE_DIFF))
		{
			select.append(getAdditionalSelect(t_surface_diffID, T_SURFACE_DIFF));
			join.append(getAdditionalJoin(t_surface_diffID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(TMRT_BOIMET))
		{
			select.append(getAdditionalSelect(tmrt_boimetID, TMRT_BOIMET));
			join.append(getAdditionalJoin(tmrt_boimetID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(UV_ABOVE_SURFACE))
		{
			select.append(getAdditionalSelect(uv_above_surfaceID, UV_ABOVE_SURFACE));
			join.append(getAdditionalJoin(uv_above_surfaceID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(WALKABILITY_VALUE))
		{
			select.append(getAdditionalSelect(walkability_valueID, WALKABILITY_VALUE));
			join.append(getAdditionalJoin(walkability_valueID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(WATER_FLUX))
		{
			select.append(getAdditionalSelect(water_fluxID, WATER_FLUX));
			join.append(getAdditionalJoin(water_fluxID, baseJoinID, runID));
		}		
	
		if (dataToPlot.contains(WIND_SPEED_BOIMET))
		{
			select.append(getAdditionalSelect(wind_speed_boimetID, WIND_SPEED_BOIMET));
			join.append(getAdditionalJoin(wind_speed_boimetID, baseJoinID, runID));
		}		
	
		if (dataToPlot.contains(Z_BOIMET))
		{
			select.append(getAdditionalSelect(z_boimetID, Z_BOIMET));
			join.append(getAdditionalJoin(z_boimetID, baseJoinID, runID));
		}		
					
		if (dataToPlot.contains(Z_NODE_BOIMET))
		{
			select.append(getAdditionalSelect(z_node_boimetID, Z_NODE_BOIMET));
			join.append(getAdditionalJoin(z_node_boimetID, baseJoinID, runID));
		}		
		
		if (dataToPlot.contains(Z_TOPO))
		{
			select.append(getAdditionalSelect(z_topoID, Z_TOPO));
			join.append(getAdditionalJoin(z_topoID, baseJoinID, runID));
		}


		return queryBase.toString() + select.toString() + from.toString() + join.toString() + queryEnd.toString();
	}

	public ArrayList<TreeMap<String, String>> getDataset(int runID, double x, double y)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String enviVersion = metaData.get(ENVICommon.ENVI_VERSION);
		ArrayList<TreeMap<String, String>> returnData = new ArrayList<TreeMap<String, String>>();		
		
		String query = getQueryStr(runID, x, y);

		Connection conn = common.getMySqlConnection();
		try
		{
			Statement stat = conn.createStatement();
			System.out.println(query);
			ResultSet rs = stat.executeQuery(query);

			while (rs.next())
			{
				boolean rediculous = false;
				TreeMap<String, String> hourlyData = new TreeMap<String, String>();

				double swDirectRadiation = rs.getDouble(SW_DIRECT_RADIATION);
				double swDiffuseRadiation = rs.getDouble(SW_DIFFUSE_RADIATION);
				double tSurfaceK = rs.getDouble(T_SURFACE_K);
				double surfaceAlbedo = rs.getDouble(SURFACE_ALBEDO);
				double longwaveRadFromEnvironment = rs.getDouble(LONGWAVE_RAD_FROM_ENVIRONMENT);
				double soilHeatFlux = rs.getDouble(SOIL_HEAT_FLUX);
				double sensibleHeatFlux = rs.getDouble(SENSIBLE_HEAT_FLUX);
				double latentHeatFlux = rs.getDouble(LATENT_HEAT_FLUX);
				double longwaveRadiationBudget = rs.getDouble(LONGWAVE_RADIATION_BUDGET);
				Timestamp variableTime = rs.getTimestamp(VARIABLE_TIME);
				

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
				String variableTimeStr = formatter.format(variableTime);

				hourlyData.put(SW_DIRECT_RADIATION, "" + swDirectRadiation);
				hourlyData.put(SW_DIFFUSE_RADIATION, "" + swDiffuseRadiation);
				hourlyData.put(T_SURFACE_K, "" + tSurfaceK);
				hourlyData.put(SURFACE_ALBEDO, "" + surfaceAlbedo);
				hourlyData.put(LONGWAVE_RAD_FROM_ENVIRONMENT, "" + longwaveRadFromEnvironment);
				hourlyData.put(LATENT_HEAT_FLUX, "" + Math.round(latentHeatFlux));
				hourlyData.put(SOIL_HEAT_FLUX, "" + Math.round(soilHeatFlux));
				hourlyData.put(SENSIBLE_HEAT_FLUX, "" + Math.round(sensibleHeatFlux));
				hourlyData.put(VARIABLE_TIME, variableTimeStr);
				hourlyData.put(LONGWAVE_RADIATION_BUDGET, "" + longwaveRadiationBudget );
				
				double swIn = swDirectRadiation + swDiffuseRadiation;
				double swOut = (surfaceAlbedo) * (swIn);
//				double lwIn = longwaveRadFromEnvironment;
//				double lwOut = 0.0000000567 * Math.pow(tSurfaceK, 4);
				
				double blackBodyRadiation = 0.0000000567 * Math.pow(tSurfaceK, 4);
				double lwOut = blackBodyRadiation;
				double lwIn = longwaveRadiationBudget + blackBodyRadiation  ;
								
				double netRadiation = (swIn - swOut) + (lwIn - lwOut);
				
				// RN = G + LE + H
				double availableEnergy = netRadiation - soilHeatFlux;
				//double availableEnergy = sensibleHeatFlux + latentHeatFlux + soilHeatFlux;
				
				double dailyEnergyBalance = netRadiation - soilHeatFlux - sensibleHeatFlux - latentHeatFlux;
				
				//filter out clearly bad data 
				if (dailyEnergyBalance > 99999)
				{
					rediculous = true;
				}
				if (dailyEnergyBalance < -99999)
				{
					rediculous = true;
				}

				hourlyData.put(NET_RADIATION, "" + Math.round(netRadiation));
				hourlyData.put(AVAILABLE_ENERGY, "" + Math.round(availableEnergy));
				
				hourlyData.put(SW_IN, "" + Math.round(swIn));
				hourlyData.put(BLACK_BODY_RADIATION, "" + Math.round(blackBodyRadiation));
				hourlyData.put(SW_OUT, "" + Math.round(swOut));
				hourlyData.put(LW_IN, "" + Math.round(lwIn));
				hourlyData.put(LW_OUT, "" + Math.round(lwOut));
				hourlyData.put(DAILY_ENERGY_BALANCE, "" + Math.round(dailyEnergyBalance));
				
				if (dataToPlot.contains(DEPOSITION_SPEED))
				{
					hourlyData.put(DEPOSITION_SPEED, "" + rs.getDouble(DEPOSITION_SPEED) );
				}
				if (dataToPlot.contains(BUILDING_HEIGHT))
				{
					hourlyData.put(BUILDING_HEIGHT, "" + rs.getDouble(BUILDING_HEIGHT) );
				}
				if (dataToPlot.contains(WALKABILITY_VALUE))
				{
					hourlyData.put(WALKABILITY_VALUE, "" + rs.getDouble(WALKABILITY_VALUE) );
				}
				if (dataToPlot.contains(WATER_FLUX))
				{
					hourlyData.put(WATER_FLUX, "" + rs.getDouble(WATER_FLUX) );
				}
				if (dataToPlot.contains(WIND_SPEED_BOIMET))
				{
					hourlyData.put(WIND_SPEED_BOIMET, "" + rs.getDouble(WIND_SPEED_BOIMET) );
				}
				if (dataToPlot.contains(Z_BOIMET))
				{
					hourlyData.put(Z_BOIMET, "" + rs.getDouble(Z_BOIMET) );
				}
				if (dataToPlot.contains(Z_NODE_BOIMET))
				{
					hourlyData.put(Z_NODE_BOIMET, "" + rs.getDouble(Z_NODE_BOIMET) );
				}
				if (dataToPlot.contains(Z_TOPO))
				{
					hourlyData.put(Z_TOPO, "" + rs.getDouble(Z_TOPO) );		
				}
				if (dataToPlot.contains(MASS_DEPOSED))
				{
					hourlyData.put(MASS_DEPOSED, "" + rs.getDouble(MASS_DEPOSED) );
				}
				if (dataToPlot.contains(Q_AIR_BIOMET))
				{
					hourlyData.put(Q_AIR_BIOMET, "" + rs.getDouble(Q_AIR_BIOMET) );
				}
				if (dataToPlot.contains(Q_SURFACE))
				{
					hourlyData.put(Q_SURFACE, "" + rs.getDouble(Q_SURFACE) );
				}
				if (dataToPlot.contains(RECEPTORS))
				{
					hourlyData.put(RECEPTORS, "" + rs.getDouble(RECEPTORS) );
				}
				if (dataToPlot.contains(SHADOW_FLAG))
				{
					hourlyData.put(SHADOW_FLAG, "" + rs.getDouble(SHADOW_FLAG) );
				}
				if (dataToPlot.contains(SKY_VIEW_FAKTOR))
				{
					hourlyData.put(SKY_VIEW_FAKTOR, "" + rs.getDouble(SKY_VIEW_FAKTOR) );
				}
				if (dataToPlot.contains(SURFACE_EXPOSITION))
				{
					hourlyData.put(SURFACE_EXPOSITION, "" + rs.getDouble(SURFACE_EXPOSITION) );
				}
				if (dataToPlot.contains(SURFACE_INCLINATION))
				{
					hourlyData.put(SURFACE_INCLINATION, "" + rs.getDouble(SURFACE_INCLINATION) );
				}
				if (dataToPlot.contains(T_AIR_BOIMET))
				{
					hourlyData.put(T_AIR_BOIMET, "" + rs.getDouble(T_AIR_BOIMET) );
				}
				if (dataToPlot.contains(T_SURFACE_CHANGE))
				{
					hourlyData.put(T_SURFACE_CHANGE, "" + rs.getDouble(T_SURFACE_CHANGE) );
				}
				
				if (dataToPlot.contains(T_SURFACE_DIFF))
				{
					hourlyData.put(T_SURFACE_DIFF, "" + rs.getDouble(T_SURFACE_DIFF) );
				}
				if (dataToPlot.contains(TMRT_BOIMET))
				{
					hourlyData.put(TMRT_BOIMET, "" + rs.getDouble(TMRT_BOIMET) );
				}
				if (dataToPlot.contains(UV_ABOVE_SURFACE))
				{
					hourlyData.put(UV_ABOVE_SURFACE, "" + rs.getDouble(UV_ABOVE_SURFACE) );
				}
				if (dataToPlot.contains(MASS_BOIMET) && enviVersion.equals(ENVICommon.ENVI_VERSION4))				
				{
					hourlyData.put(MASS_BOIMET, "" + rs.getDouble(MASS_BOIMET) );
				}
//				if (dataToPlot.contains(LONGWAVE_RADIATION_BUDGET))
//				{
//					hourlyData.put(LONGWAVE_RADIATION_BUDGET, "" + rs.getDouble(LONGWAVE_RADIATION_BUDGET) );
//				}
				if (dataToPlot.contains(LAMBERT_FACTOR))
				{
					hourlyData.put(LAMBERT_FACTOR, "" + rs.getDouble(LAMBERT_FACTOR) );
				}
				if (dataToPlot.contains(EXCHANGE_COEFF_HEAT))
				{
					hourlyData.put(EXCHANGE_COEFF_HEAT, "" + rs.getDouble(EXCHANGE_COEFF_HEAT) );
				}
					
				// throw out rediculous data
				if (rediculous)
				{
					continue;
				}
				
				returnData.add(hourlyData);
				System.out.println(hourlyData.toString());
				System.out.println();
			}

			conn.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return returnData;

	}
	


	public void generateDataFileFromDataPoints(
			ArrayList<TreeMap<String, String>> data, int runID)
	{
		TreeMap<String,String> metaData = common.getMetaDataForRun(runID);
		String filename = metaData.get(ENVICommon.RUN_NAME);
		String outputDirectory = metaData.get(ENVICommon.DATA_DIR) + "../graphs";
		common.createDirectory(outputDirectory);
		String dataFileName = outputDirectory + File.separator + filename
				+ ".dat";

		if (data != null)
		{

			StringBuffer outputFileStr = new StringBuffer();
			outputFileStr
					.append("# Time" +
							"\t" +  '\t' + '\t' +
							"swIn" +
							"\t"  +
							"lwIn" +
							"\t"  +
							"lwOut" +
							"\t"  +
							"net_radiation" +
							"\t"  +
							"soil_heat" +
							"\t" +
							"availableEnergy" +
							"\t" +
							"latentHeat" +
							"\t"  +
							"sensible_heat" +
							"\t" + 
							"Rn-G-H-LE" 
							+ '\n');

			for (TreeMap<String, String> dataItem : data)
			{
				String swIn = dataItem.get(SW_IN);
				String lwIn = dataItem.get(LW_IN);
				String netRadiation = dataItem.get(NET_RADIATION);
				String availableEnergy = dataItem.get(AVAILABLE_ENERGY);
				String soilHeat = dataItem.get(SOIL_HEAT_FLUX);
				String sensibleHeat = dataItem.get(SENSIBLE_HEAT_FLUX);
				String variableTime = dataItem.get(VARIABLE_TIME);
				String latentHeat = dataItem.get(LATENT_HEAT_FLUX); 
				String dailyEnergyBalance = dataItem.get(DAILY_ENERGY_BALANCE);				
				String lwOut = dataItem.get(LW_OUT);

				outputFileStr.append(variableTime + '\t'  
						+ swIn + '\t' 
						+ lwIn + '\t' 
						+ lwOut + '\t'
						+ netRadiation + '\t'+ '\t' 
						+ soilHeat + '\t' + '\t'
						+ availableEnergy + '\t' + '\t'
						+ latentHeat + '\t' + '\t'
						+ sensibleHeat + '\t' + '\t'
						+ dailyEnergyBalance
						+ '\n');
			}

			common.writeFile(outputFileStr.toString(), dataFileName);
		}

		String outputFile = outputDirectory + File.separator + filename
				+ ".png";

		Plot.setGnuplotExecutable("/usr/bin/gnuplot");
		Plot.setPlotDirectory(outputDirectory);
		//Graph aGraph = null;
		Plot aPlot = new Plot();
		aPlot.setDataFileName(dataFileName);
		
		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");

		aPlot.setGrid();
		aPlot.setKey("right box");
		aPlot.setXLabel("Date/Time");
		aPlot.setYLabel("W/m2");		
		aPlot.setTitle("Daily energy balance of run - " + metaData.get(common.RUN_DESC));
		aPlot.setTimeFormat("%Y-%m-%d-%H:%M");
		aPlot.addExtra("set format x '%H'");
		aPlot.addExtra("set xdata time");
				
		aPlot.pushGraph(new Graph(dataFileName, "1:2", Axes.NOT_SPECIFIED, "sw in", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:3", Axes.NOT_SPECIFIED, "lw in", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
		aPlot.pushGraph(new Graph(dataFileName, "1:4", Axes.NOT_SPECIFIED, "lw out", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:5", Axes.NOT_SPECIFIED, "net radiation", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:6", Axes.NOT_SPECIFIED, "soil heat", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:7", Axes.NOT_SPECIFIED, "available energy", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:8", Axes.NOT_SPECIFIED, "latent heat", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:9", Axes.NOT_SPECIFIED, "sensible heat", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));
		aPlot.pushGraph(new Graph(dataFileName, "1:10", Axes.NOT_SPECIFIED, "Rn-G-H-LE", Style.LINESPOINTS, LineType.NOT_SPECIFIED, PointType.NOT_SPECIFIED));		
		
//		aPlot.addExtra("plot " + "\"" 
//				+ dataFileName + "\" using 1:2 title \"sw in\" with linespoints , \""
//				+ dataFileName + "\" using 1:3 title \"lw in\" with linespoints , \""
//				+ dataFileName + "\" using 1:4 title \"lw out\" with linespoints , \""
//				+ dataFileName + "\" using 1:5 title \"net radiation\" with linespoints, \""
//				+ dataFileName + "\" using 1:6 title \"soil heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:7 title \"available energy\" with linespoints , \""
//				+ dataFileName + "\" using 1:8 title \"latent heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:9 title \"sensible heat\" with linespoints , \""
//				+ dataFileName + "\" using 1:10 title \"Rn-G-H-LE\" with linespoints"
//				);
		
		try
		{
			System.out.println("Creating plot in " + outputDirectory);						
			String execStr = aPlot.plot();
			//Process aProcess = Runtime.getRuntime().exec(execStr);
			String[] commands = new String[]{"/usr/bin/gnuplot", "\"" + outputDirectory + "\"" + "/*.txt"};
			Process aProcess = Runtime.getRuntime().exec(commands);
			System.out.println("Created file " + outputFile);
			
			commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			aProcess = Runtime.getRuntime().exec(commands);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}


}
