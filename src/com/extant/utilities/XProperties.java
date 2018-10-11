/*
 * XProperties.java
 * Extends java.util.Properties to handle object types other than Strings:
 *    Integer
 *    java.awt.Dimension
 *    java.awt.Point
 * The load() and store() methods treat a trailing backslash as a
 * line continuation, so this class notices when such values are entered and
 * encloses them in double-quotes.  (The quotes are removed before returning
 * the String value.)  Therefore values which begin and end with a double-quote
 * character will be fetched with those characters trimmed.
 * Values which have only a leading or a trailing double-quote or
 * which contain one or more imbedded double-quote characters are not changed.
 *
 * TODO:
 * Implement validate() method
 * Add capability to have comments ( '<!--' or '//' )
 *
 * Created on March 26, 2005, 11:34 AM
 */

package com.extant.utilities;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.awt.Point;
import java.awt.Dimension;

/**
 *
 * @author jms
 */
@SuppressWarnings("serial")
public class XProperties extends Properties
{
	public String propertiesFilename;
	StringBuffer report;
	String section = ""; // Default is to include all properties
	boolean propertiesHaveChanged = false;

	// // We expect our users to construct with one of these:
	// public XProperties()
	// throws IOException, FileNotFoundException
	// {
	// this( defaultPropertiesFilename );
	// }

	public XProperties(String propertiesFilename) throws IOException, FileNotFoundException
	{
		this(propertiesFilename, "");
	}

	public XProperties(String propertiesFilename, String section) throws IOException, FileNotFoundException
	{
		if (propertiesFilename == null)
			throw new IOException("propertiesFilename is null!");
		this.propertiesFilename = propertiesFilename;
		this.section = section;
		FileInputStream fis = new FileInputStream(new File(propertiesFilename));
		super.load(fis);
		fis.close();
		// !! This file should be locked by the user until he is done with any updates
	}

	public String setSection(String section)
	{
		String originalSection = this.section;
		this.section = section;
		return originalSection;
	}

	public String getSection()
	{
		return section;
	}

	public Object setProperty(String key, String value)
	{
		return putString(key, value);
	}

	// Override super.getProperty() method to hide the internal forms
	// public String getProperty( String key )
	// { return getString( key ); }
	//
	public void setProperty(String key, int value)
	{
		putInt(key, value);
	}

	public void setProperty(String key, double value)
	{
		putDouble(key, value);
	}

	public void setProperty(String key, Point value)
	{
		putPoint(key, value);
	}

	public void setProperty(String key, Dimension value)
	{
		putDimension(key, value);
	}

	// ***********************************************************************
	// Handling java.lang.String objects
	// ***********************************************************************
	// Internal Forms:
	// <string>
	// String|<string>
	// java.lang.String|<string>

	public Object putString(String key, String value)
	{
		if (value.endsWith("\\"))
			value = "\"" + value + "\"";
		propertiesHaveChanged = true;
		return super.put(realKey(key), "String|" + value);
	}

	// The getString methods will return a fully-qualified key, regardless of
	// the current section
	public String getString(String key)
	{
		return getString(key, "");
	}

	public String getString(String key, String defaultValue)
	{
		String realKey;
		if (super.containsKey(key))
			realKey = key;
		else
			realKey = realKey(key);
		if (super.containsKey(realKey))
		{
			// This method will return the internal String representation of this
			// object (excluding the type designator), regardless of its type
			String value = parseValue(super.getProperty(realKey));
			if (value.startsWith("\"") && value.endsWith("\""))
				value = Strings.trim(value, "\"");
			return value;
		} else
			return defaultValue;
	}

	// ***********************************************************************
	// Handling int values
	// ***********************************************************************
	// Internal Forms:
	// <int-string>
	// Integer|<int-string>

	public void putInt(String key, int value)
	{
		super.put(realKey(key), "int|" + Integer.toString(value));
		propertiesHaveChanged = true;
	}

	public int getInt(String key) throws NumberFormatException
	{
		return getInt(key, -1);
	}

	public int getInt(String key, int defaultInt) throws NumberFormatException
	{
		String realKey = realKey(key);
		if (super.containsKey(realKey))
			return Integer.parseInt(parseValue(super.getProperty(realKey)));
		else
			return defaultInt;
	}

	// ***********************************************************************
	// Handling double values
	// ***********************************************************************
	// Internal forms
	// Double|<double-string>

