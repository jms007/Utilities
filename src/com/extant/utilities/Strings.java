package com.extant.utilities;

import java.io.*;
import java.text.*;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.regex.*;
import java.util.Vector;

public class Strings implements FilenameFilter
{
	public static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA_LOWER = ALPHA_UPPER.toLowerCase();
	public static final String ALPHA = ALPHA_UPPER + ALPHA_LOWER;
	public static final String NUMERIC = "0123456789";
	public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;

	public Strings()
	{
	}

	// Returns the current working directory, including drive letter
	// e.g. "C:\Projects\Stock"
	public static String getCWD()
	{
		return System.getProperties().getProperty("user.dir");
	}

	public static String getParentDir()
	{
		return getParentDir(getCWD());
	}

	public static String getParentDir(String dir)
	{
		if (dir.endsWith(File.separator))
			dir = dir.substring(0, dir.length() - 1);
		int p = dir.lastIndexOf((int) File.separatorChar) + 1;
		return dir.substring(0, p);
	}

	// Returns a String array containing the names of all files
	// INCLUDING SUB-DIRECTORIES in a given
	// directory whose names match* the specified pattern.
	// The names are in no particular order.
	// *Matching is done either
	// (a) like DOS, supporting the wildcards '?' and '*'
	// (Multiple patterns can be specified, separated by |.) or
	// (b) using regex
	private String pattern;
	private boolean patternIsRegex;

	public String[] getMatchingFiles(String dir, String pattern)
	{
		this.pattern = pattern;
		patternIsRegex = false;
		String ans[] = new File(dir).list(this);
		if (ans == null)
			return new String[0];
		return ans;
	}

	public String[] getMatchingFilesRegex(String dir, String regex)
	{
		this.pattern = regex;
		patternIsRegex = true;
		String ans[] = new File(dir).list(this);
		if (ans == null)
			return new String[0];
		return ans;
	}

	public boolean accept(File dir, String name)
	{ // implements accept method of FilenameFilter interface
		if (!patternIsRegex)
			// return ( match( pattern, name ) && dir.isFile() ); // This excludes
			// sub-directories
			return match(pattern, name); // This includes sub-directories
		/* else */
		return Pattern.matches(pattern, name);
	}

	// Checks whether a String matches a DOS-style wildcard pattern.
	// Only handles ? and *, and multiple patterns separated by |.
	// public static boolean match( String pattern, String string )
	// {
	// // Make this test case-insensitive
	// pattern = pattern.toLowerCase();
	// string = string.toLowerCase();
	// for ( int p=0; ; ++p )
	// {
	// for ( int s=0; ; ++p, ++s )
	// {
	// boolean sEnd = ( s >= string.length() );
	// boolean pEnd = ( p >= pattern.length() || pattern.charAt( p ) == '|' );
	// if ( sEnd && pEnd )
	// return true;
	// if ( sEnd || pEnd )
	// break;
	// if ( pattern.charAt( p ) == '?' )
	// continue;
	// if ( pattern.charAt( p ) == '*' )
	// {
	// int i;
	// ++p;
	// for ( i = string.length(); i >= s; --i )
	// if ( match(
	// pattern.substring( p ),
	// string.substring( i ) ) ) //!! not quite right
	// return true;
	// break;
	// }
	// if ( pattern.charAt( p ) != string.charAt( s ) ) break;
	// }
	// p = pattern.indexOf( '|', p );
	// if ( p == -1 )
	// return false;
	// }
	// }
	//
	// Translates DOS-style patterns to regex
	// Handles only * and ? wildcards
	// This will mutilate some regex expressions!
	public static String patternX(String dosPattern)
	{
		StringBuffer work = new StringBuffer(dosPattern.length());
		for (int i = 0; i < dosPattern.length(); ++i)
		{
			if (dosPattern.charAt(i) == '*')
				work.append(".*");
			else if (dosPattern.charAt(i) == '.')
				work.append("\\.");
			else
				work.append(dosPattern.charAt(i));
		}
		// Console.println( "patternX( " + dosPattern + " ) = " + work );
		return work.toString();
	}

	// Performs a case-insensitive match using regex !!this is not cool
	public static boolean regexMatch(String pattern, String string)
	{
		boolean b = Pattern.matches(pattern.toLowerCase(), string.toLowerCase());
		return b;
	}

	// Performs a match of DOS-style pattern
	public static boolean match(String dosPattern, String string)
	{
		return regexMatch(patternX(dosPattern), string);
	}

	// Performs a pure regex pattern match
	public static boolean regex(String pattern, String s)
	{
		return Pattern.matches(pattern, s);
	}

