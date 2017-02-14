package model;

import java.util.ArrayList;

/**
 * GridCell.java - Model of a single cell in the {@link Grid}.  Encapsulates references to all the {@link Word}s that
 * occupy this {@link GridCell}.
 *
 * @author - Andrew McGuiness
 * @version - 14/Feb/2017
 * @see Word
 * @see Grid
 */
public class GridCell {
	/**
	 * char that is contained in this {@link GridCell}.
	 */
	private char character = 0;
	/**
	 * An {@link ArrayList} of all the {@link Word}s that overlap this {@link GridCell}.
	 */
	private ArrayList< Word > references = new ArrayList< Word >( );

	/**
	 * Get the char that this {@link GridCell} represents.
	 *
	 * @return primitive char that is contained in this {@link GridCell}.
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * Set the char for this {@link GridCell}.
	 *
	 * @param character the char to put into this {@link GridCell}.
	 */
	public void setCharacter( char character ) {
		this.character = character;
	}

	/**
	 * Get the list of all {@link Word}s that reference this {@link GridCell}.  Each one has a char that is in this {@link GridCell}.
	 *
	 * @return The list of {@link Word}s that overlap this {@link GridCell}.
	 */
	public ArrayList< Word > getReferences() {
		return references;
	}

	/**
	 * Add a {@link Word} to this {@link GridCell}'s references.
	 *
	 * @param word The {@link Word} that overlaps with this {@link GridCell} and should be added to references..
	 */
	public void addReference( Word word ) {
		references.add( word );
	}

	/**
	 * Remove a {@link Word} from this {@link GridCell}'s references.  If {@link Word} references are 0, then the char
	 * is set to 0 (empty char).
	 *
	 * @param word The {@link Word} that should be removed from the {@link GridCell} because it no longer overlaps with it.
	 */
	public void removeReference( Word word ) {
		references.remove( word );
		if ( references.size( ) <= 0 )
			setCharacter( ( char ) 0 );
	}

	/**
	 * Clear all the {@link Word} references from this {@link GridCell} and reset the char to 0 (empty char).
	 */
	public void reset() {
		setCharacter( ( char ) 0 );
		getReferences( ).clear( );
	}
}
