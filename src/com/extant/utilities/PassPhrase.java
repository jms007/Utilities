/*
 * PassPhrase.java
 *
 * Created on March 13, 2003, 1:41 AM
 * Presents a dialog (with optional instructions) to enter a passphrase.
 * By right-clicking on the passphrase text box, the user may
 *   a) Paste a passphrase from the System Clipboard,
 *   b) Generate a new random passphrase (8 bytes), or
 *   c) Copy the passphrase to the System Clipboard.
 */

package com.extant.utilities;
import java.io.IOException;
import java.awt.datatransfer.*;
import java.awt.Frame;

/**
 *
 * @author  jms
 */
public class PassPhrase
extends javax.swing.JDialog
implements ClipboardOwner  
{
    public PassPhrase(Frame parent, boolean modal)
    {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        jLabel2.setVisible( false );
        clipboard = getToolkit ().getSystemClipboard ();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        pumuCopy = new javax.swing.JMenuItem();
        pumuPaste = new javax.swing.JMenuItem();
        pumuNewPW = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        txtPassPhrase = new javax.swing.JPasswordField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        pumuCopy.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pumuCopy.setText("Copy");
        pumuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuCopyActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pumuCopy);

        pumuPaste.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pumuPaste.setText("Paste");
        pumuPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuPasteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pumuPaste);

        pumuNewPW.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pumuNewPW.setText("New Random Passphrase");
        pumuNewPW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pumuNewPWActionPerformed(evt);
            }
        });
        jPopupMenu1.add(pumuNewPW);

        setTitle("Passphrase Entry");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.FlowLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setText("Enter Passphrase for");
        getContentPane().add(jLabel1);

        lblMessage.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMessage.setPreferredSize(new java.awt.Dimension(200, 16));
        getContentPane().add(lblMessage);

        txtPassPhrase.setPreferredSize(new java.awt.Dimension(200, 20));
        txtPassPhrase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPassPhraseMouseClicked(evt);
            }
        });
        getContentPane().add(txtPassPhrase);

        btnOK.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnOK.setText("OK");
        btnOK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOK.setPreferredSize(new java.awt.Dimension(50, 22));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        getContentPane().add(btnOK);

        btnCancel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel);

        jLabel2.setText("Passphrase Copied to Clipboard");
        getContentPane().add(jLabel2);

        setSize(new java.awt.Dimension(247, 205));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pumuCopyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pumuCopyActionPerformed
    {//GEN-HEADEREND:event_pumuCopyActionPerformed
        String key = new String( txtPassPhrase.getPassword() );
        ClipboardContents clipboardContents = new ClipboardContents( key );
        clipboard.setContents( clipboardContents, (ClipboardOwner)this );
        jLabel2.setVisible( true );
    }//GEN-LAST:event_pumuCopyActionPerformed

    private void pumuNewPWActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pumuNewPWActionPerformed
    {//GEN-HEADEREND:event_pumuNewPWActionPerformed
        txtPassPhrase.setText(
            Base64.base64Encode( Blowfish.makeNewKey( 8 ) ).substring( 0, 8 ) );
        pumuCopyActionPerformed( null );
    }//GEN-LAST:event_pumuNewPWActionPerformed

    private void pumuPasteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pumuPasteActionPerformed
    {//GEN-HEADEREND:event_pumuPasteActionPerformed
        Transferable clipboardContent = clipboard.getContents (parent);
        if ( clipboardContent == null ) return;
        if ( clipboardContent.isDataFlavorSupported( DataFlavor.stringFlavor ) )
            try
            {
                txtPassPhrase.setText( (String)clipboardContent.getTransferData( DataFlavor.stringFlavor ) );
            }
            catch (UnsupportedFlavorException ufx)
            {
                Console.println( ufx.getMessage() );
            }
            catch( IOException iox)
            {
                Console.println( iox.getMessage() );
            }
        //jPopupMenu1.setVisible( false );
    }//GEN-LAST:event_pumuPasteActionPerformed

    private void txtPassPhraseMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_txtPassPhraseMouseClicked
    {//GEN-HEADEREND:event_txtPassPhraseMouseClicked
        if ( evt.getModifiers() == 4 ) // Right-click
            jPopupMenu1.show( txtPassPhrase, (int)evt.getPoint().getX(),
                (int)evt.getPoint().getY() - 20 );
    }//GEN-LAST:event_txtPassPhraseMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        cancelled = true;
        closeDialog( null );
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        cancelled = false;
        passphrase = new String( txtPassPhrase.getPassword() );
        closeDialog( null );
    }//GEN-LAST:event_btnOKActionPerformed

    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        if ( evt != null ) cancelled = true;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    public void setMessage( String message )
    {
        lblMessage.setText( message );
    }

    public String getPassphrase()
    {
        return passphrase;
    }

    public boolean wasCancelled()
    {
        return cancelled;
    }

    public void lostOwnership(java.awt.datatransfer.Clipboard clipboard, java.awt.datatransfer.Transferable transferable)
    { // It means anything we have put in the clipboard has been overwritten by somebody else
    }

    /***** FOR TESTING *****/
    public static void main(String args[])
    {
        PassPhrase passPhrase = new PassPhrase(new javax.swing.JFrame(), true);
        passPhrase.setMessage( "Tell me your secret" );
        passPhrase.setVisible( true );
        if ( passPhrase.wasCancelled() ) Console.println( "Dialog was cancelled." );
        else Console.println( "Passphrase=" + passPhrase.getPassphrase() );
        System.exit( 0 );
    }
    /***** END OF TEST *****/

    private Frame parent;
    private String passphrase;
    private boolean cancelled;
    java.awt.datatransfer.Clipboard clipboard;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JMenuItem pumuCopy;
    private javax.swing.JMenuItem pumuNewPW;
    private javax.swing.JMenuItem pumuPaste;
    private javax.swing.JPasswordField txtPassPhrase;
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

