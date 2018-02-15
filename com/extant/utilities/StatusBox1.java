/*
 * StatusBox1.java
 *
 * Created on October 20, 2006, 3:03 PM
 */

package com.extant.utilities;
import javax.swing.JFrame;
import java.awt.Point;

/**
 *
 * @author  jms
 */
public class StatusBox1
extends JFrame
{
    public StatusBox1( String msg)
    {   // Location defaults to centered, Border Title defaults to "Status"
        this(msg, null, null);
    }

    public StatusBox1(String msg, Point location)
    {   // Border Title defaults to "Status"
        this(msg, location, null);
    }

    public StatusBox1(String msg, Point location, String borderTitle)
    {
        //super(parent, false);
        initComponents();
        setup( msg, location, borderTitle );
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtMessage = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N

        txtMessage.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.add(txtMessage);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    void setup( String msg, Point location, String borderTitle )
    {
        if ( location == null )
        {   // This will center the dialog
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width-this.getWidth())/2,(screenSize.height-this.getHeight())/2);
        }
        else setLocation( location );
        txtMessage.setBackground( jPanel1.getBackground() );
        if (borderTitle != null) setBorderTitle( borderTitle );
        updateMsg(msg);
        setVisible(true);
    }

    public void updateMsg( String newMessage )
    {
        txtMessage.setText( newMessage );
        pack();
    }

    public void setBorderTitle(String title)
    {
        jPanel1.setBorder(new javax.swing.border.TitledBorder
            (null, title, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
             javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10)
            ) );
    }

    public void close()
    {
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    /***** FOR TESTING *****/
    public static void main(String args[])
    {
        try
        {
            StatusBox1 statusBox = new StatusBox1("This is the initial message", null, "Original Title" );
            Thread.sleep(5000L);
            statusBox.updateMsg( "This is a longer message that requires more width" );
            Thread.sleep(5000L);
            statusBox.setBorderTitle("New Title");
            statusBox.updateMsg( "This message is really long and has a lot of information\nthat requires more than one\nline\ntodisplay" );
            Thread.sleep(5000L);
            statusBox.updateMsg( "Message #3" );
            Thread.sleep(5000L);
            statusBox.close();
            System.exit(0);
        }
        catch (Exception x) { Console.println( x.getMessage() ); }
    }
    /*****/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea txtMessage;
    // End of variables declaration//GEN-END:variables
    
}