	public void putDouble(String key, double value)
	{
		putDouble(key, value, null);
	}

	public void putDouble(String key, double value, String format)
	{
		String v = "Double|";
		if (format == null)
			v += Double.toString(value);
		else
			v += new DecimalFormat(format).format(new Double(value));
		super.setProperty(realKey(key), v);
		propertiesHaveChanged = true;
	}

	public double getDouble(String key) throws NumberFormatException
	{
		return getDouble(key, (double) 0.0);
	}

	public double getDouble(String key, double defaultDouble) throws NumberFormatException
	{
		String realKey = realKey(key);
		if (super.containsKey(realKey))
			return Double.parseDouble(parseValue(super.getProperty(realKey)));
		else
			return defaultDouble;
	}

	// ***********************************************************************
	// Handling Point objects
	// ***********************************************************************
	// Internal form:
	// Point|<x-string>,<y-string>
	// or java.awt.Point|x=<x-string>,y=<y-string>

	public void putPoint(String key, Point point)
	{
		super.setProperty(realKey(key), new Object2D(point).toString());
		propertiesHaveChanged = true;
	}

	public Point getPoint(String key) throws UtilitiesException
	{
		return getPoint(key, null);
	}

	public Point getPoint(String key, Point defaultPoint) throws UtilitiesException
	{
		String realKey = realKey(key);
		if (super.containsKey(realKey))
		{
			String v = super.getProperty(realKey(key));
			if (!parseClass(v).toLowerCase().endsWith("point"))
				throw new UtilitiesException(UtilitiesException.PROPERTY_TYPE_MISMATCH, key + "=" + v);
			return new Object2D(v).toPoint();
		} else
			return defaultPoint;
	}

	// ***********************************************************************
	// Handling Dimension objects
	// ***********************************************************************
	// Internal form:
	// Dimension|<width-string>,<height-string>
	// ...Dimension|width=<width-string>,height=<height-string>

	public void putDimension(String key, Dimension dimension)
	{
		super.setProperty(realKey(key), new Object2D(dimension).toString());
		propertiesHaveChanged = true;
	}

	public Dimension getDimension(String key) throws UtilitiesException
	{
		return getDimension(key, null);
	}

	public Dimension getDimension(String key, Dimension defaultDimension) throws UtilitiesException
	{
		String realKey = realKey(key);
		if (super.containsKey(realKey))
		{
			String v = super.getProperty(realKey(key));
			if (!parseClass(v).toLowerCase().endsWith("dimension"))
				throw new UtilitiesException(UtilitiesException.PROPERTY_TYPE_MISMATCH, key + "=" + v);
			return new Object2D(v).toDimension();
		} else
			return defaultDimension;
	}

	// ***********************************************************************
	// Handling Boolean Valuse
	// ***********************************************************************
	// Internal form:
	// Asserted: String|true or String|yes
	// Not Asserted: String|<anything else>

	public boolean setAssert(String key, boolean yes)
	{
		boolean was = isAsserted(key);
		if (yes ^ was)
		{
			if (yes)
				setProperty(key, "true");
			else
				setProperty(key, "false");
			propertiesHaveChanged = true;
		}
		return was;
	}

	public boolean isAsserted(String key)
	{
		if (!super.containsKey(realKey(key)))
			return false;
		String v = parseValue(super.getProperty(realKey(key)));
		if (v.equalsIgnoreCase("yes"))
			return true;
		if (v.equalsIgnoreCase("true"))
			return true;
		return false;
	}

	// this will update the properties file only if there has been a change
	public void store(String comments) throws IOException
	{
		if (propertiesHaveChanged)
			this.store(propertiesFilename, comments);
	}

	// this will force an update of the specified properties file
	public void store(String outfilename, String comments) throws IOException
	{
		File file = new File(outfilename);
		FileOutputStream fos = null;
		if (file.exists())
			file.delete();
		if (file.createNewFile())
		{
			fos = new FileOutputStream(file);
			if (fos != null)
			{
				super.store(fos, comments);
				fos.close();
				propertiesHaveChanged = false;
			}
		} else
			throw new IOException("Cannot delete/create " + outfilename);
	}

