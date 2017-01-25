import javax.swing.*;
import java.awt.*;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class GenerateButton extends JButton {
	private final String GENERATE_LABEL = "Let's Light this Thing!";
	private final String INVALID_LABEL = "Choose a File";


	private StartUI startUI;
	private DictionarySelector dictionarySelector;

	public GenerateButton( StartUI startUI, DictionarySelector dictionarySelector ){
		super( );

		this.dictionarySelector = dictionarySelector;
		this.startUI = startUI;
		setEnabled( false );
		updateText();
	}
	public void sync(){
		setEnabled( dictionarySelector.isReadyToGenerate() );
		updateText( );
	}

	private void updateText( ) {
		if( isEnabled() )
			setText( GENERATE_LABEL );
		else
			setText( INVALID_LABEL );
	}
}
