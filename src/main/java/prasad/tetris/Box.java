package prasad.tetris;

import java.awt.*;

/**
 * Prasad Karunagoda
 * Date: Jan 1, 2011
 */
public class Box extends Shape
{
    void draw( Graphics g, Point p )
    {
        g.drawRect( p.x + 0,                    p.y + 0,                     Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
        g.drawRect( p.x + 0,                    p.y + Shape.UNIT_HEIGHT * 1, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
        g.drawRect( p.x + Shape.UNIT_WIDTH * 1, p.y + 0,                     Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
        g.drawRect( p.x + Shape.UNIT_WIDTH * 1, p.y + Shape.UNIT_HEIGHT * 1, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
    }

    int getWidth()
    {
        return Shape.UNIT_WIDTH * 2;
    }

    int getHeight()
    {
        return Shape.UNIT_HEIGHT * 2;
    }

    boolean canMoveFurtherDown( int currentShapeX, int currentShapeY )
    {
        return ( currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH ) * Shape.UNIT_HEIGHT - getHeight()
            && currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH + 1 ) * Shape.UNIT_HEIGHT - getHeight() );
    }

    void addToGrid( int currentShapeX, int currentShapeY )
    {
        int columnNo = currentShapeX / Shape.UNIT_WIDTH;
        int cellNo = ( MainPanel.PANEL_HEIGHT - currentShapeY ) / Shape.UNIT_HEIGHT;
        MainPanel.grid.get( columnNo ).add( cellNo );
        MainPanel.grid.get( columnNo ).add( cellNo - 1 );
        MainPanel.grid.get( columnNo + 1 ).add( cellNo );
        MainPanel.grid.get( columnNo + 1 ).add( cellNo - 1 );
    }

    Dimension rotate()
    {
        return new Dimension( 0, 0 );
    }

    Point getNextShapeStartPoint()
    {
        return new Point( Shape.UNIT_WIDTH * 2, Shape.UNIT_HEIGHT * 2 );
    }
}
