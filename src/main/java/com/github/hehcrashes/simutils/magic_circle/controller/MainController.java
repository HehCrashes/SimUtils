package com.github.hehcrashes.simutils.magic_circle.controller;

import com.github.hehcrashes.simutils.magic_circle.res.*;
import com.github.hehcrashes.simutils.magic_circle.res.marker.EmptyMarker;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.EmptyRing;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring44;
import com.github.hehcrashes.simutils.magic_circle.res.rune.EmptyRune;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private TreeView<NodeData> sceneTree;
    @FXML private ToolBar sceneToolbar;
    @FXML private Button addRootBtn;
    @FXML private TilePane ringGrid;
    @FXML private TilePane runeGrid;
    @FXML private TilePane markerGrid;
    @FXML private Canvas canvas;

    private TreeItem<NodeData> workspaceRoot;

    @FXML
    public void initialize() {

        // 初始化工作区
        workspaceRoot = new TreeItem<>(new NodeData("WORKSPACE", null));
        workspaceRoot.setExpanded(true);
        sceneTree.setRoot(workspaceRoot);
        sceneTree.setShowRoot(false);

        // 创建法阵按钮
        addRootBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("法阵001");
            dialog.setHeaderText("请输入法阵名称");
            dialog.showAndWait().ifPresent(name -> {
                TreeItem<NodeData> mc = new TreeItem<>(new NodeData("MC", name));
                workspaceRoot.getChildren().add(mc);
                workspaceRoot.setExpanded(true);
                sceneTree.refresh();
                System.out.println("Added MC: " + name);
            });
        });

        // 初始化资源面板
        Image ringIcon = new Image(getClass().getResource("/icons/ring.png").toExternalForm());
        for (Ring ring : ResourceManager.rings) {
            VBox tile = ResourceTile.create(ringIcon, ring.getDisplayName());
            tile.setOnDragDetected(e -> startDrag(tile, "RING:" + ring.getClass().getName(), ringIcon));
            attachPreviewMenu(tile, ring);
            ringGrid.getChildren().add(tile);
        }

        Image runeIcon = new Image(getClass().getResource("/icons/rune.png").toExternalForm());
        for (Rune rune : ResourceManager.runes) {
            VBox tile = ResourceTile.create(runeIcon, rune.getDisplayName());
            tile.setOnDragDetected(e -> startDrag(tile, "RUNE:" + rune.getClass().getName(), runeIcon));
            runeGrid.getChildren().add(tile);
        }

        Image markerIcon = new Image(getClass().getResource("/icons/marker.png").toExternalForm());
        for (Marker marker : ResourceManager.markers) {
            VBox tile = ResourceTile.create(markerIcon, marker.getDisplayName());
            tile.setOnDragDetected(e -> startDrag(tile, "MARKER:" + marker.getClass().getName(), markerIcon));
            attachPreviewMenu(tile, marker);
            markerGrid.getChildren().add(tile);
        }

        // TreeView 拖放
        sceneTree.setCellFactory(tv -> new TreeCell<>() {
            @Override
            protected void updateItem(NodeData item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }

            {
                setOnDragOver(e -> {
                    if (getItem() == null) return;
                    Dragboard db = e.getDragboard();
                    if (db.hasString()) {
                        e.acceptTransferModes(TransferMode.COPY);
                        System.out.println("DragOver on: " + getItem());
                    }
                    e.consume();
                });

                setOnDragDropped(e -> {
                    if (getItem() == null) return;
                    Dragboard db = e.getDragboard();
                    TreeItem<NodeData> target = getTreeItem();
                    System.out.println("DragDropped on: " + getItem() + ", target TreeItem: " + target.getValue());
                    handleDrop(target, db);
                    e.setDropCompleted(true);
                    e.consume();
                    sceneTree.refresh(); // 确保 UI 更新
                });
            }
        });
    }

    private void startDrag(VBox tile, String data, Image dragView) {
        Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
        ClipboardContent cc = new ClipboardContent();
        cc.putString(data);
        db.setContent(cc);
        db.setDragView(dragView);
        System.out.println("Drag started: " + data);
    }

    private void handleDrop(TreeItem<NodeData> target, Dragboard db) {
        String data = db.getString();
        if (data == null) return;

        System.out.println("handleDrop called! target = " + target.getValue() + ", data = " + data);

        if (data.startsWith("RING:") && (target.getValue().type.equals("RING") || target.getValue().type.equals("MC"))) {
            try {
                Class<?> clazz = Class.forName(data.substring(5));
                Ring r = (Ring) clazz.getDeclaredConstructor().newInstance();
                target.setValue(new NodeData("RING", r));
                target.getChildren().clear();
                addRingSlots(target, r, getTreeDepth(target));
                System.out.println("Added ringNode: " + r.getDisplayName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (data.startsWith("RUNE:") && target.getValue().type.equals("RUNE")) {
            try {
                Class<?> clazz = Class.forName(data.substring(5));
                Rune rune = (Rune) clazz.getDeclaredConstructor().newInstance();
                target.setValue(new NodeData("RUNE", rune));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (data.startsWith("MARKER:") && target.getValue().type.equals("MARKER")) {
            try {
                Class<?> clazz = Class.forName(data.substring(7));
                Marker marker = (Marker) clazz.getDeclaredConstructor().newInstance();
                target.setValue(new NodeData("MARKER", marker));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        sceneTree.refresh();
        printTree(workspaceRoot, 0); // 打印结构

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Render.render(workspaceRoot.getChildren().getFirst(), canvas.getGraphicsContext2D(), canvas.getWidth() / 2.0,canvas.getHeight() / 2.0,1);
    }
    private void addRingSlots(TreeItem<NodeData> ringNode, Ring ring, int depth) {
        // 添加符文
        for (int i = 0; i < ring.getRuneSlots(); i++) {
            ringNode.getChildren().add(new TreeItem<>(new NodeData("RUNE", new EmptyRune())));
        }

        // 只在 depth < MAX_DEPTH 时添加子环槽
        int MAX_DEPTH = 3;
        if (depth < MAX_DEPTH) {
            for (int i = 0; i < ring.getChildSlots(); i++) {
                TreeItem<NodeData> slot = new TreeItem<>(new NodeData("RING", new EmptyRing()));
                ringNode.getChildren().add(slot);
            }
        }

        // 添加标定
        ringNode.getChildren().add(new TreeItem<>(new NodeData("MARKER", new EmptyMarker())));
    }
    private int getTreeDepth(TreeItem<?> item) {
        int depth = 0;
        TreeItem<?> parent = item.getParent();
        while (parent != null) {
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }
    private void printTree(TreeItem<NodeData> node, int depth) {
        System.out.println(" ".repeat(depth * 2) + node.getValue() + " :: " + node.getValue().type);
        for (TreeItem<NodeData> child : node.getChildren()) {
            printTree(child, depth + 1);
        }
    }
    private <T extends IDisplayable & IRenderable> void attachPreviewMenu(VBox tile, T resource) {
        MenuItem previewItem = new MenuItem("预览：" + resource.getDisplayName());

        previewItem.setOnAction(e -> {
            try {
                Stage previewStage = new Stage();
                previewStage.setTitle("预览");
                double w = 320, h = 320;
                Canvas previewCanvas = new Canvas(w, h);
                GraphicsContext pgc = previewCanvas.getGraphicsContext2D();
                pgc.setFill(Color.color(0.12, 0.12, 0.12));
                pgc.fillRect(0, 0, w, h);
                resource.review(pgc);
                StackPane root = new StackPane(previewCanvas);
                root.setPadding(new Insets(10));
                previewStage.setScene(new Scene(root));
                previewStage.initModality(Modality.NONE);
                previewStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "预览失败: " + ex.getMessage()).showAndWait();
            }
        });
        ContextMenu menu = new ContextMenu(previewItem);
        tile.setOnContextMenuRequested(ev -> menu.show(tile, ev.getScreenX(), ev.getScreenY()));
    }


}
