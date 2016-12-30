package client;

import java.util.LinkedList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UI extends Application {
	
	private Controller controller;
	private final FlowPane flowpane = new FlowPane();
	private final Accordion accordion = new Accordion ();
	private final LinkedList<TitledPane> tps = new LinkedList<>();
	
	public static void main(String[] args) {
        launch(args);
    }
	
	/**
	 * http://docs.oracle.com/javafx/2/ui_controls/accordion-titledpane.htm
	 */
	@Override
    public void start(Stage primaryStage) {
		controller = new Controller();
		
        primaryStage.setTitle("Filmliste");
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        
        for(Tag t : Tag.getAllTags()){
			CheckBox c = new CheckBox(t.getTag());
			c.setSelected(false);
			c.setOnAction(f -> refreshFilter());
			flowpane.getChildren().add(c);
		}
        
        for (Film f : controller.getFilme()) {           
            Label l = new Label(f.getContent());
            tps.add(new TitledPane(f.getName(), l)); 
        }   
        accordion.getPanes().addAll(tps);
        
        vbox.getChildren().addAll(flowpane, accordion);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
	
	private void refreshFilter(){
		
	}

	
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Client");
//        
//        Button btnPut = new Button("Film hinzufuegen");
//        btnPut.setOnAction(f->{
//        	JSONObject json = new JSONObject();
//        	json.put("content", descriptionfield.getText());
//        	String response = HttpHandler.sendRequestWithPayload("filme/"+textfield.getText(), "PUT", json.toString());
//        	textarea.clear();
//        	textarea.setText(response);
//        });
//        Button btnDelete = new Button("Film loeschen");
//        btnDelete.setOnAction(f->{
//        	String response = HttpHandler.sendSimpleRequest("filme/"+textfield.getText(), "DELETE");
//        	textarea.clear();
//        	textarea.setText(response);
//        });
//        Button btnGetAll = new Button("alle Filme auslesen");
//        btnGetAll.setOnAction(f->{
//        	String response = HttpHandler.sendSimpleRequest("filmliste", "GET");
//        	textarea.clear();
//        	textarea.setText(response);
//        });
//        
//        StackPane root = new StackPane();
//        VBox vbox = new VBox();
//        vbox.getChildren().addAll(textfield, descriptionfield, btnPut, btnDelete, btnGetAll, textarea);
//        root.getChildren().add(vbox);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();
//    }
	
   
}

