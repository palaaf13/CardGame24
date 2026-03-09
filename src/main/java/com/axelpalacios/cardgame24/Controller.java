package com.axelpalacios.cardgame24;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;

public class Controller {

    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;
    @FXML
    private TextField answerKey;
    @FXML
    private TextField userAnswer;

    private Cards game = new Cards();


    @FXML
    public void initialize() {
        refreshCards();
    }

    @FXML
    public void refreshCards() {

        Image[] cards = game.getRandomCards();

        card1.setImage(cards[0]);
        card2.setImage(cards[1]);
        card3.setImage(cards[2]);
        card4.setImage(cards[3]);
        answerKey.setText("");
        userAnswer.setText("");
    }

    @FXML
    public void findSolution() {

        int[] values = game.getCardValues();

        String solution = findSolution(values);

        answerKey.setText(solution);
    }

    private String findSolution(int[] nums) {
        char[] ops = {'+', '-', '*', '/'};

        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++)
                for (int c = 0; c < 4; c++)
                    for (int d = 0; d < 4; d++) {

                        if (a != b && a != c && a != d && b != c && b != d && c != d) {

                            double n1 = nums[a];
                            double n2 = nums[b];
                            double n3 = nums[c];
                            double n4 = nums[d];

                            for (char op1 : ops)
                                for (char op2 : ops)
                                    for (char op3 : ops) {

                                        double r1 = calc(n1, n2, op1);
                                        double r2 = calc(r1, n3, op2);
                                        double r3 = calc(r2, n4, op3);

                                        if (Math.abs(r3 - 24) < 0.0001) {
                                            // Wrap each operation in parentheses to ensure valid expression
                                            return "((" + (int)n1 + op1 + (int)n2 + ")" + op2 + (int)n3 + ")" + op3 + (int)n4;
                                        }
                                    }
                        }
                    }

        return "No solution found.";
    }
    private double calc(double a, double b, char op) {

        switch(op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) return Double.NaN;
                return a / b;
        }

        return Double.NaN;
    }

    @FXML
    public void verifyAnswer() {

        String expression = userAnswer.getText().trim();
        int[] cardValues = game.getCardValues();

        try {
            // Check that the numbers used match the card values
            if (!checkNumbersUsed(expression, cardValues)) {
                showMessage("Invalid", "You must use each of the four card values exactly once.");
                return;
            }


            double result = evaluateExpression(expression);

            if (Math.abs(result - 24) < 0.0001) {
                showMessage("Correct!", "Your expression equals 24.");
            } else {
                showMessage("Incorrect", "Your expression does not equal 24.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error", "Invalid expression. Make sure your syntax is correct.");
        }
    }

    private double evaluateExpression(String expr) throws Exception {
        return new ExpressionParser(expr).parse();
    }

    private boolean checkNumbersUsed(String expr, int[] cards) {

        java.util.List<Integer> numbersInExpr = new java.util.ArrayList<>();

        // Match all numbers (1-13) in the expression
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\b(\\d{1,2})\\b").matcher(expr);
        while (m.find()) {
            int n = Integer.parseInt(m.group(1));
            if (n < 1 || n > 13) return false;
            numbersInExpr.add(n);
        }

        // Make a copy of the card values as a list to remove used numbers
        java.util.List<Integer> cardList = new java.util.ArrayList<>();
        for (int c : cards) cardList.add(c);

        // Remove each number found in expression from cardList
        for (int n : numbersInExpr) {
            if (!cardList.remove(Integer.valueOf(n))) {
                // Number not found or used more than available
                return false;
            }
        }

        // All numbers must be used
        return cardList.isEmpty();
    }


    private void showMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
