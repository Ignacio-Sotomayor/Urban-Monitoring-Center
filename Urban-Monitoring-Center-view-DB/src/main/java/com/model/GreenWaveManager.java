package com.model;

import com.model.Devices.TrafficLightController;
import com.model.Devices.TrafficLightState;

import java.util.List;

public class GreenWaveManager implements Runnable {

    private final List<TrafficLightController> trafficLights;
    private final long stepDelay = 500; // Medio segundo entre cada cambio de semáforo

    public GreenWaveManager(List<TrafficLightController> trafficLights) {
        this.trafficLights = trafficLights;
    }

    @Override
    public void run() {
        // Initial state is set by the cascade

        while (true) {
            try {
                // Cascade from Green to Yellow to Red
                cascadeChange(TrafficLightState.GREEN, TrafficLightState.RED, false);
                Thread.sleep(5000); // Green light duration
                
                cascadeChange(TrafficLightState.YELLOW, TrafficLightState.RED, false);
                Thread.sleep(2000); // Yellow light duration
                
                cascadeChange(TrafficLightState.RED, TrafficLightState.RED, true); // Both red
                Thread.sleep(1000); // All red duration

                // Cascade from Red to Green for the other direction (conceptually)
                // In this simplified model, we just wait for the red light time
                Thread.sleep(5000); // Red light duration (green for the other side)

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void cascadeChange(TrafficLightState mainState, TrafficLightState secondaryState, boolean isAllRed) throws InterruptedException {
        for (TrafficLightController tlc : trafficLights) {
            if (tlc.isIntermittentTime()) {
                tlc.setGreenWaveState(TrafficLightState.INTERMITTENT, TrafficLightState.INTERMITTENT);
            } else {
                if (isAllRed) {
                    tlc.setGreenWaveState(TrafficLightState.RED, TrafficLightState.RED);
                } else {
                    tlc.setGreenWaveState(mainState, secondaryState);
                }
            }
            Thread.sleep(stepDelay);
        }
    }
}
