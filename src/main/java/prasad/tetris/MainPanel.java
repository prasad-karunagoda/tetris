package prasad.tetris;

import prasad.tetris.records.RecordsManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Prasad Karunagoda
 * Date: Jan 1, 2011
 */
public class MainPanel extends JPanel implements KeyListener
{
    static final int PANEL_WIDTH = Shape.UNIT_WIDTH * 10;
    static final int PANEL_HEIGHT = Shape.UNIT_HEIGHT * 20;

    private static final Color BACKGROUND_COLOR = new Color( 160, 160, 192 );
    private static final Color PAUSED_BACKGROUND_COLOR = new Color( 192, 192, 224 );

    private static final int START_X = 80;
    private static final int INITIAL_PERIOD = 500;

    private static MainPanel instance;

    private Shape currentShape;
    private int currentShapeX;
    private int currentShapeY;
    private boolean repaintSourceTimer;
    /* Column to filled slots in that column */
    static Map<Integer, List<Integer>> grid;
    static int score;

    private java.util.Timer timer;
    private TimerTask timerTask;
    private int period;
    private boolean paused;

    private MainPanel()
    {
        setLayout( null );
        setBackground( BACKGROUND_COLOR );
        // + 1 to draw the right border of shapes properly inside the panel
        setPreferredSize( new Dimension( PANEL_WIDTH + 1, PANEL_HEIGHT ) );
        setMaximumSize( new Dimension( PANEL_WIDTH + 1, PANEL_HEIGHT ) );
        setFocusable( true );
        addKeyListener( this );

        initialize();
    }

    private void initialize()
    {
        initializeFloorMap();

        score = 0;
        paused = false;
        RightPanel.getInstance().updateScore();
        RightPanel.getInstance().setPaused( false );

        currentShape = NextShapePanel.getInstance().getNextShape();
        currentShapeX = START_X;
        currentShapeY = currentShape.getHeight() * -1;

        if( timerTask != null )
        {
            timerTask.cancel();
        }

        period = INITIAL_PERIOD;
        timer = new java.util.Timer();
        timerTask = new TimerTask()
        {
            public void run()
            {
                repaintSourceTimer = true;
                repaint();
            }
        };
        timer.schedule( timerTask, 0, period );
    }

    private static void initializeFloorMap()
    {
        grid = new HashMap<Integer, List<Integer>>();
        grid.put( 0, new ArrayList<Integer>() );
        grid.put( 1, new ArrayList<Integer>() );
        grid.put( 2, new ArrayList<Integer>() );
        grid.put( 3, new ArrayList<Integer>() );
        grid.put( 4, new ArrayList<Integer>() );
        grid.put( 5, new ArrayList<Integer>() );
        grid.put( 6, new ArrayList<Integer>() );
        grid.put( 7, new ArrayList<Integer>() );
        grid.put( 8, new ArrayList<Integer>() );
        grid.put( 9, new ArrayList<Integer>() );
    }

    static int getFloor( int column )
    {
        List<Integer> values = grid.get( column );
        if( values.isEmpty() )
        {
            return 0;
        }
        else
        {
            Collections.sort( values );
            return values.get( values.size() - 1 );
        }
    }

