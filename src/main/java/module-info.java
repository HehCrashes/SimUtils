module com.github.hehcrashes.simutils {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.github.hehcrashes.simutils.magic_circle to javafx.fxml;
    exports com.github.hehcrashes.simutils.magic_circle;
    exports com.github.hehcrashes.simutils.magic_circle.controller;
    opens com.github.hehcrashes.simutils.magic_circle.controller to javafx.fxml;
}