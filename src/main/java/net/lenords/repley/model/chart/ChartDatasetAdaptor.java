package net.lenords.repley.model.chart;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.List;

public class ChartDatasetAdaptor extends TypeAdapter<ChartDataset> {

  @Override
  public void write(JsonWriter out, ChartDataset value) throws IOException {
    out.beginObject();
    out.name("label").value(value.getLabel());
    out.name("data").beginArray();
    for (Integer data : value.getData()) {
      out.value(data);
    }
    out.endArray();

    if (value.getBackgroundColor() != null) {
      List<String> backgroundColor = value.getBackgroundColor();
      List<String> borderColor = value.getBorderColor();

      if (backgroundColor .size() == 1) {
        out.name("backgroundColor").value(backgroundColor .get(0));
        out.name("borderColor").value(borderColor.get(0));
      } else {
        out.name("backgroundColor").beginArray();
        for (String color : backgroundColor) {
          out.value(color);
        }
        out.endArray();

        out.name("borderColor").beginArray();
        for (String color : borderColor) {
          out.value(color);
        }
        out.endArray();
      }
    }

    if (value.getBorderWidth() > 0) {
      out.name("borderWidth").value(value.getBorderWidth());
    }

    out.endObject();

  }

  //TODO: this i guess? I mean we never read one in, so shouldnt really be necessary
  @Override
  public ChartDataset read(JsonReader in) throws IOException {
    return null;
  }
}
