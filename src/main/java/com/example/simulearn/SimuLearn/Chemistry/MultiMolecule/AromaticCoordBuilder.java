package com.example.simulearn.SimuLearn.Chemistry.MultiMolecule;

import javafx.geometry.Point3D;

import java.util.List;

public class AromaticCoordBuilder {

    private static final double B = 72.0;
    private static final double R6 = B;
    private static final double APO6 = B * Math.cos(Math.toRadians(30));
    private static final double R5 = B / (2 * Math.sin(Math.toRadians(36)));

    public static boolean apply(String name, MoleculeGraph graph) {
        List<MoleculeAtom> a = graph.getAtoms();
        switch (name) {
            case "Benzene" -> setBenzene(a);
            case "Toluene" -> setToluene(a);
            case "Phenol" -> setPhenol(a);
            case "Aniline" -> setAniline(a);
            case "Naphthalene" -> setNaphthalene(a);
            case "Pyridine" -> setPyridine(a);
            case "Furan" -> setFuran(a);
            case "Cyclohexane" -> setCyclohexane(a);
            default -> {
                return false;
            }
        }
        return true;
    }

    private static void pos(MoleculeAtom a, double x, double z) {
        a.position = new Point3D(x, 0, z);
    }

    private static void pos3(MoleculeAtom a, double x, double y, double z) {
        a.position = new Point3D(x, y, z);
    }

    private static double[][] ring(double cx, double cz, double radius,
                                   int n, double startDeg) {
        double[][] pts = new double[n][2];
        for (int i = 0; i < n; i++) {
            double a = Math.toRadians(startDeg + i * (360.0 / n));
            pts[i][0] = cx + radius * Math.cos(a);
            pts[i][1] = cz + radius * Math.sin(a);
        }
        return pts;
    }

    private static void placeH(MoleculeAtom h, MoleculeAtom c,
                               double ringCX, double ringCZ) {
        double dx = c.position.getX() - ringCX;
        double dz = c.position.getZ() - ringCZ;
        double len = Math.sqrt(dx * dx + dz * dz);
        h.position = new Point3D(
                c.position.getX() + dx / len * B,
                0,
                c.position.getZ() + dz / len * B);
    }

    private static void setBenzene(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        for (int i = 0; i < 6; i++) placeH(a.get(6 + i), a.get(i), 0, 0);
    }

    private static void setToluene(List<MoleculeAtom> a) {

        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        double cmex = r[0][0];
        double cmez = r[0][1];
        double odx = cmex / R6, odz = cmez / R6;

        pos(a.get(6), cmex + odx * B, cmez + odz * B);

        double mx = a.get(6).position.getX();
        double mz = a.get(6).position.getZ();

        double px = -odz, pz = odx;

        pos(a.get(7), mx + odx * B, mz + odz * B);

        double cos120 = Math.cos(Math.toRadians(120));
        double sin120 = Math.sin(Math.toRadians(120));

        double h8x = odx * cos120 - odz * sin120;
        double h8z = odx * sin120 + odz * cos120;
        double h9x = odx * cos120 + odz * sin120;
        double h9z = -odx * sin120 + odz * cos120;
        pos(a.get(8), mx + h8x * B, mz + h8z * B);
        pos(a.get(9), mx + h9x * B, mz + h9z * B);

        for (int i = 1; i <= 5; i++) placeH(a.get(9 + i), a.get(i), 0, 0);
    }

    private static void setPhenol(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        double c0x = r[0][0], c0z = r[0][1];
        double odx = c0x / R6, odz = c0z / R6;
        pos(a.get(6), c0x + odx * B * 0.85, c0z + odz * B * 0.85);

        double ox = a.get(6).position.getX();
        double oz = a.get(6).position.getZ();
        pos(a.get(7), ox + odx * B * 0.85, oz + odz * B * 0.85);

        for (int i = 1; i <= 5; i++) placeH(a.get(7 + i), a.get(i), 0, 0);
    }