	// public void storeSorted( String comments )
	// throws IOException
	// {
	// storeSorted( propertiesFilename, comments );
	// }
	//
	// public void storeSorted( String outfilename, String comments )
	// throws IOException
	// {
	// UsefulFile outfile = new UsefulFile( outfilename, "w" );
	// outfile.println( "#" + comments );
	// outfile.println( "#" + new Julian().toString( "mm-dd-yyyy hh:mm:ss" ) );
	// Vector elements = new Vector( 100, 100 );
	// Enumeration list = super.keys();
	// while ( list.hasMoreElements() )
	// {
	// String key = (String)list.nextElement();
	// String value = super.getProperty( key );
	// //outfile.println( key + "=" + value );
	// elements.addElement( key + "=" + value );
	// }
	// int p[] = Sorts.sort( elements );
	// for (int i=0; i<p.length; ++i)
	// outfile.println( translateOut( (String)elements.elementAt( p[i] ) ) );
	// outfile.close();
	// }
	//
	// public String translateOut( String s )
	// {
	// Console.println( "XP translateOut: " + s );
	// StringBuffer sb = new StringBuffer( s );
	// boolean first = true;
	// for (int i=0; i<sb.length(); ++i)
	// {
	// if ( sb.charAt( i ) == '\\' )
	// {
	// sb.insert( i-1, '\\' );
	// ++i;
	// }
	// if ( sb.charAt( i ) == '=' )
	// {
	// if ( first ) first = false;
	// else sb.insert( i-1, '\\' );
	// ++i;
	// }
	// }
	// return sb.toString();
	// }

	// This allows users to make temporary property changes
	public void setHasChanged(boolean hasChanged)
	{
		propertiesHaveChanged = hasChanged;
	}

	public boolean hasChanged()
	{
		return propertiesHaveChanged;
	}

	private String realKey(String key)
	{
		if (section.equals(""))
			return key;
		else
			return section + "." + key;
	}

	private String parseClass(String v)
	{
		int p = v.indexOf("|");
		if (p < 0)
			return "String";
		return v.substring(0, p);
	}

	private String parseValue(String v)
	{
		int p = v.indexOf("|");
		return v.substring(p + 1);
	}

	public String getClass(String key)
	{
		return parseClass(getProperty(realKey(key)));
	}

	public boolean isSectionPresent(String sectionName)
	{
		Enumeration en = super.keys();
		while (en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			if (key.startsWith(sectionName + "."))
				return true;
		}
		return false;
	}

	public String getFilename()
	{
		return propertiesFilename;
	}

	// ***********************************************************************
	// Embedded class
	// ***********************************************************************
	class Object2D
	{
		String clazz;
		int x;
		int y;

		Object2D(Point point)
		{
			clazz = "Point";
			x = point.x;
			y = point.y;
		}

		Object2D(Dimension dimension)
		{
			clazz = "Dimension";
			x = dimension.width;
			y = dimension.height;
		}

		Object2D(String s) throws UtilitiesException
		{ // Expects something of the form:
			// ...<class>|<anything>=123,<anything>=456
			// or ...<class>|123,456
			try
			{
				boolean longForm = s.indexOf("=") >= 0;
				StringTokenizer st = new StringTokenizer(s, "|=,");
				clazz = (String) st.nextElement();
				if (longForm)
					st.nextElement();
				x = Integer.parseInt((String) st.nextElement());
				if (longForm)
					st.nextElement();
				y = Integer.parseInt((String) st.nextElement());
			} catch (Exception x)
			{
				throw new UtilitiesException(UtilitiesException.PROPERTY_SYNTAX_ERROR, s);
			}
		}

		public Point toPoint()
		{
			return new Point(x, y);
		}

		public Dimension toDimension()
		{
			return new Dimension(x, y);
		}

		public String toString()
		{
			return clazz + "|" + Integer.toString(x) + "," + Integer.toString(y);
		}
	}

	// returns the number of errors found in this Properties
	public int validate()
	{
		int nErrors = 0;
		report = new StringBuffer();
		Enumeration en = this.keys();
		while (en.hasMoreElements())
		{
			String key = (String) en.nextElement();
			String value = getProperty(key);
			// Console.println( "validating: " + key + "=" + value );
			String clazz = parseClass(value).toLowerCase();
			String v = parseValue(value);
			try
			{
				if (clazz.endsWith("string"))
					; // All Strings are good
				else if (clazz.endsWith("point"))
					new Object2D(value);
				else if (clazz.endsWith("int"))
					Integer.parseInt(v);
				else if (clazz.endsWith("double"))
					Double.parseDouble(v);
				else if (clazz.endsWith("dimension"))
					new Object2D(value);
				else
					throw new Exception();
			} catch (Exception x)
			{
				report.append("Syntax Error in " + key + "=" + value + "\n");
				++nErrors;
			}
		}
		return nErrors;
	}

