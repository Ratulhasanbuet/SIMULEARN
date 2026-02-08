package com.example.simulearn;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static javafx.scene.paint.Color.*;

public class BuoyancyController {

    private double liquidStartHeight;
    private double liquidStartY;
    private double MaterialDensity;
    private double LiquidDensity;
    private double initialBallX;
    private double initialBallY;
    boolean ballCreated = false;
    boolean liquidCreated = false;
    boolean ballDropped = false;

    Material wood = new Material("Wood", 0.6, Color.SANDYBROWN);
    Material metal = new Material("Metal", 7.8, Color.GREY);
    Material plastic = new Material("Plastic", 0.8, Color.PINK);
    Material rubber = new Material("Rubber", 1.2, Color.YELLOWGREEN);

    Liquid water = new Liquid("Water", 1.0, Color.LIGHTBLUE);
    Liquid oil = new Liquid("Oil", 0.8, Color.YELLOW);
    Liquid alcohol = new Liquid("Alcohol", 0.79, Color.LIGHTGREEN);
    Liquid glycerine = new Liquid("Glycerine", 13.6, Color.SANDYBROWN);

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> MaterialChoiceBox;
    @FXML
    private ChoiceBox<String> LiquidChoiceBox;
    @FXML
    private Rectangle Liquid;
    @FXML
    private Circle Material;
    @FXML
    private Label MaterialDensityText;
    @FXML
    private Label LiquidDensityText;
    @FXML
    private Line buoyancyVector;
    @FXML
    private Line weightVector;
    @FXML
    private Polygon buoyancyArrow;
    @FXML
    private Polygon weightArrow;
    @FXML
    private CheckBox showVector;
    @FXML
    private VBox explanationBox;
    @FXML
    private Button showBtn, hideBtn;
    @FXML
    private RadioButton optionA, optionB, optionC, optionD;
    @FXML
    private Label resultLabel;
    @FXML
    private Label weight;
    @FXML
    private Label buoyancy;
    private ToggleGroup quizGroup;
    //private double initialLayoutX;
    //private double initialLayoutY;
    private Group arrow;
    // scale
    private void vectorTransition(TranslateTransition t, double Y) {
        t.setByY(Y);
        t.setCycleCount(1);
        t.play();
    }

    private void resetPositionOnChange() {
        Material.getTransforms().clear();
        Material.setTranslateX(0);
        Material.setTranslateY(0);
        Material.setCenterX(initialBallX);
        Material.setCenterY(initialBallY);
        ballDropped = false;

        weightVector.setTranslateX(0);
        weightVector.setTranslateY(0);
        weightArrow.setTranslateX(0);
        weightArrow.setTranslateY(0);
        buoyancyVector.setTranslateX(0);
        buoyancyVector.setTranslateY(0);
        buoyancyArrow.setTranslateX(0);
        buoyancyArrow.setTranslateY(0);
        buoyancyVector.setVisible(false);
        buoyancyArrow.setVisible(false);
        /*arrow.setLayoutX(initialLayoutX); // store these values at creation
        arrow.setLayoutY(initialLayoutY);
        arrow.setTranslateX(0);
        arrow.setTranslateY(0);*/
        buoyancyArrow.setFill(WHITESMOKE);
        buoyancyVector.setStroke(WHITESMOKE);
    }

    private void buoyancyScale(double time) {
        double vectorLength = buoyancyVector.getEndY() - buoyancyVector.getStartY();
        double weightLength = weightVector.getEndY() - weightVector.getStartY();
        double scale = weightLength / vectorLength;

        ScaleTransition st = new ScaleTransition(Duration.seconds(time), buoyancyVector);
        st.setToY(scale);

        st.play();
    }

    private void waterLevelRise(double rise, double time) {
        if(showVector.isSelected()) {
            buoyancyVector.setVisible(true);
            buoyancyVector.setStroke(BLUE);
        }
        //buoyancyArrow.setFill(BLUE);
        buoyancyScale(time);
        double increment1 = rise / 100;

        Timeline liquidTimeline = new Timeline(
                new KeyFrame(Duration.seconds(time / 100), e -> {
                    double h = Liquid.getHeight();
                    Liquid.setHeight(h + increment1);
                    Liquid.setY(Liquid.getY() - increment1); // move up
                })
        );
        liquidTimeline.setCycleCount(100);
        liquidTimeline.setOnFinished(e -> {
            if(showVector.isSelected()) {
                buoyancyArrow.setFill(BLUE);
                buoyancyArrow.setVisible(true);
            }
        });
        liquidTimeline.play();
    }

    private void waterLevelReset() {
        Liquid.setHeight(liquidStartHeight);
        Liquid.setY(liquidStartY);
    }

