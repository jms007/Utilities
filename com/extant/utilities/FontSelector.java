/*
 * FontSelector.java
 *
 * Created on February 16, 2005, 10:19 AM
 */

package com.extant.utilities;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.print.*;

/**
 *
 * @author  jms
 */
public class FontSelector
extends javax.swing.JFrame
implements Printable
{
    public FontSelector()
    {
        initComponents();
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblSample = new javax.swing.JLabel();
        comboFamily = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        comboStyle = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtSize = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSample = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        lblNSamples = new javax.swing.JLabel();

        setTitle("Font Selector");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sample of this Font", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N

        lblSample.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSample.setText("Sample");
        jPanel1.add(lblSample);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 460, 50));

        comboFamily.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        comboFamily.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboFamilyItemStateChanged(evt);
            }
        });
        getContentPane().add(comboFamily, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 150, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Font Family");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 150, -1));

        comboStyle.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        comboStyle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboStyleItemStateChanged(evt);
            }
        });
        getContentPane().add(comboStyle, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 130, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Style");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 130, -1));

        txtSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSizeKeyTyped(evt);
            }
        });
        getContentPane().add(txtSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 60, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Size");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 60, -1));

        txtSample.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSampleKeyTyped(evt);
            }
        });
        getContentPane().add(txtSample, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 450, -1));

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Sample Text");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 450, -1));

        btnPrint.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnPrint.setText("Print Samples");
        btnPrint.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, -1, -1));

        btnClose.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 50, -1));

        btnAdd.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnAdd.setText("Add to Samples");
        btnAdd.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, -1, -1));

        lblNSamples.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblNSamples.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblNSamples, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 90, -1));

        setSize(new java.awt.Dimension(527, 369));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        addToSamples();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPrintActionPerformed
    {//GEN-HEADEREND:event_btnPrintActionPerformed
        try
        {
            pj = PrinterJob.getPrinterJob();
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            Paper paper = new Paper();
            paper.setImageableArea( 18.0, 36.0, 576.0, 756.0 );
            pageFormat.setPaper( paper );
//Console.println( "[CheckPrinter.printCheck] ImageableX=" + pageFormat.getImageableX()
//    + "   ImageableY=" + pageFormat.getImageableY() );
            book.append( this, pageFormat, 1 );
            pj.setPageable( book );
            pj.print();
            nSamples = 0;
            update();
        }
        catch (PrinterException px) { Console.println( px.getMessage() ); }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        exitForm( null );
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtSampleKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSampleKeyTyped
    {//GEN-HEADEREND:event_txtSampleKeyTyped
        if ( evt.getKeyChar() == evt.VK_ENTER ) update();
    }//GEN-LAST:event_txtSampleKeyTyped

    private void txtSizeKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtSizeKeyTyped
    {//GEN-HEADEREND:event_txtSizeKeyTyped
        if ( evt.getKeyChar() == evt.VK_ENTER ) update();
    }//GEN-LAST:event_txtSizeKeyTyped

    private void comboStyleItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_comboStyleItemStateChanged
    {//GEN-HEADEREND:event_comboStyleItemStateChanged
        update();
    }//GEN-LAST:event_comboStyleItemStateChanged

    private void comboFamilyItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_comboFamilyItemStateChanged
    {//GEN-HEADEREND:event_comboFamilyItemStateChanged
        update();
    }//GEN-LAST:event_comboFamilyItemStateChanged
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void setup()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String families[] = ge.getAvailableFontFamilyNames();
        for (int i=0; i<families.length; ++i)
            comboFamily.addItem( families[i] );
        comboStyle.addItem( "PLAIN" );  // == 0
        comboStyle.addItem( "BOLD" );   // == 1
        comboStyle.addItem( "ITALIC" ); // == 2

//        Console.println( "PLAIN=" + Font.PLAIN );
//        Console.println( "BOLD=" + Font.BOLD );
//        Console.println( "ITALIC=" + Font.ITALIC );
        
        Graphics graphics = this.getGraphics();
        standardFont = graphics.getFont();
//        Console.println( "family=" + standardFont.getFamily() );
//        Console.println( "style=" + standardFont.getStyle() );
//        Console.println( "size=" + standardFont.getSize() );
        comboFamily.setSelectedIndex( -1 );
        String family = standardFont.getFamily();
        for (int i=0; i<families.length; ++i)
            if ( families[i].equalsIgnoreCase( family ) )
            {
                comboFamily.setSelectedIndex( i );
                break;
            }
        nSamples = 0;

        txtSample.setText( sample );
        comboStyle.setSelectedItem( styleName( standardFont.getStyle() ) );
        txtSize.setText( Strings.format( standardFont.getSize(), "0" ) );
        update();
    }

    private void update()
    {
        currentFont = new Font( (String)comboFamily.getSelectedItem(),
            comboStyle.getSelectedIndex(), Strings.parseInt( txtSize.getText() ) );
        lblSample.setFont( currentFont );
        lblSample.setText( txtSample.getText() );
        btnPrint.setEnabled( nSamples > 0 );
        btnAdd.setEnabled( nSamples < sampleFonts.length );
        if ( nSamples > 0 ) lblNSamples.setText( "(" + nSamples + ")" );
        else lblNSamples.setText( "" );
    }

    private void addToSamples()
    {
        sampleFonts[nSamples] = currentFont;
        sampleText[nSamples] = txtSample.getText();
        ++nSamples;
        update();
    }

    public int print( Graphics g, PageFormat pageFormat, int nPages )
    throws java.awt.print.PrinterException
    {
        int x = 50;
        int y = 50;
        for (int i=0; i<nSamples; ++i)
        {
            g.setFont( standardFont );
            g.drawString( sampleFonts[i].getFamily() + 
                " " + styleName( sampleFonts[i].getStyle() ) +
                " " + sampleFonts[i].getSize(), x, y );
            y += 20;
            g.setFont( sampleFonts[i] );
            g.drawString( sampleText[i], x, y );
            y += sampleFonts[i].getSize() + 10;
        }
        return nPages;
    }

    public static String styleName( int style )
    {
        if ( style == Font.PLAIN ) return "PLAIN ";
        if ( style == Font.BOLD ) return "BOLD ";
        if ( style == Font.ITALIC ) return "ITALIC ";
        return "???";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        new FontSelector().setVisible( true );
    }

    Font standardFont;
    String sample = "The quick brown fox jumps over the lazy dog.";
    Font currentFont;
    PrinterJob pj;
    int nSamples;
    Font sampleFonts[] = new Font[10];
    String sampleText[] = new String[10];

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrint;
    private javax.swing.JComboBox comboFamily;
    private javax.swing.JComboBox comboStyle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblNSamples;
    private javax.swing.JLabel lblSample;
    private javax.swing.JTextField txtSample;
    private javax.swing.JTextField txtSize;
    // End of variables declaration//GEN-END:variables
}

