package prasad.tetris.records;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * TODO
 * Author: Prasad Karunagoda
 * Date: 11/9/13
 */
class RecordsDialog extends JDialog
{
  RecordsDialog(Frame owner, List<Record> records)
  {
    super(owner, "Records", true);
    setLayout(new GridBagLayout());

    JLabel nameLabel = new JLabel("NAME");
    nameLabel.setForeground(Color.blue);

    JLabel scoreLabel = new JLabel("SCORE");
    scoreLabel.setForeground(Color.blue);

    getContentPane().add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
        GridBagConstraints.NONE, new Insets(15, 20, 5, 0), 0, 0));
    getContentPane().add(scoreLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
        GridBagConstraints.NONE, new Insets(15, 25, 5, 20), 0, 0));

    for (int i = 0; i < records.size(); i++)
    {
      getContentPane().add(new JLabel(records.get(i).getName()), new GridBagConstraints(0, i + 1, 1, 1, 0.0, 0.0,
          GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, (i == RecordsManager.NUMBER_OF_RECORDS - 1 ? 15 : 5), 0), 0, 0));
      getContentPane().add(new JLabel(String.valueOf(records.get(i).getScore())), new GridBagConstraints(1, i + 1, 1, 1, 0.0, 0.0,
          GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 25, (i == RecordsManager.NUMBER_OF_RECORDS - 1 ? 15 : 5), 25), 0, 0));
    }
    pack();
    setLocation(owner.getLocation().x + (owner.getSize().width - getSize().width) / 2,
        owner.getLocation().y + (owner.getSize().height - getSize().height) / 2);
  }
}
