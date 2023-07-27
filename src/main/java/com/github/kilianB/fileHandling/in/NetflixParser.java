package com.github.kilianB.fileHandling.in;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kilianB.model.netflix.NetflixMovie;
import com.github.kilianB.model.netflix.NetflixShowEpisode;
import com.github.kilianB.model.netflix.ViewItem;

public class NetflixParser {
    private static final Logger LOGGER = Logger.getLogger(NetflixParser.class.getName());
    private static final String INPUT_DELIMITER = ",";
    private final static Pattern splitLine = Pattern.compile("\"(?<Title>.*)\"" + INPUT_DELIMITER + "\"(?<Date>.*)\"");
    private final static Pattern showPattern = Pattern.compile(
            "(?<series>.*(?:(?:Season|Staffel|Part) [0-9]+)(?=:)): (?<epTitle>.*)",
            java.util.regex.Pattern.UNICODE_CHARACTER_CLASS);
    private final static Pattern seasonPattern = Pattern.compile(
            "(?<series>.*(?=:)): (?<seasonText>[^0-9]*)(?<season>[0-9]*)",
            java.util.regex.Pattern.UNICODE_CHARACTER_CLASS);

    private NetflixParser(String filePath) {}

    public static ArrayList<ViewItem> parseHistoryFile(String filePath) throws IOException {
        Objects.requireNonNull(filePath);
        File viewFile = new File(filePath);

        if (!viewFile.exists()) {
            throw new IllegalArgumentException("Abort: Can not read Netflix view history file.");
        }

        var viewHistory = new ArrayList<ViewItem>();

        try (BufferedReader br = new BufferedReader(new FileReader(viewFile))) {
            br.readLine(); // Skip CSV header

            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = splitLine.matcher(line);
                if (m.find()) {
                    var parsedEntry = parseEntry(m.group("Title"), m.group("Date"));
                    viewHistory.add(parsedEntry);
                } else {
                    LOGGER.warning("Line in the CSV file can not be tokenized. Skipping line: " + line);
                }
            }
            return viewHistory;
        }
    }

    static ViewItem parseEntry(String title, String date) {
        Matcher m = showPattern.matcher(title);

        boolean matchFound = m.find();
        if (matchFound) {
            String seriesRaw = m.group("series");
            String episodeTitle = m.group("epTitle");

            Matcher titleMatcher = seasonPattern.matcher(seriesRaw);

            if (titleMatcher.find()) {
                String series = titleMatcher.group("series");
                int season = Integer.parseInt(titleMatcher.group("season"));
                return new NetflixShowEpisode(episodeTitle, date, season, series);
            } else {
                return new NetflixMovie(title, date);
            }
        } else {
            return new NetflixMovie(title, date);
        }
    }

    public NetflixParser(String fileName, String delimiter) throws IOException {
        File csvFile = new File(fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            // Add logic for handling this constructor if needed
        }
    }
}