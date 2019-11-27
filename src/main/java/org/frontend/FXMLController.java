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
    

    public void initialize() {
    	choiceBoxLocalVariables.getItems().addAll("P1", "P2", "P3", "P4");
    	choiceBoxLocalVariables.setValue("P4");
    	choiceBoxScheduling.getItems().addAll("Step-by-step", "Random" , "With File");
    	choiceBoxScheduling.setValue("Step-by-step");
    	listView1.setItems(content1);
    	listView2.setItems(content2);
    	listView3.setItems(content3);
    	listView4.setItems(content4);
    	
    }

        
}
