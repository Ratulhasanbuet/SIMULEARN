package com.example.simulearn;


import eu.hansolo.toolboxfx.geom.Point;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Stack;

public class vectorController {
    @FXML private VBox questionBox;
    @FXML private Button explanationButton;
    private Group resultantX,resultantY;
    private Stack<Group> vectorStack = new Stack<>();
    private Stack<Group> Xcomponent = new Stack<>();
    private Stack<Group> Ycomponent = new Stack<>();
    @FXML private AnchorPane graph;
    @FXML AnchorPane pane;
    private Line tempLine; // temporary preview line
    private Polygon arrowHead;
    private double startX, startY;
    @FXML private Label text;
    private double originX,originY;
    private double vx ;
    private double vy;
    private Group sumVector;
    private boolean resultantChanged;
    @FXML private CheckBox components;
    @FXML
    public void initialize() {
        quizGroup = new ToggleGroup();
        optionA.setToggleGroup(quizGroup);
        optionB.setToggleGroup(quizGroup);
        optionC.setToggleGroup(quizGroup);
        optionD.setToggleGroup(quizGroup);
        questionBox.setVisible(false);

        resultantChanged=false;
        vx = vy =0;
        Platform.runLater(() -> {
            drawGraph();
            enableVectorDrawing();
            double w = graph.getWidth();
            double h = graph.getHeight();
            graph.setPrefSize(w, h);
            graph.setMinSize(w, h);
            graph.setMaxSize(w, h);
            originX = graph.getWidth()/2 - 5;
            originY = graph.getHeight()/2 - 6.4;
        });
    }
    private void enableVectorDrawing() {
        graph.setOnMousePressed(this::handleMousePressed);
        graph.setOnMouseDragged(this::handleMouseDragged);
        graph.setOnMouseReleased(this::handleMouseReleased);
    }
    private void handleMousePressed(MouseEvent e) {
        graph.getChildren().remove(sumVector);
        graph.getChildren().remove(resultantX);
        graph.getChildren().remove(resultantY);
        resultantChanged=true;
        startX = e.getX();
        startY = e.getY();

        tempLine = new Line(startX, startY, startX, startY);
        tempLine.setStroke(Color.DARKRED);
        tempLine.setStrokeWidth(2);


        arrowHead = new Polygon();
        arrowHead.setFill(Color.DARKRED);
        Group vector = new Group(tempLine, arrowHead);
        graph.getChildren().add(vector);
        vectorStack.push(vector);
    }

    private void handleMouseDragged(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        tempLine.setEndX(endX);
        tempLine.setEndY(endY);
        updateArrowHead(startX, startY, endX, endY,arrowHead);
    }

