package com.view;

import com.model.Devices.*;
import com.model.UrbanMonitoringCenter;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

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

        TimerTask failureTask = new TimerTask() {
            @Override
            public void run() {
                if (devices == null || devices.isEmpty()) return;

                Random random = new Random();
                Device deviceToChange = devices.get(random.nextInt(devices.size()));

                if (umc.isFatalError(deviceToChange) || !deviceToChange.getState()) return;

                if (random.nextDouble() < 0.05) {
                    umc.setFatalError(deviceToChange);
                } else {
                    umc.issuedDevices(deviceToChange);
                }
            }
        };

        TimerTask uiUpdateTask = new TimerTask() {
            @Override
            public void run() {
                if (devices == null || devices.isEmpty()) return;

                // Check for repair requests
                Platform.runLater(() -> {
                    processRepairQueue("getAndClearRepairQueue", false);
                    processRepairQueue("getAndClearIntermittentRepairQueue", true);
                    processRepairQueue("getAndClearNormalModeQueue", false); // Re-uses the normal repair logic
                });

                // Update UI for all devices
                devices.forEach(d -> {
                    String status = umc.isFatalError(d) ? "ERROR FATAL" : (d.getState() ? "OPERATIVO" : "NO OPERATIVO");
                    String popupText = getPopupTextForDevice(d);
                    String icon = getIconForDevice(d);
                    String type = (d instanceof TrafficLightController) ? "TrafficLightController" : "Other";
                    
                    Platform.runLater(() -> {
                        String script = String.format(Locale.US, "updateMarkerState(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\")",
                            d.getId().toString(), status, popupText.replace("\"", "\\\""), icon, type);
                        webEngine.executeScript(script);
                    });
                });
            }
        };

        simulationTimer.schedule(failureTask, 10000, 10000);
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
            return "/Icons/FatalErrorTrafficLight.png";
        }

        String iconPath = "";
        boolean isOperative = device.getState();

        if (device instanceof TrafficLightController) {
            TrafficLightController tlc = (TrafficLightController) device;
            if (!isOperative) {
                iconPath = "/Icons/InoperativeTrafficLight.png";
            } else if (tlc.isIntermittentTime()) {
                iconPath = "/Icons/TrafficLightYellow.png";
            } else {
                iconPath = tlc.getIntersectionLights().get(0).getCurrentState().getIconPath();
            }
        } else if (device instanceof Radar) {
            iconPath = isOperative ? "/Icons/OperativeRadar.png" : "/Icons/InoperativeRadar.png";
        } else if (device instanceof ParkingLotSecurityCamera) {
            iconPath = isOperative ? "/Icons/OperativeParkingLotCamera.png" : "/Icons/InoperativeParkingLotCamera.png";
        } else if (device instanceof SecurityCamera) {
            iconPath = isOperative ? "/Icons/OperativeSecurityCamera.png" : "/Icons/InoperativeSecurityCamera.png";
        }

        URL resource = getClass().getResource(iconPath);
        return resource != null ? resource.toExternalForm() : "";
    }

    private String getPopupTextForDevice(Device device) {
        String type = "Dispositivo";
        if (device instanceof TrafficLightController) {
            type = "Controlador de Semáforos";
            TrafficLightController tlc = (TrafficLightController) device;
            if (tlc.isIntermittentTime()) {
                type += "<br><b>MODO INTERMITENTE</b>";
            } else {
                type += "<br>Principal: " + tlc.getIntersectionLights().get(0).getCurrentState();
                type += "<br>Secundario: " + tlc.getIntersectionLights().get(1).getCurrentState();
            }
        } else if (device instanceof Radar) {
            type = "Radar de Velocidad";
        } else if (device instanceof ParkingLotSecurityCamera) {
            type = "Cámara de Aparcamiento";
        } else if (device instanceof SecurityCamera) {
            type = "Cámara de Seguridad";
        }
        return "<b>" + device.getAddress() + "</b><br>" + type;
    }

    public void shutdown() {
        if (simulationTimer != null) {
            simulationTimer.cancel();
        }
    }
}