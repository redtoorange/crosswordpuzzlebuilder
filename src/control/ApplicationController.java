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
import model.ApplicationState;
import model.CrosswordPuzzleImage;
import model.DictionaryFile;
import model.Grid;

public class ApplicationController extends Application {
	private DictionaryLoaderController dictionaryLoaderController;
	private CrosswordViewController crosswordViewController;
	private GeneratingViewController generatingViewController;
	private CrosswordPuzzleImageController crosswordPuzzleImageController;

	private Scene dictionaryScene;
	private Scene viewerScene;

	private Scene generatingScene;
	private Stage generatingStage;

	private Stage mainStage;
	private DictionaryFile currentDictionaryFile;

	//private CrosswordPuzzleImage currentCrosswordPuzzleImage;
	private Grid puzzleGrid;

	public static void main( String[] args ) {
		launch( args );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		mainStage = primaryStage;

		initDictionaryLoader( );
		initCrosswordViewer( );
		initGeneratingView();

		crosswordPuzzleImageController = new CrosswordPuzzleImageController();

		primaryStage.setScene( dictionaryScene );
		primaryStage.setTitle( "Crossword Generator 2K17" );

		changeScene( ApplicationState.DICTIONARY_LOADER );
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

	private void initGeneratingView() throws java.io.IOException {
		FXMLLoader generating = new FXMLLoader( getClass().getResource( "../view/GeneratingView.fxml" ) );
		generatingScene = new Scene( generating.load() );

		generatingViewController = generating.getController();
		generatingViewController.init( this );

		generatingStage = new Stage( );
		generatingStage.setScene( generatingScene );
		generatingStage.sizeToScene();
	}

	public void changeScene( ApplicationState applicationState ){
		switch( applicationState){
			case IMAGE_VIEWER:
				generatingStage.hide();
				mainStage.setScene( viewerScene );
				mainStage.sizeToScene();
				mainStage.show();
				break;
			case GENERATING:
				mainStage.hide();
				//TODO: implement a better generating screen.
				//generatingStage.show();
				//generatingViewController.show();
				break;
			case DICTIONARY_LOADER:default:
				generatingStage.hide();
				mainStage.setScene( dictionaryScene );
				mainStage.show();
				mainStage.sizeToScene();
				break;
		}
	}

	public void generateCrossword( DictionaryFile dictionaryFile ){
		this.currentDictionaryFile = dictionaryFile;

		changeScene( ApplicationState.GENERATING );

		puzzleGrid = new Grid( 100, 100, dictionaryFile );
		crosswordPuzzleImageController.createPuzzleImage( puzzleGrid, 50 );

		crosswordViewController.loadImage( crosswordPuzzleImageController );

		changeScene( ApplicationState.IMAGE_VIEWER );
	}

	public void regenerateCrossword(){
		currentDictionaryFile.reset();

		changeScene( ApplicationState.GENERATING );

		puzzleGrid = new Grid( 100, 100, currentDictionaryFile );
		crosswordPuzzleImageController.createPuzzleImage( puzzleGrid, 50 );

		crosswordViewController.loadImage( crosswordPuzzleImageController );

		changeScene( ApplicationState.IMAGE_VIEWER );
	}

	@Override
	public void stop( ) throws Exception {
		if(crosswordPuzzleImageController != null) {
			crosswordPuzzleImageController.cleanup();
		}

		super.stop( );
	}
}
