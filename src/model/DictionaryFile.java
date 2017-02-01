package model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * DictionaryFile.java - Model representation of a Dictionary file loaded in by the user.  Will be used to create the final crossword puzzle.
 *
 * @author Andrew McGuiness
 * @version 25/Jan/2017
 */
public class DictionaryFile {
	/**
	 * A reference to the file on disk.
	 */
	private File file;

	/**
	 * Create a new Dictionary file from a pre-formatted text file.
	 * @param pathname The path to the Dictionary file that will be loaded.
	 */
	public DictionaryFile( String pathname ) {
		this.file = new File( pathname );
		parseFile( );
	}

	/**
	 * Parse the text file, pulling out the tokens in word : definition pairs.
	 */
	private void parseFile() {
		try {
			Scanner scanner = new Scanner( new FileInputStream( file ) );
		} catch ( Exception e ) {
			System.out.println( "Error parsing the file." );
			e.printStackTrace( );
		}
	}
}
