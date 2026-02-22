package com.example.simulearn;

import javafx.scene.paint.Color;

public class Salt{
    String name;
    Color color;
    String colorName;
    Salt(String name,String colorName,Color color)
    {
        this.name=name;
        this.color=color;
        this.colorName=colorName;
    }
    public String getName(){return name;}
    public Color getColor(){return color;}
    public String getColorName(){return colorName;}
};