/*
 * EditProperties.java
 *
 * Created on March 27, 2005, 3:14 PM
 */

package com.extant.utilities;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.Font;

/**
 *
 * @author  jms
 */
public class EditProperties
extends javax.swing.JFrame
{
    public EditProperties( XProperties props, LogFile logger )
    {
        this( props, "", logger );
    }

    public EditProperties( XProperties props, String section, LogFile logger )
    {
        this.props = props;
        this.section = section;
        if ( logger != null ) this.logger = logger;
        else this.logger = new LogFile();
        initComponents();
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        northPanel = new javax.swing.JPanel();
        topBorder = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        comboKeys = new javax.swing.JComboBox();
        txtNewKey = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lblType = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        bottomBorder = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnCancelNew = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        statusBar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        northPanel.setLayout(new javax.swing.BoxLayout(northPanel, javax.swing.BoxLayout.Y_AXIS));

        topBorder.setPreferredSize(new java.awt.Dimension(10, 20));
        northPanel.add(topBorder);

        jPanel1.setMinimumSize(new java.awt.Dimension(300, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(1259, 100));

        comboKeys.setMaximumSize(new java.awt.Dimension(400, 32767));
        comboKeys.setPreferredSize(new java.awt.Dimension(400, 22));
        comboKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboKeysActionPerformed(evt);
            }
        });
        jPanel1.add(comboKeys);

        txtNewKey.setMaximumSize(new java.awt.Dimension(400, 2147483647));
        txtNewKey.setPreferredSize(new java.awt.Dimension(400, 19));
        jPanel1.add(txtNewKey);

        lblType.setText("Type");
        jPanel2.add(lblType);

        txtValue.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        txtValue.setMinimumSize(new java.awt.Dimension(400, 19));
        txtValue.setPreferredSize(new java.awt.Dimension(400, 19));
        txtValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtValueKeyTyped(evt);
            }
        });
        jPanel2.add(txtValue);

        jPanel1.add(jPanel2);

        northPanel.add(jPanel1);
        northPanel.add(bottomBorder);

        getContentPane().add(northPanel, java.awt.BorderLayout.NORTH);

        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel, javax.swing.BoxLayout.Y_AXIS));

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel3.add(btnDelete);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(btnUpdate);

        btnSave.setText("Save Changes");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel3.add(btnSave);

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel3.add(btnClose);

        buttonPanel.add(jPanel3);

        btnNew.setText("New Property ...");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jPanel4.add(btnNew);

        btnAdd.setText("Add New");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel4.add(btnAdd);

        btnCancelNew.setText("Cancel New");
        btnCancelNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelNewActionPerformed(evt);
            }
        });
        jPanel4.add(btnCancelNew);

        buttonPanel.add(jPanel4);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.CENTER);

        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        statusBar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        statusBar.setText("Status Bar");
        statusBar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        statusPanel.add(statusBar);

        getContentPane().add(statusPanel, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(516, 311));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelNewActionPerformed
        txtNewKey.setVisible( false );
        txtValue.setText( "" );
        lblType.setText( "" );
        manageButtons();
    }//GEN-LAST:event_btnCancelNewActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if ( props.containsKey( txtNewKey.getText()))
        {
            statusBar.setText( "This key is already defined");
            return;
        }
        if ( txtValue.getText().equals( "" ))
        {
            statusBar.setText( "No Value was entered" );
            return;
        }
        currentKey = txtNewKey.getText();
        props.setProperty( currentKey, txtValue.getText());
        hasChanged = true;
        comboKeys.addItem( currentKey );
        comboKeys.setSelectedItem( currentKey );
        txtNewKey.setVisible( false );
        manageButtons();
        statusBar.setText( "Key " + currentKey + " has been entered.");
        txtNewKey.setText( "" );
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try
        {
            props.store( "[EditProperties]" );
            String msg = "Properties saved to " + props.propertiesFilename;
            statusBar.setText( msg );
            logger.log( logger.MAJOR_INFO, msg );
            hasChanged = false;
            manageButtons();
        }
        catch (IOException iox)
        {
            statusBar.setText( iox.getMessage() );
            logger.log( logger.FATAL_ERROR, iox.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        formWindowClosing( null );
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        txtNewKey.setVisible( true );
        txtValue.setText( "" );
        lblType.setText( "String Value" );
        txtNewKey.requestFocus();
        manageButtons();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        props.remove( currentKey );
        hasChanged = true;
        String status = new String( "Key " + currentKey + " has been removed." );
        comboKeys.removeItem( currentKey ); // changes currentKey
        txtValue.setText( props.getString( currentKey ) );
        lblType.setText( props.getClass( currentKey ) + " Value");
        manageButtons();
        statusBar.setText( status );
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if ( hasChanged )
        {
            MsgBox msgBox = new MsgBox( this, "Unsaved Changes",
                "One or more changes have not been saved\nDo you want to discard these changes?",
                MsgBox.YES_NO );
            if ( !msgBox.getCommand().equalsIgnoreCase( "yes" ) ) return;
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if ( props.getString( currentKey).equals( txtValue.getText()))
        {
            statusBar.setText( "Value was not changed - Update ignored.");
            return;
        }
        try
        {
            String type = props.getClass( currentKey );
            if ( type.endsWith( "String" ) )
                props.setProperty( currentKey, txtValue.getText() );
            else if ( type.endsWith( "Point" ) ) ; //!!
            else if ( type.endsWith( "int" ) )
                props.setProperty( currentKey, Integer.parseInt( txtValue.getText() ) );
            else if ( type.endsWith( "double" ) )
                props.setProperty( currentKey, Double.parseDouble( txtValue.getText() ) );
            else if ( type.endsWith( "Dimension" ) ) ; //!!
        }
        catch (NumberFormatException nfx)
        {
            statusBar.setText( "Invalid format " + nfx.getMessage() );
            txtValue.requestFocus();
            return;
        }
        hasChanged = true;
        statusBar.setText( "Key " + currentKey + " has been changed." );
        manageButtons();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void txtValueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtValueKeyTyped
        manageButtons();
    }//GEN-LAST:event_txtValueKeyTyped

    private void comboKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboKeysActionPerformed
        if ( initializing ) return;
        if ( evt.getActionCommand().equals( "comboBoxChanged") )
        {
            currentKey = (String)comboKeys.getSelectedItem();
            txtValue.setText( props.getString( currentKey ) );
            lblType.setText( props.getClass( currentKey ) + " Value");
            manageButtons();
            statusBar.setText( "" );
        }
    }//GEN-LAST:event_comboKeysActionPerformed

    private void setup()
    {
        initializing = true;
        fixFonts();
        setTitle( "Properties Editor " + props.propertiesFilename );
        Enumeration en = props.keys();
        Vector <String> keys = new Vector <String> ();
        while ( en.hasMoreElements() )
        {
            String key = (String)en.nextElement();
            if ( section.equals( "" ) ) keys.addElement( key );
            else if ( key.startsWith( section + "." ) )
                keys.addElement( key.substring( key.indexOf( "." ) + 1 ) );
        }
        int ptr[] = Sorts.sort( keys );
        for (int i=0; i<keys.size(); ++i)
            comboKeys.addItem( (String)keys.elementAt( ptr[i] ) );
        comboKeys.setSelectedIndex( -1 );
        hasChanged = false;
        initializing = false;
        txtNewKey.setVisible( false );
        manageButtons();
        statusBar.setText( "" );
    }

    private void fixFonts()
    {
        Font ourFont = new java.awt.Font( "Arial", Font.PLAIN, 12 );
        comboKeys.setFont( ourFont );
        lblType.setFont( ourFont );
    }

    public void setSelectedItem( String item )
    {
        comboKeys.setSelectedItem( item );
    }

    private void manageButtons()
    {
        if ( txtNewKey.isVisible() )
        {
            btnAdd.setEnabled( txtNewKey.getText().length() > 0
                && txtValue.getText().length() > 0 );
            btnCancelNew.setEnabled( true );
            btnUpdate.setEnabled( false );
            btnSave.setEnabled( false );
            btnClose.setEnabled( false );
            btnDelete.setEnabled( false );
            btnSave.setEnabled( false );
            comboKeys.setEnabled( false );
            statusBar.setText( "Enter new key and value" );
        }
        else
        {
            comboKeys.setEnabled( true );
            btnNew.setEnabled( true );
            btnAdd.setEnabled( false );
            btnCancelNew.setEnabled( false );
            btnClose.setEnabled( true );
            btnDelete.setEnabled( comboKeys.getSelectedIndex() >= 0 );
            btnSave.setEnabled( hasChanged );
            //!! When coming from txtValue events, this doesn't work
            //   the character just typed is not yet in the text field
            //btnUpdate.setEnabled( (comboKeys.getSelectedIndex() >= 0)
            //    && !(xProp.getString( (String)comboKeys.getSelectedItem() ).equals( txtValue.getText() )));
            btnUpdate.setEnabled( comboKeys.getSelectedIndex() >= 0 );
        }
    }

    /**
     * @param args the command line arguments
     * file=properties-filename
     * section=section-name
     */
    /***** FOR TESTING *****/
    public static void main(String args[])
    {
        try
        {
            Clip clip = new Clip( args, new String[]
            { "file=E:\\Projects\\extant.properties"
            , "section=''"
            } );
            final XProperties props = new XProperties( clip.getParam( "file" ) );
            String sec = clip.getParam( "section" );
            final String section = sec;
            java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run()
                { EditProperties editProperties = new EditProperties
                    ( props
                    , section
                    , new LogFile()
                    );
                  editProperties.setVisible(true);
                }
            });
        }
        catch (IOException iox) { Console.println( iox.getMessage() ); }
        catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
    }

    String section;
    LogFile logger;
    XProperties props;
    boolean initializing;
    String currentKey;
    boolean hasChanged;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomBorder;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancelNew;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox comboKeys;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblType;
    private javax.swing.JPanel northPanel;
    private javax.swing.JLabel statusBar;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel topBorder;
    private javax.swing.JTextField txtNewKey;
    private javax.swing.JTextField txtValue;
    // End of variables declaration//GEN-END:variables
}

