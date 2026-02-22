//package com.molecule.moleculeplus;
//
//import javafx.geometry.Point3D;
//import javafx.scene.Group;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Cylinder;
//import javafx.scene.shape.Sphere;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * Builds the JavaFX 3D scene from a MoleculeGraph.
// * Lone pairs shown as small yellow spheres positioned around the central atom.
// */
//public class Renderer3D {
//
//    private static final Color LONE_PAIR_COLOR = Color.web("#aaaaaa", 0.95);
//    private static final double LP_RADIUS       = 7.0;
//    private static final double LP_DIST         = 38.0; // distance from nucleus
//
//    public static Group build(MoleculeGraph graph, boolean showLonePairs) {
//        Group group = new Group();
//        if (graph.atomCount() == 0) return group;
//
//        Set<MoleculeBond> drawn = new HashSet<>();
//
//        for (MoleculeAtom atom : graph.getAtoms()) {
//
//            // ── Atom sphere ──────────────────────────────────────────────────
//            Sphere sphere = new Sphere(atom.radius());
//            sphere.setTranslateX(atom.position.getX());
//            sphere.setTranslateY(atom.position.getY());
//            sphere.setTranslateZ(atom.position.getZ());
//            PhongMaterial mat = new PhongMaterial(cpk(atom.symbol));
//            mat.setSpecularColor(Color.WHITE);
//            mat.setSpecularPower(40);
//            sphere.setMaterial(mat);
//            group.getChildren().add(sphere);
//
//            // ── Bonds ────────────────────────────────────────────────────────
//            for (MoleculeBond bond : atom.bonds) {
//                if (drawn.contains(bond)) continue;
//                drawn.add(bond);
//                drawBond(group, bond);
//            }
//
//            // ── Lone pairs ───────────────────────────────────────────────────
//            if (showLonePairs) {
//                if (atom.lonePairPositions != null) {
//                    drawLonePairsExplicit(group, atom);
//                }
//            }
//        }
//        return group;
//    }
//
//    // ── Explicit lone pair rendering (from InorganicCoordBuilder positions)
//    private static void drawLonePairsExplicit(Group group, MoleculeAtom atom) {
//        if (atom.lonePairPositions == null) return;
//        PhongMaterial mat = new PhongMaterial(LONE_PAIR_COLOR);
//        mat.setSpecularColor(Color.WHITE);
//        mat.setSpecularPower(30);
//        for (Point3D lpPos : atom.lonePairPositions) {
//            Sphere s = new Sphere(8.5);
//            s.setTranslateX(atom.position.getX() + lpPos.getX());
//            s.setTranslateY(atom.position.getY() + lpPos.getY());
//            s.setTranslateZ(atom.position.getZ() + lpPos.getZ());
//            s.setMaterial(mat);
//            group.getChildren().add(s);
//        }
//    }
//
//    // ── Lone pair rendering ───────────────────────────────────────────────────
//
//    private static void drawLonePairs(Group group, MoleculeAtom atom) {
//        int lp = atom.lonePairs;
//        // Find directions NOT occupied by bonds, place LP spheres there
//        List<Point3D> bondDirs = new java.util.ArrayList<>();
//        for (MoleculeBond b : atom.bonds) {
//            Point3D dir = b.other(atom).position.subtract(atom.position);
//            if (dir.magnitude() > 0.1) bondDirs.add(dir.normalize());
//        }
//
//        // Get LP directions using the same ideal geometry
//        int steric = atom.steric();
//        Point3D[] allDirs = idealDirections(steric);
//
//        // Rotate directions to match bond orientations if we have bonds
//        if (!bondDirs.isEmpty() && allDirs.length > 0) {
//            // Align first ideal direction with first bond
//            Point3D firstBond = bondDirs.get(0);
//            Point3D firstIdeal = allDirs[0];
//            double dot = clamp(firstIdeal.dotProduct(firstBond), -1, 1);
//            if (Math.abs(dot - 1) > 1e-9) {
//                Point3D axis = Math.abs(dot + 1) < 1e-9
//                        ? perp(firstIdeal)
//                        : norm(firstIdeal.crossProduct(firstBond));
//                double angle = Math.acos(dot);
//                for (int i = 0; i < allDirs.length; i++) {
//                    allDirs[i] = rodrigues(allDirs[i], axis, angle);
//                }
//            }
//        }
//
//        // The last 'lp' directions are lone pair positions
//        int bondCount = atom.bonds.size();
//        int lpStart   = bondCount; // first LP slot index
//
//        PhongMaterial lpMat = new PhongMaterial(LONE_PAIR_COLOR);
//        lpMat.setSpecularColor(Color.web("#ffffaa"));
//        lpMat.setSpecularPower(60);
//
//        for (int i = 0; i < lp; i++) {
//            int idx = lpStart + i;
//            if (idx >= allDirs.length) break;
//            Point3D dir = allDirs[idx].normalize();
//            Point3D lpPos = atom.position.add(dir.multiply(atom.radius() + LP_DIST));
//
//            Sphere lpSphere = new Sphere(LP_RADIUS);
//            lpSphere.setTranslateX(lpPos.getX());
//            lpSphere.setTranslateY(lpPos.getY());
//            lpSphere.setTranslateZ(lpPos.getZ());
//            lpSphere.setMaterial(lpMat);
//            group.getChildren().add(lpSphere);
//
//            // Draw faint line from atom to LP
//            cylinder(group, atom.position, lpPos, 1.5, Color.web("#888833", 0.5));
//        }
//    }
//
//    // ── Bond rendering ────────────────────────────────────────────────────────
//
//    private static void drawBond(Group group, MoleculeBond bond) {
//        Point3D p1 = bond.a1.position;
//        Point3D p2 = bond.a2.position;
//        Color color = Color.web("#aaaaaa");
//
//        if (bond.order == 1) {
//            cylinder(group, p1, p2, 4.5, color);
//        } else if (bond.order == 2) {
//            Point3D off = perpOffset(p2.subtract(p1), 5.5);
//            cylinder(group, p1.add(off), p2.add(off), 3.0, color);
//            cylinder(group, p1.subtract(off), p2.subtract(off), 3.0, color);
//        } else if (bond.order == 3) {
//            Point3D off = perpOffset(p2.subtract(p1), 6.5);
//            cylinder(group, p1, p2, 2.5, color);
//            cylinder(group, p1.add(off), p2.add(off), 2.0, color);
//            cylinder(group, p1.subtract(off), p2.subtract(off), 2.0, color);
//        }
//    }
//
//    private static void cylinder(Group group, Point3D p1, Point3D p2,
//                                 double radius, Color color) {
//        Point3D dir = p2.subtract(p1);
//        double len = dir.magnitude();
//        if (len < 0.01) return;
//
//        Cylinder cyl = new Cylinder(radius, len);
//        PhongMaterial mat = new PhongMaterial(color);
//        mat.setSpecularColor(Color.web("#888888"));
//        cyl.setMaterial(mat);
//
//        Point3D mid = p1.midpoint(p2);
//        cyl.setTranslateX(mid.getX());
//        cyl.setTranslateY(mid.getY());
//        cyl.setTranslateZ(mid.getZ());
//
//        Point3D yAxis   = new Point3D(0, 1, 0);
//        Point3D normDir = dir.normalize();
//        Point3D cross   = yAxis.crossProduct(normDir);
//        double  dot     = yAxis.dotProduct(normDir);
//        double  angle   = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, dot))));
//
//        if (cross.magnitude() > 1e-6) {
//            cyl.setRotationAxis(cross);
//            cyl.setRotate(angle);
//        } else if (dot < 0) {
//            cyl.setRotate(180);
//        }
//        group.getChildren().add(cyl);
//    }
//
//    private static Point3D perpOffset(Point3D dir, double magnitude) {
//        Point3D n = dir.normalize();
//        Point3D t = Math.abs(n.getX()) < 0.9 ? new Point3D(1,0,0) : new Point3D(0,1,0);
//        return n.crossProduct(t).normalize().multiply(magnitude);
//    }
//
//    // ── Geometry helpers (same as CoordBuilder) ───────────────────────────────
//
//    private static Point3D[] idealDirections(int steric) {
//        switch (steric) {
//            case 1:  return new Point3D[]{p(1,0,0)};
//            case 2:  return new Point3D[]{p(1,0,0), p(-1,0,0)};
//            case 3:
//                return new Point3D[]{
//                        p(1,0,0), p(cos(120),0,sin(120)), p(cos(240),0,sin(240))
//                };
//            case 4: {
//                double a = 1.0/Math.sqrt(3);
//                return new Point3D[]{p(a,a,a),p(a,-a,-a),p(-a,a,-a),p(-a,-a,a)};
//            }
//            case 5:
//                return new Point3D[]{
//                        p(0,1,0),p(0,-1,0),p(1,0,0),
//                        p(cos(120),0,sin(120)),p(cos(240),0,sin(240))
//                };
//            case 6:
//                return new Point3D[]{
//                        p(1,0,0),p(-1,0,0),p(0,1,0),p(0,-1,0),p(0,0,1),p(0,0,-1)
//                };
//            default: {
//                Point3D[] pts = new Point3D[Math.max(1, steric)];
//                for (int i = 0; i < pts.length; i++) {
//                    double a = 2*Math.PI*i/pts.length;
//                    pts[i] = p(Math.cos(a),0,Math.sin(a));
//                }
//                return pts;
//            }
//        }
//    }
//
//    private static Point3D rodrigues(Point3D v, Point3D k, double t) {
//        return v.multiply(Math.cos(t))
//                .add(k.crossProduct(v).multiply(Math.sin(t)))
//                .add(k.multiply(k.dotProduct(v)*(1-Math.cos(t))));
//    }
//
//    private static Point3D norm(Point3D v) {
//        double m = v.magnitude();
//        return m < 1e-10 ? p(1,0,0) : v.multiply(1.0/m);
//    }
//    private static Point3D perp(Point3D v) {
//        Point3D t = Math.abs(v.getX()) < 0.9 ? p(1,0,0) : p(0,1,0);
//        return norm(v.crossProduct(t));
//    }
//
//    // ── CPK colors ────────────────────────────────────────────────────────────
//
//    public static Color cpk(String symbol) {
//        return switch (symbol) {
//            case "H"  -> Color.web("#e8e8e8");
//            case "C"  -> Color.web("#404040");
//            case "N"  -> Color.web("#3050f8");
//            case "O"  -> Color.web("#ff0d0d");
//            case "F"  -> Color.web("#90e050");
//            case "Cl" -> Color.web("#1ff01f");
//            case "Br" -> Color.web("#a62929");
//            case "S"  -> Color.web("#ffcc00");
//            case "P"  -> Color.web("#ff8000");
//            case "I"  -> Color.web("#940094");
//            case "B"  -> Color.web("#ffb5b5");
//            case "Si" -> Color.web("#f0c8a0");
//            case "Xe" -> Color.web("#429eb0");
//            case "Fe" -> Color.web("#e06633");
//            case "Pt" -> Color.web("#d0d0e0");
//            case "Au" -> Color.web("#ffd123");
//            default   -> Color.LIGHTGRAY;
//        };
//    }
//
//    private static Point3D p(double x, double y, double z) { return new Point3D(x,y,z); }
//    private static double cos(double d) { return Math.cos(Math.toRadians(d)); }
//    private static double sin(double d) { return Math.sin(Math.toRadians(d)); }
//    private static double clamp(double v, double a, double b) { return Math.max(a,Math.min(b,v)); }
//}

