/*
 * SelectFromList.java
 * This dialog is used by EnterTransaction to select the payee for a check and also
 * by Invoice to select the customer for an invoice.
 * Since the formats of the vendor file and the customer file are identical,
 * you can substitute 'customer' wherever you see 'vendor' in this code.  'payee'
 * is also synonomous with 'vendor'.
 *
 * Created on May 18, 2003, 9:04 PM
 */

package com.extant.utilities;
import java.io.IOException;
import javax.swing.JFrame;
import java.util.StringTokenizer;
import java.util.Vector;
/**
 *
 * @author  jms
 */
public class SelectFromList
extends javax.swing.JDialog
{
    public SelectFromList(JFrame parent, boolean modal, String fileName)
        throws IOException
    {
        super(parent, modal);
        initializing = true;
        initComponents();
        this.fileName = fileName;
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboEntry = new javax.swing.JComboBox();
        txtLine0 = new javax.swing.JTextField();
        txtLine1 = new javax.swing.JTextField();
        txtLine2 = new javax.swing.JTextField();
        txtLine3 = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblBuffer = new javax.swing.JLabel();

        setTitle("Select Vendor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        comboEntry.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        comboEntry.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboEntryFocusGained(evt);
            }
        });
        comboEntry.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboEntryItemStateChanged(evt);
            }
        });
        comboEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                comboEntryKeyTyped(evt);
            }
        });
        getContentPane().add(comboEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 340, -1));
        getContentPane().add(txtLine0, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 340, -1));
        getContentPane().add(txtLine1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 340, -1));
        getContentPane().add(txtLine2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 340, -1));
        getContentPane().add(txtLine3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 340, -1));

        btnOK.setText("OK");
        btnOK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        getContentPane().add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 50, -1));

        btnCancel.setText("Cancel");
        btnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, -1, -1));

        lblBuffer.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        getContentPane().add(lblBuffer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        setSize(new java.awt.Dimension(408, 334));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        cancelled = false;
        closeDialog( null );
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        selectedIndex = -1;
        cancelled = true;
        closeDialog( null );
    }//GEN-LAST:event_btnCancelActionPerformed

    private void comboEntryItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_comboEntryItemStateChanged
    {//GEN-HEADEREND:event_comboEntryItemStateChanged
        if ( initializing ) return;
        if ( evt.getStateChange() != evt.SELECTED ) return;
Console.println( "item selected  combo index=" + comboEntry.getSelectedIndex() );
        selectedIndex = entryP[comboEntry.getSelectedIndex()];
//Console.println( "selectedIndex = " + selectedIndex + " (" + vendorNames.elementAt( selectedIndex ) + ")" );
        String lines[] = (String[])entries.elementAt( selectedIndex );
        txtLine0.setText( lines[0] );
        txtLine1.setText( lines[1] );
        txtLine2.setText( lines[2] );
        txtLine3.setText( lines[3] );
//        txtVendorNo.setText( (String)vendorNumbers.elementAt( selectedIndex ) );
    }//GEN-LAST:event_comboEntryItemStateChanged

    private void comboEntryKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_comboEntryKeyTyped
    {//GEN-HEADEREND:event_comboEntryKeyTyped
        int i;
        if ( evt.getKeyChar() == evt.VK_ESCAPE )
            comboBuffer = new StringBuffer();
        else if ( evt.getKeyChar() == evt.VK_BACK_SPACE ) // '\b'
        {
            if ( comboBuffer.length() > 0 )
                comboBuffer.deleteCharAt( comboBuffer.length() - 1 );
        }
        else if ( evt.getKeyChar() == evt.VK_SPACE && comboBuffer.length() == 0 )
            ; // Don't allow leading blanks in the buffer
        else comboBuffer.append( evt.getKeyChar() );
        lblBuffer.setText( comboBuffer.toString() );
        if ( comboBuffer.toString().length() > 0 )
        {
            String cb = comboBuffer.toString();
            for (i=0; i<entries.size(); ++i)
                if ( ((String)comboEntry.getItemAt( i )).toLowerCase().startsWith( cb.toLowerCase() ) ) break;
            if ( i >= entries.size() ) i = 0;
        }
        else i = 0;
        comboEntry.setSelectedIndex( i );
    }//GEN-LAST:event_comboEntryKeyTyped

    private void comboEntryFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_comboEntryFocusGained
    {//GEN-HEADEREND:event_comboEntryFocusGained
        comboBuffer = new StringBuffer();
        lblBuffer.setText( "" );
    }//GEN-LAST:event_comboEntryFocusGained

    /* Close the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        if ( evt != null ) cancelled = true; // He clicked the X
        dispose();
    }//GEN-LAST:event_closeDialog

    public void setup()
    throws IOException
    {
        if ( !initialized )
        {
            String image;
            entries = new Vector <String[]> ( 50, 50 );
            UsefulFile entryFile = new UsefulFile( fileName );
            while ( !entryFile.EOF() )
            {
                image = entryFile.readLine( UsefulFile.ALL_WHITE );
                //if ( image.length() <= 3 ) continue;
                //if ( image.startsWith( "*" ) ) continue;
                String lines[] = new String[4];
                for (int i=0; i<lines.length; ++i) lines[i] = "";
                StringTokenizer st = new StringTokenizer( image, "|" );
                int i = 0;
                while ( st.hasMoreElements() )
                {
                    String line = ((String)st.nextElement()).trim();
                    lines[i++] = line;
                }
                entries.addElement( lines );
            }
            entryFile.close();
            entryP = Sorts.sort( entries );
            initialized = true;
        }
        comboEntry.removeAllItems();
        for (int i=0; i<entryP.length; ++i)
            comboEntry.addItem( entries.elementAt( entryP[i] ) );
        comboBuffer = new StringBuffer();
        initializing = false;
        comboEntry.setSelectedIndex( 0 );
    }

    public void clearForm()
    {
        txtLine0.setText( "" );
        txtLine1.setText( "" );
        txtLine2.setText( "" );
        txtLine3.setText( "" );
        comboEntry.setSelectedIndex( 0 );
//        txtVendorNo.setText( (String)vendorNumbers.elementAt(0) );
        comboBuffer = new StringBuffer();
        lblBuffer.setText( "" );
        comboEntry.requestFocus();
    }

    public String[] getSelectedVendor() { return getSelectedEntry(); }
    public String[] getSelectedCustomer() { return getSelectedEntry(); }
    public String[] getSelectedContact() { return getSelectedEntry(); }
    public String[] getSelectedEntry()
    {
        if ( cancelled ) return null;
        String[] entry = new String[4];
        entry[0] = txtLine0.getText();
        entry[1] = txtLine1.getText();
        entry[2] = txtLine2.getText();
        entry[3] = txtLine3.getText();
        return entry;
    }

    /***** FOR TESTING *****/
    public static void main(String args[])
    {
        try
        {
            SelectFromList selectVendor = new SelectFromList( new JFrame(), true, 
                "C:\\Users\\jms\\OneDrive\\ACCOUNTING\\EXTANT\\GL17\\VENDOR.LST" );
            selectVendor.setVisible( true );
            String[] lines = selectVendor.getSelectedVendor();
            if ( lines == null ) Console.println( "Vendor select was canceled." );
            else for (int i=0; i<lines.length; ++i) Console.println( lines[i] );
        }
        catch (IOException iox)
        {
            Console.println( iox.getMessage() );
            System.exit(1);
        }
        System.exit(0);
    }
    /***** END OF TEST *****/

    String fileName;
    Vector <String[]> entries;
    int[] entryP;
    boolean initialized = false;
    boolean initializing;
    int selectedIndex;
    StringBuffer comboBuffer;
    boolean cancelled;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox comboEntry;
    private javax.swing.JLabel lblBuffer;
    private javax.swing.JTextField txtLine0;
    private javax.swing.JTextField txtLine1;
    private javax.swing.JTextField txtLine2;
    private javax.swing.JTextField txtLine3;
    // End of variables declaration//GEN-END:variables
}

