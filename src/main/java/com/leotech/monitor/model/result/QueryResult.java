package com.leotech.monitor.model.result;

import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import java.util.ArrayList;

public interface QueryResult {

  ArrayList<ColumnDescription> getColumns();

  DataTable getData();

  String getFormattedQuery();
}
