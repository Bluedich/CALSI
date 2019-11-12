package org.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class FXMLController {

    @FXML
    public Label rightStatus;
    public Button startButton;
    
    ObservableList<String> content = FXCollections.observableArrayList(
    		"e = 3", "d = 7");
    ObservableList<String> content2 = FXCollections.observableArrayList(
    		"a = 1", "b = 2" , "c = 3", "d = 4");
    
    public ListView<String> listView = new ListView<String>(content);
    public ListView<String> listView2 = new ListView<String>(content2);
   
    public ChoiceBox<String> choiceBox = new ChoiceBox<String>();
    public ChoiceBox<String> choiceBox2 = new ChoiceBox<String>();
    
    public void initialize() {
    	choiceBox.getItems().addAll("P1", "P2", "P3", "P4");
    	choiceBox.setValue("P4");
    	choiceBox2.getItems().addAll("Step-by-step", "Random" , "With File");
    	choiceBox2.setValue("Step-by-step");
    	listView.setItems(content);
    	listView2.setItems(content2);
    	
    }

    public void onStartClicked(){
    	rightStatus.setText("Running...");
    }
        
}
