package com.extant.utilities;

import java.io.*;
import java.util.*;

public class LogEntries implements Enumeration
  {
  private LogFile lf;
  private Julian startAt;
  private Julian stopAt;
  private String nextEntry = null;
  private Julian nextDTG = null;
 
  public LogEntries(String fileName) throws IOException
    {
    lf = new LogFile( fileName, false );
    startAt = stopAt = null;
    }


  public LogEntries(String fileName, Julian start) throws IOException
    {
    lf = new LogFile( fileName, false );
    startAt = start;
    stopAt = null;
    }


  public LogEntries(String fileName, Julian start, Julian stop) throws IOException
    {
    lf = new LogFile( fileName, false );
    startAt = start;
    stopAt = stop;
    }
 
  public boolean hasMoreElements()
    {
    boolean ans;
 
    if (nextEntry == null)
      {
      if (lf.EOF())
        {
        //lf.close();
        return (false);
        }
      nextEntry = getNextEntry( lf );
      nextDTG = new Julian( nextEntry );
      if (startAt != null)
        {
        while ( nextDTG.compareTo( startAt ) < 0 )
          {
          if (lf.EOF())
            {
            nextEntry = null;
            //lf.close();
            return (false);
            }
          nextEntry = getNextEntry( lf );
          nextDTG = new Julian( nextEntry );
          }
        }
      }
    if ( stopAt == null ) ans = nextEntry != null;
    else ans = nextDTG.compareTo( stopAt ) <= 0;
    //if ( !ans ) lf.close();
    return ( ans );
    }
 
  public Object nextElement()
  throws NoSuchElementException
    {
    if (nextEntry == null)
      throw new NoSuchElementException( "LogEntries" );
    String ans = new String( nextEntry );
    if (lf.EOF()) nextEntry = null;
    else
      {
      nextEntry = getNextEntry( lf );
      nextDTG = new Julian( nextEntry );
      }
    return ( (Object)ans );
    }

  private String getNextEntry( LogFile lf )
  {
      try
      {
        return lf.readLine( UsefulFile.ALL_WHITE );
      }
      catch (IOException iox)
      {
          return "";
      }
  }
      
/***** FOR TESTING *****
  public static void main( String args[] )
    {
    String logFileName = "C:\\Projects\\Stock\\data\\ClosingQuotes\\TestLog.txt";
    try
      {
      LogFile r;
      r = new LogFile( logFileName );
      // Add a new entry to the log file
      r.log( "This is a new test!" );
      r.close();
      r = new LogFile( logFileName, false );

//System.out.println( "[LogFile.main] r = " + r.toString() );
//System.out.println( "            name = " + uf.getFileName() );
//System.out.println( "             raf = " + uf.getRAF() );
//try { System.out.println( "uf.EOF()    = " + uf.EOF() );
//      System.out.println( "uf.length() = " + uf.length() );
//      System.out.println( " r.EOF()    = " +  r.EOF() );
//      System.out.println( " r.length() = " +  r.length() );
//    }
//catch (Exception e) {
//  e.printStackTrace();
//  Console.prompt( "Error exit ..." );
//  return;

      System.out.println( "Simply list the file:" );
      while ( !r.EOF() )
        {
        System.out.println( "read: '" + r.getLogEntry().trim() + "'" );
        }
      r.close();

      System.out.println( "Show all using enumeration:" );
      logEntries le = new logEntries( logFileName );
      while ( le.hasMoreElements() )
        System.out.println( le.nextElement() );

      System.out.println( "Show early entries:" );
      le = new logEntries( logFileName, null,
        new Julian( "Wed Nov 03 14:00:00 PST 1999" ) );
      while ( le.hasMoreElements() )
        System.out.println( le.nextElement() );

      System.out.println( "Show late entries:" );
      le = new logEntries( logFileName,
        new Julian( "Wed Nov 03 14:00:00 PST 1999" ) );
      while ( le.hasMoreElements() )
        System.out.println( le.nextElement() );
      }
    catch (Exception e)
      {
      Console.println( e.getMessage() );
      e.printStackTrace();
      Console.prompt( "Done..." );
      }
    Console.prompt( "Done ..." );
    }
/*****/

  }



