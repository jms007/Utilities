package com.extant.utilities;
import java.io.File;
import java.awt.Frame;
import javax.swing.JFileChooser;

/**
 *
 * @author  jms
 */
public class BrowseHandler
{
    Frame parent;
    int dialogType;
    public static final int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
    public static final int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
    public static final int FILES_ONLY = JFileChooser.FILES_ONLY;
    public static final int OPEN_DIALOG = JFileChooser.OPEN_DIALOG;
    public static final int SAVE_DIALOG = JFileChooser.SAVE_DIALOG;
    int mode = FILES_ONLY; // default
    
    public BrowseHandler( Frame parent, int dialogType )
    {
        this.parent = parent;
        this.dialogType = dialogType;
    }

    public String getSelectedFile( String startingPlace )
    {
        return getSelectedFile( startingPlace, null, null );
    }

    public String getSelectedFile( String startingPlace, String[] extList, String descr )
    {
        String start = startingPlace;
        if ( start.endsWith( File.separator ) ) start += "*.*";
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory( new File(
            Strings.fileSpec( "Path", start ) ) );
        fileChooser.setSelectedFile( new File( start ) );
        fileChooser.setFileSelectionMode( mode );
        if ( extList != null )
            fileChooser.addChoosableFileFilter( new DefaultFileFilter( extList, descr ) );
        int returnVal;
        if ( dialogType == OPEN_DIALOG ) returnVal = fileChooser.showOpenDialog(parent);
        else returnVal = fileChooser.showSaveDialog( parent );
        if(returnVal == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        else return null; // Cancel
    }

    public void setMode( int mode )
    {
        this.mode = mode;
    }

    JFileChooser fileChooser;
}

