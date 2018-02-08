package de.andipaetzold.mdbexport;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseParser {
  private final String filename;

  private List<String> tableNames = new ArrayList<>();
  private Map<String, List<String>> headersMap = new HashMap<>();
  private Map<String, List<Map<String, Object>>> dataMap = new HashMap<>();

  public DatabaseParser(String filename) {
    this.filename = filename;

    if (!new File(filename).canRead()) {
      System.err.println("Cannot read input file");
      System.exit(1);
      return;
    }

    try {
      parse();
    } catch (IOException ignored) {
      System.err.println("Error reading input file");
      System.exit(1);
    }
  }

  private void parse() throws IOException {
    try (Database database = DatabaseBuilder.open(new File(filename))) {
      for (String tableName : database.getTableNames()) {
        tableNames.add(tableName);
        parseTable(database.getTable(tableName));
      }
    }
  }

  private void parseTable(Table table) {
    List<String> headers = new ArrayList<>();
    for (Column column : table.getColumns()) {
      String name = column.getName();
      headers.add(name);
    }

    List<Map<String, Object>> data = new ArrayList<>();
    for (Row row : table) {
      Map<String, Object> dataRow = new HashMap<>();

      for (String header : headers) {
        dataRow.put(header, row.get(header));
      }

      data.add(dataRow);
    }

    headersMap.put(table.getName(), headers);
    dataMap.put(table.getName(), data);
  }

  public List<String> getTableNames() {
    return tableNames;
  }

  public List<String> getTableHeaders(String tableName) {
    return headersMap.get(tableName);
  }

  public List<Map<String, Object>> getTableRows(String tableName) {
    return dataMap.get(tableName);
  }
}
