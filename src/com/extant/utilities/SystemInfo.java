/*
 * SystemInfo.java
 *
 * 09-28-06 Expand attempt to find MAC to use ipconfig when getMAC fails
 * Created on February 4, 2004, 2:29 PM
 */

package com.extant.utilities;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 *
 * @author  jms
 */
public class SystemInfo
{
    public SystemInfo()
    {
    }

    public static String getOS()
    {
        return System.getProperty( "os.name" );
    }

    public static String getUser()
    {
        return System.getProperty( "user.name" );
    }

    public static String getUserHome()
    {
        return System.getProperty( "user.home" );
    }

    public static String getMAC()
    throws UtilitiesException
    {
        String getMACResult="<undef>";
        String ipConfigResult="<undef>";
        String mac="";
        try
        {
            String osName = System.getProperty( "os.name" );
            if ( osName.equals( "Windows XP" )
              || osName.equals( "Windows 2000" ) ) // or see note at end
            {
                DOSExec dosExec = new DOSExec( "getMAC" );
                getMACResult = dosExec.getOutput();
                int p = getMACResult.lastIndexOf( "=" );
                if ( p > 0 )
                {
                    mac = getMACResult.substring( p + 1 );
                    mac = Strings.trim( mac, "\n\r" );
                    p = mac.indexOf(' ');
                    if ( p > 0 ) mac = mac.substring( 0, p );
                    else mac = "<unknown> from getMAC-1";
                }
                else mac = "<unknown> from getMAC-2";
            }
//Console.println( "MAC from getMAC: " + mac );
            if ( !Strings.regexMatch( regexMAC, mac ) )
            {   // That didn't work, try IPCONFIG
                DOSExec dosExec = new DOSExec( "ipconfig /all" );
                ipConfigResult = dosExec.getOutput();
                int p = ipConfigResult.indexOf( "Physical Address" );
                if ( p > 0 )
                {
                    mac = ipConfigResult.substring( p );
                    mac = mac.substring( mac.indexOf( ":" )+1, mac.indexOf( "\n" ) ).trim();
                }
                if ( !Strings.regexMatch( regexMAC, mac ) )
                    mac = "<unknown> from ipconfig";
            }
//Console.println( "MAC from ipconfig: " + mac );
            return mac;
        }
        catch (Exception x)
        {
            throw new UtilitiesException( UtilitiesException.UNEXPECTED, x.getMessage() +
                "  getMACResult="+getMACResult+"  ipConfigResult="+ipConfigResult +"  mac="+mac );
        }
    }

    public final static String regexMAC = "([0-9a-fA-F]{2}\\-){5}[0-9a-fA-F]{2}";

    /***** FOR TESTING *****/
    public static void main(String[] args)
    {
        try
        {
            SystemInfo systemInfo = new SystemInfo();
            Console.println( "Local OS: " + systemInfo.getOS() );
            Console.println( "Local MAC Address: '" + systemInfo.getMAC() + "'" );

            Console.println( "List of all available System Properties:" );
            Properties props = System.getProperties();
            Enumeration e = props.keys();
            while ( e.hasMoreElements() )
            {
                String key = (String)e.nextElement();
                String value = props.getProperty( key );
                Console.println( key + "=" + value );
            }
            
            // More interesting stuff relating to time
            TimeZone timeZone = TimeZone.getDefault();
            Console.println( "default timeZone=" + timeZone.getID() );
            //Console.println( "Available TimeZone ID's:" );
            //String ids[] = timeZone.getAvailableIDs();
            //for (int i=0; i<ids.length; ++i)
            //    Console.print( ids[i] + "  " );
            //Console.println( "" );
            Julian local = new Julian();
            Console.println( "local time=" + local.toString( "hh:mm:ss" ) );
            Console.println( "RawOffset=" + timeZone.getRawOffset() + " = " +
                timeZone.getRawOffset() / 3600000 + " hour(s)" );
            Julian utc = local.addSeconds( -timeZone.getRawOffset() / 1000 );
            Console.println( "summertime=" + timeZone.inDaylightTime( new Date() ) );
            Console.println( "DTSSavings=" + timeZone.getDSTSavings() + " = " +
                timeZone.getDSTSavings() / 3600000 + " hour(s)" );
            if ( timeZone.inDaylightTime( new Date() ) )
                utc.addSeconds( -timeZone.getDSTSavings()/ 1000 );
            Console.println( "current UTC=" + utc.toString( "hh:mm:ss" ) );
        }
        catch (Exception x)
        {
            Console.println( x.getMessage() );
        }
    }
    /*****/
    
