package com.extant.utilities;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class LogFile
  {
  private String logFileName;
  private UsefulFile uf = null;
  private boolean specificLevels[] = new boolean[100]; // only levels 0-99 can be specified
  // The following statements set default values:
  private int logLevel = NORMAL_LOG_LEVEL;
  private boolean logToFile = true;
  private boolean logToConsole = true;
  private boolean timestampConsoleLog = false;
  public final static String timestampFormat = "yyyy-mm-dd hh:mm:ss";
  public boolean logAll=false;
  
  // Use this constructor when you want to log to the Console only
  public LogFile()
    {
    logToFile = false;
    }

  public LogFile( String fileName )
    {
    this( fileName, true );
    }

  public LogFile( String fileName, boolean append )
    {
    String mode = "w";
    if ( append ) mode = "w+";
    logFileName = fileName;
    try { uf = new UsefulFile( logFileName, mode ); }
    catch (IOException iox)
      { // We force a valid log file to avoid throwing an exception here
      File temp;
      try { temp = File.createTempFile( "Log", ".log" ); }
      catch (IOException iox2)
      {
        Console.println( "[LogFile] Unable to use " + fileName + 
          " and unable to createTempfile() ... aborting." );
        return;
      }
      logFileName = temp.getAbsolutePath();
      Console.println( "[LogFile] Unable to use " + fileName + ". " +
        "Created " + logFileName + " for this LogFile." );
      try { uf = new UsefulFile( logFileName, "w" ); }
      catch (IOException iox3)
        {
        Console.println( "That didn't work either! ... giving up." );
        uf = null;
        logToFile = false;
        return;
        }
      }
    logToFile = true;
    for (int i=0; i<specificLevels.length; ++i )
        specificLevels[i] = false;
    }
  
  public void log(int level, String msg)
    {
    if ( level < 0 ) level = 0;
    boolean logThis = (level <= logLevel) || logAll;
    for (int i=0; i<specificLevels.length; ++i)
        logThis |= specificLevels[level];
    if ( logThis )
      {
      String logEntry = new Julian().toString( timestampFormat ) +
        " [" + Strings.colFormat( level, 2 ) + "] " + msg;
      if ( logToFile )
          uf.writeLine( logEntry );
      if ( logToConsole )
        {
        if ( timestampConsoleLog ) Console.println( logEntry );
        else Console.println( msg );
        }
      }
    }

  // This will log a message at the highest priority
  public void log( String msg )
    {
        log( 0, msg );
    }

  // This will log a message at the DEBUG level
  public void logDebug( String msg )
    {
        log( DEBUG, msg );
    }

  public void logInfo( String msg )
    {
      log( MAJOR_INFO, msg ); 
    }

  public void whereAreWe(int opt, Throwable x)
    {
        int stackSize;
        StackTraceElement stackTraceElements[] = x.getStackTrace();
        if (opt == 0) stackSize = stackTraceElements.length;
        else stackSize = opt;
        for (int i=0; i<stackSize; ++i)
            log("   " + stackTraceElements[i].toString());
    }
  
  public void whereAreWe(Throwable x)
  {
      whereAreWe(2, x);
  }
  
  /* I finally gave up on trying to make this compile and execute correctly
   * under NetBeans.  (See the method fatalError(msg) in VLUtil.)
   * Unfortunaately, this requires modification of all calls to the original
   * logFatal(msg, new Error()
   */

//  public void logFatal( String msg, Throwable x)
//    {
//    System.out.println("logFatal: " + msg);
//    log("logFatal: " + msg);
//    whereAreWe(0, new Error());
//    System.exit(1);
//    }

  public void logFatal( String msg)
  {
      System.out.println("logFatal: "+ msg);
      log(msg);
      whereAreWe(new Error());
      System.exit(1);
  }
  
  public void setLogAll(boolean yes)
  {
      whereAreWe(new Error());
      log("setting logAll=" + yes);
      logAll = yes;
  }
  
  public void setLogLevel( int newLevel )
    { // Sets the log threshold (maximum level of messages to be logged)
      // (messages with level <= newLevel will be logged)
        int oldLevel = logLevel;
        if (newLevel == oldLevel) return;
        if ( newLevel < 0 ) logLevel = 0;
        else logLevel = newLevel;
        log("logLevel changed from " + oldLevel + " to " + newLevel);
        whereAreWe(2, new Error());
    }

  public int getLogLevel()
  {
      return ( logLevel );
  }

  public boolean isLoggingDebug() 
  {
      return logLevel >= DEBUG || logAll;
  }

  public void setSpecificLogLevels( String levelString, boolean on )
  {   // Turns on/off a specific level
      // Expects a CSV list like 1,2,3,4
      if ( levelString == null || levelString.length() == 0 ) return;
      StringTokenizer st = new StringTokenizer( levelString, "," );
      while ( st.hasMoreElements() )
        specificLevels[ Integer.parseInt( (String)st.nextElement() )] = on;
    }

  public void setSpecificLogLevels( String levelString )
    {
    setSpecificLogLevels( levelString, true );
    }

  public void clearSpecificLogLevels( String levelString )
    {
    setSpecificLogLevels( levelString, false );
    }

  public void setSpecificLogLevel( int level, boolean on )
    { // Turns on (or off) logging of one specific level
    specificLevels[level] = on;
    }

  public boolean getSpecificLogLevel( int level )
    {
    return specificLevels[level];
    }

  public void startLogging()
    {
    if ( uf != null ) setLogToFile( true );
    setLogToConsole( true );
    }

  public void stopLogging()
    {
    setLogToFile( false );
    setLogToConsole( false );
    }

  public void setLogToFile( boolean on )
    { // Turns on/off logging to the log file
    logToFile = on && (uf != null);
    }

  public void setLogToConsole( boolean on )
  { // Turns on/off logging to the Console
      logToConsole = on;
  }

  public void setTimestampConsoleLog( boolean on )
  { // Turns on/off timestamping of Console log entries
      timestampConsoleLog = on;
  }

  public String getFileName()
  {
      return logFileName;
  }

  public String readLine()
  throws IOException
  {
      return uf.readLine();
  }

  public String readLine( int trimOpt )
  throws IOException
  {
      return uf.readLine( trimOpt );
  }

  public boolean EOF()
  {
      return uf.EOF();
  }

  public void close()
  {
      uf.close();
  }

  public static final int FATAL_ERROR = 0;
  public static final int APP_ENTRY_EXIT = 10;
  public static final int MAJOR_TRACE = 11;
  public static final int MAJOR_INFO = 11;
  public static final int NORMAL_LOG_LEVEL = 19;  // lower numbers are higher priority
  public static final int METHOD_ENTRY_EXIT = 20;
  public static final int MINOR_TRACE = 21;
  public static final int MINOR_INFO = 21;
  public static final int SQL_COMMANDS = 90;
  public static final int DEBUG = 91;
  public static final int DEBUG_LOG_LEVEL = 99;
}

  /* Conventions for assigning logLevels:
   *  0 -  9:  Fatal errors; Errors which interrupt processing (Exceptions)
   * 10 - 19:  Application entry & exit messages; Major Info; Major trace points; 
   * ------------- Normal Log Level -------------
   * 20 - 29:  Minor trace points; Minor Info
   * 30 - 39:  
   * 40 - 49:
   * 50 - 59:
   * 60 - 69:
   * 70 - 79:
   * 80 - 89:
   * 90:       SQL Commands
   * * ------------- Debug Log Level -------------
   * 91 - 99:  Debug output
   *
   * Note that levels > 99 can be used, but they cannot be turned on and off
   * as specific levels.
   *
   * Production applications should normally run with logLevel = NORMAL_LOG_LEVEL.
   * For debugging, use DEBUG_LOG_LEVEL.
   *
   * An application which makes a log file should make a log entry at the start
   * of execution at logLevel APP_ENTRY_EXIT in the following form:
   *    [<class-name>] <other useful information>
   * If the log might be analyzed by a program, some attention should be given to
   * ease of parsing the entries, but the primary consideration is to make
   * log entries easily readable by people.
   */

