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
    
    private Button buttonStart;
    private Button buttonStop;
    private Button buttonNewExecution;
    private Button buttonDoSteps;
    private Button buttonPlusStep;
    private Button buttonMinusStep;
    
    private ListView<String> listView1;
    private ListView<String> listView2;
    private ListView<String> listView3;
    private ListView<String> listView4;
   
    private ChoiceBox<String> choiceBoxLocalVariables;
    private ChoiceBox<String> choiceBoxScheduling;
    
    
    
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
