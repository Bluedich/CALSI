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
    
    ObservableList<String> content1 = FXCollections.observableArrayList(
    		"e", "d");
    ObservableList<String> content2 = FXCollections.observableArrayList(
    		"3", "7");
    ObservableList<String> content3 = FXCollections.observableArrayList(
    		"a", "b", "c", "d");
    ObservableList<String> content4 = FXCollections.observableArrayList(
    		"1", "2" , "3", "4");
    
    public ListView<String> listView1;
    public ListView<String> listView2;
    public ListView<String> listView3;
    public ListView<String> listView4;
   
    public ChoiceBox<String> choiceBoxLocalVariables;
    public ChoiceBox<String> choiceBoxScheduling;
    
    public void initialize() {
    	choiceBoxLocalVariables.getItems().addAll("P1", "P2", "P3", "P4");
    	choiceBox.setValue("P4");
    	choiceBox2.getItems().addAll("Step-by-step", "Random" , "With File");
    	choiceBox2.setValue("Step-by-step");
    	listView1.setItems(content1);
    	listView2.setItems(content2);
    	listView3.setItems(content3);
    	listView4.setItems(content4);
    	
    }

    public void onStartClicked(){
    	rightStatus.setText("Running...");
    }
        
}
