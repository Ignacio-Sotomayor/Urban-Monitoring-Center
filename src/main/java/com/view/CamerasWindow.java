package com.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class CamerasWindow extends JFrame {
    private GridPane grid;
    private boolean expanded = false;
    private StackPane expandedPane = null;
    private int currentIndex = 0;

    private final List<String> videoPaths = Arrays.asList(
            "/videos/TestVideo1.mp4",
            "/videos/TestVideo2.mp4",
            "/videos/TestVideo3.mp4",
            "/videos/TestVideo4.mp4"
    );

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

            for (int i = 0; i < videoPaths.size(); i++)
                grid.add(wrapMediaView(videoPaths.get(i), i), i % 2, i / 2);

            Scene scene = new Scene(grid, 800, 600);

            scene.setOnKeyPressed(e -> {
                if (expanded)
                    if (e.getCode() == KeyCode.RIGHT)
                        showNextVideo();
                    else
                        if (e.getCode() == KeyCode.LEFT)
                            showPreviousVideo();
                        else
                            if (e.getCode() == KeyCode.ESCAPE)
                                restoreGrid();
            });

            fxPanel.setScene(scene);
        });
    }

    private StackPane wrapMediaView(String path, int index) {
        MediaView view = createMediaView(path);
        StackPane wrapper = new StackPane(view);

        wrapper.setOnMouseClicked(e -> {
            if (!expanded)
                expandVideo(index);
            else {
                double clickX = e.getX();
                double width = wrapper.getWidth();
                if (clickX < width / 4)
                    showPreviousVideo();
                else
                    if (clickX > width * 3 / 4)
                        showNextVideo();
                    else
                        restoreGrid();
            }
        });

        return wrapper;
    }

    private void expandVideo(int index) {
        currentIndex = index;
        grid.getChildren().clear();

        StackPane wrapper = wrapMediaView(videoPaths.get(currentIndex), currentIndex);
        grid.add(wrapper, 0, 0);

        MediaView view = (MediaView) wrapper.getChildren().get(0);
        wrapper.setPrefSize(800, 600);
        view.setFitWidth(780);
        view.setFitHeight(580);

        expanded = true;
        expandedPane = wrapper;
    }

    private void restoreGrid() {
        grid.getChildren().clear();
        for (int i = 0; i < videoPaths.size(); i++)
            grid.add(wrapMediaView(videoPaths.get(i), i), i % 2, i / 2);
        expanded = false;
        expandedPane = null;
    }

    private void showNextVideo() {
        currentIndex = (currentIndex + 1) % videoPaths.size();
        expandVideo(currentIndex);
    }

    private void showPreviousVideo() {
        currentIndex = (currentIndex - 1 + videoPaths.size()) % videoPaths.size();
        expandVideo(currentIndex);
    }

    private MediaView createMediaView(String resourcePath) {
        String mediaUrl = getClass().getResource(resourcePath).toExternalForm();
        Media media = new Media(mediaUrl);
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