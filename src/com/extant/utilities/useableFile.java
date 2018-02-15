/****************************************************************************
 * THIS CLASS IS DEPRICATED.  USE com.extant.utilities.UsefulFile
 ****************************************************************************/

package com.extant.utilities;
import java.io.*;

public class useableFile
  {
  public static final int ALL_WHITE = 1; // readLine trims all whitespace on both ends
  public static final int RIGHT_WHITE = 2; // readLine trims whitespace at end of line
  public static final int EOL = 3; // readLine trims only the end-of-line chars at end
  // (There is no option to read the line completely without trim)

  private RandomAccessFile raf=null;
  private String fileName;

  public useableFile()
    {
    raf = null;
    }

  public useableFile( String fileName )
  throws IOException
    {
    this( fileName, "rw" );
    }

  public useableFile( String fileName, String mode )
  throws IOException
  /* Note:  For a randomAccessFile, mode must be either "r" or "rw".
  ** In addition to these options, we also allow:
  **  "w"    delete the file if it exists and then open a new file with "rw".
  **  "w+"   if the file exists, append to it, else create a new file
  **  "rw+"  same as "w+"
  */
    {
    boolean append = false;

    if ( mode.equals( "r" ) || mode.equals( "rw" ) ) append = false;
    else if ( mode.equals( "w" ) )
      {
      //if ( exists( fileName ) ) delete( fileName );
      if ( !delete( fileName ) ) ; // Console.println( fileName + " not deleted!" );
      append = false;
      mode = "rw";
      }
    else if ( mode.equals( "w+" ) || mode.equals( "rw+" ) )
      {
      append = true;
      mode = "rw";
      }
    raf = new RandomAccessFile( fileName, mode );
    this.fileName = fileName;
    if ( append ) this.seekEOF();
    }

  public String read( int n )        // Reads n bytes
  throws IOException
    {
    StringBuffer sb = new StringBuffer( n );
    sb.setLength( n );
    for (int i=0; i<n; ++i)
      sb.setCharAt( i, (char)raf.readByte() );
    return ( new String( sb ) );
    }

  // Reads to newline, then trims whitespace
  public String readLine( int trimOpt )
  throws IOException
    {
    if ( trimOpt == ALL_WHITE ) return readLine();
    else if ( trimOpt == RIGHT_WHITE ) return Strings.trimRight( raf.readLine(), " \r\n\t" );
    else if ( trimOpt == EOL ) return raf.readLine();
    else return raf.readLine(); // EOL is the default
    }

  // Reads to newline, then trims all whitespace on both ends
  public String readLine()
  throws IOException
    {
    // Reads bytes until '\n' or '\r' is encountered.  The control
    // byte is discarded, and if the '\r' is followed by a '\n',
    // that is discarded also.  So you'd better end your lines with
    // one of:  '\n'   or   '\r'   or   "\r\n" (normal)
    // Note we have expanded on this to trim all whitespace and
    // control characters from both ends of the line.
    return ( raf.readLine().trim() );
    }

  public boolean print( String s )
    {
    return ( write( s ) );
    }

  public boolean write( String s )
    {
    try
      {
      raf.writeBytes( s );
      return (true);
      }
    catch (Exception e)
      {
      e.printStackTrace();
      return (false);
      }
    }

  public boolean writeLine( String s )
    {
    return ( write( s + "\r\n" ) );
    }

  public boolean writeBytes( byte[] bytes )
  throws IOException
    {
    raf.write( bytes );
    return ( true );
    }

  public void println( String s )
    {
    writeLine( s ) ;
    }

  public void close()
    {
    try
      {
      if ( raf != null )
        {
        raf.close();
        raf = null;
        }
      }
    catch (IOException iox) { /* ignore */ }
    }

  public long copyToFile( String toFileName )
  throws IOException
    {
    // This will overwrite an extant file without warning
    return this.copyToFile( toFileName, false );
    }

  public long copyToFile( String toFileName, boolean append )
  throws IOException
    {
    // if the destination file exists
    //   if append is true, this file will be appended to the extant file
    //   if append is false, this file will overwrite the extant file
    // if the destination file does not exist
    //   a new file will be created, and this file will be copied to it

    String mode;

    //if ( new File( toFileName ).exists() && !append ) new File( toFileName ).delete();
    if ( append ) mode = "w+";
    else mode = "w";
    useableFile toFile = new useableFile( toFileName, mode );
    int byteSize = 16384;
    byte[] buffer = new byte[byteSize];
    byte[] lastBuffer;
    int count;
    long totalCount = 0;
    this.rewind();
    while ( true )
      {
      count = raf.read( buffer );
      totalCount += count;
      if ( count < byteSize )
        {
        if ( count > 0 )
          {
          lastBuffer = new byte[count];
          for (int i=0; i<count; ++i) lastBuffer[i] = buffer[i];
          toFile.writeBytes( lastBuffer );
          }
        break;
        }
      toFile.writeBytes( buffer );
      }
    toFile.close();
    return totalCount;
    }

  public String getFileName()
    {
    return fileName;
    }

  public boolean EOF()
    {
    if ( raf == null ) return true;
    try { return ( raf.getFilePointer() >= raf.length() ); }
    catch (IOException iox) { return true; }
    }

  public boolean rewind()
  throws IOException
    {
    raf.seek( 0L );
    return (true);
    }

  public boolean seek( long p )
  throws IOException
    {
    raf.seek( p );
    return (true);
    }

  public boolean seekEOF()
    {
    try
      {
      return( seek( raf.length() ) );
      }
    catch (Exception e)
      {
      e.printStackTrace();
      return (false);
      }
    }

  public long length()
  throws IOException
    {
    return ( raf.length() );
    }

  public RandomAccessFile getRAF()
    {
    return (raf);
    }

  public boolean delete()
    {
    this.close();
    return delete( fileName );
    }

  // Some convenience tools
  public static boolean exists( String fileName )
    {
    return ( new File( fileName ).exists() );
    }

  public static boolean delete( String fileName )
    {
    return ( new File( fileName ).delete() );
    }

  public static String getDir( String s )
    {
    int p = s.lastIndexOf( File.separatorChar );
    if ( p < 0 ) return "";
    return s.substring( 0, p+1 );
    }

  public static String getName( String s )
    {
    int p = s.lastIndexOf( File.separatorChar );
    if ( p < 0 ) return s;
    return s.substring( p+1 );
    }

  }

