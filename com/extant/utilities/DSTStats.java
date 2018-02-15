/*
 * DSTStats.java
 *
 * Created on June 29, 2004, 4:05 PM
 */

package com.extant.utilities;
import java.lang.NumberFormatException;

/**
 *
 * @author  jms
 */
public class DSTStats
{
    public DSTStats( int y1, int y2, boolean detail )
    {
        this.detail = detail;
        int tSummer = 0;
        int tWinter = 0;
        for (int y=y1; y<=y2; ++y)
        {
            output = "Summer: ";
            int nSummer = summerDays( y );
            output += Strings.format( nSummer, " ###" );
            output += "  Winter: ";
            int nWinter = winterDays( y );
            output += Strings.format( nWinter, " ###" );
            Console.println( output );
            tSummer += nSummer;
            tWinter += nWinter;
        }
        Console.println( "Total Summer Days = " + tSummer );
        Console.println( "Total Winter Days = " + tWinter );
    }

    public int summerDays( int year )
    {
        if ( detail )
            output += "(" + new Julian( beginSummer( year ) ).toString( "mm-dd-yyyy" ) + " - "
                + new Julian( beginWinter( year ) - 1 ).toString( "mm-dd-yyyy" ) + ")";
        return (int)( beginWinter( year ) - beginSummer( year ) );
    }

    public int winterDays( int year )
    {   // Returns the number of winter (non-DST) days for the winter beginning
        // in the specified year
        if ( detail )
            output += "(" + new Julian( beginWinter( year ) ).toString( "mm-dd-yyyy" ) + " - "
                + new Julian( beginSummer( year + 1 ) - 1).toString( "mm-dd-yyyy" ) + ")";
        return (int)( beginSummer( year + 1 ) - beginWinter( year ) );
    }

    public long beginSummer( int year )
    {   // Summertime begins on the first Sunday in April
        Julian date = new Julian( year, 4, 1 );
        return date.firstDOWinMonth( Julian.SUN, 4 );
    }

    public long beginWinter( int year )
    {   // Wintertime begins on the last Sunday in October
        Julian date = new Julian( year, 10, 1 );
        return date.lastDOWinMonth( Julian.SUN, 10 );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            Clip clip = new Clip( args, new String[]
            { "y1=1950"
            , "y2=2150"
            , "/d"
            } );
            int y1 = Integer.parseInt( clip.getParam( "y1" ) );
            int y2 = Integer.parseInt( clip.getParam( "y2" ) );
            new DSTStats( y1, y2, clip.isSet( 'd' ) );
        }
        catch (UtilitiesException ux)
        {
            Console.println ( ux.getMessage() );
        }
        catch (NumberFormatException nfx)
        {
            Console.println( nfx.getMessage() );
        }
    }

    boolean detail;
    String output;
}

