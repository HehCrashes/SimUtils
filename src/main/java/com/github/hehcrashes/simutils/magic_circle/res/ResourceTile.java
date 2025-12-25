package com.github.hehcrashes.simutils.magic_circle.res;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ResourceTile {

    public static VBox create(Image icon, String name) {

        ImageView iv = new ImageView(icon);
        iv.setFitWidth(48);
        iv.setFitHeight(48);

        Label label = new Label(name);
        label.setWrapText(true);
        label.setMaxWidth(120);
        label.setAlignment(Pos.CENTER);

        VBox box = new VBox(iv, label);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(6);
        box.setPadding(new Insets(8));

        box.setStyle("""
                -fx-background-color: #f4f4f4;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: #bbb;
                """);

        box.setOnMouseEntered(e -> box.setStyle("""
                -fx-background-color: #fff4d6;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: #d8a200;
                """));


        box.setOnMouseExited(e -> box.setStyle("""
                -fx-background-color: #f4f4f4;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: #bbb;
                """));

        box.setOnMouseEntered(e -> {
            box.setScaleX(1.025);
            box.setScaleY(1.025);
        });

        box.setOnMouseExited(e -> {
            box.setScaleX(1.0);
            box.setScaleY(1.0);
        });

        return box;
    }
}