	public static Julian getLastModified(String filename)
	{
		return getLastModified(new File(filename));
	}

	public static Julian getLastModified(File file)
	{
		return new Julian(new Date(file.lastModified()).toString());
	}

	// Here are the returned values for each valid 'what':
	// Drive - Drive Letter + ":"
	// Path - Drive Letter + ":" (if any) + path to file + trailing separator
	// (does NOT include the file name)
	// path - path to file + trailing separator
	// (does NOT include the Drive letter or the file name)
	// FO - File name only (no path, no extention)
	// File - File name, including extention, if any
	// Extention - File Extention (does not include the dot)
	public static String fileSpec(String what, String pathAndFile)
	{
		int p = pathAndFile.lastIndexOf(File.separatorChar);
		int pdot = pathAndFile.lastIndexOf('.');
		int pColon = pathAndFile.indexOf(":");
		if (pdot < 0)
		{
			pathAndFile = pathAndFile + ".";
			pdot = pathAndFile.lastIndexOf('.');
		}
		if (what.startsWith("D"))
		{
			if (pColon < 0)
				return "";
			return pathAndFile.substring(0, pColon + 1);
		} else if (what.startsWith("P"))
		{ // PATH (includes drive letter, if any) with trailing separator (does NOT
			// include the filename!)
			if (p < 0)
				return "";
			return pathAndFile.substring(0, p + 1);
		} else if (what.startsWith("p"))
		{ // path with trailing separator, does not include the Drive letter or file name
			String path = pathAndFile.substring(0, p + 1);
			if (path.indexOf(":") < 0)
				return path;
			return path.substring(path.indexOf(":") + 1);
		} else if (what.startsWith("FO"))
		{ // FileOnly (File name with no Extention)
			if (p < 0)
			{
				if (pdot < 0)
					return pathAndFile;
				else
					return pathAndFile.substring(0, pdot);
			}
			if (pdot < 0)
				return pathAndFile.substring(p + 1);
			else
				return pathAndFile.substring(p + 1, pdot);
		} else if (what.startsWith("F"))
		{ // FILE name, including Extention if any
			if (p < 0)
				return pathAndFile;
			return trim(pathAndFile.substring(p + 1), ".");
		} else if (what.startsWith("E"))
		{ // Extention
			if (pdot < 0)
				return "";
			return pathAndFile.substring(pdot + 1);
		} else if (what.equals(".."))
		{ // Path to directory containing this file //!! ?How is this different from "P"?
			String t = pathAndFile.substring(0, p); // Path to dir with trailing separator
			return t.substring(0, t.lastIndexOf(File.separatorChar) + 1);
		} else
			return "";
	}

	// getDerivedFileName returns a filename with the same path and file as the
	// input file, but with a different extention
	public static String getDerivedFileName(String inputFileName, String newExtention)
	{
		if (!newExtention.startsWith("."))
			newExtention = "." + newExtention;
		return Strings.fileSpec("P", inputFileName) + Strings.fileSpec("FO", inputFileName) + newExtention;
	}

	public static boolean isAlpha(char c)
	{
		return validateChar(c, ALPHA);
	}

	public static boolean isNumeric(char c)
	{
		return validateChar(c, NUMERIC);
	}

	public static boolean isAlphaNumeric(char c)
	{
		return validateChar(c, ALPHA_NUMERIC);
	}

	public static byte[] toBytes(String s)
	{
		byte bytes[] = new byte[s.length()];
		for (int i = 0; i < s.length(); ++i)
			bytes[i] = (byte) s.charAt(i);
		return bytes;
	}

	// Tests whether a character is contained in the String validChars
	public static boolean validateChar(char c, String validChars)
	{
		return validChars.indexOf(c) >= 0;
	}

	// Same thing, different name
	public static boolean validateChars(char c, String validChars)
	{
		return validateChar(c, validChars);
	}

	// Tests if a String contains only characters from validChars
	public static boolean validateChars(String s, String validChars)
	{
		for (int i = 0; i < s.length(); ++i)
			// if ( validChars.indexOf( s.charAt( i ) ) < 0 ) return false;
			if (!validateChar(s.charAt(i), validChars))
				return false;
		return true;
	}

	// Tests if any of the characters in needles occur in haystack
	public static boolean containsAny(String haystack, String needles)
	{
		for (int i = 0; i < needles.length(); ++i)
			if (validateChar(needles.charAt(i), haystack))
				return true;
		return false;
	}

	// Returns true if the string needle occurs anywhere in the string haystack
	public static boolean contains(String haystack, String needle)
	{
		return haystack.indexOf(needle) >= 0;
	}

