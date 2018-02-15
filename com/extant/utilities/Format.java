/********************************************************************
 * THIS CLASS IS DEPRICATED.  USE CLASS com.extant.utilities.Strings
 ********************************************************************/

package com.extant.utilities;
import java.lang.*;
import java.util.*;
import java.text.*;

public class Format {

  // The following strings set print spacing for HP printers.
  // (The little boxes are <ESC> characters)
  public static final String HPcondensed = "(s0T&k2S";
  public static final String HPnormal = "&k0S&l6D";

  public static String colFormat( long v, String f )
    { // Just a name change from jmsFormat
      // Converts using Long.toString and right-justifies a long integral value
    String sv = new Long( v ).toString();
    int fillLength = f.length() - sv.length();
    StringBuffer sb = new StringBuffer( f.length() );
    sb.append( f.substring( 0, fillLength ) );
    sb.append( sv );
    return sb.toString();
    }

  public static String colFormat( int v, String f )
    { // Converts using Long.toString and right-justifies an integer integral value
    return colFormat( (long)v, f );
    }

  public static String colFormat( int value, int colWidth )
    {
    DecimalFormat sf = new DecimalFormat( "###,##0" ); // for shares
    return ( rightJustify( sf.format( value ), colWidth ) );
    }

  public static String colFormat( double value, int colWidth )
    {
    DecimalFormat df = new DecimalFormat( "##,###,##0.00" ); // for dollars
    return ( rightJustify( df.format( value ), colWidth ) );
    }

  public static String rightJustify( String s, int colWidth )
    {
    String ans = "";
    for (int i=0; i<colWidth-s.length(); ++i) ans += " ";
    return ( ans + s );
    }

  public static String colFormat( String sv, int width )
    { // Right-justifies a string

    StringBuffer sb = new StringBuffer( width );
    int fillLength = width - sv.length();
    if ( width <= 0 ) return sv;
    for (int i=0; i<fillLength; ++i) sb.append( ' ' );
    sb.append( sv );
    return sb.toString();
    }

  public static String colFormat( String sv, String f )
    { // Left-justifies a string (truncating, if necessary)
    int finalLength = f.length();
    int fillLength = f.length() - sv.length();
    if ( fillLength < 0 ) finalLength = sv.length();
    StringBuffer sb = new StringBuffer( finalLength );
    sb.append( sv );
    if ( fillLength > 0 ) sb.append( f.substring( 0, fillLength ) );
    return sb.toString().substring( 0, f.length() );
    }

//  public static String colFormatDollars( long pennies, String f )
//    { // This is old -- better to use DollarFormatter methods for dollar fields
//    DecimalFormat df = new DecimalFormat( "###,###,##0.00" );
//    String sv = trimLeft( df.format( pennies / 100.0 ), "0" );
//    return f.substring( 0, f.length() - sv.length() ) + sv;
//    }

  // Re-formats a scientific-notation string ( x.xxxxxxEyy ) to
  // a regular number
  public static String e2d( String s )
    {
    if ( s.indexOf( "E" ) == -1 ) return( s );
    StringTokenizer st = new StringTokenizer( s, ".E" );
    String i = (String)st.nextElement();
    String frac = (String)st.nextElement();
    int e = Integer.parseInt( (String)st.nextElement() );
    while ( frac.length() < e ) frac += "0";
    String a = i + frac.substring( 0, e ) + "." +
      frac.substring( e );
    return ( a );
    }

  public static String toHexString( byte b )
    {
    byte[] ba = new byte[1];
    ba[0] = b;
    return ( toHexString( ba, 1, ' ' ) );
    }

  public static String toHexString( byte[] bytes )
    {
    return ( toHexString( bytes, bytes.length, ' ' ) );
    }

  public static String toHexString( byte[] bytes, int nBytes )
    {
    return ( toHexString( bytes, nBytes, ' ' ) );
    }

