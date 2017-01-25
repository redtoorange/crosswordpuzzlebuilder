import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 25/Jan/2017
 */
public class DictionaryFile {
	File file;
	public DictionaryFile( String pathname ) {
		this.file = new File( pathname );
		parseFile();
	}

  	private void parseFile(){
		try {
			Scanner scanner = new Scanner( new FileInputStream( file ) );
		}
		catch(Exception e){
			System.out.println( "Error parsing the file." );
			e.printStackTrace();
		}
	}


}
