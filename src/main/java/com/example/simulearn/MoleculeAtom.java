package com.example.simulearn;

import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.List;

public class MoleculeAtom {

    public final int id;
    public final String symbol;
    public int lonePairs = 0;
    public Point3D position = Point3D.ZERO;
    public List<MoleculeBond> bonds = new ArrayList<>();

    // explicit 3D positions for lone pair spheres (set by InorganicCoordBuilder)
    public Point3D[] lonePairPositions = null;

    public MoleculeAtom(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    /** CPK display radius */
    public double radius() {
        switch (symbol) {
            case "H":  return 10;
            case "C":  return 18;
            case "N":  return 16;
            case "O":  return 15;
            case "F":  return 13;
            case "P":  return 22;
            case "S":  return 20;
            case "Cl": return 23;
            case "Br": return 26;
            case "I":  return 30;
            default:   return 20;
        }
    }

    /** auto-calculate lone pairs from valence */
    public void autoLonePairs() {
        int valence = valenceElectrons();
        if (valence < 0) return;
        int used = 0;
        for (MoleculeBond b : bonds) used += b.order;
        lonePairs = Math.max(0, (valence - used) / 2);
    }

    private int valenceElectrons() {
        switch (symbol) {
            case "H":  return 1;
            case "B":  return 3;
            case "C":  return 4;
            case "N":  return 5;
            case "O":  return 6;
            case "F":  return 7;
            case "Si": return 4;
            case "P":  return 5;
            case "S":  return 6;
            case "Cl": return 7;
            case "Br": return 7;
            case "I":  return 7;
            case "Xe": return 8;
            default:   return -1;
        }
    }

    @Override
    public String toString() { return symbol + "#" + id; }
}