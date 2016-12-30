package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UTFDataFormatException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {

	private final String HOST = "http://localhost:8080/";
	private final Charset charset = Charset.forName("UTF-8");
	private final TextField textfield = new TextField();
	private final TextField descriptionfield = new TextField();
	private final TextArea textarea = new TextArea();
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client");
        
        Button btnPut = new Button("Film hinzufuegen");
        btnPut.setOnAction(f->{
        	JSONObject json = new JSONObject();
        	json.put("content", descriptionfield.getText());
        	String response = sendRequestWithPayload("filme/"+textfield.getText(), "PUT", json.toString());
        	textarea.clear();
        	textarea.setText(response);
        });
        Button btnDelete = new Button("Film loeschen");
        btnDelete.setOnAction(f->{
        	String response = sendSimpleRequest("filme/"+textfield.getText(), "DELETE");
        	textarea.clear();
        	textarea.setText(response);
        });
        Button btnGetAll = new Button("alle Filme auslesen");
        btnGetAll.setOnAction(f->{
        	String response = sendSimpleRequest("filmliste", "GET");
        	textarea.clear();
        	textarea.setText(response);
        });
        
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(textfield, descriptionfield, btnPut, btnDelete, btnGetAll, textarea);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
	
    private String sendSimpleRequest(String res, String method){
    	
    	try {
			URL url = new URL(HOST+res);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			StringBuffer sb = new StringBuffer();
			
			if (con != null && con.getInputStream() != null) {
				try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in)){
					String line = null;
					while((line=bufferedReader.readLine()) != null){
						sb.append(line);
						sb.append("/n");
					}
				}
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
    }
    
    private String sendRequestWithPayload(String res, String method, String payload){
    	
    	try {
			URL url = new URL(HOST+res);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setDoOutput(true);
			con.setRequestProperty("Accept-Charset", charset.name());
			con.setRequestProperty("Content-Type", "application/json;charset=" + charset.name());
			
			if(con != null && con.getOutputStream() != null){
				try(OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), charset)){
					out.write(payload);
					out.flush();
				}
			}
			
			StringBuffer sb = new StringBuffer();
			if (con != null && con.getInputStream() != null) {
				try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in)){
					String line = null;
					while((line=bufferedReader.readLine()) != null){
						sb.append(line);
						sb.append("/n");
					}
				}
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
    }
}

