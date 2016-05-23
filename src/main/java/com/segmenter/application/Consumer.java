package com.segmenter.application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {
	private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public final String EXT = ".log";
	private final SortedSet<Line> lines; /* list of lines to be written */

	public Consumer(SortedSet<Line> lines) {
		this.lines = lines;
	}

    /**
     * Write the already sorted lines to a file named by the parameter fileName
     *
     * @param fileName name of the file to write
     * @return true if the file has been written correctly, false otherwise
     */
	public boolean write(String fileName) {
		fileName += EXT;

		try {
			Files.deleteIfExists(Paths.get(fileName));
			Path out = Files.createFile(Paths.get(fileName));

			log.info("File [" + fileName + "] writing data...");

			for (Line l : lines) {
				FileUtils.writeStringToFile(out.toFile(), l.toString() + "\n", StandardCharsets.UTF_8, true);
			}

            return true;

		} catch (IOException ex) {
			log.info("Error creating file [" + fileName + "] ");
            return false;
		}
	}
	
}
