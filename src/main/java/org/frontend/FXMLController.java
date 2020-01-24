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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.text.*;

import org.backend.BackEndException;
import org.backend.BadSourceCodeException;
import org.backend.Infos;
import org.backend.RipException;
import org.backend.Simulation;
import org.backend.SimulationBuilder;
import org.backend.VariableInfo;

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
import javafx.scene.text.TextFlow;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author renon
 *
 */
/**
 * @author renon
 *
 */
public class FXMLController {

	ObservableList<String> content1 = FXCollections.observableArrayList(
			"e", "d","f","g","h","i","j","k","l");
	ObservableList<String> content2 = FXCollections.observableArrayList(
			"3", "7");
	ObservableList<String> content3 = FXCollections.observableArrayList(
			"a", "b", "c", "d");
	ObservableList<String> content4 = FXCollections.observableArrayList(
			"1", "2" , "3", "4");


	@FXML
	private Button buttonStart;
	@FXML
	private Button buttonStop;
	@FXML
	private Button buttonNewExecution;
	@FXML
	private Button buttonDoSteps;
	@FXML
	private Button buttonPlusStep;
	@FXML
	private Button buttonMinusStep;
	@FXML
	private Button buttonProcessCrash;
	@FXML
	private ListView<String> listView1;
	@FXML
	private ListView<String> listView2;
	@FXML
	private ListView<String> listView3;
	@FXML
	private ListView<String> listView4;

	@FXML
	private ChoiceBox<String> choiceBoxLocalVariables;

	@FXML
	private ChoiceBox<String> choiceBoxScheduling;
	
	@FXML
	private ChoiceBox<String> choiceBoxProcessToCrash;

	@FXML
	private Slider sliderSpeed;

	@FXML
	private TextArea textAreaOriginalCode;
	
	@FXML
	private TextFlow lineProc;

	@FXML
	private TextField textFieldSpeed; 

	@FXML
	private TextField textFieldNumberOfProcessesRandom;

	@FXML
	private TextField textFieldNumberOfSteps;
	
	private SimulationBuilder simulationBuilder;
	private Simulation simulation;
	private Infos infos;


	boolean auto = false;
	private static DecimalFormat df = new DecimalFormat("0.0");
	private String code="Ici votre code";
	private String fichiercode="";
	private String cordo="";
	private String listProc="";
	private int numberOfProcesses;
	private int [] processline;
	

	



