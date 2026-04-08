package com.example.simulearn.SimuLearn.Chemistry.MultiMolecule;

import javafx.geometry.Point3D;

import java.util.List;

public class OrganicCoordBuilder {

    private static final double B = 72.0;

    private static final double TC = 0.3333;
    private static final double TS = 0.9428;
    private static final double TA = 0.4714;
    private static final double TB = 0.8165;

    public static boolean apply(String moleculeName, MoleculeGraph graph) {
        List<MoleculeAtom> atoms = graph.getAtoms();
        switch (moleculeName) {
            case "Ethane" -> setEthane(atoms);
            case "Ethanol" -> setEthanol(atoms);
            case "Acetic Acid" -> setAceticAcid(atoms);
            case "Acetone" -> setAcetone(atoms);
            case "Ethylene" -> setEthylene(atoms);
            case "Acetylene" -> setAcetylene(atoms);
            case "Propane" -> setPropane(atoms);
            case "Formaldehyde" -> setFormaldehyde(atoms);
            case "Dimethyl Ether" -> setDimethylEther(atoms);
            case "Chloroform" -> setChloroform(atoms);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static Point3D p(double x, double y, double z) {
        return new Point3D(x * B, y * B, z * B);
    }

    private static void pos(MoleculeAtom a, double x, double y, double z) {
        a.position = new Point3D(x, y, z);
    }

    private static void setEthane(List<MoleculeAtom> a) {

        double cx = B / 2;
        pos(a.get(0), -cx, 0, 0);
        pos(a.get(1), cx, 0, 0);

        double hx1 = -cx - TC * B;
        pos(a.get(2), hx1, TS * B, 0);
        pos(a.get(3), hx1, -TA * B, TB * B);
        pos(a.get(4), hx1, -TA * B, -TB * B);

        double hx2 = cx + TC * B;
        pos(a.get(5), hx2, TS * B, 0);
        pos(a.get(6), hx2, -TA * B, TB * B);
        pos(a.get(7), hx2, -TA * B, -TB * B);

    }

    private static void setEthanol(List<MoleculeAtom> a) {

        pos(a.get(0), -B, 0, 0);
        pos(a.get(1), 0, 0, 0);
        pos(a.get(2), B * Math.cos(Math.toRadians(60)), 0, B * Math.cos(Math.toRadians(30)));

        double c1x = -B;
        pos(a.get(3), c1x - TC * B, TS * B, 0);
        pos(a.get(4), c1x - TC * B, -TA * B, TB * B);
        pos(a.get(5), c1x - TC * B, -TA * B, -TB * B);

        pos(a.get(6), 0, B, 0);
        pos(a.get(7), 0, -B, 0);

        Point3D oPos = a.get(2).position;
        pos(a.get(8), oPos.getX() + B - 5,
                oPos.getY(),
                oPos.getZ());
    }

    private static void setAceticAcid(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), -B, 0, 0);
        pos(a.get(1), 0, 0, 0);
        pos(a.get(2), bl, bl * 0.866, 0);
        pos(a.get(3), bl, -bl * 0.866, 0);

        pos(a.get(4), -B - TC * B, TS * B, 0);
        pos(a.get(5), -B - TC * B, -TA * B, TB * B);
        pos(a.get(6), -B - TC * B, -TA * B, -TB * B);

        Point3D o2 = a.get(3).position;
        pos(a.get(7), o2.getX() + TC * B * 0.7,
                o2.getY() - TS * B * 0.5, 0);
    }

