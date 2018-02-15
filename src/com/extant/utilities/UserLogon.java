package com.extant.utilities;

/*
 * UserLogon.java
 * This class is used by both vl and investDB
 * For vl, it presents a logon screen and validates the logon information.
 * For investDB, no logon screen is presented.
 * In either case:
 *    setupFTP() returns a connected FTP Client for that application
 *    getServerID() & getServerPW() return the UNIX user ID & PW
 *    getPT(X) returns plaintext X
 *
 * This class obsoletes the class com.extant.vl.VLUserMan
 *
 * Created on January 10, 2005, 1:52 AM
 */
/***********************************************************************
package com.extant.utilities;
import java.io.IOException;
import java.awt.Frame;
import com.enterprisedt.net.ftp.FTPClient;

/**
 *
 * @author  jms
 
public class UserLogon
extends javax.swing.JDialog
{
    public UserLogon( java.awt.Frame parent, XProperties props, boolean modal )
    {
        this( "", parent, props, modal );
    }

    public UserLogon( String userID, java.awt.Frame parent, XProperties props, boolean modal)
    {
        super(parent, modal);
        initComponents();
        this.userID = userID;
        this.props = props;
        setup();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUserID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pwUserPW = new javax.swing.JPasswordField();
        btnOK = new javax.swing.JButton();

        setTitle("User Logon");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(txtUserID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 80, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("User ID:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 70, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Password:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 70, -1));

        pwUserPW.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwUserPWKeyTyped(evt);
            }
        });
        getContentPane().add(pwUserPW, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 80, -1));

        btnOK.setText("OK");
        btnOK.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        getContentPane().add(btnOK, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 40, -1));

        setSize(new java.awt.Dimension(408, 168));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pwUserPWKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_pwUserPWKeyTyped
    {//GEN-HEADEREND:event_pwUserPWKeyTyped
        if ( evt.getKeyChar() == '\n' ) btnOKActionPerformed( null );
    }//GEN-LAST:event_pwUserPWKeyTyped

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        userID = txtUserID.getText();
        char caUserPW[] = pwUserPW.getPassword();
        String userPW = new String( caUserPW );
        Blowfish blowfish = new Blowfish();
        userIDx = blowfish.encode( sb, userID );
        if ( userPW.length() > 0 ) userPWx = getCT( userPW );
        else userPWx = "";
        closeDialog( null );
    }//GEN-LAST:event_btnOKActionPerformed
    private void closeDialog(java.awt.event.WindowEvent evt)
    {//GEN-FIRST:event_closeDialog
        setVisible(false);
        //dispose();
    }//GEN-LAST:event_closeDialog

    void setup()
    {
        key = getPubKey( "extant" );
        txtUserID.setText( userID );
        if ( userID.length() > 0 ) pwUserPW.requestFocus();
        else txtUserID.requestFocus();
    }

    public boolean isValid()
    { //return userPWx.equals( props.getString( "userPWx" ) ); }
      return true;   //Disable user PW test
    }

    public static String getCT( String pt )
    { return new Blowfish().encode( sb, pt ).trim(); }

    public static String getPT( String ct )
    throws UtilitiesException
    {
        return new Blowfish().decode( sb, ct ).trim();
    }

    public String getFtpUser()
    throws UtilitiesException
    {
        return props.getString( "serverID", "" );
    }

    public String getFtpPW()
    throws UtilitiesException
    {
        return getPT( props.getString( "serverPWx", "" ) );
    }

    public String getServerID()
    throws UtilitiesException
    { return getFtpUser(); }
    
    public String getServerPW()
    throws UtilitiesException
    { return getFtpPW(); }

    public String getUserID() { return userID; }
    public String getUserIDx() { return userIDx; }
    public String getUserPWx() { return userPWx; }

    public FTPClient setupFTP()
    throws IOException, UtilitiesException, FTPException
    {
        String remoteHost = props.getString( "remoteHost",  "extant.com" );
        String destDir = props.getString( "VL.ftpRemoteDir", "." );
        //logger.log( logger.DEBUG, "FTP Connecting to " + remoteHost + "::" + destDir );
        FTPClient ftp = new FTPClient( remoteHost );
        ftp.login( getFtpUser(), getFtpPW() );
        ftp.setType( FTPTransferType.ASCII );
        ftp.setConnectMode( FTPConnectMode.PASV );
        if ( !destDir.equals( "." ) ) ftp.chdir( destDir );
        //logger.logDebug( "FTP Connected: " + remoteHost + "::" + destDir );
        return ftp;
    }

    public void uploadFile( String filename )
    throws IOException, UtilitiesException, FTPException
    {
        FTPClient ftp = setupFTP();
        ftp.put( filename, Strings.fileSpec( "FILE", filename ) ); //!! Needs testing
    }

    public void downloadFile( String filename )
    throws IOException, UtilitiesException, FTPException
    {
        FTPClient ftp = setupFTP();
        ftp.get( filename, Strings.fileSpec( "FILE", filename ) ); //!! Needs testing
    }

    final static byte[] getPubKey( String a4s )
    {
        byte a4[] = a4s.getBytes();
        long seed = a4[1] << 40 | a4[2] << 32 | a4[0] << 24 | a4[3] << 16 | a4[4] << 8 | a4[5];
        byte r[] = new byte[128];
        java.util.Random random = new java.util.Random( seed );
        random.nextBytes( r );
        int p[] = new int[] {20, 20, 92, 60, 59};
        sb = new byte[] { r[p[2]], r[p[4]], r[p[1]], r[p[0]], r[p[3]] };
        random.nextBytes( r );
        return r;
    }

    /**
     * @param args the command line arguments
     * p=<properties filename>
     *   Either
     *      E:\Projects\extant.properties (the default)  or
     *      E:\ACCOUNTING\XXX\xxx.properties
     *   (where xxx is an ACCOUNTING client)
     *
    /***** FOR TESTING *****
    public static void main(String args[])
    {
        UserLogon userLogon;
        try
        {
            Clip clip = new Clip( args, new String[] { "p=G:\\Projects\\extant.properties" } );
            //Clip clip = new Clip( args, new String[] { "p=E:\\ACCOUNTING\\JMS\\jms.properties" } );
            boolean requiresLogon = !clip.getParam( "p" ).endsWith( "extant.properties" );
            XProperties props;
            if ( requiresLogon ) props = new XProperties( clip.getParam( "p" ), "VL" );
            else props = new XProperties( clip.getParam( "p" ), "investDB" );
            userLogon = new UserLogon( new Frame(), props, requiresLogon );
            if ( requiresLogon )
            {
                userLogon.setVisible( true );
                Console.println( "UserID=" + userLogon.getUserID() + " [" + userLogon.getUserIDx() + "]" );
                Console.println( "UserPW [" + userLogon.getUserPWx() + "]" );
                if ( userLogon.isValid() )
                {
                    Console.println( "Logon is valid." );
                    Console.println( "   ftpUser=" + userLogon.getFtpUser() );
                    Console.println( "   ftpPW=" + userLogon.getFtpPW() );
                }
                else Console.println( "Logon is not valid." );
            }
            else
            {
                Console.println( "ftpUser=" + userLogon.getFtpUser() );
                Console.println( "ftpPW=" + userLogon.getFtpPW() );
                FTPClient ftp = userLogon.setupFTP();
                ftp.get( "C:\\Projects\\DownloadTest.txt", "test.txt" );
                ftp.quit();
            }
        }
        catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
        catch (IOException iox) { Console.println( iox.getMessage() ); }
        catch (FTPException ftpx) { Console.println( ftpx.getMessage() ); }
        System.exit( 0 );
    }
    /*****

    /***** TO ENCODE/DECODE PASSWORDS *****
    public static void main(String args[])
    {
        if ( args.length != 2 )
        {
            Console.println( "Use: UserLogon {ENcode | DEcode} input" );
            return;
        }
        try
        {
            byte key[] = UserLogon.getPubKey( "extant" );
            if ( args[0].toUpperCase().startsWith( "E") )
                Console.println( args[1] + " ==> " + UserLogon.getCT( args[1] ) );
            if ( args[0].toUpperCase().startsWith( "D" ) )
                Console.println( args[1] + " ==> " + UserLogon.getPT( args[1] ) );
        }
        catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
    }
    /*****

    //XProperties props;
    //private byte key[];
    //static private byte sb[];
    //private String userID;
    //private String userIDx;
    //private String userPWx;

 /*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField pwUserPW;
    private javax.swing.JTextField txtUserID;
    // End of variables declaration//GEN-END:variables


}
*/
