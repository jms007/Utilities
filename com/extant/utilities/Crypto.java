package com.extant.utilities;
/**
 *
 * @author  jms
 * Provides a front end for the Blowfish file functions
 */

import java.io.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.*;
import java.awt.Color;
import javax.swing.JFrame;

public class Crypto
    extends javax.swing.JFrame
    implements ClipboardOwner
        
{
    Blowfish bf;
    boolean encrypt;
    String inFileName;
    String outFileName;
    Color defaultBackground;
    private String keyPhrase;
    Clipboard clipboard;

    public Crypto( String inputFileName )
    {
        inFileName = inputFileName;
        initComponents();
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtInFileName = new javax.swing.JTextField();
        txtOutFileName = new javax.swing.JTextField();
        chkBoxVerify = new javax.swing.JCheckBox();
        chkBoxWipe = new javax.swing.JCheckBox();
        btnBrowseIn = new javax.swing.JButton();
        btnBrowseOut = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        statusBar = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();
        rbEncrypt = new javax.swing.JRadioButton();
        rbDecrypt = new javax.swing.JRadioButton();

        setTitle("File Encryption/Decryption Utility");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Input File:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 39, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Output File:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, -1));

        txtInFileName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInFileNameFocusLost(evt);
            }
        });
        getContentPane().add(txtInFileName, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 39, 310, -1));
        getContentPane().add(txtOutFileName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 310, -1));

        chkBoxVerify.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        chkBoxVerify.setText("Verify File Length");
        getContentPane().add(chkBoxVerify, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, -1, -1));

        chkBoxWipe.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        chkBoxWipe.setText("Wipe & Delete Input File");
        getContentPane().add(chkBoxWipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 197, -1));

        btnBrowseIn.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnBrowseIn.setText("Browse ...");
        btnBrowseIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBrowseIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseInActionPerformed(evt);
            }
        });
        getContentPane().add(btnBrowseIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 39, -1, -1));

        btnBrowseOut.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnBrowseOut.setText("Browse ...");
        btnBrowseOut.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBrowseOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseOutActionPerformed(evt);
            }
        });
        getContentPane().add(btnBrowseOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        statusBar.setText(" ");
        jPanel1.add(statusBar);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 480, 30));

        btnCancel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 210, 80, -1));

        btnOK.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnOK.setText("OK");
        btnOK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        getContentPane().add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 80, -1));

        rbEncrypt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        rbEncrypt.setText("Encrypt");
        getContentPane().add(rbEncrypt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, -1, -1));

        rbDecrypt.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        rbDecrypt.setText("Decrypt");
        getContentPane().add(rbDecrypt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, -1, -1));

        setSize(new java.awt.Dimension(575, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtInFileNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInFileNameFocusLost
        //Console.println( "txtInFileNameFocusLost: " + evt.toString() );
        calcDefaults( txtInFileName.getText() );
        setMode( encrypt );
        txtOutFileName.setText( outFileName );
    }//GEN-LAST:event_txtInFileNameFocusLost

    private void btnBrowseOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseOutActionPerformed
        String defaultDir = Strings.fileSpec( "P", outFileName );
        if ( defaultDir.equals( "" ) ) defaultDir = new Strings().getCWD();
        FileChooserFrame frame = new FileChooserFrame
          ( "Select Output File"
          , FileChooserFrame.SAVE
          , defaultDir
          );

        if ( frame.getAction() == frame.APPROVE ) {
            outFileName = frame.getSelectedFile().getPath();
            txtOutFileName.setText( outFileName );
        }
        frame.close();
    }//GEN-LAST:event_btnBrowseOutActionPerformed

    private void btnBrowseInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseInActionPerformed
        String defaultDir = Strings.fileSpec( "P", inFileName );
        if ( defaultDir.equals( "" ) ) defaultDir = new Strings().getCWD();
        FileChooserFrame frame = new FileChooserFrame
          ( "Select Input File"
          , FileChooserFrame.OPEN
          , defaultDir
          );

        if ( frame.getAction() == frame.APPROVE ) {
            inFileName = frame.getSelectedFile().getPath();
            txtInFileName.setText( inFileName );
            txtInFileNameFocusLost( null );
        }
        frame.close();
    }//GEN-LAST:event_btnBrowseInActionPerformed

    private void rbDecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDecryptActionPerformed
        setMode( false );
    }//GEN-LAST:event_rbDecryptActionPerformed

    private void rbEncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEncryptActionPerformed
        setMode( true );
    }//GEN-LAST:event_rbEncryptActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        FileInputStream inFile;
        FileOutputStream outFile;

        PassPhrase passPhrase = new PassPhrase( (JFrame)this, true );
        if ( encrypt ) passPhrase.setMessage( "Encrypting this file" );
        else passPhrase.setMessage( "Decrypting this file" );
        passPhrase.setVisible( true );
        if ( passPhrase.wasCancelled() ) return;
        byte[] keyBytes = passPhrase.getPassphrase().getBytes();

        if ( keyBytes.length == 0 )
        {
            statusBar.setText( "No Passphrase was entered." );
            return;
        }

        inFileName = txtInFileName.getText();
        outFileName = txtOutFileName.getText();
        try {
            inFile = new FileInputStream( inFileName );
        }
        catch (FileNotFoundException x) {
            statusBar.setText( "Input File was not found." );
            txtInFileName.requestFocus();
            return;
        }
        if ( new File( outFileName ).exists() ) {
        MsgBox msgBox = new MsgBox
            ( this
            , "Crypto Utility"
            , "The output file\n" + outFileName + "\nalready exists.\nOverwrite?"
            , MsgBox.YES_NO
            );
        if ( msgBox.getCommand().equalsIgnoreCase( "Yes" ) ) return;
        }
        try {
            outFile = new FileOutputStream( outFileName );
        }
        catch (IOException iox) {
            statusBar.setText( iox.getMessage() );
            return;
        }
        bf.processFile( inFile, outFile, encrypt, keyBytes );
        String statusText = "Completed ... ";
        if ( !encrypt )
            statusText = "Trailing Magic found=" + bf.isTrailingMagicFound() + ".";
        if ( chkBoxVerify.isSelected() ) {
            String[] args = new String[2];
            if ( encrypt ) {
                args[0] = "pt=" + inFileName;
                args[1] = "ze=" + outFileName;
            }
            else {
                args[0] = "pt=" + outFileName;
                args[1] = "ze=" + inFileName;
            }
            CheckLength checkLength = new CheckLength( args );
            if ( checkLength.isGood() )
                statusText += " File Length checks good.";
            else
                statusText += " File Length does NOT check (" +
                  "calc=" + checkLength.getCalcLength() + " " +
                  "actual=" + checkLength.getEzLength() + ")";
        }
        if ( chkBoxWipe.isSelected() ) {
            try {
                statusBar.setText( "Wiping " + inFileName + " ..." );
                new FileWipe( inFileName, 3 );
                // We assume that he wants to delete a wiped file
                UsefulFile.delete( inFileName );
            }
            catch (Exception x) {
                statusBar.setText( x.getMessage() );
            }
        }
        statusBar.setText( statusText );       
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        exitForm( null );
    }//GEN-LAST:event_btnCancelActionPerformed

    private void setup() {
        bf = new Blowfish();
        //clearScreen();
        defaultBackground = txtInFileName.getBackground();
        clipboard = getToolkit ().getSystemClipboard ();
        txtInFileName.setText( inFileName );
        calcDefaults( inFileName );
        txtOutFileName.setText( outFileName );
        setMode( encrypt );
    }

    private void calcDefaults( String inFileName ) {
        if ( inFileName.equals( "" ) ) {
            outFileName = "";
            encrypt = true;
            return;
        }
        String ext = Strings.fileSpec( "E", inFileName ).toLowerCase();
        String defaultOutFileName = inFileName.substring( 0, inFileName.length() - ext.length() );
        encrypt = !ext.equals( "ze" ) && !ext.equals( "xe" );
        if ( encrypt )
            if ( ext.equals( "zip" ) ) defaultOutFileName += "ze";
            else defaultOutFileName += "xe";
        else
            if ( ext.equals( "ze" ) ) defaultOutFileName += "zip";
            else defaultOutFileName += "txt";
        outFileName = defaultOutFileName;
        chkBoxWipe.setSelected( false );
    }

    private void setMode( boolean encryptMode ) {
        encrypt = encryptMode;
        rbEncrypt.setSelected( encrypt );
        rbDecrypt.setSelected( !encrypt );
        chkBoxVerify.setSelected( encrypt );
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable transferable)
    { // It means anything we have put in the clipboard has been overwritten by somebody else
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if ( args.length >= 1 ) new Crypto( args[0] ).setVisible( true );
        else new Crypto( "" ).setVisible( true );
    }   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseIn;
    private javax.swing.JButton btnBrowseOut;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkBoxVerify;
    private javax.swing.JCheckBox chkBoxWipe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton rbDecrypt;
    private javax.swing.JRadioButton rbEncrypt;
    private javax.swing.JLabel statusBar;
    private javax.swing.JTextField txtInFileName;
    private javax.swing.JTextField txtOutFileName;
    // End of variables declaration//GEN-END:variables

    class ClipboardContents
    implements Transferable
    {
        ClipboardContents()
        { }

        ClipboardContents( String contents )
        {
            this.contents = contents;
        }

        public void setTransferData( String contents )
        {
            this.contents = contents;
        }

        public Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor) throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException
        {
            return contents;
        }
        
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[] { DataFlavor.stringFlavor };
        }
        
        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor dataFlavor)
        {
            return dataFlavor == dataFlavor.stringFlavor;
        }

        private String contents;
    }
}

