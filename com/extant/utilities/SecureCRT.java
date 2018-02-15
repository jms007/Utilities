/*
 * SecureCRT.java
 * Runs the batch file SecureCRT 
 * which logs on with session "extant.com + MySQL"
 *
 * Created on July 12, 2004, 4:43 PM
 */

package com.extant.utilities;
import java.io.IOException;
import java.lang.InterruptedException;
import com.extant.utilities.UtilitiesException;

/**
 *
 * @author  jms
 */
public class SecureCRT
implements Runnable
{
    public SecureCRT( LogFile logger )
    {
        if ( logger == null ) this.logger = new LogFile();
        else this.logger = logger;
    }
    
    public void run()
    {
        try { new DOSExec( "SecureCRT" ); }
        catch (IOException iox) { logger.log( LogFile.FATAL_ERROR, iox.getMessage() ); }
        catch (InterruptedException ix) { logger.log( LogFile.FATAL_ERROR, ix.getMessage() ); }
        catch (UtilitiesException ux) { logger.log( LogFile.FATAL_ERROR, ux.getMessage() ); }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        LogFile logger = new LogFile();
        new SecureCRT( logger ).run();
    }

    LogFile logger;
}

