package com.extant.utilities;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/* This class operates similarly to StringTokenizer, except that it handles
 * input which may contain quoted strings, which in turn may contain
 * tokens.  This tokenizer will ignore any tokens contained within quoted
 * strings.
 */

public class ParseWithQuotes
implements Enumeration
{
  String token;
  String delimiter; // the string delimiter ( usually ' or " )
  StringTokenizer st;

  public ParseWithQuotes( String s, char token, char delimiter )
  throws UtilitiesException // for unmatched delimiters
    {
    this.token = new String( new char[] {token} );
    this.delimiter = new String( new char[] {delimiter} );
    st = new StringTokenizer( Strings.preParse( s, this.token, this.delimiter ), this.token );
    }

  public boolean hasMoreElements()
    {
    return st.hasMoreElements();
    }

  public Object nextElement()
  throws NoSuchElementException
    {
    return Strings.postParse( (String)st.nextElement(), this.token );
    }

  /***** For testing: *****
  public static void main( String[] args )
    {
    String[] test = new String[] { "1,2,3,'this is easy',4,5,6"
                                 , "1,2,3,'this is harder, right?',4,5,6"
                                 , "1,2,3,'broken,4,5,6"
                                 , "'JMS',20,20000.00"
                                 };
    int i=-1;
    for (i=0; i<test.length; ++i)
      {
      try
        {
        ParseWithQuotes pwq = new ParseWithQuotes( test[i], ',', '\'' );
        Console.println( "elements:" );
        while ( pwq.hasMoreElements() ) Console.print( "   <" + pwq.nextElement() + ">" );
        Console.println( "" );
        }
      catch (UtilitiesException ux)
        {
        Console.println( ux.getMessage() + " in line:" );
        Console.println( test[i] );
        }
      catch (NoSuchElementException nsex)
        {
        Console.println( nsex.getMessage() );
        }
      }
    Console.prompt( "Done ..." );
    }
  /*****/
}
