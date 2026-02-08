package com.example.simulearn;


import javafx.scene.paint.Color;

public class Liquid {
    private String name;
    private double density;
    private Color color;

    public Liquid(String name, double density, Color color) {
        this.name = name;
        this.density = density;
        this.color = color;
    }

    // Getters
    public String getName() { return name; }
    public double getDensity() { return density; }
    public Color getColor() { return color; }
}
