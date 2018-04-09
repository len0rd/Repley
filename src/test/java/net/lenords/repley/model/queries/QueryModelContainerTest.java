package net.lenords.repley.model.queries;

import com.google.gson.Gson;
import java.io.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryModelContainerTest {

  @Test
  void readSimpleQuery() throws Exception {
    Gson gson = new Gson();

    QueryModelContainer container = gson.fromJson(new FileReader(
        "/Users/tylermiller/code/Repley/conf/query_conf-sample-simple.json"),
        QueryModelContainer.class);

    assert container != null;
    assert !container.getQueryModels().isEmpty();

    QueryModel model = container.getQueryModels().get(0);
    Assertions.assertEquals("stage", model.getName(), "Query model doesnt have name");
    Assertions.assertEquals("SELECT stage, COUNT(*) FROM re_%byo_%end.re_fsbo GROUP BY stage", model.getQuery().getSql(), "SQL is incorrect");
    Assertions.assertEquals("column.0", model.getData().getTitle(), "Data title def failed");
    assert model.getParams().getRequired() != null;
    assert !model.getParams().getRequired().isEmpty();

    System.out.println(model.toString());

  }
}
