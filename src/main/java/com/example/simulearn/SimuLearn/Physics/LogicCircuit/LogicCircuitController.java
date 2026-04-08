package com.example.simulearn.SimuLearn.Physics.LogicCircuit;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LogicCircuitController {
    ImageView bulbView = null;
    @FXML
    private Button BackButton;
    private Image bulbOn;
    private Image bulbOff;
    private ArrayList<NodePoint> selectedNodes = new ArrayList<>();

    private enum ToolMode {NONE, ADD_NODE, AND_GATE, OR_GATE, NOT_GATE, NAND_GATE, NOR_GATE, TOGGLE}

    private ToolMode currentMode = ToolMode.NONE;
    private ArrayList<Gate> Gates = new ArrayList<>();
    @FXML
    private AnchorPane pane;
    @FXML
    private Button addNodeButton, AndButton, orButton, notButton, nandButton, norButton, ToggleButton;
    private ArrayList<Boolean> isUsedNode = new ArrayList<Boolean>();
    private ArrayList<NodePoint> Nodes = new ArrayList<>();
    private ArrayList<NodePoint> secondaryNodes = new ArrayList<>();
    NodePoint lastNode;

    @FXML
    void onBackButtonClicked() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/physicsMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) addNodeButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    private void initialize() {
        bulbOn = new Image(getClass().getResource("/images/BulbOn.png").toExternalForm());
        bulbOff = new Image(getClass().getResource("/images/BulbOff.jpg").toExternalForm());
        bulbView = new ImageView();
        bulbView.setFitWidth(40);
        bulbView.setFitHeight(40);
        pane.getChildren().add(bulbView);
        bulbView.setVisible(false);
        currentMode = ToolMode.NONE;
    }

    @FXML
    private void onAddNode() {
        currentMode = ToolMode.ADD_NODE;
        addNodeButton.getStyleClass().remove("button-74");
        addNodeButton.getStyleClass().add("button-33");
        addNodeButton.setText("Adding Node");
    }

    private void AddNode(MouseEvent e) {
        double x = e.getX();
        double y = e.getY();

        double sceneX = e.getSceneX();
        double sceneY = e.getSceneY();

        Bounds bounds = pane.localToScene(pane.getBoundsInLocal());
        if (!bounds.contains(sceneX, sceneY)) {
            currentMode = ToolMode.NONE;
            return;
        }
        Circle node = new Circle();
        Label label = new Label();
        label.setLayoutX(e.getX());
        label.setLayoutY(e.getY() + 10);
        char c = (char) (Nodes.size() + secondaryNodes.size() + 65);
        Boolean b = true;
        label.setText(c + ": " + b);
        node.setCenterX(e.getX());
        node.setCenterY(e.getY());
        node.setRadius(4);
        node.setFill(Color.BLACK);
        pane.getChildren().addAll(node, label);
        NodePoint nodePoint = new NodePoint(node, label, b, c);
        Nodes.add(nodePoint);
        lastNode = nodePoint;
        isUsedNode.add(false);
        currentMode = ToolMode.NONE;
        node.setOnMouseEntered(e1 -> {
            node.setScaleX(2);
            node.setScaleY(2);
        });

        node.setOnMouseExited(e1 -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
        node.setOnMouseClicked(ev -> handleNodeClick(node));
    }

    @FXML
    void NodeAddition(MouseEvent e) {
        if (currentMode == ToolMode.ADD_NODE) {
            AddNode(e);
            addNodeButton.getStyleClass().remove("button-33");
            addNodeButton.getStyleClass().add("button-74");
            addNodeButton.setText("Add Node");
        }
    }

    private Arc drawD(double arcCenterX, double arcCenterY, double radiusX, double radiusY) {

        Arc arc = new Arc();
        arc.setCenterX(arcCenterX);
        arc.setCenterY(arcCenterY);
        arc.setRadiusX(radiusX);
        arc.setRadiusY(radiusY);
        arc.setLength(-180);
        arc.setStartAngle(90);
        arc.setType(ArcType.OPEN);
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(2);
        return arc;
    }

    private void drawANDGate(NodePoint n1, NodePoint n2, boolean isNAND) {
        Circle a = null;
        Circle b = null;
        if (n1.getNode().getCenterY() > n2.getNode().getCenterY()) {
            b = n1.getNode();
            a = n2.getNode();
        } else {
            a = n1.getNode();
            b = n2.getNode();
        }
        Line vertical = null;
        Line horizontal = null;
        boolean tooFar = Math.abs(a.getCenterY() - b.getCenterY()) > 150;
        Circle right = (a.getCenterX() > b.getCenterX()) ? a : b;
        Line line1 = new Line(a.getCenterX(), a.getCenterY(), right.getCenterX() + 100, a.getCenterY());
        line1.setStrokeWidth(2);
        Line line2 = new Line(b.getCenterX(), b.getCenterY(), right.getCenterX() + 100, b.getCenterY());
        line2.setStrokeWidth(2);
        Line line3;
        if (tooFar) {
            Line up = a.getCenterY() > b.getCenterY() ? line2 : line1;
            Line down = a.getCenterY() > b.getCenterY() ? line1 : line2;
            down.setEndX(down.getEndX() - 50);
            vertical = new Line(down.getEndX(), down.getEndY(), up.getEndX() - 50, up.getEndY() + 100);
            vertical.setStrokeWidth(2);
            horizontal = new Line(vertical.getEndX(), vertical.getEndY(), vertical.getEndX() + 50, vertical.getEndY());
            horizontal.setStrokeWidth(2);
            line3 = new Line(up.getEndX(), up.getEndY() - 30, horizontal.getEndX(), horizontal.getEndY() + 30);
            line3.setStrokeWidth(2);
            vertical.setStroke(Color.RED);
            horizontal.setStroke(Color.RED);
        } else {
            line3 = new Line(line1.getEndX(), line1.getEndY() - 30, line2.getEndX(), line2.getEndY() + 30);
            line3.setStrokeWidth(2);
        }
        double arcCenterX = line3.getStartX();
        double arcCenterY = (line3.getStartY() + line3.getEndY()) / 2;
        double radius = (line3.getEndY() - line3.getStartY()) / 2;
        Arc arc;
        arc = drawD(arcCenterX, arcCenterY, radius, radius);
        Line ext = new Line(arcCenterX + radius, arcCenterY, arcCenterX + radius + 20, arcCenterY);

        Circle circle = new Circle();
        circle.setRadius(4);
        circle.setCenterX(ext.getStartX());
        circle.setCenterY(ext.getStartY());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setFill(Color.TRANSPARENT);
        Circle node = new Circle();
        Label label = new Label();
        label.setLayoutX(ext.getEndX());
        label.setLayoutY(ext.getEndY() + 10);
        char c = (char) (Nodes.size() + secondaryNodes.size() + 65);
        Boolean bool = n1.getValue() && n2.getValue();
        if (isNAND)
            bool = !bool;
        label.setText(c + ": " + bool);
        node.setCenterX(ext.getEndX());
        node.setCenterY(ext.getEndY());
        node.setRadius(4);
        node.setFill(Color.BLACK);
        pane.getChildren().addAll(node, label);
        NodePoint nodePoint = new NodePoint(node, label, bool, c);
        secondaryNodes.add(nodePoint);
        lastNode = nodePoint;
        node.setOnMouseEntered(e1 -> {
            node.setScaleX(2);
            node.setScaleY(2);
        });

        node.setOnMouseExited(e1 -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
        node.setOnMouseClicked(ev -> handleNodeClick(node));
        Group gate;
        line1.setStroke(Color.RED);
        line2.setStroke(Color.RED);
        line3.setStroke(Color.RED);
        arc.setStroke(Color.RED);
        if (isNAND) {
            if (tooFar)
                gate = new Group(line1, line2, line3, arc, ext, circle, vertical, horizontal);
            else
                gate = new Group(line1, line2, line3, arc, ext, circle);
        } else {
            if (tooFar)
                gate = new Group(line1, line2, line3, arc, ext, vertical, horizontal);
            else
                gate = new Group(line1, line2, line3, arc, ext);
        }
        pane.getChildren().add(gate);
        Gate m = null;
        if (isNAND)
            m = new Gate("NAND", n1, n2, nodePoint);
        else
            m = new Gate("AND", n1, n2, nodePoint);
        Gates.add(m);
        n1.incrementUsage();
        n2.incrementUsage();
    }

    private void drawOrGate(NodePoint n1, NodePoint n2, boolean isNOR) {
        Circle a = null;
        Circle b = null;
        if (n1.getNode().getCenterY() > n2.getNode().getCenterY()) {
            b = n1.getNode();
            a = n2.getNode();
        } else {
            a = n1.getNode();
            b = n2.getNode();
        }
        Line vertical = null;
        Line horizontal = null;
        boolean tooFar = Math.abs(a.getCenterY() - b.getCenterY()) > 150;
        Circle right = (a.getCenterX() > b.getCenterX()) ? a : b;
        Line line1 = new Line(a.getCenterX(), a.getCenterY(), right.getCenterX() + 100, a.getCenterY());
        line1.setStrokeWidth(2);
        Line line2 = new Line(b.getCenterX(), b.getCenterY(), right.getCenterX() + 100, b.getCenterY());
        line2.setStrokeWidth(2);
        Line line3;

        if (tooFar) {
            Line up = a.getCenterY() > b.getCenterY() ? line2 : line1;
            Line down = a.getCenterY() > b.getCenterY() ? line1 : line2;
            down.setEndX(down.getEndX() - 50);
            vertical = new Line(down.getEndX(), down.getEndY(), up.getEndX() - 50, up.getEndY() + 100);
            vertical.setStrokeWidth(2);
            horizontal = new Line(vertical.getEndX(), vertical.getEndY(), vertical.getEndX() + 50, vertical.getEndY());
            horizontal.setStrokeWidth(2);
            line3 = new Line(up.getEndX(), up.getEndY() - 30, horizontal.getEndX(), horizontal.getEndY() + 30);
            line3.setStrokeWidth(2);
            vertical.setStroke(Color.BLUE);
            horizontal.setStroke(Color.BLUE);
        } else {
            line3 = new Line(line1.getEndX(), line1.getEndY() - 30, line2.getEndX(), line2.getEndY() + 30);
            line3.setStrokeWidth(2);
        }
        double arcCenterX = line3.getStartX();
        double arcCenterY = (line3.getStartY() + line3.getEndY()) / 2;
        double radius = (line3.getEndY() - line3.getStartY()) / 2;
        Arc arc;
        double k = Math.sqrt(radius * radius - (radius - 30) * (radius - 30));
        Line ext = new Line(arcCenterX - k + radius + 50, arcCenterY, arcCenterX - k + radius + 70, arcCenterY);

        Circle circle = new Circle();
        circle.setRadius(4);
        circle.setCenterX(ext.getStartX());
        circle.setCenterY(ext.getStartY());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setFill(Color.TRANSPARENT);

        arc = drawD(arcCenterX - k, arcCenterY, radius, radius);
        Arc arc2;
        arc2 = drawD(arcCenterX - k, arcCenterY, radius + 50, radius);
        Circle node = new Circle();
        Label label = new Label();
        label.setLayoutX(ext.getEndX());
        label.setLayoutY(ext.getEndY() + 10);
        char c = (char) (Nodes.size() + secondaryNodes.size() + 65);
        Boolean bool = n1.getValue() || n2.getValue();
        if (isNOR)
            bool = !bool;
        label.setText(c + ": " + bool);
        node.setCenterX(ext.getEndX());
        node.setCenterY(ext.getEndY());
        node.setRadius(4);
        node.setFill(Color.BLACK);
        node.setOnMouseEntered(e1 -> {
            node.setScaleX(2);
            node.setScaleY(2);
        });

        node.setOnMouseExited(e1 -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
        node.setOnMouseClicked(ev -> handleNodeClick(node));
        pane.getChildren().addAll(node, label);
        NodePoint nodePoint = new NodePoint(node, label, bool, c);
        secondaryNodes.add(nodePoint);
        lastNode = nodePoint;
        Group gate;
        line1.setStroke(Color.BLUE);
        line2.setStroke(Color.BLUE);
        arc.setStroke(Color.BLUE);
        arc2.setStroke(Color.BLUE);
        if (isNOR) {
            if (tooFar)
                gate = new Group(line1, line2, arc, ext, arc2, circle, vertical, horizontal);
            else
                gate = new Group(line1, line2, arc, ext, arc2, circle);
        } else {
            if (tooFar)
                gate = new Group(line1, line2, arc, ext, arc2, vertical, horizontal);
            else
                gate = new Group(line1, line2, arc, ext, arc2);
        }
        pane.getChildren().add(gate);
        Gate m = null;
        if (isNOR)
            m = new Gate("NOR", n1, n2, nodePoint);
        else
            m = new Gate("OR", n1, n2, nodePoint);
        Gates.add(m);
        n1.incrementUsage();
        n2.incrementUsage();
    }

    private void drawNotGate(NodePoint n) {
        Circle a = n.getNode();
        Line line1 = new Line(a.getCenterX(), a.getCenterY(), a.getCenterX() + 100, a.getCenterY());
        line1.setStrokeWidth(2);
        double x = line1.getEndX();
        double y = line1.getEndY();
        Polygon polygon = new Polygon(x, y - 30, x, y + 30, x + 30, y);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(2);
        polygon.setFill(Color.TRANSPARENT);
        Line ext = new Line(x + 30, y, x + 50, y);
        Circle circle = new Circle();
        circle.setRadius(4);
        circle.setCenterX(x + 30);
        circle.setCenterY(y);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setFill(Color.TRANSPARENT);
        Circle node = new Circle();
        Label label = new Label();
        label.setLayoutX(x + 30);
        label.setLayoutY(y + 10);
        char c = (char) (Nodes.size() + secondaryNodes.size() + 65);
        Boolean bool = !n.getValue();
        label.setText(c + ": " + bool);
        node.setCenterX(x + 50);
        node.setCenterY(y);
        node.setRadius(4);
        node.setFill(Color.BLACK);
        node.setOnMouseEntered(e1 -> {
            node.setScaleX(2);
            node.setScaleY(2);
        });

        node.setOnMouseExited(e1 -> {
            node.setScaleX(1);
            node.setScaleY(1);
        });
        node.setOnMouseClicked(ev -> handleNodeClick(node));
        pane.getChildren().addAll(node, label);
        NodePoint nodePoint = new NodePoint(node, label, bool, c);
        secondaryNodes.add(nodePoint);
        lastNode = nodePoint;
        line1.setStroke(Color.GREEN);
        polygon.setStroke(Color.GREEN);
        Group gate = new Group(line1, polygon, circle, ext);
        pane.getChildren().addAll(gate);
        Gate m = new Gate("NOT", n, null, nodePoint);
        Gates.add(m);
        n.incrementUsage();
    }

    @FXML
    private void onANDGate() {
        if (currentMode == ToolMode.AND_GATE) {
            AndButton.getStyleClass().remove("button-33");
            AndButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE) {
            return;
        }
        currentMode = ToolMode.AND_GATE;
        selectedNodes.clear();
        AndButton.getStyleClass().remove("button-74");
        AndButton.getStyleClass().add("button-33");
    }

    @FXML
    private void onORGate() {
        if (currentMode == ToolMode.OR_GATE) {
            orButton.getStyleClass().remove("button-33");
            orButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE) {
            return;
        }
        currentMode = ToolMode.OR_GATE;
        selectedNodes.clear();
        orButton.getStyleClass().remove("button-74");
        orButton.getStyleClass().add("button-33");
    }

    @FXML
    private void onNotGate() {
        if (currentMode == ToolMode.NOT_GATE) {
            notButton.getStyleClass().remove("button-33");
            notButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE) {
            return;
        }
        currentMode = ToolMode.NOT_GATE;
        selectedNodes.clear();
        notButton.getStyleClass().remove("button-74");
        notButton.getStyleClass().add("button-33");
    }

    @FXML
    private void onNandGate() {
        if (currentMode == ToolMode.NAND_GATE) {
            nandButton.getStyleClass().remove("button-33");
            nandButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE) {
            return;
        }
        currentMode = ToolMode.NAND_GATE;
        selectedNodes.clear();
        nandButton.getStyleClass().remove("button-74");
        nandButton.getStyleClass().add("button-33");
    }

    @FXML
    private void onNorGate() {
        if (currentMode == ToolMode.NOR_GATE) {
            norButton.getStyleClass().remove("button-33");
            norButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE) {
            return;
        }
        currentMode = ToolMode.NOR_GATE;
        selectedNodes.clear();
        norButton.getStyleClass().remove("button-74");
        norButton.getStyleClass().add("button-33");
    }

    private void handleNodeClick(Circle node) {

        if (currentMode == ToolMode.NONE) return;

        NodePoint clickedNode = null;
        for (NodePoint np : Nodes) {
            if (np.getNode() == node) {
                clickedNode = np;
                break;
            }
        }
        if (currentMode != ToolMode.TOGGLE) {
            for (NodePoint np : secondaryNodes) {
                if (np.getNode() == node) {
                    clickedNode = np;
                    break;
                }
            }
        }
        if (clickedNode == null) return;

        selectedNodes.add(clickedNode);
        node.setFill(Color.DEEPSKYBLUE);

        if (currentMode == ToolMode.AND_GATE && selectedNodes.size() == 2) {
            drawANDGate(selectedNodes.get(0), selectedNodes.get(1), false);
            AndButton.getStyleClass().remove("button-33");
            AndButton.getStyleClass().add("button-74");
        } else if (currentMode == ToolMode.NAND_GATE && selectedNodes.size() == 2) {
            drawANDGate(selectedNodes.get(0), selectedNodes.get(1), true);
            nandButton.getStyleClass().remove("button-33");
            nandButton.getStyleClass().add("button-74");
        } else if (currentMode == ToolMode.OR_GATE && selectedNodes.size() == 2) {
            drawOrGate(selectedNodes.get(0), selectedNodes.get(1), false);
            orButton.getStyleClass().remove("button-33");
            orButton.getStyleClass().add("button-74");
        } else if (currentMode == ToolMode.NOR_GATE && selectedNodes.size() == 2) {
            drawOrGate(selectedNodes.get(0), selectedNodes.get(1), true);
            norButton.getStyleClass().remove("button-33");
            norButton.getStyleClass().add("button-74");
        } else if (currentMode == ToolMode.NOT_GATE && selectedNodes.size() == 1) {
            drawNotGate(selectedNodes.get(0));
            notButton.getStyleClass().remove("button-33");
            notButton.getStyleClass().add("button-74");
        } else if (currentMode == ToolMode.TOGGLE && selectedNodes.size() == 1) {
            NodePoint nodePoint = selectedNodes.get(0);
            nodePoint.ToggleValue();
            nodePoint.getLabel().setText(nodePoint.getCharacter() + ": " + nodePoint.getValue());
            ToggleButton.getStyleClass().remove("button-33");
            ToggleButton.getStyleClass().add("button-74");
            for (Gate gate : Gates)
                gate.compute();
            onConnect();
        } else
            return;

        for (NodePoint np : selectedNodes)
            np.getNode().setFill(Color.BLACK);

        selectedNodes.clear();
        currentMode = ToolMode.NONE;
    }

    @FXML
    private void onToggle() {
        if (currentMode == ToolMode.TOGGLE) {
            ToggleButton.getStyleClass().remove("button-33");
            ToggleButton.getStyleClass().add("button-74");
            currentMode = ToolMode.NONE;
            return;
        } else if (currentMode != ToolMode.NONE)
            return;
        currentMode = ToolMode.TOGGLE;
        selectedNodes.clear();
        ToggleButton.getStyleClass().remove("button-74");
        ToggleButton.getStyleClass().add("button-33");
    }

    @FXML
    private void onReset() {

        pane.getChildren().removeIf(node ->
                !(node == bulbView) && !(node == BackButton)
        );
        bulbView.setVisible(false);

        selectedNodes.clear();
        Nodes.clear();
        secondaryNodes.clear();
        Gates.clear();
        isUsedNode.clear();
        lastNode = null;

    }

    @FXML
    private void onConnect() {
        if (lastNode == null) return;

        boolean value = lastNode.getValue();

        bulbView.setImage(value ? bulbOn : bulbOff);

        bulbView.setLayoutX(lastNode.getNode().getCenterX() + 50);
        bulbView.setLayoutY(lastNode.getNode().getCenterY() - 10);

        bulbView.setVisible(true);

    }

}