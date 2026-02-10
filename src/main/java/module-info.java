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
    opens com.example.simulearn to javafx.fxml;
    exports com.example.simulearn;
}