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
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/*
The associated info file (.EDI) describes the dimension of the .EDT file with the following format:

1: Title of sim (Text)
2: number of x-grids
3: number of y-grids
4: number of z-grids
5: number of variables in file
6ff: Name of variables in file, Unit in "(" XXXX ")", one line for each VAR

{after the variable names}

Gridspacing in x-direction (one value for each grid)
For x:=1 to XX {Write dx[x] }

Gridspacing in y-direction (one value for each grid)
For y:=1 to YY {Write dy[y] }

Gridspacing in z-direction (one value for each grid)
For z:=1 to ZZ {Write dz[z] }

{after the spacing}
some additional infos, not required to read the file 
*/

public class ReadEDIFile {
		
	private String titleOfSim;
	private int numOfXGrids;
	private int numOfYGrids;
	private int numOfZGrids;
	private int numOfVariablesInFile;
	private ArrayList<String> fileVariables = new ArrayList<String>();
	private ArrayList<String> gridSpacingX = new ArrayList<String>();
	private ArrayList<String> gridSpacingY = new ArrayList<String>();
	private ArrayList<String> gridSpacingZ = new ArrayList<String>();
	private String title;
	private String day;
	private String time;
	private String stage;
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDay()
	{
		return day;
	}

	public void setDay(String day)
	{
		this.day = day;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getStage()
	{
		return stage;
	}

	public void setStage(String stage)
	{
		this.stage = stage;
	}

	public ReadEDIFile(String filename) {
		super();
		readEDIFile(filename);		
	}
	
	private void parseTitle(String titleOfSim)
	{
		StringTokenizer st = new StringTokenizer(titleOfSim, " ");
		this.setTitle((String)st.nextElement());
		this.setTime((String)st.nextElement());
		this.setDay((String)st.nextElement());
		try
		{
			this.setStage((String)st.nextElement());
		}
		catch (NoSuchElementException e)
		{
			this.setStage("");
		}
		
	}
	
	@SuppressWarnings({ "deprecation"})
	public void readEDIFile(String filename)
	{
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);

			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			
			titleOfSim = dis.readLine();
			parseTitle(titleOfSim);
			numOfXGrids = new Integer(dis.readLine()).intValue();
			numOfYGrids = new Integer(dis.readLine()).intValue();
			numOfZGrids = new Integer(dis.readLine()).intValue();
			numOfVariablesInFile = new Integer(dis.readLine()).intValue();
			
			for (int i=0;i<numOfVariablesInFile;i++)
			{
				String variable = dis.readLine();
				fileVariables.add(variable);
			}
			
			//use up label line
			String gridSpacingLabel = dis.readLine();
			
			for (int x=0;x<numOfXGrids;x++)
			{
				String spacing = dis.readLine();
				gridSpacingX.add(spacing);
			}
			for (int y=0;y<numOfYGrids;y++)
			{
				String spacing = dis.readLine();
				gridSpacingY.add(spacing);
			}
			for (int z=0;z<numOfZGrids;z++)
			{
				String spacing = dis.readLine();
				gridSpacingZ.add(spacing);
			}
			
			// dispose all the resources after using them.
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getGridSpacingX() {
		return gridSpacingX;
	}

	public void setGridSpacingX(ArrayList<String> gridSpacingX) {
		this.gridSpacingX = gridSpacingX;
	}

	public ArrayList<String> getGridSpacingY() {
		return gridSpacingY;
	}

	public void setGridSpacingY(ArrayList<String> gridSpacingY) {
		this.gridSpacingY = gridSpacingY;
	}

	public ArrayList<String> getGridSpacingZ() {
		return gridSpacingZ;
	}

	public void setGridSpacingZ(ArrayList<String> gridSpacingZ) {
		this.gridSpacingZ = gridSpacingZ;
	}

	public String getTitleOfSim() {
		return titleOfSim;
	}

	public void setTitleOfSim(String titleOfSim) {
		this.titleOfSim = titleOfSim;
	}

	public int getNumOfXGrids() {
		return numOfXGrids;
	}

	public void setNumOfXGrids(int numOfXGrids) {
		this.numOfXGrids = numOfXGrids;
	}

	public int getNumOfYGrids() {
		return numOfYGrids;
	}

	public void setNumOfYGrids(int numOfYGrids) {
		this.numOfYGrids = numOfYGrids;
	}

	public int getNumOfZGrids() {
		return numOfZGrids;
	}

	public void setNumOfZGrids(int numOfZGrids) {
		this.numOfZGrids = numOfZGrids;
	}

	public int getNumOfVariablesInFile() {
		return numOfVariablesInFile;
	}

	public void setNumOfVariablesInFile(int numOfVariablesInFile) {
		this.numOfVariablesInFile = numOfVariablesInFile;
	}

	public ArrayList<String> getFileVariables() {
		return fileVariables;
	}

	public void setFileVariables(ArrayList<String> fileVariables) {
		this.fileVariables = fileVariables;
	}

}
