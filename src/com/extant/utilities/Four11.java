/*
 * Four11.java
 * Collects information about the directory tree starting at a specified root:
 *   builds a list of sub-directories in the tree (which includes the root)
 *   builds a list of qualifying files in the tree (defaults to all files)
 *   computes the total number of bytes in the qualifying files
 *   builds a JTree representing the directory structure
 *
 * Created on December 10, 2002, 11:03 AM
 */

package com.extant.utilities;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JScrollPane;
//import utilities.DisplayTree;

/**
 *
 * @author  jms
 */
public class Four11
    extends JFrame
{

    public Four11( String rootDir )
    throws UtilitiesException
    {
        this (rootDir, "*" );
    }

    public Four11( String rootDir, String match )
    throws UtilitiesException
    {
        if ( !new File( rootDir ).exists() )
            throw new UtilitiesException( UtilitiesException.FILENOTFOUND, rootDir );
        if ( !new File( rootDir ).isDirectory() )
            throw new UtilitiesException( UtilitiesException.NOT_DIRECTORY, rootDir );
        this.match = match;
        root = new DefaultMutableTreeNode( rootDir );
        DefaultTreeModel treeModel = new DefaultTreeModel( root );
        build( rootDir, root );
        tree = new JTree( treeModel );
    }
    
    private void build( String dir, DefaultMutableTreeNode parent )
    throws UtilitiesException
    {
        File file;
        if ( !dir.endsWith( File.separator ) ) dir += File.separator;
        dirList.addElement( dir );
        //String files[] = Sorts.sort( new Strings().getMatchingFiles( dir, match ), true );
        // We want all of the files in each directory (some of them may be directories)
        String files[] = Sorts.sort( new Strings().getMatchingFiles( dir, "*" ), true );
        for (int i=0; i<files.length; ++i)
        {
            file = new File( dir + files[i] );
            if ( file.isDirectory() )
            {
                DefaultMutableTreeNode x = new DefaultMutableTreeNode( files[i] );
                parent.add( x );
                build( dir + files[i], x );
            }
            else
            {
                // Then, include the files only if they match the pattern
                if ( Strings.match( match, files[i] ) )
                {
//Console.println( "match( " + files[i] + ", " + match + ") = " + Strings.match( match, files[i] ) );
                    fileList.addElement( dir + files[i] );
                    parent.add( new DefaultMutableTreeNode( formatNode( file ) ) );
                    nBytes += file.length();
                }
            }
        }
    }

    private String formatNode( File file )
    {
        String node = file.getName();
        if ( !reportSize && (dateFormat.length() == 0) ) return node;
        node += " [";
        if ( reportSize ) node += Strings.plurals( "byte", file.length() ) + " ";
        if ( dateFormat.length() > 0 )
            node += Strings.getLastModified( file ).toString( dateFormat );
        node += "]";
        return node;
    }

    public void showTree()
    {
        // ( String frameTitle, JTree tree, Point location, Dimension size )
       DisplayTree displayTree = new DisplayTree(
           "Diretory Tree",
           tree,
           new Point(400, 200),
           new Dimension(400, 1000));
//        JFrame treeFrame = new JFrame();
//        //Dimension treeSize = tree.getSize();
//        //treeFrame.setSize(treeSize);
//        treeFrame.setPreferredSize(new Dimension(300,500));
//        treeFrame.setLocation(new Point(500,200));
//        jScrollPane1 = new JScrollPane( treeFrame );
//        jScrollPane1.setViewportView( tree );
//        treeFrame.add(jScrollPane1);
//        treeFrame.pack();
//        treeFrame.setVisible(true);
    }
    
    public void setReportSize( boolean reportSize )
    {
        this.reportSize = reportSize;
    }

    public void setDateFormat( String dateFormat )
    { // Set to "" to suppress last mod date
        this.dateFormat = dateFormat;
    }

    public Vector getDirList()
    {
        return dirList;
    }

    public Vector getFileList()
    {
        return fileList;
    }

    public int getNDirs()
    {
        return dirList.size();
    }

    public int getNFiles()
    {
        return fileList.size();
    }

    public long getNBytes()
    {
        return nBytes;
    }

    public JTree getTree()
    {
        return tree;
    }

    Vector dirList = new Vector( 100, 100 );
    Vector fileList = new Vector( 1000, 1000 );
    String match;
    long nBytes = 0L;
    DefaultMutableTreeNode root;
    JTree tree;
    javax.swing.JScrollPane jScrollPane1;
    private boolean reportSize = true;
    private String dateFormat = "mm-dd-yy hh:mm:ss";

    /***** FOR TESTING *****/
    public static void main( String[] args )
    {
        try
        {
            Clip clip = new Clip( args, new String[] { "E:", "Tree" } );
            Four11 four11 = new Four11( clip.getParam( 0 ) );
            Console.println( "Structure rooted at " + clip.getParam( 0 ) + " contains:" );
            Console.println( Strings.plurals( "directory", four11.getNDirs() ) );
            Console.println( Strings.plurals( "file", four11.getNFiles() ) );
            Console.println( Strings.plurals( "byte", four11.getNBytes() ) +
                " (" + Strings.plurals("MByte", four11.getNBytes() / 1000000) + ")");
            Console.println( "The last directory is: " +
                (String)four11.getDirList().lastElement() );
            Console.println( "The last file is: " +
                (String)four11.getFileList().lastElement() );

            //if (clip.isSet("T"))
            {
                four11.showTree();
            }
        }
        catch (UtilitiesException ux)
        {
            Console.println( ux.getMessage() );
        }
    }
    /*****/
}