    /* List of all properties available in System Properties:
    java.runtime.name=Java(TM) 2 Runtime Environment, Standard Edition
    sun.boot.library.path=C:\Program Files\Java\jdk1.5.0_02\jre\bin
    java.vm.version=1.5.0_02-b09
    java.vm.vendor=Sun Microsystems Inc.
    java.vendor.url=http://java.sun.com/
    path.separator=;
    java.vm.name=Java HotSpot(TM) Client VM
    file.encoding.pkg=sun.io
    user.country=US
    sun.os.patch.level=Service Pack 2
    java.vm.specification.name=Java Virtual Machine Specification
    user.dir=E:\Projects\Java5\extant
    java.runtime.version=1.5.0_02-b09
    java.awt.graphicsenv=sun.awt.Win32GraphicsEnvironment
    java.endorsed.dirs=C:\Program Files\Java\jdk1.5.0_02\jre\lib\endorsed
    os.arch=x86
    java.io.tmpdir=C:\DOCUME~1\jms\LOCALS~1\Temp\
    line.separator=

    java.vm.specification.vendor=Sun Microsystems Inc.
    user.variant=
    os.name=Windows XP
    sun.jnu.encoding=Cp1252
    java.library.path=C:\Program Files\Java\jdk1.5.0_02\jre\bin;.;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\Batch;C:\Progra~1\Java\jdk1.5.0_02\bin;C:\Program Files\SecureCRT;C:\Progra~1\PKWARE\PKZIPC;C:\Program Files\Java\jwsdp-1.5\jwsdp-shared\bin
    java.specification.name=Java Platform API Specification
    java.class.version=49.0
    sun.management.compiler=HotSpot Client Compiler
    os.version=5.1
    user.home=C:\Documents and Settings\jms
    user.timezone=
    java.awt.printerjob=sun.awt.windows.WPrinterJob
    file.encoding=Cp1252
    java.specification.version=1.5
    java.class.path=E:\Projects\Java5\extant\lib\jaxrpc-api.jar;E:\Projects\Java5\extant\lib\jaxrpc-impl.jar;E:\Projects\Java5\extant\lib\activation.jar;E:\Projects\Java5\extant\lib\mail.jar;E:\Projects\Java5\extant\lib\saaj-impl.jar;E:\Projects\Java5\extant\lib\awtabsolute.jar;E:\Projects\Java5\stockQuoteClient\dist\stockQuoteClient.jar;E:\Projects\Java5\extant\lib\itext-1.02b.jar;E:\Projects\Java5\extant\lib\mysql-connector-java-3.1.7-bin.jar;E:\Projects\Java5\extant\lib\enterprisedt.jar;E:\Projects\Java5\extant\lib\mindterm.jar;E:\Projects\Java5\extant\build\classes
    user.name=jms
    java.vm.specification.version=1.0
    java.home=C:\Program Files\Java\jdk1.5.0_02\jre
    sun.arch.data.model=32
    user.language=en
    java.specification.vendor=Sun Microsystems Inc.
    awt.toolkit=sun.awt.windows.WToolkit
    java.vm.info=mixed mode
    java.version=1.5.0_02
    java.ext.dirs=C:\Program Files\Java\jdk1.5.0_02\jre\lib\ext
    sun.boot.class.path=C:\Program Files\Java\jdk1.5.0_02\jre\lib\rt.jar;C:\Program Files\Java\jdk1.5.0_02\jre\lib\i18n.jar;C:\Program Files\Java\jdk1.5.0_02\jre\lib\sunrsasign.jar;C:\Program Files\Java\jdk1.5.0_02\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.5.0_02\jre\lib\jce.jar;C:\Program Files\Java\jdk1.5.0_02\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.5.0_02\jre\classes
    java.vendor=Sun Microsystems Inc.
    file.separator=\
    java.vendor.url.bug=http://java.sun.com/cgi-bin/bugreport.cgi
    sun.io.unicode.encoding=UnicodeLittle
    sun.cpu.endian=little
    sun.desktop=windows
    sun.cpu.isalist=
    default timeZone=America/Chicago
    local time=10:34:02
    RawOffset=-21600000 = -6 hour(s)
    summertime=true
    DTSSavings=3600000 = 1 hour(s)
    current UTC=15:34:02
    */

    /***** NOTES RELATING TO MAC ADDRESS *****
     * We do not know what responses (if any) are given to these commands
     * under Windows 2000.
     *
     * This is the response to the DOS Command 'ipconfig /all' under Windows XP:

Windows IP Configuration

        Host Name . . . . . . . . . . . . : DELTA
        Primary Dns Suffix  . . . . . . . :
        Node Type . . . . . . . . . . . . : Hybrid
        IP Routing Enabled. . . . . . . . : No
        WINS Proxy Enabled. . . . . . . . : No

Ethernet adapter Local Area Connection:

        Connection-specific DNS Suffix  . : satx.rr.com
        Description . . . . . . . . . . . : GVC-REALTEK Ethernet 10/100 PCI Adap
ter
        Physical Address. . . . . . . . . : 00-C0-A8-80-77-B4
        Dhcp Enabled. . . . . . . . . . . : Yes
        Autoconfiguration Enabled . . . . : Yes
        IP Address. . . . . . . . . . . . : 192.168.1.100
        Subnet Mask . . . . . . . . . . . : 255.255.255.0
        Default Gateway . . . . . . . . . : 192.168.1.1
        DHCP Server . . . . . . . . . . . : 192.168.1.1
        DNS Servers . . . . . . . . . . . : 24.93.40.64
                                            24.93.40.65
                                            24.93.40.66
        Lease Obtained. . . . . . . . . . : Sunday, May 09, 2004 12:32:13 AM
        Lease Expires . . . . . . . . . . : Monday, May 10, 2004 12:32:13 AM

     * This is the response to the DOS Command 'getMac' under Windows XP

       Local MAC Address: 
       Physical Address    Transport Name                                            
       =================== ==========================================================
       00-C0-A8-80-77-B4   \Device\Tcpip_{C55ACFAA-2983-48FF-9E1E-E682D8D2D258}      
       Disabled            Disconnected

     *****/

}

