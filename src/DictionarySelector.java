import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class DictionarySelector extends JPanel {
	private File selectedDictionary;
	private JFileChooser dictionaryChooser;
	private FileTextField fileNameTextField;
	private JButton openDictionaryButton;
	private JLabel dictionaryLabel;
	private StartUI startui;
	private boolean readyToGenerate = false;

	public DictionarySelector( StartUI startui ){
		init();
		this.startui = startui;
	}

	private void init(){
		initLabel( );
		initField( );
		initChooser( );
		initOpenButton( );
	}

	private void initLabel( ) {
		dictionaryLabel = new JLabel( "Dictionary Location" );
		add( dictionaryLabel );
	}

	private void initField( ) {
		fileNameTextField = new FileTextField( 20 );
		add( fileNameTextField );
	}

	private void initChooser( ) {
		dictionaryChooser = new JFileChooser( new File( System.getProperty( "user.dir" ) ) );
		dictionaryChooser.addChoosableFileFilter( new FileNameExtensionFilter( "Text Document", ".txt" ) );
	}

	private void initOpenButton( ) {
		openDictionaryButton = new JButton( "Open Dictionary" );
		openDictionaryButton.addActionListener( new OpenDictionaryButtonListener(
				openDictionaryButton, dictionaryChooser, this, fileNameTextField
		) );
		add( openDictionaryButton );
	}

	public void setSelectedDictionary(File file){
		selectedDictionary = file;
		startui.sync();
	}

	public void sync(){
		updateReadyToGenerate( );
	}

	private void updateReadyToGenerate( ) {
		if(selectedDictionary != null)
			readyToGenerate = true;
		else
			readyToGenerate = false;
	}

	public boolean isReadyToGenerate(){
		return readyToGenerate;
	}
}
