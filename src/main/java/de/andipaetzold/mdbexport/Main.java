package de.andipaetzold.mdbexport;

import de.andipaetzold.mdbexport.builder.Builder;
import de.andipaetzold.mdbexport.builder.JSONBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.commons.cli.CommandLine;

public class Main {
  public static void main(String[] args) throws IOException {
    CLIParser cliParser = new CLIParser();
    CommandLine cmd = cliParser.parse(args);

    String filename = getFilename(cmd);
    DatabaseParser databaseParser = new DatabaseParser(filename);

    Writer writer = getWriter(cmd);
    Builder builder = getBuilder(cmd);
    builder.write(databaseParser, writer);
    writer.close();
  }

  private static Builder getBuilder(CommandLine cmd) {
    Format format = Format.JSON;
    if (cmd.hasOption("format")) {
      try {
        format = Format.valueOf(cmd.getOptionValue("format"));
      } catch (IllegalArgumentException e) {
        System.err.println("Invalid format");
      }
    }

    Builder builder;
    switch (format) {
      case JSON:
      default:
        builder = new JSONBuilder();
    }

    return builder;
  }

  private static Writer getWriter(CommandLine cmd) throws IOException {
    if (cmd.hasOption("o")) {
      String filename = cmd.getOptionValue("o");
      if (new File(filename).exists() && !new File(filename).canWrite()) {
        System.err.println("Cannot write to output file");
        System.exit(1);
        return null;
      }
      return new FileWriter(filename, false);
    } else {
      return new PrintWriter(System.out);
    }
  }

  private static String getFilename(CommandLine cmd) {
    return cmd.getOptionValue("i");
  }
}