  public static String toHexString( byte[] bytes, int nBytes, char separator )
    {
    String ans = "";
    for (int i=0; i<nBytes; ++i)
      {
      if ( (((int)bytes[i]) & 0xFF) < 0x10 ) ans += "0";
      if ( separator == '\0' )
        ans += Integer.toHexString( (((int)bytes[i]) & 0xFF ) ).toUpperCase();
      else
        ans += Integer.toHexString( (((int)bytes[i]) & 0xFF ) ).toUpperCase() + separator;
      }
    return ( ans );
    }

  // skip leading & trailing blanks, THEN parseInt
//  public static int parseInt( String s )
//    {
//    int i = 0, j;
//    while (s.substring(i, i+1).equals( " " ) ) ++i;
//    if ( (j = s.substring( i ).indexOf( ' ' )) > 0 )
//      return Integer.parseInt( s.substring( i, j ) );
//    return Integer.parseInt( s.substring( i ) );
//    // A better version (if it had been tested) would be:
//    //return Integer.parseInt( trim( s, " " ) );
//    }


  public static long parseLong( String s )
    {
    String s1 = trim( s, " " );
    long l=0L;
    int p=0;
    while ( (p < s.length()) && isDecimalDigit( s1.charAt( p ) ) )
      l = l * 10 + ( s1.charAt( p++ ) - '0' );
    return ( l );
    }

  public static int parseInt( String s )
    {
    return ( (int)parseLong( s ) );
    }

  public static boolean isDecimalDigit( char c )
    {
    return ( (c >= '0') && (c <= '9') );
    }

  public static boolean isValidFloat( String s )
    {
    try
      {
      Float f = Float.valueOf( s );
      return true;
      }
    catch (NumberFormatException nfx)
      {
      return false;
      }
    }

  public static float parseFloat( String s )
  throws NumberFormatException
    {
    return Float.valueOf( s ).floatValue();
    }

  public static boolean isValidDouble( String s )
    {
    return isValidFloat( s );
    }

  public static double parseDouble( String s )
  throws NumberFormatException
    {
    return Double.valueOf( s ).doubleValue();
    }

  public static long parseHex( String s )
    {
    long a=0;
    int p=0;
    char c;
    while ( s.charAt( p ) == ' ' ) ++p;
    for (int i=0; i<16; ++i)
      {
      if ( p >= s.length() ) break;
      c = s.toUpperCase().charAt( p++ );
      if ( (c >= 'A') && (c <= 'F') ) a = (a << 4) + (c - 'A' + 10);
      else if ( (c >= '0') && (c <= '9') ) a = (a << 4) + (c - '0');
      else break;
      }
    return ( a );
    }

  public static byte[] parseHexBytes( String s )
    {
    return parseHexBytes( s, s.length() / 2 );
    }

  public static byte[] parseHexBytes( String s, int nBytes )
    {
    int m;
    if ( s.indexOf( ' ' ) >= 0 ) m = 3; else m = 2;
    byte[] ans = new byte[nBytes];
    for (int i=0; i<nBytes; ++i)
      {
      ans[i] = (byte)parseHex( s.substring( i*m, (i+1)*m ) );
      }
    return ( ans );
    }

  public static boolean isDecimalNumber( String s )
    {
    if ( s.length() <= 0 ) return false;
    for (int i=0; i<s.length(); ++i)
      if ( !isDecimalDigit( s.charAt( i ) ) ) return ( false );
    return ( true );
    }

  public static String indentedString( String sin )
    {
    String sout="";
    int i, p=0;
    int level = -1;
    String indent="   ";

    while ( p < sin.length() )
      {
      char c = sin.charAt( p );
      if ( c == '(' )
        {
        sout += "\n";
        ++level;
        for (i=0; i<level; ++i) sout += indent;
        sout += "(";
        }
      else if ( c == ')' )
        {
        sout += ")\n";
        --level;
        for (i=0; i<level; ++i) sout += indent;
        }
      else sout += c;
      ++p;
      }
    return ( sout );
    }

