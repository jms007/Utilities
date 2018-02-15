package com.extant.utilities;

import java.io.*;  // for main() only
import java.text.*;
import java.util.*;
import java.lang.*;

/*********
 * Notes on improvements
 * date parser
 *  Common formats                                                              regex (tested)
 *      an integer (representing a Julian Day Number)                           "\d+"
 *      the 'toString' format: j+t+mm/dd/yyyy hh:mm:ss                          "\d+\+\d+\+\d\d/\d\d/\d{4}[ ]\d\d:\d\d:\d\d"
 *          where '+' is a delimiter, j is the Julian Day Number,
            and t is seconds since midnight
 *      the 'toShortString' format: JulianDayNumber + '~' + TODSeconds          "\d+~\d+"
 *      the 'SQL' format: yyyy-mm-dd                                            "\d{4}\-\d\d\-\d\d"
 *      the java.util.Date() format
 *          IETF standard date syntax: "Sat, 12 Aug 1995 13:30:00 GMT"          "\w{3},[ ]\d{1,2}[ ]\w{3}[ ]\d{4}[ ]\d\d:\d\d:\d\d[ ]\w{3}"
 *      DTG mm/dd/yyyy hhmmss                                                   "\d\d/\d\d/\d{4}[ ]\d{6}"
 *      SORTABLE_DTG yyyy-mm-dd hhmmss                                          "\d{4}-\d\d-\d\d[ ]\d{6}"
 *      mmddyy                                                                  "\d{6}"
 *      yymmdd                                                                  "\d{6}"
 *      mmddyyyy                                                                "\d{8}"
 *      yyyymmdd                                                                "\d{8}"
 *      mm/dd/yy and other delimiters '-'                                       "\d\d[/-]\d\d[/-]\d\d"
 *      mm/dd/yyyy                                                              "\d\d[/-]\d\d[/-]\d{4}"
 *  Consider algorithm to extend yy to yyyy (See oracle section in notes file)
 *  Also review fixDate to correct intentional invalid dates used by VL
 *      (Dec 32 and Jan 0)
 * 
 *      
 * @author jms
 */
