/*
 * DisplayTree.java
 *
 * This class creates a JFrame and displays a JTree in a scrollPanel in
 * that frame.
 * The title, location, and size of the frame are set by parameters, and
 * a Close button is implemented.
 *
 * Created on September 3, 2006, 9:35 AM
 */

package com.extant.utilities;

import java.awt.Point;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTree;

/**
 *
 * @author  jms
 */
public class DisplayTree
    extends JFrame
{
    public DisplayTree( String frameTitle, JTree tree, Point location, Dimension size )
    {
        // Dimension is (height, width)
        // Point is (x, y)
        //Console.println("location="+location.x+","+location.y);
        //Console.println("size="+size.height+","+size.width);
        initComponents();
        if ( frameTitle != null ) setTitle( frameTitle );
        if ( location != null ) this.setLocation( location );
        if ( size != null ) this.setSize( size );
        jScrollPane1.setPreferredSize(size);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.WEST);
        //jScrollPane1.setLocation(new Point(this.getLocation().x + this.getWidth(), this.getLocation.y));
        jScrollPane1.setViewportView( tree );
        this.pack();
        this.setVisible(true);
    }

    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Dialog", 0, 12));
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jPanel1.add(btnClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //setBounds((screenSize.width-305)/2, (screenSize.height-419)/2, 305, 419);
    }//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        exitForm( null );
    }//GEN-LAST:event_btnCloseActionPerformed

    /** Exit the Application */
    public void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        dispose();
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
