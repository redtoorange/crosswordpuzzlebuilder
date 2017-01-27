import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 27/Jan/2017
 */
public class DictionaryLoaderController {
	@FXML private Button generateButton;
	@FXML private Button fileChooserButton;
	@FXML private TextField filePathText;

	@FXML public void generateButtonClicked( ActionEvent event){
		System.out.println( "generateButtonClicked" );
	}

	@FXML public void fileChooserButtonClicked( ActionEvent event){
		System.out.println( "fileChooserButtonClicked");
	}

	@FXML public void filePathTextEntered( ActionEvent event ){
		System.out.println( "filedPathTextEntered" );
	}
}
