package com.github.hehcrashes.simutils.magic_circle;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.marker.HiMarker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.RuneCWD;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class MagicCircleSim extends Application {

    @Override
    public void start(Stage stage) {

        // ---------- 左侧：场景列表 ----------
        /**
         * 四分四位点环 主环
         * ├ 顺时针逐步深度 符文
         * ├ 缓存环 符文
         * ├ 条件 符文
         * ├ 返回 符文
         * ├ 无分无位点环 从环
         * │  ├ 顺时针逐步深度 符文
         * │  └ 充能(R) 标定
         * ├ 二分无位点环 从环
         * │  ├ 顺时针逐步深度 符文
         * │  ├ 硬编码缓存(坐标) 符文
         * │  └ 容器检测 标定
         * ├ 无分无位点环 从环
         * │  ├ 顺时针逐步深度 符文
         * │  └ 重置 标定
         * ├ 空 从环
         * └ 充能(R) 标定
         *  约等于：
         *  send(Item);
         *  if(!container.hasItem()){
         *      send(Item);
         *  }
         */

        TreeItem<String> send = new TreeItem<>("无分无位点圆环 从环");
        send.getChildren().addAll(new TreeItem<>("顺时针逐步深度 符文"), new TreeItem<>("充能(R) 标定"));

        TreeItem<String> get = new TreeItem<>("二分无位点圆环 从环");
        get.getChildren().addAll(new TreeItem<>("顺时针逐步深度 符文"), new TreeItem<>("硬编码缓存(坐标) 符文"), new TreeItem<>("容器检测 标定"));

        TreeItem<String> reset = new TreeItem<>("无分无位点圆环 从环");
        reset.getChildren().addAll(new TreeItem<>("顺时针逐步深度 符文"), new TreeItem<>("重置 标定"));

        TreeItem<String> mainR = new TreeItem<>("四分四位点圆环 主环");
        mainR.getChildren().addAll(
                new TreeItem<>("顺时针逐步深度 符文"),
                new TreeItem<>("缓存环 符文"),
                new TreeItem<>("条件 符文"),
                new TreeItem<>("返回 符文"),
                send,
                get,
                reset,
                new TreeItem<>("空 从环"),
                new TreeItem<>("充能(R) 标定"));

        TreeItem<String> root = new TreeItem<>("法阵001");
        root.getChildren().add(mainR);
        TreeView<String> sceneTree = new TreeView<>(root);
        sceneTree.setPrefWidth(220);

        // ---------- 中间：画布 ----------
        Canvas canvas = new Canvas(600, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.DARKBLUE);
        gc.fillText("这里是画布", 270, 200);

        StackPane canvasPane = new StackPane(canvas);
        canvasPane.setStyle("-fx-background-color: #2b2b2b;");

        // ---------- 右侧：属性页（可折叠） ----------
        VBox propertyBox = new VBox(8);
        propertyBox.setFillWidth(true);

        TitledPane transformPane = new TitledPane("Transform", new Label("位置、旋转、缩放"));
        TitledPane materialPane  = new TitledPane("Material", new Label("材质设置"));
        TitledPane scriptPane    = new TitledPane("Scripts", new Label("脚本组件"));

        Accordion accordion = new Accordion(transformPane, materialPane, scriptPane);
        accordion.setExpandedPane(transformPane);

        propertyBox.getChildren().add(accordion);
        propertyBox.setPrefWidth(260);

        // ---------- 下方：资源页 ----------
        TabPane resourceTabs = new TabPane();
        resourceTabs.getTabs().addAll(
                new Tab("环", new Label("环列表")),
                new Tab("符文", new Label("符文资源列表")),
                new Tab("标定", new Label("标定资源列表")),
                new Tab("预制件", new Label("预制件资源列表"))
        );
        resourceTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        resourceTabs.setPrefHeight(160);

        // ---------- 中间主区域（画布 + 资源） ----------
        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(canvasPane);
        centerPane.setBottom(resourceTabs);

        // ---------- 整体分割：左 | 中 | 右 ----------
        SplitPane mainSplit = new SplitPane();
        mainSplit.getItems().addAll(sceneTree, centerPane, propertyBox);
        mainSplit.setDividerPositions(0.18, 0.78); // 初始比例

        Scene scene = new Scene(mainSplit, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("魔法阵编辑界面");
        stage.show();






        Ring44 rm = new Ring44();
        rm.getRunes().set(0, new RuneCWD());
        rm.setMarker(new HiMarker());
        ExecutionContext ctx = new ExecutionContext();
        ctx.run(rm);
    }

    public static void main(String[] args) {
        launch();
    }
}
