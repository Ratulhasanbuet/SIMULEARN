package com.example.simulearn;

public class MoleculeBond {

    public final MoleculeAtom a1;
    public final MoleculeAtom a2;
    public int order; // 1=single, 2=double, 3=triple

    public MoleculeBond(MoleculeAtom a1, MoleculeAtom a2, int order) {
        this.a1 = a1;
        this.a2 = a2;
        this.order = order;
    }

    public MoleculeAtom other(MoleculeAtom a) {
        return a == a1 ? a2 : a1;
    }

    public boolean connects(MoleculeAtom x, MoleculeAtom y) {
        return (a1 == x && a2 == y) || (a1 == y && a2 == x);
    }
}
