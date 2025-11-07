package com.view;

import com.controller.UrbanMonitoringCenter;
import com.model.Devices.Device;
import com.model.Devices.ParkingLotSecurityCamera;
import com.model.Devices.SecurityCamera;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CamerasWindow extends JFrame {
    private GridPane grid;
    private boolean expanded = false;
    private StackPane expandedPane = null;
    private int currentIndex = 0;

    private List<Device> cameras;

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

        UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        cameras = umc.getDevices().values().stream()
                .filter(d -> d instanceof SecurityCamera || d instanceof ParkingLotSecurityCamera)
                .collect(Collectors.toList());

        Platform.runLater(() -> {
            grid = new GridPane();
            grid.setHgap(5);
            grid.setVgap(5);

            for (int i = 0; i < cameras.size(); i++)
                grid.add(wrapMediaView(cameras.get(i), i), i % 2, i / 2);

            Scene scene = new Scene(grid, 800, 600);

            scene.setOnKeyPressed(e -> {
                if (expanded)
                    if (e.getCode() == KeyCode.RIGHT)
                        showNextVideo();
                    else if (e.getCode() == KeyCode.LEFT)
                        showPreviousVideo();
                    else if (e.getCode() == KeyCode.ESCAPE)
                        restoreGrid();
            });

            fxPanel.setScene(scene);
        });
    }

    private VBox wrapMediaView(Device camera, int index) {
        MediaView view = createMediaView("/videos/TestVideo" + (index + 1) + ".mp4");
        Button reportButton = new Button("Report Incident");
        VBox wrapper = new VBox(view, reportButton);

        reportButton.setOnAction(e -> {
            if (camera.getDeviceTypeName() == "SecurityCamera") {
                SecurityNoticeDialog dialog = new SecurityNoticeDialog(this, (SecurityCamera) camera);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "This camera type does not support incident reporting.");
            }
        });

        wrapper.setOnMouseClicked(e -> {
            if (!expanded)
                expandVideo(index);
            else {
                double clickX = e.getX();
                double width = wrapper.getWidth();
                if (clickX < width / 4)
                    showPreviousVideo();
                else if (clickX > width * 3 / 4)
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

        VBox wrapper = wrapMediaView(cameras.get(currentIndex), currentIndex);
        grid.add(wrapper, 0, 0);

        MediaView view = (MediaView) wrapper.getChildren().get(0);
        wrapper.setPrefSize(800, 600);
        view.setFitWidth(780);
        view.setFitHeight(580);

        expanded = true;
    }

    private void restoreGrid() {
        grid.getChildren().clear();
        for (int i = 0; i < cameras.size(); i++)
            grid.add(wrapMediaView(cameras.get(i), i), i % 2, i / 2);
        expanded = false;
    }

    private void showNextVideo() {
        currentIndex = (currentIndex + 1) % cameras.size();
        expandVideo(currentIndex);
    }

    private void showPreviousVideo() {
        currentIndex = (currentIndex - 1 + cameras.size()) % cameras.size();
        expandVideo(currentIndex);
    }

    private MediaView createMediaView(String resourcePath) {
        try {
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl == null) {
                System.err.println("Resource not found: " + resourcePath);
                return new MediaView();
            }
            String mediaUrl = resourceUrl.toExternalForm();
            Media media = new Media(mediaUrl);

            media.setOnError(() -> {
                System.err.println("Media error for " + resourcePath + ": " + media.getError());
            });

            MediaPlayer player = new MediaPlayer(media);

            player.setOnError(() -> {
                System.err.println("MediaPlayer error for " + resourcePath + ": " + player.getError());
            });

            player.setOnReady(() -> {
                player.setMute(true);
                player.play();
            });

            MediaView view = new MediaView(player);
            view.setFitWidth(380);
            view.setFitHeight(280);
            view.setPreserveRatio(true);

            return view;
        } catch (Exception e) {
            System.err.println("Error loading video: " + resourcePath);
            e.printStackTrace();
            return new MediaView();
        }
    }
}