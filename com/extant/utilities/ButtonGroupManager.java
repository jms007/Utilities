/*
 * ButtonGroupManager.java
 *
 * Created on September 3, 2002, 10:08 PM
 */

package com.extant.utilities;
import java.util.Vector;
import javax.swing.JRadioButton;
// For testing:
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author  jms
 */
public class ButtonGroupManager
{
    JPanel panel;
    Vector buttons = new Vector( 10, 10 );
    JRadioButton initialButton = null;
    
    /** Creates a new instance of ButtonGroupManager */
    public ButtonGroupManager( JPanel panel )
    {
        this.panel = panel;
    }

    public void setInitialButton( JRadioButton rb )
    {
        initialButton = rb;
    }
    
    public void addButton( JRadioButton rb )
    {
        buttons.addElement( rb );
        panel.add( rb );
        rb.addActionListener( new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                processEvent( e );
            }
        });
    }

    public void processEvent( java.awt.event.ActionEvent evt )
    {
        JRadioButton iButton;
        if ( evt == null )
        {
            if ( initialButton == null )
                iButton = (JRadioButton)buttons.elementAt( 0 );
            else iButton = initialButton;
        }
        else iButton = (JRadioButton)evt.getSource();
        
        for (int i=0; i<buttons.size(); ++i)
            ((JRadioButton)buttons.elementAt(i)).setSelected
                ( iButton == (JRadioButton)buttons.elementAt(i) );
    }

    /***** FOR TESTING *****/
    public static void main( String[] args )
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout( new FlowLayout() );
        final ButtonGroupManager bgm = new ButtonGroupManager( panel );
        for (int i=0; i<10; ++i)
            bgm.addButton( new JRadioButton( "Button #" + (i+1) ) );
        frame.getContentPane().add( panel );
        frame.setSize( 300, 200 );
        frame.setVisible( true );
        bgm.processEvent( null );
    }
/***** END OF TEST CODE *****/
}

