package com.universalgravitylab.clientapp.service;

public class VelocityTerm {

    private static final double MULTIPLIER = 4.2E-9;

    public double getAx(double[] v0) {
        return v0[0] * MULTIPLIER;
    }

    public double getAy(double[] v0) {
        return v0[1] * MULTIPLIER;
    }

    public double getAz(double[] v0) {
        return v0[2] * MULTIPLIER;
    }
}
