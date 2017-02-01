package view;

import control.GenerateButton;

import javax.swing.*;

/**
 * DictionaryLoaderView.java - UI View to select the Dictionary file.
 *
 * @author Andrew McGuiness
 * @version 25/Jan/2017
 */
public class DictionaryLoaderView {
	private JFrame windowFrame;
	private JPanel primaryPanel;

	private DictionarySelector dictionarySelector;
	private GenerateButton generateButton;

	/**
	 * Attempt to set the look and feel to something more pleasant.  Then initalize all the elements of the Window.
	 */
	public DictionaryLoaderView( ) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e ){ }

		initUI();
	}

	/**
	 * Create and add all the elements to the window.
	 */
	private void initUI(){
		windowFrame = new JFrame( "Crossword Builder 2K17" );
		windowFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );


		primaryPanel = new JPanel(  );
		primaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		primaryPanel.setLayout(  new BoxLayout( primaryPanel, BoxLayout.Y_AXIS )  );
		windowFrame.add( primaryPanel );


		dictionarySelector = new DictionarySelector( this );
		primaryPanel.add( dictionarySelector );

		generateButton = new GenerateButton( this );
		primaryPanel.add( generateButton );

		windowFrame.pack();
		windowFrame.setVisible( true );
	}

	/**
	 * Allows for the elements to create a callback system with the main window.
	 */
	public void syncChildren(){
		dictionarySelector.sync();
		generateButton.sync();

		windowFrame.pack();
	}

	/**
	 * Grab a reference to the DictionarySelector that is in the windowFrame.
	 * @return The DictionarySelector which contains information about which file was selected and if it was valid.
	 */
	public DictionarySelector getDictionarySelector(){
		return dictionarySelector;
	}
}
