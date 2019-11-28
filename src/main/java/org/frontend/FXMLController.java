package org.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import javafx.stage.FileChooser;
import javafx.scene.control.MenuItem;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.util.List;
import javafx.scene.control.TextArea;
import java.io.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;



public class FXMLController {

    @FXML
    
    private Button buttonStart = new Button();
    private Button buttonStop = new Button();
    private Button buttonNewExecution = new Button();
    private Button buttonDoSteps = new Button();
    private Button buttonPlusStep = new Button();
    private Button buttonMinusStep = new Button();
    
    private ListView<String> listView1 = new ListView<String>();
    private ListView<String> listView2 = new ListView<String>();
    private ListView<String> listView3 = new ListView<String>();
    private ListView<String> listView4 = new ListView<String>();
   
    private ChoiceBox<String> choiceBoxLocalVariables = new ChoiceBox<String>();
    private ChoiceBox<String> choiceBoxScheduling= new ChoiceBox<String>();
    
    
    
    ObservableList<String> content1 = FXCollections.observableArrayList(
    		"e", "d");
    ObservableList<String> content2 = FXCollections.observableArrayList(
    		"3", "7");
    ObservableList<String> content3 = FXCollections.observableArrayList(
    		"a", "b", "c", "d");
    ObservableList<String> content4 = FXCollections.observableArrayList(
    		"1", "2" , "3", "4");
    
	
	private static DecimalFormat df = new DecimalFormat("0.0");
	private String code="Ici votre code";
	private String fichiercode="";
	private String cordo="";

    @FXML
    private Slider sliderSpeed;
    
    
    @FXML
    private TextArea textAreaOriginalCode;
    
    @FXML
    private TextField textFieldSpeed;
    
    
    @FXML
    public ChoiceBox<String> choixeBoxScheduling;

    public void initialize() {
    	choiceBoxLocalVariables.getItems().addAll("P1", "P2", "P3", "P4");
    	choiceBoxLocalVariables.setValue("P4");
    	choiceBoxScheduling.getItems().addAll("Step-by-step", "Random" , "With File");
    	choiceBoxScheduling.setValue("Step-by-step");
    	listView1.setItems(content1);
    	listView2.setItems(content2);
    	listView3.setItems(content3);
    	listView4.setItems(content4);
    	textAreaOriginalCode.setText(code);
    	choixeBoxScheduling.getItems().addAll("random","pas random","n1");
    	
    }
    public void speedtex() {
    	sliderSpeed.setValue(Double.valueOf(textFieldSpeed.getText()) );
    }
    public void slidert() {
    	Double s= sliderSpeed.getValue();
    	String s2= df.format(s);
    	System.out.print(s+"\n");
    	textFieldSpeed.setText(""+s2);
    }
    public void savefile() {
    	System.out.print("test save\n");
    	code=textAreaOriginalCode.getText();
    	try (FileWriter fw = new FileWriter(fichiercode)){
    	BufferedWriter bw = new BufferedWriter(fw);
    	bw.write(code);
    	bw.flush();
    	bw.close();
    	System.out.print("saved\n");
    	}catch (IOException e) {
	        e.printStackTrace();
	    }
    	
    }
    public void choiceordon() {
    	cordo=choixeBoxScheduling.getValue();
    	System.out.print(cordo+"\n");    	
    }
    public void btnfile() {
    	System.out.print("test"+"\n");
    	
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			fichiercode= selectedFile.getAbsolutePath();
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(fichiercode)))) {

		        String line;
		        code="";
		        while ((line = reader.readLine()) != null)
		        	code=code+line+"\n";
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			textAreaOriginalCode.setText(code);
		}
		else {
			System.out.print("cancel"+"\n");
		}
		
    }
}
