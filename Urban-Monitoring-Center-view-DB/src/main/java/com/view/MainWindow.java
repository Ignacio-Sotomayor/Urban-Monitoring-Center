package com.view;

import javax.swing.*;
import java.awt.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class MainWindow extends JFrame {

    private MapWindow mapWindow;
    private FailuresWindow failuresWindow; // Add reference to the failures window

    public MainWindow() {
        super("Urban Monitoring Center");

        setSize(500, 200); // Increased size to fit the new button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new JFXPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        JButton mapButton = new JButton("Map");
        JButton camerasButton = new JButton("Cameras");
        JButton finesButton = new JButton("Fines");
        JButton securityButton = new JButton("Security Notices");
        JButton failPanelButton = new JButton("Fail Panel"); // New button

        mapButton.addActionListener(e -> {
            if (mapWindow == null)
                mapWindow = new MapWindow();
            mapWindow.setVisible(true);
            mapWindow.toFront();
        });

        camerasButton.addActionListener(e -> {
            CamerasWindow camerasWindow = new CamerasWindow();
            camerasWindow.setVisible(true);
        });

        finesButton.addActionListener(e -> {
            FinesWindow finesWindow = new FinesWindow();
            finesWindow.setVisible(true);
        });

        securityButton.addActionListener(e -> {
            SecurityNoticeWindow securityNoticeWindow = new SecurityNoticeWindow();
            securityNoticeWindow.setVisible(true);
        });

        failPanelButton.addActionListener(e -> {
            if (failuresWindow == null || !failuresWindow.isDisplayable()) {
                failuresWindow = new FailuresWindow();
            }
            failuresWindow.setVisible(true);
            failuresWindow.toFront();
        });

        add(mapButton);
        add(camerasButton);
        add(finesButton);
        add(securityButton);
        add(failPanelButton); // Add the new button to the layout
    }

    public static void main(String[] args) {
        com.model.UrbanMonitoringCenter.Initialize();

        new JFXPanel();
        Platform.setImplicitExit(false);

        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

}