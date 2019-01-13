package de.andipaetzold.mdbexport.builder;

import de.andipaetzold.mdbexport.DatabaseParser;

import org.json.JSONObject;

import java.io.Writer;
import java.util.List;
import java.util.Map;

public class JSONBuilder implements Builder {
  public void write(DatabaseParser parser, Writer writer) {
    JSONObject object = new JSONObject();
    for (String tableName : parser.getTableNames()) {
      List<Map<String, Object>> rows = parser.getTableRows(tableName);
      for (Map<String, Object> row : rows) {
        object.append(tableName, row);
      }
    }

    object.write(writer);
  }
}
