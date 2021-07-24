package prasad.tetris.records;

import prasad.tetris.MainPanel;

import javax.swing.JOptionPane;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 * Author: Prasad Karunagoda
 * Date: 11/9/13
 */
public class RecordsManager
{
  static final int NUMBER_OF_RECORDS = 10;

  private static final String RECORD_FILENAME = "rec";
  private static final String VALIDATION_FILENAME = "v";
  private static final String FILES_FOLDER_NAME = "record_files";

  private static final int VALIDATION_NUMBER = 50000;

  public static void checkForRecord(int score)
  {
    List<Record> records = loadRecords();
    if (records != null)
    {
      Record lastRecord = records.get(NUMBER_OF_RECORDS - 1);
      if (score > lastRecord.getScore())
      {
        String response = JOptionPane.showInputDialog(MainPanel.getInstance().getTopLevelAncestor(),
            "You have scored a high-score. Please enter your name:", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        if (response != null)
        {
          records.remove(lastRecord);
          records.add(new Record(response, score));
          Collections.sort(records);
          writeRecords(records);
        }
      }

      RecordsDialog dialog = new RecordsDialog((Frame) MainPanel.getInstance().getTopLevelAncestor(), records);
      dialog.setVisible(true);
    }
  }

  public static void showRecords()
  {
    List<Record> records = loadRecords();
    if (records != null)
    {
      RecordsDialog dialog = new RecordsDialog((Frame) MainPanel.getInstance().getTopLevelAncestor(), records);
      dialog.setVisible(true);
    }
  }

  private static List<Record> loadRecords()
  {
    BufferedReader reader = null;
    try
    {
      List<Record> records = new ArrayList<Record>();
      reader = new BufferedReader(new FileReader(FILES_FOLDER_NAME + File.separator + RECORD_FILENAME));
      String line;
      while ((line = reader.readLine()) != null)
      {
        String[] nameAndScore = line.split(":");
        Record record = new Record(nameAndScore[0], Integer.valueOf(nameAndScore[1]));
        records.add(record);
      }

      boolean recordsValid = validateRecords(records);
      if (!recordsValid)
      {
        records.clear();
        for (int i = 0; i < NUMBER_OF_RECORDS; i++)
        {
          records.add(new Record("Anonymous", 0));
        }
        writeRecords(records);
      }

      Collections.sort(records);
      return records;
    }
    catch (FileNotFoundException ex)
    {
      JOptionPane.showMessageDialog(MainPanel.getInstance().getTopLevelAncestor(),
          "Records file not found. Blank records file will be created.", "Error", JOptionPane.WARNING_MESSAGE);
      List<Record> records = new ArrayList<Record>();
      for (int i = 0; i < NUMBER_OF_RECORDS; i++)
      {
        records.add(new Record("Anonymous", 0));
      }
      writeRecords(records);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }

    return null;
  }

  private static boolean validateRecords(List<Record> records)
  {
    int recordsNumber = calculateRecordsNumber(records);
    int validationNumber = loadValidationNumber();
    return (recordsNumber + validationNumber) == VALIDATION_NUMBER;
  }

  private static int calculateRecordsNumber(List<Record> records)
  {
    int number = 0;
    for (Record r : records)
    {
      char[] chars = r.getName().toCharArray();
      for (char c : chars)
      {
        number = number + c;
      }
      number = number + r.getScore();
    }
    return number;
  }

  private static int loadValidationNumber()
  {
    int number = -1;
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(FILES_FOLDER_NAME + File.separator + VALIDATION_FILENAME));
      String line = reader.readLine();
      if (line != null)
      {
        number = Integer.valueOf(line);
      }
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
    return number;
  }

  private static void writeRecords(List<Record> records)
  {
    BufferedWriter writer = null;
    try
    {
      StringBuilder sb = new StringBuilder();
      for (Record r : records)
      {
        sb.append(r.getName());
        sb.append(":");
        sb.append(r.getScore());
        sb.append("\n");
      }

      writer = new BufferedWriter(new FileWriter(FILES_FOLDER_NAME + File.separator + RECORD_FILENAME));
      writer.write(sb.toString());
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }

    int recordsNumber = calculateRecordsNumber(records);

    BufferedWriter validateNumberWriter = null;
    try
    {
      validateNumberWriter = new BufferedWriter(new FileWriter(FILES_FOLDER_NAME + File.separator + VALIDATION_FILENAME));
      validateNumberWriter.write(String.valueOf(VALIDATION_NUMBER - recordsNumber));
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if (validateNumberWriter != null)
      {
        try
        {
          validateNumberWriter.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }
}
