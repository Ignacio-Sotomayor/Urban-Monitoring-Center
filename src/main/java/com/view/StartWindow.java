package com.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame {
    JButton lastSaveLoadButton;
    JButton baseBrandStartButton;
    private MenuWindow menuWindow;

    public StartWindow(){
        super("Urban Monitoring Center");

        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        new JFXPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));

        lastSaveLoadButton = new JButton("Start from last State");
        baseBrandStartButton = new JButton("Restart system");

        if(menuWindow == null)
            menuWindow = new MenuWindow();

        lastSaveLoadButton.addActionListener(e->{
            com.controller.UrbanMonitoringCenter.lastStateStart();
            menuWindow.setVisible(true);
            menuWindow.toFront();
        });
        baseBrandStartButton.addActionListener(e->{
            com.controller.UrbanMonitoringCenter.baseBrandStart();
            menuWindow.setVisible(true);
            menuWindow.toFront();
        });

        add(lastSaveLoadButton);
        add(baseBrandStartButton);
    }
    public static void main(String[] args){
        new JFXPanel();
        Platform.setImplicitExit(false);

        SwingUtilities.invokeLater(()->{
            new StartWindow().setVisible(true);
        });
    }
}