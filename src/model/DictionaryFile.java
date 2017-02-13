package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * DictionaryFile.java - Model representation of a Dictionary file loaded in by the user.  Will be used to create the final crossword puzzle.
 *
 * @author Andrew McGuiness
 * @version 25/Jan/2017
 */
//TODO: Fix Comments
public class DictionaryFile {
	/**
	 * A reference to the file on disk.
	 */
	private File file;
	private WordList wordList;

	/**
	 * Create a new Dictionary file from a pre-formatted text file.
	 * @param file The Dictionary file that will be loaded.
	 */
	public DictionaryFile( File file ) throws IncompleteWordException, FileNotFoundException {
		this.file = file;
		parseFile( );
		reset();
	}

	/**
	 * Parse the text file, pulling out the tokens in word : definition pairs.
	 */
	private void parseFile() throws IncompleteWordException, FileNotFoundException {
		Scanner fileScanner = new Scanner( file );
		generateWordList( fileScanner );
		fileScanner.close();
	}

	private void generateWordList( Scanner scanner ) throws IncompleteWordException{
		wordList = new WordList();

		while(scanner.hasNext()){
			String word = scanner.next();
			String definition = scanner.nextLine();

			if(word.isEmpty() || definition.isEmpty())
				throw new IncompleteWordException();
			else
				wordList.addWord( new Word( word, definition ) );
		}

		wordList.shuffle();
	}

	public WordList getWordList(){
		return wordList;
	}

	@Override
	public String toString() {
		String s = "";
		if(wordList != null)
			s += wordList;
		return s;
	}

	public void reset(){
		wordList.reset();
		wordList.shuffle();
	}
}
