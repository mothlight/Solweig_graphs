package au.edu.monash.mes.envimet;

public class DataPoint
{
	
	private double x;
	public double getX()
	{
		return x;
	}

	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getZ()
	{
		return z;
	}


	@Override
	public String toString()
	{
		return "DataPoint [x=" + x + ", y=" + y + ", z=" + z + ", value="
				+ value + ", outputFile=" + outputFile + "]";
	}

	public DataPoint(double x, double y, double z, double value, String outputFile)
	{
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = value;
		this.outputFile = outputFile;
	}

	public void setZ(double z)
	{
		this.z = z;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	private double y;
	private double z;
	private double value;
	
	public String getOutputFile()
	{
		return outputFile;
	}

	public void setOutputFile(String outputFile)
	{
		this.outputFile = outputFile;
	}

	private String outputFile;

	public DataPoint()
	{
		super();		
	}

}
