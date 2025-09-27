package com.view;

import javax.swing.*;
import java.awt.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class MainWindow extends JFrame {

    private MapWindow mapWindow;

    public MainWindow() {
        super("Urban Monitoring Center");

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new JFXPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        JButton mapButton = new JButton("Map");
        JButton camerasButton = new JButton("Cameras");
        JButton finesButton = new JButton("Fines");
        JButton securityButton = new JButton("Security Notices");

        mapButton.addActionListener(e -> {
            if (mapWindow == null) {
                mapWindow = new MapWindow();
            }
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

        add(mapButton);
        add(camerasButton);
        add(finesButton);
        add(securityButton);
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