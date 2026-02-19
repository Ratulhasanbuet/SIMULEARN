
package com.example.simulearn;

import javafx.geometry.Point3D;

public class VSEPRCalculator {

    // Helper method to get electron geometry based on steric number
    private static String getElectronGeometry(int stericNumber) {
        switch (stericNumber) {
            case 2: return "Linear";
            case 3: return "Trigonal Planar";
            case 4: return "Tetrahedral";
            case 5: return "Trigonal Bipyramidal";
            case 6: return "Octahedral";
            default: return "Custom";
        }
    }

    public static VSEPRGeometry calculate(int stericNumber, int lonePairs, int bondedAtoms) {
        double bondLength = 70; // Reduced from 100 to 70 for smaller molecule

        switch (stericNumber) {
            case 2:
                return new VSEPRGeometry("Linear", getElectronGeometry(2), 2, lonePairs, bondedAtoms, "180°",
                        new Point3D[]{
                                new Point3D(bondLength, 0, 0),
                                new Point3D(-bondLength, 0, 0)
                        });

            case 3:
                if (lonePairs == 0) {
                    return new VSEPRGeometry("Trigonal Planar", getElectronGeometry(3), 3, 0, 3, "120°",
                            getTrigonalPlanar(bondLength));
                } else if (lonePairs == 1) {
                    return new VSEPRGeometry("Bent", getElectronGeometry(3), 3, 1, 2, "~120°",
                            getTrigonalPlanar(bondLength));
                } else {
                    return new VSEPRGeometry("Linear", getElectronGeometry(3), 3, 2, 1, "180°",
                            getTrigonalPlanar(bondLength));
                }

            case 4:
                if (lonePairs == 0) {
                    return new VSEPRGeometry("Tetrahedral", getElectronGeometry(4), 4, 0, 4, "109.5°",
                            getTetrahedral(bondLength));
                } else if (lonePairs == 1) {
                    return new VSEPRGeometry("Trigonal Pyramidal", getElectronGeometry(4), 4, 1, 3, "~107°",
                            getTetrahedral(bondLength));
                } else if (lonePairs == 2) {
                    return new VSEPRGeometry("Bent", getElectronGeometry(4), 4, 2, 2, "~104.5°",
                            getTetrahedral(bondLength));
                } else {
                    return new VSEPRGeometry("Linear", getElectronGeometry(4), 4, 3, 1, "180°",
                            getTetrahedral(bondLength));
                }

            case 5:
                return handleTrigonalBipyramidal(lonePairs, bondedAtoms, bondLength);

            case 6:
                return handleOctahedral(lonePairs, bondedAtoms, bondLength);

            default:
                return new VSEPRGeometry("Custom", "Custom", stericNumber, lonePairs, bondedAtoms, "N/A",
                        getCustomGeometry(stericNumber, bondLength));
        }
    }

    static Point3D[] getTrigonalPlanar(double r) {
        return new Point3D[]{
                new Point3D(r, 0, 0),
                new Point3D(r * Math.cos(Math.toRadians(120)), r * Math.sin(Math.toRadians(120)), 0),
                new Point3D(r * Math.cos(Math.toRadians(240)), r * Math.sin(Math.toRadians(240)), 0)
        };
    }

    static Point3D[] getTetrahedral(double r) {
        double a = r / Math.sqrt(3);
        return new Point3D[]{
                new Point3D(a, a, a),
                new Point3D(a, -a, -a),
                new Point3D(-a, a, -a),
                new Point3D(-a, -a, a)
        };
    }

    static VSEPRGeometry handleTrigonalBipyramidal(int lonePairs, int bondedAtoms, double r) {
        Point3D[] positions = new Point3D[]{
                new Point3D(0, r, 0),
                new Point3D(0, -r, 0),
                new Point3D(r, 0, 0),
                new Point3D(r * Math.cos(Math.toRadians(120)), 0, r * Math.sin(Math.toRadians(120))),
                new Point3D(r * Math.cos(Math.toRadians(240)), 0, r * Math.sin(Math.toRadians(240)))
        };

        if (lonePairs == 0) {
            return new VSEPRGeometry("Trigonal Bipyramidal", getElectronGeometry(5), 5, 0, 5, "90°, 120°, 180°", positions);
        } else if (lonePairs == 1) {
            return new VSEPRGeometry("Seesaw", getElectronGeometry(5), 5, 1, 4, "~90°, ~120°", positions);
        } else if (lonePairs == 2) {
            return new VSEPRGeometry("T-shaped", getElectronGeometry(5), 5, 2, 3, "~90°", positions);
        } else {
            return new VSEPRGeometry("Linear", getElectronGeometry(5), 5, 3, 2, "180°", positions);
        }
    }

    static VSEPRGeometry handleOctahedral(int lonePairs, int bondedAtoms, double r) {
        Point3D[] positions = new Point3D[]{
                new Point3D(r, 0, 0),
                new Point3D(-r, 0, 0),
                new Point3D(0, r, 0),
                new Point3D(0, -r, 0),
                new Point3D(0, 0, r),
                new Point3D(0, 0, -r)
        };

        if (lonePairs == 0) {
            return new VSEPRGeometry("Octahedral", getElectronGeometry(6), 6, 0, 6, "90°, 180°", positions);
        } else if (lonePairs == 1) {
            return new VSEPRGeometry("Square Pyramidal", getElectronGeometry(6), 6, 1, 5, "~90°", positions);
        } else {
            return new VSEPRGeometry("Square Planar", getElectronGeometry(6), 6, 2, 4, "90°, 180°", positions);
        }
    }

    static Point3D[] getCustomGeometry(int count, double r) {
        Point3D[] positions = new Point3D[count];
        for (int i = 0; i < count; i++) {
            double theta = 2 * Math.PI * i / count;
            positions[i] = new Point3D(r * Math.cos(theta), r * Math.sin(theta), 0);
        }
        return positions;
    }
}