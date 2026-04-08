package com.example.simulearn.SimuLearn.Chemistry.MultiMolecule;

import javafx.geometry.Point3D;

import java.util.List;

public class InorganicCoordBuilder {

    public static boolean apply(String moleculeName, MoleculeGraph graph) {
        List<MoleculeAtom> atoms = graph.getAtoms();
        if (atoms.isEmpty()) return false;

        switch (moleculeName) {
            case "Water" -> buildWater(atoms);
            case "Ammonia" -> buildAmmonia(atoms);
            case "Methane" -> buildMethane(atoms);
            case "Carbon Dioxide" -> buildCO2(atoms);
            case "Boron Trifluoride" -> buildBF3(atoms);
            case "Sulfur Hexafluoride" -> buildSF6(atoms);
            case "Phosphorus Pentachloride" -> buildPCl5(atoms);
            case "Xenon Difluoride" -> buildXeF2(atoms);
            case "Sulfur Dioxide" -> buildSO2(atoms);
            case "Hydrogen Sulfide" -> buildH2S(atoms);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static final double R = 70.0;

    private static void place(List<MoleculeAtom> atoms,
                              Point3D[] allPositions,
                              int bondedCount,
                              int lonePairCount) {

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(0).lonePairPositions = null;

        for (int i = 0; i < bondedCount && (i + 1) < atoms.size(); i++) {
            atoms.get(i + 1).position = allPositions[i];
        }

        if (lonePairCount > 0) {
            Point3D[] lpPos = new Point3D[lonePairCount];
            for (int i = 0; i < lonePairCount; i++) {
                lpPos[i] = allPositions[bondedCount + i];
            }
            atoms.get(0).lonePairPositions = lpPos;
        }
    }

    private static void buildWater(List<MoleculeAtom> atoms) {

        double angle = Math.toRadians(104.5 / 2);
        double r = R;

        Point3D h1 = new Point3D(Math.sin(angle) * r, -Math.cos(angle) * r, 0);
        Point3D h2 = new Point3D(-Math.sin(angle) * r, -Math.cos(angle) * r, 0);

        double lpAngle = Math.toRadians(109.5 / 2);
        Point3D lp1 = new Point3D(Math.sin(lpAngle) * r * 0.85, Math.cos(lpAngle) * r * 0.85, 0);
        Point3D lp2 = new Point3D(-Math.sin(lpAngle) * r * 0.85, Math.cos(lpAngle) * r * 0.85, 0);

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = h1;
        atoms.get(2).position = h2;
        atoms.get(0).lonePairPositions = new Point3D[]{lp1, lp2};
    }

    private static void buildAmmonia(List<MoleculeAtom> atoms) {

        double bondAngle = Math.toRadians(107.0);
        double r = R;

        double coneAngle = Math.toRadians(90 - (180 - 107) / 2.0);
        double hy = -r * Math.sin(Math.toRadians(20));
        double hr = r * Math.cos(Math.toRadians(20));

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(hr, hy, 0);
        atoms.get(2).position = new Point3D(hr * Math.cos(Math.toRadians(120)),
                hy,
                hr * Math.sin(Math.toRadians(120)));
        atoms.get(3).position = new Point3D(hr * Math.cos(Math.toRadians(240)),
                hy,
                hr * Math.sin(Math.toRadians(240)));

        atoms.get(0).lonePairPositions = new Point3D[]{
                new Point3D(0, r * 0.85, 0)
        };
    }

    private static void buildMethane(List<MoleculeAtom> atoms) {
        double a = R / Math.sqrt(3);
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(a, a, a);
        atoms.get(2).position = new Point3D(a, -a, -a);
        atoms.get(3).position = new Point3D(-a, a, -a);
        atoms.get(4).position = new Point3D(-a, -a, a);
        atoms.get(0).lonePairPositions = null;
    }

    private static void buildCO2(List<MoleculeAtom> atoms) {
        double bl = R * 0.87;
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(bl, 0, 0);
        atoms.get(2).position = new Point3D(-bl, 0, 0);
        atoms.get(0).lonePairPositions = null;
    }

    private static void buildBF3(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(R, 0, 0);
        atoms.get(2).position = new Point3D(R * Math.cos(Math.toRadians(120)),
                0,
                R * Math.sin(Math.toRadians(120)));
        atoms.get(3).position = new Point3D(R * Math.cos(Math.toRadians(240)),
                0,
                R * Math.sin(Math.toRadians(240)));
        atoms.get(0).lonePairPositions = null;
    }

    private static void buildSF6(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(R, 0, 0);
        atoms.get(2).position = new Point3D(-R, 0, 0);
        atoms.get(3).position = new Point3D(0, R, 0);
        atoms.get(4).position = new Point3D(0, -R, 0);
        atoms.get(5).position = new Point3D(0, 0, R);
        atoms.get(6).position = new Point3D(0, 0, -R);
        atoms.get(0).lonePairPositions = null;
    }

    private static void buildPCl5(List<MoleculeAtom> atoms) {
        double rCl = R * 1.1;
        atoms.get(0).position = Point3D.ZERO;

        atoms.get(1).position = new Point3D(0, rCl, 0);
        atoms.get(2).position = new Point3D(0, -rCl, 0);

        atoms.get(3).position = new Point3D(rCl, 0, 0);
        atoms.get(4).position = new Point3D(rCl * Math.cos(Math.toRadians(120)),
                0,
                rCl * Math.sin(Math.toRadians(120)));
        atoms.get(5).position = new Point3D(rCl * Math.cos(Math.toRadians(240)),
                0,
                rCl * Math.sin(Math.toRadians(240)));
        atoms.get(0).lonePairPositions = null;
    }

    private static void buildXeF2(List<MoleculeAtom> atoms) {
        atoms.get(0).position = Point3D.ZERO;

        atoms.get(1).position = new Point3D(0, R, 0);
        atoms.get(2).position = new Point3D(0, -R, 0);

        double lpR = R * 0.8;
        atoms.get(0).lonePairPositions = new Point3D[]{
                new Point3D(lpR, 0, 0),
                new Point3D(lpR * Math.cos(Math.toRadians(120)), 0, lpR * Math.sin(Math.toRadians(120))),
                new Point3D(lpR * Math.cos(Math.toRadians(240)), 0, lpR * Math.sin(Math.toRadians(240)))
        };
    }

    private static void buildSO2(List<MoleculeAtom> atoms) {
        double angle = Math.toRadians(119.0 / 2);
        double bl = R * 0.87;

        atoms.get(0).position = Point3D.ZERO;

        atoms.get(1).position = new Point3D(Math.sin(angle) * bl, -Math.cos(angle) * bl, 0);
        atoms.get(2).position = new Point3D(-Math.sin(angle) * bl, -Math.cos(angle) * bl, 0);

        atoms.get(0).lonePairPositions = new Point3D[]{
                new Point3D(0, R * 0.85, 0)
        };
    }

    private static void buildH2S(List<MoleculeAtom> atoms) {
        double angle = Math.toRadians(92.0 / 2);
        double r = R;

        atoms.get(0).position = Point3D.ZERO;
        atoms.get(1).position = new Point3D(Math.sin(angle) * r, -Math.cos(angle) * r, 0);
        atoms.get(2).position = new Point3D(-Math.sin(angle) * r, -Math.cos(angle) * r, 0);

        double lpAngle = Math.toRadians(109.5 / 2);
        double lpR = R * 0.8;
        atoms.get(0).lonePairPositions = new Point3D[]{
                new Point3D(Math.sin(lpAngle) * lpR, Math.cos(lpAngle) * lpR, 0),
                new Point3D(-Math.sin(lpAngle) * lpR, Math.cos(lpAngle) * lpR, 0)
        };
    }
}