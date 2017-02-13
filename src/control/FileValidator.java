package control;

import java.io.File;

/**
 * control.FileValidator.java - DESCRIPTION
 *
 * @author - Andrew M.
 * @version - 04/Feb/2017
 */
//TODO: Fix Comments
public class FileValidator {
	public static boolean valid( File file ){
		boolean validFile = true;

		if( !file.getAbsoluteFile().toString().endsWith( ".txt" )){
			validFile = false;
		}

		return validFile;
	}

	public static boolean valid( String path ){
		boolean validFile = true;

		if( !path.endsWith( ".txt" )){
			validFile = false;
		}

		return validFile;
	}
}
