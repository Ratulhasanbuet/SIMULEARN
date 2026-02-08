package com.example.simulearn;

import javafx.scene.paint.Color;

public class Material {
    private String name;
    private double density; // in g/cm³ or kg/L
    private Color color;

    public Material(String name, double density, Color color) {
        this.name = name;
        this.density = density;
        this.color = color;
    }

    // Getters
    public String getName() { return name; }
    public double getDensity() { return density; }
    public Color getColor() { return color; }
}
