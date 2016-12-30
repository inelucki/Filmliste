package client;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {
	
	private Controller controller;
	private final FlowPane flowpane = new FlowPane();
	private final Accordion accordion = new Accordion ();
	
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
        
        HBox hboxtags = new HBox();
        TextField textTags = new TextField();
        Button btnTags = new Button("Create Tag");
        btnTags.setOnAction(f -> createTag(textTags.getText().trim()));
        Button btnDltTag = new Button("Delete Tag");
        btnDltTag.setOnAction(f -> deleteTag(textTags.getText().trim()));
        hboxtags.getChildren().addAll(textTags, btnTags, btnDltTag);
        
        HBox hboxfilm = new HBox();
        TextField textFilm = new TextField();
        Button btnFilm = new Button("Add Film");
        btnFilm.setOnAction(f -> createFilm(textFilm.getText().trim()));
        hboxfilm.getChildren().addAll(textFilm, btnFilm);
        
        for (Film f : controller.getFilme()) {
        	TextArea ta = new TextArea();
        	ta.setText(f.getContent());
        	TitledPane p = new TitledPane();
        	
        	Label lblTags = generateFilmTagLabel(f.getTags());
        	TextField txtFilmTag = new TextField();
        	Button btnAddTagToFilm = new Button("Add Tag");
        	btnAddTagToFilm.setOnAction(k -> addTagToFilm(f, txtFilmTag.getText().trim(), lblTags));
        	Button btnDeleteTagFromFilm = new Button("Delete Tag");
        	btnDeleteTagFromFilm.setOnAction(k -> removeTagFromFilm(f, txtFilmTag.getText().trim(), lblTags));
        	HBox filmTagControl = new HBox();
        	filmTagControl.getChildren().addAll(txtFilmTag, btnAddTagToFilm, btnDeleteTagFromFilm);
        	
        	Button btnUpdate = new Button("Update");
        	btnUpdate.setOnAction(k -> updateFilm(f, ta));
        	Button btnDelete = new Button("Delete");
        	btnDelete.setOnAction(k -> deleteFilm(f, p));
        	HBox filmControl = new HBox();
        	filmControl.getChildren().addAll(btnUpdate, btnDelete);
        	
        	VBox vbxFilm = new VBox();
        	vbxFilm.getChildren().addAll(lblTags, filmTagControl, ta, filmControl);
        	
        	p.setContent(vbxFilm);
        	p.setText(f.getName());
        	accordion.getPanes().add(p);
        }   
        
        vbox.getChildren().addAll(flowpane, hboxtags, hboxfilm, accordion);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 600, 500));
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
	
	private Label generateFilmTagLabel(List<Tag> tags){
		StringBuffer bf = new StringBuffer("Tags : ");
		for(Tag t : tags){
			bf.append(t.getTag()+", ");
		}
		return new Label(bf.toString());
	}
	
	private void refreshFilter(){
		
	}
	
	private void createFilm(String str){
		
	}
	
	private void updateFilm(Film f, TextArea t){
		
	}
	
	private void deleteFilm(Film f, TitledPane p){
		
	}
	
	private void createTag(String str){
		
	}
	
	private void deleteTag(String str){
		
	}
	
	private void addTagToFilm(Film f, String t, Label l){
		
	}
	
	private void removeTagFromFilm(Film f, String t, Label l){
		
	}
	
	private void showDialog(String msg){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
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

