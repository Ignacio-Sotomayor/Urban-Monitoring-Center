package com.view;

import javax.swing.*;
import java.awt.*;
import com.view.FailuresWindow;

import com.controller.UrbanMonitoringCenter;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import com.view.Reports.*;

public class MenuWindow extends JFrame {

    private MapWindow mapWindow;
    private FailuresWindow failuresWindow;

    public MenuWindow() {
        super("Urban Monitoring Center");

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new JFXPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        JButton mapButton = new JButton("Map");
        JButton camerasButton = new JButton("Cameras");
        JButton reportsButton = new JButton("Reports");
        JButton failPanelButton = new JButton("Fail Panel");

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

        failPanelButton.addActionListener(e -> {
            if (failuresWindow == null || !failuresWindow.isDisplayable()) {
                failuresWindow = new FailuresWindow();
            }
            failuresWindow.setVisible(true);
            failuresWindow.toFront();
        });

        // Botón REPORTS → abre submenú con opciones
        reportsButton.addActionListener(e -> openReportsMenu());

        add(mapButton);
        add(camerasButton);
        add(reportsButton);
        add(failPanelButton);
    }

    private void openReportsMenu() {
        // Creamos el diálogo modal
        JDialog reportsDialog = new JDialog(this, "Reports Menu", true);
        reportsDialog.setSize(350, 350);
        reportsDialog.setLocationRelativeTo(this);
        reportsDialog.setLayout(new GridLayout(6, 1, 10, 10)); // 5 botones + 1 de Close

        // Botones de reportes
        JButton devicesStateButton = new JButton("Devices State");
        JButton finesListButton = new JButton("Fines List");
        JButton carFinesButton = new JButton("Particular Automobile Fines List");
        JButton securityNoticesButton = new JButton("Security Notices");
        JButton deviceEventsButton = new JButton("Device Events");
        JButton closeButton = new JButton("Close");

        // Acción: abrir cada reporte y cerrar el diálogo
        devicesStateButton.addActionListener(e -> {
            reportsDialog.dispose();
            new DevicesStateWindow().setVisible(true);
        });

        finesListButton.addActionListener(e -> {
            reportsDialog.dispose();
            new FinesWindow().setVisible(true);
        });

        carFinesButton.addActionListener(e -> {
            reportsDialog.dispose();
            new AutomobileFinesWindow().setVisible(true);
        });

        securityNoticesButton.addActionListener(e -> {
            reportsDialog.dispose();
            new SecurityNoticeWindow().setVisible(true);
        });

        deviceEventsButton.addActionListener(e -> {
            reportsDialog.dispose();
            new DeviceEventsWindow().setVisible(true);
        });

        // Botón para cerrar el diálogo sin abrir nada
        closeButton.addActionListener(e -> reportsDialog.dispose());

        // Añadir botones al diálogo
        reportsDialog.add(devicesStateButton);
        reportsDialog.add(finesListButton);
        reportsDialog.add(carFinesButton);
        reportsDialog.add(securityNoticesButton);
        reportsDialog.add(deviceEventsButton);
        reportsDialog.add(closeButton);

        reportsDialog.setVisible(true);
    }
}