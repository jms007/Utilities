/*
 * ProgressMonitor.java
 *
 *        ***** THIS CLASS IS DEPRECATED IN FAVOR OF ProgressMonitor2 *****
 *
 * 2-4-04:  The only known user of this class is BeamUp (and associated FTPClient)
 *
 * This class executes in its own thread and presents an indication of progress on
 * one or more tasks.  One or two progress bars are displayed depending on the
 * constructor parameter 'dual'.
 *
 * This class and the class performing the task(s) must each have access to an
 * instance of class Shared.
 * The performing class sets the total job with a call to
 * setBytesTotal( long ) in this class.  For each task, it calls
 * startOp( long ) in this class.  It then uses method
 * setSharedData( long ) in the Shared class to indicate progress of the current task.
 *
 * For example:
 *   progressMonitor.setBytesTotal( totalBytes );       // Init the total job
 *   progressMonitor.startOp( currentBytes );           // Init the current task
 *   while ( something )
 *     if ( shared.isWriteable() )
 *       shared.setSharedData( bytesSoFar );            // Progress on current task
 *
 * This class computes the percentage complete and displays on the progress bar(s).
 * It also computes the estimated time to completion for the current task (and the
 * complete job, if appropriate) and displays this information under the appropriate
 * progress bar.
 *
 * Created on March 10, 2003, 7:37 PM
 */

package com.extant.utilities;

/**
 *
 * @author  jms
 */
public class ProgressMonitor extends javax.swing.JFrame implements Runnable
{
    private Shared shared;
    
