package com.view;

import com.model.Devices.*;
import com.controller.UrbanMonitoringCenter;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MapController {

    @FXML
    private WebView webView;

    private WebEngine webEngine;
    private List<Device> devices;
    private Timer simulationTimer;

    public MapController(WebView webView) {
        this.webView = webView;
    }

    public void setDeviceData(List<Device> devices) {
        this.devices = new ArrayList<>(devices);
    }

    public void initialize() {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                prepareAndSendInitialData();
                startSimulation();
            }
        });

        URL url = getClass().getResource("map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("No se pudo encontrar el fichero map.html");
        }
    }

    private void prepareAndSendInitialData() {
        new Thread(() -> {
            if (devices == null || devices.isEmpty()) return;

            String allDevicesJson = devices.stream()
                    .map(d -> {
                        String status = d.getState() ? "OPERATIVO" : "NO OPERATIVO";
                        String icon = getIconForDevice(d);
                        String type = (d instanceof TrafficLightController) ? "TrafficLightController" : "Other";
                        return String.format(Locale.US, "{\"id\":\"%s\", \"lat\":%f, \"lon\":%f, \"popup\":\"%s\", \"status\":\"%s\", \"icon\":\"%s\", \"type\":\"%s\"}",
                                d.getId().toString(), d.getLocation().getLatitude(), d.getLocation().getLongitude(), getPopupTextForDevice(d).replace("\"", "\\\""), status, icon, type);
                    })
                    .collect(Collectors.joining(",", "[", "]"));

            Platform.runLater(() -> {
                webEngine.executeScript(String.format("setMarkers(%s)", allDevicesJson));
            });
        }).start();
    }

    private void startSimulation() {
        simulationTimer = new Timer(true);
        UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();

        TimerTask uiUpdateTask = new TimerTask() {
            @Override
            public void run() {
                if (devices == null || devices.isEmpty()) return;

                Platform.runLater(() -> {
                    processRepairQueue("getAndClearRepairQueue", false);
                    processRepairQueue("getAndClearIntermittentRepairQueue", true);
                    processRepairQueue("getAndClearNormalModeQueue", false);
                });

                devices.forEach(d -> {
                    String status = umc.isFatalError(d) ? "ERROR FATAL" : (d.getState() ? "OPERATIVO" : "NO OPERATIVO");
                    String popupText = getPopupTextForDevice(d);
                    String icon = d.getIconPath();


                    Platform.runLater(() -> {
                        String script = String.format(Locale.US, "updateMarkerState(\"%s\", \"%s\", \"%s\", \"%s\")",
                                d.getId().toString(), status, popupText.replace("\"", "\\\""), icon);
                        webEngine.executeScript(script);
                    });
                });
            }
        };

        simulationTimer.schedule(uiUpdateTask, 250, 250);
    }

    private void processRepairQueue(String getQueueFunction, boolean isIntermittent) {
        UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        Object result = webEngine.executeScript(getQueueFunction + "()");
        if (result instanceof String && !((String) result).equals("[]")) {
            String json = (String) result;
            String[] ids = json.replace("[", "").replace("]", "").replace("\"", "").split(",");
            for (String idStr : ids) {
                try {
                    UUID id = UUID.fromString(idStr.trim());
                    Device device = umc.getSpecificDevice(id);
                    if (device != null) {
                        if (isIntermittent) {
                            umc.repairToIntermittent(device);
                        } else {
                            umc.repairDevices(device);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing UUID from JS: " + idStr);
                }
            }
        }
    }

    private String getIconForDevice(Device device) {
        UrbanMonitoringCenter umc = UrbanMonitoringCenter.getUrbanMonitoringCenter();
        if (umc.isFatalError(device)) {
            URL resource = getClass().getResource("/Icons/FatalErrorTrafficLight.png");
            return resource != null ? resource.toExternalForm() : device.getIconPath(); // Fallback to device's default icon
        }
        // If not a fatal error, check if the device is simply not operative
        if (!device.getState()) {
            // Assuming a generic icon for non-operative devices exists
            URL resource = getClass().getResource("/Icons/NonOperativeDevice.png");
            return resource != null ? resource.toExternalForm() : device.getIconPath(); // Fallback to device's default icon
        }
        // Otherwise, get the default icon from the device itself
        return device.getIconPath();
    }

    private String getPopupTextForDevice(Device device) {
        return "<b>" + device.getAddress() + "</b><br>" +
               device.getDeviceTypeName() +
               device.getDeviceSpecificInfo();
    }

    public void shutdown() {
        if (simulationTimer != null) {
            simulationTimer.cancel();
        }
    }
}