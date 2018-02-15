/*
 * Progress.java
 *
 * Created on August 11, 2002, 10:38 PM
 */

package com.extant.utilities;
import java.lang.Thread;

/**
 *
 * @author  jms
 */
public class Progress extends javax.swing.JFrame {
    
    public Progress( String title ) {
        initComponents();
        this.setTitle( title );
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        txtStatus = new javax.swing.JTextField();
        txtLabel = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 340, -1));

        txtStatus.setBackground(new java.awt.Color(204, 204, 204));
        txtStatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 340, -1));

        txtLabel.setBackground(new java.awt.Color(204, 204, 204));
        txtLabel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 340, -1));

        setSize(new java.awt.Dimension(441, 183));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        //System.exit(0);
        this.dispose();
    }//GEN-LAST:event_exitForm
    
    public void setProgress( int percent ) {
        jProgressBar1.setValue( percent );
    }
    
    public void setProgress( long current, long total )
    {
        setProgress ( (int)(current / total) * 100 );
    }

    public void setProgress( int current, int total )
    {
        setProgress( (long)current, (long)total );
    }

    public void setLabel( String label ) {
        txtLabel.setText( label );
        txtLabel.setVisible( !label.equals( "" ) );
    }
        
    public void setStatus( String status ) {
        txtStatus.setText( status );
        txtStatus.setVisible( !status.equals( "" ) );
    }    

    /*** FOR TESTING ***
    public static void main(String args[]) {
        Progress progress = new Progress( "Progress Test" );
        progress.show();
        int percent = 0;
        try {
            progress.setLabel( "Our Process" );
            while ( percent <= 110 ) {
                Thread.sleep( 1000L );
                progress.setProgress( percent );
                progress.setStatus( percent + "% complete" );
                percent += 10;
            }
        }
        catch (InterruptedException ix )
        {
            System.out.println( ix.getMessage() );
        }
        catch (Exception x) {
            System.out.println( "Unexpected exception: " + x.getMessage() );
            x.printStackTrace();
        }
        System.exit(0);
    }
    /***/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JTextField txtStatus;
    // End of variables declaration//GEN-END:variables
}

