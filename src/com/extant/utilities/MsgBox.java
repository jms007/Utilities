package com.extant.utilities;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.datatransfer.*;
/**
 *
 * @author  jms
 * 
 * MsgBox is a general prompt/response tool.
 * The standard options display a message and offer:
 *       a single button "OK"      (Option MsgBox.OK)
 *       "OK" and "Cancel" buttons (Option MsgBox.OK_CANCEL)
 *       "Yes" and "No" buttons    (Option MsgBox.YES_NO)
 *       no buttons at all         (Option MsgBox.NO_BUTTONS)
 *
 * The custom options display a message and offer a text response
 * (with an optional default) and 1 - 5 buttons labeled as given
 * in the String array 'commands' parameter.
 */
public class MsgBox
extends javax.swing.JDialog
//implements java.awt.datatransfer.ClipboardOwner  I think you will need this if you want to 'Copy'
{
    /** Creates new MsgBox for one of the standard options */
    public MsgBox(JFrame parent, String title, String msg, int option)
    {
        initComponents();
        setup( title, msg, option );
        setVisible( true );
    }

    public MsgBox(String title, String msg, int option)
    {
        this(new JFrame(), title, msg, option);
    }
    
    /** Creates a new MsgBox with custom options */
    public MsgBox(JFrame parent, String title, String msg, String defaultResponse, String[] commands)
    {
        super( parent, commands.length > 0 );
        this.parent = parent;
        initComponents();
        setup( title, msg, defaultResponse, commands );
        setVisible( true );
    }

    public MsgBox(String title, String msg, String defaultResponse, String[] commands)
    {
        this(new JFrame(), title, msg, defaultResponse, commands);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        pumuPaste = new javax.swing.JMenuItem();
        pumuNewPW = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        txtMessage = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        txtResponse = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        pumuPaste.setText("Paste");
        pumuPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuPasteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pumuPaste);

        pumuNewPW.setText("New Random Password");
        pumuNewPW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuNewPWActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pumuNewPW);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MsgBox");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        txtMessage.setEditable(false);
        txtMessage.setText("This is the default message.");
        jPanel1.add(txtMessage);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        txtResponse.setColumns(20);
        txtResponse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtResponseMouseClicked(evt);
            }
        });
        jPanel3.add(txtResponse);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jButton1.setText("jButton1");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton2.setText("jButton2");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton3.setText("jButton3");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton4.setText("jButton4");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton5.setText("jButton5");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        setBounds(0, 0, 408, 133);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jButton1KeyPressed
    {//GEN-HEADEREND:event_jButton1KeyPressed
        if ( evt.getKeyCode() == evt.VK_ENTER ) jButton1ActionPerformed( null );
    }//GEN-LAST:event_jButton1KeyPressed

    private void pumuNewPWActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pumuNewPWActionPerformed
    {//GEN-HEADEREND:event_pumuNewPWActionPerformed
        txtResponse.setText(
            Base64.base64Encode( Blowfish.makeNewKey( 8 ) ).substring( 0, 8 ) );
    }//GEN-LAST:event_pumuNewPWActionPerformed

    private void txtResponseMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtResponseMouseClicked
    {//GEN-HEADEREND:event_txtResponseMouseClicked
        if ( evt.getModifiers() == 4 ) // Right-click
        {
            jPopupMenu1.show( txtResponse, (int)evt.getPoint().getX(),
                (int)evt.getPoint().getY() - 20 );
        }
    }//GEN-LAST:event_txtResponseMouseClicked

    private void pumuPasteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pumuPasteActionPerformed
    {//GEN-HEADEREND:event_pumuPasteActionPerformed
        Transferable clipboardContent = clipboard.getContents (parent);
        if ( clipboardContent == null ) return;
        if ( clipboardContent.isDataFlavorSupported( DataFlavor.stringFlavor ) )
            try
            {
                txtResponse.setText( (String)clipboardContent.getTransferData( DataFlavor.stringFlavor ) );
            }
            catch (UnsupportedFlavorException ufx)
            {   Console.println( ufx.getMessage() ); }
            catch( IOException iox)
            {   Console.println( iox.getMessage() ); }
        jPopupMenu1.setVisible( false );
    }//GEN-LAST:event_pumuPasteActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
    {//GEN-HEADEREND:event_jButton5ActionPerformed
        response = txtResponse.getText();
        command = jButton5.getText();
        closeDialog( null );
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
    {//GEN-HEADEREND:event_jButton4ActionPerformed
        response = txtResponse.getText();
        command = jButton4.getText();
        closeDialog( null );
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        response = txtResponse.getText();
        command = jButton3.getText();
        closeDialog( null );
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        response = txtResponse.getText();
        command = jButton1.getText();
        closeDialog( null );
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        response = txtResponse.getText();
        command = jButton2.getText();
        closeDialog( null );
    }//GEN-LAST:event_jButton2ActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        jPopupMenu1.setVisible( false );
        if ( evt != null ) command = response = ""; // Dialog was canceled
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyTyped
        int KeyCode = evt.getKeyCode();
        if (KeyCode == KeyEvent.VK_ENTER)
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_KeyTyped

    
    
    
    // Setup for standard options
    private void setup( String title, String msg, int option )
    {
        setTitle( title );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)screenSize.getWidth()/2,
            ((int)screenSize.getHeight()/2));
        txtMessage.setBackground( jButton1.getBackground() );
        txtMessage.setText( msg );
        jButton1.setVisible( false );
        jButton2.setVisible( false );
        jButton3.setVisible( false );
        jButton4.setVisible( false );
        jButton5.setVisible( false );
        txtResponse.setVisible( false );
        if ( option == OK )
        {
            jButton1.setVisible( true );
            jButton1.setText( "OK" );
        }
        else if ( option == OK_CANCEL )
        {
            jButton1.setVisible( true );
            jButton1.setText( "OK" );
            jButton2.setVisible( true );
            jButton2.setText( "Cancel" );
        }
        else if ( option == YES_NO )
        {
            jButton1.setVisible( true );
            jButton1.setText( "Yes" );
            jButton2.setVisible( true );
            jButton2.setText( "No" );
        }
        pack();
        validate();
        jButton1.requestFocus();
    }

    // Setup for custom options
    private void setup
        ( String title
        , String msg
        , String defaultResponse
        , String commands[]
        )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)screenSize.getWidth()/2,
            ((int)screenSize.getHeight()/2));

        setTitle( title );
        txtMessage.setBackground( jButton1.getBackground() );
        txtMessage.setText( msg );
        if ( defaultResponse == null ) txtResponse.setVisible( false );
        else txtResponse.setText( defaultResponse );
        clipboard = getToolkit ().getSystemClipboard ();
        jButton1.setVisible( false );
        jButton2.setVisible( false );
        jButton3.setVisible( false );
        jButton4.setVisible( false );
        jButton5.setVisible( false );

        //jButton1.setVisible( true ); // NONO: there may not be ANY buttons
        //jButton1.setText( "OK" ); // just in case 
        if ( commands.length >= 1 ) { jButton1.setVisible( true ); jButton1.setText( commands[0] ); }
        if ( commands.length >= 2 ) { jButton2.setVisible( true ); jButton2.setText( commands[1] ); }
        if ( commands.length >= 3 ) { jButton3.setVisible( true ); jButton3.setText( commands[2] ); }
        if ( commands.length >= 4 ) { jButton4.setVisible( true ); jButton4.setText( commands[3] ); }
        if ( commands.length >= 5 ) { jButton5.setVisible( true ); jButton5.setText( commands[4] ); }
        pack();
        validate();
        if ( txtResponse.isVisible() ) txtResponse.requestFocus();
        else if ( jButton1.isVisible() ) jButton1.requestFocus();
    }

    public String getCommand()
    {   // This returns the label on the button that he pushed
        return command;
    }

    public String getResponse()
    {   // This returns the text he typed in the response box
        return response;
    }

    public void updateMsg( String newMsg )
    {
        txtMessage.setText( newMsg );
        //this.pack();
        int width = txtMessage.getWidth() + 16;
        this.setSize( width, this.getHeight() );
        super.doLayout();
        int xCenter = (parent.getWidth() - width) / 2;
        int yCenter = (parent.getHeight() - this.getHeight()) / 2;
        this.setLocation(xCenter, yCenter);
        //this.pack();
        //txtMessage.revalidate();
        //txtMessage.repaint();
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    /**
     * @param args the command line arguments
     */
    /***** FOR TESTING *****
    public static void main(String args[])
    {
        MsgBox msgBox;
        JFrame frame = new JFrame();
        while ( true )
        {
            msgBox = new MsgBox
                ( frame, "MsgBox Test Option 0"
                , "This is the stuff we want you to know"
                , OK
                );
            if ( msgBox.getCommand().equals( "" ) )
            {
                Console.println( "Dialog canceled." );
                System.exit( 0 );
            }
            Console.println( "He clicked " + msgBox.getCommand() );
            if ( msgBox.getCommand().equals( "OK" ) ) break;
            msgBox.dispose();
        }

        while ( true )
        {
            msgBox = new MsgBox
                ( frame, "MsgBox Test Option 1"
                , "This is some more information for you.\n" +
                    "Now you have a choice to make!"
                , OK_CANCEL
                );
            if ( msgBox.getCommand().equals( "" ) )
            {
                Console.println( "Dialog canceled." );
                System.exit( 0 );
            }
            Console.println( "He clicked " + msgBox.getCommand() );
            if ( msgBox.getCommand().equals( "OK" ) ) break;
            msgBox.dispose();
        }

        while ( true )
        {
            msgBox = new MsgBox
                ( frame, "MsgBox Test Option 2"
                , "This is the last of the standard options.\n" +
                    "This message has three lines, just for fun.\n" +
                    "Are you having fun yet?"
                , YES_NO
                );
            if ( msgBox.getCommand().equals( "" ) )
            {
                Console.println( "Dialog canceled." );
                System.exit( 0 );
            }
            Console.println( "He clicked " + msgBox.getCommand() );
            if ( msgBox.getCommand().equals( "Yes" ) ) break;
            msgBox.dispose();
        }

        msgBox = new MsgBox
            ( frame
            , "MsgBox Test - One Button Custom"
            , "This is a custom option.\nWhat is your name?"
            , "Smith"
            , new String[] { "Enter" }
            );
        if ( msgBox.getCommand().equals( "" ) )
        {
            Console.println( "Dialog canceled." );
            System.exit( 0 );
        }
        Console.println( "He entered '" + msgBox.getResponse() + "' and clicked " +
            msgBox.getCommand() );

        msgBox = new MsgBox
            ( frame
            , "MsgBox Test - Two Button Custom"
            , "This is the custom option with two choices.\nWhat is your FIRST name?"
            , "Silver"
            , new String[] { "Enter", "Cancel" }
            );
        if ( msgBox.getCommand().equals( "" ) )
        {
            Console.println( "Dialog canceled." );
            System.exit( 0 );
        }
        Console.println( "He entered '" + msgBox.getResponse() + "' and clicked " +
            msgBox.getCommand() );

        msgBox = new MsgBox
            ( frame
            , "MsgBox Test - Three Button Custom"
            , "This is the custom option with three choices.\nWhat is your MIDDLE name?"
            , ""
            , new String[] { "Enter", "None", "Cancel" }
            );
        if ( msgBox.getCommand().equals( "" ) )
        {
            Console.println( "Dialog canceled." );
            System.exit( 0 );
        }
        Console.println( "He entered '" + msgBox.getResponse() + "' and clicked " +
            msgBox.getCommand() );

        msgBox = new MsgBox
            ( frame
            , "MsgBox Test - Four Button Custom"
            , "This is the custom option with four choices\nWhat is your age?"
            , ""
            , new String[] { "Enter", "0 - 20", "21 - 40", "41 - Death" }
            );
        if ( msgBox.getCommand().equals( "" ) )
        {
            Console.println( "Dialog canceled." );
            System.exit( 0 );
        }
        Console.println( "He entered '" + msgBox.getResponse() + "' and clicked " +
            msgBox.getCommand() );

        msgBox = new MsgBox
            ( frame
            , "MsgBox Test - Five Button Custom"
            , "This is the custom option with five choices\n" +
                "And no response field ... The last one!\n" +
                "What is your sexual preference?"
            , null
            ,new String[] { "Straight", "Gay", "Lesbian", "Bi Curious", "Bi Not Curious" }
            );
        if ( msgBox.getCommand().equals( "" ) )
        {
            Console.println( "Dialog canceled." );
            System.exit( 0 );
        }
        Console.println( "He entered '" + msgBox.getResponse() + "' and clicked " +
            msgBox.getCommand() );

        System.exit( 0 );
    }
    /***** END OF TEST CODE *****/

//    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable transferable)
//    {
//    }
//
    public final static int OK = 0;
    public final static int OK_CANCEL = 1;
    public final static int YES_NO = 2;
    public final static int NO_BUTTONS = 3;
    JFrame parent;
    String command;
    String response;
    java.awt.datatransfer.Clipboard clipboard;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private volatile javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JMenuItem pumuNewPW;
    private javax.swing.JMenuItem pumuPaste;
    private javax.swing.JTextArea txtMessage;
    private javax.swing.JTextField txtResponse;
    // End of variables declaration//GEN-END:variables
}

