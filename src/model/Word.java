package model;

/**
 * Word.java - A model of a word.  Contains the {@link String} definition, the {@link String} word and it's {@link WordPlacement}.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 */
public class Word implements Comparable {
	/**
	 * Immutable. The {@link String} that this word represents.  Loaded from a {@link DictionaryFile} during construction.
	 */
	private final String word;

	/**
	 * Immutable. The {@link String} definition for this word.  Loaded from a {@link DictionaryFile} during construction.
	 */
	private final String definition;

	/**
	 * The {@link WordPlacement} of a word on the {@link Grid}.  It is only set by the {@link control.GridController}.
	 */
	private WordPlacement wordPlacement = null;

	/**
	 * Flag used by the {@link control.GridController}. to determine if a {@link Word} is currently written on a {@link Grid}.
	 */
	private boolean writtenOnGrid = false;

	/**
	 * Constructor that sets the Word's word and definition, they are immutable.
	 *
	 * @param word       A word loaded from a {@link DictionaryFile} that this Word object represents.
	 * @param definition A definition loaded from a {@link DictionaryFile} that is related to this Word's word.
	 */
	public Word( String word, String definition ) {
		this.word = word;
		this.definition = definition;
	}

	/**
	 * Get the {@link String} that was given in the original {@link DictionaryFile}.
	 *
	 * @return {@link String} word that was inside the {@link DictionaryFile}.
	 */
	public String getWordString() {
		return word;
	}

	/**
	 * Get the word's {@link String} definition as it was written in the {@link DictionaryFile}.
	 *
	 * @return {@link String} definition that was inside the {@link DictionaryFile}.
	 */
	public String getDefinitionString() {
		return definition;
	}

	/**
	 * Has this {@link Word} already been written to a {@link Grid} by the {@link control.GridController}.
	 *
	 * @return Flag that represents if the word is currently written to the {@link Grid}.
	 */
	public boolean isWrittenOnGrid() {
		return writtenOnGrid;
	}

	/**
	 * Set that this {@link Word} has been written to a {@link Grid} by the {@link control.GridController}.
	 *
	 * @param writtenOnGrid Flag that represents if the word is currently written to the {@link Grid}.
	 */
	public void setWrittenOnGrid( boolean writtenOnGrid ) {
		this.writtenOnGrid = writtenOnGrid;
	}


	/**
	 * Get the word's {@link WordPlacement} on the {@link Grid}.
	 *
	 * @return {@link WordPlacement} of the word.
	 */
	public WordPlacement getWordPlacement() {
		return wordPlacement;
	}

	/**
	 * Set the word's {@link WordPlacement} on the {@link Grid}.
	 *
	 * @param wordPlacement {@link WordPlacement} this word should have.
	 */
	public void setWordPlacement( WordPlacement wordPlacement ) {
		this.wordPlacement = wordPlacement;
	}

	/**
	 * Reset the Word's {@link WordPlacement} and Flag.
	 */
	public void reset() {
		wordPlacement = null;
		writtenOnGrid = false;
	}

	/**
	 * Converts this {@link Word} into a {@link String}.
	 *
	 * @return formatted {@link String} in the form of: "word : definition\n"
	 */
	@Override
	public String toString() {
		return word + " - " + definition + "\n";
	}

	/**
	 * Compares the length of two {@link Word}'s and return the difference.
	 *
	 * @param otherWord reference to a {@link Word} you want to compare to.
	 * @return returns negative if this word is shorter than the argument.
	 */
	@Override
	public int compareTo( Object otherWord ) {
		int difference = 0;

		if ( otherWord instanceof Word )
			difference = word.length( ) - ( ( Word ) otherWord ).getWordString( ).length( );

		return difference;
	}
}
