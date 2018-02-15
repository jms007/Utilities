//$Id: InsidePoly.java,v 1.1.1.1 2005/03/20 17:58:23 jms Exp $
/*
 * InsidePoly.java
 *
 * Created on February 7, 2004, 9:45 AM
 * The algorithms here were obatined from:
 *    http://cgm.cs.mcgill.ca/~msuder/courses/250/lectures/computational_geometry/
 */

package com.extant.utilities;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 *
 * @author  jms
 */
public class InsidePoly
{
    public InsidePoly()
    {
    }

    public static int orientation( Point2D.Double a, Point2D.Double b, Point2D.Double c )
    {
        // Algorithm orientation(a, b, c)
        // Input: Points a, b and c.
        // Output: LEFT if b is a left turn, RIGHT if b is right turn 
        //         and COLLINEAR if b is not a turn on the path from a to c
        //         consisting of segment ab and bc.
        // z <- (bx - ax)(cy - ay) - (cx - ax)(by - ay)
        // if (z > 0)
        //     return LEFT
        // else if (z < 0)
        //     return RIGHT
        // else
        //     return COLLINEAR

        double z = ( (b.getX() - a.getX()) * (c.getY() - a.getY()) )
                 - ( (c.getX() - a.getX()) * (b.getY() - a.getY()) );
        if ( z > 0 ) return LEFT;
        else if ( z < 0 ) return RIGHT;
        else return COLLINEAR;

    }

    public static boolean between( Point2D.Double a, Point2D.Double b, Point2D.Double c )
    {
        // Algorithm between(a,b,c)
        // Input: Collinear points a, b and c.
        // Output: Returns true if c is on ab and false otherwise.

        // if a.x < b.x then
        // return a.x <= c.x <= b.x
        // else if a.x > b.x then
        // return b.x <= c.x <= a.x
        // else if a.y < b.y then
        // return a.y <= c.y <= b.y
        // else
        // return b.y <= c.y <= a.y

             if ( a.getX() < b.getX() ) return ( (a.getX() <= c.getX()) && (c.getX() <= b.getX()) );
        else if ( a.getX() > b.getX() ) return ( (b.getX() <= c.getX()) && (c.getX() <= a.getX()) );
        else if ( a.getY() < b.getY() ) return ( (a.getY() <= c.getY()) && (c.getY() <= b.getY()) );
        else return ( (b.getY() <= c.getY()) && (c.getY() <= a.getY()) );
    }

    public static boolean intersecting
        ( Point2D.Double a
        , Point2D.Double b
        , Point2D.Double c
        , Point2D.Double d
        )
    {
        // Algorithm intersecting(a,b,c,d)
        // Input: Points a, b, c and d.
        // Output: Returns true if ab intersects cd; otherwise, false.

        // abc <- orientation(a,b,c)
        // abd <- orientation(a,b,d)
        // if abc # abd then
        // return orientation(c,d,a) # orientation(c,d,b)
        // else
        // return abc = COLLINEAR and (between(a,b,c) or between(a,b,d) or between(c,d,a))

        int abc = orientation( a, b, c );
        int abd = orientation( a, b, d );
        if ( abc != abd ) return ( orientation( c, d, a ) != orientation( c, d, b ) );
        else return ( (abc == COLLINEAR) && ( between( a, b, c ) || between( a, b, d ) || between( c, d, a ) ) );
    }

    public static boolean inside( Vector p, Point2D.Double q )
    {
        // Algorithm inside(P, q)
        // Input: P=p0p1...pn-1 is a simple polygon and q is a point.
        // Output: Returns true if q is inside P and false otherwise.

        // x <- 0
        // for i <- 0, 1, ..., n-1 do
        // if x <= pi.x then
        //     x <- pi.x + 1
        // r <- the point (x, q.y)

        // intersections <- 0
        // for i <- 0, 1, ..., n-1 do
        // if intersects(q, r, pi, pi+1 mod n) then
        //     intersections <- intersections + 1

        // return intersections mod 2 = 1
        double x = 0;
        for (int i=0; i<p.size(); ++i)
        {
            if ( x <= ((Point2D.Double)p.elementAt( i )).getX() )
                x = ((Point2D.Double)p.elementAt( i )).getX() + 1;
        }
        Point2D.Double r = new Point2D.Double( x, q.getY() );
        int intersections = 0;
        for (int i=0; i<p.size(); ++i)
            if ( intersecting( q, r, (Point2D.Double)p.elementAt(i),
                (Point2D.Double)p.elementAt( (i+1) % p.size() ) ) )
                ++intersections;
        return ( intersections & 1 ) == 1;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // Test #1
        Vector t1 = new Vector( 4 );
        t1.addElement( new Point2D.Double( 0, 0 ) );
        t1.addElement( new Point2D.Double( 0, 1 ) );
        t1.addElement( new Point2D.Double( 1, 1 ) );
        t1.addElement( new Point2D.Double( 1, 0 ) );

        // Test #2
        Vector t2 = new Vector( 6 );
        t2.addElement( new Point2D.Double( 240.14,  6000 ) );
        t2.addElement( new Point2D.Double( 240.14,  7700 ) );
        t2.addElement( new Point2D.Double( 242.43,  8800 ) );
        t2.addElement( new Point2D.Double( 243.94, 10400 ) );
        t2.addElement( new Point2D.Double( 248.78, 10400 ) );
        t2.addElement( new Point2D.Double( 248.78,  6000 ) );

        Vector t = t2; // Set this to the test you want to run

        // Run a test
        //InsidePoly insidePoly = new InsidePoly();
        while ( true )
        {
            Double xD = new Double( Console.prompt( "Enter x: " ) );
            double x = xD.doubleValue();
            Double yD = new Double( Console.prompt( "      y: " ) );
            double y = yD.doubleValue();
            Point2D.Double p = new Point2D.Double( x, y );
            Console.println( "inside=" + InsidePoly.inside( t, p ) );
        }
        
    }

    public static final int LEFT = 1;
    public static final int RIGHT = -1;
    public static final int COLLINEAR = 0;
}

