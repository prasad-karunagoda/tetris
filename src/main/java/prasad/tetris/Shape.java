package prasad.tetris;

import java.awt.*;

/**
 * Prasad Karunagoda
 * Date: Jan 1, 2011
 */
public abstract class Shape
{
    static final int UNIT_WIDTH = 20;
    static final int UNIT_HEIGHT = 20;

    static final int ORIENTATION_DEFAULT = 1;

    protected int orientation = ORIENTATION_DEFAULT;

    abstract void draw( Graphics g, Point p );

    abstract int getWidth();

    abstract int getHeight();

    abstract boolean canMoveFurtherDown( int currentShapeX, int currentShapeY );

    boolean canMoveFurtherLeft( int currentShapeX, int currentShapeY )
    {
        // This logic does not depend on orientation
        int distanceToBottomEdgeOfTheShape = currentShapeY + getHeight();
        int distanceToTopOfColumnToLeft = MainPanel.PANEL_HEIGHT - MainPanel.getFloor( currentShapeX / Shape.UNIT_WIDTH - 1 ) * Shape.UNIT_HEIGHT;
        return ( distanceToBottomEdgeOfTheShape <= distanceToTopOfColumnToLeft );
    }

    boolean canMoveFurtherRight( int currentShapeX, int currentShapeY )
    {
        // This logic does not depend on orientation
        int distanceToBottomEdgeOfTheShape = currentShapeY + getHeight();
        int distanceToTopOfColumnToRight = MainPanel.PANEL_HEIGHT - MainPanel.getFloor( (currentShapeX + getWidth()) / Shape.UNIT_WIDTH ) * Shape.UNIT_HEIGHT;
        return ( distanceToBottomEdgeOfTheShape <= distanceToTopOfColumnToRight );
    }

    abstract void addToGrid( int currentShapeX, int currentShapeY );

    /**
     * Rotate the shape clockwise.
     */
    abstract Dimension rotate();

    abstract Point getNextShapeStartPoint();
}
