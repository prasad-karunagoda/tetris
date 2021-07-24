package prasad.tetris;

import java.awt.*;
import javax.swing.*;

/**
 * Prasad Karunagoda
 * Date: Jan 15, 2011
 */
public class NextShapePanel extends JPanel
{
    static final int PANEL_WIDTH = Shape.UNIT_WIDTH * 5;
    static final int PANEL_HEIGHT = Shape.UNIT_HEIGHT * 6;

    private static final int NUMBER_OF_SHAPES = 7; // Box, L, ReverseL, Line, S, Z, T

    private static NextShapePanel instance;

    private Shape nextShape;
    
    private NextShapePanel()
    {
        setLayout( null );
        setPreferredSize( new Dimension( PANEL_WIDTH, PANEL_HEIGHT ) );
        setMaximumSize( new Dimension( PANEL_WIDTH, PANEL_HEIGHT ) );

        nextShape = getRandomShape();
    }

    public static NextShapePanel getInstance()
    {
        if( instance == null )
        {
            instance = new NextShapePanel();
        }
        return instance;
    }

    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        nextShape.draw( g, nextShape.getNextShapeStartPoint() );
    }

    Shape getNextShape()
    {
        Shape consumingShape = nextShape;
        nextShape = getRandomShape();
        repaint();
        return consumingShape;
    }

    private static Shape getRandomShape()
    {
        int index = (int) ( Math.random() * NUMBER_OF_SHAPES );
        switch( index )
        {
            case 0:
                return new Box();
            case 1:
                return new L();
            case 2:
                return new ReverseL();
            case 3:
                return new Line();
            case 4:
                return new S();
            case 5:
                return new Z();
            case 6:
                return new T();
            default:
                return new Box();
        }
    }
}