    public ProgressMonitor(Shared shared, boolean dual)
    {
        this.shared = shared;
        this.dual = dual;
        initComponents();
        if ( dual ) initMoreComponents();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        if ( dual ) setSize(new java.awt.Dimension(406, 172));
        else setSize( new java.awt.Dimension( 406, 110 ) );
        setLocation((screenSize.width-406)/2,(screenSize.height-172)/2);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        lblEteCurrent = new javax.swing.JLabel();

        setTitle("Progress");
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new java.awt.FlowLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Current Task", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jProgressBar1.setStringPainted(true);
        jPanel1.add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 360, 20));

        lblEteCurrent.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblEteCurrent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEteCurrent.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.add(lblEteCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 370, 20));

        getContentPane().add(jPanel1);

        setSize(new java.awt.Dimension(406, 172));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initMoreComponents()
    {
        jPanel2 = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        lblEteTotal = new javax.swing.JLabel();
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.setBorder(new javax.swing.border.TitledBorder(null, "All Tasks", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10)));
        jProgressBar2.setStringPainted(true);
        jPanel2.add(jProgressBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 360, 20));
        lblEteTotal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblEteTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        //lblEteTotal.setBorder(new javax.swing.border.EtchedBorder());
        jPanel2.add(lblEteTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 370, 20));
        getContentPane().add(jPanel2);
        pack();
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    public void run ()
    {
        start = new Julian();
//Console.println( "[ProgressMonitor.run] executing ..." );
        while ( true )
        {
            try { Thread.sleep ( 1000L ); } // update once per second
            catch (InterruptedException e) { break; }

            long byteCount = shared.getSharedData();
//Console.println (byteCount + " / " + bytesCurrent + " received.");
            double pct = (int)( (double)byteCount / (double)bytesCurrent * 100.0 );
            if ( pct > 100.0 ) pct = 99.5;
            jProgressBar1.setValue( (int)(pct + 0.5) );
            jProgressBar1.setString( Strings.format( pct, "##0" ) + "%" );
            long elapsed = new Julian().minus( startCurrent ).getTODSeconds();
            int ete = -1;
            if ( pct > 99.0 ) ete = 0;
            else
            {
                // Bypass ete calculation during the first second
                if ( elapsed > 1 )
                {
                    double avg = (double)byteCount / (double)elapsed;
                    ete = (int)((bytesCurrent - byteCount) / avg);
                }
            }
            if ( ete >= 0 )
                lblEteCurrent.setText( "Estimated time to complete: " +
                    Strings.plurals( "second", ete ) + "." );

            if ( dual )
            {
//Console.println( (byteCount+bytesPrevious) + " / " + bytesTotal + " total." );
                double pctTotal = (int)( (double)(byteCount + (double)bytesPrevious) / (double)bytesTotal * 100.0 );
                if ( pct > 100.0 ) pct = 99.5;
                jProgressBar2.setValue( (int)(pctTotal + 0.5) );
                jProgressBar2.setString( Strings.format( pctTotal, "##0" ) + "%" );
                int eteTotal = -1;
                if ( pct > 99.0 ) eteTotal = 0;
                else
                {
                    // Bypass ete calculation for small elapsed times
                    if ( (elapsed + elapsedPrevious) > 1 )
                    {
                        double avg = (double)(bytesPrevious + byteCount) / (double)(elapsed + elapsedPrevious);
                        eteTotal = (int)( (double)(bytesTotal - byteCount - bytesPrevious) / avg );
                    }
                }
                if ( eteTotal > 0 )
                    lblEteTotal.setText( "Estimated time to complete: " +
                        Strings.plurals( "second", eteTotal ) + "." );
            }
        }
    }

    // Obsolete
    //public void setBytesCurrent( long bytesCurrent )
    //{
    //    this.bytesCurrent = bytesCurrent;
    //}

    public void setBytesTotal( long bytesTotal )
    {
        this.bytesTotal = bytesTotal;
    }

    // Obsolete
    //public void addBytesPrevious( long bytesPrevious )
    //{
    //    this.bytesPrevious += bytesPrevious;
    //    bytesCurrent = 0L;
    //}

    public void startOp( long bytesCurrent )
    {
        bytesPrevious += this.bytesCurrent;
        this.bytesCurrent = bytesCurrent;
        startCurrent = new Julian();
        if ( start == null ) start = startCurrent;
        elapsedPrevious = startCurrent.minus( start ).getTODSeconds();
    }

    /**
     * @param args the command line arguments
     * There is a problem with this test.  The direct calls to ProgressMonitor
     * are not properly synchronized with the shared data transfers, resulting
     * in inconsistent behavior around the time that a task completes.  It doesn't
     * seem to bother the actual application (BeamUp), so I haven't tried to fix it.
     * The real fix is to re-structure the whole thing (ProgressMonitor, Shared,
     * and the application) so that it is both easier to use and also works.
     * One of these days ...
     */
    public static void main(String args[])
    {
        Shared shared = new Shared();
        ProgressMonitor progressMonitor = new ProgressMonitor( shared, true );
        Thread thread = new Thread( progressMonitor );
        thread.start();
        thread.setPriority( Thread.MIN_PRIORITY );
        progressMonitor.setBytesTotal( 60000L ); // 6 tasks, 10000 bytes each
        progressMonitor.setVisible( true );
        for (int task=0; task<6; ++task)
        {
            progressMonitor.setTitle( "Task " + (task+1) );
            progressMonitor.startOp( 10000L ); // length of current task
            for (long bytes=0L; bytes<10000L; bytes+=1000L)
            {
                try { Thread.sleep( 977L ); } // simulate processing time for 1000 bytes
                catch (InterruptedException ix)
                {
                    Console.println( "main loop interrupted" );
                    System.exit( 1 );
                }
                if ( shared.isWritable() ) shared.setSharedData( bytes );
            }
        }
        Console.prompt( "Done ..." );
        progressMonitor.dispose();
        System.exit( 0 );
    }

    boolean dual;
    long bytesCurrent = 0L;
    long bytesTotal;
    long bytesPrevious = 0L;
    long elapsedPrevious = 0L;
    Julian start;
    Julian startCurrent;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel lblEteCurrent;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblEteTotal;
    private javax.swing.JProgressBar jProgressBar2;
}

