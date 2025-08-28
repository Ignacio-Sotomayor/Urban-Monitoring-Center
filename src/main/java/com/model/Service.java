package com.model;

public enum Service {
    Police(911),
    FireFighters(100),
    Ambulance(107),
    CivilDefense(105);

    Service(int telephonicNumber) {
    }
}