    private void handleMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();
        double dx = endX - startX;
        double dy = endY - startY;
        double angle = Math.atan2(dy, dx);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double len = Math.sqrt(dx*dx + dy*dy);
        vx += len * cos;
        vy += len * sin;
        updateArrowHead(startX, startY, endX, endY,arrowHead);
        // Optional: convert to graph coordinates (center origin)
        double graphX1 = startX - originX;
        double graphY1 = originY - startY;
        double graphX2 = endX - originX;
        double graphY2 = originY - endY;
        text.setText(String.format(
                "Vector from (%.2f, %.2f) to (%.2f, %.2f)",
                graphX1, graphY1, graphX2, graphY2
        ));
        showComp();
    }

    private void updateArrowHead(double x1, double y1, double x2, double y2,Polygon targetArrowHead) {
        double arrowLength = 10;
        double arrowWidth = 7;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double xBase = x2 - arrowLength * cos;
        double yBase = y2 - arrowLength * sin;

        double xLeft = xBase - arrowWidth * sin;
        double yLeft = yBase + arrowWidth * cos;

        double xRight = xBase + arrowWidth * sin;
        double yRight = yBase - arrowWidth * cos;

        targetArrowHead.getPoints().setAll(x2, y2, xLeft, yLeft, xRight, yRight);
    }
    private void drawGraph()
    {
        double width = graph.getWidth();
        double height = graph.getHeight();
        double cellSize = 5.35;
        // Light grid lines
        for (int i = 0; i <= width; i += cellSize) {
            Line vLine = new Line(i, 0, i, height);
            vLine.setStroke(Color.LIGHTGRAY);
            graph.getChildren().add(vLine);
        }
        for (int j = 0; j <= height; j += cellSize) {
            Line hLine = new Line(0, j, width, j);
            hLine.setStroke(Color.LIGHTGRAY);
            graph.getChildren().add(hLine);
        }

        // Thicker major lines (every 100 px)
        for (int i = 0; i <= width; i += cellSize * 5) {
            Line majorV = new Line(i, 0, i, height);
            majorV.setStroke(Color.GRAY);
            majorV.setStrokeWidth(1.2);
            graph.getChildren().add(majorV);
        }
        for (int j = 0; j <= height; j += cellSize * 5) {
            Line majorH = new Line(0, j, width, j);
            majorH.setStroke(Color.GRAY);
            majorH.setStrokeWidth(1.2);
            graph.getChildren().add(majorH);
        }

        // Center axes (optional)
        Line xAxis = new Line(0, height / 2 - cellSize, width, height / 2 - cellSize);
        xAxis.setStroke(Color.RED);
        xAxis.setStrokeWidth(1.5);
        Line yAxis = new Line(width / 2 - cellSize, 0, width / 2 - cellSize, height);
        yAxis.setStroke(Color.BLUE);
        yAxis.setStrokeWidth(1.5);

        graph.getChildren().addAll(xAxis, yAxis);

        // Set preferred size so ScrollPane knows how large it is
        graph.setPrefSize(width, height);
    }
    @FXML
    private void onReset() {
        graph.getChildren().clear();
        drawGraph();
        text.setText("");
        while(!vectorStack.empty())
            vectorStack.pop();
        while(!Xcomponent.empty())
            Xcomponent.pop();
        while (!Ycomponent.empty())
            Ycomponent.pop();
        vx = vy =0;
        resultantChanged=false;
    }
    @FXML
    private void undoVector()
    {
        resultantChanged=true;
        graph.getChildren().remove(sumVector);
        Group group = vectorStack.pop();
        graph.getChildren().remove(group);
        text.setText("");
        for (var node : group.getChildren()) {
            if (node instanceof Line line) {
                double dx = line.getEndX() - line.getStartX();
                vx -=dx;
                double dy = line.getEndY() - line.getStartY();
                vy-=dy;
            }
        }
        group = Xcomponent.pop();
        graph.getChildren().remove(group);
        group = Ycomponent.pop();
        graph.getChildren().remove(group);
        graph.getChildren().remove(resultantX);
        graph.getChildren().remove(resultantY);
        if(vectorStack.empty())
            onReset();
        else {
            showSum();
            showComp();
        }
    }
    @FXML
    private void showSum()
    {
        if(!resultantChanged)
            return;
        graph.getChildren().remove(sumVector);
        graph.getChildren().remove(resultantX);
        graph.getChildren().remove(resultantY);
        tempLine = new Line(originX, originY,originX+vx,originY+vy );
        tempLine.setStroke(Color.YELLOWGREEN);
        tempLine.setStrokeWidth(2);


        arrowHead = new Polygon();
        updateArrowHead(originX,originY,originX+vx,originY+vy,arrowHead);
        arrowHead.setFill(Color.YELLOWGREEN);
        sumVector = new Group(tempLine, arrowHead);
        graph.getChildren().add(sumVector);
        // for the resultant
        if(components.isSelected())
        {
            tempLine = new Line(originX, originY,originX+vx,originY);
            tempLine.setStroke(Color.BLACK);
            tempLine.setStrokeWidth(2);
            arrowHead = new Polygon();
            updateArrowHead(originX,originY,originX+vx,originY,arrowHead);
            arrowHead.setFill(Color.BLACK);
            resultantX = new Group(tempLine, arrowHead);
            graph.getChildren().add(resultantX);
            tempLine = new Line(originX, originY,originX,originY+vy);
            tempLine.setStroke(Color.PURPLE);
            tempLine.setStrokeWidth(2);
            arrowHead = new Polygon();
            updateArrowHead(originX,originY,originX,originY+vy,arrowHead);
            arrowHead.setFill(Color.PURPLE);
            resultantY = new Group(tempLine, arrowHead);
            graph.getChildren().add(resultantY);
        }
        else
        {
            graph.getChildren().remove(resultantX);
            graph.getChildren().remove(resultantY);
        }
        resultantChanged=false;
    }
    @FXML
    private void showComp()
    {
        if(components.isSelected())
            showComponents();
        else
        {
            while (!Xcomponent.empty())
            {
                Group group = Xcomponent.pop();
                graph.getChildren().remove(group);
            }
            while (!Ycomponent.empty())
            {
                Group group = Ycomponent.pop();
                graph.getChildren().remove(group);
            }
            graph.getChildren().remove(resultantX);
            graph.getChildren().remove(resultantY);
        }
    }
    private void showComponents()
    {
        while (!Xcomponent.empty()) {
           Group group = Xcomponent.pop();
           graph.getChildren().remove(group);
        }
        while (!Ycomponent.empty()) {
            Group group = Ycomponent.pop();
            graph.getChildren().remove(group);
        }
        graph.getChildren().remove(resultantX);
        graph.getChildren().remove(resultantY);
        ArrayList<Group> vectors = new ArrayList<>();
        while (!vectorStack.empty())
            vectors.add(vectorStack.pop());
        int i;
        for(i=0;i<vectors.size();i++)
        {
            for(var node : vectors.get(i).getChildren())
            {
                if(node instanceof Line line)
                {
                    double dx = line.getEndX() - line.getStartX();
                    tempLine = new Line(originX, originY,originX+dx,originY);
                    tempLine.setStroke(Color.BLACK);
                    tempLine.setStrokeWidth(2);

                    arrowHead = new Polygon();
                    updateArrowHead(originX,originY,originX+dx,originY,arrowHead);
                    arrowHead.setFill(Color.BLACK);
                    Group component= new Group(tempLine, arrowHead);
                    graph.getChildren().add(component);
                    Xcomponent.push(component);

                    double dy = line.getEndY() - line.getStartY();
                    tempLine = new Line(originX, originY,originX,originY+dy);
                    tempLine.setStroke(Color.PURPLE);
                    tempLine.setStrokeWidth(2);

                    arrowHead = new Polygon();
                    updateArrowHead(originX,originY,originX,originY+dy,arrowHead);
                    arrowHead.setFill(Color.PURPLE);
                    component= new Group(tempLine, arrowHead);
                    graph.getChildren().add(component);
                    Ycomponent.push(component);
                }
            }
        }
        tempLine = new Line(originX, originY,originX+vx,originY);
        tempLine.setStroke(Color.BLACK);
        tempLine.setStrokeWidth(2);
        arrowHead = new Polygon();
        updateArrowHead(originX,originY,originX+vx,originY,arrowHead);
        arrowHead.setFill(Color.BLACK);
        resultantX = new Group(tempLine, arrowHead);
        graph.getChildren().add(resultantX);
        tempLine = new Line(originX, originY,originX,originY+vy);
        tempLine.setStroke(Color.PURPLE);
        tempLine.setStrokeWidth(2);
        arrowHead = new Polygon();
        updateArrowHead(originX,originY,originX,originY+vy,arrowHead);
        arrowHead.setFill(Color.PURPLE);
        resultantY = new Group(tempLine, arrowHead);
        graph.getChildren().add(resultantY);
        for(i= vectors.size() - 1; i>=0;i--)
        {
            vectorStack.push(vectors.get(i));
        }

    }
    @FXML
    private void onExplanation()
    {
        if(explanationButton.getText().equals("Show Explanation")) {
            graph.setVisible(false);
            explanationButton.setText("Hide Explanation");
            questionBox.setVisible(true);
        }
        else
        {
            questionBox.setVisible(false);
            graph.setVisible(true);
            explanationButton.setText("Show Explanation");
        }
    }
    @FXML private Label resultLabel;
    @FXML
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup quizGroup;
    @FXML
    private void checkAnswer() {
        if (optionB.isSelected()) {
            resultLabel.setText("✅ Correct! ");
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