package com.view;

import javax.swing.*;
import java.awt.*;

import com.view.MotorVehicleRegistry.InsertWindow;
import com.view.MotorVehicleRegistry.ViewWindow;
import javafx.embed.swing.JFXPanel;
import com.view.Reports.*;

public class MenuWindow extends JFrame {

    private MapWindow mapWindow;
    private FailuresWindow failuresWindow;
    private InsertWindow insertWindow;

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
        JButton motorVehicleButton = new JButton("Motor Vehicle Registry");
        JButton addButton = new JButton("Ingresar al sistema");
        
        addButton.addActionListener(e->{ 
            if (insertWindow == null)
                insertWindow = new InsertWindow();
            insertWindow.setVisible(true);
            insertWindow.toFront();
        });

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

        motorVehicleButton.addActionListener(e -> openMVRMenu());;

        reportsButton.addActionListener(e -> openReportsMenu());

        add(mapButton);
        add(camerasButton);
        add(reportsButton);
        add(failPanelButton);
        add(motorVehicleButton);
    }

    private void openReportsMenu() {
        JDialog reportsDialog = new JDialog(this, "Reports Menu", true);
        reportsDialog.setSize(350, 350);
        reportsDialog.setLocationRelativeTo(this);
        reportsDialog.setLayout(new GridLayout(6, 1, 10, 10)); // 5 botones + 1 de Close

        JButton devicesStateButton = new JButton("Devices State");
        JButton finesListButton = new JButton("Fines List");
        JButton carFinesButton = new JButton("Particular Automobile Fines List");
        JButton securityNoticesButton = new JButton("Security Notices");
        JButton deviceEventsButton = new JButton("Device Events");
        JButton closeButton = new JButton("Close");

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

        closeButton.addActionListener(e -> reportsDialog.dispose());

        reportsDialog.add(devicesStateButton);
        reportsDialog.add(finesListButton);
        reportsDialog.add(carFinesButton);
        reportsDialog.add(securityNoticesButton);
        reportsDialog.add(deviceEventsButton);
        reportsDialog.add(closeButton);

        reportsDialog.setVisible(true);
    }

    private void openMVRMenu() {
        JDialog MVRDialog = new JDialog(this, "Motor Vehicle Registry", true);
        MVRDialog.setSize(350, 350);
        MVRDialog.setLocationRelativeTo(this);
        MVRDialog.setLayout(new GridLayout(3, 1, 10, 10));

        JButton insert = new JButton("Insert");
        JButton view = new JButton("View");
        JButton closeButton = new JButton("Close");

        insert.addActionListener(e -> {
            MVRDialog.dispose();
            new InsertWindow().setVisible(true);
        });

        view.addActionListener(e -> {
            MVRDialog.dispose();
            new ViewWindow().setVisible(true);
        });




        closeButton.addActionListener(e -> MVRDialog.dispose());

        MVRDialog.add(insert);
        /* Due a rollback caused by a computer brock down
        * the Update delete and view of the owners, models, brands and automobiles the view section
        * isn't ready to be integrated to the project*/
        //MVRDialog.add(view);
        MVRDialog.add(closeButton);

        MVRDialog.setVisible(true);
    }
}