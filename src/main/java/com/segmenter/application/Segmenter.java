package com.segmenter.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
public class Segmenter {
	private static final Logger log = LoggerFactory.getLogger(Segmenter.class);

    /* Map having client as key and a SortedSet of its lines as values */
    protected static Map<String, SortedSet<Line>> map = new ConcurrentHashMap<>();

	@Autowired
	private SegmenterSettings config;


	public Segmenter() {}


    public void run(String baseUrl, int filesNumber) {
        this.segmentLines(baseUrl, filesNumber);
        this.writeLines();
    }

    /**
     * Makes "filesNumber" requests to the log files residing under baseUrl
     * A thread for each http request is created which reads the log (test#.log) InputStream,
     * then add each line to the proper client TreeSet (sorted) in the static ConcurrentHashMap
     *
     * @param baseUrl baseUrl of the files to request (to be combined with the domain)
     * @param filesNumber Number of files to request
     * @return Map of Lines divided by client and already sorted by timestamp
     */
    public Map<String, SortedSet<Line>> segmentLines(String baseUrl, int filesNumber) {

        Instant start = Instant.now();

        final ExecutorService producers = Executors.newFixedThreadPool(filesNumber);

        IntStream.range(0, filesNumber).forEachOrdered(n -> {

            //
            String url = baseUrl+"/"+config.getLogname(n);

            Producer p = new Producer(url, n, config);
            producers.submit(p);
        });


        try {
            producers.shutdown();
            producers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error executing threads: "+e.getMessage());
        }

        int execTime = Duration.between(start, Instant.now()).getNano();
        log.info("Producers executed in: " + TimeUnit.NANOSECONDS.toMillis(execTime)+" ms");

        return map;
    }

    /**
     * Creates a file for every key (clientName) of the ConcurrentHashMap
     * and write a new line for every node of its respective TreeSet
     *
     * Note: A true producer consumer model is not possible because we need all the sorted lines before writing.
     * An alternative could be using a database to store our lines, that would be advantagious for memoory consumption
     */
	public void writeLines() {
		
        Instant start = Instant.now();

		map.forEach((k, v)->{
			
			Consumer c = new Consumer(v);
			c.write(k);
		});


		int execTime2 = Duration.between(start, Instant.now()).getNano();
		log.info("Files written in: " + TimeUnit.NANOSECONDS.toMillis(execTime2)+" ms");
	}

    public SegmenterSettings getConfig() {
        return config;
    }

    public static Map<String, SortedSet<Line>> getMap() {
        return map;
    }

    public void resetMap() {
        map = new ConcurrentHashMap<>();
    }

}