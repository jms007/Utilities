package com.extant.utilities;
import java.io.*;

public class Console
  {
  public static String prompt( String question, String defaultAnswer )
    {
    try
      {
      if (question.length() > 0) System.out.print( question );
      if (defaultAnswer == null) defaultAnswer = "";
      if (defaultAnswer.length() > 0)
        System.out.print( "(" + defaultAnswer + ") " );
      byte bans[] = new byte[80];
      System.in.read( bans );
      String answer = new String( bans ).trim();
      if ( answer.length() == 0 ) answer = defaultAnswer;
      return ( answer );
      }
    catch (IOException iox)
      {
      //iox.printStackTrace();  // probably is using a file for execution log
      return ( defaultAnswer );
      }
    }

  public static String prompt( String question )
    {
    return ( prompt ( question, "" ) );
    }
 
  public static String getKBInput()
    {
    return prompt( "" );
    }

  public static void println( String out )
    {
    System.out.println( out );
    }

  public static void print( String out )
    {
    System.out.print( out );
    }
  
  /***** FOR TESTING *****
  public static void main( String args[] )
    {
    String defaultAnswer = "Nowhere, thanks";
    try
      {
      while (true)
        {
        String ans = prompt( "Where do you want to go today? ", defaultAnswer );
        System.out.println( "Answer(" + ans.length() + "): '" + ans + "'" );
        if (ans.equals( defaultAnswer ) ) break;
        }
      prompt( "\nDone ..." );
      }
    catch (Exception e)
      {
      e.printStackTrace();
      prompt( "Error ..." );
      }
    }
  /*****/
  }

