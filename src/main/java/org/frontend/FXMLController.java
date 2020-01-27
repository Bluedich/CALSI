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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
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
	private TextArea textAreaParsedCode;
	
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
		textAreaParsedCode.setText(infos.getNewSourceCode());
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
		processline[nump]=linep;

		for (int l = 0; l < countLines(code) ; l++) {
			Text textForProcess2 = new Text(Integer.toString(l)+")"); 
			textForProcess2.setFont(Font.font("System", 18.9));
			textForProcess2.setStyle("-fx-font-weight: regular");
			textForProcess2.setFill(Color.BLACK);
			lineProc.getChildren().add(textForProcess2);
			for (int i = 0; i < numberOfProcesses; i++) {
				if (l==processline[i]) {
					Text textForProcess = new Text("P"+Integer.toString(i)+","); 
					textForProcess.setFont(Font.font("System", 18.9));
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
			textForProcess.setFont(Font.font("System", 18.9));
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
	
	public void startAuto() throws BackEndException, InterruptedException{
		auto = true;
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() { 

		    @Override
		    public void handle(ActionEvent event) {
		    	if (!infos.simulationIsDone() && auto) {
		    		System.out.println( "youpi");
		    	}
		    }
		}));
		timeline.setCycleCount(10000000);
		timeline.play();

	}
	
	public void stopAuto(){
		auto = false;		
	}
	
	
	public void help1(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("AtomicOperation");
        alert.setHeaderText(null);
        alert.setContentText("This is a list of operation that you can made** on shared variable. \r\n" + 
        		"This list is exhaustive of what our program can made and you cannot do any other atomic operation with shared variable.***\r\n" + 
        		"\r\n" + 
        		"	1) .read()			: Return the value of the shared register\r\n" + 
        		"	2) .write(x)		: Write x in the shared register\r\n" + 
        		"	\r\n" + 
        		"	3) .update(x)		: Write x in the shared array at the position i (i=index of the process)\r\n" + 
        		"	4) .snapshot()		: Return the value of the shared array\r\n" + 
        		"	\r\n" + 
        		"	5) .enqueue(x)		: Add the value x on queue of the shared queue\r\n" + 
        		"	6) .dequeue()		: Return and removed the value at the tail of the shared queue\r\n" + 
        		"\r\n" + 
        		"**	All those operations are not yet available. ");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	public void help2(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToLaunchYourSimulation");
        alert.setHeaderText(null);
        alert.setContentText("How To Launch Your Simulation.\r\n" + 
        		"\r\n" + 
        		"To launch the execution, first load (or write) your algorithm on the tab \"Original Code\". To load your algorithm, use the tab \"File->Open...\"\r\n" + 
        		"Make sure this file respect the rules describe in HowToWriteYourCode. \r\n" + 
        		"Then you have to choose your scheduler policy with \"Scheduling Policy\". You have the choice between \"Random, StepByStep and with File\".\r\n" + 
        		"Unless you choose \"With file\", you have then to specify the number of process in your simulation in \"Number of processes\" at the right of the Window. \r\n" + 
        		"\r\n" + 
        		"After those settings, you can launch the simulation clicking on \"New Execution\".\r\n" + 
        		"Once launch, you can use the button \"+step\" to execute one step. \r\n" + 
        		"The scheduler will then choose a process and execute a step, all the value of local and shared variables will be updated.\r\n" + 
        		"The button \"-step\" allow you to go back one step before.**\r\n" + 
        		"\r\n" + 
        		"At the end of the simulation you can then save the last execution, \"Scheduler->Save last execution\". To know more about the format of those file read HowToWriteYourScheduler\r\n" + 
        		"\r\n" + 
        		"** The feature \"-step\" is not yet implemented");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	public void help3(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToWriteYourCode");
        alert.setHeaderText(null);
        alert.setContentText("When you write your code you have to respect the following syntax, you can find an example in the file sampleCode.txt\r\n" + 
        		"Before your code, your text file must be compose of three compact blocks without any blank line.**\r\n" + 
        		"The different block must be separated from each other and from the code of your algorithm with a blank line.\r\n" + 
        		"	1)	Import are done in the beginning of the file, before the first block, without any blank line between the different import\r\n" + 
        		"	2)	The first block is there for the Shared Variable declaration, without any blank line between the different declaration\r\n" + 
        		"	3)	The second block is there for the Shared Variable initialization, without any blank line between the different initialization\r\n" + 
        		"	4)	The third block is there for Local Variable declaration, without any blank line between the different declaration\r\n" + 
        		"	5)	Then you can write the code of your concurrent algorithm*\r\n" + 
        		"** The initialization of the simulation may change in the future, and the usage of block may be outdated soon.***\r\n" + 
        		"*** This is still how you have to write your code for now.\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"\r\n" + 
        		"* When you write your code you have to respect the following rules:\r\n" + 
        		"	1)	The code of your algorithm should be written respecting the Java syntax\r\n" + 
        		"	2)	You only can made 1 atomic operation per line (without what your severals operation will be done atomicly, which may change the way your algorithm work)\r\n" + 
        		"	\r\n" + 
        		"What is important to notice is that all operation made in one line are done atomicly.\r\n" + 
        		"We know that the second rule is restrictive for the user and that it asks him to assure that every line don't contain more than 1 atomic operation.\r\n" + 
        		"That why this implementation should be changed in the future.");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	public void help4(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("HowToWriteYourScheduler");
        alert.setHeaderText(null);
        alert.setContentText("// Scheduler Syntax.**\r\n" + 
        		"When you want to use your own scheduler policy*, you have to specify at every step which process has the hand.\r\n" + 
        		"So if you want to load a scheduler you have to write a text file with the following syntax :\r\n" + 
        		"	1)	In the first line you write the number of process which are running you algorithm\r\n" + 
        		"	2)	At every line you write the ID of the process that has the hand\r\n" + 
        		"	3) 	To simulate a crash, use \"!\" before the number of the process.\r\n" + 
        		"\r\n" + 
        		"If at the end of the file all the processes have not finished the execution (or are crashed), a loop is made.\r\n" + 
        		"It is important to note nothing guaranteed that the simulation will finish. \r\n" + 
        		"\r\n" + 
        		"You can see an example of the scheduler format, in the text file sampleScheduler.txt\r\n" + 
        		"\r\n" + 
        		"To generate those files, you can at the end of the simulation save the last execution, \"Scheduler->Save last execution\".\r\n" + 
        		"	\r\n" + 
        		"* ie. not random\r\n" + 
        		"** Feature not implemented yet");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	public void help5(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("samleScheduler");
        alert.setHeaderText(null);
        alert.setContentText("5\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"2\r\n" + 
        		"1\r\n" + 
        		"5\r\n" + 
        		"4\r\n" + 
        		"4\r\n" + 
        		"3\r\n" + 
        		"2\r\n" + 
        		"1\r\n" + 
        		"3\r\n" + 
        		"5\r\n" + 
        		"4");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	public void help6(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("sampleCode");
        alert.setHeaderText(null);
        alert.setContentText("import java.lang.Math; 						//	All import have to be made without any blank line\r\n" + 
        		"\r\n" + 
        		"// // Shared variables declaration			//	All declaration have to be made without any blank line\r\n" + 
        		"Integer turn;\r\n" + 
        		"Boolean[] flag;\r\n" + 
        		"\r\n" + 
        		"// // Shared variables initialization		//	All initialization have to be made without any blank line\r\n" + 
        		"turn = new Integer(0);\r\n" + 
        		"flag = new Boolean[2];\r\n" + 
        		"flag[0] = false;\r\n" + 
        		"flag[1] = false;\r\n" + 
        		"\r\n" + 
        		"// // Local variables declaration			//	All declaration have to be made without any blank line\r\n" + 
        		"int j;\r\n" + 
        		"boolean a;\r\n" + 
        		"int b;\r\n" + 
        		"\r\n" + 
        		"// // Algorithm								//	You can write your algorithm using Java Syntax, blank line are allowed\r\n" + 
        		"// 											//	You only can access one variable operation at a time\r\n" + 
        		"// \r\n" + 
        		"\r\n" + 
        		"// // There for example, Petreson algorithm \r\n" + 
        		"j = (i+1) % 2;\r\n" + 
        		"flag[i] = true;\r\n" + 
        		"turn = j;\r\n" + 
        		"a = flag[j];								// All operation made in one line are atomic\r\n" + 
        		"b = turn;										\r\n" + 
        		"											//	Blank line are allowed\r\n" + 
        		"while ( a == true && b == j) {				//	The opening accolade \"{\" have to be in the same line than the condition\r\n" + 
        		"a = flag[j];\r\n" + 
        		"b = turn;\r\n" + 
        		"}											//	The closing accolade \"}\" have to be alone on the line\r\n" + 
        		"// critical section \r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"1\r\n" + 
        		"// end of critical section\r\n" + 
        		"flag[i] = false;\r\n" + 
        		"");
        alert.setResizable(true);
        alert.getDialogPane().setPrefSize(500, 500);
        alert.showAndWait();
	}
	
}
