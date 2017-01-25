import javax.swing.*;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class FileTextField extends JTextField {
	private static final String INVALID_STRING = "INVALID FILETYPE";
	private static final String REQUIED_EXTENSION = ".txt";

	private boolean validFileName = false;

	public FileTextField(int width){
		super("", width);
	}

	public void setText( String text ){
		if( !text.isEmpty() ){
			if( text.endsWith( REQUIED_EXTENSION ) ){
				super.setText( text );
				validFileName = true;
			}
			else {
				super.setText( INVALID_STRING );
				validFileName = false;
			}
		}
		else{
			super.setText( "No File Selected" );
			validFileName = false;
		}
	}

	public boolean getValidFileName(){
		return validFileName;
	}
}
