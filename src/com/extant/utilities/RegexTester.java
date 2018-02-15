/*
 * RegexTester.java
 *
 * Created on October 9, 2006, 10:32 PM
 */

package com.extant.utilities;
import java.io.IOException;
import java.util.regex.*;
import java.awt.Color;
import java.awt.datatransfer.*;
import javax.swing.JTextField;
import java.awt.datatransfer.*;

/**
 *
 * @author  jms
 */
public class RegexTester extends javax.swing.JFrame
implements ClipboardOwner  
{
    public RegexTester()
    {
        initComponents();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize( 450, 300 );
        setLocation((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2);
        clipboard = getToolkit ().getSystemClipboard ();
        txtResult.setVisible( false );
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pumu = new javax.swing.JPopupMenu();
        pumuCopy = new javax.swing.JMenuItem();
        pumuPaste = new javax.swing.JMenuItem();
        txtPattern = new javax.swing.JTextField();
        txtTest = new javax.swing.JTextField();
        txtResult = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        txtJavaString = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        pumuCopy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pumuCopy.setText("Copy");
        pumuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuCopyActionPerformed(evt);
            }
        });
        pumu.add(pumuCopy);

        pumuPaste.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pumuPaste.setText("Paste");
        pumuPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuPasteActionPerformed(evt);
            }
        });
        pumu.add(pumuPaste);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Regex Tester");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPattern.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPattern.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPatternKeyTyped(evt);
            }
        });
        txtPattern.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPatternMouseClicked(evt);
            }
        });
        getContentPane().add(txtPattern, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 260, -1));

        txtTest.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtTest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTestKeyTyped(evt);
            }
        });
        txtTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTestMouseClicked(evt);
            }
        });
        getContentPane().add(txtTest, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 260, -1));

        txtResult.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtResult.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtResult.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(txtResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 180, 20));

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        getContentPane().add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, -1, -1));

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, -1, -1));

        txtJavaString.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtJavaString.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtJavaStringMouseClicked(evt);
            }
        });
        getContentPane().add(txtJavaString, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 260, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Pattern:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 80, 20));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Java String:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 80, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Test String:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 80, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtJavaStringMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtJavaStringMouseClicked
        processMouseClick( evt );
    }//GEN-LAST:event_txtJavaStringMouseClicked

    private void txtTestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTestMouseClicked
        processMouseClick( evt );
    }//GEN-LAST:event_txtTestMouseClicked

    private void txtPatternMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPatternMouseClicked
        processMouseClick( evt );
    }//GEN-LAST:event_txtPatternMouseClicked

    private void txtPatternKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPatternKeyTyped
        txtResult.setVisible( false );
        char c = evt.getKeyChar();
        if ( Strings.isPrintable(c) ) txtJavaString.setText( computeJavaString( txtPattern.getText() + c ) );
        else txtJavaString.setText( computeJavaString( txtPattern.getText() ) );
        if ( c == evt.VK_ENTER )
            doit();
    }//GEN-LAST:event_txtPatternKeyTyped

    private void pumuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pumuCopyActionPerformed
        ClipboardContents clipboardContents = new ClipboardContents( pumuField.getSelectedText() );
        clipboard.setContents( clipboardContents, (ClipboardOwner)this );
        pumu.setVisible( false );
    }//GEN-LAST:event_pumuCopyActionPerformed

    private void pumuPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pumuPasteActionPerformed
        String pasteText="";
        Transferable clipboardContent = clipboard.getContents (this);
        if ( clipboardContent == null ) return;
        if ( clipboardContent.isDataFlavorSupported( DataFlavor.stringFlavor ) )
        {
            try { pasteText = (String)clipboardContent.getTransferData( DataFlavor.stringFlavor ); }
            catch (UnsupportedFlavorException ufx)
            { Console.println( ufx.getMessage() ); }
            catch( IOException iox)
            { Console.println( iox.getMessage() ); }
        }
        if ( pumuField == txtPattern || pumuField == txtTest )
        {
            String newText = pumuField.getText().substring( 0, pumuField.getCaretPosition() ) +
                pasteText +
                pumuField.getText().substring( pumuField.getCaretPosition() );
            pumuField.setText( newText );
        }
        pumu.setVisible( false );
    }//GEN-LAST:event_pumuPasteActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
        System.exit( 0 );
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        doit();
    }//GEN-LAST:event_btnOKActionPerformed

    private void txtTestKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTestKeyTyped
        txtResult.setVisible( false );
        if (evt.getKeyChar() == evt.VK_ENTER)
            doit();
    }//GEN-LAST:event_txtTestKeyTyped

    void processMouseClick( java.awt.event.MouseEvent evt )
    {
        JTextField source = (JTextField)evt.getSource();
        if ( evt.getButton() == 1 )
        {
            pumu.setVisible( false );
        }
        if ( evt.getButton() == 3 )
        {
            pumu.setLocation( this.getX()+source.getX()+evt.getX(), this.getY()+source.getY()+evt.getY()-20 );
            //if ( source == txtJavaString ) pumuPaste.setEnabled( false );
            pumuPaste.setEnabled( source != txtJavaString );
            pumu.setVisible( true );
            pumuField = source;
        }
    }

    void doit()
    {
        if ( txtPattern.getText().equals("") ) return;
        txtJavaString.setText( computeJavaString( txtPattern.getText() ) );
        boolean match = Pattern.matches( txtPattern.getText(), txtTest.getText() );
        if ( match )
        {
            txtResult.setText( "Match!" );
            txtResult.setBackground( Color.GREEN );
        }
        else
        {
            txtResult.setText( "No Match" );
            txtResult.setBackground( Color.RED );
        }
        txtResult.setVisible( true );
    }

    String computeJavaString( String s )
    {
        String js = s;
        js = s.replace( "\\", "\\\\" );
        return "\"" + js + "\"";
    }

    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable transferable)
    { // It means that anything we may have put in the clipboard has been overwritten by somebody else
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegexTester().setVisible(true);
            }
        });
    }

    java.awt.datatransfer.Clipboard clipboard;
    JTextField pumuField;

    // A small library of potentially useful match patterns:
    public static final String regexFileName = "[a-zA-Z0-9_]+\\.[a-zA-Z0-9_]+";           // DOS File Names
    public static final String regexMAC = "([0-9a-fA-F]{2}\\-){5}[0-9a-fA-F]{2}";         // MAC addresses
    public static final String regexAcctNo_1 = "(\\[\\d*\\])|(\\[\\d*/[a-zA-Z0-9]*\\])";  // Account numbers with brackets
    public static final String regexAcctNo_2 = "(\\d*)|(\\d*/[a-zA-Z0-9]*)";              // Account numbers, no brackets

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu pumu;
    private javax.swing.JMenuItem pumuCopy;
    private javax.swing.JMenuItem pumuPaste;
    private javax.swing.JTextField txtJavaString;
    private javax.swing.JTextField txtPattern;
    private javax.swing.JLabel txtResult;
    private javax.swing.JTextField txtTest;
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

        public Object getTransferData( DataFlavor dataFlavor )
        throws UnsupportedFlavorException, java.io.IOException
        {
            return contents;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[] { DataFlavor.stringFlavor };
        }

        public boolean isDataFlavorSupported( DataFlavor dataFlavor )
        {
            return dataFlavor == DataFlavor.stringFlavor;
        }

        private String contents;
    }

}