	public static boolean isPrintable(char c)
	{
		return (int) c >= 32 && (int) c <= 255;
	}

	public static String format(int v, String f)
	{
		return format((long) v, f);
	}

	public static String format(long v, String f)
	{
		DecimalFormat df = new DecimalFormat(f);
		return df.format(v);
	}

	public static String format(double v, String f)
	{
		DecimalFormat df = new DecimalFormat(f);
		return df.format(v);
	}

	public static String colFormat(long v, String f)
	{ // Converts using Long.toString and right-justifies in f
		String sv = new Long(v).toString();
		int fillLength = f.length() - sv.length();
		StringBuffer sb = new StringBuffer(f.length());
		sb.append(f.substring(0, fillLength));
		sb.append(sv);
		return sb.toString();
	}

	public static String colFormat(int v, String f)
	{ // Converts using Long.toString and right-justifies in f
		return colFormat((long) v, f);
	}

	public static String colFormat(int value, int colWidth)
	{ // Converts to string and right-justifies, padded with blanks
		DecimalFormat sf = new DecimalFormat("###,##0");
		return (rightJustify(sf.format(value), colWidth));
	}

	public static String colFormat(double value, int colWidth)
	{
		DecimalFormat df = new DecimalFormat("##,###,##0.00"); // for dollars
		return (rightJustify(df.format(value), colWidth));
	}

	public static String rightJustify(String s, int colWidth)
	{
		if (s.length() >= colWidth)
			if (colWidth > 3)
				return s.substring(0, colWidth - 3) + "...";
			else
				return "?";
		String ans = "";
		for (int i = 0; i < colWidth - s.length(); ++i)
			ans += " ";
		return (ans + s);
	}

	public static String leftJustify(String s, int colWidth)
	{
		if (s.length() > colWidth)
		{
			if (colWidth >= 3)
			{
				return s.substring(0, colWidth - 3) + "...";
			} else
				return "...";
		}
		String ans = s;
		while (ans.length() < colWidth)
			ans += " ";
		return ans;
	}

	public static String center(String s, int colWidth)
	{
		String space = "";
		if (s.length() >= colWidth)
			return s.substring(0, colWidth - 5) + "...";
		int nSpace = (colWidth - s.length()) / 2;
		for (int i = 0; i < nSpace; ++i)
			space += " ";
		String ans = space + s;
		// if (ans.length() > colWidth)
		// ans = ans.substring(0, colWidth - 1);
		// while (ans.length() < colWidth)
		// ans += " ";
		return ans;
	}

	public static String colFormat(String s, int width)
	{ // Right-justifies a string
		return rightJustify(s, width);
		// StringBuffer sb = new StringBuffer( width );
		// int fillLength = width - sv.length();
		// if ( width <= 0 ) return sv;
		// for (int i=0; i<fillLength; ++i) sb.append( ' ' );
		// sb.append( sv );
		// return sb.toString();
	}

	public static String colFormat(String sv, String f)
	{ // Left-justifies a string (truncating, if necessary)
		// int finalLength = f.length();
		// int fillLength = f.length() - sv.length();
		// if ( fillLength < 0 ) finalLength = sv.length();
		// StringBuffer sb = new StringBuffer( finalLength );
		// sb.append( sv );
		// if ( fillLength > 0 ) sb.append( f.substring( 0, fillLength ) );
		// return sb.toString().substring( 0, f.length() );
		if (sv.length() > f.length())
			return sv.substring(0, f.length());
		String s = sv;
		while (s.length() < f.length())
			s += " ";
		return s;
	}

	// Re-formats a scientific-notation string ( x.xxxxxxEyy ) to
	// a regular number
	public static String e2d(String s)
	{
		if (s.indexOf("E") == -1)
			return (s);
		StringTokenizer st = new StringTokenizer(s, ".E");
		String i = (String) st.nextElement();
		String frac = (String) st.nextElement();
		int e = Integer.parseInt((String) st.nextElement());
		while (frac.length() < e)
			frac += "0";
		String a = i + frac.substring(0, e) + "." + frac.substring(e);
		return (a);
	}

	public static String toHexString(byte b)
	{
		byte[] ba = new byte[1];
		ba[0] = b;
		return (toHexString(ba, 1, ' '));
	}

	public static String toHexString(String s)
	{
		return toHexString(s.getBytes());
	}

	public static String toHexString(byte[] bytes)
	{
		return (toHexString(bytes, bytes.length, ' '));
	}

	public static String toHexString(byte[] bytes, int nBytes)
	{
		return (toHexString(bytes, nBytes, ' '));
	}