  public static String plural( int n )
    {
    if ( n != 1 ) return ( "s" );
    return ( "" );
    }

  public static String plural( String singularNoun, int n )
    {
    if ( n == 1 ) return singularNoun;
    if ( singularNoun.endsWith( "y" ) )
      return singularNoun.substring( 0, singularNoun.length() - 1 ) + "ies";
    else if ( singularNoun.endsWith( "s" ) )
      return singularNoun + "es";
    return singularNoun + "s";
    }

  public static String plural( String singularNoun, String pluralNoun, int n )
    {
    if ( n == 1 ) return singularNoun;
    return pluralNoun;
    }

  public static String trim( String s, String t )
    {
    // Removes from the beginning and the end of s any character
    // which occurs in t.  If the first character of t is '^',
    // removes any characters which are NOT in t.
    // '^' can appear as an item in the list.  e.g. "^x^" will trim
    // all the characters except 'x' and '^'
    return ( trimLeft( trimRight( s, t ), t ) );
    }

  public static String trimLeft( String input, String t )
    {
    // Operates like trim, but only on the beginning of the string
    if ( input == null ) return null;
    int i = 0;
    if ( t.startsWith( "^" ) )
      // don't include leading '^' in trim set
      while ( (i < input.length()) && (t.indexOf( input.charAt(i) ) <= 0) ) ++i;
    else
      while ( (i < input.length()) && (t.indexOf( input.charAt(i) ) >= 0) ) ++i;
    return input.substring( i );
    }

  public static String trimRight( String input, String t )
    {
    // Operates like trim, but only on the end of the string
    if ( input == null ) return null;
    int i = input.length() - 1;
    if ( t.startsWith( "^" ) )
      // don't include leading '^' in trim set
      while ( (i > 0) && (t.indexOf( input.charAt(i) ) <= 0) ) --i;
    else
      while ( (i > 0) && (t.indexOf( input.charAt(i) ) >= 0) ) --i;
    return input.substring( 0, i+1 );
    }

/********* TESTING ****
  public static void testit()
    {
    Console.println( colFormat( 123, "   " ) );
    Console.println( colFormat( 123, "0000" ) );
    Console.println( colFormat( 12345678L, "        0" ) );
    Console.println( e2d( "3.156049486451478E9" ) );
    Console.println( "trim tests: '" +
      trimLeft(  "xyz", "x" ) + "'  '" +
      trimRight( "xyzzz", "z" ) + "'  '" +
      trim(      "xxx", "x" ) + "'  '" +
      trim(      "xyz", "^xyz" ) + "'  '" +
      trim(      "xyz", "xz" ) + "'" );
    DecimalFormat df = new DecimalFormat( "#,###,##0.00" );
    double dv[] = {1234567.89, 0.0, 0.015, 100.014, 100.01499999999, 100.015};
    for (int i=0; i<dv.length; ++i)
      {
      Console.print( "default format: '" + dv[i] + "'   " );
      Console.println( "result: '" + trimLeft( df.format( dv[i] ), "0" ) + "'" );
      }
    byte[] bytes = new byte[256];
    for (int i=0; i<256; ++i) bytes[i] = (byte)i;
    String s = Format.toHexString( bytes );
    Console.println( s );
    for (int i=0; i<256; ++i) bytes[i] = (byte)0;
    bytes = parseHexBytes( s, 256 );
    for (int i=0; i<256; ++i)
      {
      Console.print( colFormat( (long)bytes[i] & 0xFF , "    " ) );
      if ( (i & 0xF) == 0xF ) Console.println( "" );
      }
    Console.println( "3 " + plural( "man", "men", 3 ) + " can likely subdue 1 " +
      plural( "man", "men", 1 ) + " but 3 " + plural( "fly", 3 ) +
      " probably can't." );
    }

  public static void main(String[] args)
    {
    try
      {
      testit();
      }
    catch (Exception e)
      {
      e.printStackTrace();
      }
    Console.prompt( "Done ..." );
    }
/***END OF TEST CODE */

  }

