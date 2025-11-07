package com.view;

import com.model.Devices.SecurityCamera;
import com.model.Service;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SecurityNoticeDialog extends JDialog {
    private final SecurityCamera camera;
    private final JTextArea descriptionArea;
    private final JCheckBox policeCheckBox;
    private final JCheckBox fireFightersCheckBox;
    private final JCheckBox ambulanceCheckBox;
    private final JCheckBox civilDefenseCheckBox;

    public SecurityNoticeDialog(Frame owner, SecurityCamera camera) {
        super(owner, "Report Security Incident", true);
        this.camera = camera;

        setLayout(new BorderLayout());

        // Description Panel
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionArea = new JTextArea(5, 30);
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        // Services Panel
        JPanel servicesPanel = new JPanel();
        servicesPanel.setBorder(BorderFactory.createTitledBorder("Select Services"));
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        policeCheckBox = new JCheckBox(Service.Police.getName());
        fireFightersCheckBox = new JCheckBox(Service.FireFighters.getName());
        ambulanceCheckBox = new JCheckBox(Service.Ambulance.getName());
        civilDefenseCheckBox = new JCheckBox(Service.CivilDefense.getName());
        servicesPanel.add(policeCheckBox);
        servicesPanel.add(fireFightersCheckBox);
        servicesPanel.add(ambulanceCheckBox);
        servicesPanel.add(civilDefenseCheckBox);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(submitButton);

        // Add panels to dialog
        add(descriptionPanel, BorderLayout.CENTER);
        add(servicesPanel, BorderLayout.WEST);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Action Listeners
        submitButton.addActionListener(e -> submitReport());
        cancelButton.addActionListener(e -> setVisible(false));

        pack();
        setLocationRelativeTo(owner);
    }

    private void submitReport() {
        String description = descriptionArea.getText();
        if (description.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<Service> selectedServices = new HashSet<>();
        if (policeCheckBox.isSelected()) {
            selectedServices.add(Service.Police);
        }
        if (fireFightersCheckBox.isSelected()) {
            selectedServices.add(Service.FireFighters);
        }
        if (ambulanceCheckBox.isSelected()) {
            selectedServices.add(Service.Ambulance);
        }
        if (civilDefenseCheckBox.isSelected()) {
            selectedServices.add(Service.CivilDefense);
        }

        if (selectedServices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one service must be selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        camera.issueSecurityNotice(description, selectedServices);
        JOptionPane.showMessageDialog(this, "Security notice created for device: " + camera.getAddress());
        setVisible(false);
    }
}
