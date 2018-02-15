/*
 * Shared.java
 *
 * Created on March 10, 2003, 6:27 PM
 */

package com.extant.utilities;
import java.util.Vector;

/**
 *
 * @author  jms
 */
public class Shared
{
    private long data;
    private boolean writeable = true;

    public Shared()
    {
    }

    synchronized public void setSharedData( long data )
    {
        if (!writeable)
            try { wait (); } // waits for a notify() from somebody
            catch (InterruptedException e) {}

        this.data = data;
        writeable = false;
        this.notify();
    }

    synchronized public long getSharedData ()
    {
        if (writeable)
            try { wait (); } // waits for a notify() from someone
            catch (InterruptedException e) { }

        writeable = true;
        this.notify();
        return data;
    }
        
    public boolean isWritable()
    {
        return writeable;
    }
    
}

/***** Here is the original example:

// ProdCons2.java

class ProdCons2
{
   public static void main (String [] args)
   {
      Shared s = new Shared ();
      new Producer (s).start ();
      new Consumer (s).start ();
   }
}

class Shared
{
   private char c = '\u0000';
   private boolean writeable = true;

   synchronized void setSharedChar (char c)
   {
      if (!writeable)
         try
         {
            wait ();
         }
         catch (InterruptedException e) {}

      this.c = c;
      writeable = false;
      notify ();
   }

   synchronized char getSharedChar ()
   {
      if (writeable)
         try
         {
            wait ();
         }
         catch (InterruptedException e) { }

      writeable = true;
      notify ();

      return c;
   }
}

class Producer extends Thread
{
   private Shared s;

   Producer (Shared s)
   {
      this.s = s;
   }

   public void run ()
   {
      for (char ch = 'A'; ch <= 'Z'; ch++)
      {
           try
           {
              Thread.sleep ((int) (Math.random () * 4000));
           }
           catch (InterruptedException e) {}

           s.setSharedChar (ch);
           System.out.println (ch + " produced by producer.");
      }
   }
}

class Consumer extends Thread
{
   private Shared s;

   Consumer (Shared s)
   {
      this.s = s;
   }

   public void run ()
   {
      char ch;

      do
      {
         try
         {
            Thread.sleep ((int) (Math.random () * 4000));
         }
         catch (InterruptedException e) {}

         ch = s.getSharedChar ();
         System.out.println (ch + " consumed by consumer.");
      }
      while (ch != 'Z');
   }
}
***** End of Example *****/
