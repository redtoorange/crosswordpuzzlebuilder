package control;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

/**
 * GeneratingViewController.java - Controller class for the GeneratingView that controls the background functionality of
 * the window.
 *
 * @author
 * @version 08/Feb/2017
 */
public class GeneratingViewController {
	private ApplicationController controller;
	@FXML private Pane root;
	@FXML private ProgressIndicator indicator;

	/**
	 * Used as a proxy constructor since the FXML loader auto injects most of the parameters from the FXML file.
	 * @param controller The main ApplicationController that can be used to switchScenes
	 */
	public void init( ApplicationController controller ){
		this.controller = controller;
	}

	public void show(){
		indicator.setVisible( true );
	}

}
