package com.github.hehcrashes.simutils.thermodynamics;

import com.github.hehcrashes.simutils.thermodynamics.Block.*;
import com.github.hehcrashes.simutils.thermodynamics.Renderer.HeatRenderer;
import com.github.hehcrashes.simutils.thermodynamics.Renderer.IBlockRenderer;
import com.github.hehcrashes.simutils.thermodynamics.Renderer.NormalRenderer;
import com.github.hehcrashes.simutils.thermodynamics.Renderer.TinHeatRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Supplier;

public class JavaFX_2D_Minecraft_Simulator extends Application {

    public static final int thermal_conductivity_speed = 10000;
    static final int COLS = 30;
    static final int ROWS = 20;
    static final int TILE = 28;

    Block[][] world = new Block[ROWS][COLS];

    Canvas canvas;
    GraphicsContext gc;

    Block selected = null;
    Point2D selectedPos = null;

    VBox propertyPane = new VBox(8);

    IBlockRenderer currentRenderer = new NormalRenderer();

    Timeline ticker;
    double tickIntervalMs = 50; // 默认 50ms/tick → 20 tick/s

    static class BlockChoice {
        final String name;
        final Supplier<Block> factory;
        final Node icon;
        BlockChoice(String name, Supplier<Block> factory, Node icon) { this.name = name; this.factory = factory; this.icon = icon; }
        @Override public String toString() { return name; }
    }

