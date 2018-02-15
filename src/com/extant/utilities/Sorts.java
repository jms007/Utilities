package com.extant.utilities;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Collections.*;
import java.util.Comparator;
import java.io.IOException;

public class Sorts
  {

  public Sorts()
    {
    }

  // For sorting Vectors or String arrays
  //
  // The sort ( list, ..., boolean ) methods return a sorted list of the same type.
  // The sort( list, ... ) methods return an integer array containing indices to the input
  // object, list, such that the elements of list, when accessed in the order of
  // the index array, will be sorted.
  // To sort a file (producing a new sorted file), use method sortFile().
  // These sorts are non-destructive, stable, and very cool.

  // A Vector is sorted based on the value of its toString() method.
  // fieldP[] describes the key fields:
  //   fieldP[k*2] is the starting column, and
  //   fieldP[k*2+1] is the ending column
  //   for key field k
  // If there is exactly one key field, use the simpler form, e.g. sort( Vector, int )
  //   with keyCol set to the column number in which the key field starts.
  // If the single key field starts in column 0, use the simplest form, sort( Vector ).

  // The sorting sequence is determined by class FirstComparator.  Refer to that
  // class for information on how to change the sequence.

  // Pointer sorts for Vectors
  public static int[] sort( Vector v )
    { // The whole record is the key field
    return sort( v, 0 );
    }

  public static int[] sort( Vector v, int keyCol )
    { // The key field starts in keyCol and extends to the end of the record
    return sort( v, new int[] { keyCol, 0 } );
    }

  //public static int[] sort( Vector v, int fieldP[] )
  // This method does the real work ... it appears later

  // List sorts for Vectors
  public static Vector sort( Vector v, boolean makeList )
    { // note the value of makeList is immaterial
    int p[] = sort( v );
    Vector sv = new Vector( v.size() );
    for (int i=0; i<v.size(); ++i) sv.addElement( v.elementAt( p[i] ) );
    return sv;
    }

  public static Vector sort( Vector v, int keyCol, boolean makeList )
    { // note the value of makeList is immaterial
    int p[] = sort( v, keyCol );
    Vector sv = new Vector( v.size() );
    for (int i=0; i<v.size(); ++i) sv.addElement( v.elementAt( p[i] ) );
    return sv;
    }

  public static Vector sort( Vector v, int fieldP[], boolean makeList )
    { // note the value of makeList is immaterial
    int p[] = sort( v, fieldP );
    Vector sv = new Vector( v.size() );
    for (int i=0; i<v.size(); ++i) sv.addElement( v.elementAt( p[i] ) );
    return sv;
    }

  // Pointer sorts for String arrays
  public static int[] sort( String[] sa )
    {
    return sort( buildVector( sa ) );
    }

  public static int[] sort( String[] sa, int keyCol )
    {
    return sort( buildVector( sa ), keyCol );
    }

  public static int[] sort( String[] sa, int fieldP[] )
    {
    return sort( buildVector( sa ), fieldP );
    }

  // List sorts for String arrays
  public static String[] sort( String[] a, boolean makeList )
    { // Note the value of makeList is immaterial
    int p[] = sort( a );
    String[] sa = new String[a.length];
    for (int i=0; i<a.length; ++i) sa[i] = a[p[i]];
    return sa;
    }

  public static String[] sort( String[] a, int keyCol, boolean makeList )
    { // Note the value of makeList is immaterial
    int p[] = sort( a, keyCol );
    String sa[] = new String[a.length];
    for (int i=0; i<a.length; ++i) sa[i] = a[p[i]];
    return sa;
    }

  public static String[] sort( String[] a, int fieldP[], boolean makeList )
    { // Note the value of makeList is immaterial
    int p[] = sort( a, fieldP );
    String sa[] = new String[a.length];
    for (int i=0; i<a.length; ++i) sa[i] = a[p[i]];
    return sa;
    }

  public static int[] sort( Vector v, int fieldP[] )
    {
    // Try out the new method.  If it doesn't work, just remove the next line
    // and use the code that follows instead.
    return sortX( v, fieldP );
    
    // this method was modified 3-26-03 to perform a case-insensitive sort
    //!! it should be an option, but for now it's just the two calls to
    // toUpperCase() flagged below.
    // Note that this code does not implement sort in the manner described in
    // the comments herein
//    int nItems = v.size();
//    int[] answer = new int[nItems];
//    Vector inV = new Vector( v.size() );
//    int keyLength = 0;
//    for (int i=0; i<fieldP.length; i+=2)
//        keyLength += (fieldP[i+1] - fieldP[i] + 1);
//    StringBuffer sb = new StringBuffer( keyLength );
//    for (int i=0; i<keyLength; ++i) sb.append( (char)'\255' );
//    String afterLast = sb.toString();
//    int p=0;
//    int nextIndex=-1;
//    String compositeKey;
//
//    for (int i=0; i<v.size(); ++i)
//      {
//      compositeKey = "";
//      for (int j=0; j<fieldP.length; j+=2)
//        if ( fieldP[j+1] > 0 ) compositeKey += v.elementAt( i ).toString().substring( fieldP[j], fieldP[j+1] );
//        else compositeKey += v.elementAt( i ).toString().substring( fieldP[j] );
//      inV.addElement( compositeKey );
//      }
//
//    // This is an n**2 sort -- not the fastest way to do it!
//    while ( p < nItems )
//      {
//      String t = afterLast;
//      for (int i=0; i<inV.size(); ++i )
//        {
//        if ( ((String)inV.elementAt( i )).toUpperCase().compareTo( t ) < 0 ) // case insensitive
//          {
//          t = ((String)inV.elementAt( i )).toUpperCase(); // case insensitive
//          nextIndex = i;
//          }
//        }
//      answer[p++] = nextIndex;
//      inV.setElementAt( afterLast, nextIndex );
//      }
//    return answer;
    }

  private static Vector buildVector( String[] sa )
  {
    Vector v = new Vector( sa.length );
    for (int i=0; i<sa.length; ++i) v.addElement( sa[i] );
    return v;
  }

  public static void sortFile
    ( String infileName
    , String outfileName
    , boolean ascending
    )
  throws IOException
  {
      sortFile( infileName, outfileName, ascending, new int[] {0,0} );
  }

  public static void sortFile
    ( String infileName
    , String outfileName
    , boolean ascending
    , int keyCol
    )
  throws IOException
  {
      sortFile( infileName, outfileName, ascending, new int[] { keyCol,0 } );
  }

  public static void sortFile
    ( String infileName
    , String outfileName
    , boolean ascending
    , int fieldP[]
    )
  throws IOException
    {
    UsefulFile infile = new UsefulFile( infileName, "r" );
    UsefulFile outfile = new UsefulFile( outfileName, "w" );
    Vector inRecords = new Vector( 1000, 1000 );
    while ( !infile.EOF() )
      inRecords.addElement( infile.readLine( UsefulFile.EOL ) );
    infile.close();
    int sortP[] = sort( inRecords, fieldP );
    if ( ascending )
        for (int i=0; i<inRecords.size(); ++i)
            outfile.writeLine( (String)inRecords.elementAt( sortP[i] ) );
    else
        for (int i=inRecords.size()-1; i>=0; --i)
            outfile.writeLine( (String)inRecords.elementAt( sortP[i] ) );
    outfile.close();
    }

    /****************************************************
     *** hasDupKeys methods HAVE NOT BEEN TESTED !!!  ***
     ****************************************************/
    //!! There is a small problem here. hasDupKeys will not report a duplicate key
    //   when the keys differ only in case.  This could be changed by performing
    //   toLowerCase() on the compositeKey, but then these methods would report
    //   duplicate keys when they DO differ (only) in case.
    public static boolean hasDupKeys( Vector v )
    {
        return hasDupKeys( v, 0 );
    }

    public static boolean hasDupKeys( Vector v, int keyCol )
    {
        return hasDupKeys( v, new int[] { keyCol, 0 } );
    }

    public static boolean hasDupKeys( Vector v, int fieldP[] )
    {   // Note that v does not have to be sorted
        Vector inV = new Vector( v.size() );
        for (int i=0; i<v.size(); ++i)
        {
            String compositeKey = getCompositeKey( v.elementAt( i ).toString(), fieldP );
            if ( inV.contains( compositeKey ) ) return true;
            inV.addElement( compositeKey );
        }
        return false;
    }

    public static boolean hasDupKeys( String[] s )
    {
        return hasDupKeys( s, 0 );
    }

    public static boolean hasDupKeys( String[] s, int keyCol )
    {
        return hasDupKeys( s, new int[] {keyCol, 0} );
    }

    public static boolean hasDupKeys( String[] s, int fieldP[] )
    {
        return hasDupKeys( buildVector( s ), fieldP );
    }

    /***** EXPERIMENT FOR DOING THIS A DIFFERENT WAY *****/
    // Since the Collections.sort() do not provide a tagged sort, we create one:
    // a) Build a Vector with elements consisting of
    //    compositeKey + original-record-No
    // b) Sort this Vector using Collections
    // c) Construct the pointer array by extracting the original record numbers
    //    from the sorted Vector.
    // Note that this is a stable sort, since the original record number
    // actually becomes part of the sort field.
    // In addition, this method allows multiple, arbitrary key fields.
    
    public static int[] sortX( Vector v, int fieldP[] )
    {
        int answer[] = new int[v.size()];
        Vector s = buildKeys( v, fieldP );
        FirstComparator comparator = new FirstComparator();
        java.util.Collections.sort( s, comparator );
        for (int i=0; i<v.size(); ++i )
        {
            String record = (String)s.elementAt( i );
            answer[i] = Integer.parseInt( record.substring( record.length() - tagLength( v ) ) );
        }
        return answer;
    }

// OBSOLETE
//    private static Vector fix( Vector vIn, int fieldP[] )
//    {
//        // We must ensure that all of the records are long enough to include all of
//        // the key fields (to avoid exceptions while constructing the composite keys).
//        Vector v = new Vector( vIn.size() );
//        int minLength = maxP( fieldP ) + 1;
//        for (int i=0; i<vIn.size(); ++i)
//        {
//            String record = (String)vIn.elementAt( i );
//            while ( record.length() < minLength ) record += " ";
//            v.addElement( record );
//        }
//        return v;
//    }
//
    private static Vector buildKeys( Vector v, int fieldP[] )
    {   // returns a vector containing:
        //    compositeKey + recordNumber
        String tagFormat = zeros.substring( 0, tagLength( v ) );
        Vector keys = new Vector( v.size() );
        for (int i=0; i<v.size(); ++i)
            keys.addElement( getCompositeKey( v.elementAt( i ).toString(), fieldP )
                + Strings.format( i, tagFormat ) );
        return keys;
    }

    private static int tagLength( Vector v )
    {
        return Integer.toString( v.size() ).length();
    }

    private static String getCompositeKey( String record, int fieldP[] )
    {
        String compositeKey = "";
        int m = maxP( fieldP );
        // if record.length() < max( fieldP ) append " " to record here
        while ( record.length() < m ) record += " ";
        for (int j=0; j<fieldP.length; j+=2)
            if ( fieldP[j+1] > 0 ) compositeKey += record.substring( fieldP[j], fieldP[j+1] );
            else compositeKey += record.substring( fieldP[j] );
        return compositeKey;
    }

    private static int maxP( int fieldP[] )
    {
        int max = 0;
        for (int j=0; j<fieldP.length; ++j)
            if ( fieldP[j] > max ) max = fieldP[j];
        return max;
    }

    public static int invertP ( int p[], int iP )
    {
        for (int i=0; i<p.length; ++i)
            if ( p[i] == iP ) return i;
        return -1;
    }

// OBSOLETE
//    private static int compositeKeyLength( Vector v, int fieldP[] )
//    {
//        return compositeKeyLength( (String)v.elementAt(0), fieldP );
//    }
//
//    private static int compositeKeyLength( String record, int fieldP[] )
//    {
//        return getCompositeKey( record, fieldP ).length();
//    }
//
//    private static boolean isFixedLength( Vector v )
//    {
//        int l = ((String)v.elementAt( 0 )).length();
//        for (int i=1; i<v.size(); ++i )
//            if ( ((String)v.elementAt( i )).toString().length() != l ) return false;
//        return true;
//    }
//
//    public static final boolean violatesFixedLengthRule( Vector v, int fieldP[] )
//    {
//        for (int j=0; j<fieldP.length; j+=2)
//            if ( fieldP[j+1] == 0 )
//                if ( !isFixedLength( v ) ) return true;
//        return false;
//    }
//
    /***** END OF EXPERIMENTS *****/

    /***** STAND-ALONE APPLICATION FOR SORTING A FILE *****/
    
    // Use:  sorts i=<infile> o=<outfile>
    //       -a for ascending sort (default)
    //       -d for descending sort
    //        k=<fieldP>[,<fieldP>]...
    //          fieldP = b-e:  begin & end (0-based) column numbers for sort key(s)
    //          defaults to k=0-0:  use the entire record as the key
    
    public static void main( String[] args )
    {
        Clip clip = null;
    try { clip = new Clip( args, new String[] { "k=0-0" } ); }
    catch (UtilitiesException ux) { Console.println( ux.getMessage() ); }
    if ( !clip.isSet( "i" ) || !clip.isSet( "o" ) )
      {
      Console.println( "Use: sorts i=<infile> o=<outfile> [-a | -d] [[k=<fieldP>][,<fieldP>]...]" );
      Console.println( "  -a for ascending sort (default); -d for descending sort" );
      Console.println( "  fieldP = b-e: begin & end column numbers for sort key(s)" );
      Console.println( "    (If e is 0), the rest of the record is used as the key.)" );
      Console.println( "  If k is not specified, the entire record is the key." );
      Console.println( "    (This is equivalent to 'k=0-0' which is the default )" );
      System.exit( 1 );
      }
    try
      {
      StringTokenizer stFields = new StringTokenizer( clip.getParam( "k" ), "," );
      int nKeys = 0;
      while ( stFields.hasMoreElements() ) { stFields.nextElement(); ++nKeys; }
//Console.println( "nKeys=" + nKeys );
      int fieldP[] = new int[nKeys * 2];
      int iKey = 0;
      stFields = new StringTokenizer( clip.getParam( "k" ), "," );
      while ( stFields.hasMoreElements() )
        {
        StringTokenizer stCols = new StringTokenizer( (String)stFields.nextElement(), "-" );
        if ( stCols.hasMoreElements() ) fieldP[iKey] = Strings.parseInt( (String)stCols.nextElement() );
        else fieldP[iKey] = 0;
        if ( stCols.hasMoreElements() ) fieldP[iKey+1] = Strings.parseInt( (String)stCols.nextElement() );
        else fieldP[iKey+1] = 0;
        iKey += 2;
        }
//Console.print( "fieldP: " );
//for (int i=0; i<fieldP.length; ++i) Console.print( "[" + fieldP[i] + "," + fieldP[++i] + "] " );
//Console.println( "ascending=" + !clip.isSet( 'd' ) );
//Console.println( "sortFile( " + clip.getParam( "i" ) + ", " + clip.getParam( "o" ) + ", " +
//(!clip.isSet( 'd' ) ) + ", fieldP )" );
      Sorts.sortFile( clip.getParam( "i" ), clip.getParam( "o" ), !clip.isSet( 'd' ), fieldP );
      System.exit( 0 );
      }
    catch (IOException iox)
      {
      Console.println( iox.getMessage() );
      }
    catch (Exception x)
      {
      Console.println( x.getMessage() );
      }
    System.exit( 1 );
    }
    /***** END OF STAND-ALONE APPLICATION FOR SORTING A FILE *****/

    /***** FOR TESTING "NEW" METHODS *****
    public static void main( String args[] )
    {
        // Test #1 - numbers
        int testSize = 100;
        Vector v = new Vector( testSize );
        int p[] = null;
        java.util.Random random = new java.util.Random( System.currentTimeMillis() );
        for (int i=1; i<testSize; ++i)
        {
            int r = random.nextInt();
            if ( r < 0 ) r = -r;
            v.addElement( Strings.format( r, "0000000000" ) + "X" );
        }
        p = Sorts.sortX( v, new int[] {0,9, 20,21} );
        for (int i=0; i<p.length; ++i)
            Console.println( "'" + (String)v.elementAt( p[i] ) + "'" );
        Console.println( "hasDupKeys=" + Sorts.hasDupKeys( v, new int[]{0,0} ) );

        // Test #2 - a mixture of alphanumeric and special characters
        String d1 = "ABCDWXYZabcdwxyz1234[];',./<>?:\"{}=+-_`~!@#$%^&*()X"; // 'X' is duplicated
        v = new Vector( 100 );
        for (int i=0; i<d1.length(); ++i) v.addElement( d1.charAt( i ) + " " );
        p = Sorts.sortX( v, new int[] {0,0} );
        for (int i=0; i<p.length; ++i)
            Console.println( "'" + (String)v.elementAt( p[i] ) + "'" );
        Console.println( "hasDupKeys=" + Sorts.hasDupKeys( v, new int[]{0,0} ) + "  (should be true)");
    }   
    /***** END OF "NEW" METHODS TEST *****/

  static String zeros = "00000000000000000"; // I hope this is enough zeros.

  }

