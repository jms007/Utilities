/*
 * FileChooserFrame.java
 *
 * Created on August 12, 2002, 8:03 AM
 */

package com.extant.utilities;
import java.io.*;
import javax.swing.JFileChooser;

/**
 *
 * @author  jms
 */
public class FileChooserFrame
extends javax.swing.JFrame
{

    public FileChooserFrame
        ( String title
        , int mode // OPEN or SAVE
        , String directory
        ) {
            this( title, mode, FILES_ONLY, directory, null );
    }

    public FileChooserFrame
      ( String title
      , int mode // OPEN or SAVE
      , int fileSelectionMode // FILES_ONLY, DIRECTORIES_ONLY, or FILES_AND_DIRECTORIES
      , String directory
      , String fileName
      ) {
        String btnLabel;
        initComponents();
        jFileChooser1.setDialogTitle( title );
        jFileChooser1.setDialogType( mode );
        if ( mode == OPEN ) btnLabel = "Open";
        else btnLabel = "Create";
        jFileChooser1.setFileSelectionMode( fileSelectionMode );
        jFileChooser1.setCurrentDirectory( new File( directory ) );
        if ( fileName != null ) jFileChooser1.setSelectedFile( new File( fileName ) );
        action = jFileChooser1.showDialog( this, btnLabel );
        if ( action == APPROVE )
            selectedFile = jFileChooser1.getSelectedFile();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().add(jFileChooser1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm

    public int getAction() {
        // returns APPROVE or CANCEL
        return action;
    }
    
    public File getSelectedFile() {
        return selectedFile;
    }
    
    public void close() {
        exitForm( null );
    }
    
    /***** For Testing *****
    public static void main(String args[]) {
        File selectedFile;
        String selectedFilePath;
        String actionDesc;
        
        FileChooserFrame frame = new FileChooserFrame
          ( "FileChooserFrame Test"
          , FileChooserFrame.OPEN
          , "C:\\Downloads"
          );

        if ( frame.getAction() == frame.APPROVE ) {
            selectedFile = frame.getSelectedFile();
            selectedFilePath = selectedFile.getPath();
            actionDesc = "Selected";
        }
        else {
            selectedFilePath = "<none>";
            actionDesc = "Canceled";
        }
        //if ( selectedFile == null ) selectedFilePath = "<none>";
        //else selectedFilePath = selectedFile.getPath();
        Console.println( "FileChooserFrame: action=" + actionDesc +
          " file=" + selectedFilePath );
        frame.close();
        Console.prompt( "Done ..." );
        System.exit( 0 );
    }
    /***** End of Testing *****/
    
    public final static int OPEN = JFileChooser.OPEN_DIALOG;
    public final static int SAVE = JFileChooser.SAVE_DIALOG;
    public final static int APPROVE = JFileChooser.APPROVE_OPTION;
    public final static int CANCEL = JFileChooser.CANCEL_OPTION;
    public final static int FILES_ONLY = JFileChooser.FILES_ONLY;
    public final static int DIRECTORIES_ONLY = JFileChooser.DIRECTORIES_ONLY;
    public final static int FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
    private int action;
    private File selectedFile = null;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooser1;
    // End of variables declaration//GEN-END:variables
    
}

