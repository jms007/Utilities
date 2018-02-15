package com.extant.utilities;
import java.io.File;

// Compares the actual length of an encrypted file with the expected
// length calculated from the length of the plaintext file.

public class CheckLength
  {
  String ptFileName;
  String ezFileName;
  long ptLength;
  long ezLength;
  long calcLength;
  boolean error = false;
  String report = "";

  public CheckLength( String[] args )
    {
    try
    {
        Clip clip = new Clip( args );
        ptFileName = clip.getParam( "pt" );
        ezFileName = clip.getParam( "ez" );
        if ( ezFileName == null ) ezFileName = clip.getParam( "ze" );
        if ( ptFileName == null || ezFileName == null )
        {
            Console.println( "Use: CheckLength pt=<plaintext filename> ez=<encrypted file name>" );
            error = true;
            return;
        }
        File ptFile = new File( ptFileName );
        File ezFile = new File( ezFileName );
        if ( !ptFile.exists() )
        {
            report += "File '" + ptFileName + "' was not found.\n";
            error = true;
        }
        if ( !ezFile.exists() )
        {
            report += "File '" + ezFileName + "' was not found.\n";
            error = true;
        }
        if ( error ) return;
        ptLength = ptFile.length();
        ezLength = ezFile.length();

        calcLength = ( Blowfish.magic.length * 2 )+ ptLength;
        if ( (ptLength & 7) != 0 ) calcLength += 8 - (ptLength & 7);
    }
    catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
    }

  public String getReport()
    {
    if ( error ) return report;
    if ( ezLength == calcLength )
      report = ( "GOOD  (" + ezLength + " bytes)" );
    else
      report = ( "NO GOOD  ezLength=" + ezLength
        + " calcLength=" + calcLength +
        " (" + (calcLength-ezLength) + " bytes difference)" );
    return report;
    }

  public boolean isGood()
    {
    return ezLength == calcLength;
    }

  public long getEzLength()
    {
    return ezLength;
    }

  public long getCalcLength()
    {
    return calcLength;
    }

  public static void main(String[] args)
    {
    CheckLength checkLength = new CheckLength( args );
    Console.println( checkLength.getReport() );
    }

  }

