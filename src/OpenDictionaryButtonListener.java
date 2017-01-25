import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class OpenDictionaryButtonListener implements ActionListener {
	private JButton openDictionaryButton;
	private JFileChooser dictionaryChooser;
	private DictionarySelector dictionarySelector;
	private FileTextField fileNameTextField;
	private File selectedFile;

	public OpenDictionaryButtonListener( JButton openDictionaryButton, JFileChooser dictionaryChooser,
										 DictionarySelector dictionarySelector, FileTextField fileNameTextField ) {
		this.openDictionaryButton = openDictionaryButton;
		this.dictionaryChooser = dictionaryChooser;
		this.dictionarySelector = dictionarySelector;
		this.fileNameTextField = fileNameTextField;
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if ( e.getSource( ) == openDictionaryButton ) {
			int chooserReturnState = dictionaryChooser.showOpenDialog( dictionarySelector );
			switch(chooserReturnState){
				case JFileChooser.APPROVE_OPTION:
					selectedFile = dictionaryChooser.getSelectedFile( );
					fileNameTextField.setText( selectedFile.getAbsolutePath( ) );

					if( fileNameTextField.getValidFileName() )
						dictionarySelector.setSelectedDictionary( selectedFile );
					else
						dictionarySelector.setSelectedDictionary( null );

					break;
				case JFileChooser.CANCEL_OPTION: default:
					if( selectedFile != null )
						fileNameTextField.setText( selectedFile.getAbsolutePath( ) );
					else
						fileNameTextField.setText( "" );
					break;
			}
		}
	}
}
