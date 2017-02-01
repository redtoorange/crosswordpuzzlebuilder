package view;

import control.OpenDictionaryButtonListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
	private DictionaryLoaderView startui;
	private boolean readyToGenerate = false;
	private BoxLayout layout;

	public DictionarySelector( DictionaryLoaderView startui ) {
		layout = new BoxLayout( this, BoxLayout.X_AXIS );
		setLayout( layout );
		setBorder( BorderFactory.createEmptyBorder( 0, 0, 10, 0 ) );
		init( );
		this.startui = startui;
	}

	private void init() {
		initLabel( );
		initField( );
		initChooser( );
		initOpenButton( );
	}

	private void initLabel() {
		dictionaryLabel = new JLabel( "Dictionary Location " );
		add( dictionaryLabel );
	}

	private void initField() {
		fileNameTextField = new FileTextField( 20 );
		add( fileNameTextField );
	}

	private void initChooser() {
		dictionaryChooser = new JFileChooser( new File( System.getProperty( "user.dir" ) ) );
		dictionaryChooser.addChoosableFileFilter( new FileNameExtensionFilter( "Text Document", ".txt" ) );
	}

	private void initOpenButton() {
		openDictionaryButton = new JButton( "Open Dictionary" );
		openDictionaryButton.addActionListener( new OpenDictionaryButtonListener(
				openDictionaryButton, dictionaryChooser, this, fileNameTextField
		) );
		add( openDictionaryButton );
	}

	public void setSelectedDictionary( File file ) {
		selectedDictionary = file;
		startui.syncChildren( );
	}

	public void sync() {
		updateReadyToGenerate( );
	}

	private void updateReadyToGenerate() {
		if ( selectedDictionary != null ) {
			readyToGenerate = true;
		}
		else {
			readyToGenerate = false;
		}
	}

	public boolean isReadyToGenerate() {
		return readyToGenerate;
	}
}
