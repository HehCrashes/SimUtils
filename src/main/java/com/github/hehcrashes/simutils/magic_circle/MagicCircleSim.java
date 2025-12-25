package com.github.hehcrashes.simutils.magic_circle;

import com.github.hehcrashes.simutils.magic_circle.res.ExecutionContext;
import com.github.hehcrashes.simutils.magic_circle.res.ResourceManager;
import com.github.hehcrashes.simutils.magic_circle.res.ResourceTile;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.RuneCWD;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.HiMarker;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

        TreeItem<String> workspaceRoot = new TreeItem<>("工作区");
        workspaceRoot.setExpanded(true);

        TreeView<String> sceneTree = new TreeView<>(workspaceRoot);
        sceneTree.setShowRoot(false);

        sceneTree.setPrefWidth(220);

        sceneTree.setOnDragOver(e -> {

            TreeItem<String> target = sceneTree.getSelectionModel().getSelectedItem();
            if (target == null) return;

            Dragboard db = e.getDragboard();
            String data = db.getString();

            if (data == null) return;

            // 允许拖环、符文、标定
            if (data.startsWith("RING:") ||
                    data.startsWith("RUNE:") ||
                    data.startsWith("MARKER:")) {

                e.acceptTransferModes(TransferMode.COPY);
            }
        });

        sceneTree.setOnDragDropped(e -> {

            Dragboard db = e.getDragboard();
            String data = db.getString();
            TreeItem<String> target = sceneTree.getSelectionModel().getSelectedItem();

            if (data == null || target == null) return;

            // --------- 拖入「环」 ---------
            if (data.startsWith("RING:")) {

                // 只能加到根节点
                if (target.getParent() != null) {
                    System.out.println("环只能放在根节点下");
                    return;
                }

                try {
                    Class<?> clazz = Class.forName(data.substring(5));
                    Ring r = (Ring) clazz.getDeclaredConstructor().newInstance();

                    TreeItem<String> ringNode = new TreeItem<>("环：" + r.getDisplayName());
                    target.getChildren().add(ringNode);

                    addRingSlots(ringNode, r, 1);   // 🔥 展开槽位（层级=1）

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // --------- 拖入符文（只能拖到符文槽） ---------
            if (data.startsWith("RUNE:") && target.getValue().startsWith("符文槽")) {
                target.setValue("符文：" + data.substring(5));
            }

            // --------- 拖入标定（只能拖到标定槽） ---------
            if (data.startsWith("MARKER:") && target.getValue().startsWith("标定槽")) {
                target.setValue("标定：" + data.substring(7));
            }

            e.setDropCompleted(true);
        });


        ToolBar sceneToolbar = new ToolBar();

        Button addRootBtn = new Button("+");
        addRootBtn.setTooltip(new Tooltip("创建法阵"));

        addRootBtn.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog("法阵001");
            dialog.setHeaderText("请输入法阵名称");

            dialog.showAndWait().ifPresent(name -> {
                TreeItem<String> mc = new TreeItem<>(name);
                workspaceRoot.getChildren().add(mc);
            });
        });

        sceneToolbar.getItems().add(addRootBtn);

        VBox scenePanel = new VBox(sceneToolbar, sceneTree);
        VBox.setVgrow(sceneTree, Priority.ALWAYS);


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
        // 环资源列表
        TilePane ringGrid = new TilePane();
        ringGrid.setHgap(10);
        ringGrid.setVgap(10);
        ringGrid.setPrefColumns(6);
        ringGrid.setPadding(new Insets(10));
        ringGrid.setTileAlignment(Pos.CENTER);

        Image ringIcon = new Image(getClass().getResource("/icons/ring.png").toExternalForm());

        for (Ring ring : ResourceManager.rings) {
            VBox tile = ResourceTile.create(ringIcon, ring.getDisplayName());

            tile.setOnDragDetected(e -> {
                Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString("RING:" + ring.getClass().getName());
                db.setContent(cc);
                e.consume();
            });

            ringGrid.getChildren().add(tile);
        }


        // 符文资源列表
        TilePane runeGrid = new TilePane();
        runeGrid.setHgap(10);
        runeGrid.setVgap(10);
        runeGrid.setPrefColumns(6);
        runeGrid.setPadding(new Insets(10));

        Image runeIcon = new Image(getClass().getResource("/icons/rune.png").toExternalForm());

        for (Rune rune : ResourceManager.runes) {
            VBox tile = ResourceTile.create(runeIcon, rune.getDisplayName());

            tile.setOnDragDetected(e -> {
                Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString("RUNE:" + rune.getClass().getName());
                db.setContent(cc);
            });

            runeGrid.getChildren().add(tile);
        }

        // 标定资源列表
        TilePane markerGrid = new TilePane();
        markerGrid.setHgap(10);
        markerGrid.setVgap(10);
        markerGrid.setPrefColumns(6);
        markerGrid.setPadding(new Insets(10));

        Image markerIcon = new Image(getClass().getResource("/icons/marker.png").toExternalForm());

        for (Marker marker : ResourceManager.markers) {
            VBox tile = ResourceTile.create(markerIcon, marker.getDisplayName());

            tile.setOnDragDetected(e -> {
                Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString("MARKER:" + marker.getClass().getName());
                db.setContent(cc);
            });

            markerGrid.getChildren().add(tile);
        }


        // 预制件
        ListView<Ring> prefabList = new ListView<>();
        prefabList.getItems().addAll(ResourceManager.prefabs);

        // 添加到 TabPane
        TabPane resourceTabs = new TabPane();
        resourceTabs.getTabs().addAll(
                new Tab("环", new ScrollPane(ringGrid)),
                new Tab("符文", new ScrollPane(runeGrid)),
                new Tab("标定", new ScrollPane(markerGrid)),
                new Tab("预制件", prefabList)
        );
        resourceTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        resourceTabs.setPrefHeight(160);

        // ---------- 中间主区域（画布 + 资源） ----------
        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(canvasPane);
        centerPane.setBottom(resourceTabs);

        // ---------- 整体分割：左 | 中 | 右 ----------
        SplitPane mainSplit = new SplitPane();
        mainSplit.getItems().addAll(scenePanel, centerPane, propertyBox);
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

    private void addRingSlots(TreeItem<String> ringNode, Ring ring, int depth) {

        // depth=1 主环  depth=2 子环  depth=3 孙环

        // 1. 符文槽
        for (int i = 0; i < ring.getRuneSlots(); i++) {
            ringNode.getChildren().add(new TreeItem<>("符文槽 #" + i));
        }

        // 2. 子环槽
        if (depth < 3) {   // 最多三层
            for (int i = 0; i < ring.getChildSlots(); i++) {

                TreeItem<String> childRing = new TreeItem<>("子环槽 #" + i);
                ringNode.getChildren().add(childRing);
            }
        }

        // 3. 标定槽
        ringNode.getChildren().add(new TreeItem<>("标定槽"));
    }
    public static void main(String[] args) {
        launch();
    }
}
