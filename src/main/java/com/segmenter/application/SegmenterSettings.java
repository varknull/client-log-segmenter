package com.segmenter.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "client")
public class SegmenterSettings {

    private String baseUrl;
    private String files;
    private String name;
    private String timestamp;
    private String logname;


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getFiles() {
        try {
            return Integer.parseInt(files);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getLogname(int n) {
        return getLogname().replace("#", String.valueOf(n));
    }

    /**
     * Should be in format https://www.alephd.com/{word]/
     * @param baseurl
     * @return
     */
    public static boolean isBaseUrlValid(String baseurl) {
        String regex = "^(https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]\\/\\w+\\/";

        if (baseurl.matches(regex)) {
            return true;
        }
        throw new UnsupportedOperationException("Invalid baseUrl parameter");
    }

    public static int isFilesNumberValid(String filesNumber) {

        int files = 0;
        try {
            files = Integer.parseInt(filesNumber);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Invalid filesNumber parameter");
        }

        return files;
    }
}