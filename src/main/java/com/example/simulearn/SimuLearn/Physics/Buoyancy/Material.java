package com.example.simulearn.SimuLearn.Physics.Buoyancy;

import javafx.scene.paint.Color;

public class Material {
    private String name;
    private double density;
    private Color color;

    public Material(String name, double density, Color color) {
        this.name = name;
        this.density = density;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public double getDensity() {
        return density;
    }

    public Color getColor() {
        return color;
    }
}