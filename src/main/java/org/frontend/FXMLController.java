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
import java.util.Arrays;
import java.util.List;

import org.backend.BackEndException;
import org.backend.BadSourceCodeException;
import org.backend.Infos;
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
	private Slider sliderSpeed;

	@FXML
	private TextArea textAreaOriginalCode;
	
	@FXML
	private TextArea lineProc;

	@FXML
	private TextField textFieldSpeed; 

	@FXML
	private TextField textFieldNumberOfProcessesRandom;

	@FXML
	private TextField textFieldNumberOfSteps;

	private SimulationBuilder simulationBuilder;
	private Simulation simulation;
	private Infos infos;



	private static DecimalFormat df = new DecimalFormat("0.0");
	private String code="Ici votre code";
	private String fichiercode="";
	private String cordo="";
	private String listProc="";
	private int nbrofprocess;
	private int [] processline;





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
	}

	
	public void controllerDoSteps() throws BadSourceCodeException{
		int count = Integer.parseInt(textFieldNumberOfSteps.getText());
		while (!infos.simulationIsDone() && count>0) {
			count -= 1;
			simulation.nextStep();
			updateProcess(infos.getIdOfLastExecutedProcess(),processline[infos.getIdOfLastExecutedProcess()]+1);
			System.out.println(infos.getIdOfLastExecutedProcess());
			System.out.println(infos.getSharedVariables()[1].getName());
		}
		updateSharedVariables();
	}
	
	public void controllerPlusStep() throws BadSourceCodeException{
		if (!infos.simulationIsDone()) {
			simulation.nextStep();
			System.out.println(infos.getIdOfLastExecutedProcess());
			updateProcess(infos.getIdOfLastExecutedProcess(),processline[infos.getIdOfLastExecutedProcess()]+1);
			updateSharedVariables();
		}
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
	
	public void initalizeProcess(int nbrp){
		nbrofprocess=nbrp;
		processline= new int[nbrp];
		Arrays.fill(processline, 0);
		updateProcess(0,0);
	}
	
	
	private static int countLines(String str){
		   String[] lines = str.split("\r\n|\r|\n");
		   return  lines.length;
	}
	
	public void updateProcess(int nump,int linep){
		listProc="";
		processline[nump]=linep;
		for (int l = 0; l < countLines(code) ; l++) {
			for (int i = 0; i < nbrofprocess; i++) {
				if (l==processline[i]) {
					listProc=listProc+"P"+Integer.toString(i)+",";
				}
			}
			listProc=listProc+"\n";
		}
		lineProc.setText(listProc);
	}
	
}
