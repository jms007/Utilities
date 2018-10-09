/*
 * ComboMatch.java
 * Given a single value (the error) and a list of values (the detail),
 * this class produces a list of all possible combinations of the detail values
 * which sum to the error value.  (All values are positive long integers.)
 *
 * Execution time increases exponentially with the number of values.
 * 20 values (1,048,575 possibilities) executes in less than 1 sec; each additional value
 * doubles the execution time.
 * 30 values (1,073,741,823 possibilities) executes in 23:34
 * We have not tested with a larger number of values.
 *
 * Created on May 8, 2006, 8:02 PM
 */

package com.extant.utilities;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author jms
 */
public class ComboMatch
{
	public ComboMatch(long error, long details[])
	{
		this.error = error;
		this.details = details;
	}

	public Vector<int[]> findCombos()
	{
		Console.println("There are " + new Possibilities(details.length).getNCombos() + " possibile combinations.");
		Enumeration possibilities = new Possibilities(details.length);
		Vector<int[]> answers = new Vector<int[]>(10, 10);
		while (possibilities.hasMoreElements())
		{
			int combo[] = (int[]) possibilities.nextElement();
			if (match(error, details, combo))
				answers.addElement(combo);
		}
		return answers;
	}

	boolean match(long error, long details[], int combo[])
	{
		long sum = 0L;
		for (int i = 0; i < combo.length; ++i)
			sum += details[combo[i]];
		return (sum == error);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	/***** FOR TESTING *****/
	public static void main(String[] args)
	{
		long startTime;
		long endTime;

		startTime = System.currentTimeMillis();

		try
		{
			long details[] = initTest();
			Clip clip = new Clip(args, new String[] { "d=100" });
			long error = Strings.parseLong(clip.getParam("d"));
			ComboMatch comboMatch = new ComboMatch(error, details);
			Vector<int[]> answers = new Vector<int[]>(10);
			answers = comboMatch.findCombos();
			if (answers.size() == 0)
			{
				Console.println("No combination sums to " + error);
				return;
			}
			endTime = System.currentTimeMillis();
			Console.println(Strings.plurals("combination", answers.size()) + " summing to " + error + ":");
			for (int i = 0; i < answers.size(); ++i)
			{
				int combo[] = answers.elementAt(i);
				Console.print("combination " + i + ": ");
				for (int j = 0; j < combo.length; ++j)
					Console.print("[" + combo[j] + "]=" + details[combo[j]] + " ");
				Console.println("");
			}
			System.out.println("Run time: " + (endTime - startTime) + " milliseconds" + " (excluding report)");
		} catch (UtilitiesException ux)
		{
			Console.println(ux.getMessage());
		}
	}

	static long[] initTest()
	{
		long details[] = new long[20];
		details[0] = 5L;
		details[1] = 10L;
		details[2] = 20L;
		details[3] = 30L;
		details[4] = 40L;
		details[5] = 50L;
		details[6] = 60L;
		details[7] = 70L;
		details[8] = 80L;
		details[9] = 90L;
		details[10] = 100L;
		details[11] = 110L;
		details[12] = 120L;
		details[13] = 130L;
		details[14] = 140L;
		details[15] = 150L;
		details[16] = 160L;
		details[17] = 170L;
		details[18] = 180L;
		details[19] = 190L;
		return details;
	}

	/***** END OF TEST CODE *****/

	long error;
	long details[];
}
