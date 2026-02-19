
package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MultiMoleculeController implements Initializable {

    @FXML
    private VBox mainpanel;
    @FXML
    private VBox instruction;
    @FXML
    private void onBackButtonClicked(ActionEvent event) {
        try {
            // Load FXML correctly
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
    // ── 3D ───────────────────────────────────────────────────────────────────
    private Group moleculeGroup = new Group();
    private SubScene subScene;
    private PerspectiveCamera camera;
    private final Rotate rotX = new Rotate(25, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(-30, Rotate.Y_AXIS);
    private double anchorX, anchorY, anchorAngleX, anchorAngleY;

    // ── State ─────────────────────────────────────────────────────────────────
    private MoleculePresets.PresetInfo current = null;

    // ── Info labels (built in buildRightPanel, updated in updateInfo) ─────────
    private Label lblName = new Label("—");
    private Label lblFormula = new Label("—");
    private Label lblShape = new Label("—");
    private Label lblAngles = new Label("—");
    private Label lblLP = new Label("—");
    private Label lblTop = new Label("");   // top bar of 3D view

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        build3DPanel();
        buildRightPanel();
        loadPreset(MoleculePresets.all().get(0)); // default: Water
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  LEFT — 3D VIEW
    // ═════════════════════════════════════════════════════════════════════════

    private void build3DPanel() {
        mainpanel.setStyle("-fx-background-color: #1a1a1a;");

        // ── top bar ──────────────────────────────────────────────────────────
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 16, 0, 16));
        topBar.setPrefHeight(36);
        topBar.setStyle("-fx-background-color: #0d1a0d; -fx-background-radius: 10 10 0 0;");

        Label lbl = new Label("3D Molecular Model");
        lbl.setStyle("-fx-text-fill: #66ee88; -fx-font-size: 13px; -fx-font-weight: bold;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        lblTop.setStyle("-fx-text-fill: #aaaacc; -fx-font-size: 12px;");

        Label hint = new Label("Drag = rotate   Scroll = zoom");
        hint.setStyle("-fx-text-fill: #2a5a2a; -fx-font-size: 10px;");
        hint.setPadding(new Insets(0, 0, 0, 16));

        topBar.getChildren().addAll(lbl, sp, lblTop, hint);

        // ── SubScene ─────────────────────────────────────────────────────────
        moleculeGroup = new Group();
        moleculeGroup.getTransforms().addAll(rotX, rotY);

        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-650);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);

        AmbientLight ambient = new AmbientLight(Color.web("#555555"));
        PointLight key = new PointLight(Color.WHITE);
        key.setTranslateX(400);
        key.setTranslateY(-500);
        key.setTranslateZ(-400);
        PointLight fill = new PointLight(Color.web("#223355", 0.5));
        fill.setTranslateX(-300);
        fill.setTranslateY(300);
        fill.setTranslateZ(300);

        Group sceneRoot = new Group(ambient, key, fill, moleculeGroup);
        subScene = new SubScene(sceneRoot, 10, 10, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#060608"));
        subScene.setCamera(camera);

        StackPane viewport = new StackPane(subScene);
        viewport.setStyle("-fx-background-color: #060608;");
        VBox.setVgrow(viewport, Priority.ALWAYS);

        // Resize subScene when container resizes
        viewport.layoutBoundsProperty().addListener((obs, o, n) -> {
            if (n.getWidth() > 1 && n.getHeight() > 1) {
                subScene.setWidth(n.getWidth());
                subScene.setHeight(n.getHeight());
            }
        });

        // Mouse rotate
        viewport.setOnMousePressed(e -> {
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
            anchorAngleX = rotX.getAngle();
            anchorAngleY = rotY.getAngle();
        });
        viewport.setOnMouseDragged(e -> {
            rotX.setAngle(anchorAngleX - (e.getSceneY() - anchorY) * 0.4);
            rotY.setAngle(anchorAngleY + (e.getSceneX() - anchorX) * 0.4);
        });
        // Scroll zoom
        viewport.setOnScroll(e ->
                camera.setTranslateZ(Math.min(-80, camera.getTranslateZ() + e.getDeltaY() * 1.2))
        );

        // ── toolbar (just reset view) ─────────────────────────────────────────
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(8, 12, 8, 12));

        Button reset = btn("⟳  Reset View", "#222244");
        reset.setOnAction(e -> {
            rotX.setAngle(25);
            rotY.setAngle(-30);
            camera.setTranslateZ(-650);
        });
        toolbar.getChildren().add(reset);

        // ── outer panel ───────────────────────────────────────────────────────
        VBox panel = new VBox(topBar, viewport);
        VBox.setVgrow(panel, Priority.ALWAYS);
        panel.setStyle("-fx-background-color: #060608; -fx-background-radius: 10;" +
                "-fx-border-color: #1a2a1a; -fx-border-radius: 10; -fx-border-width: 1;");

        mainpanel.getChildren().addAll(toolbar, panel);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  RIGHT — PRESETS + INFO
    // ═════════════════════════════════════════════════════════════════════════

    private void buildRightPanel() {
        instruction.setStyle("-fx-background-color: #1a1a1a;");
        VBox.setVgrow(instruction, Priority.ALWAYS);

        // Single scroll pane covering both sections
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #1e1e2e; -fx-background-color: #1e1e2e;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox content = new VBox(16);
        content.setPadding(new Insets(14));
        content.setStyle("-fx-background-color: #1e1e2e;");

        // ── Preset buttons by category ────────────────────────────────────────
        Map<String, List<MoleculePresets.PresetInfo>> grouped = MoleculePresets.byCategory();

        for (Map.Entry<String, List<MoleculePresets.PresetInfo>> entry : grouped.entrySet()) {
            // Category header
            Label catLbl = new Label(entry.getKey().toUpperCase());
            catLbl.setStyle("-fx-text-fill: #8888cc; -fx-font-size: 11px;" +
                    "-fx-font-weight: bold; -fx-padding: 4 0 2 0;");
            content.getChildren().add(catLbl);

            // Molecule buttons
            FlowPane flow = new FlowPane(8, 8);
            for (MoleculePresets.PresetInfo p : entry.getValue()) {
                Button b = presetBtn(p.name());
                b.setOnAction(e -> loadPreset(p));
                flow.getChildren().add(b);
            }
            content.getChildren().add(flow);

            Separator sep = new Separator();
            content.getChildren().add(sep);
        }

        // ── Info section ──────────────────────────────────────────────────────
        Label infoHeader = new Label("MOLECULE INFO");
        infoHeader.setStyle("-fx-text-fill: #8888cc; -fx-font-size: 11px; -fx-font-weight: bold;");
        content.getChildren().add(infoHeader);

        VBox infoBox = new VBox(7);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #141428; -fx-background-radius: 10;" +
                "-fx-border-color: #2a2a4a; -fx-border-radius: 10; -fx-border-width: 1;");

        infoBox.getChildren().addAll(
                infoRow("Name:", lblName),
                infoRow("Formula:", lblFormula),
                infoRow("Shape:", lblShape),
                infoRow("Bond Angles:", lblAngles),
                infoRow("Lone Pairs:", lblLP)
        );
        styleInfoLabels();
        content.getChildren().add(infoBox);

        // ── CPK legend ────────────────────────────────────────────────────────
        Label legendHeader = new Label("CPK COLORS");
        legendHeader.setStyle("-fx-text-fill: #8888cc; -fx-font-size: 11px; -fx-font-weight: bold;");
        content.getChildren().addAll(legendHeader, buildWasif());

        scroll.setContent(content);
        instruction.getChildren().add(scroll);
    }

    private HBox infoRow(String key, Label val) {
        Label k = new Label(key);
        k.setMinWidth(90);
        k.setStyle("-fx-text-fill: #5555aa; -fx-font-size: 11px;");
        HBox row = new HBox(8, k, val);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void styleInfoLabels() {
        for (Label l : new Label[]{lblName, lblFormula, lblShape, lblAngles, lblLP}) {
            l.setStyle("-fx-text-fill: #ddddff; -fx-font-size: 12px;");
            l.setWrapText(true);
        }
    }

    private FlowPane buildWasif() {
        FlowPane flow = new FlowPane(8, 5);
        String[][] items = {
                {"H", "White"}, {"C", "Gray"}, {"N", "Blue"}, {"O", "Red"},
                {"F", "Lt.Green"}, {"Cl", "Green"}, {"Br", "Dk.Red"},
                {"S", "Yellow"}, {"P", "Orange"}, {"I", "Purple"},
                {"B", "Pink"}, {"Si", "Tan"}, {"Xe", "Teal"}
        };
        for (String[] it : items) {
            HBox box = new HBox(4);
            box.setAlignment(Pos.CENTER_LEFT);
            Rectangle r = new Rectangle(10, 10, Renderer3D.cpk(it[0]));
            r.setArcWidth(3);
            r.setArcHeight(3);
            Label l = new Label(it[0] + " = " + it[1]);
            l.setStyle("-fx-text-fill: #7777aa; -fx-font-size: 9px;");
            box.getChildren().addAll(r, l);
            flow.getChildren().add(box);
        }
        return flow;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  LOAD & RENDER
    // ═════════════════════════════════════════════════════════════════════════

    private void loadPreset(MoleculePresets.PresetInfo preset) {
        current = preset;
        CoordBuilder.build(preset.graph(), preset.name());
        boolean showLP = MoleculePresets.CAT_INORGANIC.equals(preset.category());
        Group scene = Renderer3D.build(preset.graph(), showLP);
        moleculeGroup.getChildren().setAll(scene.getChildren());
        updateInfo();
    }

    private void updateInfo() {
        if (current == null) return;

        lblName.setText(current.name());
        lblFormula.setText(current.formula());
        lblShape.setText(current.shape());
        lblAngles.setText(current.angles());

        int totalLP = current.graph().getAtoms().stream()
                .mapToInt(a -> a.lonePairs).sum();
        lblLP.setText(totalLP + " lone pair" + (totalLP != 1 ? "s" : ""));

        lblTop.setText(current.name() + "   " + current.formula() +
                "   |   " + current.shape() +
                "   |   " + current.angles());
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private Button presetBtn(String name) {
        Button b = new Button(name);
        String base = "-fx-background-color:#1e1e3a;-fx-text-fill:#ccccee;" +
                "-fx-background-radius:8;-fx-border-color:#3333660;" +
                "-fx-border-radius:8;-fx-cursor:hand;" +
                "-fx-font-size:11px;-fx-padding:6 12;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color:#3333aa;-fx-text-fill:white;" +
                        "-fx-background-radius:8;-fx-border-color:#5555cc;" +
                        "-fx-border-radius:8;-fx-cursor:hand;" +
                        "-fx-font-size:11px;-fx-padding:6 12;"));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    private Button btn(String text, String bg) {
        Button b = new Button(text);
        String s = "-fx-background-color:" + bg + ";-fx-text-fill:white;" +
                "-fx-background-radius:8;-fx-cursor:hand;" +
                "-fx-font-size:11px;-fx-padding:5 14;";
        b.setStyle(s);
        return b;
    }
}