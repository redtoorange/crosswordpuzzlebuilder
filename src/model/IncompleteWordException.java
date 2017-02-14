package model;

/**
 * IncompleteWordException.java - A custom exception that is used to handle a case where a {@link Word} in {@link DictionaryFile}
 * contains a word {@link String}, but no definition {@link String}.  This might be typical for the last word in a
 * {@link DictionaryFile} or something.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see DictionaryFile
 */
public class IncompleteWordException extends Exception {
	/**
	 * Default constructor that is used to create a new exception indicating that the DictionaryFile contains a {@link Word}
	 * that is ill-formed because it doesn't have a definition.
	 */
	public IncompleteWordException() {
		super( "The Word - Defintion pair is incomplete." );
	}
}
