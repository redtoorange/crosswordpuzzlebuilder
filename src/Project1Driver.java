import control.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Project1Driver.java - Driver for the project 1.
 *
 * @author - Andrew M.
 * @version - 16/Feb/2017
 */
public class Project1Driver extends Application {
	/**
	 * launch() must always be called from main inside of an Application class for JavaFX to work.
	 *
	 * @param args {@link String} array of arguments passed via the commandline.
	 */
	public static void main( String[] args ) {
		launch( args );
	}

	/**
	 * Create an instance of the actual {@link ApplicationController} and hand it the {@link Stage}.
	 *
	 * @param primaryStage {@link Stage} passed in by the system.
	 * @throws Exception If there is an error creating a window.
	 */
	@Override
	public void start( Stage primaryStage ) throws Exception {
		new ApplicationController( ).start( primaryStage );
	}
}
