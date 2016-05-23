package com.segmenter.application;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


public class Producer implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Producer.class);

    private final String aUrl; /* url to request */
    private final int id; /* thread id */
    private SegmenterSettings config; /* setting needed for building Line object */
    

    public Producer(String url, int id, SegmenterSettings config) {
        this.aUrl = url;
        this.id = id;
        this.config = config;
    }

    /**
     * Run method which open a connection to the defined URL and get the stream response.
     * Each line read is used to create a new Line object that will be stored in a sorted TreeSet
     */
    public void run() {
        log.info("Thread ["+ id + "] about to get something from " + aUrl);

        BufferedReader br = null;

        try {

            URL url = new URL(aUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int statusCode = connection.getResponseCode();

            if (statusCode / 100 != 2) { // Not a succesful status code
                log.info("Thread ["+ id + "] got unsuccesful status code "+ statusCode);
            } else {

                // get the response body as input stream
                br =  new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while (((line = br.readLine()) != null)) {
                    log.info("Thread ["+ id + "] producing: "+ line);

                    try {
                        insert(new Line(line, config));
                    } catch (UnsupportedOperationException e) {
                        // Could not parse line (eg. empty string or wrong format)
                        log.info("Thread ["+ id + "] parse error: " + e.getMessage());
                    }
                }
            }

        } catch(Exception e) {
            log.error("Thread ["+ id + "] error: " + e.getMessage());
        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("Thread ["+ id + "] error: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Insert a new Line to the respective client TreeSet in the static ConcurrentHashMap
     * @param l line to add to HashMap
     */
    private void insert(Line l) {

    	if (Segmenter.map.containsKey(l.getClient())) {
    		Segmenter.map.get(l.getClient()).add(l);
    	} else {
            SortedSet<Line> lines = Collections.synchronizedSortedSet(new TreeSet<>());
    		lines.add(l);
    		
    		Segmenter.map.put(l.getClient(), lines);
    	}
    }
}