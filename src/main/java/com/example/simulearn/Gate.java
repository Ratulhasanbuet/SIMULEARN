package com.example.simulearn;

public class Gate {
    String name;
    NodePoint operand1;
    NodePoint operand2;
    NodePoint output;
    Gate(String name,NodePoint operand1,NodePoint operand2,NodePoint output)
    {
        this.name=name;
        this.operand1=operand1;
        this.operand2=operand2;
        this.output=output;
    }
    public void compute()
    {
        if(name.equals("AND"))
        {
            output.value = operand1.value && operand2.value;
            output.getLabel().setText(output.getCharacter()+": "+output.getValue());
        }
        else if(name.equals("OR"))
        {
            output.value = operand1.value || operand2.value;
            output.getLabel().setText(output.getCharacter()+": "+output.getValue());
        }
        else if (name.equals("NOT")) {
            output.value = !operand1.value;
            output.getLabel().setText(output.getCharacter()+": "+output.getValue());
        } else if (name.equals("NAND")) {
            output.value = !(operand1.value && operand2.value);
            output.getLabel().setText(output.getCharacter()+": "+output.getValue());
        } else if (name.equals("NOR")) {
            output.value = !(operand1.value || operand2.value);
            output.getLabel().setText(output.getCharacter()+": "+output.getValue());
        }
    }
}