public class Julian
  {
                          // CONSTRUCTORS

  public Julian ()
    {
    // Set to current date & time
    GregorianCalendar now = new GregorianCalendar ();
    setDayNumber( DayNumber(
                  now.get (Calendar.YEAR),
                  now.get (Calendar.MONTH) + 1,
                  now.get (Calendar.DATE) )
                   );
    int hour = now.get( Calendar.HOUR_OF_DAY );
    setTime( hour,
             now.get( Calendar.MINUTE ),
             now.get( Calendar.SECOND ) // Does this return microseconds ??
            );
    valid = true; // have faith in Gregory
    return;
    }


  public Julian (long jd)
    {
    this( jd, 0L );
    }

  public Julian( long jd, long seconds )
    {
    setDayNumber( jd );
    TODSeconds = seconds;
    reCompute();
    valid = true;
    }

  public Julian( int y, int m, int d )
    {
    this( y, m, d, 0L );
    }

  public Julian( int y, int m, int d, long TODSeconds )
    {
    JulianDayNumber = DayNumber( y, m, d );
    this.TODSeconds = TODSeconds;
    reCompute();
    if ( !isValid( y, m, d ) ) parseInput = "(" + y + "," + m + "," + d + ")";
    }
  
  public Julian( String s )
    {
    // Expects a string in one of these formats:
    //   an integer (representing a Julian Day Number) ("\\d+"),
    //   the 'toString' format,
    //   the 'toShortString' format, or
    //   the java.util.Date() format.
    // Failing these, we try using our own date parser
    //
    long a[] = parse( s );
    JulianDayNumber = a[0];
    TODSeconds = a[1];
    reCompute();
    valid = isValid( (int)a[2], (int)a[3], (int)a[4] );
//Console.println( "[Julian] parse returns " + a[0] + ", " + a[1] + ", " +
//a[2] + ", " + a[3] + ", " + a[4] + " valid=" + valid );
    if ( !valid ) parseInput = s;
    }

  public static long DayNumber
    ( int yr
    , int mn
    , int dy
    )
      // Returns the Julian Day Number for the given calendar date
      {
      if (mn < 3)
        {
        yr--;
        mn += 12;
        }

      int century = yr / 100;

      int extraDays = mn * 30 + (mn + 1) * 3 / 5 +
                century / 4 - century + yr / 4 + dy;

      long jd = 1721026L + yr * 365L + extraDays;
      return (jd);
    }


  public static int[] CalendarDate( long JulianDayNumber )
    // Returns {year, month, day} for the given Julian Day Number
    {
    int century = (int)
     	((((JulianDayNumber -= (1721026L + 92)) << 2) - 1) / 146097L);
    long DayOfFourCent =
   		((((JulianDayNumber << 2) - century * 146097L - 1) >> 2) << 2) + 3;
    int yearOfCentury = (int) (DayOfFourCent / 1461);
    int dayOfFourYear = (int) (DayOfFourCent - yearOfCentury * 1461L);
    int dayOfYearTimes5 = (dayOfFourYear + 4) / 4 * 5 - 3;

    int year  = century * 100 + yearOfCentury;
    int month = dayOfYearTimes5 / 153;
    int day = (dayOfYearTimes5 - month * 153 + 5) / 5;

    if ((month += 3) > 12)
      {
   	month -= 12;
   	year++;
      }
    int ymd[] = {year, month, day};
    return (ymd);
    }

  public static int DayOfWeek( long JulianDayNumber )
    // Returns the day of the week for the given Julian Day Number
    // (0 = Sunday)
    {
    return ((int) ((JulianDayNumber + 2) % 7));
    }

  public static long subtractDates
    ( int y1, int m1, int d1, int y2, int m2, int d2 )
    // Computes the number of days from date1 to date2
    {
    return (DayNumber (y2, m2, d2) - DayNumber (y1, m1, d1));
    }

/*
 * 
 * @param date
 * @return 
 */

  // Returns [JulianDay, TODSeconds, y, m, d]
  // Note this always returns values, valid or not.
  // Use isValid( String ) to determine if a string parses to a valid date.
  public static long[] parse( String date )
    {
    //    boolean debug = true;
    int i, j;
    int y=0, m=0, d=0, hour, minute, second;
    long JulianDayNumber=-1;
    long TODSeconds=0L;
    int[] ymd;
    String time="";
    StringTokenizer st=null;
    long[] INVALID_DATE = {0, 0, 1999, 2, 29};

    date = Strings.trim( date, " " );
    if ( date.equals( "" ) ) return INVALID_DATE;

    // If date is a decimal number with > 6 digits, we assume it's a Julian Day Number
    // Append DELIMITER, and it will parse correctly
    if ( date.length() > 6 && Strings.isValidLong( date ) ) date += DELIMITER;

    try
      {
      i = date.indexOf( DELIMITER );
      if ( i > 0 )
        {
//if (debug) Console.println("trying default toString formats ...");
        // This is either 'toString()' or 'toShortString()' format
        // j+t+mm/dd/yyyy+hh:mm:ss   OR
        // j+t
        // where
        //   + is the delimiter
        //   j=Julian Day
        //   t=Seconds from midnight

        if ( Strings.isDecimalNumber( date.substring( 0, i ) ) )
          JulianDayNumber = Long.parseLong( date.substring( 0, i ) );
        j = date.substring( i+1 ).indexOf( DELIMITER );
        if (j < 0)
          {
          if ( Strings.isDecimalNumber( date.substring( i+1 ) ) )
            TODSeconds = Long.parseLong( date.substring( i+1 ) );
          else TODSeconds = 0L;
          }
        else
          {
          if ( Strings.isDecimalNumber( date.substring( i+1 ).substring( 0, j )) )
            TODSeconds = Long.parseLong( date.substring( i+1 ).substring( 0, j ));
          else TODSeconds = 0L;
          }
        // These are always valid, but we return the derived
        // y,m,d anyway, to be consistent
        ymd = CalendarDate( JulianDayNumber );
        y = ymd[0]; m=ymd[1]; d=ymd[2];
        }
      else if ( (date.length() > 22) && (date.charAt( 22 ) == 'T' ) )
        {
//if (debug) Console.println("trying java date format ..." );
        // This is probably a java.util.Date() format:
        //    Wed Nov 03 09:52:59 PST 1999 -or-
        //    Sun Mar 30 10:54:32 GMT-06:00 2003
        st = new StringTokenizer( date, " " );
        st.nextElement(); // skip DOW
        String s = (String)st.nextElement(); // month
        for (m=0; m<12; ++m) if ( s.equalsIgnoreCase( monthAbbr[m] ) ) break;
        if ( ++m > 12 ) ; // We shouldn't even be here
        s = (String)st.nextElement(); // day
        if ( Strings.isValidInt( s ) ) d = Integer.parseInt( s );
        s = (String)st.nextElement(); // time
        hour   = Integer.parseInt( s.substring( 0, 2 ) );
        minute = Integer.parseInt( s.substring( 3, 5 ) );
        second = Integer.parseInt( s.substring( 6, 8 ) );
        st.nextElement(); // skip timezone
        y = Integer.parseInt( (String)st.nextElement() );
//        for (m=0; m<12; ++m)
//          if (date.substring( 4, 7 ).equalsIgnoreCase( monthAbbr[m] ) )
//            break;
//        if ( m >= 12 ) ; //!! hmmmmm
//        ++m;
//        d      = Integer.parseInt( date.substring(  8, 10 ) );
//        hour   = Integer.parseInt( date.substring( 11, 13 ) );
//        minute = Integer.parseInt( date.substring( 14, 16 ) );
//        second = Integer.parseInt( date.substring( 17, 19 ) );
//        y   = Integer.parseInt( date.substring( 24, 28 ) );
        JulianDayNumber = DayNumber( y, m, d );
        TODSeconds = ((long)hour * 3600L) + ((long)minute * 60L) + (long)second;
        }
      else
        { // try our parser
//if (debug) Console.println("trying our parser...");
        String delimiters = "/-, ";
        if ( !Strings.containsAny( date, delimiters ) )
          {
          if ( date.length() == 6 && Strings.isValidInt( date ) )
            { // GL Date format
//if (debug) Console.println( "going for GL date" );
            y = Integer.parseInt( date.substring( 0, 2 ) );
            m = Integer.parseInt( date.substring( 2, 4 ) );
            d = Integer.parseInt( date.substring( 4 ) );
            }
          }
        else
          {
//if (debug) Console.println( "tokenizing " + date + " (" + delimiters + ")" );
          st = new StringTokenizer( date, delimiters );
          String t = (String)st.nextElement();
          if ( Strings.isDecimalNumber( t ) ) m = Integer.parseInt( t );
          if ( m > 100  && date.indexOf( '-' ) == 4 )
            { // Probably an SQL date:
              //   2001-2-28
//if (debug) Console.println("going for SQL date");
            y = m;
            m = Integer.parseInt( (String)st.nextElement() );
            d = Integer.parseInt( (String)st.nextElement() );
//if (debug) Console.println("SQL got " + y + " " + m + " " + d );
            }
          else if ( m > 0 )
            { // Probably a numeric date, e.g. 7/14/88
//if (debug) Console.println("going for mm/dd/yy format" );
            d = Integer.parseInt( (String)st.nextElement() );
            y = Integer.parseInt( (String)st.nextElement() );
            }
          else
            { // Probably like Jul 14, 88 or July 14, 1988
//if (debug) Console.println( "trying for Jul 14, 88 or July 14, 1988 format" );
            for (m = 0; m<12; ++m)
              if (t.toUpperCase().startsWith( monthAbbr[m].toUpperCase() ) ) break;
            if (++m > 12) throw new ParseException( "Invalid date: " + date, -1 );
            d = Integer.parseInt( st.nextToken() );
            y = Integer.parseInt( st.nextToken() );
            }
          }
//        else // there were no date delimiters
//          { // Try GL date format
//if (debug) Console.println( "no delimiters in date" );
//          if ( date.length() == 6 && Strings.isValidInt( date ) )
//            {
//if (debug) Console.println( "going for GL date" );
//            y = Integer.parseInt( date.substring( 0, 2 ) );
//            m = Integer.parseInt( date.substring( 2, 4 ) );
//            d = Integer.parseInt( date.substring( 4 ) );
//            st = new StringTokenizer( "", "." );  st.nextElement(); // defeat the time parser
//            }
//          }

        // Note you can't enter a date in the first century, e.g. Apr 15, 0088
        // it will get translated to 1988
        if (y < 50) y += 2000;
        else if (y < 100) y += 1900;
        JulianDayNumber = DayNumber( y, m, d );
//if (debug)Console.println( "JulianDayNumber set to " + JulianDayNumber );


        // Now the time   !! needs work
//if (debug) Console.println( "ready for time; moreElements = " + st.hasMoreElements() );
        // all this can do right now is decode hhmmss, hh:mm:ss, or hh:mm
        if ( st.hasMoreElements() ) time = (String)st.nextElement();
        else time = "";
//if (debug) Console.println( "time=" + time );
        if ( time.length() == 0 ) TODSeconds = hour = minute = second = 0;
        else if ( Strings.isDecimalNumber( time ) )
          {
//if (debug) Console.println( "found time hhmm[ss]" );
          long hms = Long.parseLong( time );
          if ( time.length() > 4 )
            {
            second = (int)(hms % 100);
            hms = (int)(hms / 100);
            }
          else
            {
            second = 0;
            }
          hour = (int)(hms / 100);
          minute = (int)(hms % 100);
          TODSeconds = ((long)hour * 3600L) + ((long)minute * 60L) + (long)second;
          }
        else if ( time.indexOf( ':' ) >= 0 )
          {
//if (debug) Console.println( "found time hh:mm[:ss]" );
          int p1 = time.indexOf( ':' );
          int p2 = time.lastIndexOf( ':' );
          hour = Integer.parseInt( time.substring( 0, p1 ) );
          if ( p2 == p1 )
            {
            minute = Integer.parseInt( time.substring( p1+1 ) );
            second = 0;
            }
          else
            {
            minute = Integer.parseInt( time.substring( p1+1, p2 ) );
            second = Integer.parseInt( time.substring( p2+1 ) );
            }
          TODSeconds = ((long)hour * 3600L) + ((long)minute * 60L) + (long)second;
          }
        else TODSeconds = hour = minute = second = 0;
        }
      }
    catch ( Exception e )
      { // Catch parseInt exceptions here, too
      //e.printStackTrace();
      }
//hour = (int)(TODSeconds / 3600L);
//minute = (int)(TODSeconds - (hour*3600L) / 60L);
//second = (int)(TODSeconds % 60L);
//Console.println("("+y+","+m+","+d+","+hour+","+minute+","+second+")" + "valid="+isValid(y,m,d) );
    long answer[] = new long[5];
    answer[0] = JulianDayNumber;
    answer[1] = TODSeconds;
    answer[2] = y; answer[3] = m; answer[4] = d;
//if (debug) Console.print( "parse returning " );
//if (debug) for (int z=0; z<answer.length; ++z) Console.print( answer[z] + "  " );
//if (debug) Console.println( "" );
    return ( answer ); // return values, valid or not
    }


  public long getDayNumber()
    // Returns the Julian Day Number in this instance
    {
    return (JulianDayNumber);
    }

  public int getDayOfWeek()
    // Returns the day of the week in this instance
    // (0 = Sunday)
    {
    return ( (int)((JulianDayNumber + 2) % 7) );
    }

  public int[] getYMD()
    // Returns the {year, month, day} in this instance
    {
    int ymd[] = {year, month, day};
    return (ymd);
    }

  public int getYear()
    // Returns the year in this instance
    {
    return (year);
    }

  public int getMonth()
    // Returns the month number (1-12) in this instance
    {
    return ( month );
    }

  public String getMonthAbbr()
    // Returns the 3-char month abbreviation in this instance
    {
    return ( monthAbbr[month - 1] );
    }

  public static String getMonthAbbr( int m )
    {
    return ( monthAbbr[m - 1] );
    }

  public String getMonthName()
    // Returns the complete month name in this instance
    {
    return ( monthNames[month - 1] );
    }

  public static String getMonthName( int m )
    {
    return ( monthNames[m - 1] );
    }

  public int getDay()
    // Returns the day of the month in this instance
    {
    return (day);
    }

  public long getTODSeconds()
    // Returns seconds since midnight in this instance
    {
    return (TODSeconds);
    }

  public int[] getTime()
    // Returns {hour, minute, second} in this instance
    {
    int hms[] = {hour, minute, second};
    return (hms);
    }

  public int getHour()
    // Returns the hour in this instance
    {
    return (hour);
    }

  public int getMinute()
    // Returns the minute in this instance
    {
    return (minute);
    }


  public int getSecond()
    // Returns the second in this instance
    {
    return (second);
    }

  public String getHMS()
    // Returns time in format hh:mm:ss (24-hour clock)
    {
    return ( twodformat.format( hour ) + ":" +
             twodformat.format( minute ) + ":" +
             twodformat.format( second )
           );
    }

  public String getHMSap()
    // Returns the time in format hh:mm:s
    // where XM is either AM or PM
    {
    int h;
    String ampm = "AM";

    h = hour;
    if ( h >= 12 ) ampm = "PM";
    if ( h > 12 ) h -= 12;
    return ( twodformat.format( h ) + ":" +
             twodformat.format( minute ) + ":" +
             twodformat.format( second ) +
             ampm
           );
    }

  public String getHMMilitary()
    // Returns time in format HHMM (24-hour clock)
    {
    return twodformat.format( hour) + twodformat.format( minute );
    }

  public String dateToString()
    {
    // Returns the instance date in the format mm/dd/yyyy
    return dateToString( '/' );
    }

  public String dateToString( char separator )
    {
    String s = twodformat.format (month) + separator
       + twodformat.format (day) + separator + year;
    return s;
    }

  public String timeToString()
    // Returns the instance time in format hh:mm:ss
    {
    String s = twodformat.format (hour) + ":"
       + twodformat.format (minute) + ":"
       + twodformat.format (second);
    return (s);
    }

   @Override
  public String toString()
    // Returns the instance data in format
    //    j+t+mm/dd/yyyy hh:mm:ss
    // where + is the delimiter
    // j is the Julian Day Number
    // and t is seconds since midnight
    {
    if ( valid )
      return ( JulianDayNumber + DELIMITER
         + TODSeconds + DELIMITER
         + dateToString () + DELIMITER
         + timeToString () );
    else return ( parseInput );
    }

  public String toString( String format )
    { // !! not thoroughly tested
    String ans="";
    String f;
    int p;
    int h;
    boolean inTime=false;
    boolean milTime=true;

    if ( JulianDayNumber <= 0L ) return ( "<invalid>" );
    if ( !valid ) return ( "<invalid>" );
    if ( format == null || format.equals( "" ) ) // Default mm/dd/yyyy hhmm
      return ( month + "/" + day + "/" + year + " " + getHMMilitary() );
    if ( (format.indexOf( "AM/PM" ) >= 0) || (format.indexOf( "am/pm" ) >= 0) ||
         (format.indexOf( "A/P"   ) >= 0) || (format.indexOf( "a/p"   ) >= 0)
       )
      milTime = false;
    // Other formatting options
    p = 0;
    while ( p < format.length() )
      {
      f = format.substring( p );
      if ( f.startsWith( "mmmm" ) )
        {
        ans += monthNames[month - 1];
        p += 3;
        }
      else if ( f.startsWith( "mmm" ) )
        {
        ans += monthAbbr[month - 1];
        p += 2;
        }
      else if ( f.startsWith( "mm" ) )
        {
        if (inTime) ans += twodformat.format( minute );
        else ans += twodformat.format( month );
        p += 1;
        }
      else if (f.startsWith( "m" ) )
        {
        if (inTime) ans += Integer.toString( minute );
        else ans += Integer.toString( month );
        }
      else if (f.startsWith( "dddd" ) )
        {
        ans += DOWNames[(int)((JulianDayNumber + 2) % 7)];
        p += 3;
        }
      else if (f.startsWith( "ddd" ) )
        {
        ans += DOWAbbr[(int)((JulianDayNumber + 2) % 7)];
        p += 2;
        }
      else if (f.startsWith( "dd" ) )
        {
        ans += twodformat.format( day );
        p += 1;
        }
      else if (f.startsWith( "d" ) )
        ans += Integer.toString( day );
      else if (f.startsWith( "yyyy" ) )
        {
        ans += fourdformat.format( year );
        p += 3;
        }
      else if (f.startsWith( "yy" ) )
        {
        ans += twodformat.format( year % 100 );
        p += 1;
        }
      else if (f.startsWith( "hh" ) )
        {
        if (milTime) ans += twodformat.format( hour );
        else
          {
          h = hour;
          if ( h > 12 ) h -= 12;
          ans += twodformat.format( h );
          }
        p += 1;
        }
      else if (f.startsWith( "h" ) )
        {
        if (milTime) ans += Integer.toString( hour );
        else
          {
          h = hour;
          if ( h > 12 ) h -= 12;
          ans += Integer.toString( h );
          }
        }
      else if (f.startsWith( "ss" ) )
        {
        ans += twodformat.format( second );
        p += 1;
        }
      else if (f.startsWith( "s" ) )
        ans += Integer.toString( second );
      else if (f.startsWith( "AM/PM" ) )
        {
        if (hour < 12 ) ans += "AM";
        else ans += "PM";
        p += 4;
        }
      else if (f.startsWith( "am/pm" ) )
        {
        if (hour < 12) ans += "am";
        else ans += "pm";
        p += 4;
        }
      else if (f.startsWith( "A/P" ) )
        {
        if (hour < 12) ans += "A";
        else ans += "P";
        p += 2;
        }
      else if (f.startsWith( "a/p" ) )
        {
        if (hour < 12) ans += "a";
        else ans += "p";
        p += 2;
        }
      else
        ans += f.substring( 0, 1 );
 
      if ( !f.startsWith( ":" ) && !inTime ) inTime = f.startsWith( "h" );
      ++p;
      }
    return ( ans );
    }

  public String toShortString()
    {
    if ( valid ) return ( JulianDayNumber + DELIMITER + TODSeconds );
    else return ( parseInput );
    }

  public long firstDOWinMonth( int DOW, int month )
    {
    int t[] = new int[] { 1,7,6,5,4,3,2 };
    Julian fdom = new Julian( this.year, month, 1 );
    int dom = t[fdom.getDayOfWeek()];
    return new Julian( this.year, month, dom ).getDayNumber();
    }

  public long lastDOWinMonth( int DOW, int month )
    {
    Julian ldom = new Julian( this.year, month, 1 );
    ldom.setDay( ldom.daysInMonth() );
    int delta = ldom.getDayOfWeek() - DOW;
//Console.println( "year=" + this.year + "  ldom=" + ldom.getDayOfWeek() + "  delta=" + delta );
    if ( delta < 0 ) delta = -delta;
    return ldom.getDayNumber() - delta;
    }

  public boolean isValid()
    {
//Console.println("[Julian.isValid()] checking ("+year+","+month+","+day+")" );
    return ( valid );
    }

  public static boolean isValid( int y, int m, int d )
    {
    int ymd[] = CalendarDate( DayNumber( y, m, d ) );
    return ((ymd[0] == y) && (ymd[1] == m) && (ymd[2] == d) );
    }

  public static boolean isValid( String s )
    {
    long[] t = parse( s );
    return isValid( (int)t[2], (int)t[3], (int)t[4] );
    }

  public boolean isLeapYear()
    {
    return new Julian( year, 2, 29 ).isValid();
    }

  public static boolean isLeapYear( int year )
    {
    return new Julian( year, 2, 29 ).isValid();
    }

  public int daysInMonth()
    // Returns the number of days in this month
    {
    if ( month == 2 )
      {
      if ( this.isLeapYear() ) return ( 29 );
      else return ( 28 );
      }
    if ( (month == 4) || (month == 6) ||
         (month == 9) || (month == 11) )
      return (30);
    return (31);
    }

  public void setDayNumber (long DayNumber)
    {
    if (DayNumber != JulianDayNumber)
      {
      int[] ymd = CalendarDate (DayNumber);
      year = ymd[0];
      month = ymd[1];
      day = ymd[2];
      JulianDayNumber = DayNumber;
      }
    return;
    }

  public void setYearMonthDay (int y, int m, int d)
    {
    if ((y != year) || (m != month) || (d != day))
      {
      valid = isValid( y, m, d );
      if ( !valid ) parseInput = "(" + y + "," + m + "," + d + ")";
      year = y;
      month = m;
      day = d;
      JulianDayNumber = DayNumber (y, m, d);
      }
    }

  public void setYear( int y )
    {
    if (y != year)
      {
      year = y;
      JulianDayNumber = DayNumber( year, month, day );
      reCompute();
      }
    }

  public void setMonth (int m)
    {
    if (m != month)
      {
      month = m;
      valid = isValid( year, m, day );
      if ( !valid ) parseInput = "(" + year + "," + m + "," + day + ")";
      JulianDayNumber = DayNumber( year, month, day );
      reCompute();
      }
    }

  public void setDay (int d)
    {
    if (d != day)
      {
      day = d;
      valid = isValid( year, month, d );
      if ( !valid ) parseInput = "(" + year + "," + month + "," + d + ")";
      JulianDayNumber = DayNumber( year, month, day );
      reCompute();
      }
    }

  public void setTime (int h, int m, int s)
    {
    if ((h != hour) || (m != minute) || (s != second))
      {
      hour = h;
      minute = m;
      second = s;
      TODSeconds = h * 3600 + m * 60 + s;
      reCompute();
      }
    }

  public void setTODSeconds( long TOD )
    {
    TODSeconds = TOD;
    reCompute();
    }

  public Julian addSeconds (long secondsToAdd)
    {
    TODSeconds += secondsToAdd;
    reCompute();
    return this;
    }

  public Julian addMinutes (long minutesToAdd)
    {
    addSeconds (minutesToAdd * 60L);
    return this;
    }


  public Julian addHours (long hoursToAdd)
    {
    addSeconds (hoursToAdd * 3600L);
    return this;
    }

  public Julian addDays (long daysToAdd)
    {
    JulianDayNumber += daysToAdd;
    reCompute();
    return this;
    }

  public Julian addMonths( int monthsToAdd)
  {
      int[] ymd = new int[3];
      ymd = (getYMD());
      ++ymd[1];
      if (ymd[1] > 12)
      {
          ymd[1] = ymd[1]-12;
          ++ymd[2];
      }
      else ++ymd[1];
      this.setYearMonthDay(ymd[0], ymd[1], ymd[2]);
      return this;
  }
  
  public Julian minus( Julian from )
    {
    Julian diff = new Julian( this.getDayNumber() - from.getDayNumber() );
    diff.setTODSeconds( this.getTODSeconds() - from.getTODSeconds() );
    if (diff.getTODSeconds() < 0)
      {
      diff.addDays( -1 );
      diff.addSeconds( 86400 );
      }
    return diff;
    }

  public static long elapsed( Julian start, Julian stop )
    {
      Julian diff = stop.minus( start );
      return (long)( ( diff.getDayNumber() * 86400 ) + diff.getTODSeconds() );
    }

  public int compareTo( Julian that )
    {
    if (this.JulianDayNumber < that.JulianDayNumber) return (-1);
    if (this.JulianDayNumber > that.JulianDayNumber) return (1);
    if (this.TODSeconds < that.TODSeconds) return (-1);
    if (this.TODSeconds > that.TODSeconds) return (1);
    return (0);
    }

  public boolean isEqualTo( Julian that )
    {
    return compareTo( that ) == 0;
    }
 
  public boolean isEarlierThan( Julian that )
    {
    return compareTo( that ) < 0;
    }

  public boolean isLaterThan( Julian that )
    {
    return compareTo( that ) > 0;
    }

  public boolean isSummertime()
    {
    return isSummertime( TimeZone.getDefault() );
    }

  public boolean isSummertime( TimeZone timeZone )
    {
    // m = milliseconds since January 1, 1970, 00:00:00 GMT
    Julian sillyDate = this.minus( new Julian( "1-1-1970" ) );
    long m = (sillyDate.getDayNumber() * 86400 + TODSeconds ) * 1000L;
    return timeZone.inDaylightTime( new Date( m ) );
    }

  public Julian toUTC()
    { // adjusts this instance from our TimeZone to UTC
    return toUTC( TimeZone.getDefault() );
    }

  public Julian toUTC( TimeZone timeZone )
    { // adjusts this instance from the specified TimeZone to UTC 
    int adjustment = -timeZone.getRawOffset() / 1000;
    if ( isSummertime( timeZone ) )
        adjustment += -timeZone.getDSTSavings() / 1000;
    addSeconds( (long)adjustment );
    return this;
    }

  public Julian toLocalTime()
    { // adjusts this instance from UTC to our local time
    return toLocalTime( TimeZone.getDefault() );
    }

  public Julian toLocalTime( TimeZone timeZone )
    { // adjusts this instance from UTC to local time in the specified TimeZone
    int adjustment = timeZone.getRawOffset() / 1000;
    if ( isSummertime( timeZone ) )
        adjustment += timeZone.getDSTSavings() / 1000;
    addSeconds( (long)adjustment );
    return this;
    }



                        /* PRIVATE METHODS */


  private void reCompute ()
    {
    int days = (int)(TODSeconds / 86400L);
    if (days != 0)
      {
      TODSeconds -= (days * 86400L);
      JulianDayNumber += days;
      }
    if (TODSeconds < 0)
      {
      TODSeconds += 86400L;
      JulianDayNumber -= 1;
      }
    hour = (int)(TODSeconds / 3600L);
    minute = (int)((TODSeconds - ((long)hour * 3600L)) / 60L);
    second = (int)(TODSeconds % 60);
    int[] ymd = CalendarDate (JulianDayNumber);
    year = ymd[0];
    month = ymd[1];
    day = ymd[2];
    valid = true;
    }

                        /* STATIC VARIABLES */

  private static DecimalFormat twodformat = new DecimalFormat ("00");
  private static DecimalFormat fourdformat = new DecimalFormat ("0000");
  public static final String monthAbbr[]=
    { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "???"
    };
  public static final String monthNames[] =
    { "January", "February", "March", "April", "May", "June", "July",
      "August", "September", "October", "November", "December", "???"
    };
  public static final String DOWNames[] =
    { "Sunday", "Monday", "Tuesday", "Wednesday",
      "Thursday", "Friday", "Saturday"
    };
  public static final String DOWAbbr[] =
    { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
  public static final int SUN = 0;
  public static final int MON = 1;
  public static final int TUE = 2;
  public static final int WED = 3;
  public static final int THU = 4;
  public static final int FRI = 5;
  public static final int SAT = 6;
  public static final String SQL_FORMAT = "yyyy-mm-dd";
  public static final String DELIMITER = "~";
  public static final String USA = "USA";
  public static final String DTG = "mm/dd/yyyy hhmmss";
  public static final String SORTABLE_DTG = "yyyy-mm-dd hhmmss";

                        /* INSTANCE VARIABLES */

  private long JulianDayNumber;
  private int year;
  private int month;
  private int day;
  private long TODSeconds;
  private int hour;
  private int minute;
  private int second;
  private boolean valid;
  private String parseInput;


  /***** TEST PROGRAM *****/
  public static void main( String args[] )
  throws IOException
    {
    // Options:
    // a - Calculate the number of days between two dates
    // b - Run some standard tests
    // c - Get current date & time
    // d - Parse dates input by user (this is the default option)
    Julian j;
    String[] tests =
      { "Wed Nov 03 09:52:59 PST 1999"   // java.util.Date
      , "2001-2-28"                      // SQL date
      , "06/06/1939"                     // random other formats (all valid)
      , "6-6-39"
      , " 6 6 39"
      , "Jun 6, 1939"
      , "June 6, 1939"
      , "07-09-2000 02:03:55"
      , " 2/29/99"                       // invalid
      , ""                               // filled with current date later
      };

    try
      {
      // Analyze the arguments and set the default to "/d"
      Clip clip = new Clip( args, new String[] { "/d" } );
      if ( clip.isSet( 'a' ) )
        {
        String in;
        while ( true )
          {
          in = Console.prompt( "Enter first date: " );
          if ( in.length() == 0 ) break;
          Julian j1 = new Julian( in );
          if ( !j1.isValid() )
            {
            Console.println( "Invalid date" );
            continue;
            }
          in = Console.prompt( "Enter second date: " );
          Julian j2 = new Julian( in );
          if ( !j2.isValid() )
            {
            Console.println( "Invalid date" );
            continue;
            }
          Console.println( "Number of days = " + (int)(j2.getDayNumber() - j1.getDayNumber()) );
          }
        Console.println( "Done" );
        return;
        }

      if ( clip.isSet( 'b' ) )
      {
        System.out.println( "2-24-1999 " + isValid( 1999, 2, 24 ) + " (should be true)" );
        System.out.println( "2-29-1999 " + isValid( 1999, 2, 29 ) + " (should be false)" );
        System.out.println( "2-29-2000 " + isValid( 2000, 2, 29 ) + " (should be true)" );
        System.out.println( "2-29-2100 " + isValid( 2100, 2, 29 ) + " (should be false)" );
        System.out.println( "13-10-455 " + isValid( 455, 13, 10 ) + " (should be false)" );
        tests[tests.length - 1] = new Julian().toString();
        for (int i=0; i<tests.length; ++i)
          {
          Console.prompt( tests[i] + " --> " + new Julian( tests[i] ).toString( "mm/dd/yyyy hh:mm:ss" ) );
          }
      } // end of test 'b'

      if ( clip.isSet( 'c' ) )
        {
        Julian now = new Julian();
        System.out.println( "Local Time is " + now.toString( "mm-dd-yyyy hh:mm:ss" ) );
        System.out.println( "TODSeconds is " + now.getTODSeconds() );
        Console.println( "UTC is " + now.toUTC().toString( "mm-dd-yyyy hh:mm:ss" ) );
        Console.println( "Time Zone: " + TimeZone.getDefault().getDisplayName() );
        Console.println( "summertime = " + now.isSummertime() );
        Console.println( "converting UTC back to local time -> " +
            now.toLocalTime().toString( "mm-dd-yyyy hh:mm:ss" ) );
        } // end of test 'c'

      if ( clip.isSet( 'd' ) )
      {
        String s;
        Julian jd;
        String format;


        while (true)
          {
          format = Console.prompt( "Enter the format you want to output: "
              + "(SQL DTG SORT)  " );
          if ( format.length() <= 0 ) break;
          if ( format.equalsIgnoreCase( "SQL" ) ) format = SQL_FORMAT;
          else if (format.startsWith( "DTG" ) ) format = DTG;
          else if (format.startsWith( "SORT" ) ) format = SORTABLE_DTG;
          while (true)
            {
            s = Console.prompt( "Enter a date string to parse: " );
            if (s.length() <= 0) break;
            try
              {
              j = new Julian( s );
              Console.print( "\"" + s + "\"-->" + j.toString( format ) + " valid=" + j.isValid() );
              Console.print( "  Day Number " + j.getDayNumber() );
              Console.println( "  summertime=" + j.isSummertime() );
              Console.println( "In this month, the first Sunday is " + new Julian( j.firstDOWinMonth( 0, j.getMonth() ) ).toString( "mm-dd-yy" ) +
                  " and he last Sunday is " + new Julian( j.lastDOWinMonth( 0, j.getMonth() ) ).toString( "mm-dd-yy" ) );
              Console.print( "  (this date is in the " );
              if ( j.isEarlierThan( new Julian() ) )
              {
                  Console.println( "past.)" );
              }
              else Console.println( "future.)" );
              }
            catch (Exception e)
              {
              Console.println( "Invalid date: " + s );
              }
            }
          }
        } // end of test 'd'

      Console.println( "Done" );
      }
    catch (Exception e)
      {
      e.printStackTrace();
      Console.prompt( "Error ..." );
      }
    return;
    }
  /***** END OF TEST PROGRAM *****/
  }
