package com.universalgravitylab.clientapp.service;

import com.universalgravitylab.clientapp.model.Body;

public class VelocityTerm {

    public static final double MULTIPLIER = 2.2E-18;

    public double getAx(Body body) {
        return body.getV0()[0] * 4d / 3d * MULTIPLIER;
    }

    public double getAy(Body body) {
        return body.getV0()[1] * 4d / 3d * MULTIPLIER;
    }

    public double getAz(Body body) {
        return body.getV0()[2] * 4d / 3d * MULTIPLIER;
    }
}
