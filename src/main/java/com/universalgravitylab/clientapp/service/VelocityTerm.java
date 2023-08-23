package com.universalgravitylab.clientapp.service;

import com.universalgravitylab.clientapp.model.Body;

public class VelocityTerm {

    private static final double MULTIPLIER = 4.2E-9;

    public double getAx(Body body) {
        return body.getV0()[0] * MULTIPLIER;
    }

    public double getAy(Body body) {
        return body.getV0()[1] * MULTIPLIER;
    }

    public double getAz(Body body) {
        return body.getV0()[2] * MULTIPLIER;
    }
}
