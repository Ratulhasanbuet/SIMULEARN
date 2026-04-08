package com.example.simulearn.SimuLearn.Math.Euler;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class EulerController {
    @FXML
    private Label warning;
    @FXML
    private Button addNodeButton;
    @FXML
    private Button addEdgeButton;
    @FXML
    private Button backButton;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane faceLayer;
    @FXML
    private Label f;
    @FXML
    private Label e;
    @FXML
    private Label v;
    @FXML
    private Label connectedComponents;
    private ArrayList<Circle> selectedNodes = new ArrayList<>();
    private ArrayList<Circle> Nodes = new ArrayList<>();
    private ArrayList<Line> Edges = new ArrayList<>();

    private ArrayList<Polygon> Faces = new ArrayList<>();

    private enum ToolMode {NONE, ADD_NODE, ADD_EDGE}

    private ToolMode currentMode = ToolMode.NONE;
    ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
    private int numConnectedComp = 0;

    @FXML
    void onBackButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/mathMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    private void initialize() {
        v.setText(" " + Nodes.size());
        e.setText(" " + Edges.size());

        int k = Faces.size() + 1;
        f.setText(" " + k);
        connectedComponents.setText(" " + numConnectedComp);
        warning.setVisible(false);
    }

    @FXML
    private void onAddNode() {
        if (currentMode == ToolMode.ADD_NODE) {
            currentMode = ToolMode.NONE;
            addNodeButton.getStyleClass().remove("button-33");
            addNodeButton.getStyleClass().add("button-74");
            addNodeButton.setText("Add vertex");
            return;
        }
        if (currentMode != ToolMode.NONE)
            return;
        currentMode = ToolMode.ADD_NODE;
        addNodeButton.getStyleClass().remove("button-74");
        addNodeButton.getStyleClass().add("button-33");
        addNodeButton.setText("Adding Vertex");
    }

    @FXML
    void NodeAddition(MouseEvent e) {
        if (currentMode == ToolMode.ADD_NODE) {
            AddNode(e);
            addNodeButton.getStyleClass().remove("button-33");
            addNodeButton.getStyleClass().add("button-74");
            addNodeButton.setText("Add vertex");
        }
    }

    @FXML
    private void onAddEdge() {
        if (currentMode == ToolMode.ADD_EDGE) {
            currentMode = ToolMode.NONE;
            addEdgeButton.getStyleClass().remove("button-33");
            addEdgeButton.getStyleClass().add("button-74");
            addEdgeButton.setText("Add Edge");
            return;
        }
        if (currentMode != ToolMode.NONE)
            return;
        currentMode = ToolMode.ADD_EDGE;
        addEdgeButton.getStyleClass().remove("button-74");
        addEdgeButton.getStyleClass().add("button-33");
        addEdgeButton.setText("Adding Edge");
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
        node.setCenterX(e.getX());
        node.setCenterY(e.getY());
        node.setRadius(4);
        node.setFill(Color.BLACK);
        pane.getChildren().addAll(node);
        Nodes.add(node);

        adj.add(new ArrayList<>());

        numConnectedComp++;

        initialize();
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

    private boolean linesIntersect(double x1, double y1, double x2, double y2,
                                   double x3, double y3, double x4, double y4) {
        double det = (x2 - x1) * (y4 - y3) - (y2 - y1) * (x4 - x3);
        if (det == 0) return false;
        double t = ((x3 - x1) * (y4 - y3) - (y3 - y1) * (x4 - x3)) / det;
        double u = ((x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1)) / det;
        return t > 0 && t < 1 && u > 0 && u < 1;
    }

    private boolean isIntersecting(Line a, Line b) {

        if (a.getStartX() == b.getStartX() && a.getStartY() == b.getStartY()) return false;
        if (a.getStartX() == b.getEndX() && a.getStartY() == b.getEndY()) return false;
        if (a.getEndX() == b.getStartX() && a.getEndY() == b.getStartY()) return false;
        if (a.getEndX() == b.getEndX() && a.getEndY() == b.getEndY()) return false;

        return linesIntersect(a.getStartX(), a.getStartY(), a.getEndX(), a.getEndY(),
                b.getStartX(), b.getStartY(), b.getEndX(), b.getEndY());
    }

    private static boolean isEndpoint(double ix, double iy, double ex, double ey) {
        double epsilon = 8;
        return Math.abs(ix - ex) < epsilon && Math.abs(iy - ey) < epsilon;
    }

    private boolean isValidEdge(Line a) {
        for (Line line : Edges) {
            boolean sameEndpoints =
                    (line.getStartX() == a.getStartX() && line.getStartY() == a.getStartY() &&
                            line.getEndX() == a.getEndX() && line.getEndY() == a.getEndY()) ||
                            (line.getStartX() == a.getEndX() && line.getStartY() == a.getEndY() &&
                                    line.getEndX() == a.getStartX() && line.getEndY() == a.getStartY());

            if (sameEndpoints) {
                return false;
            }
            if (isIntersecting(line, a)) {
                return false;
            }
        }
        return true;
    }

    private void updateConnectedComponent() {
        int temp = 0;
        Queue<Integer> bfs = new LinkedList<>();
        boolean[] visited = new boolean[Nodes.size()];
        for (int i = 0; i < Nodes.size(); i++) {
            if (visited[i])
                continue;
            visited[i] = true;
            bfs.add(i);
            while (!bfs.isEmpty()) {
                int r = bfs.poll();
                for (int l : adj.get(r)) {
                    if (!visited[l]) {
                        visited[l] = true;
                        bfs.add(l);
                    }
                }
            }
            temp++;
        }
        numConnectedComp = temp;
    }

    private boolean contains(Polygon outer, Polygon inner) {
        Shape inter = Shape.intersect(inner, outer);
        Bounds b = inter.getBoundsInLocal();
        return !(b == null || b.isEmpty());
    }

    private void drawFace() {
        faceLayer.getChildren().clear();
        for (Polygon polygon : Faces) {
            faceLayer.getChildren().add(polygon);
        }
    }

    private void updatePolygon(Polygon polygon) {
        if (Faces.isEmpty() || contains(Faces.getLast(), polygon)) {
            Faces.add(polygon);
            drawFace();
            return;
        }
        int idx;
        for (idx = Faces.size() - 1; idx >= 0; idx--) {
            if (contains(Faces.get(idx), polygon))
                break;
        }
        idx++;
        Faces.add(idx, polygon);
        drawFace();
    }

    private void checkCycle(int idx1, int idx2) {
        boolean[] visited = new boolean[Nodes.size()];
        int[] parent = new int[Nodes.size()];
        int i;
        for (i = 0; i < Nodes.size(); i++)
            parent[i] = -1;
        Queue<Integer> bfs = new LinkedList<>();
        visited[idx1] = true;
        bfs.add(idx1);
        while (!bfs.isEmpty()) {
            int r = bfs.poll();
            if (r == idx2)
                break;
            for (int l : adj.get(r)) {
                if (!visited[l]) {
                    parent[l] = r;
                    visited[l] = true;
                    bfs.add(l);
                }
            }
        }

        if (!visited[idx2])
            return;
        ArrayList<Integer> path = new ArrayList<>();
        for (int v = idx2; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);

        Polygon polygon = new Polygon();
        for (int v : path) {
            Circle p = Nodes.get(v);
            polygon.getPoints().addAll((p.getCenterX()), p.getCenterY());
        }
        Random rand = new Random();
        Color randomColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
        polygon.setFill(randomColor);

        updatePolygon(polygon);
    }

    private void handleNodeClick(Circle node) {

        if (currentMode == ToolMode.NONE) return;

        Circle clickedNode = null;
        for (Circle np : Nodes) {
            if (np == node) {
                clickedNode = np;
                break;
            }
        }
        if (clickedNode == null) return;

        selectedNodes.add(clickedNode);
        node.setFill(Color.DEEPSKYBLUE);

        if (currentMode == ToolMode.ADD_EDGE && selectedNodes.size() == 2) {
            Circle a = selectedNodes.get(0);
            Circle b = selectedNodes.get(1);
            Line line = new Line(a.getCenterX(), a.getCenterY(), b.getCenterX(), b.getCenterY());
            line.setStrokeWidth(2);
            if (isValidEdge(line)) {
                int idx1 = 0;
                int idx2 = 0;
                for (int i = 0; i < Nodes.size(); i++) {
                    if (a == Nodes.get(i))
                        idx1 = i;
                    else if (b == Nodes.get(i))
                        idx2 = i;
                }

                try {
                    checkCycle(idx1, idx2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adj.get(idx1).add(idx2);
                adj.get(idx2).add(idx1);
                pane.getChildren().add(line);
                Edges.add(line);

                updateConnectedComponent();
                initialize();

            } else {
                warning.setText("Cannot add multiple edges between same node or add edges" + '\n' + "overlapping with previous edges to preserve planar graph");
                warning.setVisible(true);
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> warning.setVisible(false));
                pause.play();
            }
            addEdgeButton.getStyleClass().remove("button-33");
            addEdgeButton.getStyleClass().add("button-74");
            addEdgeButton.setText("Add Edge");
        } else
            return;

        for (Circle np : selectedNodes)
            np.setFill(Color.BLACK);

        selectedNodes.clear();
        currentMode = ToolMode.NONE;
    }
}