package de.andipaetzold.mdbexport;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLIParser {
  CLIParser() {}

  public CommandLine parse(String[] args) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd;

    try {
      cmd = parser.parse(helpOption(), args);

      if (cmd.hasOption("help")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("mdbexport", allOptions(), false);
        System.exit(1);
        return null;
      }
    } catch (ParseException ignored) {
    }

    try {
      return parser.parse(allOptions(), args);
    } catch (ParseException e) {
      System.err.println(e.getMessage());
      System.exit(1);
      return null;
    }
  }

  private Options allOptions() {
    Option helpOption = Option.builder("h").longOpt("help").desc("print this message").build();
    Option formatOption =
        Option.builder("f").longOpt("format").hasArg().desc("output format").build();
    Option inputFileOption =
        Option.builder("i")
            .longOpt("input")
            .hasArg()
            .argName("filename")
            .required()
            .desc("input file")
            .build();
    Option outputFileOption =
        Option.builder("o")
            .longOpt("output")
            .hasArg()
            .argName("filename")
            .desc("output file")
            .build();

    Options options = new Options();
    options.addOption(helpOption);
    options.addOption(formatOption);
    options.addOption(inputFileOption);
    options.addOption(outputFileOption);
    return options;
  }

  private Options helpOption() {
    Options options = new Options();
    options.addOption(Option.builder("h").longOpt("help").desc("print this message").build());
    return options;
  }
}
