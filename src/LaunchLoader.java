/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 27/Jan/2017
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LaunchLoader extends Application {

	public static void main( String[] args ) {
		launch( args );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		Pane root = FXMLLoader.load( getClass().getResource( "DictionaryLoader.fxml" ) );

		Scene scene = new Scene( root, 400, 200 );


		primaryStage.setScene( scene );

		primaryStage.setWidth( 400 );
		primaryStage.setHeight( 100 );

		primaryStage.setTitle( "Crossword Generator 2K17" );

		primaryStage.show();
		primaryStage.setResizable( false );
	}
}