/********** NOTES FROM JDC TECH TIPS October 23, 2001 ************
SORTING LISTS

The List implementations found in the Java Collections Framework
are Vector, ArrayList, and LinkedList. These collections offer 
indexed access to groups of objects. They provide support for 
adding and removing elements. However they offer no built-in 
support for sorting elements. 

To sort List elements you can use the sort() method in the 
java.util.Collections class. You can either pass the method 
a List object, or you can pass it a List and a Comparator. If the 
elements in the List are all the same class type and that class 
implements the Comparable interface, you can simply call 
Collections.sort(). If, however, the class doesn't implement 
Comparator, you can pass the sort() method a Comparator to do the
sort. You might also want to pass the sort() method a Comparator
if you don't want to use the default sort order for the elements 
of the class. If the elements in the List are not all the same 
class type, you're out of luck as far as sorting goes -- unless
you write a special cross-class Comparator.

What about sorting order? If the elements are String objects, 
the default sort order is based on the character codes, basically 
the ASCII/Unicode value for each character. If you strictly work 
with English, the default sort order is usually sufficient 
because it will sort A-Z first, followed by the lowercase letters 
a-z. However, if you work with non-English words, or you just 
want a different sorting order, that's where the second variety 
of Collections.sort() comes in handy. For instance, say you want 
to sort the elements of the List in the reverse natural order of 
the strings. To do this, you can get a reverse Comparator from 
the Collections class with reverseOrder(). Then, you pass the 
reverse Comparator to the sort() method. In other words, you do 
the following:

    List list = ...;
    Comparator comp = Collections.reverseOrder();
    Collections.sort(list, comp);
    
If the list contained the terms Man, man, Woman, and woman, the 
sorted list would be Man, Woman, man, woman. Nothing too 
complicated here. An important thing to remember is 
Collections.sort() does an in-place sort. If you need to retain
the original order, make a copy of the collection first, then 
sort it, like this:

    List list = ...;
    List copyOfList = new ArrayList(list);
    Collections.sort(copyOfList);
  
Here, the sorted list is Man, Woman, man, woman, but the original 
list (Man, man, Woman, and woman) is retained.    

So far the sorting has been case-sensitive. How would you do a
case-insensitive sort? One way to do it is to implement a  
Comparator like this:

    public static class CaseInsensitiveComparator 
        implements Comparator {
      public int compare(Object element1, 
          Object element2) {
        String lower1 = element1.toString().toLowerCase();
        String lower2 = element2.toString().toLowerCase();
        return lower1.compareTo(lower2);
      }
    }

There is a slight problem with this approach. The sort() 
algorithm provides stable sorting, keeping equal elements in the 
original order. This means that a list that contains the two
elements "woman" and "Woman" would sort differently based upon
which of the two elements appear first in the list.

What about language differences? The java.text package provides 
Collator and CollationKey classes for doing language-sensitive 
sorting. Here's an example:

    public static class CollatorComparator 
        implements Comparator {
      Collator collator = Collator.getInstance();
      public int compare(Object element1, 
          Object element2) {
        CollationKey key1 = collator.getCollationKey(
          element1.toString());
        CollationKey key2 = collator.getCollationKey(
          element2.toString());
        return key1.compareTo(key2);
      }
    }

Instead of sorting on the actual string, you sort on a collation 
key. Not only does this provide consistent case-insensitive 
sorting, but it also works across languages. In other words, if 
you sort a combination of Spanish and non-Spanish words, the word 
[manana Spanish] (tomorrow) would come before mantra. If you don't use a 
Collator, [manana Spanish] would come after mantra.

Here's a program that does various types of sorting (default, 
case-insensitive, and language-sensitive) on a List:

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class SortIt {
    
  public static class CollatorComparator 
      implements Comparator {
    Collator collator = Collator.getInstance();
    public int compare(Object element1, 
        Object element2) {
      CollationKey key1 = collator.getCollationKey(
        element1.toString());
      CollationKey key2 = collator.getCollationKey(
        element2.toString());
      return key1.compareTo(key2);
    }
  }

  public static class CaseInsensitiveComparator 
      implements Comparator {
    public int compare(Object element1, 
        Object element2) {
      String lower1 = element1.toString().
        toLowerCase();
      String lower2 = element2.toString().
        toLowerCase();
      return lower1.compareTo(lower2);
    }
  }

  public static void main(String args[]) {
    String words[] = 
      {"man", "Man", "Woman", "woman", 
       "Manana", "manana", "[manana Spanish]", "[Manana Spanish]",
       "Mantra", "mantra", "mantel", "Mantel"
      };

    // Create frame to display sortings
    JFrame frame = new JFrame("Sorting");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPane = frame.getContentPane();
    JTextArea textArea = new JTextArea();
    JScrollPane pane = new JScrollPane(textArea);
    contentPane.add(pane, BorderLayout.CENTER);

    // Create buffer for output
    StringWriter buffer = new StringWriter();
    PrintWriter out = new PrintWriter(buffer);

    // Create initial list to sort
    List list = new ArrayList(Arrays.asList(words));
    out.println("Original list:");
    out.println(list);
    out.println();

    // Perform default sort
    Collections.sort(list);
    out.println("Default sorting:");
    out.println(list);
    out.println();

    // Reset list 
    list = new ArrayList(Arrays.asList(words));

    // Perform case insensitive sort
    Comparator comp = new CaseInsensitiveComparator();
    Collections.sort(list, comp);
    out.println("Case insensitive sorting:");
    out.println(list);
    out.println();

    // Reset list
    list = new ArrayList(Arrays.asList(words));

    // Perform collation sort
    comp = new CollatorComparator();
    Collections.sort(list, comp);
    out.println("Collator sorting:");
    out.println(list);
    out.println();

    // Fill text area and display
    textArea.setText(buffer.toString());
    frame.pack();
    frame.show();
  }
}

If your primary concern is ordered access, perhaps a List isn't 
the best data structure for you. As long as your collection 
doesn't have duplicates, you can keep the elements in a TreeSet 
(with or without providing a Comparator). Then, the elements will 
always be in sorted order.

For more information about sorting Lists, see "Introduction to 
the Collections Framework" 
(http://java.sun.com/jdc/onlineTraining/collections/index.html).

******/

