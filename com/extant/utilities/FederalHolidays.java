/*
 * FederalHolidays.java
 *
 * Created on January 18, 2004, 2:05 AM
 */

package com.extant.utilities;
import java.util.Vector;

/**
 *
 * @author  jms
 */
public class FederalHolidays
{
    // See http://www.opm.gov/fedhol/
    public static final String sHolidays[] = new String[]
        { "1-1-2002"
        , "1-21-2002"
        , "2-18-2002"
        , "5-27-2002"
        , "7-4-2002"
        , "9-2-2002"
        , "10-14-2002"
        , "11-11-2002"
        , "11-28-2002"
        , "12-25-2002"
        , "1-1-2003"
        , "1-20-2003"
        , "2-17-2003"
        , "5-26-2003"
        , "7-4-2003"
        , "9-1-2003"
        , "10-13-2003"
        , "11-11-2003"
        , "11-27-2003"
        , "12-25-2003"
        , "1-1-2004"
        , "1-19-2004"
        , "2-16-2004"
        , "4-09-2004" // Markets closed (Good Friday)
        , "5-31-2004"
        , "6-11-2004" // Closed - Reagan's death
        , "7-5-2004"
        , "9-6-2004"
//      , "10-11-2004" Columbus Day - Markets open
        , "11-11-2004"
        , "11-25-2004"
        , "12-24-2004"
        , "12-31-2004"
        , "1-17-2005"
        , "2-21-2005"
//      , "4-25-2005" //  Good Friday ?
        , "5-30-2005"
        , "7-4-2005"
        , "9-5-2005"
        , "10-10-2005"
        , "11-11-2005"
        , "11-24-2005"
        , "12-26-2005"
        , "1-16-2006"
        , "1-2-2006"
        , "1-16-2006"
        , "2-20-2006"
        , "5-29-2006"
        , "7-4-2006"
        , "9-4-2006"
        , "10-9-2006"
        , "11-10-2006"
        , "11-23-2006"
        , "12-25-2006"
        , "1-1-2007"
        , "1-15-2007"
        , "2-19-2007"
        , "5-28-2007"
        , "7-4-2007"
        , "9-3-2007"
        , "10-8-2007"
        , "11-12-2007"
        , "11-22-2007"
        , "12-25-2007"
        , "1-1-2008"
        , "1-21-2008"
        , "2-18-2008"
        , "5-26-2008"
        , "7-4-2008"
        , "9-1-2008"
        , "10-13-2008"
        , "11-11-2008"
        , "11-27-2008"
        , "12-25-2008"
        , "1-1-2009"
        , "1-19-2009"
        , "2-16-2009"
        , "5-25-2009"
        , "7-3-2009"
        , "9-7-2009"
        , "10-12-2009"
        , "11-11-2009"
        , "11-26-2009"
        , "12-25-2009"
        , "1-1-2010"
        , "1-18-2010"
        , "2-15-2010"
        , "5-31-2010"
        , "7-5-2010"
        , "9-6-2010"
        , "10-11-2010"
        , "11-11-2010"
        , "11-25-2010"
        , "12-24-2010"
        };
    private static long iHolidays[];

    public FederalHolidays()
    {
        init();
    }

    private static void init()
    {
        iHolidays = new long[sHolidays.length];
        for (int i=0; i<sHolidays.length; ++i)
            iHolidays[i] = new Julian( sHolidays[i] ).getDayNumber();
    }

    public static boolean isHoliday( Julian date )
    {
        if ( iHolidays == null ) init();
        long dayNo = date.getDayNumber();
        for (int i=0; i<iHolidays.length; ++i)
            if ( iHolidays[i] == dayNo ) return true;
        return false;
    }

    public static boolean isMarketDay( Julian date )
    {
        int iDOW = date.getDayOfWeek();
        return ( (iDOW != Julian.SAT ) && (iDOW != Julian.SUN) && (!isHoliday( date ) ) );
    }

    /***** FOR TESTING *****
    public static void main( String args[] )
    {
        //FederalHolidays federalHolidays = new FederalHolidays();
        for (int i=1; i<=31; ++i)
        {
            Julian date = new Julian( "1-" + i + "-2004" );
            Console.println( date.toString( "mm-dd-yy" ) + ": " +
                "Holiday=" + FederalHolidays.isHoliday( date ) +
                "  MarketDay=" + isMarketDay( date ) );
        }
    }
    /*****/
}