    private static void setAcetone(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), -B, 0, 0);
        pos(a.get(1), 0, 0, 0);
        pos(a.get(2), B, 0, 0);
        pos(a.get(3), 0, bl, 0);

        pos(a.get(4), -B - TC * B, TS * B, 0);
        pos(a.get(5), -B - TC * B, -TA * B, TB * B);
        pos(a.get(6), -B - TC * B, -TA * B, -TB * B);

        pos(a.get(7), B + TC * B, TS * B, 0);
        pos(a.get(8), B + TC * B, -TA * B, TB * B);
        pos(a.get(9), B + TC * B, -TA * B, -TB * B);

        double cos60 = 0.5, sin60 = Math.sqrt(3) / 2;
        for (int i = 7; i <= 9; i++) {
            double y = a.get(i).position.getY();
            double z = a.get(i).position.getZ();
            a.get(i).position = new Point3D(
                    a.get(i).position.getX(),
                    y * cos60 - z * sin60,
                    y * sin60 + z * cos60);
        }
    }

    private static void setEthylene(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), -bl / 2, 0, 0);
        pos(a.get(1), bl / 2, 0, 0);

        double hx = bl / 2 + B * Math.cos(Math.toRadians(60));
        double hy = B * Math.sin(Math.toRadians(60));
        pos(a.get(2), -bl / 2 - B * Math.cos(Math.toRadians(60)), hy, 0);
        pos(a.get(3), -bl / 2 - B * Math.cos(Math.toRadians(60)), -hy, 0);
        pos(a.get(4), bl / 2 + B * Math.cos(Math.toRadians(60)), hy, 0);
        pos(a.get(5), bl / 2 + B * Math.cos(Math.toRadians(60)), -hy, 0);
    }

    private static void setAcetylene(List<MoleculeAtom> a) {
        double bl = B * 0.78;
        pos(a.get(0), -bl / 2, 0, 0);
        pos(a.get(1), bl / 2, 0, 0);
        pos(a.get(2), -bl / 2 - B, 0, 0);
        pos(a.get(3), bl / 2 + B, 0, 0);
    }

    private static void setPropane(List<MoleculeAtom> a) {
        pos(a.get(0), -B, 0, 0);
        pos(a.get(1), 0, 0, 0);
        pos(a.get(2), B, 0, 0);

        pos(a.get(3), -B - TC * B, TS * B, 0);
        pos(a.get(4), -B - TC * B, -TA * B, TB * B);
        pos(a.get(5), -B - TC * B, -TA * B, -TB * B);

        pos(a.get(6), 0, TS * B, TB * B);
        pos(a.get(7), 0, TS * B, -TB * B);

        pos(a.get(8), B + TC * B, TS * B, 0);
        pos(a.get(9), B + TC * B, -TA * B, TB * B);
        pos(a.get(10), B + TC * B, -TA * B, -TB * B);

        double cos60 = 0.5, sin60 = Math.sqrt(3) / 2;
        for (int i = 8; i <= 10; i++) {
            double y = a.get(i).position.getY();
            double z = a.get(i).position.getZ();
            a.get(i).position = new Point3D(
                    a.get(i).position.getX(),
                    y * cos60 - z * sin60,
                    y * sin60 + z * cos60);
        }
    }

    private static void setFormaldehyde(List<MoleculeAtom> a) {
        double bl = B * 0.87;
        pos(a.get(0), 0, 0, 0);
        pos(a.get(1), bl, 0, 0);

        double hx = -B * Math.cos(Math.toRadians(60));
        double hy = B * Math.sin(Math.toRadians(60));
        pos(a.get(2), hx, hy, 0);
        pos(a.get(3), hx, -hy, 0);
    }

    private static void setDimethylEther(List<MoleculeAtom> a) {

        double halfAngle = Math.toRadians(109.5 / 2);

        pos(a.get(0), 0, 0, 0);

        double cx = B * Math.sin(halfAngle);
        double cy = B * Math.cos(halfAngle);

        pos(a.get(1), -cx, cy, 0);
        pos(a.get(2), cx, cy, 0);

        double c1x = -cx;
        double c1y = cy;

        pos(a.get(3), c1x - B * Math.cos(Math.toRadians(60)), c1y - B * Math.sin(Math.toRadians(60)), 0);

        pos(a.get(4), c1x - TC * B * 0.6, c1y + TA * B, TB * B);
        pos(a.get(5), c1x - TC * B * 0.6, c1y + TA * B, -TB * B);

        double c2x = cx;
        double c2y = cy;
        pos(a.get(6), c2x + B * Math.cos(Math.toRadians(60)), c2y - B * Math.sin(Math.toRadians(60)), 0);
        pos(a.get(7), c2x + TC * B * 0.6, c2y + TA * B, TB * B);
        pos(a.get(8), c2x + TC * B * 0.6, c2y + TA * B, -TB * B);
    }

    private static void setChloroform(List<MoleculeAtom> a) {
        double clB = B * 1.1;
        pos(a.get(0), 0, 0, 0);
        pos(a.get(1), 0, B, 0);

        double clY = -TC * clB;
        double clR = TS * clB;
        for (int i = 0; i < 3; i++) {
            double angle = Math.toRadians(120 * i);
            pos(a.get(2 + i),
                    clR * Math.cos(angle),
                    clY,
                    clR * Math.sin(angle));
        }
    }
}