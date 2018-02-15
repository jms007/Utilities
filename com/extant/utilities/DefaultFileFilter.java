/*
 * DefaultFileFilter.java
 *
 * Created on September 29, 2002, 6:23 AM
 */

package com.extant.utilities;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author  jms
 */
public class DefaultFileFilter extends FileFilter
{
    String[] extentions;
    String description;
    
    public DefaultFileFilter( String[] extentions, String description )
    {
        this.extentions = extentions;
        this.description = description;
        // Take off the dots
        for (int i=0; i<extentions.length; ++i)
            if ( extentions[i].startsWith( "." ) )
                extentions[i] = extentions[i].substring( 1 );
    }

    public boolean accept( File file )
    {
        if ( file.isDirectory() ) return true; // Always show directories
        for (int i=0; i<extentions.length; ++i)
            if ( Strings.fileSpec( "Ext", file.getName() ).equalsIgnoreCase( extentions[i] ) )
                return true;
        return false;
    }

    public String getDescription()
    {
        return description;
    }

}

