package prasad.tetris.records;

/**
 * TODO
 * Author: Prasad Karunagoda
 * Date: 11/9/13
 */
class Record implements Comparable
{
  private String name;
  private int score;

  Record(String name, int score)
  {
    this.name = name;
    this.score = score;
  }

  String getName()
  {
    return name;
  }

  int getScore()
  {
    return score;
  }

  public int compareTo(Object obj)
  {
    if (this.score < ((Record) obj).score)
    {
      return 1;
    }
    else if (this.score > ((Record) obj).score)
    {
      return -1;
    }
    return 0;
  }
}