package com.example.simulearn;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builds the JavaFX 3D scene from a MoleculeGraph.
 * Lone pairs shown as small yellow spheres positioned around the central atom.
 */
public class Renderer3D {

    private static final Color LONE_PAIR_COLOR = Color.web("#CB3434", 0.95);

    public static Group build(MoleculeGraph graph, boolean showLonePairs) {
        Group group = new Group();
        if (graph.atomCount() == 0) return group;

        Set<MoleculeBond> drawn = new HashSet<>();

        for (MoleculeAtom atom : graph.getAtoms()) {

            // ── Atom sphere ──────────────────────────────────────────────────
            Sphere sphere = new Sphere(atom.radius());
            sphere.setTranslateX(atom.position.getX());
            sphere.setTranslateY(atom.position.getY());
            sphere.setTranslateZ(atom.position.getZ());
            PhongMaterial mat = new PhongMaterial(cpk(atom.symbol));
            mat.setSpecularColor(Color.WHITE);
            mat.setSpecularPower(40);
            sphere.setMaterial(mat);
            group.getChildren().add(sphere);

            // ── Bonds ────────────────────────────────────────────────────────
            for (MoleculeBond bond : atom.bonds) {
                if (drawn.contains(bond)) continue;
                drawn.add(bond);
                drawBond(group, bond);
            }

            // ── Lone pairs ───────────────────────────────────────────────────
            if (showLonePairs) {
                if (atom.lonePairPositions != null) {
                    drawLonePairsExplicit(group, atom);
                }
            }
        }
        return group;
    }