    @Override
    public void start(Stage primaryStage) {
        double roomTempK = 273.15 + 20;

        // 初始化世界
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                world[r][c] = new DirtBlock(r, c);
                world[r][c].setTemperatureK(roomTempK);
            }
        }

        BorderPane root = new BorderPane();
        canvas = new Canvas(COLS * TILE, ROWS * TILE);
        gc = canvas.getGraphicsContext2D();

        ToolBar top = new ToolBar();

        ComboBox<BlockChoice> placeChoice = new ComboBox<>();
        placeChoice.getItems().addAll(
                new BlockChoice("Dirt", () -> new DirtBlock(0,0), createIcon(Color.SIENNA)),
                new BlockChoice("Grass", () -> new GrassBlock(0,0), createIcon(Color.LIMEGREEN)),
                new BlockChoice("Iron", () -> new IronBlock(0,0), createIcon(Color.SILVER)),
                new BlockChoice("Constant", () -> new ConstantBlock(0,0), createIcon(Color.YELLOW))
        );
        placeChoice.setValue(placeChoice.getItems().get(0));

        placeChoice.setCellFactory(cb -> new ListCell<>() {
            @Override protected void updateItem(BlockChoice item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); }
                else { setGraphic(new HBox(6, item.icon, new Label(item.name))); }
            }
        });
        placeChoice.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(BlockChoice item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); }
                else { setGraphic(new HBox(6, item.icon, new Label(item.name))); }
            }
        });

        ChoiceBox<String> viewChoice = new ChoiceBox<>();
        viewChoice.getItems().addAll("Normal", "Heat", "TinHeat");
        viewChoice.setValue("Normal");
        viewChoice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if ("Normal".equals(n)) currentRenderer = new NormalRenderer();
            else if ("Heat".equals(n)) currentRenderer = new HeatRenderer();
            else if ("TinHeat".equals(n)) currentRenderer = new TinHeatRenderer();
            else currentRenderer = new NormalRenderer();
            draw();
        });

        top.getItems().addAll(new Label("Place:"), placeChoice, new Separator(), new Label("View:"), viewChoice);

        Button pauseBtn = new Button("Pause");
        Button stepBtn = new Button("Step");
        Button resetSpeedBtn = new Button("Reset Speed (20/s)");
        Slider speedSlider = new Slider(50, 1000, tickIntervalMs);
        speedSlider.setPrefWidth(150);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(300);
        speedSlider.setMinorTickCount(2);
        speedSlider.setBlockIncrement(50);

        pauseBtn.setOnAction(e -> {
            if (ticker.getStatus() == Timeline.Status.RUNNING) { ticker.pause(); pauseBtn.setText("Resume"); }
            else { ticker.play(); pauseBtn.setText("Pause"); }
        });

        stepBtn.setOnAction(e -> { ticker.pause(); pauseBtn.setText("Resume"); tick(); });

        resetSpeedBtn.setOnAction(e -> {
            tickIntervalMs = 50;
            speedSlider.setValue(tickIntervalMs);
            ticker.stop();
            ticker.getKeyFrames().setAll(new KeyFrame(Duration.millis(tickIntervalMs), ev -> tick()));
            ticker.play();
        });

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            tickIntervalMs = newVal.doubleValue();
            ticker.stop();
            ticker.getKeyFrames().setAll(new KeyFrame(Duration.millis(tickIntervalMs), ev -> tick()));
            ticker.play();
        });

        top.getItems().addAll(new Separator(), new Label("Tick speed:"), speedSlider, pauseBtn, stepBtn, resetSpeedBtn);

        StackPane canvasWrapper = new StackPane(canvas);
        canvasWrapper.setPadding(new Insets(6));

        propertyPane.setPadding(new Insets(8));
        propertyPane.setPrefWidth(300);
        propertyPane.getChildren().add(new Label("Select a block to see properties"));

        root.setTop(top);
        root.setCenter(canvasWrapper);
        root.setRight(propertyPane);

        canvas.setOnMouseClicked(ev -> {
            int col = (int) (ev.getX() / TILE);
            int row = (int) (ev.getY() / TILE);
            if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;

            if (ev.getButton() == MouseButton.PRIMARY) {
                BlockChoice bc = placeChoice.getValue();
                Block nb = bc.factory.get();
                nb.row = row; nb.col = col;
                nb.setTemperatureK(273.15 + 20);
                world[row][col] = nb;
                select(row, col);
            } else if (ev.getButton() == MouseButton.SECONDARY) {
                select(row, col);
            }
            draw();
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Minecraft-like Simulator (Refactored)");
        primaryStage.show();

        ticker = new Timeline(new KeyFrame(Duration.millis(tickIntervalMs), e -> tick()));
        ticker.setCycleCount(Timeline.INDEFINITE);
        ticker.play();

        draw();
    }

    void select(int row, int col) {
        selected = world[row][col];
        selectedPos = new Point2D(row, col);
        refreshPropertyPane();
    }

    void refreshPropertyPane() {
        propertyPane.getChildren().clear();
        if (selected == null) { propertyPane.getChildren().add(new Label("No selection")); return; }

        Label title = new Label("Block: " + selected.getDisplayName());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        propertyPane.getChildren().add(title);
        propertyPane.getChildren().add(new Label("Position: " + selected.row + ", " + selected.col));

        double tempK = selected.getTemperatureK();
        double minK = 0;
        double maxK = selected.boiling_temperature;
        double meltK = selected.melting_temperature;
        double progress = (tempK - minK) / (maxK - minK);

        ProgressBar tempBar = new ProgressBar(progress);
        tempBar.setPrefWidth(200);
        tempBar.setPrefHeight(20);

        Rectangle meltMarker = new Rectangle(2, 20, Color.RED);
        double meltPos = (meltK - minK) / (maxK - minK) * 200;
        meltMarker.setTranslateX(meltPos - 100);

        StackPane tempStack = new StackPane();
        tempStack.setPrefWidth(200);
        tempStack.setPrefHeight(20);
        tempStack.getChildren().addAll(tempBar, meltMarker);

        propertyPane.getChildren().add(new Label(String.format("Temperature: %.1f K", tempK)));
        propertyPane.getChildren().add(tempStack);

        TextField energyField = new TextField(String.format("%.2f", selected.energy));
        energyField.setPrefWidth(100);
        Button setEnergyBtn = new Button("Set Energy");
        setEnergyBtn.setOnAction(e -> {
            try {
                double newEnergy = Double.parseDouble(energyField.getText());
                selected.energy = Math.max(0, Math.min(newEnergy, selected.m * selected.c * selected.boiling_temperature));
                draw();
                refreshPropertyPane();
            } catch (NumberFormatException ex) {
                energyField.setText(String.format("%.2f", selected.energy));
            }
        });
        HBox energyBox = new HBox(4, new Label("Energy:"), energyField, setEnergyBtn);
        propertyPane.getChildren().add(energyBox);

        Button remove = new Button("Remove Block");
        remove.setOnAction(e -> {
            int r = selected.row, c = selected.col;
            world[r][c] = new DirtBlock(r, c);
            selected = world[r][c];
            draw();
            refreshPropertyPane();
        });
        propertyPane.getChildren().add(remove);
    }

    void tick() {
        Block[][] snapshot = new Block[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                snapshot[r][c] = world[r][c].copy();
            }
        }

        double deltaTime = tickIntervalMs / 1000.0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                world[r][c].onTick(snapshot,world, r, c, deltaTime);
            }
        }

        draw();
        refreshPropertyPane();
    }

    void draw() {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) {
            Block b = world[r][c];
            double x = c * TILE, y = r * TILE;
            Color col = currentRenderer.render(b);
            gc.setFill(col);
            gc.fillRect(x, y, TILE, TILE);
            gc.setStroke(Color.rgb(40,40,40,0.6));
            gc.strokeRect(x+0.5, y+0.5, TILE-1, TILE-1);
        }

        if (selected != null && selectedPos != null) {
            int r = (int) selectedPos.getX();
            int c = (int) selectedPos.getY();
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(2);
            gc.strokeRect(c * TILE + 1, r * TILE + 1, TILE - 2, TILE - 2);
            gc.setLineWidth(1);
        }
    }

    Node createIcon(Color c) {
        Rectangle r = new Rectangle(12,12);
        r.setFill(c);
        r.setStroke(Color.BLACK);
        return r;
    }

    public static void main(String[] args) { launch(args); }
}
