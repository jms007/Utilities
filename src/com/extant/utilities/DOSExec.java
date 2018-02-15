/*
 * DOSExec.java
 *
 * Created on March 6, 2003, 9:38 PM
 */

package com.extant.utilities;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author  jms
 */
public class DOSExec
{
    public DOSExec( String cmd )
    throws IOException, InterruptedException, UtilitiesException
    {
        this( cmd, null );
    }

    public DOSExec( String cmd, String inputForProcess )
    throws IOException, InterruptedException, UtilitiesException
    {
        Runtime runtime = Runtime.getRuntime();
        //shell = runtime.exec( "command.com /c " + cmd );
        shell = runtime.exec( "cmd /c " + cmd ); // for XP
        if ( inputForProcess != null )
        {
            OutputStreamWriter writer = new OutputStreamWriter( shell.getOutputStream() );
            if ( !inputForProcess.endsWith( "\n" ) ) inputForProcess += "\n";
            writer.write( inputForProcess );
            writer.flush();
        }
        collectOutput();
        shell.waitFor();
        int exitValue = shell.exitValue();
        if ( exitValue != 0 )
            throw new UtilitiesException( UtilitiesException.DOSEXEC_ABNORMAL,
                "ExitCode=" + exitValue + " cmd=" + cmd );
    }

    private void collectOutput()
    throws IOException
    {
        InputStreamReader reader = new java.io.InputStreamReader( shell.getInputStream() );
        output = new StringBuffer();
        int c;
        while ( true )
        {
            c = reader.read();
            if ( c == -1 ) break;
            output.append( (char)c );
        }
    }

    public void logOutput( LogFile logger, int logLevel )
    {
        StringTokenizer st = new StringTokenizer( output.toString(), "\n" );
        while ( st.hasMoreElements() )
            logger.log( logLevel, (String)st.nextElement() );
    }

    public String getOutput()
    {
        return output.toString();
    }

    public InputStream getInputStream()
    throws IOException
    { // This is the standard output from the shell process
        return shell.getInputStream();
    }

    public OutputStream getOutputStream()
    { // This is the standard input to the shell process
        return shell.getOutputStream();
    }

    public int exitValue()
    {
        return exitValue;
    }

    public void destroy()
    throws IOException
    {
        shell.getInputStream().close(); 
        shell.getOutputStream().close(); 
        shell.getErrorStream().close(); 
        shell.destroy();
    }

    Process shell;
    int exitValue;
    StringBuffer output;

    /***** FOR TESTING *****
    public static void main( String[] args )
    {
        try
        {
            String cmd = "dir C:\\Projects";
            DOSExec dosExec = new DOSExec( cmd );
            Console.println( "ExitValue = " + dosExec.exitValue() );
            Console.println( dosExec.getOutput() );
            dosExec.destroy();
            System.exit( 0 );
        }
        catch (IOException iox) { Console.println( iox.getMessage() ); }
        catch (InterruptedException ix) { Console.println( ix.getMessage() ); }
        catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
        System.exit( 1 );
    }
    /*****/

    // Note:  If you need to change the current working directory for a thread:
    //        Properties properties;
    //        Properties = System.getProperties();
    //        Console.println( "user.dir=" + properties.getProperty( "user.dir" ) );
    //        properties.setProperty( "user.dir", dataDir );
    //        Console.println( "CWD=" + Strings.getCWD() );
    // 
}

