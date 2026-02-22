package com.example.simulearn;

/**
 * Helper class to store delta values for drag operations
 */
public class Delta {
    public double x;
    public double y;

    public Delta() {
        this.x = 0;
        this.y = 0;
    }

    public Delta(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