    @FXML
    private void initialize() {
        // Populate ChoiceBoxes
        /*arrow = new Group(buoyancyVector, buoyancyArrow);
        initialLayoutX = arrow.getLayoutX();
        initialLayoutY = arrow.getLayoutY();
        rootPane.getChildren().add(arrow);*/
        weight.setVisible(false);
        buoyancy.setVisible(false);
        quizGroup = new ToggleGroup();
        optionA.setToggleGroup(quizGroup);
        optionB.setToggleGroup(quizGroup);
        optionC.setToggleGroup(quizGroup);
        optionD.setToggleGroup(quizGroup);
        buoyancyVector.setVisible(false);
        buoyancyArrow.setVisible(false);
        weightArrow.setVisible(false);
        weightVector.setVisible(false);
        initialBallX = Material.getCenterX();
        initialBallY = Material.getCenterY();
        liquidStartY = Liquid.getY();
        liquidStartHeight = Liquid.getHeight();

        MaterialChoiceBox.getItems().addAll("Wood", "Metal", "Plastic", "Rubber");
        LiquidChoiceBox.getItems().addAll("Water", "Oil", "Alcohol", "Glycerine");
    }

    @FXML
    private void onMaterialSelected(ActionEvent event) {
        resetPositionOnChange();
        waterLevelReset();
        ballCreated = true;

        String selected = MaterialChoiceBox.getValue();
        if (selected.equals("Wood")) {
            Material.setFill(wood.getColor());
            MaterialDensityText.setText(String.valueOf(wood.getDensity()));
            MaterialDensity = wood.getDensity();
        } else if (selected.equals("Metal")) {
            Material.setFill(metal.getColor());
            MaterialDensityText.setText(String.valueOf(metal.getDensity()));
            MaterialDensity = metal.getDensity();
        } else if (selected.equals("Plastic")) {
            Material.setFill(plastic.getColor());
            MaterialDensityText.setText(String.valueOf(plastic.getDensity()));
            MaterialDensity = plastic.getDensity();
        } else if (selected.equals("Rubber")) {
            Material.setFill(rubber.getColor());
            MaterialDensityText.setText(String.valueOf(rubber.getDensity()));
            MaterialDensity = rubber.getDensity();
        }
    }

    @FXML
    private void onResetSelected(ActionEvent e) {
        MaterialChoiceBox.setValue("");
        LiquidChoiceBox.setValue("");
        Liquid.setFill(WHITESMOKE);
        Material.setFill(WHITESMOKE);
        LiquidDensityText.setText("");
        MaterialDensityText.setText("");
        ballCreated = false;
        liquidCreated = false;
        ballDropped = false;

        // resetting ball position
        Material.getTransforms().clear();
        Material.setTranslateX(0);
        Material.setTranslateY(0);
        Material.setCenterX(initialBallX);
        Material.setCenterY(initialBallY);

        waterLevelReset();

        weightVector.setTranslateX(0);
        weightVector.setTranslateY(0);
        weightArrow.setTranslateX(0);
        weightArrow.setTranslateY(0);
        buoyancyVector.setTranslateX(0);
        buoyancyVector.setTranslateY(0);
        buoyancyArrow.setTranslateX(0);
        buoyancyArrow.setTranslateY(0);
        buoyancyVector.setVisible(false);
        buoyancyArrow.setVisible(false);
        /*arrow.setLayoutX(initialLayoutX); // store these values at creation
        arrow.setLayoutY(initialLayoutY);
        arrow.setTranslateX(0);
        arrow.setTranslateY(0);*/
    }

    @FXML
    private void onLiquidSelected(ActionEvent e) {
        liquidCreated = true;
        resetPositionOnChange();
        waterLevelReset();

        String selected = LiquidChoiceBox.getValue();
        if (selected.equals("Water")) {
            Liquid.setFill(water.getColor());
            LiquidDensityText.setText(String.valueOf(water.getDensity()));
            LiquidDensity = water.getDensity();
        } else if (selected.equals("Oil")) {
            Liquid.setFill(oil.getColor());
            LiquidDensityText.setText(String.valueOf(oil.getDensity()));
            LiquidDensity = oil.getDensity();
        } else if (selected.equals("Glycerine")) {
            Liquid.setFill(glycerine.getColor());
            LiquidDensityText.setText(String.valueOf(glycerine.getDensity()));
            LiquidDensity = glycerine.getDensity();
        } else if (selected.equals("Alcohol")) {
            Liquid.setFill(alcohol.getColor());
            LiquidDensityText.setText(String.valueOf(alcohol.getDensity()));
            LiquidDensity = alcohol.getDensity();
        }
    }

    @FXML
    private void onDropBallSelected(ActionEvent event) {
        double vectorLength = buoyancyVector.getEndY() - buoyancyVector.getStartY();
        double weightLength = weightVector.getEndY() - weightVector.getStartY();
        if (ballCreated && liquidCreated && !ballDropped) {
            if (MaterialDensity > LiquidDensity) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), Material);
                double y = 57;
                tt.setByY(233); // move 233 pixels downward (use relative motion)
                tt.setCycleCount(1);
                tt.play();

                TranslateTransition ttWeight = new TranslateTransition(Duration.seconds(2), weightVector);
                vectorTransition(ttWeight, 233);

                TranslateTransition ttWeightArrow = new TranslateTransition(Duration.seconds(2), weightArrow);
                vectorTransition(ttWeightArrow, 233);

