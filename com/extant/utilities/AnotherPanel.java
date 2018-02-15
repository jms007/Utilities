/*
 * AnotherPanel.java
 *
 * Created on October 15, 2006, 5:18 PM
 */

/**
 *
 * @author jms
 */
package com.extant.utilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;

public class AnotherPanel
extends javax.swing.JPanel
{
    public AnotherPanel( Image image, int position )
    {
        super();
        this.image = image;
        this.position = position;
        imageSize = new Dimension( image.getWidth(this), image.getHeight(this) );
        this.setPreferredSize( imageSize );
    }

    public AnotherPanel( Image image, Dimension offset )
    {
        super();
        this.image = image;
        this.offset = offset;
        position=0;
        imageSize = new Dimension( image.getWidth(this), image.getHeight(this) );
        this.setPreferredSize( imageSize );
    }

    private Dimension center( int viewportWidth, int viewportHeight )
    {
        xOffset = (viewportWidth - imageSize.width) / 2;
        yOffset = (viewportHeight - imageSize.height) / 2;
        return new Dimension( xOffset, yOffset );
    }

    public void paint( Graphics graphics )
    {
        super.paint( graphics );
        int viewportWidth = this.getWidth();
        int viewportHeight = this.getHeight();
        if ( position == CENTER ) offset = center( viewportWidth, viewportHeight );
        graphics.drawImage(image, offset.width, offset.height, null);        
    }

    public static final int CENTER = 1;
    int position;
    Image image;
    Dimension offset;
    Dimension imageSize;
    int xOffset;
    int yOffset;
}

