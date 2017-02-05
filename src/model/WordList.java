package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * WordList.java - A custom type of collection that will encapsulate the list of word-definition pairs from a DictionaryFile.
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class WordList {
	private ArrayList<Word> wordArrList;

	public WordList( ){
		this( new ArrayList<Word>() );
	}

	public WordList(ArrayList<Word> wordArrList){
		this.wordArrList = wordArrList;
	}

	/**
	 * Add the given word to the ArrayList.
	 * @param word word to be added to the ArrayList.
	 */
	public void addWord( Word word ){
		wordArrList.add( word );
	}

	/**
	 * Return the next word in the ArrayList that has it's isPlaced flag set to false.
	 * @return the next word in the ArrayList that hasn't been placed.
	 */
	public Word getNextUnplaced(){
		Word unplaced = null;

		int index = 0;
		while(unplaced == null && index < wordArrList.size()){
			if( !wordArrList.get( index ).isWrittenOnGrid() )
				unplaced = wordArrList.get( index );
			else
				index++;
		}

		return unplaced;
	}

	/**
	 * How many words are currently inside the WordList.
	 * @return the int size of the list.
	 */
	public int listSize(){
		return wordArrList.size();
	}

	/**
	 * Reset each word.  Nullifies it's placement and resets it's isPlaced flag to false.
	 */
	public void reset(){
		for(Word w : wordArrList)
			w.reset();
	}

	/**
	 * Shuffle the WordList.  This allows for more randomness when generating new grids.
	 */
	public void shuffle(){
		Collections.shuffle( wordArrList );
	}

	/**
	 * Print each word inside the WordList using the word's toString method.
	 * @return A String containing all the word-definition pairs inside the WordList.  Each word on a new line.
	 */
	@Override
	public String toString() {
		String string = "";

		for( Word w : wordArrList )
			string += w;

		return string;
	}

	/**
	 * Push the indicated word to the back of the ArrayList,  this will effectively make it the last word in line
	 * to be placed on the board
	 * @param word Which word to push to the back.
	 */
	public void pushToBack( Word word ){
		wordArrList.remove( word );
		word.reset();
		wordArrList.add( word );
	}

}
