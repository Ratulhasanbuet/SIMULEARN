package com.example.simulearn;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static com.example.simulearn.PhysicsConstants.*;
import static com.example.simulearn.PhysicsConstants.STEPS_PER_FRAME;

public class ProjectileController {

    // ── FXML injections ────────────────────────────────────────────────────

    @FXML
    void onBackButtonClicked() throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/physicsMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }
    @FXML
    private StackPane canvasWrap;
    @FXML
    private Canvas simCanvas;
    @FXML
    private Canvas figureCanvas;

    // Header
    @FXML
    private Label headerIcon, headerSub;
    @FXML
    private Label lblStatus, lblScore, lblAttempts;

    // Mode tabs
    @FXML
    private ToggleButton btnBasketball, btnBaseball, btnHockey;

    // Sliders + value labels
    @FXML
    private Slider slSpeed, slAngle, slDist, slPlayerH, slCd;
    @FXML
    private Label lblSpeed, lblAngle, lblDist, lblPlayerH, lblCd;

    // Physics checkboxes
    @FXML
    private CheckBox chkAir, chkTrail, chkDims, chkVec;

    // Telemetry labels
    @FXML
    private Label tSpeed, tHeight, tDist, tTime, tDrag, tAngle;

    // ── Model ────────────────────────────────────────────────────────────
    private final SimState state = new SimState();
    private final PhysicsEngine engine = new PhysicsEngine(state);
    private SceneRenderer renderer;
    private ProjectileMode currentMode = ProjectileMode.BASKETBALL;

    // ── Called by App after scene is ready ──────────────────────────────
    public void onSceneReady(Scene scene, Stage stage) {
        renderer = new SceneRenderer(simCanvas, state);

        // Bind canvas size to its parent
        simCanvas.widthProperty().bind(canvasWrap.widthProperty());
        simCanvas.heightProperty().bind(canvasWrap.heightProperty());
        simCanvas.widthProperty().addListener(e -> recalcAndDraw());
        simCanvas.heightProperty().addListener(e -> recalcAndDraw());

        // Keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) onLaunch();
            if (e.getCode() == KeyCode.R) onReset();
        });

        // Wire slider value listeners
        slSpeed.valueProperty().addListener((o, ov, nv) -> {
            lblSpeed.setText(f1(nv) + " ft/s");
            recalcAndDraw();
        });
        slAngle.valueProperty().addListener((o, ov, nv) -> {
            lblAngle.setText(f1(nv) + "°");
            recalcAndDraw();
            drawFigure();
        });
        slDist.valueProperty().addListener((o, ov, nv) -> {
            lblDist.setText(f1(nv) + " ft");
            recalcAndDraw();
        });
        slPlayerH.valueProperty().addListener((o, ov, nv) -> {
            lblPlayerH.setText(f1(nv) + " ft");
            recalcAndDraw();
            drawFigure();
        });
        slCd.valueProperty().addListener((o, ov, nv) -> lblCd.setText(String.format("%.2f", nv.doubleValue())));

        // Apply initial mode defaults
        applyMode(ProjectileMode.BASKETBALL);

        // Start animation loop
        startLoop();
    }

    // ── FXML event handlers ───────────────────────────────────────────────

    @FXML
    private void onSliderChange() {
        recalcAndDraw();
    }

    @FXML
    private void onToggleTrail() {
        renderer.showTrail = chkTrail.isSelected();
    }

    @FXML
    private void onToggleDims() {
        renderer.showDims = chkDims.isSelected();
    }

    @FXML
    private void onToggleVec() {
        renderer.showVectors = chkVec.isSelected();
    }

    @FXML
    private void onModeBasketball() {
        applyMode(ProjectileMode.BASKETBALL);
    }

    @FXML
    private void onModeBaseball() {
        applyMode(ProjectileMode.BASEBALL);
    }

    @FXML
    private void onModeHockey() {
        applyMode(ProjectileMode.HOCKEY);
    }

    @FXML
    public void onLaunch() {
        if (state.running) return;
        updateWorld();
        double v0 = slSpeed.getValue(), theta = Math.toRadians(slAngle.getValue());
        state.x = state.launchX;
        state.y = state.launchY;
        state.vx = v0 * Math.cos(Math.PI - theta);
        state.vy = v0 * Math.sin(theta);
        state.t = 0;
        state.running = true;
        state.landed = false;
        state.scored = false;
        state.trail.clear();
        state.attempts++;
        lblAttempts.setText("ATTEMPTS: " + state.attempts);
        setStatus("IN FLIGHT", "#f97316", "#431407");
    }

    @FXML
    public void onReset() {
        state.reset();
        state.x = state.launchX;
        state.y = state.launchY;
        setStatus("READY", "#38bdf8", "#0c2340");
    }

    // ── Mode switching ────────────────────────────────────────────────────
    private void applyMode(ProjectileMode m) {
        currentMode = m;
        state.mode = m;

        // Tab styles
        btnBasketball.setStyle(modeTabStyle(m == ProjectileMode.BASKETBALL));
        btnBaseball.setStyle(modeTabStyle(m == ProjectileMode.BASEBALL));
        btnHockey.setStyle(modeTabStyle(m == ProjectileMode.HOCKEY));

        btnBasketball.setSelected(m == ProjectileMode.BASKETBALL);
        btnBaseball.setSelected(m == ProjectileMode.BASEBALL);
        btnHockey.setSelected(m == ProjectileMode.HOCKEY);

        // Header
        headerIcon.setText(m.icon);
        headerSub.setText(m.label + " · Air Resistance · 2D Physics");

        // Slider defaults (suppress listener re-entry with a flag guard handled by updateWorld)
        slSpeed.setValue(m.defaultSpeed);
        slAngle.setValue(m.defaultAngle);
        slDist.setValue(m.defaultDist);
        slPlayerH.setValue(m.defaultLaunchH);

        updateWorld();
        onReset();
        drawFigure();
    }

    // ── World geometry update ─────────────────────────────────────────────
    private void updateWorld() {
        double dist = slDist.getValue();
        state.mode = currentMode;
        state.launchX = 0;
        state.launchY = slPlayerH.getValue();
        state.hoopDist = dist;
        state.hoopX = -dist;
        state.hoopY = currentMode.targetH;
        state.targetH = currentMode.targetH;
        state.targetW = currentMode.targetW;
        state.targetDepth = currentMode.targetDepth;
        renderer.recalc(dist);
        if (!state.running) {
            state.x = state.launchX;
            state.y = state.launchY;
        }
    }

    private void recalcAndDraw() {
        updateWorld();
        renderer.draw(slAngle.getValue());
    }

    // ── Animation loop ────────────────────────────────────────────────────
    private void startLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 0; i < STEPS_PER_FRAME; i++) {
                    PhysicsEngine.SimEvent ev = engine.step(
                            chkAir.isSelected(), slCd.getValue(), chkTrail.isSelected());
                    if (ev == PhysicsEngine.SimEvent.SCORE) {
                        renderer.showResult(true);
                        lblScore.setText("SCORE: " + state.score);
                        setStatus("SCORED!", "#22c55e", "#14532d");
                    } else if (ev == PhysicsEngine.SimEvent.MISS) {
                        renderer.showResult(false);
                        setStatus("MISSED", "#f97316", "#431407");
                    }
                }
                updateTelemetry();
                renderer.draw(slAngle.getValue());
            }
        }.start();
    }

    // ── Telemetry update ──────────────────────────────────────────────────
    private void updateTelemetry() {
        tSpeed.setText(state.running ? String.format("%.1f ft/s", state.telSpeed) : "—");
        tHeight.setText(String.format("%.2f ft", state.telHeight));
        tDist.setText(state.running ? String.format("%.2f ft", state.telDist) : "—");
        tTime.setText(state.running ? String.format("%.3f s", state.telTime) : "—");
        tDrag.setText(chkAir.isSelected() && state.running
                ? String.format("%.3f lb", state.telDrag) : "OFF");
        tAngle.setText(state.running && state.telSpeed > 0.5
                ? String.format("%.1f°", state.telAngle) : "—");
    }

    // ── Player figure mini-canvas ─────────────────────────────────────────
    private void drawFigure() {
        double h = slPlayerH == null ? 6.8 : slPlayerH.getValue();
        double minH = 0.1, maxH = 8.0;
        double t = (h - minH) / (maxH - minH);

        GraphicsContext g = figureCanvas.getGraphicsContext2D();
        double W = figureCanvas.getWidth(), H = figureCanvas.getHeight();

        g.setFill(Color.web("#0d1520"));
        g.fillRect(0, 0, W, H);
        double ground = H - 12;
        g.setStroke(Color.web("#1e2d40"));
        g.setLineWidth(1);
        g.strokeLine(10, ground, W - 10, ground);

        double figH = 40 + t * 50;
        double cx = W / 2, feet = ground, head = feet - figH;

        g.setFill(Color.web("#00000055"));
        g.fillOval(cx - 10, feet - 3, 20, 6);

        double hip = feet - figH * 0.35, shoulder = feet - figH * 0.72;

        // Legs
        g.setStroke(Color.web("#1e3a5f"));
        g.setLineWidth(4);
        g.setLineCap(StrokeLineCap.ROUND);
        g.strokeLine(cx, hip, cx - 7, feet);
        g.strokeLine(cx, hip, cx + 6, feet);

        // Sport-specific torso colour
        String torsoColor = switch (currentMode) {
            case BASKETBALL -> "#dc2626";
            case BASEBALL -> "#dc2626";
            case HOCKEY -> "#f97316";
        };
        g.setFill(Color.web(torsoColor));
        g.fillRoundRect(cx - 8, shoulder, 16, hip - shoulder, 4, 4);

        // Jersey label
        String jerseyNum = switch (currentMode) {
            case BASKETBALL -> "23";
            case BASEBALL -> "P";
            case HOCKEY -> "99";
        };
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(jerseyNum, cx, shoulder + (hip - shoulder) * .6);

        // Raised arm
        double armAngle = -Math.toRadians(slAngle == null ? 45 : slAngle.getValue());
        double armLen = figH * 0.28;
        g.setStroke(Color.web("#d4a76a"));
        g.setLineWidth(3);
        g.strokeLine(cx - 4, shoulder + 4, cx - 4 + armLen * Math.cos(Math.PI + armAngle * .7),
                shoulder + 4 + armLen * Math.sin(armAngle * .7));
        // Other arm
        g.strokeLine(cx + 4, shoulder + 4, cx + 10, shoulder + 20);

        // Head
        double headR = figH * 0.1;
        g.setFill(Color.web("#d4a76a"));
        g.fillOval(cx - headR, head, headR * 2, headR * 2);

        // Hockey helmet
        if (currentMode == ProjectileMode.HOCKEY) {
            g.setFill(Color.web("#1e1e1e"));
            g.fillArc(cx - headR, head - headR * .3, headR * 2, headR * 2, 0, 200, ArcType.CHORD);
        }
        // Baseball cap
        if (currentMode == ProjectileMode.BASEBALL) {
            g.setFill(Color.web("#1e3a5f"));
            g.fillArc(cx - headR, head - headR * .2, headR * 2, headR * 2, 0, 180, ArcType.CHORD);
        }

        g.setLineCap(StrokeLineCap.BUTT);

        // Height annotation
        double lineX = cx + 26;
        g.setStroke(Color.web("#38bdf8"));
        g.setLineWidth(1);
        g.setLineDashes(3, 3);
        g.strokeLine(lineX, feet, lineX, head);
        g.setLineDashes(null);
        g.setFill(Color.web("#38bdf8"));
        g.fillPolygon(new double[]{lineX - 4, lineX + 4, lineX}, new double[]{feet - 7, feet - 7, feet}, 3);
        g.fillPolygon(new double[]{lineX - 4, lineX + 4, lineX}, new double[]{head + 7, head + 7, head}, 3);
        g.setFont(Font.font("Courier New", 10));
        g.setTextAlign(TextAlignment.LEFT);
        g.fillText(String.format("%.1f ft", h), lineX + 6, (feet + head) / 2 + 4);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private String f1(Number n) {
        return String.format("%.1f", n.doubleValue());
    }

    private void setStatus(String text, String fg, String bg) {
        lblStatus.setText(text);
        lblStatus.setStyle(String.format(
                "-fx-font-family:'Courier New';-fx-font-size:11;-fx-padding:3 10 3 10;" +
                        "-fx-background-color:%s;-fx-text-fill:%s;-fx-border-color:%s;" +
                        "-fx-border-width:1;-fx-background-radius:4;-fx-border-radius:4;", bg, fg, fg));
    }

    private String modeTabStyle(boolean active) {
        if (active)
            return "-fx-background-color:#0c2340;-fx-text-fill:#38bdf8;" +
                    "-fx-border-color:#38bdf8;-fx-border-width:1;" +
                    "-fx-border-radius:6;-fx-background-radius:6;" +
                    "-fx-font-size:11;-fx-font-weight:bold;-fx-padding:6 4;-fx-cursor:hand;";
        return "-fx-background-color:#0d1520;-fx-text-fill:#64748b;" +
                "-fx-border-color:#1e2d40;-fx-border-width:1;" +
                "-fx-border-radius:6;-fx-background-radius:6;" +
                "-fx-font-size:11;-fx-font-weight:bold;-fx-padding:6 4;-fx-cursor:hand;";
    }
}
