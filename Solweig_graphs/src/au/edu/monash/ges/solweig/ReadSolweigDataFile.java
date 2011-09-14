package au.edu.monash.ges.solweig;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.io.OpenDialog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jgnuplot.Graph;
import org.jgnuplot.Splot;
import org.jgnuplot.Terminal;

import au.edu.monash.mes.envimet.ENVICommon;

public class ReadSolweigDataFile {

	ImagePlus img;
	ENVICommon common = new ENVICommon();
	float minimumValue = Float.MAX_VALUE;
	float maximumValue = -Float.MAX_VALUE;

	final float NO_DATA_VALUE = -9999.0f;
	final float GRID_SQUARE_SIZE = 3f;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ReadSolweigDataFile readSolweigDataFile = new ReadSolweigDataFile();

		// AverageDaytimeKdown_20110408_STAND.asc
		// AverageDaytimeGVF, AverageDaytimeKdown AverageDaytimeKup AverageDaytimeLdown AverageDaytimeLup AverageDaytimeTmrt AverageDiurnalLdown
		// AverageDiurnalLup AverageDiurnalTmrt

		// BuildingAndGroundSVF.asc

		// Ldown_at_20110408_15_STAND.asc
		// GVF Kdown Kup Ldown Lup Tmrt

		//String dir = "/home/kerryn/Documents/Work/Solweig-Monash/";
		String dir = "/home/nice/Climate_Research/Solweig-Monash/results2/ImagesAndAsciiGrids/";
		//String filename = "ascii_kerri_aoi_1.txt";
		//String filename = "Tmrt_at_20110408_16_STAND.asc";
		String filename = "Tmrt_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

		filename = "GVF_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

		filename = "Kdown_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

		filename = "Ldown_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

		filename = "Kup_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

		filename = "Lup_at_20110408_14_STAND.asc";
		readSolweigDataFile.plotOneDataFile(dir, filename);

//		ReadSolweigDataFile dataFile = new ReadSolweigDataFile();
//		BiDimensionalArray imageArray = dataFile.read(dir, filename);
//		if (dataFile.img == null)
//			return;

		//dataFile.img.setTitle("Some title");


		//dataFile.img.show();

