package control;
/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 27/Jan/2017
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CrosswordPuzzleImage;
import model.DictionaryFile;
import model.Grid;
import model.SceneType;

import java.io.File;

public class ApplicationController extends Application {
	private DictionaryLoaderController dictionaryLoaderController;
	private CrosswordViewController crosswordViewController;

	private Scene dictionaryScene;
	private Scene viewerScene;

	private Stage mainStage;
	private DictionaryFile currentDictionaryFile;
	private File crosswordImage;
	private Grid puzzleGrid;

	public static void main( String[] args ) {
		launch( args );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		mainStage = primaryStage;

		initDictionaryLoader( );
		initCrosswordViewer( );

		primaryStage.setScene( dictionaryScene );
		primaryStage.setTitle( "Crossword Generator 2K17" );
		primaryStage.show();
	}

	private void initDictionaryLoader() throws java.io.IOException {
		FXMLLoader dictionay = new FXMLLoader( getClass().getResource("../view/DictionaryLoader.fxml") );
		dictionaryScene = new Scene( dictionay.load() );

		dictionaryLoaderController = dictionay.getController();
		dictionaryLoaderController.init( this );
	}

	private void initCrosswordViewer() throws java.io.IOException {
		FXMLLoader viewer = new FXMLLoader( getClass().getResource( "../view/CrosswordView.fxml" ) );
		viewerScene = new Scene( viewer.load() );

		crosswordViewController = viewer.getController();
		crosswordViewController.init( this );
	}

	public void swapScene( SceneType sceneType ){
		mainStage.hide();

		if(sceneType == SceneType.DICTIONARY_LOADER )
			mainStage.setScene( dictionaryScene );
		else
			mainStage.setScene( viewerScene );

		mainStage.sizeToScene();
		mainStage.show();
	}

	public void generateCrossword( DictionaryFile dictionaryFile ){
		this.currentDictionaryFile = dictionaryFile;

		puzzleGrid = new Grid( 100, 100, dictionaryFile );
		CrosswordPuzzleImage image = puzzleGrid.getPuzzleImage();

		crosswordImage = image.writeImageToFile( "temp.png" );
		crosswordViewController.loadImage( crosswordImage );
		swapScene( SceneType.IMAGE_VIEWER );
	}

	public void regenerateCrossword(){
		currentDictionaryFile.reset();

		puzzleGrid.rebuildPuzzle( 100, 100, currentDictionaryFile );
		CrosswordPuzzleImage image = puzzleGrid.getPuzzleImage();

		crosswordImage = image.writeImageToFile( "temp.png" );
		crosswordViewController.loadImage( crosswordImage );
	}

	@Override
	public void stop() throws Exception {

		if(crosswordImage != null)
			crosswordImage.delete();
		super.stop( );
	}
}
