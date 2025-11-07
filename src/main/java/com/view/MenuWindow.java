package com.view;

import javax.swing.*;
import java.awt.*;
import com.view.Reports.*;

public class MenuWindow extends JFrame {

    private MapWindow mapWindow;

    public MenuWindow() {
        super("Urban Monitoring Center");

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        // Botones principales
        JButton mapButton = new JButton("Map");
        JButton camerasButton = new JButton("Cameras");
        JButton reportsButton = new JButton("Reports");

        // Botón MAP
        mapButton.addActionListener(e -> {
            if (mapWindow == null)
                mapWindow = new MapWindow();
            mapWindow.setVisible(true);
            mapWindow.toFront();
        });

        // Botón CAMERAS
        camerasButton.addActionListener(e -> {
            CamerasWindow camerasWindow = new CamerasWindow();
            camerasWindow.setVisible(true);
        });

        // Botón REPORTS → abre submenú con opciones
        reportsButton.addActionListener(e -> openReportsMenu());

        // Agregar los tres botones
        add(mapButton);
        add(camerasButton);
        add(reportsButton);
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