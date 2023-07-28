package com.github.kilianB.fileHandling.out;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import com.github.kilianB.model.netflix.NetflixMovie;
import com.uwetrottmann.trakt5.entities.Movie;

/**
 * A synchronized CSV writer for movie data.
 * 
 * This class extends the CSVWriter class and provides a method to push movie
 * data to a CSV file.
 * 
 * @author md-shadhin
 */
public class CSVMovieWriter extends CSVWriter {

	/**
	 * Constructs a CSVMovieWriter instance.
	 * 
	 * @param csvOutPath The path to the CSV file.
	 * @param delimiter  The CSV field delimiter.
	 * @throws IOException if an IOError occurs during file operations.
	 */
	public CSVMovieWriter(File csvOutPath, String delimiter) throws IOException {
		super(csvOutPath, delimiter, "Date", "Title", "Runtime", "Certificate", "Released", "Genres");
	}

	/**
	 * Appends movie data to the end of the CSV file in a synchronized manner.
	 * 
	 * @param movie      A Movie object retrieved from Trakt.
	 * @param netflixObj A matching movie item parsed from the viewing history file.
	 * @throws IOException if an IOError occurs during file operations.
	 */
	public void push(Movie movie, NetflixMovie netflixObj) throws IOException {
		String genres = (movie.genres != null) ? Arrays.toString(movie.genres.toArray(new String[movie.genres.size()]))
				: "";

		writeLine(netflixObj.getViewDate(), movie.title, movie.runtime, movie.certification, movie.released, genres);
	}
}
