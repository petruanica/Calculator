package com.example.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;


public class Controller {
    @FXML
    private Label mainLabel;
    @FXML
    private Label currentLabel;

    private Alert alert;

    @FXML
    public void getOperation(ActionEvent event) {
        Button button = (Button) event.getSource();
        operationLogic(button.getText());
    }

    @FXML
    public void getDigit(ActionEvent event) {
        Button button = (Button) event.getSource();
        digitLogic(button.getText());
    }

    @FXML
    public void toggleNegative() {
        if (currentLabel.getText().startsWith("-")) {
            currentLabel.setText(currentLabel.getText().substring(1));
        } else {
            currentLabel.setText("-" + currentLabel.getText());
        }
    }

    @FXML
    public void getInverse() {
        // no number was entered in currentLabel
        if (currentLabel.getText().equals("overflow")) {
            clearAll();
        }
        if (currentLabel.getText().equals("")) {
            alert = new Alert(Alert.AlertType.ERROR, "Enter a number first!");
            alert.show();
//            AlertBox.display("Error!",  "Enter a number first!");
            return;
        }
        String result;
        // two numbers have already been entered
        if (mainLabel.getText().equals("") || mainLabel.getText().endsWith("=")) {
            // equals operation has just been done
            mainLabel.setText("1÷" + currentLabel.getText() + "=");
            try {
                result = Application.parseOperation("1÷", currentLabel.getText());
                currentLabel.setText(result);
            } catch (ArithmeticException e) {
                currentLabel.setText("null");
                alert = new Alert(Alert.AlertType.ERROR, "Division by zero is not possible!");
                alert.show();
//                    AlertBox.display("Error!", "Division by zero is not possible!");
                clearAll();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Operation not valid!");
            alert.show();
//            AlertBox.display("Error!", "Operation not valid!");
        }
    }

    @FXML
    public void clearAll() {
        mainLabel.setText("");
        clearCurrent();
    }

    @FXML
    public void clearCurrent() {
        currentLabel.setText("");
        if (mainLabel.getText().endsWith("=")) {
            mainLabel.setText("");
        }
    }

    @FXML
    public void clearLastCharacter() {
        if (!currentLabel.getText().equals("")) {
            currentLabel.setText(currentLabel.getText().substring(0, currentLabel.getText().length() - 1));
        }
    }

    @FXML
    public void operationLogic(String sourceText) {
        // no number was entered in currentLabel
        if (currentLabel.getText().equals("overflow")) {
            clearAll();
        }
        if (currentLabel.getText().equals("") || currentLabel.getText().endsWith(".")) {
            alert = new Alert(Alert.AlertType.ERROR, "Enter a number first!");
            alert.show();
//            AlertBox.display("Error!",  "Enter a number first!");
            return;
        }
        String result;
        // two numbers have already been entered
        if (!mainLabel.getText().equals("")) {
            // equals operation has just been done
            if (mainLabel.getText().endsWith("=")) {
                mainLabel.setText(currentLabel.getText() + sourceText);
                clearCurrent();
            } else {
                // compute result of operation
                try {
                    result = Application.parseOperation(mainLabel.getText(), currentLabel.getText());
                } catch (ArithmeticException e) {
                    mainLabel.setText(mainLabel.getText() + currentLabel.getText() + sourceText);
                    currentLabel.setText("null");
                    alert = new Alert(Alert.AlertType.ERROR, "Division by zero is not possible!");
                    alert.show();
//                    AlertBox.display("Error!", "Division by zero is not possible!");
                    clearAll();
                    return;
                }
                // check if equals button was pressed
                if (sourceText.equals("=")) {
                    mainLabel.setText(mainLabel.getText() + currentLabel.getText() + sourceText);
                    currentLabel.setText(result);
                } else {
                    if (result.equals("overflow")) {
                        clearAll();
                        currentLabel.setText(result);
                        return;
                    }
                    mainLabel.setText(result + sourceText);
                    clearCurrent();
                }
            }
        } else {
            mainLabel.setText(currentLabel.getText() + sourceText);
            clearCurrent();
        }
    }

    @FXML
    public void digitLogic(String sourceText) {
        if (mainLabel.getText().endsWith("=")) {
            clearAll();
        }
        if (currentLabel.getText().length() == 0 || Application.checkNumberSize(currentLabel.getText())) {
            if (currentLabel.getText().equals("") && sourceText.equals(".")) {
                currentLabel.setText("0.");
            } else {
                currentLabel.setText(Application.formatNumber(currentLabel.getText() + sourceText));
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Number is too large!");
            alert.show();
//            AlertBox.display("Error!", "Number is too large!");
        }
    }

    @FXML
    public void keyBinds(KeyEvent event) {
        String eventCode = event.getCode().toString();
        switch (eventCode) {
            case "NUMPAD0", "DIGIT0" -> digitLogic("0");
            case "NUMPAD1", "DIGIT1" -> digitLogic("1");
            case "NUMPAD2", "DIGIT2" -> digitLogic("2");
            case "NUMPAD3", "DIGIT3" -> digitLogic("3");
            case "NUMPAD4", "DIGIT4" -> digitLogic("4");
            case "NUMPAD5", "DIGIT5" -> digitLogic("5");
            case "NUMPAD6", "DIGIT6" -> digitLogic("6");
            case "NUMPAD7", "DIGIT7" -> digitLogic("7");
            case "NUMPAD8", "DIGIT8" -> digitLogic("8");
            case "NUMPAD9", "DIGIT9" -> digitLogic("9");
            case "DECIMAL", "PERIOD" -> digitLogic(".");
            case "DIVIDE" -> operationLogic("÷");
            case "MULTIPLY" -> operationLogic("*");
            case "SUBTRACT" -> operationLogic("-");
            case "ADD" -> operationLogic("+");
            case "MINUS" -> toggleNegative();
            case "BACK_SPACE" -> clearLastCharacter();
            case "DELETE" -> clearAll();
        }
    }
}