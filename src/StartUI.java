import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class StartUI {
	private JFrame mainFrame;
	private JPanel mainPanel;
	private DictionarySelector dictionarySelector;
	private GenerateButton generateButton;

	public StartUI( ) {
		init();
	}

	private void init(){
		mainFrame = new JFrame( "Crossword Builder 2K17" );
		//mainFrame.setResizable( false );
		mainFrame.setSize( 1000, 400 );
		mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );


		mainPanel = new JPanel(  );
		//mainPanel.setLayout(  new BoxLayout( mainPanel, BoxLayout.Y_AXIS )  );
		//mainPanel.setSize( 400, 400 );
		mainFrame.add( mainPanel );


		dictionarySelector = new DictionarySelector( this );
		dictionarySelector.setLocation( 100, 10 );
		mainPanel.add( dictionarySelector );

		generateButton = new GenerateButton( this, dictionarySelector );
		mainPanel.add( generateButton );

		//mainFrame.pack();
		mainFrame.setVisible( true );
	}

	public void sync(){
		dictionarySelector.sync();
		generateButton.sync();

		mainFrame.pack();
	}
}
