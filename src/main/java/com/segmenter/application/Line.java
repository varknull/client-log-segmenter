package com.segmenter.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Splitter;
import sun.nio.ch.IOStatus;

public class Line implements Comparable<Line> {

	private String k; /* A string of some random characters */ 
	private String client; /* A unique string identifying a client */ 
	private long timestamp; /* A unix timestamp */


    public Line(String k, String client, long timestamp) {
        this.k = k;
        this.client = client;
        this.timestamp = timestamp;
    }

    /**
     * Line constructor that builds the object according to the string format
     * "K client=String&timestamp=Integer"
     *
     * @param line String formatted as "K client=String&timestamp=Integer"
     * @param config define the name of the parameters client and timestamp
     */
	public Line(String line, SegmenterSettings config) {

        try {
            List<String> results = Splitter.on(" ")
                       .trimResults() // only if you need it
                       .omitEmptyStrings() // only if you need it
                       .splitToList(line);

            Map<String,String> parameters = Splitter
                    .on("&")
                    .withKeyValueSeparator("=")
                    .split(results.get(1));

            this.k = results.get(0);
            this.client = parameters.get(config.getName());


			this.timestamp = Long.parseLong(parameters.get(config.getTimestamp()));
		} catch (Exception e) {
			throw new UnsupportedOperationException("Could not build bean");
		}
	}
	
	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

    /**
     * Used to keep sorted the Lines in the TreeSet
     */
	@Override 
	public int compareTo(Line other) {
		return Long.compare(this.timestamp, other.timestamp);
	}

    /**
     * Assuming the lines have always different k,client,timestamp triple (because TreeSet doesn't allow duplicate)
     */
    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Line) {
            final Line other = (Line) obj;

            return  Objects.equals(this.k, other.k)
                    && Objects.equals(this.client, other.client)
                    && Objects.equals(this.timestamp, other.timestamp);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.k, this.client, this.timestamp);
    }

    /**
     * Convert the Line object to a String ready to be written in the file
     * @return String in the chosen format
     */
    @Override
	public String toString() {
		return k + " " + "client=" + client + "&timestamp=" + timestamp;
	}
}
