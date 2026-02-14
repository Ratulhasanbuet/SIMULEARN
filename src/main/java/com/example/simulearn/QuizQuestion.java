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

// Inner class to handle quiz questions
public class QuizQuestion {
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

    public QuizQuestion(int questionNum, String questionText, String[] options,
                        int correctIndex, String feedback, int maxAttempts, boolean isFeedback) {
        this.correctAnswerIndex = correctIndex;
        this.feedbackText = feedback;
        this.attemptsLeft = maxAttempts;
        this.optionBoxes = new ArrayList<>();

        questionBox = new VBox(15);
        questionBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 25;"
        );
        questionBox.setMaxWidth(700);

        // Question text
        Label question = new Label(questionNum + ". " + questionText);
        question.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        question.setStyle("-fx-text-fill: #1a5490;");
        question.setWrapText(true);

        // Options
        toggleGroup = new ToggleGroup();
        VBox optionsContainer = new VBox(10);

        for (int i = 0; i < options.length; i++) {
            HBox optionBox = createOptionBox(options[i], (char) ('A' + i), i);
            optionBoxes.add(optionBox);
            optionsContainer.getChildren().add(optionBox);
        }

        // Bottom section (attempts + submit button)
        HBox bottomSection = new HBox(20);
        bottomSection.setAlignment(Pos.CENTER_LEFT);

        if (!isFeedback) {
            attemptsLabel = new Label("Attempts left: " + attemptsLeft);
            attemptsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            attemptsLabel.setStyle("-fx-text-fill: #333333;");
        }

        submitButton = new Button("Submit answer");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 30;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        submitButton.setDisable(true);

        // Enable submit when an option is selected
        toggleGroup.selectedToggleProperty().addListener((obs, old, newVal) -> {
            if (newVal != null && !answered) {
                submitButton.setDisable(false);
                submitButton.setStyle(
                        "-fx-background-color: #f39c12;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 10 30;" +
                                "-fx-background-radius: 5;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        submitButton.setOnAction(e -> handleSubmit(isFeedback));

        HBox.setHgrow(new Region(), Priority.ALWAYS);

        if (!isFeedback) {
            bottomSection.getChildren().addAll(attemptsLabel, submitButton);
        } else {
            bottomSection.getChildren().add(submitButton);
        }

        questionBox.getChildren().addAll(question, optionsContainer, bottomSection);
    }

    private HBox createOptionBox(String optionText, char letter, int index) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle(
                "-fx-background-color: #f9f9f9;" +
                        "-fx-border-color: #d0d0d0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;"
        );
        box.setPrefHeight(60);

        // Letter circle
        Circle circle = new Circle(20);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.web("#333333"));
        circle.setStrokeWidth(2);

        Label letterLabel = new Label(String.valueOf(letter));
        letterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        letterLabel.setStyle("-fx-text-fill: #333333;");

        StackPane circleStack = new StackPane(circle, letterLabel);

        // Option text
        Label optionLabel = new Label(optionText);
        optionLabel.setFont(Font.font("Arial", 15));
        optionLabel.setStyle("-fx-text-fill: #333333;");
        optionLabel.setWrapText(true);
        optionLabel.setMaxWidth(550);

        HBox.setHgrow(optionLabel, Priority.ALWAYS);

        box.getChildren().addAll(circleStack, optionLabel);

        // Radio button (invisible, just for toggle group)
        RadioButton radioButton = new RadioButton();
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setVisible(false);
        radioButton.setUserData(index);

        // Click handler
        box.setOnMouseClicked(e -> {
            if (!answered) {
                radioButton.setSelected(true);
                // Highlight selected option
                for (HBox optBox : optionBoxes) {
                    optBox.setStyle(
                            "-fx-background-color: #f9f9f9;" +
                                    "-fx-border-color: #d0d0d0;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-border-radius: 5;" +
                                    "-fx-background-radius: 5;" +
                                    "-fx-padding: 15;" +
                                    "-fx-cursor: hand;"
                    );
                }
                box.setStyle(
                        "-fx-background-color: #e8f4f8;" +
                                "-fx-border-color: #00aaff;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 5;" +
                                "-fx-background-radius: 5;" +
                                "-fx-padding: 15;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        return box;
    }

    private void handleSubmit(boolean isFeedback) {
        if (answered) return;

        Toggle selectedToggle = toggleGroup.getSelectedToggle();
        if (selectedToggle == null) return;

        int selectedIndex = (int) selectedToggle.getUserData();

        if (isFeedback) {
            // For feedback question, just mark as answered
            answered = true;
            submitButton.setDisable(true);
            submitButton.setText("Submitted");
            submitButton.setStyle(
                    "-fx-background-color: #95a5a6;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 10 30;" +
                            "-fx-background-radius: 5;"
            );
            // Highlight selected option in blue
            optionBoxes.get(selectedIndex).setStyle(
                    "-fx-background-color: #d4e6f1;" +
                            "-fx-border-color: #00aaff;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 15;"
            );
        } else {
            // For quiz questions
            if (selectedIndex == correctAnswerIndex) {
                // Correct answer - show green
                answered = true;
                showFeedback(selectedIndex, true);
                submitButton.setDisable(true);
            } else {
                // Wrong answer - show red
                attemptsLeft--;
                attemptsLabel.setText("Attempts left: " + attemptsLeft);
                showFeedback(selectedIndex, false);

                if (attemptsLeft <= 0) {
                    // No more attempts - show correct answer
                    answered = true;
                    showCorrectAnswer();
                    submitButton.setDisable(true);
                } else {
                    // Reset selection for retry
                    toggleGroup.selectToggle(null);
                    submitButton.setDisable(true);
                    submitButton.setStyle(
                            "-fx-background-color: #95a5a6;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-padding: 10 30;" +
                                    "-fx-background-radius: 5;" +
                                    "-fx-cursor: hand;"
                    );
                }
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
                    "-fx-background-color: #d5f4e6;" +
                            "-fx-border-color: #27ae60;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 15;"
            );

            // Show feedback text if available
            if (feedbackText != null) {
                Label feedback = new Label("Feedback: " + feedbackText);
                feedback.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
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
                    "-fx-background-color: #fadbd8;" +
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
                "-fx-background-color: #d5f4e6;" +
                        "-fx-border-color: #27ae60;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;"
        );
    }
}
