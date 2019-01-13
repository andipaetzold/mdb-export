package de.andipaetzold.mdbexport.builder;

import de.andipaetzold.mdbexport.DatabaseParser;

import java.io.Writer;

public interface Builder {
  void write(DatabaseParser parser, Writer writer);
}
