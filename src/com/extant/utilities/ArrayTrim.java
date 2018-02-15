/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.extant.utilities;

/**
 *
 * @author jms
 */
public class ArrayTrim
{
    public ArrayTrim()
    {
    }

    public static int[] trim( int[] in, int length )
    {
        int out[] = new int[length];
        for (int i=0; i<length; ++i) out[i] = in[i];
        return out;
    }

    public static long[] trim( long[] in, int length )
    {
        long out[] = new long[length];
        for (int i=0; i<length; ++i) out[i] = in[i];
        return out;
    }

    public static double[] trim( double[] in, int length)
    {
        double out[] = new double[length];
        for (int i=0; i<length; ++i) out[i] = in[i];
        return out;
    }

    public static float[] trim( float[] in, int length)
    {
        float out[] = new float[length];
        for (int i=0; i<length; ++i) out[i] = in[i];
        return out;
    }
}

