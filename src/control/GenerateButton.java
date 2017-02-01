package control;

import view.DictionaryLoaderView;

import javax.swing.*;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class GenerateButton extends JButton {
	private final String GENERATE_LABEL = "Let's Light this Thing!";
	private final String INVALID_LABEL = "Choose a File";

	private DictionaryLoaderView dictionaryLoaderView;

	public GenerateButton(DictionaryLoaderView dictionaryLoaderView ){
		super( );
		this.dictionaryLoaderView = dictionaryLoaderView;

		setEnabled( false );
		updateText();
	}
	public void sync(){
		setEnabled( dictionaryLoaderView.getDictionarySelector().isReadyToGenerate() );
		updateText( );
	}

	private void updateText( ) {
		if( isEnabled() )
			setText( GENERATE_LABEL );
		else
			setText( INVALID_LABEL );
	}
}
