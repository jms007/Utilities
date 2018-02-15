/*
 * Clip.java
 *
 * Created on October 5, 2002, 6:49 AM
 * This is a revision of CmdLineParam
 * Major Revision 10-8-02 to use ParseWithQuotes
 */

package com.extant.utilities;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.JFrame;

/* Provides convenient methods to access command-line parameters.
 * Supports both positional and named ("x=y") parameters;
 * Supports single-character switches of the form "-xyz" ( or "/xyz" )
 *   (meaning that the three switches x, y, and z are to be set).
 * Any combination of these can appear in any order on the same command line.
 *
 * NOTE:  There is a problem here with parameters which contain imbedded
 * blanks (such as Windows paths).  Such values get parsed into
 * multiple parameters, even if they are enclosed in double-quotes on the
 * command line.
 * We have attempted to solve this problem by re-building the command line,
 * parsing first with token <single-quote> THEN with <space>.
 * So if you have a parameter value with embedded spaces,
 * enclose the value in single-quotes.  For example:
 *     path='Program Files'
 *
 * Methods for obtaining parameter values:
 *   Named parameters are returned by the method getParam( String ).
 *   The existence of a named parameter is returned by isSet( String ).
 *   Positional parameters are returned by getParam( int ).
 *   Switch settings are returned by isSet( char )
 *
 * Also recognizes common forms of help requests.  If the first
 * parameter is any of the following forms, the method isHelpRequest()
 * returns true:
 *    ?   -?   /?   -h   /h   -help   /help
 * but the parameters nevertheless are treated as described above.
 *   (For example -help will set the 4 switches e, h, l, & p.)
 *
 * Examples:
 *   Command line = "SomeProgram first x=y third -last"
 *     getParam( 0 ) returns "first"
 *     getParam( 1 ) returns "x=y"
 *     getParam( "x" ) returns "y"
 *     isSet( "x" ) returns true
 *     isSet( 'x' ) returns false
 *     getParam( 2 ) returns "third"
 *     isSet( 'l' ) returns true
 *     isSet( 't' ) returns true
 *     getParam( "option" ) returns null
 *     getParam( 4 ) returns null
 *   Command line = "SomeProgram -help second x=y last"
 *     getParam( 0 ) returns "-help"
 *     isHelpRequest() returns true
 *     isSet( 'p' ) returns true
 */

