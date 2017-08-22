package com.beatrice.gwt.calculator.client;

import java.util.ArrayList;

import org.eclipse.jdt.internal.core.NameLookup.Answer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dev.javac.testing.Source;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Calculator implements EntryPoint {

	private ArrayList<Button> numberButtonList;
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel numberPanel;
	private HorizontalPanel displayPanel;
	private HorizontalPanel operatorPanel;
	private Label operand1Label;
	private Label operand2Label;
	private Label operatorLabel;
	private Label answerLabel;
	private Label activeOperand;
	private Button calculateButton;
	private FlexTable resultsTable = new FlexTable();
	private static final String OPERATOR_STRING = "+-*/%";
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		numberButtonList = new ArrayList<>();
		numberPanel = new HorizontalPanel();
		numberPanel.addStyleName("numberPanel");
		displayPanel = new HorizontalPanel();
		operatorPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();
		answerLabel = new Label();
		operand1Label = new Label();
		operand2Label = new Label();
		operatorLabel = new Label();
		activeOperand = operand1Label;
		calculateButton = new Button("Calculate");
		calculateButton.addStyleName("calcButton");
		
		displayPanel.add(operand1Label);
		displayPanel.add(operatorLabel);
		displayPanel.add(operand2Label);
		
		setUpNumberPanel();
		setUpOperatorPanel();
		resultsTable.setText(0, 0, "Problem");
		resultsTable.setText(0, 2, "Answer");

		mainPanel.add(displayPanel);
		mainPanel.add(numberPanel);
		mainPanel.add(operatorPanel);
		mainPanel.add(calculateButton);
		mainPanel.add(answerLabel);
		mainPanel.add(resultsTable);

		// Associate the Number panel with the HTML host page.
		RootPanel.get("numberButtonContainer").add(mainPanel);

		calculateButton.addClickHandler(e -> calculate());

	}
	
	private void setUpOperatorPanel() {
		
		for(char ch: OPERATOR_STRING.toCharArray()) {
			Button button = new Button(Character.toString(ch));
			operatorPanel.add(button);
			button.addStyleName("numberButton");
			button.setEnabled(true);
			//button.setFocus(true);
			button.addClickHandler(e -> addOperator(e));
		}	
	}

	private void addOperator(ClickEvent e) {
		if(enterPressed== true) {
			enterPressed = false;
			return;
		}
		Button sourceButton = (Button)e.getSource();
		String operator = sourceButton.getText();
		if(activeOperand == operand1Label && activeOperand.getText() != "") {
			operatorLabel.setText(operator);
			activeOperand = operand2Label;
		} 
		
		/*else if (activeOperand.getText() == "" && operator == "-") {
			activeOperand.setText(operator);
		}*/
	}
	

	private boolean enterPressed = false;
	
	private void setUpNumberPanel() {
		for (int i = 0; i<10; i++) {
			setUpNumberButton(Integer.toString(i));
		}
		setUpNumberButton(".");
	}

	private void setUpNumberButton(String buttonText) {
		Button button = new Button(buttonText);
		numberButtonList.add(button);
		numberPanel.add(button);
		button.addStyleName("numberButton");
		button.setEnabled(true);
		button.setFocus(true);
		button.addClickHandler(e -> addToOperand(e));
		button.addKeyDownHandler(e -> {
	    	if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    		enterPressed = true;
	    		calculate();
	    	}
	    });
	}

	private void addToOperand(ClickEvent e) {
		Button sourceButton = (Button)e.getSource();
		String operandTextString = activeOperand.getText();
		String buttonText = sourceButton.getText();
		activeOperand.setText(operandTextString + buttonText);
	}

	//_______________________________________________________________________
	private void calculate() {
		String operator = operatorLabel.getText();
		double operand1 = Double.parseDouble(operand1Label.getText());
		double operand2 = Double.parseDouble(operand2Label.getText());
		double answer;
		
		
		if(operator.equals("*")) {
			answer = multiply(operand1, operand2);
		} else if (operator.equals("%")) {
			answer = modulo(operand1, operand2);			 
		} else if (operator.equals("/")) {
			answer = divide(operand1, operand2);	 
		} else if (operator.equals("-")) {
			answer = subtract(operand1, operand2);			 
		} else {   							 //addition
			answer = add(operand1, operand2);				
		}
		
		String problem = operand1Label.getText() + operatorLabel.getText() + operand2Label.getText();
		answerLabel.setText(problem + " = " + answer); 
		
		resetOperands();
		addResultToTable(problem, Double.toString(answer));
	}
	
	private double add(double operand1, double operand2) {
		return operand1 + operand2;
	}

	private double subtract(double operand1, double operand2) {
		return operand1 - operand2;
	}

	private double modulo(double operand1, double operand2) {
		return operand1 % operand2;
	}

	private double divide(double operand1, double operand2) {
		return operand1 / operand2;
	}

	private double multiply(double operand1, double operand2) {
		return operand1 * operand2;
	}
	

	private void addResultToTable(String problem, String answer) {
		int row = resultsTable.getRowCount();
		resultsTable.setText(row, 0, problem);
		resultsTable.setText(row, 1, "=");
		resultsTable.setText(row, 2, answer);
	}

	private void resetOperands() {
		operand1Label.setText("");
		operand2Label.setText("");
		operatorLabel.setText("");
		activeOperand = operand1Label;
		
	}

	//Checks if a String could be seen as an integer
	public boolean isInteger( String input )
	{
	   try
	   {
	      Integer.parseInt( input );
	      return true;
	   }
	   catch(NumberFormatException e)
	   {
	      return false;
	   }
	}
	//_________________________________________________________________________
}