    // ONLY FOR INORGANIC COMPOUND
    private static void drawLonePairsExplicit(Group group, MoleculeAtom atom) {
        if (atom.lonePairPositions == null) return;
        PhongMaterial mat = new PhongMaterial(LONE_PAIR_COLOR);
        mat.setSpecularColor(Color.WHITE);
        mat.setSpecularPower(30);

        double smallR = 5.5;
        double spread = 6.0;

        for (Point3D lpPos : atom.lonePairPositions) {
            Point3D dir = lpPos.magnitude() > 0.01 ? lpPos.normalize() : new Point3D(1, 0, 0);

            Point3D perpVec = perp(dir).multiply(spread);

            Point3D base = new Point3D(
                    atom.position.getX() + lpPos.getX(),
                    atom.position.getY() + lpPos.getY(),
                    atom.position.getZ() + lpPos.getZ()
            );
            Point3D pos1 = base.add(perpVec);
            Point3D pos2 = base.subtract(perpVec);

            Sphere s1 = new Sphere(smallR);
            s1.setTranslateX(pos1.getX());
            s1.setTranslateY(pos1.getY());
            s1.setTranslateZ(pos1.getZ());
            s1.setMaterial(mat);
            group.getChildren().add(s1);

            Sphere s2 = new Sphere(smallR);
            s2.setTranslateX(pos2.getX());
            s2.setTranslateY(pos2.getY());
            s2.setTranslateZ(pos2.getZ());
            s2.setMaterial(mat);
            group.getChildren().add(s2);
        }
    }

