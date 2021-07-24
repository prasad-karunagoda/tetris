package prasad.tetris;

import java.awt.*;

/**
 * Prasad Karunagoda
 * Date: Jan 1, 2011
 */
public class Line extends Shape
{
    /**
     *  ----
     */
    private static final int ORIENTATION_2 = 2;

    void draw( Graphics g, Point p )
    {
        if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            g.drawRect( p.x + 0, p.y + 0,                     Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + 0, p.y + Shape.UNIT_HEIGHT * 1, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + 0, p.y + Shape.UNIT_HEIGHT * 2, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + 0, p.y + Shape.UNIT_HEIGHT * 3, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
        }
        else if( orientation == ORIENTATION_2 )
        {
            g.drawRect( p.x + 0,                    p.y + 0, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + Shape.UNIT_WIDTH * 1, p.y + 0, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + Shape.UNIT_WIDTH * 2, p.y + 0, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
            g.drawRect( p.x + Shape.UNIT_WIDTH * 3, p.y + 0, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
        }
    }

    int getWidth()
    {
        if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            return Shape.UNIT_WIDTH * 1;
        }
        else if( orientation == ORIENTATION_2 )
        {
            return Shape.UNIT_WIDTH * 4;
        }
        else
        {
            return -1;
//            throw new Exception();
        }
    }

    int getHeight()
    {
        if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            return Shape.UNIT_HEIGHT * 4;
        }
        else if( orientation == ORIENTATION_2 )
        {
            return Shape.UNIT_HEIGHT * 1;
        }
        else
        {
            return -1;
//            throw new Exception();
        }
    }

    boolean canMoveFurtherDown( int currentShapeX, int currentShapeY )
    {
        if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            return ( currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH ) * Shape.UNIT_HEIGHT - getHeight() );
        }
        else if( orientation == ORIENTATION_2 )
        {
            return ( currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH ) * Shape.UNIT_HEIGHT - getHeight()
                    && currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH + 1 ) * Shape.UNIT_HEIGHT - getHeight()
                    && currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH + 2 ) * Shape.UNIT_HEIGHT - getHeight()
                    && currentShapeY < MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH + 3 ) * Shape.UNIT_HEIGHT - getHeight() );
        }
        else
        {
            return false;
//            throw new Exception();
        }
    }

    void addToGrid( int currentShapeX, int currentShapeY )
    {
        int columnNo = currentShapeX / Shape.UNIT_WIDTH;
        int cellNo = ( MainPanel.PANEL_HEIGHT - currentShapeY ) / Shape.UNIT_HEIGHT;

        if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            MainPanel.grid.get( columnNo ).add( cellNo );
            MainPanel.grid.get( columnNo ).add( cellNo - 1 );
            MainPanel.grid.get( columnNo ).add( cellNo - 2 );
            MainPanel.grid.get( columnNo ).add( cellNo - 3 );
        }
        else if( orientation == ORIENTATION_2 )
        {
            MainPanel.grid.get( columnNo ).add( cellNo );
            MainPanel.grid.get( columnNo + 1 ).add( cellNo );
            MainPanel.grid.get( columnNo + 2 ).add( cellNo );
            MainPanel.grid.get( columnNo + 3 ).add( cellNo );
        }
    }

    /**
     * Default orientation is   |
     *                          |
     *                          |
     *                          |
     */
    Dimension rotate()
    {
        if( orientation == ORIENTATION_2 )
        {
            orientation = Shape.ORIENTATION_DEFAULT;
            return new Dimension( Shape.UNIT_WIDTH * 1, Shape.UNIT_HEIGHT * -2 );
        }
        else if( orientation == Shape.ORIENTATION_DEFAULT )
        {
            orientation = ORIENTATION_2;
            return new Dimension( Shape.UNIT_WIDTH * -1, Shape.UNIT_HEIGHT * 2 );
        }
        else
        {
            return null;
//            throw new Exception();
        }
    }

    Point getNextShapeStartPoint()
    {
        return new Point( Shape.UNIT_WIDTH * 2, Shape.UNIT_HEIGHT * 1 );
    }
}
