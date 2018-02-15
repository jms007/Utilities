/*
 * WipeFiles.java
 *
 * Created on October 10, 2002, 11:20 AM
 */

/**
 *
 * @author  jms
 */
package com.extant.utilities;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class WipeFiles
extends javax.swing.JFrame
implements Runnable
{
    public WipeFiles
        ( Vector fileList
        , Progress progress
        , long totalBytes
        , int nPasses
        , boolean delete
        )
    {
        this.fileList = fileList;
        this.progress = progress;
        this.totalBytes = totalBytes;
        this.nPasses = nPasses;
        this.delete = delete;
    }

    public WipeFiles()
    {
        fileList = null;
        initComponents();
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFilename = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Start = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        statusBar = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        ckbWipeSubDir = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        txtNPasses = new javax.swing.JTextField();
        ckbDelete = new javax.swing.JCheckBox();

        setTitle("Wipe Files");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtFilename, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 360, -1));

        btnBrowse.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        getContentPane().add(btnBrowse, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Directory or File Name:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        Start.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        Start.setText("Start");
        Start.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartActionPerformed(evt);
            }
        });
        getContentPane().add(Start, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 60, 30));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(statusBar);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 450, 30));

        btnCancel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 60, 30));

        ckbWipeSubDir.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ckbWipeSubDir.setText("Wipe Sub-Directories");
        getContentPane().add(ckbWipeSubDir, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("Number of overwrites:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, -1));
        getContentPane().add(txtNPasses, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 30, -1));

        ckbDelete.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ckbDelete.setText("Delete files after overwriting");
        getContentPane().add(ckbDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        setSize(new java.awt.Dimension(510, 347));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        exitForm( null );
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBrowseActionPerformed
    {//GEN-HEADEREND:event_btnBrowseActionPerformed
        BrowseHandler bh = new BrowseHandler( this, BrowseHandler.OPEN_DIALOG );
        bh.setMode( BrowseHandler.FILES_AND_DIRECTORIES );
        if ( !txtFilename.getText().equals( "" ) ) startingPlace = txtFilename.getText();
        String selectedFile = bh.getSelectedFile( startingPlace );
        if ( selectedFile != null )
        {
            // Always turn off subdirectory wipe when there is a new selection
            if ( ckbWipeSubDir.isSelected() )
                ckbWipeSubDir.setSelected( selectedFile.equals( txtFilename.getText() ) );
            boolean isDir = new File( selectedFile ).isDirectory();
            ckbWipeSubDir.setEnabled( isDir );
            //if ( isDir ) selectedFile += File.separator + "*";
            txtFilename.setText( selectedFile );
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void StartActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_StartActionPerformed
    {//GEN-HEADEREND:event_StartActionPerformed
        if ( confirm() ) doit();
    }//GEN-LAST:event_StartActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void setup()
    {
        txtNPasses.setText( "3" );
        ckbDelete.setSelected( true );
    }

    private boolean confirm()
    {
        boolean ans = true;
        if ( new File( txtFilename.getText() ).isDirectory() )
        {
            String msg = "This will wipe all files in this directory";
            if ( ckbWipeSubDir.isSelected() ) msg += "\nand all sub-directories.";
            msg += "\nDo you want to continue?";
            MsgBox msgBox = new MsgBox( this, "Confirm Directory Wipe", msg,
                MsgBox.YES_NO );
            ans = msgBox.getCommand().equalsIgnoreCase( "yes" );
            msgBox.setVisible( false );
        }
        return ans;
    }

    private void doit()
    {
        if ( !Strings.isValidInt( txtNPasses.getText() ) )
        {
            statusBar.setText( "Number of overwrites is not a valid number." );
            txtNPasses.requestFocus();
            return;
        }
        nPasses = Strings.parseInt( txtNPasses.getText() );
        delete = ckbDelete.isSelected();
        if ( new File( txtFilename.getText() ).isFile() )
        {   // handle the easy case here
            wipeFile( txtFilename.getText() );
            if ( delete ) statusBar.setText( "File wiped & deleted." );
            else statusBar.setText( "File wiped." );
            return;
        }
        statusBar.setText( "" );
        // else we collect the list of files & directories
        fileList = new Vector( 100, 100 );
        nFiles = 0;
        nBytes = 0;
        buildDir( fileList, txtFilename.getText() );
        String msg = Strings.plurals( "file", nFiles ) + " and " +
            Strings.format( nBytes, "###,###,###,##0" ) + " bytes";
        statusBar.setText( msg );
        Progress progress = new Progress( "Wipe Files" );
        progress.setVisible( true );
        progress.setLabel( "Wiping: " + txtFilename.getText() );
        WipeFiles wipeFiles = new WipeFiles
            ( fileList
            , progress
            , totalBytes
            , nPasses
            , delete
            );
        new Thread( wipeFiles ).start();
        // Note that our thread is still running, and our Frame is active.
        // The Progress Frame overlays ours, so maybe they won't click on us
    }

    private void buildDir( Vector fileList, String dir )
    {
        if ( !dir.endsWith( File.separator ) ) dir += File.separator;
        statusBar.setText( dir );
        Strings strings = new Strings();
        String[] files = strings.getMatchingFiles( dir, "*" );
        for (int i=0; i<files.length; ++i)
        {
            if ( new File( dir + files[i] ).isDirectory() )
                buildDir( fileList, dir + files[i] + File.separator );
            else buildFile( fileList, dir + files[i] );
        }
        totalBytes = nBytes;
    }

    private void buildFile( Vector fileList, String fileName )
    {
        fileList.addElement( fileName );
        ++nFiles;
        try { nBytes += UsefulFile.getFileSize( fileName ); }
        catch (IOException iox) { }
    }

    public void run()
    {
        nWiped = 0;
        nBytes = 0L;
//Console.println( "Beginning wipe (" + nPasses + ") ... " );
        for (int i=0; i<fileList.size(); ++i)
        {
            progress.setStatus( (String)fileList.elementAt( i ) );
            wipeFile( (String)fileList.elementAt( i ) );
            progress.setProgress( nBytes, totalBytes );
        }
        progress.dispose();
    }

// (Old code)
// If you (think you) want to do the processing in this thread, you can use this
// code instead of making a new thread for processing.
// But be aware that you can't display anything while the processing is being
// done.
// Add to doit, replacing the code that builds of Vector fileList and starts
// the new Thread.
//        if ( new File( txtFilename.getText() ).isFile() )
//        {
//            wipeFile( txtFilename.getText() );
//            return;
//        }
//        wipeDir( txtFilename.getText() );
//        String msg = Strings.plurals( " File", nWiped ) +
//            " wiped with " + Strings.plurals( " pass", nPasses );
//        if ( ckbDelete.isSelected() ) msg += " and deleted";
//        msg += ".  (" + Strings.format( nBytes, "###,###,###,###" ) + " bytes)";
//        statusBar.setText( msg );
//
//    private void wipeDir( String dir )
//    {
//        if ( !dir.endsWith( File.separator ) ) dir += File.separator;
//        statusBar.setText( dir );
//        Strings strings = new Strings();
//        String[] files = strings.getMatchingFiles( dir, "*" );
//        for (int i=0; i<files.length; ++i)
//        {
////Console.println( "checking " + files[i] + "  dir? " + new File( dir + files[i] ).isDirectory() );
//            if ( new File( dir + files[i] ).isDirectory() ) wipeDir( dir + files[i] + File.separator );
//            else wipeFile( dir + files[i] );
//        }
//        if ( ckbDelete.isSelected() ) new File( dir ).delete();
//    }
//

    private void wipeFile( String file )
    {
        try
        {
//Console.println( "wiping file " + file );
            new FileWipe( file, nPasses );
            ++nWiped;
            nBytes += UsefulFile.getFileSize( file );
            if ( delete ) new File( file ).delete();
        }
        catch (IOException iox)
        {
            //statusBar.setText( iox.getMessage() );
            Console.println( "Error processing file " + file + ": " + iox.getMessage() );
        }
        catch (UtilitiesException ux)
        {
            //statusBar.setText( ux.getMessage() );
            Console.println( "Error processing file " + file + ": " + ux.getMessage() );
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        try
        {
            Clip clip = new Clip( args, new String[] { "s=C:\\" } );
            WipeFiles wipeFiles = new WipeFiles();
            wipeFiles.startingPlace = clip.getParam( "s" );
            wipeFiles.setVisible( true );
        }
        catch (UtilitiesException ux)
        {
            Console.println( ux.getMessage() );
        }
    }

    int nPasses;
    int nWiped;
    int nFiles;
    long nBytes;
    Vector fileList;
    long totalBytes;
    Progress progress;
    boolean delete;
    public String startingPlace;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Start;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JCheckBox ckbDelete;
    private javax.swing.JCheckBox ckbWipeSubDir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel statusBar;
    private javax.swing.JTextField txtFilename;
    private javax.swing.JTextField txtNPasses;
    // End of variables declaration//GEN-END:variables
}

