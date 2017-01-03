package client;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileChooserWrapper {
	private final FileChooser chooser = new FileChooser();
	private final Stage stage;
	
	public FileChooserWrapper(Stage s){
		stage = s;
		chooser.setTitle("Choose picture");
		chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
	}
	
	public File choosePicture(){
		File f = chooser.showOpenDialog(stage);
		if(f != null && f.exists()){
			return f;
		}
		else{
			return null;
		}
	}
}
