package com.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import com.model.UrbanMonitoringCenter;

public class MapWindow extends JFrame {

    private MapController controller;

    public MapWindow() {
        super("Map Viewer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            controller = new MapController(webView);

            // Pass a fresh copy of the devices list
            controller.setDeviceData(
                    new ArrayList<>(UrbanMonitoringCenter.getUrbanMonitoringCenter().getDevices().values())
            );

            controller.initialize();

            fxPanel.setScene(new Scene(webView, 800, 600));
        });
    }


    @Override
    public void dispose() {
        if (controller != null) {
            controller.shutdown();
        }
        super.dispose();
    }
}