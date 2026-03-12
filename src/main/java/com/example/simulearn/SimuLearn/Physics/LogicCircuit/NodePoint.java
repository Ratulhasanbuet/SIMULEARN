package com.example.simulearn.SimuLearn.Physics.LogicCircuit;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;


public class NodePoint {
    Circle node;
    Label label;
    Boolean value;
    Character character;
    int usage;
    NodePoint(Circle node, Label label, Boolean value,Character c)
    {
        this.node=node;
        this.label=label;
        this.value=value;
        this.character=c;
        this.usage=0;
    }

    public void ToggleValue() {
        this.value = !value;
    }
    public Boolean getValue(){return value;}
    public Circle getNode(){return node;}
    public Label getLabel(){return label;}
    public Character getCharacter(){return character;}
    public void incrementUsage(){usage++;}
    public int getUsage(){return usage;}
}
