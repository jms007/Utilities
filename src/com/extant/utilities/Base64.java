package com.extant.utilities;

// A base-64 encoder/decoder
    // This encodes binary data as printable ASCII
    // characters.  Three 8-bit binary bytes are turned into four 6-bit
    // values, like so:
    //
    //   [11111111]  [22222222]  [33333333]
    //
    //   [111111] [112222] [222233] [333333]
    //
    // Then the 6-bit values are represented using the characters "A-Za-z0-9+/".

public class Base64
  {

  public Base64()
    {
    }

  public static String base64Encode( String srcString )
    {
    return base64Encode( srcString.getBytes() );
    }

  public static String base64Encode( byte[] src )
    {
    StringBuffer encoded = new StringBuffer();
    int i, phase = 0;
    char c = 0;

    for ( i = 0; i < src.length; ++i )
      {
      switch ( phase )
        {
        case 0:
        c = b64EncodeTable[( src[i] >> 2 ) & 0x3f];
        encoded.append( c );
  	     c = b64EncodeTable[( src[i] & 0x3 ) << 4];
  	     encoded.append( c );
  	     ++phase;
  	     break;
  	     case 1:
        // Jef's original code (sign-extention got 'im)
        //c = b64EncodeTable[( b64DecodeTable[c] | ( src[i] >> 4 ) ) & 0x3f];
        c = b64EncodeTable[( b64DecodeTable[c] | ( (src[i]&0xFF) >> 4 ) ) & 0x3f];
        encoded.setCharAt( encoded.length() - 1, c );
        c = b64EncodeTable[( src[i] & 0xf ) << 2];
        encoded.append( c );
        ++phase;
        break;
        case 2:
        // Jef's original code (sign-extention got 'im again)
        //c = b64EncodeTable[( b64DecodeTable[c] | ( src[i] >> 6 ) ) & 0x3f];
        c = b64EncodeTable[( b64DecodeTable[c] | ( (src[i]&0xFF) >> 6 ) ) & 0x3f];
        encoded.setCharAt( encoded.length() - 1, c );
        c = b64EncodeTable[src[i] & 0x3f];
        encoded.append( c );
        phase = 0;
        break;
        }
      }
    /* Pad with ='s. */
    while ( phase++ < 3 ) encoded.append( '=' );
    return encoded.toString();
    }

  public static byte[] base64Decode( String encoded )
  throws UtilitiesException
    {
    return base64Decode( encoded.getBytes() );
    }

  public static byte[] base64Decode( byte[] encoded )
  throws UtilitiesException
    {
    byte[] ans;
    byte work[] = new byte[((encoded.length / 4) + 1)* 3]; // just a guess
    int b=0;
    int pw = 0;
    int pi = 0;
    int p = 0;
    int t;
    int c;
    while ( true )
      {
      if ( pi >= encoded.length ) break; // 4-3-03
      c = encoded[pi++];
      if ( c == '=' ) break;
      t = b64DecodeTable[c];
      if ( t < 0 )
        throw new UtilitiesException( UtilitiesException.BAD_BASE64_BYTE );
      if ( p == 0 ) b = (t & 0x3F) << 2;
      else if ( p == 1 )
        {
        work[pw++] = (byte)(b | ((t >> 4) & 0x3) );
        b = t & 0xF;
        }
      else if ( p == 2 )
        {
        work[pw++] = (byte)( (b << 4) | ((t >> 2) & 0xF) );
        b = t & 3;
        }
      else if ( p == 3 )
        {
        work[pw++] = (byte)( (b << 6) | (t & 0x3F) );
        p = -1;
        }
      ++p;
      }
    ans = new byte[pw];
    for (int i=0; i<pw; ++i) ans[i] = work[i];
    return ans;
    }

  private static char b64EncodeTable[] =
    {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',  // 00-07
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',  // 08-15
    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',  // 16-23
    'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',  // 24-31
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',  // 32-39
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v',  // 40-47
    'w', 'x', 'y', 'z', '0', '1', '2', '3',  // 48-55
    '4', '5', '6', '7', '8', '9', '+', '/'   // 56-63
    };
  private static int b64DecodeTable[] =
    {
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 00-0F
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 10-1F
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,62,-1,-1,-1,63,  // 20-2F
    52,53,54,55,56,57,58,59,60,61,-1,-1,-1,-1,-1,-1,  // 30-3F
    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,  // 40-4F
    15,16,17,18,19,20,21,22,23,24,25,-1,-1,-1,-1,-1,  // 50-5F
    -1,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,  // 60-6F
    41,42,43,44,45,46,47,48,49,50,51,-1,-1,-1,-1,-1,  // 70-7F
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 80-8F
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 90-9F
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // A0-AF
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // B0-BF
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // C0-CF
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // D0-DF
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // E0-EF
    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1   // F0-FF
    };

  /***** TEST PROGRAM #1 *****
  public static void main( String[] args )
    {
    byte[] tb;
    String encoded;
    byte[] result;
    int k;
    String[] ts = new String[8];
    ts[0] = "1";
    ts[1] = "12";
    ts[2] = "123";
    ts[3] = "1234";
    ts[4] = "12345";
    ts[5] = "123456";
    ts[6] = "1234567";
    ts[7] = "12345678";
    int[] t =
      { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
        0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
        0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
        0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
        0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
        0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
        0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F,
        0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47,
        0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F,
        0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57,
        0x58, 0x59, 0x5A, 0x5B, 0x5C, 0x5D, 0x5E, 0x5F,
        0x60, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67,
        0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F,
        0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77,
        0x78, 0x79, 0x7A, 0x7B, 0x7C, 0x7D, 0x7E, 0x7F, // it breaks right here

        0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87,
        0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F,
        0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97,
        0x98, 0x99, 0x9A, 0x9B, 0x9C, 0x9D, 0x9E, 0x9F,
        0xA0, 0xA1, 0xA2, 0xA3, 0xA4, 0xA5, 0xA6, 0xA7,
        0xA8, 0xA9, 0xAA, 0xAB, 0xAC, 0xAD, 0xAE, 0xAF,
        0xB0, 0xB1, 0xB2, 0xB3, 0xB4, 0xB5, 0xB6, 0xB7,
        0xB8, 0xB9, 0xBA, 0xBB, 0xBC, 0xBD, 0xBE, 0xBF,
        0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7,
        0xC8, 0xC9, 0xCA, 0xCB, 0xCC, 0xCD, 0xCE, 0xCF,
        0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7,
        0xD8, 0xD9, 0xDA, 0xDB, 0xDC, 0xDD, 0xDE, 0xDF,
        0xE0, 0xE1, 0xE2, 0xE3, 0xE4, 0xE5, 0xE6, 0xE7,
        0xE8, 0xE9, 0xEA, 0xEB, 0xEC, 0xED, 0xEE, 0xEF,
        0xF0, 0xF1, 0xF2, 0xF3, 0xF4, 0xF5, 0xF6, 0xF7,
        0xF8, 0xF9, 0xFA, 0xFB, 0xFC, 0xFD, 0xFE, 0xFF,
      };

    try
      {
      for (int i=0; i<ts.length; ++i)
        {
        //tb = ts[i].getBytes();
        tb = new byte[ t.length ];
        for (int j=0; j<t.length; ++j) tb[j] = (byte)t[j];
        encoded = base64Encode( tb );
        result = base64Decode( encoded.getBytes() );
        Console.print( "input[" + ts[i].length() + "]" + ts[i] +
          " encoded[" + encoded.length() + "]" + encoded +
          " decoded[" + result.length + "]" + new String( result ) );
        if ( result.length != tb.length )
          Console.print( " Length Error(" + tb.length + "/" + result.length + ")" );
        for ( k=0; k<tb.length; ++k )
          if ( result[k] != tb[k] ) break;
        if ( k < tb.length ) Console.println( " Error" );
        else Console.println( " Good" );
        }
      }
    catch ( Exception x )
      {
      x.printStackTrace();
      }
    Console.prompt( "Done ..." );
    }
  /*****END OF TEST #1 *****/
    
  /***** TEST PROGRAM #2 *****/
  public static void main( String[] args )
  {
    while ( true )
    {
        try
        {
            String which = Console.prompt( "Encode or Decode? " );
            String in, out;
            if ( which.length() == 0 ) break;
            while ( true )
            {
                if ( which.startsWith( "E" ) )
                {
                    in = Console.prompt( "Enter String to encode: " );
                    if ( in.length() == 0 ) break;
                    out = base64Encode( in );
                    Console.println( "'" + in  + "' --> '" + out+ "'" );
                    Console.println( "'" + out + "' --> '" + new String( base64Decode( out ) ) + "'" );
                }
                else if ( which.startsWith( "D" ) )
                {
                    in = Console.prompt( "Enter String to decode: " );
                    if ( in.length() == 0 ) break;
                    out = new String( base64Decode( in ) );
                    Console.println( "'" + in + "' --> '" + out + "'" );
                    Console.println( "'" + out + "' --> '" + base64Encode( out ) + "'" );
                }
                else break;
            }
        }
        catch (UtilitiesException ux)
        {
            Console.println( ux.getMessage() );
        }
    }
  }
  /***** END OF TEST #2 *****/
}

