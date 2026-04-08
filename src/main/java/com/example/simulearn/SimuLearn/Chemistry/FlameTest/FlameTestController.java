package com.example.simulearn.SimuLearn.Chemistry.FlameTest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class FlameTestController {
    private Salt sample;
    private Salt[] salts = {
            new Salt("Sodium", "Bright Yellow", Color.rgb(255, 220, 0)),
            new Salt("Potassium", "Pale Lilac", Color.rgb(200, 162, 200)),
            new Salt("Calcium", "Orange-Red", Color.rgb(255, 77, 0)),
            new Salt("Barium", "Yellow-Green", Color.rgb(173, 255, 47)),
            new Salt("Copper(II)", "Blue-Green", Color.rgb(0, 255, 128)),
            new Salt("Strontium", "Crimson Red", Color.rgb(255, 0, 0)),
            new Salt("Lithium", "Carmine Red", Color.rgb(255, 48, 48)),
            new Salt("Sodium", "Bright Yellow", Color.rgb(255, 220, 0)),
            new Salt("Zinc", "Bluish-White", Color.rgb(200, 255, 255)),
            new Salt("Lead", "Pale Bluish-White", Color.rgb(240, 248, 255))
    };
    @FXML
    private Group PtRod;
    @FXML
    private Circle HCl;
    @FXML
    private Ellipse Salt;
    @FXML
    private Ellipse flame;
    @FXML
    private Label Instruction;
    @FXML
    private Line Tip;
    private double offsetX;
    private double offsetY;

    private boolean saltTaken;
    private boolean dippedInHCl;

    private boolean isRodTouching(Node target) {
        return PtRod.getBoundsInParent().intersects(target.getBoundsInParent());
    }

    @FXML
    private void onResetButtonClicked(ActionEvent e) {
        initialize();
    }

    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/chemistryMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    private void updateInstruction() {
        if (!dippedInHCl) {
            Instruction.setText("Dip the Pt rod in HCl");
        } else if (!saltTaken) {
            Instruction.setText("Take sample");
        } else {
            Instruction.setText("Hold the rod in the flame");
        }
    }

    @FXML
    public void initialize() {
        int i = (int) (Math.random() * 10);
        sample = salts[i];
        flame.setFill(Color.web("#efb209"));

        Instruction.setText("Dip the Pt rod in HCl");
        saltTaken = false;
        dippedInHCl = false;
        Tip.setVisible(false);
        HCl.setVisible(false);
        PtRod.setOnMousePressed(event -> {
            offsetX = event.getSceneX() - PtRod.getLayoutX();
            offsetY = event.getSceneY() - PtRod.getLayoutY();
        });

        PtRod.setOnMouseDragged(event -> {
            PtRod.setLayoutX(event.getSceneX() - offsetX);
            PtRod.setLayoutY(event.getSceneY() - offsetY);
            if (isRodTouching(HCl)) {
                dippedInHCl = true;
                updateInstruction();
            }
            if (isRodTouching(Salt)) {
                saltTaken = true;
                Tip.setVisible(true);
                updateInstruction();
            }
            if (isRodTouching(flame)) {
                if (dippedInHCl && saltTaken) {
                    flame.setFill(sample.getColor());
                    Instruction.setText("Flame color: " + sample.getColorName() + ", " + sample.getName() + " Salt identified");
                }
            }
        });
    }

}