		//System.out.println(imageArray.get(50,50));

//		String outputDirectory = dir + "graphs/";

//		String dataFileName = outputDirectory + filename + ".dat";
//		StringBuffer outputStr = new StringBuffer();
//
//		for (int i=0;i<dataFile.img.getHeight();i++)
//		{
//			for (int j=0;j<dataFile.img.getWidth();j++)
//			{
//				float value = (Float)imageArray.get(i,j);
//				String valueStr = "";
//				if (value == dataFile.NO_DATA_VALUE)
//				{
//					valueStr = "?";
//				}
//				else
//				{
//					valueStr = Float.toString(value);
//				}
//				outputStr.append((i+1)*dataFile.GRID_SQUARE_SIZE + " " + (j+1)*dataFile.GRID_SQUARE_SIZE + " " + valueStr + '\n');
//			}
//		}



//		String xLabel = "Label";
//		String outputFile = dataFileName + ".png";
//		dataFile.common.writeFile(outputStr.toString(), dataFileName);
//		//dataFile.plotData(dataFileName, outputFile, xLabel, outputDirectory);
//
//		System.out.println("Wrote out " + dataFileName);
//	    System.out.println("Minimum=" + dataFile.minimumValue);
//	    System.out.println("Maximum=" + dataFile.maximumValue);


	}

	public void plotOneDataFile(String dir, String filename)
	{
		BiDimensionalArray<Float> imageArray = readSolDataFile(dir, filename);

		String outputDirectory = dir + "graphs/";

		String dataFileName = outputDirectory + filename + ".dat";
		StringBuffer outputStr = new StringBuffer();

		for (int i=0;i<imageArray.lengthX;i++)
		{
			for (int j=0;j<imageArray.lengthY;j++)
			{
				float value = (Float)imageArray.get(i,j);
				String valueStr = "";
				if (value == NO_DATA_VALUE)
				{
					valueStr = "?";
				}
				else
				{
					valueStr = Float.toString(value);

					if (value == NO_DATA_VALUE || Float.isNaN(value))
					{
						//pixels[offset + col] = 0; // Float.NaN;
					} else
					{
						//pixels[offset + col] = value;
						if (value < minimumValue)
						{
							minimumValue = value;
						}
						if (value > maximumValue)
						{
							maximumValue = value;
						}
					}



				}
				outputStr.append((i+1)*GRID_SQUARE_SIZE + " " + (j+1)*GRID_SQUARE_SIZE + " " + valueStr + '\n');
			}
		}

		String xLabel = "Label";
		String outputFile = dataFileName + ".png";
		common.writeFile(outputStr.toString(), dataFileName);
		plotData(dataFileName, outputFile, xLabel, outputDirectory);

		System.out.println("Wrote out " + dataFileName);
	    System.out.println("Minimum=" + minimumValue);
	    System.out.println("Maximum=" + maximumValue);


	}

	public BiDimensionalArray<Float> readSolDataFile(String dir, String filename)
	{
		ReadSolweigDataFile solDataFile = new ReadSolweigDataFile();
		BiDimensionalArray<Float> imageArray = solDataFile.read(dir, filename);
		return imageArray;
	}

	public void plotData(String dataFileName, String outputFile, String xLabel, String outputDirectory)
	{
		/*
		 * set title "Simple demo of scatter data conversion to grid data" unset
		 * hidden3d set ticslevel 0.5 set view 60,30 set autoscale set
		 * parametric set style data points set xlabel
		 * "data style point - no dgrid" set key box splot "concrete.dat"
		 *
		 * pause -1 "Hit return to continue (1)"
		 *
		 * set dgrid3d 10,10,1 set xlabel " data style lines, dgrid3d 10,10,1"
		 * set style data lines #splot "hemisphr.dat" splot "concrete.dat"
		 */

		Splot.setGnuplotExecutable("gnuplot");

		Splot.setPlotDirectory(outputDirectory);
		Graph aGraph = null;

		Splot aPlot = new Splot();

		aPlot.setGrid();
		aPlot.setKey(null);
		aPlot.setParametric();

		aPlot.addExtra("unset hidden3d");
		aPlot.addExtra("set ticslevel 0.5");
		aPlot.addExtra("set view 60,30");
		aPlot.addExtra("set autoscale");
		aPlot.addExtra("set parametric");
		aPlot.addExtra("set style data points");
		// aPlot.addExtra("set xlabel \"data style point - no dgrid\"");
		aPlot.addExtra("set key box");

		aPlot.addExtra("set dgrid3d 166,166,1");
		aPlot.addExtra("set xlabel \" " + xLabel + "\"");
		aPlot.addExtra("set style data lines");

		aPlot.setDataFileName(dataFileName);

		aPlot.setOutput(Terminal.PNG, outputFile, " 1024,600  small ");
		try
		{
			aPlot.plot();

			//String[] commands = new String[]{"/home/kerryn/bin/gnuplot_bin.sh",  outputDirectory };
			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);


		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}

		aPlot.addExtra("set size square");
		aPlot.addExtra("set pm3d map");
		aPlot.addExtra("set palette rgbformulae 22,13,10");
		aPlot.addExtra("set autoscale");
		aPlot.setOutput(Terminal.PNG, outputFile + "_c", " 1024,600  small ");
		try
		{
			aPlot.plot();

			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);


		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}

		aPlot.addExtra("set palette gray");
		aPlot.addExtra("set samples 100; set isosamples 100");

		aPlot.setOutput(Terminal.PNG, outputFile + "_c_g", " 1024,600  small ");
		try
		{
			aPlot.plot();

			String[] commands = new String[]{"/home/nice/bin/gnuplot_bin.sh",  outputDirectory };
			Process aProcess = Runtime.getRuntime().exec(commands);


		} catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}



		aPlot = new Splot();
		aPlot.setDataFileName(dataFileName);
		aPlot.addExtra("set grid; unset key; set parametric; unset polar; unset hidden3d; set ticslevel 0.5; set view 60,30");
		aPlot.addExtra("set autoscale; set parametric; set style data points; set dgrid3d 166, 166,1; set style data lines; set size square");
		aPlot.addExtra("set pm3d map; set palette rgbformulae 22,13,10; unset logscale; set contour both; set cntr levels 100; unset clabel; unset surface");
		aPlot.setOutput(Terminal.PNG, outputFile, "  2048,1200  small ");
		aPlot.addExtra("set xlabel \"x (m)\"");
		aPlot.addExtra("set ylabel \"y (m)\"");
		aPlot.addExtra("set label \"" + xLabel + "\"  at 0,520");

		//aPlot.setPm3d(true);
		aPlot.setWithLinePalette(true);


		common.runPlotCmd(outputDirectory, aPlot);


	}


	private static class ESRIASCIIGridHeader {

		protected int cols = 0;
		protected int rows = 0;
		protected double west = Double.NaN;
		protected double south = Double.NaN;
		protected double cellSize = Double.NaN;
		protected float noDataValue = Float.NaN;

		/*
		 * returns whether valid values have been found
		 */
		protected boolean isValid() {

			return (cols > 0 && rows > 0 && cellSize > 0 && !Double.isNaN(west) && !Double
					.isNaN(south));
			// noDataValue is optional
		}

		/**
		 * Reads cols, rows, west, south, cellSize and noDataValue from the
		 * header. Throws an exception if this is not a valid header.
		 *
		 * @param scanner
		 *            Scanner must be initialized to use dot as decimal
		 *            separator.
		 * @throws IOException
		 */
		private void readHeader(Scanner scanner) throws IOException {

			cols = rows = 0;
			west = south = cellSize = Double.NaN;
			noDataValue = Float.NaN;

			while (scanner.hasNext()) {

				if (scanner.hasNextDouble()) {
					// next line starts with number, must be grid
					break;
				}

				String str = scanner.next().trim().toLowerCase();
				if (str.equals("ncols")) {
					this.cols = scanner.nextInt();
				} else if (str.equals("nrows")) {
					this.rows = scanner.nextInt();
				} else if (str.equals("xllcenter") || str.equals("xllcorner")) {
					this.west = scanner.nextDouble();
				} else if (str.equals("yllcenter") || str.equals("yllcorner")) {
					this.south = scanner.nextDouble();
				} else if (str.equals("cellsize")) {
					this.cellSize = scanner.nextDouble();
				} else if (str.startsWith("nodata")) {
					this.noDataValue = scanner.nextFloat();
				} else {

					// make sure the line starts with a number
					if (!scanner.hasNextDouble()) {
						throw new IOException();
					}

					// done reading the header
					break;
				}
			}

			if (!isValid()) {
				throw new IOException();
			}
		}
	}

	public ReadSolweigDataFile() {
		img = null;
	}

	/**
	 * Returns whether a scanner references valid data that can be read.
	 *
	 * @param scanner
	 * @return
	 * @throws IOException
	 */
	public static boolean canRead(Scanner scanner) {
		try {
			ESRIASCIIGridHeader header = new ESRIASCIIGridHeader();
			header.readHeader(scanner);
			return header.isValid();
		} catch (Exception exc) {
			return false;
		}
	}

	public static boolean canRead(String filePath) {
		Scanner scanner = null;
		try {
			scanner = createUSScanner(new FileInputStream(filePath));
			return canRead(scanner);
		} catch (Exception exc) {
			return false;
		} finally {
			if (scanner != null) {
				try {
					scanner.close();
				} catch (Throwable exc) {
				}
			}
		}
	}

	public void run(String arg) {

		OpenDialog od = new OpenDialog("Open ESRI grid ...", arg);
		String directory = od.getDirectory();
		String fileName = od.getFileName();
		if (fileName == null)
			return;

		IJ.showStatus("Opening: " + directory + fileName);

		read(directory, fileName);

		if (img == null)
			return;

		img.show();

	}

	protected BiDimensionalArray<Float> read(String dir, String filename) {
		Scanner scanner = null;
		BiDimensionalArray<Float> imageArray = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(dir
					+ filename));
			File file = new File(dir + filename);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			scanner = createUSScanner(new BufferedInputStream(fis));
			ESRIASCIIGridHeader header = new ESRIASCIIGridHeader();
			header.readHeader(scanner);

			int height = header.rows;
			int width = header.cols;
			img = NewImage.createFloatImage(filename, width, height, 1,
					NewImage.FILL_BLACK);

			imageArray = new BiDimensionalArray<Float>(height, width);

			// use legacy StringTokenizer, which is considerably faster than
			// the Scanner class, which uses regular expressions.
			StringTokenizer tokenizer = new StringTokenizer(scanner.nextLine(),
					" ");

			float[] pixels = (float[]) img.getProcessor().getPixels();
			float minimum = Float.MAX_VALUE;
			float maximum = -Float.MAX_VALUE;
			for (int row = 0; row < height; row++) {
				int offset = row * width;
				for (int col = 0; col < width; col++) {
					// a logical row in the grid does not necesseraly correspond
					// to a line in the file!
					if (!tokenizer.hasMoreTokens()) {
						tokenizer = new StringTokenizer(scanner.nextLine(), " ");
					}
					final float v = Float.parseFloat(tokenizer.nextToken());

					//System.out.println (row + " " + col + " " + v);
					imageArray.set(row, col, v);

					if (v == header.noDataValue || Float.isNaN(v)) {
						pixels[offset + col] = 0; // Float.NaN;
					} else {
						pixels[offset + col] = v;
						if (v < minimum) {
							minimum = v;
							minimumValue = v;
						}
						if (v > maximum) {
							maximum = v;
							maximumValue = v;
						}
					}
				}
			}

			((ij.process.FloatProcessor) (img.getProcessor())).setMinAndMax(
					minimum, maximum);

		} catch (Exception e) {
			IJ.error("Simple ASCII Reader", e.getMessage());
		} finally {
			try {
				// this closes the input stream
				if (scanner != null) {
					scanner.close();
				}
			} catch (Exception exc) {
			}
		}

		return imageArray;
	}

	/**
	 * Creates a scanner for ASCII text with a period as decimal separator.
	 *
	 * @param is
	 * @return
	 * @throws FileNotFoundException
	 */
	private static Scanner createUSScanner(InputStream is)
			throws FileNotFoundException {
		Scanner scanner = new Scanner(is, "US-ASCII");
		scanner.useLocale(Locale.US);
		return scanner;
	}

}
