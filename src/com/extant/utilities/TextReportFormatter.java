/*
 * TextReportFormatter.java
 *
 * Created on January 16, 2004, 2:04 AM
 */

package com.extant.utilities;

import java.io.IOException;
import java.awt.Frame;
import java.util.StringTokenizer;

/**
 *
 * @author jms
 */
public class TextReportFormatter extends javax.swing.JFrame
{

	public TextReportFormatter()
	{
		this(null, null, false);
	}

	public TextReportFormatter(String inputFileName, String outputFileName, boolean standalone)
	{
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.standalone = standalone;
		initComponents();
		setup();
	}

	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents()
	{

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtDelimiter = new javax.swing.JTextField();
		txtInputFile = new javax.swing.JTextField();
		btnBrowseIn = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		txtNoColumns = new javax.swing.JTextField();
		jPanel2 = new javax.swing.JPanel();
		txtOutputFile = new javax.swing.JTextField();
		btnBrowseOut = new javax.swing.JButton();
		btnOK = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		statusBar = new javax.swing.JLabel();

		setTitle("Text Report Formatter");
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				exitForm(evt);
			}
		});
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Input File",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Arial", 0, 10))); // NOI18N
		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		jLabel1.setText("Delimiter:");
		jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
		jPanel1.add(txtDelimiter, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 20, -1));
		jPanel1.add(txtInputFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 360, -1));

		btnBrowseIn.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnBrowseIn.setText("Browse");
		btnBrowseIn.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnBrowseInActionPerformed(evt);
			}
		});
		jPanel1.add(btnBrowseIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

		jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		jLabel2.setText("Number of columns:");
		jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, -1, -1));
		jPanel1.add(txtNoColumns, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 30, -1));

		getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 510, 120));

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Output File",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Arial", 0, 10))); // NOI18N
		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jPanel2.add(txtOutputFile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 370, -1));

		btnBrowseOut.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnBrowseOut.setText("Browse");
		btnBrowseOut.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnBrowseOutActionPerformed(evt);
			}
		});
		jPanel2.add(btnBrowseOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, -1, -1));

		getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 510, 80));

		btnOK.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnOK.setText("OK");
		btnOK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		btnOK.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnOKActionPerformed(evt);
			}
		});
		getContentPane().add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 70, -1));

		btnCancel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
		btnCancel.setText("Cancel");
		btnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
		btnCancel.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				btnCancelActionPerformed(evt);
			}
		});
		getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 60, -1));

		statusBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		getContentPane().add(statusBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 510, 20));

		setSize(new java.awt.Dimension(547, 415));
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnCancelActionPerformed
	{// GEN-HEADEREND:event_btnCancelActionPerformed
		exitForm(null);
	}// GEN-LAST:event_btnCancelActionPerformed

	private void btnBrowseOutActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnBrowseOutActionPerformed
	{// GEN-HEADEREND:event_btnBrowseOutActionPerformed
		outputFileName = new BrowseHandler((Frame) this, BrowseHandler.SAVE_DIALOG).getSelectedFile("C:\\");
		txtOutputFile.setText(outputFileName);
	}// GEN-LAST:event_btnBrowseOutActionPerformed

	private void btnBrowseInActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnBrowseInActionPerformed
	{// GEN-HEADEREND:event_btnBrowseInActionPerformed
		inputFileName = new BrowseHandler((Frame) this, BrowseHandler.OPEN_DIALOG).getSelectedFile("C:\\");
		txtInputFile.setText(inputFileName);
	}// GEN-LAST:event_btnBrowseInActionPerformed

	private void btnOKActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_btnOKActionPerformed
	{// GEN-HEADEREND:event_btnOKActionPerformed
		try
		{
			doit();
		} catch (IOException iox)
		{
			statusBar.setText(iox.getMessage());
		} catch (UtilitiesException ux)
		{
			statusBar.setText(ux.getMessage());
		}
	}// GEN-LAST:event_btnOKActionPerformed

	private void exitForm(java.awt.event.WindowEvent evt)
	{// GEN-FIRST:event_exitForm
		if (standalone)
			System.exit(0);
		this.dispose();
	}// GEN-LAST:event_exitForm

	private void setup()
	{
		try
		{
			if (inputFileName == null)
			{
				// Get an input file even before we show our dialog
				// (so we can show the computed info as defaults in the dialog)
				inputFileName = new BrowseHandler((Frame) this, BrowseHandler.OPEN_DIALOG).getSelectedFile("C:\\");
				if (inputFileName == null)
					exitForm(null);
				txtInputFile.setText(inputFileName);
			}
			analyzeInput();
			if (txtOutputFile.getText().equals(""))
				txtOutputFile.setText(Strings.getDerivedFileName(txtInputFile.getText(), ".txt"));
			if (txtOutputFile.getText().equals(inputFileName))
				throw new UtilitiesException(UtilitiesException.I_O_FILES_MATCH, inputFileName);
			if (!standalone)
				doit();
		} catch (IOException iox)
		{
			statusBar.setText("iox: " + iox.getMessage());
		} catch (UtilitiesException ux)
		{
			statusBar.setText("ux: " + ux.getMessage());
		}
	}

	private void analyzeInput() throws IOException
	{
		String image;
		inputFile = new UsefulFile(inputFileName, "r");
		image = inputFile.readLine(UsefulFile.EOL);
		int maxCount = 0;
		char maxChar = '#';
		for (int i = 0; i < delimiters.length; i++)
		{
			int t = Strings.countChar(delimiters[i], image);
			if (t > maxCount)
			{
				maxCount = t;
				maxChar = delimiters[i];
			}
		}
		char da[] = new char[1];
		da[0] = maxChar;
		txtDelimiter.setText(new String(da));
		txtNoColumns.setText(new Integer(maxCount + 1).toString());
		// inputFile.rewind(); we exclude the first line when computing column widths &
		// types
		colWidths = new int[maxCount + 1];
		colTypes = new int[maxCount + 1];
		for (int i = 0; i <= maxCount; ++i)
		{
			colWidths[i] = 0;
			colTypes[i] = RIGHT;
		}
		int lineNo = 0;
		while (!inputFile.EOF())
		{
			image = inputFile.readLine(UsefulFile.EOL);
			++lineNo;
			StringTokenizer st = new StringTokenizer(image, txtDelimiter.getText());
			int col = 0;
			while (st.hasMoreElements())
			{
				String entry = Strings.trim((String) st.nextElement(), " '\"");
				if (entry.length() > colWidths[col])
					colWidths[col] = entry.length();
				if (!isNumber(entry) && colTypes[col] == RIGHT)
					colTypes[col] = LEFT;
				++col;
			}
			// if ( col != maxCount+1 ) Console.println( "col=" + col + " maxCount=" +
			// maxCount +
			// " in Line " + lineNo );
		}
		// for (int i=0; i<colTypes.length; ++i)
		// Console.println( "colTypes[" + i + "]=" + colTypes[i] );
		inputFile.close();
	}

	private void doit() throws IOException, UtilitiesException
	{
		String image;
		outputFileName = txtOutputFile.getText();
		if (outputFileName.equals(""))
			throw new UtilitiesException(UtilitiesException.NO_OUTPUT_FILE);
		inputFile = new UsefulFile(inputFileName, "r");
		outputFile = new UsefulFile(outputFileName, "w");
		while (!inputFile.EOF())
		{
			image = inputFile.readLine(UsefulFile.EOL);
			String outLine = "";
			StringTokenizer st = new StringTokenizer(image, txtDelimiter.getText());
			int col = 0;
			while (st.hasMoreElements())
			{
				String entry = Strings.trim((String) st.nextElement(), " '\"");
				if (colTypes[col] == LEFT)
					outLine += Strings.leftJustify(entry, colWidths[col]);
				else
					outLine += Strings.rightJustify(entry, colWidths[col]);
				outLine += columnSeparator;
				++col;
			}
			outputFile.writeLine(outLine);
		}
		inputFile.close();
		outputFile.close();
		exitForm(null);
	}

	public static boolean isNumber(String v)
	{
		return Strings.validateChars(v, "0123456789$,.-");
	}

	public static void main(String args[])
	{
		TextReportFormatter trf = new TextReportFormatter(null, null, true);
		trf.setVisible(true);
	}

	boolean standalone = false;
	String inputFileName;
	String outputFileName;
	UsefulFile inputFile;
	UsefulFile outputFile;
	char delimiters[] = { '\t', ',', ';' }; // These are the ones used for auto-detect
	int colWidths[];
	int colTypes[];
	int LEFT = 0;
	int RIGHT = 1;
	String columnSeparator = "  ";

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnBrowseIn;
	private javax.swing.JButton btnBrowseOut;
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnOK;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JLabel statusBar;
	private javax.swing.JTextField txtDelimiter;
	private javax.swing.JTextField txtInputFile;
	private javax.swing.JTextField txtNoColumns;
	private javax.swing.JTextField txtOutputFile;
	// End of variables declaration//GEN-END:variables
}
