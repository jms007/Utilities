/*
 * ViewFile.java
 *
 * Created on February 15, 2005, 11:44 AM
 */

package com.extant.utilities;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author jms
 */
public class ViewFile extends javax.swing.JFrame
{
	/**
	 * @wbp.parser.constructor
	 */
	public ViewFile(String fileName, LogFile logger) throws UtilitiesException
	{
		this(fileName, logger, false);
	}

	public ViewFile(String fileName, LogFile logger, boolean standalone) throws UtilitiesException
	{
		this.fileName = fileName;
		if (logger != null)
			this.logger = logger;
		else
			this.logger = new LogFile();
		this.standalone = standalone;
		initComponents();
		setup();
	}

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents()
	{

		jLabel1 = new javax.swing.JLabel();
		lblFileName = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		btnYes = new javax.swing.JButton();
		btnNo = new javax.swing.JButton();

		setTitle("View File");
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				exitForm(evt);
			}
		});
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		jLabel1.setText("The output is in file");
		getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));
		getContentPane().add(lblFileName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 370, 20));

		jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel3.setText("Do you want to view this file now?");
		getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 60, 380, -1));

		btnYes.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnYes.setText("Yes");
		btnYes.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		btnYes.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnYesActionPerformed(evt);
			}
		});
		getContentPane().add(btnYes, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 50, -1));

		btnNo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnNo.setText("No");
		btnNo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		btnNo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnNoActionPerformed(evt);
			}
		});
		getContentPane().add(btnNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 40, -1));

		setSize(new java.awt.Dimension(428, 214));
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	private void btnYesActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnYesActionPerformed
	{// GEN-HEADEREND:event_btnYesActionPerformed
		view();
	}// GEN-LAST:event_btnYesActionPerformed

	private void btnNoActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnNoActionPerformed
	{// GEN-HEADEREND:event_btnNoActionPerformed
		exitForm(null);
	}// GEN-LAST:event_btnNoActionPerformed

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt)
	{// GEN-FIRST:event_exitForm
		dispose();
		if (standalone)
			System.exit(0);
	}// GEN-LAST:event_exitForm

	private void setup() throws UtilitiesException
	{
		if (!new File(fileName).exists())
			throw new UtilitiesException(UtilitiesException.FILENOTFOUND, fileName);
		lblFileName.setText(fileName);
		this.pack();
		this.setVisible(true);
	}

	private void view()
	{
		try
		{
			String ext = Strings.fileSpec("EXT", fileName);
			if (ext.equalsIgnoreCase("TXT"))
			{ // Use our text file viewer (which does not allow editing)
				StringBuffer text = new StringBuffer();
				UsefulFile file = new UsefulFile(fileName, "r");
				while (!file.EOF())
					text.append(file.readLine(UsefulFile.RIGHT_WHITE) + "\n");
				file.close();
				TextDialog textDialog = new TextDialog(this, true);
				textDialog.setText(text.toString());
				textDialog.doSetTitle(fileName);
				textDialog.setOutfileName(fileName);
				textDialog.pack();
				textDialog.setVisible(true);
			} else
				openFile(fileName);
		} catch (IOException iox)
		{
			logger.log(LogFile.FATAL_ERROR, iox.getMessage());
		} catch (InterruptedException ix)
		{
			logger.log(LogFile.FATAL_ERROR, ix.getMessage());
		} catch (UtilitiesException ux)
		{
			logger.log(LogFile.FATAL_ERROR, ux.getMessage());
		}
		exitForm(null);
	}

	public static void openFile(String filename) throws IOException, InterruptedException, UtilitiesException
	{
		new DOSExec(filename);
	}

	// public static void viewPDF( String filename )
	// throws IOException, InterruptedException, UtilitiesException
	// { DOSExec exec = new DOSExec( "\"C:\\Batch\\PDFView.bat\" \"" + filename +
	// "\"" ); }
	//
	// public static void printPDF( String filename )
	// throws IOException, InterruptedException, UtilitiesException
	// { DOSExec exec = new DOSExec( "\"C:\\Batch\\PDFPrint.bat\" \"" + filename +
	// "\"" ); }
	//
	// public static void viewXLS( String filename )
	// throws IOException, InterruptedException, UtilitiesException
	// //{ DOSExec exec = new DOSExec( "\"C:\\Batch\\XLSView.bat\" \"" + filename +
	// "\"" ); }
	// { new DOSExec( "\"C:\\Program Files\\Microsoft Office\\Office\\excel.exe\"
	// \"" + filename + "\"" ); }
	//
	/**
	 * @param args
	 *            the command line arguments
	 */
	/*****
	 * FOR TESTING ***** public static void main(String args[]) { try { new
	 * ViewFile( "E:ACCOUNTING\\EXTANT\\GL17\\CHART.PDF", new LogFile(), true
	 * ).setVisible( true ); return; } catch (UtilitiesException ux) {
	 * Console.println( ux.getMessage() ); } System.exit( 0 ); } /
	 *****/

	String fileName;
	LogFile logger;
	boolean standalone;

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnNo;
	private javax.swing.JButton btnYes;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel lblFileName;
	// End of variables declaration//GEN-END:variables
}
