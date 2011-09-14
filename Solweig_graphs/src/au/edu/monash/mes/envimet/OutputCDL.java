package au.edu.monash.mes.envimet;
/* ====================================================================
Kerry Nice
Monash University
 ==================================================================== */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class OutputCDL
{
	
	public static final String OUTPUT_FILENAME = "/home/nice/grads/javaout.cdl";
	public static final String COMPILE_CDL_CMD = "/home/nice/bin/compile_cdl_grads.sh";
	/*
	 *  $ cat "/home/nice/bin/compile_cdl_grads.sh"
	 *	cd /home/nice/grads
	 *  ncgen -o javaout.nc javaout.cdl
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		OutputCDL outputCDL = new OutputCDL();	
		outputCDL.outputExampleCDL(outputCDL);
	}
	
	public void outputExampleCDL(OutputCDL outputCDL)
	{
		ExampleCDLData exampleCDLData = new ExampleCDLData();

		String[] uArray = exampleCDLData.uString.split("\\,",-1);
		System.out.println("uArray=" + uArray);
		
		String[] vArray = exampleCDLData.vString.split("\\,",-1);
		
		outputCDL.writeFile(outputCDL.constructExampleData(uArray, vArray));
		outputCDL.compileCDL();		
	}
	
	public void compileCDL()
	{
		 try {
             Runtime rt = Runtime.getRuntime();
             Process pr = rt.exec(COMPILE_CDL_CMD);

             BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

             String line=null;

             while((line=input.readLine()) != null) {
                 System.out.println(line);
             }

             int exitVal = pr.waitFor();
             System.out.println("Exited with error code "+exitVal);

         } catch(Exception e) {
             System.out.println(e.toString());
             e.printStackTrace();
         }
	}
	
	public void writeFile(String text)	
	{		
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object

        try
        {
                out = new FileOutputStream(OUTPUT_FILENAME);
                p = new PrintStream( out );
                p.println (text);
                p.close();
        }
        catch (Exception e)
        {
                System.err.println ("Error writing to file");
        }
		
	}
	
	private String constructExampleData(String[] uArray, String[] vArray)
	{
		StringBuffer textBuffer = new StringBuffer();
//		int latitude = 73;
//		int longitude = 144;
		
		int latitude = 5;
		int longitude = 10;
		
		double latitudeStart = 0.0;
		double latitudeWidth = 0.0001; 
		
		StringBuffer latStr = new StringBuffer("		 latitude = ");
		for (int i=0;i<latitude-1;i++)
		{
			latStr.append(latitudeStart + ", ");
			latitudeStart += latitudeWidth;
		}
		latStr.append(latitudeStart + ";\n ");
		
		System.out.println(latStr.toString());		
		
		double longitudeStart = 0.0;
		double longitudeWidth = 0.0001; 
		
		StringBuffer lonStr = new StringBuffer("		 longitude = ");
		for (int i=0;i<longitude-1;i++)
		{
			lonStr.append(longitudeStart + ", ");
			longitudeStart += longitudeWidth;
		}
		lonStr.append(longitudeStart + ";\n ");
		
		System.out.println(lonStr.toString());
		
		int currentUCount = 0;
		
		StringBuffer uStr = new StringBuffer("		 u = ");
		for (int i=0;i<latitude;i++)
		{
			for (int j=0;j<longitude;j++)
			{
				System.out.println(uArray[currentUCount]);
				
				uStr.append(uArray[currentUCount]);
				
				if (currentUCount < (latitude * longitude)-1 )
				{
					uStr.append(", ");
				}
				currentUCount++;
			}
		}
		uStr.append(";\n ");
		
		System.out.println(uStr.toString());	
		
		int currentvCount =0;
		StringBuffer vStr = new StringBuffer("		 v = ");
		for (int i=0;i<latitude;i++)
		{
			for (int j=0;j<longitude;j++)
			{
				System.out.println(vArray[currentvCount]);
				vStr.append(vArray[currentvCount]);
				if (currentvCount < (latitude * longitude) -1 )
				{
					vStr.append(", ");
				}
				currentvCount++;
			}
		}
		vStr.append(";\n ");
		
		System.out.println(vStr.toString());		
		
		textBuffer.append(
		
		"netcdf wind_comps {\n\n" +
		"dimensions:\n\n" +
		"	time = UNLIMITED ; // (1 currently)\n" +
		"	level = 1 ;\n" +
		"	latitude = " +
		latitude +
		" ;\n" +
		"	longitude = " +
		longitude +
		" ;\n" +
		"variables:\n" +
		"	double time(time) ;\n" +
		"		time:units = \"hours since 2003-1-1 0:0\" ;\n" +
		"		time:calendar = \"proleptic_gregorian\" ;\n" +
		"		time:axis = \"T\" ;\n" +
		"	float level(level) ;\n" +
		"		level:units = \"lev\" ;\n" +
		"		level:axis = \"Z\" ;\n" +
		"	float latitude(latitude) ;\n" +
		"		latitude:units = \"degrees_north\" ;\n" +
		"		latitude:axis = \"Y\" ;\n" +
		"	float longitude(longitude) ;\n" +
		"		longitude:axis = \"X\" ;\n" +
		"		longitude:units = \"degrees_east\" ;\n" +
		"		longitude:modulo = 360. ;\n" +
		"		longitude:topology = \"circular\" ;\n" +
		"	float u(time, level, latitude, longitude) ;\n" +
		"		u:name = \"u\" ;\n" +
		"		u:title = \"** U-velocity m s**-1\" ;\n" +
		"		u:standard_name = \"eastward_wind\" ;\n" +
		"		u:missing_value = 9.999e+20f ;\n" +
		"		u:units = \"m s**-1\" ;\n" +
		"	float v(time, level, latitude, longitude) ;\n" +
		"		v:name = \"v\" ;\n" +
		"		v:title = \"** V-velocity m s**-1\" ;\n" +
		"		v:standard_name = \"northward_wind\" ;\n" +
		"		v:missing_value = 9.999e+20f ;\n" +
		"		v:units = \"m s**-1\" ;\n" +		
		" // global attributes:\n" +		
		"data:\n" +
		"		 time = 0 ;\n" +
		"		 level = 1000 ;\n" +
		latStr +
		lonStr +
		uStr + 
		vStr +
		"}");		
		
		return textBuffer.toString();
	}

}
