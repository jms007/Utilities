/*
 * Possibilities.java
 * Produces a list (by Enumeration) of all possible combinations of n items.
 * (Excludes the combination containing no items, so the number of
 *  possibilities is 2**n - 1)
 * There is no upper limit on the value of n.
 *
 * Created on May 7, 2006, 5:40 PM
 */

/**
 *
 * @author jms
 */
package com.extant.utilities;
import java.util.Enumeration;
import java.util.Vector;
import java.math.BigInteger;
  
public class Possibilities
implements Enumeration
{
    
    public Possibilities( int nItems )
    {
        this.nItems = nItems;
        int nPoss = (int)java.lang.Math.pow( 2, nItems ) - 1;
        nCombos = new BigInteger( new Integer( nPoss ).toString() );
        counter = new BigInteger( "0" );
    }

    public boolean hasMoreElements()
    {
        return counter.compareTo( nCombos ) < 0;
    }

    public Object nextElement()
    // Returns an integer array containing the indices in this combination
    {
        if ( counter.compareTo( nCombos ) >= 0 ) return null; // no more elements
        int ans[] = new int[nItems];
        counter = counter.add( BigInteger.ONE );
        int p = 0;
        for (int i=0; i<nItems; ++i)
            if ( counter.testBit( i ) ) ans[p++] = i;
        return ArrayTrim.trim( ans, p );
    }

    public String getNCombos()
    {
        return nCombos.toString();
    }

    /**
     * @param args the command line arguments
     * Use: Possibilities n
     *    where n = number of items
     */
    /***** FOR TESTING *****/
    public static void main(String[] args)
    {
        try
        {
            Clip clip = new Clip( args, new String[] { "n=4" } );
            Console.println( "running Possibilities( " + clip.getParam( "n" ) + " )" );
            Possibilities possibilities = new Possibilities( Strings.parseInt( clip.getParam( "n" ) ) );
            Console.println( "There are " + possibilities.getNCombos() + " possible combinations." );
            Enumeration combos = new Possibilities( Strings.parseInt( clip.getParam( "n" ) ) );
            while ( combos.hasMoreElements() )
            {
                int thisCombo[] = (int [])combos.nextElement();
                Console.print( "combo: " );
                for (int i=0; i<thisCombo.length; ++i) Console.print( thisCombo[i] + " " );
                Console.println( "" );
            }
        }
        catch (UtilitiesException ux)
        {   Console.println( ux.getMessage() ); }
    }
    /***** END OF TEST CODE *****/


    int nItems;
    BigInteger nCombos;
    BigInteger counter;
}

