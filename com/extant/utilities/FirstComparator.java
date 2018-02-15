/*
 * FirstComparator.java
 * A Comparator implementation which (by default) compares Strings in the following sequence:
 *    a) letters (case-insensitive)
 *    b) numbers
 *    c) right-bracket
 *    d) forward slash
 *    e) everything else in ASCII sequence
 *
 * The first 4 items (a-d) are determined by the default sequence.  Any character
 * which does not appear in sequence is sorted according to its ASCII value.
 * The default sequence can be overridden with constructor FirstComparator( String ),
 * and the case sensitivity can be contolled using the method setCaseSensitivity( boolean ).
 * Default caseSensitivity is false.
 * 
 * Created on April 2, 2003, 9:15 PM
 */

package com.extant.utilities;
import java.util.Comparator;

/**
 *
 * @author  jms
 */
public class FirstComparator
implements Comparator
{
    public FirstComparator()
    {
    }

    public FirstComparator( String sequence )
    {
        this.sequence = sequence;
    }

    public int compare(Object obj1, Object obj2)
    {
        if ( obj1.equals( obj2 ) ) return 0;
        String s1 = obj1.toString();
        String s2 = obj2.toString();
        if ( !caseSensitive )
        {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();
        }
        if ( s1.equals( s2 ) ) return 0;
        char c1, c2;
        for (int i=0; i<s1.length(); ++i)
        {
            c1 = s1.charAt( i );
            if ( i < s2.length() )c2 = s2.charAt( i );
            else c2 = ' '; // s2 is shorter; pad it with blanks
            if ( c1 == c2 ) continue;
            int i1 = sequence.indexOf( c1 );
            int i2 = sequence.indexOf( c2 );
            if ( i1 >= 0 )
            {
                if ( i2 >= 0 )
                {   // Both are in our table:  use the table
                    return i1 - i2;
                }
                else // c1 is in our table; c2 is not:  c1 comes first
                    return -1;
            }
            else
            {
                if ( i2 >= 0 )
                {   // c1 is not in our table; c2 is:  c2 comes first
                    return 1;
                }
                else // neither c1 nor c2 is in our table:  use ASCII order
                    return (int)c1 - (int)c2;
            }
        }
        // We have finished with s1.  If s2 is longer, then s1 comes first
        if ( s1.length() < s2.length() ) return -1;
        else return 1;
    }

    public void setCaseSensitive( boolean caseSensitive )
    {
        this.caseSensitive = caseSensitive;
    }

    // This is the default sequence.  It can be overridden by the constructor
    //    FirstComparator( String )
    // the right-bracket and slash are here to sort accounts correctly when
    // populating the account drop-down lists in VL.
    // Examples we want [899] to sort before [899/SUBACCT]
    //      and we want [007] to sort before [0071]
    private String sequence =
        " AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz]0123456789/";
    // Note if you use caseSensitive = false, there is no need for capital
    // letters in sequence, since they cannot occur
    private boolean caseSensitive = false;
}

