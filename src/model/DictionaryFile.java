package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * DictionaryFile.java - Model representation of a {@link DictionaryFile} loaded by the {@link
 * control.DictionaryLoaderController}. The contents of the file will be used to populate the {@link WordList} with
 * words and definitions from the selected text file.
 *
 * @author Andrew McGuiness
 * @version 14/Feb/2017
 */
public class DictionaryFile {
	/**
	 * A collection of {@link Word}s that has helpful manipulation commands.
	 */
	private WordList wordList;

	/**
	 * Create a new {@link DictionaryFile} from a pre-formatted text {@link File}.  A reference to the {@link File} is
	 * not maintained.e
	 *
	 * @param file The {@link File} that will be scanned for {@link Word}s.
	 *
	 * @throws IncompleteWordException	Thrown if the passed {@link File} contains a word but no definition on a line.
	 * @throws FileNotFoundException	Thrown if the passed {@link File} is cannot be found.
	 */
	public DictionaryFile( File file ) throws IncompleteWordException, FileNotFoundException {
		parseFile( file );
		reset( );
	}

	/**
	 * Load up a new {@link Scanner} from the passed in {@link File}.  This {@link Scanner} is then passed to generate
	 * the {@link WordList}.  The handle to the {@link File} is released after this method.
	 *
	 * @param file	The {@link File} to be parsed.
	 *
	 * @throws IncompleteWordException	Thrown if the passed {@link File} contains a word but no definition on a line.
	 * @throws FileNotFoundException	Thrown if the passed {@link File} is cannot be found.
	 * @see #generateWordList(Scanner) Does the actual parsing
	 */
	private void parseFile( File file ) throws IncompleteWordException, FileNotFoundException {
		Scanner fileScanner = new Scanner( file );
		generateWordList( fileScanner );
		fileScanner.close( );
	}

	/**
	 * This does the actual work of scanning the {@link File} with a {@link Scanner}.  Each {@link String} "word
	 * definition" token pair is stored in a {@link Word} and then added to the {@link WordList}.
	 *
	 * @param scanner A {@link Scanner} that has been initialized with a {@link File} that contains "word definition"
	 *                pairs.
	 *
	 * @throws IncompleteWordException If there is a word with no definition, then this exception will be thrown.
	 */
	private void generateWordList( Scanner scanner ) throws IncompleteWordException {
		wordList = new WordList( );

		while ( scanner.hasNext( ) ) {
			String word = scanner.next( );
			String definition = scanner.nextLine( );

			if ( word.isEmpty( ) || definition.isEmpty( ) )
				throw new IncompleteWordException( );
			else
				wordList.addWord( new Word( word, definition ) );
		}

		wordList.shuffle( );
	}

	/**
	 * Get a reference to the {@link WordList}.  Used to access the actual {@link Word}s inside the {@link File}.
	 *
	 * @return The {@link WordList} that contains all the {@link Word}s from the original {@link File}.
	 */
	public WordList getWordList( ) {
		return wordList;
	}

	/**
	 * Convert the {@link WordList} into a {@link String}.  Used mostly for debugging.
	 *
	 * @return {@link String} of all the {@link Word}s in the {@link WordList}.
	 */
	@Override
	public String toString( ) {
		String s = "";
		if ( wordList != null )
			s += wordList;
		return s;
	}

	/**
	 * Call reset on the {@link WordList} and then shuffle it into a random order.  This shuffling will help with
	 * randomizing  the list to generate new {@link Grid}s from the same list and also to prevent the {@link Grid} from
	 * failing because the {@link Word}s are in a certain order.
	 */
	public void reset( ) {
		wordList.reset( );
		wordList.shuffle( );
	}
}
