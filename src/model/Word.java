package model;

/**
 * Word.java - A model of a word.  Contains the definition, the word and it's eventual placement.  Has a flag for the words
 * placement.
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
//TODO: Fix Comments
public class Word implements Comparable {
	private String word;
	private String definition;

	private WordPlacement wordPlacement;
	private boolean writtenOnGrid = false;

	public Word( String word, String definition ){
		this.word = word;
		this.definition = definition;
	}

	/**
	 * Get the String that was given in the original DictionaryFile.
	 * @return the String word that was inside the dictionary file
	 */
	public String getWordString(){
		return word;
	}

	/**
	 * Get the word's definition as it was written in the original DictionaryFile.
	 * @return the String definition that was inside the dictionary file
	 */
	public String getDefinitionString(){
		return definition;
	}

	/**
	 * Get the flag to determine if this word has already been written.
	 * @return a flag that represents if the word is currently written to the board
	 */
	public boolean isWrittenOnGrid(){
		return writtenOnGrid;
	}

	/**
	 * Set the flag that says if this word is already on the board.
	 * @param writtenOnGrid a flag that represents if the word is currently written to the board
	 */
	public void setWrittenOnGrid( boolean writtenOnGrid ){
		this.writtenOnGrid = writtenOnGrid;
	}


	/**
	 * Get the word's placement on the board.
	 * @return the placement of the word.
	 */
	public WordPlacement getWordPlacement() {
		return wordPlacement;
	}

	/**
	 * Set the word's placement on the board.
	 * @param wordPlacement the placement this word will have.
	 */
	public void setWordPlacement( WordPlacement wordPlacement ) {
		this.wordPlacement = wordPlacement;
	}

	/**
	 * Converts the word model into a word representation
	 * @return formatted word in the form of: word : definition \n
	 */
	@Override
	public String toString() {
		return word + " - " + definition + "\n";
	}

	/**
	 * Compares the length of two words and returns the difference.
	 * @param otherWord reference to a word you want to compare to
	 * @return returns negative if this word is shorter than the argument.
	 */
	public int compareTo( Object otherWord ) {
		int difference = 0;

		if( otherWord instanceof Word)
			difference = word.length() - ((Word)otherWord).getWordString().length();

		return difference;
	}

	/**
	 * Reset the word's placement and flags.
	 */
	public void reset(){
		wordPlacement = null;
		writtenOnGrid = false;
	}
}