                TranslateTransition ttBuoyancy = new TranslateTransition(Duration.seconds(2), buoyancyVector);
                vectorTransition(ttBuoyancy, 233);

                TranslateTransition ttBuoyancyArrow = new TranslateTransition(Duration.seconds(2), buoyancyArrow);
                vectorTransition(ttBuoyancyArrow, 233- weightLength+vectorLength+30);

                double animationSpeed = (70 + y) / 2;
                double liquidRiseTime = y / animationSpeed;
                double t = 2 - liquidRiseTime; // seconds to wait before starting liquid
                Timeline delay = new Timeline(new KeyFrame(Duration.seconds(t), e -> waterLevelRise(y / 3, liquidRiseTime/3)));
                delay.play();
                ballDropped = true;

            } else if (MaterialDensity == LiquidDensity) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), Material);
                double y = 40;
                tt.setByY(137); // move pixels downward (use relative motion)
                tt.setCycleCount(1);
                tt.play();

                TranslateTransition ttWeight = new TranslateTransition(Duration.seconds(2), weightVector);
                vectorTransition(ttWeight, 137);

                TranslateTransition ttWeightArrow = new TranslateTransition(Duration.seconds(2), weightArrow);
                vectorTransition(ttWeightArrow, 137);

                TranslateTransition ttBuoyancy = new TranslateTransition(Duration.seconds(2), buoyancyVector);
                vectorTransition(ttBuoyancy, 137);

                TranslateTransition ttBuoyancyArrow = new TranslateTransition(Duration.seconds(2), buoyancyArrow);
                vectorTransition(ttBuoyancyArrow, 137- weightLength+vectorLength+30);

                double animationSpeed = 142.0 / 2;
                double liquidRiseTime = y / animationSpeed;
                double t = 2 - liquidRiseTime; // seconds to wait before starting liquid
                Timeline delay = new Timeline(new KeyFrame(Duration.seconds(t), e -> waterLevelRise(y / 3, liquidRiseTime)));
                delay.play();
                ballDropped = true;

            } else {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), Material);
                double y = MaterialDensity / LiquidDensity * 2 * Material.getRadius();
                tt.setByY(95 + y - y / 3); // move pixels downward (use relative motion)
                tt.setCycleCount(1);
                tt.play();

                TranslateTransition ttWeight = new TranslateTransition(Duration.seconds(2), weightVector);
                vectorTransition(ttWeight, 95 + y - y / 3);

                TranslateTransition ttWeightArrow = new TranslateTransition(Duration.seconds(2), weightArrow);
                vectorTransition(ttWeightArrow, 95 + y - y / 3);

                TranslateTransition ttBuoyancy = new TranslateTransition(Duration.seconds(2), buoyancyVector);
                vectorTransition(ttBuoyancy, 95 + y - y / 3);
                TranslateTransition ttBuoyancyArrow = new TranslateTransition(Duration.seconds(2), buoyancyArrow);
                vectorTransition(ttBuoyancyArrow, 95 + y - y / 3 - weightLength + vectorLength + 30);

                double animationSpeed = (95 + y - y / 3) / 2;
                double liquidRiseTime = (y / 3) / animationSpeed;
                double t = 2 - liquidRiseTime; // seconds to wait before starting liquid
                Timeline delay = new Timeline(new KeyFrame(Duration.seconds(t), e -> waterLevelRise(y / 3, liquidRiseTime)));
                delay.play();
                ballDropped = true;
            }
        }
    }
    @FXML
    private void onCheckBoxSelected()
    {
        if(showVector.isSelected())
        {
            weightVector.setVisible(true);
            weightArrow.setVisible(true);
            weightVector.setStroke(RED);
            weightArrow.setFill(RED);
            weight.setVisible(true);
            buoyancy.setVisible(true);
            if(ballDropped)
            {
                buoyancyVector.setVisible(true);
                buoyancyVector.setStroke(BLUE);
                buoyancyArrow.setVisible(true);
                buoyancyArrow.setFill(BLUE);
            }
        }
        else
        {
            weightVector.setVisible(false);
            weightArrow.setVisible(false);
            buoyancyVector.setVisible(false);
            buoyancyArrow.setVisible(false);
            weight.setVisible(false);
            buoyancy.setVisible(false);
        }
    }
    @FXML
    private void showExplanation() {
        explanationBox.setVisible(true);
        showBtn.setVisible(false);
        hideBtn.setVisible(true);
    }

    @FXML
    private void hideExplanation() {
        explanationBox.setVisible(false);
        showBtn.setVisible(true);
        hideBtn.setVisible(false);
    }

    @FXML
    private void checkAnswer() {
        if (optionD.isSelected()) {
            resultLabel.setText("✅ Correct! Buoyant force equals the weight of the sphere.");
            resultLabel.setTextFill(Color.GREEN);
        } else if (quizGroup.getSelectedToggle() == null) {
            resultLabel.setText("⚠️ Please select an answer.");
            resultLabel.setTextFill(Color.ORANGE);
        } else {
            resultLabel.setText("❌ Incorrect. Try again!");
            resultLabel.setTextFill(Color.RED);
        }
    }
}
