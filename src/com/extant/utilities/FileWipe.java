package com.extant.utilities;
import java.io.*;
import java.util.Random;

/******************************************************************************
 * THIS CLASS IS OBSOLETE AS AN APPLICATION (10-10-02)
 * If you want to wipe files, use utilities.WipeFiles (which calls this class)
 * (See utilities.WipeFiles)
 ******************************************************************************/

/* This class overwrites a file with random characters.
 *   Use:  FileWipe f=<fileName> [p=<number of passes>]
 *     p defaults to 10.
 *
 * On each pass, a random byte is chosen, and this value is written into
 * all of the bytes of the file.
 */

public class FileWipe
  {
  String fileName;
  UsefulFile file;
  int nPasses = 10;
  long origLength, finalLength;

  public FileWipe( String fileName, int nPasses )
  throws UtilitiesException, IOException
    {
    this.fileName = fileName;
    this.nPasses = nPasses;
    if ( fileName == null )
      throw new UtilitiesException( UtilitiesException.NOFILE, "f" );
    if ( !UsefulFile.exists( fileName ) )
      throw new UtilitiesException( UtilitiesException.FILENOTFOUND, fileName );
    file = new UsefulFile( fileName, "rw" );
    wipe();
    }

  public long wipe()
  throws IOException, UtilitiesException
    {
    origLength = file.length();
    Random random = new Random( System.currentTimeMillis() );
    for (int nPass=0; nPass<nPasses; ++nPass)
      {
      file.seek( 0 );
      byte[] wipeByte = new byte[1];
      wipeByte[0] = (byte)(random.nextInt() & 0xFF);
      //Console.println( "wiping with " + (char)wipeByte[0] + " ..." );
      for ( int p=0; p<origLength; ++p ) file.writeBytes( wipeByte );
      }

    finalLength = file.length();
    file.close();
    if ( finalLength != origLength )
      throw new UtilitiesException( UtilitiesException.INTERNAL_ERROR,
        "[FileWipe] File Length has changed (" + origLength + " --> " + finalLength + ")." );
    return finalLength;
    }

  public void setNPasses( int n )
    {
    nPasses = n;
    }

  public int getNPasses()
    {
    return nPasses;
    }

  public String report()
    {
    String r = "[FileWipe] " + Strings.plurals( "pass", nPasses ) +
      " completed on '" + fileName + "' of length " + finalLength + " bytes.";
    return r;
    }

  }

