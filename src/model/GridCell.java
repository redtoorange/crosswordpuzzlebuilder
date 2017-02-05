package model;

import java.util.ArrayList;

/**
 * GridCell.java - Model of a single cell in the grid.  Encapsulates references to all the words that occupy the cell.
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
public class GridCell {
	private char character = 0;
	private ArrayList<Word> references = new ArrayList<Word>(  );

	public char getCharacter() {
		return character;
	}

	public void setCharacter( char character ) {
		this.character = character;
	}

	public ArrayList<Word> getReferences() {
		return references;
	}

	public void addReference( Word w){
		references.add( w );
	}

	public void removeReference( Word w){
		references.remove( w );
		if(references.size() <= 0)
			setCharacter( (char)0 );
	}

	public void reset(){
		setCharacter( ( char ) 0 );
		getReferences().clear();
	}
}
