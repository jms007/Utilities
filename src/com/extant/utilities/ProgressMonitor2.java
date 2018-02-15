/*
 * ProgressMonitor2.java
 *
 * Created on February 3, 2004, 1:05 PM
 * Yet another attempt at progress monitoring.  This class is intended to replace
 * com.extant.utilities.ProgressMonitor
 * 2-4-04:  The only known user of this class is DBInterchange
 *
 * Here's how to use it:
 *      ProgressMonitor2 progressMonitor2 = new ProgressMonitor2( true ); // Dual progress bars
 *      // -or: ProgressMonitor2 progressMonitor2 = new ProgressMonitor2 ();      // single task (the GUI sucks)
 *      Thread thread = new Thread( progressMonitor2 );
 *      thread.start();
 *      thread.setPriority( Thread.MIN_PRIORITY );
 *      progressMonitor2.setTotalThings( 50000L ); // total number of things to do
 *
 *      for (int task=0; task<5; ++task) // loop through the tasks
 *      {
 *          progressMonitor2.startTask( "Task " + (task+1), 10000L ); // number of things in current task
 *          for (long bytes=0L; bytes<10000L; bytes+=1000L)
 *          {
 *              // Do your things here ...
 *
 *              // We can report progress incrementally, or by total
 *              progressMonitor2.addThingsDone( 1000L );
 *              //progressMonitor2.setThingsDone( bytes ); // but this doesn't work very well
 *          }
 *          progressMonitor2.markTaskComplete();
 *      }
 *      progressMonitor2.markJobComplete();
 */

package com.extant.utilities;
import java.util.Vector;
import java.util.Random;
import java.awt.Graphics;

/**
 *
 * @author  jms
 */
public class ProgressMonitor2 extends javax.swing.JFrame implements Runnable
{
    // Constructor for a single progress bar
    public ProgressMonitor2()
    {
        this ( false );
    }

    // Constructor for either single (false) or dual (true) progress bars
    public ProgressMonitor2(boolean dual)
    {
        this.dual = dual;
        initComponents();

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //if ( dual )
            setSize(new java.awt.Dimension(450, 220));
        //else setSize( new java.awt.Dimension( 450, 110 ) );
        setLocation((screenSize.width-450)/2,(screenSize.height-220)/2);
        setup();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Current Task", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jProgressBar1.setStringPainted(true);
        jPanel1.add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 410, 20));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 410, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 430, 60));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Job", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jProgressBar2.setStringPainted(true);
        jPanel2.add(jProgressBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 410, 20));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 410, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 430, 70));

        setSize(new java.awt.Dimension(459, 201));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm

    void setup()
    {
        jPanel1.setVisible( dual );
    }

    public void run()
    {
        this.setVisible( true );
        Graphics graphics = this.getGraphics();
        start = new Julian();
        while ( true )
        {
            try { Thread.sleep ( 1000L ); } // update once per second
            catch (InterruptedException e) { break; }

            if ( currentThings == 0 || totalThings == 0 ) break;
            int pctTask = (int)((currentThingsDone * 100) / currentThings );
            int pctJob = (int)((totalThingsDone * 100) / totalThings );
            jProgressBar1.setValue( pctTask );
            jProgressBar2.setValue( pctJob );
            if ( pctJob > 0 )
            {
                double avg = (double)new Julian().minus( start ).getTODSeconds() / (double)totalThingsDone;
                int eteTotal = (int)((double)(totalThings - totalThingsDone) * avg );
                int eteCurrent = (int)((double)(currentThings - currentThingsDone) * avg );
                jLabel1.setText( "Estimated completion: " + eteCurrent  + " seconds" );
                jLabel2.setText( "Estimated completion: " + eteTotal + " seconds" );
            }
            if ( (pctJob >= 100) || weAreDone ) break;
            this.paintAll( graphics ); // We shouldn't have to do this
            // The test works OK .. but in a real application, this window does not paint :(
            // and, even with this paintAll statement, the labels do not show!
        }
        this.dispose();
    }

    public void setTotalThings( long totalThings )
    {
        this.totalThings = totalThings;
    }

    public void startTask( String taskName, long currentThings )
    {
        setTitle( taskName );
        this.currentThings = currentThings;
        currentThingsDone = 0L;
    }

    public void addThingsDone( long thingsDone )
    {
        currentThingsDone += thingsDone;
        totalThingsDone += thingsDone;
    }

    public void setThingsDone( long thingsDone )
    {   // This doesn't work well
        totalThingsDone += (thingsDone - currentThingsDone );
        currentThingsDone = thingsDone;
    }

    public void markTaskComplete()
    {
        currentThingsDone = currentThings;
    }

    public void markJobComplete()
    {
        currentThingsDone = currentThings;
        totalThingsDone = totalThings;
        weAreDone = true;
    }

    /**
     * @param args the command line arguments<BR>
     * None
     */
    /***** FOR TESTING *****
    public static void main(String args[])
    {
        ProgressMonitor2 progressMonitor2 = new ProgressMonitor2( true ); // Dual progress bars
        //ProgressMonitor2 progressMonitor2 = new ProgressMonitor2 ();      // single task
        Thread thread = new Thread( progressMonitor2 );
        thread.start();
        thread.setPriority( Thread.MIN_PRIORITY );
        progressMonitor2.setTotalThings( 50000L ); // 5 tasks of 10,000 bytes each

        Random random = new Random();
        for (int task=0; task<5; ++task)
        {
            progressMonitor2.setTitle( "Task " + (task+1) );
            progressMonitor2.startTask( "Task" + (task+1), 10000L ); // length of current task
            for (long bytes=0L; bytes<10000L; bytes+=1000L)
            {
                int r = random.nextInt() & 0xFF;
                if ( (r & 1) == 1 ) r = -r;
                try { Thread.sleep( 1000L + r ); } // simulate processing time for 1000 bytes
                catch (InterruptedException ix)
                {
                    Console.println( "main loop interrupted" );
                    System.exit( 1 );
                }
                // We can report progress incrementally, or by total
                progressMonitor2.addThingsDone( 1000L );
                //progressMonitor2.setThingsDone( bytes ); // but this doesn't work very well
            }
            progressMonitor2.markTaskComplete();
        }
        progressMonitor2.markJobComplete();
        Console.prompt( "Done ..." );
        System.exit( 0 );
    }
    /*****/

    boolean dual;
    Julian start;
    long totalThings;
    long currentThings;
    long currentThingsDone;
    long totalThingsDone;
    boolean weAreDone = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    // End of variables declaration//GEN-END:variables
}

