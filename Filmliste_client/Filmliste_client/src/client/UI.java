package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

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
        	String response = sendRequest("filme/"+textfield.getText(), "PUT");
        	textarea.clear();
        	textarea.setText(response);
        });
        Button btnDelete = new Button("Film loeschen");
        btnDelete.setOnAction(f->{
        	String response = sendRequest("filme/"+textfield.getText(), "DELETE");
        	textarea.clear();
        	textarea.setText(response);
        });
        Button btnGetAll = new Button("alle Filme auslesen");
        btnGetAll.setOnAction(f->{
        	String response = sendRequest("filmliste", "GET");
        	textarea.clear();
        	textarea.setText(response);
        });
        
        StackPane root = new StackPane();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(textfield, btnPut, btnDelete, btnGetAll, textarea);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
	
    private String sendRequest(String res, String method){
    	
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
}

/*
 * public class CrunchifyCallUrlAndGetResponse {
 
	public static void main(String[] args) {
		System.out.println("\nOutput: \n" + callURL("http://cdn.crunchify.com/wp-content/uploads/code/json.sample.txt"));
	}
 
	public static String callURL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
}
*/