	public static String toHexString(byte[] bytes, int nBytes, char separator)
	{
		String ans = "";
		for (int i = 0; i < nBytes; ++i)
		{
			if ((((int) bytes[i]) & 0xFF) < 0x10)
				ans += "0";
			ans += Integer.toHexString((((int) bytes[i]) & 0xFF)).toUpperCase();
			if (separator != '\0')
				ans += separator;
		}
		return (ans);
	}

	public static String remove(String s, char c)
	{ // Removes all occurances of c in the String s
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); ++i)
			if (s.charAt(i) != c)
				sb.append(s.charAt(i));
		return sb.toString();
	}

	public static String deComma(String s)
	{
		return remove(s, ',');
	}

	public static String deDot(String s)
	{
		return remove(s, '.');
	}

	public static boolean isDecimalNumber(String s)
	{ // This works for either int or long
		if (s.length() <= 0)
			return false;
		for (int i = 0; i < s.length(); ++i)
			if (!isDecimalDigit(s.charAt(i)))
				return (false);
		return (true);
	}

	public static boolean isValidInt(String s)
	{
		return isDecimalNumber(s);
	}

	public static boolean isValidLong(String s)
	{
		return isDecimalNumber(s);
	}

	public static long parseLong(String s)
	{
		String s1 = trim(s, " ");
		long l = 0L;
		int p = 0;
		boolean minus = false;
		if (s1.startsWith("-"))
		{
			minus = true;
			p = 1;
		}
		while ((p < s1.length()) && isDecimalDigit(s1.charAt(p)))
			l = l * 10 + (s1.charAt(p++) - '0');
		if (minus)
			return (-l);
		return (l);
	}

	public static int parseInt(String s)
	{
		return ((int) parseLong(s));
	}

	public static boolean isDecimalDigit(char c)
	{
		return ((c >= '0') && (c <= '9'));
	}

	public static boolean isValidPennies(String s)
	{ // For currency strings of the form xx,xxx.xx
		// to be converted to long pennies
		return fixPennies(s).length() > 0;
	}

	public static String fixPennies(String s)
	{
		// Makes a valid pennies field from things like:
		// xxxxx xxxxx. xxxxx.x xxxxx.xx xx,xxx xx,xxx. xx,xxx.x xx,xxx.xx
		// returns the empty String on error
		String s1 = new String(s.trim());
		if (s1.equals(""))
			return "";
		if (s1.startsWith("."))
			s1 = "0" + s1; // for .xx
		if (s1.startsWith("-."))
			s1 = "-0." + s1.substring(2); // for -.xx
		int p1 = s1.indexOf('.');
		if (p1 != s1.lastIndexOf('.'))
			return "";
		if (p1 < 0)
			s1 += ".";
		p1 = s1.indexOf('.');
		int decimalDigits = s1.length() - p1 - 1;
		if (decimalDigits > 2)
			s1 = s1.substring(0, p1 + 2);
		// if ( decimalDigits > 2 ) return ""; // mils are not allowed
		// if ( decimalDigits == 2 ) ; // This is the normal case
		if (decimalDigits == 1)
			s1 += "0";
		if (decimalDigits == 0)
			s1 += "00";
		int p0 = 0;
		if (s1.startsWith("-"))
			p0 = 1;
		if (isValidLong(deComma(s1.substring(p0, p1))) && isValidLong(s1.substring(p1 + 1)))
			return (s1);
		else
			return "";
	}

	public static long parsePennies(String s)
	{
		String s1 = deComma(trim(s, " "));
		int sign = 1;
		if (s1.startsWith("-"))
			sign = -1;
		int p1 = s1.indexOf('.');
		if (p1 < 0)
		{
			s1 += ".00";
			p1 = s1.indexOf('.');
		}
		return (parseLong(s1.substring(0, p1)) * 100L) + sign * parseLong(s1.substring(p1 + 1));
	}

	public static String formatPennies(long pennies)
	{
		return formatPennies(pennies, ",");
	}

	public static String formatPennies(long pennies, String formatOptions)
	{
		// This DecimalFormat just plain doesn't work (the millions comma never appears)
		// DecimalFormat df = new DecimalFormat( "###,###,##000" );
		StringBuffer sb = new StringBuffer(20);
		String ts = "";
		String sign = "-";
		boolean negative = false;
		if (validateChars(',', formatOptions))
			ts = ",";
		if (validateChars('<', formatOptions))
			sign = "<";
		if (validateChars('(', formatOptions))
			sign = "(";
		if (pennies < 0)
		{
			negative = true;
			pennies = -pennies;
			sb.append(sign);
		}
		long dollars = pennies / 100;
		long cents = pennies % 100;
		DecimalFormat df1 = new DecimalFormat("###" + ts + "###" + ts + "##0");
		sb.append(df1.format(dollars));
		DecimalFormat df2 = new DecimalFormat("00");
		sb.append("." + df2.format(cents));
		if (negative)
		{
			if (sign.equals("("))
				sb.append(")");
			else if (sign.equals("<"))
				sb.append(">");
			else
				sb.append(" ");
		} else
			sb.append(" ");
		return sb.toString();
	}

	// The original Basic code for these routines is in ...\VB\VL.SRC
	public static String pennies2Text(long pennies)
	{
		String text = "";
		long dollars = pennies / 100L;
		if (dollars == 0)
		{
			text += "***ONLY " + Strings.format(pennies % 100, "00") + " CENTS***";
			return text;
		}
		int million = (int) (dollars / 1000000L);
		text += three2text(million, "MILLION ");
		dollars -= million * 1000000L;
		int thousand = (int) (dollars / 1000L);
		text += three2text(thousand, "THOUSAND ");
		dollars -= (thousand * 1000L);
		text += three2text((int) dollars, "");
		text += "DOLLAR";
		if (dollars != 1)
			text += "S";
		if ((pennies % 100) == 0)
			text += " AND NO CENTS";
		else
			text += " AND " + Strings.format(pennies % 100, "00") + " CENTS";
		return "***" + text.trim() + "***";
	}

	public static String three2text(int n, String tag)
	{
		// Console.println( "three2text( " + n + ", " + tag + ")" );
		String text = "";
		if (n >= 100)
			text += units[n / 100] + " HUNDRED ";
		int t = (n % 100) / 10;
		if (t == 1)
		{
			text += teens[n % 10] + " ";
		} else
		{
			if (t > 0)
				text += tens[t] + " ";
			text += units[n % 10];
		}
		if (n > 0)
			text += " " + tag;
		return text;
	}

	public static final String[] units = new String[] { "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN",
			"EIGHT", "NINE" };
	public static final String[] teens = new String[] { "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN",
			"SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN" };
	public static final String[] tens = new String[] { "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY",
			"EIGHTY", "NINETY" };

	public static boolean isValidFloat(String s)
	{
		try
		{
			Float f = Float.parseFloat(s);
			return true;
		} catch (NumberFormatException nfx)
		{
			return false;
		}
	}

	public static float parseFloat(String s) throws NumberFormatException
	{
		if (trim(s, " ").length() == 0)
			return (float) 0.0;
		return Float.valueOf(s).floatValue();
	}

	public static boolean isValidDouble(String s)
	{
		return isValidFloat(s);
	}

	public static double parseDouble(String s) throws NumberFormatException
	{
		if (trim(s, " ").length() == 0)
			return 0.0;
		return Double.valueOf(s).doubleValue();
	}

	public static long parseHex(String s)
	{
		long a = 0;
		int p = 0;
		char c;
		while (s.charAt(p) == ' ')
			++p;
		for (int i = 0; i < 16; ++i)
		{
			if (p >= s.length())
				break;
			c = s.toUpperCase().charAt(p++);
			if ((c >= 'A') && (c <= 'F'))
				a = (a << 4) + (c - 'A' + 10);
			else if ((c >= '0') && (c <= '9'))
				a = (a << 4) + (c - '0');
			else
				break;
		}
		return (a);
	}

	public static byte[] parseHexBytes(String s)
	{
		return parseHexBytes(s, s.length() / 2);
	}

	public static byte[] parseHexBytes(String s, int nBytes)
	{
		int m;
		if (s.indexOf(' ') >= 0)
			m = 3;
		else
			m = 2;
		byte[] ans = new byte[nBytes];
		for (int i = 0; i < nBytes; ++i)
		{
			ans[i] = (byte) parseHex(s.substring(i * m, (i + 1) * m));
		}
		return (ans);
	}

	public static String indentedString(String sin)
	{
		String sout = "";
		int i, p = 0;
		int level = -1;
		String indent = "   ";

		while (p < sin.length())
		{
			char c = sin.charAt(p);
			if (c == '(')
			{
				sout += "\n";
				++level;
				for (i = 0; i < level; ++i)
					sout += indent;
				sout += "(";
			} else if (c == ')')
			{
				sout += ")\n";
				--level;
				for (i = 0; i < level; ++i)
					sout += indent;
			} else
				sout += c;
			++p;
		}
		return (sout);
	}

	public static String plural(int n)
	{
		if (n != 1)
			return ("s");
		return ("");
	}

	public static String plurals(String singularNoun, int n)
	{
		return plurals(singularNoun, (long) n);
	}

	public static String plurals(String singularNoun, long n)
	{
		if (n == 1)
			return "1 " + singularNoun;
		if (singularNoun.endsWith("y"))
			return n + " " + singularNoun.substring(0, singularNoun.length() - 1) + "ies";
		if (singularNoun.endsWith("s") || singularNoun.endsWith("z"))
			return n + " " + singularNoun + "es";
		return n + " " + singularNoun + "s";
	}

	// In case you don't like our computed plural nouns
	public static String plurals(String singularNoun, String pluralNoun, int n)
	{
		if (n == 1)
			return "1 " + singularNoun;
		return n + " " + pluralNoun;
	}

	public static String trim(String s)
	{
		return trim(s, " ");
	}

	public static String trim(String s, String t)
	{
		// Removes from the beginning and the end of s any character
		// which occurs in t. If the first character of t is '^',
		// removes any characters which are NOT in t.
		// '^' can appear as an item in the list. e.g. "^x^" will trim
		// all the characters except 'x' and '^'
		return (trimLeft(trimRight(s, t), t));
	}

	public static String trimLeft(String input)
	{
		return trimLeft(input, " ");
	}

	public static String trimLeft(String input, String t)
	{
		// Implements trim on the beginning of the string
		if (input == null)
			return null;
		int i = 0;
		if (t.startsWith("^"))
			// don't include leading '^' in trim set
			while ((i < input.length()) && (t.indexOf(input.charAt(i)) <= 0))
				++i;
		else
			while ((i < input.length()) && (t.indexOf(input.charAt(i)) >= 0))
				++i;
		return input.substring(i);
	}

	public static String trimRight(String input)
	{
		return trimRight(input, " "); // Defaults to blanks
	}

	public static String trimRight(String input, String t)
	{
		// Implements trim on the end of the string
		if (input == null)
			return null;
		int i = input.length() - 1;
		if (t.startsWith("^"))
			// don't include leading '^' in trim set
			while ((i > 0) && (t.indexOf(input.charAt(i)) <= 0))
				--i;
		else
			while ((i > 0) && (t.indexOf(input.charAt(i)) >= 0))
				--i;
		return input.substring(0, i + 1);
	}

	public static String sanitizeString(String s, String removeChars)
	{ // Returns a string in which the characters in s which exist in removeChars
		// have been translated to non-printable values. (See restoreString)
		StringBuffer rc = new StringBuffer(removeChars);
		String newString = s;
		for (int i = 0; i < removeChars.length(); ++i)
			newString = newString.replace(rc.charAt(i), (char) i);
		return newString;
	}

	public static String restoreString(String s, String removeChars)
	{ // Restores a sanitized string to its original state. (See sanitizeString)
		// The String removeChars in restoreString must be exactly the same as the
		// one used in sanitizeString.
		StringBuffer rc = new StringBuffer(removeChars);
		String newString = s;
		for (int i = 0; i < removeChars.length(); ++i)
			newString = newString.replace((char) i, rc.charAt(i));
		return newString;
	}

	public static String preParse(String s, String token, String delimiter) throws UtilitiesException // for unmatched
																										// delimiters in
																										// s
	{ // Prepares a string for parsing by eliminating the parsing token characters
		// which are contained within quoted strings inside of s.
		// s = original String
		// token = the token to be used in parsing s
		// delimiter = the character used to delimit strings (usually a quote)
		// Note that token and delimiter must be Strings of length 1.
		// After parsing, each element must be restored with restoreString
		// (or postParse which is the same thing), obviously using the same token.
		String newString = "";
		boolean inQuote = false;
		for (int i = 0; i < s.length(); ++i)
		{
			if (s.charAt(i) == delimiter.charAt(0))
			{
				inQuote = !inQuote;
				newString += delimiter;
				continue;
			}
			if (inQuote && s.charAt(i) == token.charAt(0))
				newString += (char) 0;
			else
				newString += s.charAt(i);
		}
		if (inQuote)
			throw new UtilitiesException(UtilitiesException.UNMATCHED_DELIMITER, delimiter);
		// Console.println( "preParse returns " + newString );
		return newString;
	}

	public static String postParse(String s, String token)
	{
		return restoreString(s, token);
	}

	public static String replace(String origString, String oldThing, String newThing)
	{
		int p1 = origString.indexOf(oldThing);
		if (p1 < 0)
			return origString;
		StringBuffer sb = new StringBuffer(origString);
		sb.delete(p1, p1 + oldThing.length());
		sb.insert(p1, newThing);
		return replace(sb.toString(), oldThing, newThing);
	}

	public static int countChar(char c, String image)
	{
		int count = 0;
		for (int i = 0; i < image.length(); ++i)
			if (image.charAt(i) == c)
				++count;
		return count;
	}

	public static String findLongestLine(String image)
	{
		// Expects a String containing either
		// (a) the character pair '\\' followed by 'n' or
		// (b) the single character '\n'
		// to delimit lines
		String delimiter = "";
		if (image.indexOf("\\n") < 0)
		{
			if (image.indexOf('\n') < 0)
			{
				// Neither delimiter found
				return image;
			} else // single character '\n' was found
				delimiter = "\n";
		} else // character pair "\\n" was found
			delimiter = "\\n";
		StringTokenizer st = new StringTokenizer(image, delimiter);
		String line = "";
		String howAboutThis;
		while (st.hasMoreElements())
		{
			howAboutThis = (String) st.nextElement();
			if (howAboutThis.length() > line.length())
				line = howAboutThis;
		}
		return line;
	}

	public static String[] split(String s, String delim)
	{
		Vector<String> v = new Vector<String>(10, 10);
		StringTokenizer st = new StringTokenizer(s, delim);
		while (st.hasMoreElements())
			v.addElement((String) st.nextElement());
		String ans[] = new String[v.size()];
		for (int i = 0; i < v.size(); ++i)
			ans[i] = v.elementAt(i);
		return ans;
	}

	/*****
	 * First test program ***** // Use: Strings -n // where n is the test number you
	 * want to run // Defaults to test #8
	 * 
	 * public static void main( String[] args ) { Clip clip = null; try { clip = new
	 * Clip( args, new String[] { "-8" } ); } catch ( UtilitiesException ux) {
	 * Console.println( ux.getMessage() ); }
	 * 
	 * Console.println("Running main test"); if ( clip.isSet( '1' ) ) {
	 * Console.println("Running test #1"); try { Strings strings = new Strings();
	 * String[] javaFiles; String dir =
	 * "E:\\Projects\\Java\\com\\extant\\utilities\\";
	 * 
	 * String pattern = "*.java"; javaFiles = strings.getMatchingFiles( dir, pattern
	 * ); Console.println( "DOS-style: Found " + javaFiles.length + " files matching
	 * " + pattern ); pattern = ".*\\.java"; javaFiles =
	 * strings.getMatchingFilesRegex( dir, pattern ); Console.println( "regex-style:
	 * Found " + javaFiles.length + " files matching " + pattern );
	 * 
	 * pattern = "*.java~"; javaFiles = strings.getMatchingFiles( dir, pattern );
	 * Console.println( "DOS-style: Found " + javaFiles.length + " files matching "
	 * + pattern ); pattern = ".*\\.java~"; javaFiles =
	 * strings.getMatchingFilesRegex( dir, pattern ); Console.println( "regex-style:
	 * Found " + javaFiles.length + " files matching " + pattern );
	 * 
	 * String pathAndFile = dir + "example.java"; Console.println( "For: " +
	 * pathAndFile + "\n" + " PATH = " + Strings.fileSpec( "PATH", pathAndFile ) +
	 * "\n" + " path = " + Strings.fileSpec( "path", pathAndFile ) + "\n" + " FILE =
	 * " + Strings.fileSpec( "FILE", pathAndFile ) + "\n" + " FO = " +
	 * Strings.fileSpec( "FO" , pathAndFile ) + "\n" + " EXT = " + Strings.fileSpec(
	 * "EXT" , pathAndFile ) ); pathAndFile = dir + "example"; Console.println(
	 * "For: " + pathAndFile + "\n" + " PATH = " + Strings.fileSpec( "PATH",
	 * pathAndFile ) + "\n" + " path = " + Strings.fileSpec( "path", pathAndFile ) +
	 * "\n" + " FILE = " + Strings.fileSpec( "FILE", pathAndFile ) + "\n" + " FO = "
	 * + Strings.fileSpec( "FO" , pathAndFile ) + "\n" + " EXT = " +
	 * Strings.fileSpec( "EXT" , pathAndFile ) + "\n" + " .. = " + Strings.fileSpec(
	 * ".." , pathAndFile ) ); Console.println( "For " + dir + "\n" + " PATH = " +
	 * Strings.fileSpec( "PATH", dir ) + "\n" + " path = " + Strings.fileSpec(
	 * "path", pathAndFile ) + "\n" + " FILE = " + Strings.fileSpec( "FILE", dir ) +
	 * "\n" + " FO = " + Strings.fileSpec( "FO" , dir ) + "\n" + " EXT = " +
	 * Strings.fileSpec( "EXT" , dir ) + "\n" + " .. = " + Strings.fileSpec( ".." ,
	 * dir ) ); } catch (Exception x) { Console.println( x.getMessage() );
	 * x.printStackTrace(); } } if ( clip.isSet( '2' ) ) { Console.println( "Running
	 * test #2 ..." ); long t = 12345678L; Console.println( t + " -> " +
	 * formatPennies( t ) ); t = 123456789L; Console.println( t + " -> " +
	 * formatPennies( t ) ); t = 1234567899L; Console.println( t + " -> " +
	 * formatPennies( t ) ); t = 12345600L; Console.println( t + " -> " +
	 * formatPennies( t ) ); t = 12345670L; Console.println( t + " -> " +
	 * formatPennies( t ) ); t = 0L; Console.println( t + " -> " + formatPennies( t
	 * ) ); t = -123456789; Console.println( t + " -> " + formatPennies( t ) );
	 * 
	 * String[] pennies = new String[] { "123.45", "123,456.78", "123", "123.4",
	 * "12x.45" }; for (int i=0; i<pennies.length; ++i ) Console.println( pennies[i]
	 * + " --> " + fixPennies( pennies[i] ) + " valid=" + isValidPennies( pennies[i]
	 * ) ); } if ( clip.isSet( '3' ) ) { Console.println("Running test #3"); String
	 * test[] = new String[] { "1,2,3,4", "1,2,3,4,'this is easy.',5,6,7,8",
	 * "1,2,3,4,'this is hard, right?',5,6,7,8", "1,2,3,4,'and this is even
	 * worse!,5,6,7,8" }; int i=-1; try { for (i=0; i<test.length; ++i) {
	 * Console.println( "Test String: " + test[i] ); Console.print( "elements: " );
	 * StringTokenizer st = new StringTokenizer( preParse(test[i], ",", "'" ), ","
	 * ); while ( st.hasMoreElements() ) { Console.print( " <" + postParse(
	 * (String)st.nextElement(), "," ) + ">" ); } Console.println( "" ); } } catch
	 * (UtilitiesException ux) { Console.println( ux.getMessage() + " in line " + i
	 * + ":" ); Console.println( test[i] ); } catch (Exception x) { Console.println(
	 * x.getMessage() ); } } if ( clip.isSet( '4' ) ) { Console.println("Running
	 * test #4"); Console.println( "'" + Strings.format( 12345, "####0" ) + "'" );
	 * Console.println( "'" + Strings.format( 1, "####0" ) + "'" ); Console.println(
	 * "'" + Strings.colFormat( 1, 2 ) + "'" ); } if ( clip.isSet( '5' ) ) {
	 * Console.println("Running test #5"); String[] test = new String[] {
	 * "1\\n12\\n123\\n12345\\n1234\\n" , "This is short\\nThis is a little
	 * longer\\nAnd this one really wins the turkey" , "\nA pathological case\n\n" ,
	 * "An easy one" }; for (int i=0; i<test.length; ++i) Console.println( "The
	 * longest line in test[" + i + "] is:\n'" + Strings.findLongestLine( test[i] )
	 * + "'" ); } if ( clip.isSet( '6' ) ) { Console.println("Running test #6");
	 * String test[] = new String[] { "a string with one #oldThing# to be replaced"
	 * , "multiple #oldThing# fields that should change to #oldThing#" }; for (int
	 * i=0; i<test.length; ++i) Console.println( test[i] + " --> " + replace(
	 * test[i], "#oldThing#", "longNewThing" ) ); }
	 * 
	 * if ( clip.isSet( '7' ) ) { Console.println("Running test #7"); long values[]
	 * = new long[] { 99L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L,
	 * 100000000L, 1000000000L }; for (int i=0; i<values.length; ++i)
	 * Console.println( formatPennies( values[i], "," ) + " --> " + pennies2Text(
	 * values[i] ) ); } if ( clip.isSet( '8' ) ) { Console.println("Running test
	 * #8"); String s1[] = new String[] { "12345", "655*", "655*", "*.*" }; String
	 * s2[] = new String[] { "12345", "655" , "655x", "file.ext" }; for (int i=0;
	 * i<s1.length; ++i) Console.println( s2[i] + " match " + s1[i] + " = " + match(
	 * s1[i], s2[i] ) ); }
	 * 
	 * Console.prompt( "Done ..." ); } /
	 *****/
}
