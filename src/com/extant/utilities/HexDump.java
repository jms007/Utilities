/*
 * HexDump.java
 *
 * Created on April 29, 2005, 10:31 AM
 */

package com.extant.utilities;
import java.io.IOException;

/**
 *
 * @author jms
 */
public class HexDump
{
    public HexDump( UsefulFile inFile )
    throws IOException
    {
        while ( !inFile.EOF() )
        {
            String line = inFile.read( 16 );
            if ( showASCII ) Console.println( Strings.toHexString( line ) + "   '" + line + "'" );
            else Console.println( Strings.toHexString( line ) );
        }
    }

    public HexDump( UsefulFile inFile, UsefulFile outFile )
    throws IOException
    {
        while ( !inFile.EOF() )
        {
            String line = inFile.read( 16 );
            //byte bytes[] = line.getBytes();
            if ( showASCII ) outFile.println( Strings.toHexString( line ) + "  " + line );
            else outFile.println( Strings.toHexString( line ) );
        }
    }

    public void showASCII( boolean showASCII )
    {
        this.showASCII = showASCII;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        UsefulFile inFile = null;
        UsefulFile outFile = null;
        try
        {
            Clip clip = new Clip( args, new String[] { "i=C:\\Downloads\\testBack.txt" } );
            if ( !clip.isSet( "i" ) )
            {
                Console.println( "Use: HexDump i=<inFilename> [o=<outFilemame>]" );
                System.exit( 1 );
            }
            inFile = new UsefulFile( clip.getParam( "i" ), "r" );
            if ( clip.isSet( "o" ) )
            {
                outFile = new UsefulFile( clip.getParam( "o" ), "w" );
                HexDump hexDump = new HexDump( inFile, outFile );
            }
            else
            {
                HexDump hexDump = new HexDump( inFile );
            }
        }
        catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
        catch (IOException iox) { Console.println( iox.getMessage() ); }
        if ( inFile != null ) inFile.close();
        if ( outFile != null ) outFile.close();
        System.exit( 0 );
    }
 
    boolean showASCII = false;
}

