package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {

	private final TextField textfield = new TextField();
	private final TextArea textarea = new TextArea();
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client");
        
        Button btnPut = new Button("Film hinzufuegen");
        btnPut.setOnAction(f->{
        	
        });
        Button btnDelete = new Button("Film loeschen");
        btnDelete.setOnAction(f->{
        	
        });
        Button btnGetAll = new Button("alle Filme auslesen");
        btnGetAll.setOnAction(f->{
        	
        });
        
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(textfield, btnPut, btnDelete, btnGetAll, textarea);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
	
}