    // ── Bond rendering ────────────────────────────────────────────────────────

    private static void drawBond(Group group, MoleculeBond bond) {
        Point3D p1 = bond.a1.position;
        Point3D p2 = bond.a2.position;
        Color color = Color.web("#aaaaaa");

        if (bond.order == 1) {
            cylinder(group, p1, p2, 4.5, color);
        } else if (bond.order == 2) {
            Point3D off = perpOffset(p2.subtract(p1), 5.5);
            cylinder(group, p1.add(off), p2.add(off), 3.0, color);
            cylinder(group, p1.subtract(off), p2.subtract(off), 3.0, color);
        } else if (bond.order == 3) {
            Point3D off = perpOffset(p2.subtract(p1), 6.5);
            cylinder(group, p1, p2, 2.5, color);
            cylinder(group, p1.add(off), p2.add(off), 2.0, color);
            cylinder(group, p1.subtract(off), p2.subtract(off), 2.0, color);
        }
    }

    private static void cylinder(Group group, Point3D p1, Point3D p2,
                                 double radius, Color color) {
        Point3D dir = p2.subtract(p1);
        double len = dir.magnitude();
        if (len < 0.01) return;

        Cylinder cyl = new Cylinder(radius, len);
        PhongMaterial mat = new PhongMaterial(color);
        mat.setSpecularColor(Color.web("#888888"));
        cyl.setMaterial(mat);

        Point3D mid = p1.midpoint(p2);
        cyl.setTranslateX(mid.getX());
        cyl.setTranslateY(mid.getY());
        cyl.setTranslateZ(mid.getZ());

        Point3D yAxis   = new Point3D(0, 1, 0);
        Point3D normDir = dir.normalize();
        Point3D cross   = yAxis.crossProduct(normDir);
        double  dot     = yAxis.dotProduct(normDir);
        double  angle   = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, dot))));

        if (cross.magnitude() > 1e-6) {
            cyl.setRotationAxis(cross);
            cyl.setRotate(angle);
        } else if (dot < 0) {
            cyl.setRotate(180);
        }
        group.getChildren().add(cyl);
    }

    private static Point3D perpOffset(Point3D dir, double magnitude) {
        Point3D n = dir.normalize();
        Point3D t = Math.abs(n.getX()) < 0.9 ? new Point3D(1,0,0) : new Point3D(0,1,0);
        return n.crossProduct(t).normalize().multiply(magnitude);
    }

    // ── Geometry helpers (same as CoordBuilder) ───────────────────────────────

    private static Point3D[] idealDirections(int steric) {
        switch (steric) {
            case 1:  return new Point3D[]{p(1,0,0)};
            case 2:  return new Point3D[]{p(1,0,0), p(-1,0,0)};
            case 3:
                return new Point3D[]{
                        p(1,0,0), p(cos(120),0,sin(120)), p(cos(240),0,sin(240))
                };
            case 4: {
                double a = 1.0/Math.sqrt(3);
                return new Point3D[]{p(a,a,a),p(a,-a,-a),p(-a,a,-a),p(-a,-a,a)};
            }
            case 5:
                return new Point3D[]{
                        p(0,1,0),p(0,-1,0),p(1,0,0),
                        p(cos(120),0,sin(120)),p(cos(240),0,sin(240))
                };
            case 6:
                return new Point3D[]{
                        p(1,0,0),p(-1,0,0),p(0,1,0),p(0,-1,0),p(0,0,1),p(0,0,-1)
                };
            default: {
                Point3D[] pts = new Point3D[Math.max(1, steric)];
                for (int i = 0; i < pts.length; i++) {
                    double a = 2*Math.PI*i/pts.length;
                    pts[i] = p(Math.cos(a),0,Math.sin(a));
                }
                return pts;
            }
        }
    }

    private static Point3D rodrigues(Point3D v, Point3D k, double t) {
        return v.multiply(Math.cos(t))
                .add(k.crossProduct(v).multiply(Math.sin(t)))
                .add(k.multiply(k.dotProduct(v)*(1-Math.cos(t))));
    }

    private static Point3D norm(Point3D v) {
        double m = v.magnitude();
        return m < 1e-10 ? p(1,0,0) : v.multiply(1.0/m);
    }
    private static Point3D perp(Point3D v) {
        Point3D t = Math.abs(v.getX()) < 0.9 ? p(1,0,0) : p(0,1,0);
        return norm(v.crossProduct(t));
    }

    // ── CPK colors ────────────────────────────────────────────────────────────

    public static Color cpk(String symbol) {
        return switch (symbol) {
            case "H"  -> Color.web("#e8e8e8");
            case "C"  -> Color.web("#404040");
            case "N"  -> Color.web("#3050f8");
            case "O"  -> Color.web("#ff0d0d");
            case "F"  -> Color.web("#90e050");
            case "Cl" -> Color.web("#1ff01f");
            case "Br" -> Color.web("#a62929");
            case "S"  -> Color.web("#ffcc00");
            case "P"  -> Color.web("#ff8000");
            case "I"  -> Color.web("#940094");
            case "B"  -> Color.web("#ffb5b5");
            case "Si" -> Color.web("#f0c8a0");
            case "Xe" -> Color.web("#429eb0");
            case "Fe" -> Color.web("#e06633");
            case "Pt" -> Color.web("#d0d0e0");
            case "Au" -> Color.web("#ffd123");
            default   -> Color.LIGHTGRAY;
        };
    }

    private static Point3D p(double x, double y, double z) { return new Point3D(x,y,z); }
    private static double cos(double d) { return Math.cos(Math.toRadians(d)); }
    private static double sin(double d) { return Math.sin(Math.toRadians(d)); }
    private static double clamp(double v, double a, double b) { return Math.max(a,Math.min(b,v)); }
}