	public void initialize() {

		choiceBoxLocalVariables.getSelectionModel().selectedItemProperty()
	    .addListener((obs, oldV, newV) -> 
	    updateLocalVariables());
		
		choiceBoxScheduling.getItems().addAll("Step-by-step", "Random" , "With File");
		choiceBoxScheduling.setValue("Step-by-step");
		listView1.setItems(content1);
		listView2.setItems(content2);
		listView3.setItems(content3);
		listView4.setItems(content4);
		textAreaOriginalCode.setText(code);


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
	public void saveFile() {
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
		cordo=choiceBoxScheduling.getValue();
		System.out.print(cordo+"\n");    	
	}
	
	public void openFile() {
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

	public void newExecution() throws BackEndException {
		simulationBuilder = new SimulationBuilder();
		File sourceFile = new File("/tests/source.txt");			
		code=textAreaOriginalCode.getText();
		try (FileWriter fw = new FileWriter("tests/source.txt")){
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(code);
			bw.flush();
			bw.close();
			System.out.print("saved\n");
		}catch (IOException e) {
			e.printStackTrace();
		}	
		
		simulation = simulationBuilder
				.withSourceCodeFromFile(fichiercode)
				.withNumberOfProcesses(Integer.parseInt(textFieldNumberOfProcessesRandom.getText()))
				.withScheduler("random")
				.build();
		infos = simulation.getInfos();
		System.out.print(infos.simulationIsDone());
		initalizeProcess(Integer.parseInt(textFieldNumberOfProcessesRandom.getText()));
		updateChoiceBoxLocalVariables();
		updateChoiceBoxProcessToCrash();
		
	}

	
	public void controllerDoSteps() throws BackEndException{
		int count = Integer.parseInt(textFieldNumberOfSteps.getText());
		while (!infos.simulationIsDone() && count>0) {
			count -= 1;
			controllerPlusStep();
		}
	}
	
	public void controllerPlusStep() throws BackEndException{
		try {
		if (!infos.simulationIsDone()) {
			simulation.nextStep();
			System.out.println(infos.getIdOfLastExecutedProcess());
			ArrayList<Integer> arrayExec = infos.getOriginalSourceLinesExecutedDuringLastStep(infos.getIdOfLastExecutedProcess());
			updateProcess(infos.getIdOfLastExecutedProcess(),arrayExec.get(0));
			updateSharedVariables();
			updateLocalVariables();
		}
	    } catch (Exception e) {
	        System.out.println(e);
	      }
	}
	
	public void updateChoiceBoxLocalVariables() {
		choiceBoxLocalVariables.getItems().clear();
		for (int i = 0; i < numberOfProcesses; i++) {
			choiceBoxLocalVariables.getItems().add("P"+ Integer.toString(i));
		}
		choiceBoxLocalVariables.setValue("P0");
	}
	
	public void updateChoiceBoxProcessToCrash() {
		choiceBoxProcessToCrash.getItems().clear();
		for (int i = 0; i < numberOfProcesses; i++) {
			choiceBoxProcessToCrash.getItems().add("P"+ Integer.toString(i));
		}
		choiceBoxProcessToCrash.setValue("P0");
	}

	public void updateSharedVariables() {
		content3.remove(0, content3.size());
		content4.remove(0, content4.size());
		VariableInfo[] variableInfo = infos.getSharedVariables();
		for(int i=0;i<variableInfo.length;i++)
		{
			if(variableInfo[i] == null)
			{		  
				break;
			}
			else {
				content3.addAll(variableInfo[i].getName());
				content4.addAll(variableInfo[i].getValue());
			}
		}
	}
	
	
	
	public void updateLocalVariables() {
		content1.remove(0, content1.size());
		content2.remove(0, content2.size());
		String currentProcess = choiceBoxLocalVariables.getSelectionModel().getSelectedItem();
		int currentProcessId = Character.getNumericValue(currentProcess.charAt(1));
		System.out.println("chosen process " + Integer.toString(currentProcessId));
		VariableInfo[] variableInfo = infos.getLocalVariables(currentProcessId);
		for(int i=0;i<variableInfo.length;i++)
		{
			if(variableInfo[i] == null)
			{		  
				break;
			}
			else {
				content1.addAll(variableInfo[i].getName());
				content2.addAll(variableInfo[i].getValue());
			}
		}
	}
	
	
	
	public void initalizeProcess(int nbrp) throws RipException{
		numberOfProcesses=nbrp;
		processline= new int[nbrp];
		Arrays.fill(processline, 0);
		updateProcess(0,0);
	}
	
	
	private static int countLines(String str){
		   String[] lines = str.split("\r\n|\r|\n");
		   return  lines.length;
	}
	
	public void updateProcess(int nump,int linep) throws RipException{
        lineProc.getChildren().clear();
		listProc="";
		processline[nump]=linep;

		for (int l = 0; l < countLines(code) ; l++) {
			for (int i = 0; i < numberOfProcesses; i++) {
				if (l==processline[i]) {
					Text textForProcess = new Text("P"+Integer.toString(i)+","); 
					textForProcess.setFont(Font.font("System", 12));
					textForProcess.setStyle("-fx-font-weight: regular");
					textForProcess.setFill(Color.BLACK);
					if(infos.processIsDone(i)) {
						textForProcess.setFill(Color.BLUE);
					}
					if(infos.processIsCrashed(i)) {
						textForProcess.setFill(Color.RED);
					}
					if(i==nump) {
						textForProcess.setStyle("-fx-font-weight: bold");
					}
					lineProc.getChildren().add(textForProcess);
				}
			}
			Text textForProcess = new Text("\n"); 
			lineProc.getChildren().add(textForProcess);
		}
	}
	
	public void onClickedCrashProcess() throws RipException {
		String currentProcess = choiceBoxProcessToCrash.getSelectionModel().getSelectedItem();
		int currentProcessId = Character.getNumericValue(currentProcess.charAt(1));
		simulation.crashProcess(currentProcessId);
		choiceBoxProcessToCrash.getItems().remove(currentProcess);
		System.out.println(currentProcess + " crashed");
		
	}
	
}
