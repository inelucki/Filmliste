package client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Node;
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
	
	private final String defaultInfo = "Es gab einen Fehler aber keine Meldung dazu. Ganz doll...";
	private Controller controller;
	private final FlowPane flowpane = new FlowPane();
	private final Accordion accordion = new Accordion ();
	private HashMap<Film,TitledPane> pns = new HashMap<>();
	
	public static void main(String[] args) {
        launch(args);
    }
	
	/**
	 * http://docs.oracle.com/javafx/2/ui_controls/accordion-titledpane.htm
	 */
	@Override
    public void start(Stage primaryStage) {
		controller = new Controller();
		ResponseObject resp = controller.init();
		if(!resp.isOk()){
			showDialog("Das Programm wird beendet. Initialisierung mit folgendem Fehler abgebrochen : "+resp.getInfoMessage());
			return;
		}
		
        primaryStage.setTitle("Filmliste");
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        
        for(Tag t : controller.getTags()){
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
        	generateTitledPane(f);
        }   
        
        vbox.getChildren().addAll(flowpane, hboxtags, hboxfilm, accordion);
        refreshFilter();
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 600, 500));
        //primaryStage.setMaximized(true);
        primaryStage.show();
    }
	
	private void generateTitledPane(Film f){
		TextArea ta = new TextArea();
    	ta.setText(f.getContent());
    	
    	Label lblTags = new Label(generateFilmTagLabelContent(f.getTags()));
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
    	btnDelete.setOnAction(k -> deleteFilm(f));
    	HBox filmControl = new HBox();
    	filmControl.getChildren().addAll(btnUpdate, btnDelete);
    	
    	VBox vbxFilm = new VBox();
    	vbxFilm.getChildren().addAll(lblTags, filmTagControl, ta, filmControl);
    	
    	TitledPane p = new TitledPane();
    	p.setContent(vbxFilm);
    	p.setText(f.getName());
    	pns.put(f, p);
	}
	
	private String generateFilmTagLabelContent(List<Tag> tags){
		StringBuffer bf = new StringBuffer("Tags : ");
		for(Tag t : tags){
			bf.append(t.getTag()+", ");
		}
		return bf.toString();
	}
	
	private void refreshFilter(){
		LinkedList<Tag> selected = new LinkedList<>();
		flowpane.getChildren().stream().forEach(f ->{
			if(f instanceof CheckBox){
				CheckBox box = (CheckBox) f;
				if(box.isSelected()){
					selected.add(controller.getTag(box.getText()));
				}
			}
		});
		accordion.getPanes().clear();
		
		if(selected.isEmpty()){
			accordion.getPanes().addAll(pns.values());
		}
		else{
			for(Entry<Film, TitledPane> elem : pns.entrySet()){
				boolean check = true;
				for(Tag t : selected){
					if(!elem.getKey().hasTag(t)){
						check = false;
						break;
					}
				}
				if(check){
					accordion.getPanes().add(elem.getValue());
				}
			}
		}
	}
	
	private void createFilm(String str){
		ResponseObject resp = controller.createFilm(str);
		if(resp.isOk() && resp.getFilm() != null){
			generateTitledPane(resp.getFilm());
			refreshFilter();
		}
		else{
			showDialog(resp.getInfoMessage());
		}
	}
	
	private void updateFilm(Film f, TextArea t){
		if(t.getText().equals(f.getContent())){
			showDialog("Kein Update, da selbe Beschreibung");
		}
		else{
			ResponseObject resp = controller.updateFilm(f, t.getText());
			if(!resp.isOk()){
				showDialog(resp.getInfoMessage());
			}
		}
	}
	
	private void deleteFilm(Film f){
		ResponseObject resp = controller.deleteFilm(f);
		if(resp.isOk()){
			pns.remove(f);
			refreshFilter();
		}
		else{
			showDialog(resp.getInfoMessage());
		}
	}
	
	private void createTag(String str){
		ResponseObject resp = controller.createTag(str);
		if(resp.isOk()){
			CheckBox c = new CheckBox(controller.getTag(str).getTag());
			c.setSelected(false);
			c.setOnAction(f -> refreshFilter());
			flowpane.getChildren().add(c);
		}
		else{
			showDialog(resp.getInfoMessage());
		}
	}
	
	private void deleteTag(String str){
		ResponseObject resp = controller.deleteTag(str);
		if(resp.isOk()){
			for(Node n : flowpane.getChildren()){
				if(n instanceof CheckBox){
					if(controller.getTag(((CheckBox) n).getText()).equals(controller.getTag(str))){
						flowpane.getChildren().remove(n);
						break;
					}
				}
			}
			
			accordion.getPanes().clear();
			pns.clear();
			for(Film f : controller.getFilme()){
				generateTitledPane(f);
			}
			refreshFilter();
		}
		else{
			showDialog(resp.getInfoMessage());
		}
	}
	
	private void addTagToFilm(Film f, String t, Label l){
		if(controller.existTag(t)){
			ResponseObject resp = controller.addTagToFilm(f, t);
			if(resp.isOk()){
				l.setText(generateFilmTagLabelContent(f.getTags()));
				refreshFilter();
			}
			else{
				showDialog(resp.getInfoMessage());
			}
		}
		else{
			showDialog("Diesen Tag gibt es nicht!");
		}
	}
	
	private void removeTagFromFilm(Film f, String t, Label l){
		if(controller.existTag(t)){
			ResponseObject resp = controller.deleteTagFromFilm(f, t);
			if(resp.isOk()){
				l.setText(generateFilmTagLabelContent(f.getTags()));
				refreshFilter();
			}
			else{
				showDialog(resp.getInfoMessage());
			}
		}
		else{
			showDialog("Diesen Tag gibt es nicht!");
		}
	}
	
	private void showDialog(String msg){
		String info = msg != null ? msg : defaultInfo;
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(info);
		alert.showAndWait();
	}
}
