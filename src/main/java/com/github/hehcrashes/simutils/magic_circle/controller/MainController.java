package com.github.hehcrashes.simutils.magic_circle.controller;

import com.github.hehcrashes.simutils.magic_circle.res.*;
import com.github.hehcrashes.simutils.magic_circle.res.marker.Marker;
import com.github.hehcrashes.simutils.magic_circle.res.ring.Ring;
import com.github.hehcrashes.simutils.magic_circle.res.rune.Rune;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

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

        if (data.startsWith("RING:") &&
                (target.getValue().type.equals("WORKSPACE") ||
                 target.getValue().type.equals("SLOT") ||
                 target.getValue().type.equals("MC"))) {
            try {
                Class<?> clazz = Class.forName(data.substring(5));
                Ring r = (Ring) clazz.getDeclaredConstructor().newInstance();

                TreeItem<NodeData> ringNode = new TreeItem<>(new NodeData("RING", r));
                target.getChildren().add(ringNode);
                target.setExpanded(true);
                addRingSlots(ringNode, r, 1);
                System.out.println("Added ringNode: " + ringNode.getValue());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (data.startsWith("RUNE:") && target.getValue().type.equals("RUNE_SLOT")) {
            target.setValue(new NodeData("RUNE", data.substring(5)));
            System.out.println("Added RUNE: " + data.substring(5));
        }

        if (data.startsWith("MARKER:") && target.getValue().type.equals("MARKER_SLOT")) {
            target.setValue(new NodeData("MARKER", data.substring(7)));
            System.out.println("Added MARKER: " + data.substring(7));
        }

        sceneTree.refresh();
        printTree(workspaceRoot, 0); // 打印结构
    }

    private void addRingSlots(TreeItem<NodeData> ringNode, Ring ring, int depth) {
        for (int i = 0; i < ring.getRuneSlots(); i++)
            ringNode.getChildren().add(new TreeItem<>(new NodeData("RUNE_SLOT", "符文槽 #" + i)));

        if (depth < 3) {
            for (int i = 0; i < ring.getChildSlots(); i++)
                ringNode.getChildren().add(new TreeItem<>(new NodeData("SLOT", "子环槽 #" + i)));
        }

        ringNode.getChildren().add(new TreeItem<>(new NodeData("MARKER_SLOT", "标定槽")));
    }

    private void printTree(TreeItem<NodeData> node, int depth) {
        System.out.println(" ".repeat(depth * 2) + node.getValue());
        for (TreeItem<NodeData> child : node.getChildren()) {
            printTree(child, depth + 1);
        }
    }
}
