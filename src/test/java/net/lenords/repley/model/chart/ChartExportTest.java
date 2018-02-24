package net.lenords.repley.model.chart;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class ChartExportTest {


  @Test
  public void chartToJSON() {
    ChartDataset cds = new ChartDataset("ds-label", Arrays.asList(1,2,3,4,5));
    System.out.println("Created Dataset");
    cds.setBorderWidth(1);
    System.out.println("Set border width");
    cds.generateRandomColorsForDataset();
    System.out.println("Generated random colors");

    ChartData cd = new ChartData(Arrays.asList("one", "two", "three", "four", "five"),
        Collections.singletonList(cds));
    System.out.println("Created Data");
    Chart chart = new Chart("pie", cd);
    System.out.println("Created Chart");
    Gson gson = new Gson();

    System.out.println("Serialize!\n\n");
    String gsonResult = gson.toJson(chart, Chart.class);

    System.out.println(gsonResult);
  }
}
