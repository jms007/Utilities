/*
 * ChangeWorkDir.java
 *
 * Created on January 21, 2004, 10:59 PM
 */

package com.extant.utilities;
import java.util.Properties;

/**
 *
 * @author  jms
 * Changes the current directory for this thread.
 * (Note that workDir can contain a drive letter.)
 *
 * To execute as a separate application
 *   ... com.extant.utilities.ChangeWorkDir <newDirectory>
 */

public class ChangeWorkDir
{
    public ChangeWorkDir( String workDir )
    {
        chdir( workDir );
    }

    public static void chdir( String workDir )
    {
        Properties properties = System.getProperties();
//Console.println( "user.dir=" + properties.getProperty( "user.dir" ) );
        properties.setProperty( "user.dir", workDir );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new ChangeWorkDir( args[0] );
    }

}