	public String getReport()
	{
		if (report == null)
			return null;
		return report.toString();
	}

	/***** FOR TESTING *****/
	/*****
	 * MAIN FOR TEST #1 ***** public static void main( String args[] ) { try {
	 * XProperties xProperties = new XProperties (
	 * "G:\\Projects\\Java7\\EXTANT\\Extant.properties", "TEST" ); int nErrors =
	 * xProperties.validate(); Console.println( "validate reports " +
	 * Strings.plurals( "error", nErrors ) ); if ( nErrors > 0 ) Console.println(
	 * xProperties.getReport() );
	 * 
	 * // This is a simple string Console.println( "transporter.archive=" +
	 * xProperties.getString( "transporter.archive" ) ); // This is a string with
	 * some "\\" characters Console.println( "transporterLogFilename=" +
	 * xProperties.getString( "transporterLogFilename" ) ); // This is a directory
	 * name (with trailing "\\") Console.println( "transporterDir=" +
	 * xProperties.getString( "transporterDir" ) ); // This is a type int
	 * Console.println( "DBMaint.COINDEX.CONAME.width=" + xProperties.getInt(
	 * "DBMaint.COINDEX.CONAME.width" ) ); // This is a String with an integer value
	 * Console.println( "DBMaint.COINDEX.CUSIP.width=" + xProperties.getString(
	 * "DBMaint.COINDEX.CUSIP.width" ) ); Console.println(
	 * "DBMaint.COINDEX.CUSIP.width=" + xProperties.getInt(
	 * "DBMaint.COINDEX.CUSIP.width" ) ); // This is a Point Point point =
	 * xProperties.getPoint( "dbTools.ReportViewer.StockTrans.frameLocation" );
	 * Console.print( "dbTools.ReportViewer.StockTrans.frameLocation=" ); if ( point
	 * == null ) Console.println( "null" ); else Console.println( point.toString()
	 * ); // This is a Dimension Dimension dim = xProperties.getDimension(
	 * "dbTools.ReportViewer.StockTrans.frameSize" ); Console.print(
	 * "dbTools.ReportViewer.StockTrans.frameSize=" ); if ( dim == null )
	 * Console.println( "null" ); else Console.println( dim.toString() ); // Now we
	 * put some new things in xProperties.putString( "TestString", "This is a test
	 * string." ); Console.println( "TestString=" + xProperties.getString(
	 * "TestString" ) ); xProperties.putInt( "TestInt", 77 ); Console.println(
	 * "TestInt=" + xProperties.getInt( "TestInt" ) ); xProperties.putPoint(
	 * "TestPoint", new Point( 13, 17 ) ); Console.println( "TestPoint=" +
	 * xProperties.getPoint( "TestPoint" ).toString() ); xProperties.putDimension(
	 * "TestDimension", new Dimension( 23, 27 ) ); Console.println( "TestDimension="
	 * + xProperties.getDimension( "TestDimension" ).toString() );
	 * 
	 * String storeFile = "C:\\Projects\\xPropTest2.properties"; Console.println(
	 * "storing to " + storeFile + " ..." ); xProperties.store( storeFile, "Testing
	 * 1 2 3" );
	 * 
	 * Console.println( "This should cause a PropertyTypeMismatch exception:" );
	 * point = xProperties.getPoint( "TestDimension" ); if (point == null)
	 * Console.println( null ); Console.println( point.toString() ); } catch
	 * (IOException iox) { Console.println( iox.getMessage() ); } catch
	 * (UtilitiesException ux) { Console.println( ux.getMessage() ); } } /***** END
	 * OF TEST #1
	 *****/

	/***** MAIN FOR TEST #2 *****/
	public static void main(String args[])
	{
		XProperties props;
		try
		{
			props = new XProperties("G:\\ACCOUNTING\\JMS\\JMS.properties");
		} catch (IOException iox)
		{
			System.out.println("Cannot open JMS.properties");
			return;
		}
		Enumeration keys = props.keys();
		while (keys.hasMoreElements())
			System.out.println(keys.nextElement());
		String glFile = props.getString("GLFile");
		System.out.println("GLFile=" + glFile);
	}

	/***** END OF TEST #2 *****/
}
