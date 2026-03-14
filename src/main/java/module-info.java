module com.example.simulearn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;

    // For SVG/Image handling
    requires java.desktop;
    requires java.xml;


    // Batik - minimum required
    requires transitive batik.transcoder;
    requires transitive batik.codec;


    requires javafx.base;
    requires java.sql;
    requires javax.mail.api;
    requires annotations;
    opens com.example.simulearn to javafx.fxml;
    exports com.example.simulearn;
    exports com.example.simulearn.SimuLearn.Biology.Ligation;
    opens com.example.simulearn.SimuLearn.Biology.Ligation to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Biology.MicropipettingSolution;
    opens com.example.simulearn.SimuLearn.Biology.MicropipettingSolution to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Biology.PPCC;
    opens com.example.simulearn.SimuLearn.Biology.PPCC to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Chemistry.RealMolecule;
    opens com.example.simulearn.SimuLearn.Chemistry.RealMolecule to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Chemistry.MultiMolecule;
    opens com.example.simulearn.SimuLearn.Chemistry.MultiMolecule to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Physics.Projectile;
    opens com.example.simulearn.SimuLearn.Physics.Projectile to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Chemistry;
    opens com.example.simulearn.SimuLearn.Chemistry to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Biology;
    opens com.example.simulearn.SimuLearn.Biology to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Chemistry.FlameTest;
    opens com.example.simulearn.SimuLearn.Chemistry.FlameTest to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Physics.Buoyancy;
    opens com.example.simulearn.SimuLearn.Physics.Buoyancy to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Physics.LogicCircuit;
    opens com.example.simulearn.SimuLearn.Physics.LogicCircuit to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Physics;
    opens com.example.simulearn.SimuLearn.Physics to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Math.Vector;
    opens com.example.simulearn.SimuLearn.Math.Vector to javafx.fxml;
    exports com.example.simulearn.SimuLearn.Math;
    opens com.example.simulearn.SimuLearn.Math to javafx.fxml;
    exports com.example.simulearn.SimuLearn;
    opens com.example.simulearn.SimuLearn to javafx.fxml;
    exports com.example.simulearn.Information;
    opens com.example.simulearn.Information to javafx.fxml;
    opens com.example.simulearn.SimuLearn.Math.Euler to javafx.fxml;
    opens com.example.simulearn.SimuLearn.Math.Histogram to javafx.fxml;
}