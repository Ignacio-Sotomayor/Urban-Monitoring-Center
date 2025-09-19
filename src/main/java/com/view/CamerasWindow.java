package com.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class CamerasWindow extends JFrame {
    private GridPane grid;
    private boolean expanded = false;
    private StackPane expandedPane = null;

    public CamerasWindow() {
        super("Cameras Control");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            grid = new GridPane();
            grid.setHgap(5);
            grid.setVgap(5);

            grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 0, 0);
            grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 1, 0);
            grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 0, 1);
            grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 1, 1);

            fxPanel.setScene(new Scene(grid, 800, 600));
        });
    }

    private StackPane wrapMediaView(String path) {
        MediaView view = createMediaView(path);
        StackPane wrapper = new StackPane(view);

        wrapper.setOnMouseClicked(e -> {
            if (!expanded) {
                // Expand this video
                grid.getChildren().clear();
                grid.add(wrapper, 0, 0);
                wrapper.setPrefSize(800, 600);
                view.setFitWidth(780);
                view.setFitHeight(580);

                expanded = true;
                expandedPane = wrapper;
            } else {
                // Restore all videos
                grid.getChildren().clear();
                grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 0, 0);
                grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 1, 0);
                grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 0, 1);
                grid.add(wrapMediaView("C:\\Users\\FrAnC\\Downloads\\TestVideo.mp4"), 1, 1);

                expanded = false;
                expandedPane = null;
            }
        });

        return wrapper;
    }

    // Local video helper
    private MediaView createMediaView(String path) {
        File file = new File(path);
        Media media = new Media(file.toURI().toString());
        MediaPlayer player = new MediaPlayer(media);

        player.setOnReady(() -> {
            player.setMute(true);
            player.play();
        });

        MediaView view = new MediaView(player);
        view.setFitWidth(380);
        view.setFitHeight(280);
        view.setPreserveRatio(true);

        return view;
    }
}
