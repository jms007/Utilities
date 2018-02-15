/*
 * DirectoryTree.java
 *
 * Created on December 10, 2002, 12:25 PM
 */

package com.extant.utilities;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;
import javax.swing.JScrollPane;

/**
 *
 * @author  jms
 */
public class DirectoryTree
    extends javax.swing.JFrame
{

    public DirectoryTree()
    throws UtilitiesException
    {
        this ( false, null );
    }

    public DirectoryTree( boolean standalone )
    throws UtilitiesException
    {
        this ( standalone, null );
    }

    public DirectoryTree( boolean standalone, String rootDir )
    throws UtilitiesException
    {
        this.standalone = standalone;
        initComponents();
        doit( rootDir );
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();

        setTitle("Directory Tree");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 400));
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.NORTH);

        btnClose.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel1.add(btnClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(336, 533));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        exitForm( null );
    }//GEN-LAST:event_btnCloseActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        if ( standalone ) System.exit(0);
        else this.dispose();
    }//GEN-LAST:event_exitForm

    private void doit( String rootDir )
    throws UtilitiesException
    {
        BrowseHandler browser = new BrowseHandler( this, BrowseHandler.OPEN_DIALOG );
        browser.setMode( BrowseHandler.DIRECTORIES_ONLY );
        if ( rootDir == null )
            rootDir = browser.getSelectedFile( "C:\\" );
        four11 = new Four11( rootDir );
        jScrollPane1.setViewportView( four11.getTree() );
    }

    public Vector getDirList()
    {
        return four11.getDirList();
    }

    /**
     * @param args the command line arguments
     * Use: DirectoryTree [rootDir]
     * If rootDir is not specified, a FileChooser Dialog is presented
     */
    /***** FOR TESTING *****/
    public static void main(String args[])
    {
        try
        {
            Clip clip = new Clip( args, new String[] {} );
            new DirectoryTree( true, clip.getParam( 0 ) ).setVisible( true );
        }
        catch (UtilitiesException ux)
        {
            Console.println( ux.getMessage() );
        }
    }
    /*****/
    
    boolean standalone;
    Four11 four11;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

