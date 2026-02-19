

package com.example.simulearn;

import javafx.geometry.Point3D;

public class VSEPRGeometry {
    String name;
    String electronGeometry; // Added for electron geometry
    int stericNumber;
    int lonePairs;
    int bondedAtoms;
    String bondAngles;
    Point3D[] positions;

    VSEPRGeometry(String name, String electronGeometry, int stericNumber, int lonePairs, int bondedAtoms,
                  String bondAngles, Point3D[] positions) {
        this.name = name;
        this.electronGeometry = electronGeometry;
        this.stericNumber = stericNumber;
        this.lonePairs = lonePairs;
        this.bondedAtoms = bondedAtoms;
        this.bondAngles = bondAngles;
        this.positions = positions;
    }

    Point3D[] getPositions() {
        return positions;
    }
}