    protected void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        if( repaintSourceTimer )
        {
            if( currentShape.canMoveFurtherDown( currentShapeX, currentShapeY ) )
            {
                currentShapeY = currentShapeY + Shape.UNIT_HEIGHT;
            }
            else
            {
                currentShape.addToGrid( currentShapeX, currentShapeY );
                updateGrid();

                if( currentShapeY <= 0 )
                {
                    timerTask.cancel();
                    SwingUtilities.invokeLater( new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog( MainPanel.this.getTopLevelAncestor(), "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE );
                            RecordsManager.checkForRecord( score );
                        }
                    });
                }
                else
                {
                    currentShape = NextShapePanel.getInstance().getNextShape();
                    currentShapeX = START_X;
                    currentShapeY = currentShape.getHeight() * -1;
                }
            }
            repaintSourceTimer = false;
        }
        Point p = new Point( currentShapeX, currentShapeY );
        drawGrid( g );
        currentShape.draw( g, p );
    }

    private static void drawGrid( Graphics g )
    {
        for( Integer columnNo : grid.keySet() )
        {
            List<Integer> column = grid.get( columnNo );
            for( Integer cell : column )
            {
                g.drawRect( columnNo * Shape.UNIT_WIDTH, PANEL_HEIGHT - cell * Shape.UNIT_HEIGHT, Shape.UNIT_WIDTH, Shape.UNIT_HEIGHT );
                Color originalColor = g.getColor();
                g.setColor( new Color( 112, 112, 255 ) );
                g.fillRect( columnNo * Shape.UNIT_WIDTH + 1, PANEL_HEIGHT - cell * Shape.UNIT_HEIGHT + 1, Shape.UNIT_WIDTH - 1, Shape.UNIT_HEIGHT - 1 );
                g.setColor( originalColor );
            }
        }
    }

    // Grid:
    // 0,20  1,20  2,20 ...  9,20
    //
    // 0,2   1,2   2,2  ...  9,2
    // 0,1   1,1   2,1  ...  9,1
    private void updateGrid()
    {
        List<Integer> filledRows = new ArrayList<Integer>();
        for( int row = 1; rowNotEmpty( row ); row++ )
        {
            boolean rowFilled = true;
            for( Integer columnNo : grid.keySet() )
            {
                if( ! grid.get( columnNo ).contains( row ) )
                {
                    rowFilled = false;
                    break;
                }
            }
            if( rowFilled )
            {
                filledRows.add( row );
            }
        }

        Collections.sort( filledRows );
        // Filled rows are removed from top to bottom. E.g. if rows 2 and 3 are filled, first row 3 is removed.
        for( int i = filledRows.size() - 1; i >= 0; i-- )
        {
            Integer filledRow = filledRows.get( i );
            for( Integer columnNo : grid.keySet() )
            {
                // Remove cell of filled row from this column.
                grid.get( columnNo ).remove( filledRow );
                // Cells above the removed cell is pushed down.
                for( int j = 0; j < grid.get( columnNo ).size(); j++ )
                {
                    Integer cell = grid.get( columnNo ).get( j );
                    if( cell > filledRow )
                    {
                        grid.get( columnNo ).set( grid.get( columnNo ).indexOf( cell ), cell - 1 );
                    }
                }
            }
        }

        if( filledRows.size() == 1 )
        {
            score = score + 10;
        }
        else if( filledRows.size() == 2 )
        {
            score = score + 30;
        }
        else if( filledRows.size() == 3 )
        {
            score = score + 50;
        }
        else if( filledRows.size() == 4 )
        {
            score = score + 70;
        }

        RightPanel.getInstance().updateScore();

        if( score >= 500 && score < 1000 && period == INITIAL_PERIOD )
        {
            period = 400;
            timerTask.cancel();
            timerTask = new TimerTask()
            {
                public void run()
                {
                    repaintSourceTimer = true;
                    repaint();
                }
            };
            timer.schedule( timerTask, 0, period );
        }
        else if( score >= 1000 && score < 1500 && period == 400 )
        {
            period = 300;
            timerTask.cancel();
            timerTask = new TimerTask()
            {
                public void run()
                {
                    repaintSourceTimer = true;
                    repaint();
                }
            };
            timer.schedule( timerTask, 0, period );
        }
        else if( score >= 1500 && period == 300 )
        {
            period = 200;
            timerTask.cancel();
            timerTask = new TimerTask()
            {
                public void run()
                {
                    repaintSourceTimer = true;
                    repaint();
                }
            };
            timer.schedule( timerTask, 0, period );
        }
    }

    private static boolean rowNotEmpty( int rowNo )
    {
        for( Integer columnNo : grid.keySet() )
        {
            if( grid.get( columnNo ).contains( rowNo ) )
            {
                return true;
            }
        }
        return false;
    }


    public static MainPanel getInstance()
    {
        if( instance == null )
        {
            instance = new MainPanel();
        }
        return instance;
    }

    public void keyPressed( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_LEFT )
        {
            if( !paused && currentShapeX > 0 && currentShape.canMoveFurtherLeft( currentShapeX, currentShapeY ) )
            {
                currentShapeX = currentShapeX - Shape.UNIT_WIDTH;
                repaint();
            }
        }
        else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            if( !paused && currentShapeX < PANEL_WIDTH - currentShape.getWidth() &&
                currentShape.canMoveFurtherRight( currentShapeX, currentShapeY ) )
            {
                currentShapeX = currentShapeX + Shape.UNIT_WIDTH;
                repaint();
            }
        }
        else if( e.getKeyCode() == KeyEvent.VK_UP )
        {
            if ( !paused )
            {
                Dimension movement = currentShape.rotate();
                currentShapeX = currentShapeX + movement.width;
                if( currentShapeX < 0 )
                {
                    currentShapeX = 0;
                }
                else if( currentShapeX > PANEL_WIDTH - currentShape.getWidth() )
                {
                    currentShapeX = PANEL_WIDTH - currentShape.getWidth();
                }
                currentShapeY = currentShapeY + movement.height;
                repaint();
            }
        }
        else if( e.getKeyCode() == KeyEvent.VK_DOWN )
        {
            if( !paused && currentShape.canMoveFurtherDown( currentShapeX, currentShapeY ) )
            {
                currentShapeY = currentShapeY + Shape.UNIT_HEIGHT;
                repaint();
            }
        }
        else if ( e.getKeyCode() == KeyEvent.VK_P )
        {
          timerTask.cancel();
          setBackground( PAUSED_BACKGROUND_COLOR );
          RightPanel.getInstance().setPaused( true );
          paused = true;
        }
        else if ( e.getKeyCode() == KeyEvent.VK_R )
        {
          timerTask = new TimerTask()
          {
              public void run()
              {
                  repaintSourceTimer = true;
                  repaint();
              }
          };
          timer.schedule( timerTask, 0, period );
          setBackground( BACKGROUND_COLOR );
          RightPanel.getInstance().setPaused( false );
          paused = false;
        }
    }

    public void keyReleased( KeyEvent e )
    {
    }

    public void keyTyped( KeyEvent e )
    {
    }

    public static void main( String[] args )
    {
        JFrame frame = new JFrame( "Tetris" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable( false );
        frame.setSize( 400, 300 );
        frame.setLocation( ( Toolkit.getDefaultToolkit().getScreenSize().width - 400 ) / 2,
                ( Toolkit.getDefaultToolkit().getScreenSize().height - 300 ) / 2 );

        frame.getContentPane().add( MainPanel.getInstance(), BorderLayout.WEST );
        frame.getContentPane().add( RightPanel.getInstance(), BorderLayout.EAST );

        frame.setJMenuBar( new JMenuBar() );

        JMenu gameMenu = new JMenu( "Game" );
        gameMenu.add( new JMenuItem( new AbstractAction( "New" )
        {
            public void actionPerformed( ActionEvent e )
            {
                MainPanel.getInstance().initialize();
            }
        }));
        gameMenu.add( new JMenuItem( new AbstractAction( "Records" )
        {
            public void actionPerformed( ActionEvent e )
            {
                RecordsManager.showRecords();
            }
        }));
        gameMenu.add( new JMenuItem( new AbstractAction( "Exit" )
        {
            public void actionPerformed( ActionEvent e )
            {
                System.exit( 0 );
            }
        }));
        frame.getJMenuBar().add( gameMenu );

        JMenu helpMenu = new JMenu( "Help" );
        helpMenu.add( new JMenuItem( new AbstractAction( "Controls" )
        {
            public void actionPerformed( ActionEvent e )
            {
                JOptionPane.showMessageDialog( MainPanel.getInstance().getTopLevelAncestor(),
                        "LEFT ARROW - Move left\nRIGHT ARROW - Move right\nUP ARROW - Rotate\nDOWN ARROW - Fast drop\n\nP - Pause\nR - Resume",
                        "Controls", JOptionPane.INFORMATION_MESSAGE );
            }
        }));
        helpMenu.add( new JMenuItem( new AbstractAction( "About" )
        {
            public void actionPerformed( ActionEvent e )
            {
                JOptionPane.showMessageDialog( MainPanel.getInstance().getTopLevelAncestor(),
                        "Tetris v1.0\nPrasad Karunagoda\n2011.01.23", "About", JOptionPane.INFORMATION_MESSAGE );
            }
        }));
        frame.getJMenuBar().add( helpMenu );

        frame.pack();
        frame.setVisible( true );
    }
}
