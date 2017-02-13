package model;

import java.util.ArrayList;

/**
 * GridCell.java - Model of a single cell in the grid.  Encapsulates references to all the words that occupy the cell.
 *
 * @author - Andrew McGuiness
 * @version - 04/Feb/2017
 */
public class GridCell {
	private char character = 0;
	private ArrayList<Word> references = new ArrayList<Word>(  );

	/**
	 * Get the character that this GridCell represents.
	 * @return
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * Set the character for this GridCell.
	 * @param character
	 */
	public void setCharacter( char character ) {
		this.character = character;
	}

	/**
	 * Get the list of all Words that reference this cell.  Each one has a letter that is in this GridCell.
	 * @return The list of Words that overlap this GridCell.
	 */
	public ArrayList<Word> getReferences() {
		return references;
	}

	/**
	 * Add a word to this GridCell's references.
	 * @param word The word that overlaps with this GridCell.
	 */
	public void addReference( Word word){
		references.add( word );
	}

	/**
	 * Remove a word from this GridCell's references.  If references are 0, then the character is reset to 0.
	 * @param word The word that should be removed from the GridCell because it no longer overlaps with it.
	 */
	public void removeReference( Word word){
		references.remove( word );
		if(references.size() <= 0)
			setCharacter( (char)0 );
	}

	/**
	 * Clear all the references from this GridCell and reset the character to 0.
	 */
	public void reset(){
		setCharacter( ( char ) 0 );
		getReferences().clear();
	}
}
