package com.example.simulearn;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionDark {
    private VBox questionBox;
    private ToggleGroup toggleGroup;
    private int correctAnswerIndex;
    private String feedbackText;
    private int attemptsLeft;
    private Label attemptsLabel;
    private Button submitButton;
    private boolean answered = false;
    private List<HBox> optionBoxes;

    public VBox getQuestionBox() {
        return questionBox;
    }

    public QuizQuestionDark(int questionNum, String questionText, String[] options,
                            int correctIndex, String feedback, int maxAttempts) {
        this.correctAnswerIndex = correctIndex;
        this.feedbackText = feedback;
        this.attemptsLeft = maxAttempts;
        this.optionBoxes = new ArrayList<>();

        questionBox = new VBox(15);
        questionBox.setStyle(
                "-fx-background-color: #2a2a2a;" +
                "-fx-border-color: #3a3a3a;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 25;"
        );
        questionBox.setMaxWidth(700);

        // Question text
        Label question = new Label(questionNum + ". " + questionText);
        question.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        question.setStyle("-fx-text-fill: #14a6ac;");
        question.setWrapText(true);

        // Options
        toggleGroup = new ToggleGroup();
        VBox optionsContainer = new VBox(10);

        for (int i = 0; i < options.length; i++) {
            HBox optionBox = createOptionBox(options[i], (char) ('A' + i), i);
            optionBoxes.add(optionBox);
            optionsContainer.getChildren().add(optionBox);
        }

        // Bottom section
        HBox bottomSection = new HBox(20);
        bottomSection.setAlignment(Pos.CENTER_LEFT);

        attemptsLabel = new Label("Attempts left: " + attemptsLeft);
        attemptsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        attemptsLabel.setStyle("-fx-text-fill: #ffffff;");

        submitButton = new Button("Submit Answer");
        submitButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        submitButton.setPadding(new Insets(10, 30, 10, 30));
        submitButton.setStyle(
                "-fx-background-color: #4a4a4a;" +
                "-fx-text-fill: #999999;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;" +
                "-fx-border-width: 0;"
        );
        submitButton.setDisable(true);

        toggleGroup.selectedToggleProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !answered) {
                submitButton.setDisable(false);
                submitButton.setStyle(
                        "-fx-background-color: #0d7377;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 30;" +
                        "-fx-border-width: 0;"
                );
            }
        });

        submitButton.setOnAction(e -> handleSubmit());
        submitButton.setOnMouseEntered(e -> {
            if (!submitButton.isDisabled()) {
                submitButton.setOpacity(0.8);
            }
        });
        submitButton.setOnMouseExited(e -> submitButton.setOpacity(1.0));

        HBox.setHgrow(new Region(), Priority.ALWAYS);
        bottomSection.getChildren().addAll(attemptsLabel, submitButton);

        questionBox.getChildren().addAll(question, optionsContainer, bottomSection);
    }

    private HBox createOptionBox(String optionText, char letter, int index) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(
                "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #3a3a3a;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 15;" +
                "-fx-cursor: hand;"
        );
        box.setPrefHeight(60);

        // Letter circle
        Circle circle = new Circle(20);
        circle.setFill(Color.web("#2a2a2a"));
        circle.setStroke(Color.web("#5a5a5a"));
        circle.setStrokeWidth(2);

        Label letterLabel = new Label(String.valueOf(letter));
        letterLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        letterLabel.setStyle("-fx-text-fill: #ffffff;");

        StackPane circleStack = new StackPane(circle, letterLabel);

        // Option text
        Label optionLabel = new Label(optionText);
        optionLabel.setFont(Font.font("Segoe UI", 14));
        optionLabel.setStyle("-fx-text-fill: #cccccc;");
        optionLabel.setWrapText(true);
        optionLabel.setMaxWidth(550);

        HBox.setHgrow(optionLabel, Priority.ALWAYS);

        box.getChildren().addAll(circleStack, optionLabel);

        // Radio button
        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setVisible(false);
        radioButton.setUserData(index);

        // Click handler
        box.setOnMouseClicked(e -> {
            if (!answered) {
                radioButton.setSelected(true);
                // Highlight selected
                for (HBox optBox : optionBoxes) {
                    optBox.setStyle(
                            "-fx-background-color: #1e1e1e;" +
                            "-fx-border-color: #3a3a3a;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 15;" +
                            "-fx-cursor: hand;"
                    );
                }
                box.setStyle(
                        "-fx-background-color: #0d3436;" +
                        "-fx-border-color: #14a6ac;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;"
                );
            }
        });

        // Hover effect
        box.setOnMouseEntered(e -> {
            if (!answered && toggleGroup.getSelectedToggle() != radioButton) {
                box.setStyle(
                        "-fx-background-color: #2a2a2a;" +
                        "-fx-border-color: #4a4a4a;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;"
                );
            }
        });

        box.setOnMouseExited(e -> {
            if (!answered && toggleGroup.getSelectedToggle() != radioButton) {
                box.setStyle(
                        "-fx-background-color: #1e1e1e;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;"
                );
            }
        });

        return box;
    }

    private void handleSubmit() {
        if (answered) return;

        Toggle selectedToggle = toggleGroup.getSelectedToggle();
        if (selectedToggle == null) return;

        int selectedIndex = (int) selectedToggle.getUserData();

        if (selectedIndex == correctAnswerIndex) {
            // Correct answer
            answered = true;
            showFeedback(selectedIndex, true);
            submitButton.setDisable(true);
            submitButton.setText("Correct!");
            submitButton.setStyle(
                    "-fx-background-color: #27ae60;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 10 30;"
            );
        } else {
            // Wrong answer
            attemptsLeft--;
            attemptsLabel.setText("Attempts left: " + attemptsLeft);
            showFeedback(selectedIndex, false);

            if (attemptsLeft <= 0) {
                // No more attempts
                answered = true;
                showCorrectAnswer();
                submitButton.setDisable(true);
                submitButton.setText("Out of Attempts");
                submitButton.setStyle(
                        "-fx-background-color: #4a4a4a;" +
                        "-fx-text-fill: #999999;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 10 30;"
                );
            } else {
                // Reset for retry
                toggleGroup.selectToggle(null);
                submitButton.setDisable(true);
                submitButton.setStyle(
                        "-fx-background-color: #4a4a4a;" +
                        "-fx-text-fill: #999999;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 10 30;" +
                        "-fx-cursor: hand;"
                );
            }
        }
    }

    private void showFeedback(int selectedIndex, boolean correct) {
        HBox selectedBox = optionBoxes.get(selectedIndex);

        if (correct) {
            // Green checkmark
            Circle checkCircle = new Circle(15);
            checkCircle.setFill(Color.web("#27ae60"));
            checkCircle.setStroke(Color.web("#229954"));
            checkCircle.setStrokeWidth(2);

            Label checkmark = new Label("✓");
            checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            checkmark.setStyle("-fx-text-fill: white;");

            StackPane checkIcon = new StackPane(checkCircle, checkmark);

            selectedBox.getChildren().add(checkIcon);
            selectedBox.setStyle(
                    "-fx-background-color: #1a4d2e;" +
                    "-fx-border-color: #27ae60;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 15;"
            );

            // Feedback
            if (feedbackText != null) {
                Label feedback = new Label("✓ " + feedbackText);
                feedback.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 13));
                feedback.setStyle("-fx-text-fill: #27ae60;");
                feedback.setPadding(new Insets(10, 0, 0, 0));
                feedback.setWrapText(true);
                questionBox.getChildren().add(questionBox.getChildren().size() - 1, feedback);
            }
        } else {
            // Red X
            Circle xCircle = new Circle(15);
            xCircle.setFill(Color.web("#e74c3c"));
            xCircle.setStroke(Color.web("#c0392b"));
            xCircle.setStrokeWidth(2);

            Label xMark = new Label("✗");
            xMark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            xMark.setStyle("-fx-text-fill: white;");

            StackPane xIcon = new StackPane(xCircle, xMark);

            selectedBox.getChildren().add(xIcon);
            selectedBox.setStyle(
                    "-fx-background-color: #4d1a1a;" +
                    "-fx-border-color: #e74c3c;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 15;"
            );
        }
    }

    private void showCorrectAnswer() {
        HBox correctBox = optionBoxes.get(correctAnswerIndex);

        Circle checkCircle = new Circle(15);
        checkCircle.setFill(Color.web("#27ae60"));
        checkCircle.setStroke(Color.web("#229954"));
        checkCircle.setStrokeWidth(2);

        Label checkmark = new Label("✓");
        checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        checkmark.setStyle("-fx-text-fill: white;");

        StackPane checkIcon = new StackPane(checkCircle, checkmark);

        correctBox.getChildren().add(checkIcon);
        correctBox.setStyle(
                "-fx-background-color: #1a4d2e;" +
                "-fx-border-color: #27ae60;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 15;"
        );

        // Show feedback
        if (feedbackText != null) {
            Label feedback = new Label("Correct answer: " + feedbackText);
            feedback.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 13));
            feedback.setStyle("-fx-text-fill: #27ae60;");
            feedback.setPadding(new Insets(10, 0, 0, 0));
            feedback.setWrapText(true);
            questionBox.getChildren().add(questionBox.getChildren().size() - 1, feedback);
        }
    }
}
