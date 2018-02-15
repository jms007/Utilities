package com.extant.utilities;

import java.text.*;
import java.awt.*;
import javax.swing.JTextField;

public class InputChecker
{
  // These are the types of checks that I can do:
  public static final int STRING = 1;
  public static final int STRING_NOT_EMPTY = 2;
  public static final int INTEGER = 3;
  public static final int NONNEGATIVE_INTEGER = 4;
  public static final int POSITIVE_INTEGER = 5;
  public static final int FLOAT = 6;
  public static final int NONNEGATIVE_FLOAT = 7;
  public static final int POSITIVE_FLOAT = 8;
  public static final int DATE = 9;
  public static final int PAST_DATE = 10;
  public static final int FUTURE_DATE = 11;

  public static final long jan_1_1970 = new Julian( 1969, 12, 31 ).getDayNumber();

//  private StatusBar statusBar = null;
  public InputChecker()
  {
  }
//  public InputChecker( StatusBar errorStatusBar )
//  {
//    statusBar = errorStatusBar;
//  }
//
//  public boolean isValid( TextFieldControl field, int type )
  public boolean isValid( JTextField field, int type )
  {
    String text = Strings.trim( field.getText(), " " );
    switch ( type )
    {
      case STRING:
        return true;
      case STRING_NOT_EMPTY:
        return text.length() > 0;
      case INTEGER:
        return text.length() > 0 && Strings.isDecimalNumber( text );
      case NONNEGATIVE_INTEGER:
        if ( !isValid( field, INTEGER ) ) return false;
        return Integer.parseInt( text ) >= 0;
      case POSITIVE_INTEGER:
        if ( !isValid( field, INTEGER ) ) return false;
        return Integer.parseInt( text ) > 0;
      case FLOAT:
        return text.length() > 0 && isValidFloat( text );
      case NONNEGATIVE_FLOAT:
        if ( !isValid( field, FLOAT ) ) return false;
        return new Float( text ).floatValue() >= 0.0;
      case POSITIVE_FLOAT:
        if ( !isValid( field, FLOAT ) ) return false;
        return new Float( text ).floatValue() > 0.0;
      case DATE:
        return text.length() > 0 && new Julian( text ).isValid();
      case PAST_DATE:
        if ( !isValid( field, DATE ) ) return false;
        return new Julian().isLaterThan( new Julian( text ) );
      case FUTURE_DATE:
        if ( !isValid( field, DATE ) ) return false;
        return new Julian().isEarlierThan( new Julian( text ) );
      default:
        return false;
    }
  }

  private void reportError( JTextField field, String msg )
  throws ParseException
  {
    field.requestFocus();
    throw new ParseException( msg, 0 );
  }

  private String strip( JTextField field, String fieldName )
  throws ParseException
  {
    String text = Strings.trim( field.getText(), " " );
    if ( text.length() == 0 ) reportError( field, fieldName + " field was not entered" );
    return text;
  }

  // Note:  Don't call this if you will accept an empty String (there
  //        can be no errors in this case).  Just use:
  //            field.getText()
//  public String getString( TextFieldControl field, String fieldName )
  public String getString( JTextField field, String fieldName )
  throws ParseException
  {
    return strip( field, fieldName );
  }

//  public int getInt( TextFieldControl field, String fieldName )
  public int getInt( JTextField field, String fieldName )
  throws ParseException
  {
    return getInt( field, fieldName, INTEGER );
  }

//  public int getNonNegInt( TextFieldControl field, String fieldName )
  public int getNonNegInt( JTextField field, String fieldName )
  throws ParseException
  {
    return getInt( field, fieldName, NONNEGATIVE_INTEGER );
  }

//  public int getPosInt( TextFieldControl field, String fieldName )
  public int getPosInt( JTextField field, String fieldName )
  throws ParseException
  {
    return getInt( field, fieldName, POSITIVE_INTEGER );
  }

//  public int getInt( TextFieldControl field, String fieldName, int type )
  public int getInt( JTextField field, String fieldName, int type )
  throws ParseException
  {
    String text = strip( field, fieldName );
    if ( !isValid( field, type ) ) reportError( field, fieldName + " field is not valid." );
    return Integer.parseInt( text );
  }

//  public float getFloat( TextFieldControl field, String fieldName )
  public float getFloat( JTextField field, String fieldName )
  throws ParseException
  {
    return getFloat( field, fieldName, FLOAT );
  }

//  public float getNonNegFloat( TextFieldControl field, String fieldName )
  public float getNonNegFloat( JTextField field, String fieldName )
  throws ParseException
  {
    return getFloat( field, fieldName, NONNEGATIVE_FLOAT );
  }

//  public float getPosFloat( TextFieldControl field, String fieldName )
  public float getPosFloat( JTextField field, String fieldName )
  throws ParseException
  {
    return getFloat( field, fieldName, POSITIVE_FLOAT );
  }

//  public float getFloat( TextFieldControl field, String fieldName, int type )
  public float getFloat( JTextField field, String fieldName, int type )
  throws ParseException
  {
    String text = strip( field, fieldName );
    if ( !isValid( field, type) ) reportError( field, fieldName + " field is not valid." );
    return new Float( text ).floatValue();
  }

//  public Julian getDate( TextFieldControl field, String fieldName )
  public Julian getDate( JTextField field, String fieldName )
  throws ParseException
  {
    return getDate( field, fieldName, DATE );
  }

//  public Julian getPastDate( TextFieldControl field, String fieldName )
  public Julian getPastDate( JTextField field, String fieldName )
  throws ParseException
  {
    return getDate( field, fieldName, PAST_DATE );
  }

//  public Julian getFutureDate( TextFieldControl field, String fieldName )
  public Julian getFutureDate( JTextField field, String fieldName )
  throws ParseException
  {
    return getDate( field, fieldName, FUTURE_DATE );
  }

//  public Julian getDate( TextFieldControl field, String fieldName, int type )
  public Julian getDate( JTextField field, String fieldName, int type )
  throws ParseException
  {
    String text = strip( field, fieldName );
    if ( !isValid( field, type) ) reportError( field, fieldName + " field is not valid." );
    return new Julian( text );
  }

  public static boolean isValidFloat( String s )
  {
    if ( s == null ) return false;
    if ( s.equals( "" ) ) return false;
    if ( s.startsWith( "+" ) || s.startsWith( "-" ) ) s = s.substring( 1 );
    int dot = s.indexOf( '.' );
    if ( dot != s.lastIndexOf( '.' ) ) return false;
    if ( dot >= 0 )
      {
      if ( !Strings.isDecimalNumber( s.substring( 0, dot ) ) ) return false;
      if ( !Strings.isDecimalNumber( s.substring( dot + 1 )) ) return false;
      return true;
      }
    else return Strings.isDecimalNumber( s );
  }

  // Note Date is ambiguous:  java.sql.Date   and   java.util.Date

  // Try to parse a string and make an SQL date (best effort)
  public static java.sql.Date sillyDate( String sDate )
  {
    return sillyDate( new Julian( sDate ) );
  }

  // Convert a Julian date to an SQL date
  public static java.sql.Date sillyDate( Julian jDate )
  {
    return new java.sql.Date( (jDate.getDayNumber() - jan_1_1970) * 86400000L );
  }

}

