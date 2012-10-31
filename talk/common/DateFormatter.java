package talk.common;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateFormatter {
  private SimpleDateFormat formatter;
  public DateFormatter(){
    this("HH:mm:ss");
  }

  public DateFormatter(String format){
    formatter = new SimpleDateFormat(format);
  }

  public String format(Date to_format){
    return formatter.format(to_format);
  }
}
