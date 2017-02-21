package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * WordList.java - A custom collection that is backed by an ArrayList.  Encapsulates the list of word-definition pairs
 * from a DictionaryFile.  This simplifies interaction between the controllers and the DictionaryFile.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 */
public class WordList {
	/** The {@link ArrayList} of {@link Word}s that contains all the words from a {@link DictionaryFile}. */
	private ArrayList< Word > wordsArrayList;

	/**
	 * Create a new {@link WordList} with no {@link Word}s.
	 */
	public WordList() {
		this( new ArrayList< Word >( ) );
	}

	/**
	 * Create a new {@link WordList} with a starting {@link ArrayList} of {@link Word}s that is passed in.
	 *
	 * @param wordsArrayList The initial {@link ArrayList} of {@link Word}s to use.
	 */
	public WordList( ArrayList< Word > wordsArrayList ) {
		this.wordsArrayList = wordsArrayList;
	}

	/**
	 * Add the given {@link Word} to the {@link WordList}.
	 *
	 * @param word {@link Word} to be added to the {@link WordList}.
	 */
	public void addWord( Word word ) {
		wordsArrayList.add( word );
	}

	/**
	 * Return the next {@link Word} in the {@link WordList} that has it's placement flag set to false.
	 *
	 * @return Next {@link Word} in the {@link WordList} that hasn't been placed.
	 */
	public Word getNextUnplaced() {
		Word unplaced = null;

		int index = 0;
		while ( unplaced == null && index < wordsArrayList.size( ) ) {
			if ( !wordsArrayList.get( index ).isWrittenOnGrid( ) )
				unplaced = wordsArrayList.get( index );
			else
				index++;
		}

		return unplaced;
	}

	/**
	 * How many {@link Word}s are currently inside this {@link WordList}.
	 *
	 * @return Length of the {@link WordList}.
	 */
	public int listSize() {
		return wordsArrayList.size( );
	}

	/**
	 * Reset each {@link Word}. This Nullifies it's {@link WordPlacement} and resets it's placed flag to false.
	 */
	public void reset() {
		for ( Word w : wordsArrayList )
			w.reset( );
	}

	/**
	 * Shuffle the {@link WordList}.  This allows for more randomness when generating new {@link Grid}s.
	 */
	public void shuffle() {
		Collections.shuffle( wordsArrayList );
	}

	/**
	 * Print each {@link Word} in this {@link WordList} using the {@link Word}'s toString method.
	 *
	 * @return A {@link String} containing all the word-definition pairs in this {@link WordList}.  Each word on a new
	 * line.
	 */
	@Override
	public String toString() {
		String string = "";

		for ( Word w : wordsArrayList )
			string += w;

		return string;
	}

	/**
	 * Push the indicated {@link Word} to the back of the {@link WordList},  this will effectively make it the last word
	 * in line to be placed on the {@link Grid}.  Used if a word is unable to find a suitable {@link WordPlacement}
	 * based on the current {@link Grid}.
	 *
	 * @param word Which {@link Word} to push to the back.
	 */
	public void pushToBack( Word word ) {
		wordsArrayList.remove( word );
		word.reset( );
		wordsArrayList.add( word );
	}
}