public class Clip
  {
  //LogFile logger;
  Vector <String> vCL;
  Vector <String> vDL;
  String[] args;
  String[] defaults;
  Hashtable hashtable;
  String switches = "";
  int nParams=0;
  public final static String REQUIRED = "#REQUIRED#";

  public Clip( String[] argsIn )
  throws UtilitiesException
    {
    this ( argsIn, null );
    }

  public Clip( String[] clargs, String[] defaultArgs )
  throws UtilitiesException
    {
    //logger = new LogFile();
    //logger.setLogLevel( 19 );
    vCL = new Vector <String> (10, 10);
    vDL = new Vector <String> (10, 10);

    // Put the command line (and the default arguments) back into cl-form
    String cl = "";
    String dl = "";
    if ( clargs != null )
      for (int i=0; i<clargs.length; ++i) cl += clargs[i] + " ";
    if ( defaultArgs != null )
      for (int i=0; i<defaultArgs.length; ++i) dl += defaultArgs[i] + " ";
    // Now use ParseWithQuotes to get the real parameters
    ParseWithQuotes pwq = new ParseWithQuotes( cl, ' ', '\'' );
    String logMsg = "cl:";
    String e;
    while ( pwq.hasMoreElements() )
      {
      e = (String)pwq.nextElement();
      logMsg += "   '" + e + "'";
      vCL.addElement( e );
      }
    //logger.log( logMsg );
    pwq = new ParseWithQuotes( dl, ' ', '\'' );
    logMsg = "dl:";
    while ( pwq.hasMoreElements() )
      {
      e = (String)pwq.nextElement();
      logMsg += "   '" + e + "'";
      vDL.addElement( e );
      }
    //logger.log( logMsg );
    // Now make the String[]'s
    args = new String[vCL.size()];
    for (int i=0; i<vCL.size(); ++i) args[i] = (String)vCL.elementAt( i );
    defaults = new String[vDL.size()];
    for (int i=0; i<vDL.size(); ++i) defaults[i] = (String)vDL.elementAt( i );
    // Parse all arguments -- defaults first, then actual
    hashtable = new Hashtable( vCL.size() + vDL.size() );
    parseArgs( defaults, hashtable );
    parseArgs( args, hashtable );
    // Now prompt for any required parameters which were not supplied
    getRequiredArgs( hashtable );
    }

  void parseArgs( String[] args, Hashtable hashtable )
    {
    String name;
    String value;
    for (int i=0; i<args.length; ++i)
      {
      if ( args[i] == null ) continue;
      if ( args[i].indexOf( "=" ) > 0 )
        { // If this param is of the form 'name=value' it is
          // added to the named parameter table.  Otherwise, it
          // is retrievable only as a positional parameter.
          // For example the parameter "name=" will not be added
          // to the named parameter table.
          // ( If you want an empty string, use "name=''" )
        StringTokenizer st = new StringTokenizer( args[i], "=" );
        if ( st.hasMoreElements() ) name = (String)st.nextElement();
        else continue;
        if ( st.hasMoreElements() )
          value = Strings.trim( (String)st.nextElement(), "'" );
        else continue;
        //logger.log( logger.DEBUG, "Clip putting " + name + "=" + value + " in hashtable." );
        hashtable.put( name, value );
        }
      else if ( args[i].startsWith( "-" ) || args[i].startsWith( "/" ) )
        { // Treat as a list of switches
        switches += args[i].substring( 1 );
        }
      }
    }

  private void getRequiredArgs( Hashtable hashtable )
  throws UtilitiesException
  {
      jFrame = new JFrame();
      MsgBox msgBox = null;
      Enumeration args = hashtable.keys();
      while ( args.hasMoreElements() )
      {
          String name = (String)args.nextElement();
          String value = (String)hashtable.get( name );
//Console.println( "[getRequiredArgs] " + name + "=" + value );
          if ( value == null ) continue;
          if ( !value.equals( REQUIRED ) ) continue;
          msgBox = new MsgBox( jFrame, "Required Parameter",
            "Enter value for " + name + ":", "", new String[] { "OK", "Cancel" } );
          if ( !msgBox.getCommand().equals( "OK" ) )
          {
              jFrame.dispose();
              throw new UtilitiesException( UtilitiesException.REQD_PARAM_MISSING, name );
          }
          hashtable.put( name, msgBox.getResponse() );
      }
      if ( msgBox != null ) msgBox.dispose();
      jFrame.dispose();
  }

//  public void setParam( String name, String value )
//  throws UtilitiesException
//  {
//    //!! May need to expand args[] here
//    throw new UtilitiesException( UtilitiesException.NOT_IMPLEMENTED, "CmdLineParam.setParam()" );
//  }
//
  public int getParamCount()
    {
    if ( args.length > defaults.length ) return args.length;
    else return defaults.length;
    }

  public boolean isHelpRequest()
    {
    if ( args.length < 1 ) return false;
    return
      (  getParam( 0 ).equals( "?" )
      || getParam( 0 ).equals( "-?" )
      || getParam( 0 ).equals( "/?" )
      || getParam( 0 ).equals( "-h" )
      || getParam( 0 ).equals( "-help" )
      || getParam( 0 ).equals( "/help" )
      || getParam( 0 ).equals( "/h" )
      );
    }

  public String getParam( int index )
    { // Returns null if neither the actuals nor the defaults has such an element
    if ( index < args.length ) return args[index];
    if ( index < defaults.length ) return defaults[index];
    return null;
    }

  public String getParam( String paramName )
    { // Returns null if paramName is not defined
    return (String)hashtable.get( paramName );
    }

  public boolean isSet( String paramName )
    {
    //logger.log( logger.DEBUG, "[isSet] " + paramName + " = " + (hashtable.get( paramName ) != null) );
    return hashtable.get( paramName ) != null;
    }

  public boolean isSet( char x )
    {
    return ( switches.indexOf( x ) >= 0 );
    }

// These are ambiguous/useless, so they are going away
// It would be better to have getActual() & getDefault(), but I'm not sure
// if they would be useful either.

//  public String getAll()
//    {
//    // Returns the parameters in command-line form (including defaults)
//    String a = "";
//    for (int i=0; i<defaults.length; ++i) a += defaults[i] + " ";
//    for (int i=0; i<args.length; ++i) a += args[i] + " ";
//    return a.trim();
//    }
//
//  public String[] getArgs()
//   {
//   // Returns these arguments in a string array (including defaults)
//   }
//
  /***** TEST PROGRAM *****/
  static String print( String[] array )
    {
        String ans = "";
        for (int i=0; i<array.length; ++i) ans += array[i] + " ";
        return ans;
    }

  public static void main( String[] args )
    {
      try
      {
        logger = new LogFile(); // Un-comment logger calls
        Console.println( "Testing Command Line = SomeProgram first x='imbedded space' third -last" );
        String[] testArgs = new String[5];
        testArgs[0] = "first";
        testArgs[1] = "x='imbedded";
        testArgs[2] = "space'";
        testArgs[3] = "third";
        testArgs[4] = "-last";
        Clip clip = new Clip( testArgs );
        Console.println( "  getParam( 0 ) returns " + clip.getParam( 0 ) );
        Console.println( "  getParam( \"x\" ) returns " + clip.getParam( "x" ) );
        Console.println( "  getParam( 1 ) returns " + clip.getParam( 1 ) );
        Console.println( "  getParam( 2 ) returns " + clip.getParam( 2 ) );
        Console.println( "  isSet( 'l' ) returns " + clip.isSet( 'l' ) );
        Console.println( "  isSet( 't' ) returns " + clip.isSet( 't' ) );
        Console.println( "  isSet( 'x' ) returns " + clip.isSet( 'x' ) );
        Console.println( "  isSet( \"x\" ) returns " + clip.isSet( "x" ) );
        Console.println( "  getParam( \"option\" ) returns " + clip.getParam( "option" ) );
        Console.println( "  getParam( 4 ) returns " + clip.getParam( 4 ) );
        Console.println( " No of parameters: " + clip.getParamCount() );
        
        Console.println( "Testing Command Line = SomeProgram -help second x=y last" );
        testArgs = new String[4];
        testArgs[0] = "-help";
        testArgs[1] = "second";
        testArgs[2] = "x=y";
        testArgs[3] = "last";
        clip = new Clip( testArgs );
        Console.println( "  getParam( 0 ) returns " + clip.getParam( 0 ) );
        Console.println( "  isHelpRequest() returns " + clip.isHelpRequest() );
        Console.println( "  isSet( 'p' ) returns " + clip.isSet( 'p' ) );
        Console.println( " No of parameters: " + clip.getParamCount() );

        Console.print  ( "Testing Default Parameters: " );
        Console.println( "{x=defaultX y='default Y value' -z}" );
        clip = new Clip( testArgs, new String[] {"x=defaultX", "y='default", "Y", "value'", "-z"} );
        Console.println( "  getParam( 0 ) returns " + clip.getParam( 0 ) );
        Console.println( "  isHelpRequest() returns " + clip.isHelpRequest() );
        Console.println( "  isSet( 'p' ) returns " + clip.isSet( 'p' ) );
        Console.println( "  getParam( \"x\" ) returns " + clip.getParam( "x" ) );
        Console.println( "  getParam( \"y\" ) returns " + clip.getParam( "y" ) );
        Console.println( "  isSet( 'z' ) returns " + clip.isSet( 'z' ) );
        Console.println( " No of parameters: " + clip.getParamCount() );

        Console.print  ( "Testing zero-length parameters: " );
        Console.println( "db=test u=user p=''" );
        testArgs = new String[] { "db=test", "u=user", "p=''" };
        clip = new Clip( testArgs, new String[] { "x=''" } );
        Console.println( "  getParam(0) returns " + clip.getParam( 0 ) );
        Console.println( "  getParam(3) returns " + clip.getParam( 3 ) );
        Console.println( "  getParam(\"p\") returns " + clip.getParam( "p" ) );
        Console.println( "  getParam(\"x\") returns " + clip.getParam( "x" ) );
        Console.println( " No of parameters: " + clip.getParamCount() );

        Console.println( "Testing required parameters: " );
        testArgs = new String[] { "db=dbname", "u=username" };
        String testDefaults[] = new String[] {"db=" + REQUIRED, "u=" + REQUIRED, "p=" + REQUIRED };
        Console.println( "  Arguments: " + Clip.print( testArgs ) );
        Console.println( "  Defaults: " + Clip.print( testDefaults ) );
        clip = new Clip( testArgs, testDefaults );
        Console.println( "  getParam(\"db\") returns " + clip.getParam( "db" ) );
        Console.println( "  getParam(\"u\")  returns " + clip.getParam( "u" ) );
        Console.println( "  getParam(\"p\")  returns " + clip.getParam( "p" ) );

        Console.prompt( "Done ..." );
        //System.exit( 0 );
      }
      catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
  }
/***** END OF TEST PROGRAM *****/
  static LogFile logger;
  JFrame jFrame;
}

