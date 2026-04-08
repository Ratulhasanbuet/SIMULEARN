package com.example.simulearn.SimuLearn.Math.Histogram;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistogramController {
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
    private Spinner<Integer> binSpinner;
    @FXML
    private TextArea dataInputArea;
    @FXML
    private ColorPicker barColorPicker;
    private List<Double> numericData = new ArrayList<>();
    private int binCount;
    @FXML
    private BarChart histogramChart;

    @FXML
    public void initialize() {

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 50, 10);

        binSpinner.setValueFactory(valueFactory);
    }

    private void processData() {
        numericData.clear();
        String rawData = dataInputArea.getText();

        binCount = binSpinner.getValue();

        String[] parts = rawData.split("[,\\s]+");

        try {
            for (String p : parts) {
                String cleanPart = p.trim();
                if (!cleanPart.isEmpty()) {
                    numericData.add(Double.parseDouble(cleanPart));
                }
            }

            Collections.sort(numericData);

        } catch (NumberFormatException e) {
            System.err.println("Error: Input contains non-numeric values.");
        }
    }

    @FXML
    private void onRunClicked() {
        processData();
        double min = numericData.getFirst();
        double max = numericData.getLast();
        double range = max - min;
        double binWidth = range / binCount;
        int[] frequencies = new int[binCount];

        for (double value : numericData) {
            int binIndex = (int) ((value - min) / binWidth);

            if (binIndex >= binCount) {
                binIndex = binCount - 1;
            }

            frequencies[binIndex]++;
        }
        updateChart(frequencies, min, binWidth, binCount);
    }

    private void updateChart(int[] frequencies, double min, double binWidth, int binCount) {

        CategoryAxis xAxis = (CategoryAxis) histogramChart.getXAxis();
        histogramChart.setAnimated(false);
        histogramChart.getData().clear();
        xAxis.getCategories().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data Distribution");

        for (int i = 0; i < binCount; i++) {
            double lowerBound = min + (i * binWidth);
            double upperBound = lowerBound + binWidth;
            String label = String.format("%.1f-%.1f", lowerBound, upperBound);

            series.getData().add(new XYChart.Data<>(label, frequencies[i]));
        }

        xAxis.setTickLabelRotation(-45);

        histogramChart.getData().add(series);

        applyCustomColor(series);
    }

    private void applyCustomColor(XYChart.Series<String, Number> series) {

        Color selectedColor = barColorPicker.getValue();
        String webColor = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        for (XYChart.Data<String, Number> data : series.getData()) {
            data.getNode().setStyle("-fx-bar-fill: " + webColor + ";");
        }
    }

    @FXML
    private void onReset() {
        CategoryAxis xAxis = (CategoryAxis) histogramChart.getXAxis();
        histogramChart.setAnimated(false);
        histogramChart.getData().clear();
        xAxis.getCategories().clear();
    }
}