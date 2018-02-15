/*
 * TextDialog.java
 * This dialog may be used for displaying either general text files or text files
 * intended for use as help screens.  By default, the dialog is sized to show
 * the entire width of the text files and 400 pixels in height and is centered
 * on the screen.  Both dimensions are scrollable, if necessary, to display the
 * complete text file with no line-wrap.
 *
 * If the dialog is used to display a help window, the caller should specify the
 * size (by calling setDialogSize(), then override the height setting) and location
 * of the dialog window and also remove the buttons by calling showButtons( false ).
 *
 * Created on November 26, 2002, 6:15 PM
 */

/**
 *
 * @author  jms
 */

package com.extant.utilities;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.awt.*;

public class TextDialog
    extends javax.swing.JDialog
{
    public TextDialog(javax.swing.JFrame parent, boolean modal)
    {
        super(parent, modal);
        this.parent = parent;
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setTitle("Text Dialog");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(500, 300));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPanel2KeyTyped(evt);
            }
        });

        btnSave.setMnemonic('S');
        btnSave.setText("Save to File");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel2.add(btnSave);

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel2.add(btnClose);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(523, 456));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel2KeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jPanel2KeyTyped
    {//GEN-HEADEREND:event_jPanel2KeyTyped
        //!! Typing causes a ding
        if ( evt.getKeyChar() == java.awt.event.KeyEvent.VK_ENTER )
        {
            evt.consume();
            closeDialog( null );
        }
    }//GEN-LAST:event_jPanel2KeyTyped

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSaveActionPerformed
    {//GEN-HEADEREND:event_btnSaveActionPerformed
        FileChooserFrame fileChooser = new FileChooserFrame
            ( "Select Output File"
            , FileChooserFrame.SAVE
            , Strings.fileSpec( "Path", outfileName )
            //, fileName if you want to select a particular file
            );
        if ( fileChooser.getAction() == FileChooserFrame.CANCEL ) return;
        outfileName = fileChooser.getSelectedFile().getPath();
        Console.println("TextDialog.btnSaveAction: outfileName="+outfileName);
        try
        {
            String mode = "w";
            if ( new File( outfileName ).exists() )
            {
                MsgBox msgBox = new MsgBox( parent, "Confirm Overwrite/Append",
                    "File " + outfileName + " already exists.", null, 
                    new String[] { "Overwrite", "Append", "Cancel" } );
                    //"Do you want to overwrite this file?", MsgBox.YES_NO );
                if ( msgBox.getCommand().equalsIgnoreCase( "Cancel" ) ) return;
                if ( msgBox.getCommand().equalsIgnoreCase( "Append" ) ) mode = "w+";
            }
        UsefulFile file = new UsefulFile( outfileName, mode );
        StringTokenizer st = new StringTokenizer( jTextArea1.getText(), "\n" );
        while ( st.hasMoreElements() )
            file.println( (String)st.nextElement() );
        file.close();
        }
        catch (IOException iox)
        {
            Console.println( iox.getMessage() );
            MsgBox msgBox = new MsgBox( parent, "Error",
                "Unable to save:\n" +
                iox.getMessage(), MsgBox.OK );
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        closeDialog( null );
    }//GEN-LAST:event_btnCloseActionPerformed

    /** Close the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

//    public void setText( StringBuffer sb )
//    {
//        setText( sb.toString() );
//    }
//
    public void setText( String text )
    {
        jTextArea1.setText( text );
        jTextArea1.setCaretPosition(0);
        //setDialogSize( text );
    }

    public void setOutfileName( String outfileName )
    {
        this.outfileName = outfileName;
    }

    public int getTextWidth( String text )
    {
        FontMetrics fontMetrics = jTextArea1.getFontMetrics( jTextArea1.getFont() );
        return fontMetrics.stringWidth( Strings.findLongestLine( text ) );
    }

    public void setDialogSize( String text )
    {
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //FontMetrics fontMetrics = jTextArea1.getFontMetrics( jTextArea1.getFont() );
        int width = getTextWidth( text ) + 50; // to accomodate possible vertical scroll bar
        if ( width > screenSize.width ) width = screenSize.width;
        if ( width < 250 ) width = 250;
        int height = 400;
        if ( height > screenSize.height ) height = screenSize.height;
        setSize( width, height );
        setLocation((screenSize.width-width)/2,(screenSize.height-height)/2);
    }

    public void showButtons( boolean showButtons )
    {
        if ( showButtons ) return; // Buttons are shown by default;
                                   // Once removed, they cannot be put back
        getContentPane().remove( jPanel2 );
        pack();
    }

    public void doSetTitle( String title )
    {
        setTitle( title );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new TextDialog(new javax.swing.JFrame(), true).setVisible( true );
    }

    javax.swing.JFrame parent;
    String outfileName = "C:\\Temp\\TextDialogSave.txt"; // Default

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