    private static void setAniline(List<MoleculeAtom> a) {
        double[][] r = ring(0, 0, R6, 6, 90);
        for (int i = 0; i < 6; i++) pos(a.get(i), r[i][0], r[i][1]);

        double c0x = r[0][0], c0z = r[0][1];
        double odx = c0x / R6, odz = c0z / R6;
        pos(a.get(6), c0x + odx * B, c0z + odz * B);

        double nx = a.get(6).position.getX();
        double nz = a.get(6).position.getZ();
        double spreadRad = Math.toRadians(30);
        double cos30 = Math.cos(spreadRad), sin30 = Math.sin(spreadRad);

        double h7x = odx * cos30 - odz * sin30;
        double h7z = odx * sin30 + odz * cos30;

        double h8x = odx * cos30 + odz * sin30;
        double h8z = -odx * sin30 + odz * cos30;
        pos(a.get(7), nx + h7x * B, nz + h7z * B);
        pos(a.get(8), nx + h8x * B, nz + h8z * B);

        for (int i = 1; i <= 5; i++) placeH(a.get(8 + i), a.get(i), 0, 0);
    }

    private static void setNaphthalene(List<MoleculeAtom> a) {

        double[] r1ang = {90, 150, 210, 270, 330, 30};
        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(r1ang[i]);
            pos(a.get(i), -APO6 + R6 * Math.cos(ang), R6 * Math.sin(ang));
        }

        double[] r2ang = {90, 30, 330, 270};
        int[] r2idx = {6, 7, 8, 9};
        for (int i = 0; i < 4; i++) {
            double ang = Math.toRadians(r2ang[i]);
            pos(a.get(r2idx[i]), APO6 + R6 * Math.cos(ang), R6 * Math.sin(ang));
        }

        int[] hCarbon = {0, 1, 2, 3, 6, 7, 8, 9};
        double[] hRingX = {-APO6, -APO6, -APO6, -APO6, APO6, APO6, APO6, APO6};
        for (int i = 0; i < 8; i++) {
            placeH(a.get(10 + i), a.get(hCarbon[i]), hRingX[i], 0);
        }
    }

    private static void setPyridine(List<MoleculeAtom> a) {

        double[][] r = ring(0, 0, R6, 6, 90);

        pos(a.get(0), r[0][0], r[0][1]);
        for (int i = 1; i <= 5; i++) pos(a.get(i), r[i][0], r[i][1]);

        for (int i = 1; i <= 5; i++) placeH(a.get(5 + i), a.get(i), 0, 0);
    }

    private static void setFuran(List<MoleculeAtom> a) {

        double[][] r = ring(0, 0, R5, 5, 90);
        pos(a.get(0), r[0][0], r[0][1]);
        for (int i = 1; i <= 4; i++) pos(a.get(i), r[i][0], r[i][1]);

        for (int i = 1; i <= 4; i++) placeH(a.get(4 + i), a.get(i), 0, 0);
    }

    private static void setCyclohexane(List<MoleculeAtom> a) {

        double ringR = R6 * 0.95;
        double chairY = B * 0.25;

        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(90 + i * 60);
            double y = (i % 2 == 0) ? +chairY : -chairY;
            pos3(a.get(i), ringR * Math.cos(ang), y, ringR * Math.sin(ang));
        }

        for (int i = 0; i < 6; i++) {
            double ang = Math.toRadians(90 + i * 60);
            double y = (i % 2 == 0) ? +chairY : -chairY;

            double axY = (i % 2 == 0) ? +B : -B;
            pos3(a.get(6 + i * 2),
                    a.get(i).position.getX(),
                    y + axY * 0.85,
                    a.get(i).position.getZ());

            double eqX = ringR * Math.cos(ang) * 0.6;
            double eqZ = ringR * Math.sin(ang) * 0.6;
            double eqY = -y * 0.5;
            pos3(a.get(6 + i * 2 + 1),
                    a.get(i).position.getX() + eqX,
                    y + eqY,
                    a.get(i).position.getZ() + eqZ);
        }
    }
}