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


public class FXMLController {
	
	private static DecimalFormat df = new DecimalFormat("0.0");
	private String code="Ici votre code";
	private String fichiercode="";
	private String cordo="";

    @FXML
    private Slider speed;
    
    @FXML
    private Menu ttsp;
    
    @FXML
    private TextArea codetext;
    
    @FXML
    private TextField speedtext;
    
    
    @FXML
    public ChoiceBox<String> choiceordo;

    public void initialize() {
    	codetext.setText(code);
    	choiceordo.getItems().addAll("random","pas random","n1");
    	
    }
    public void speedtex() {
    	speed.setValue(Double.valueOf(speedtext.getText()) );
    }
    public void slidert() {
    	Double s= speed.getValue();
    	String s2= df.format(s);
    	System.out.print(s+"\n");
    	speedtext.setText("Speed:"+s2);
    }
    public void savefile() {
    	System.out.print("test save\n");
    	code=codetext.getText();
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
    	cordo=choiceordo.getValue();
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
			codetext.setText(code);
		}
		else {
			System.out.print("cancel"+"\n");
		}
		
    }
}
