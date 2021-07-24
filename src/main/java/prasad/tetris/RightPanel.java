package prasad.tetris;

import java.awt.*;
import javax.swing.*;

/**
 * Prasad Karunagoda
 * Date: Jan 15, 2011
 */
public class RightPanel extends JPanel
{
    private static RightPanel instance;

    private JLabel pausedLabel;

    private JLabel score;

    private RightPanel()
    {
        score = new JLabel( String.valueOf( MainPanel.score ) );
        score.setFont( new Font( Font.MONOSPACED, Font.BOLD, 24 ) );

        JLabel nextLabel = new JLabel( "Next" );
        nextLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 18 ) );
        pausedLabel = new JLabel( " " );
        pausedLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 18 ) );
        pausedLabel.setForeground( Color.blue );
        JLabel scoreLabel = new JLabel( "Score" );
        scoreLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 18 ) );

        setLayout( new GridBagLayout() );
        add( nextLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( NextShapePanel.getInstance(), new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( pausedLabel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( scoreLabel, new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( score, new GridBagConstraints( 0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    public static RightPanel getInstance()
    {
        if( instance == null )
        {
            instance = new RightPanel();
        }
        return instance;
    }

    void updateScore()
    {
        score.setText( String.valueOf( MainPanel.score ) );
    }

    void setPaused(boolean paused)
    {
      if ( paused )
      {
          pausedLabel.setText( "Paused" );
      }
      else
      {
          pausedLabel.setText( " " );
      }
    }
}
