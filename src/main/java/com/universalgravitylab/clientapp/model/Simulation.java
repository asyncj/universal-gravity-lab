package com.universalgravitylab.clientapp.model;

import com.universalgravitylab.clientapp.service.VelocityTerm;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private String name;

    public static final double G = 6.6743e-11;

    private int numSteps;
    private int iterationsPerStep;

    private double dt;
    private double scale;

    private List<Body> bodyList = new ArrayList<>();
    private VelocityTerm term;

    public Simulation(String name, int numSteps, int iterationsPerStep, double scale, double dt) {
        this.name = name;
        this.numSteps = numSteps;
        this.iterationsPerStep = iterationsPerStep;
        this.dt = dt;      // time step (s)
        this.scale = scale;
    }

    public void runSimulation() {
        // Perform Verlet integration for remaining time steps

        for (int i = 0; i < numSteps ; i++) {
            for (int j = 0; j < iterationsPerStep; j++)
            {
                for (int m = 0; m < bodyList.size(); m++) {

                    Body bodyTo = bodyList.get(m);
                    if (bodyTo.isStationary()) {
                        continue;
                    }
                    double[] r0 = bodyTo.getR0();
                    double[] v0 = bodyTo.getV0();
                    double[] a0 = bodyTo.getA0();

                    double[] r1 = bodyTo.getR1();
                    double[] v1 = bodyTo.getV1();
                    double[] a1 = bodyTo.getA1();

                    a1[0] = 0;
                    a1[1] = 0;
                    a1[2] = 0;
                    for (int k = 0; k < bodyList.size(); k++) {
                        if (k == m) continue;
                        Body bodyFrom = bodyList.get(k);
                        double dx = r0[0] - bodyFrom.getR0()[0];
                        double dy = r0[1] - bodyFrom.getR0()[1];
                        double dz = r0[2] - bodyFrom.getR0()[2];

                        double rMag = Math.sqrt(dx * dx + dy * dy + dz * dz);

                        a1[0] += -G * bodyFrom.getMass() * dx / (rMag * rMag * rMag);
                        a1[1] += -G * bodyFrom.getMass() * dy / (rMag * rMag * rMag);
                        a1[2] += -G * bodyFrom.getMass() * dz / (rMag * rMag * rMag);

                        if (term != null) {
                            a1[0] += term.getAx(v0);
                            a1[1] += term.getAy(v0);
                            a1[2] += term.getAz(v0);
                        }
                    }

                    r1[0] = r0[0] + v0[0] * dt + 0.5 * a0[0] * dt * dt;
                    r1[1] = r0[1] + v0[1] * dt + 0.5 * a0[1] * dt * dt;
                    r1[2] = r0[2] + v0[2] * dt + 0.5 * a0[2] * dt * dt;

                    v1[0] = v0[0] + 0.5 * (a0[0] + a1[0]) * dt;
                    v1[1] = v0[1] + 0.5 * (a0[1] + a1[1]) * dt;
                    v1[2] = v0[2] + 0.5 * (a0[2] + a1[2]) * dt;

                    double[] tmp = v1;
                    bodyTo.setV1(v0);
                    bodyTo.setV0(tmp);

                    tmp = r1;
                    bodyTo.setR1(r0);
                    bodyTo.setR0(tmp);

                    tmp = a1;
                    bodyTo.setA1(a0);
                    bodyTo.setA0(tmp);
                }

            }

            for (int k = 0; k < bodyList.size(); k++) {
                Body body = bodyList.get(k);
                body.updateIndex(i);
            }

        }
    }

    public List<Body> getBodyList() {
        return bodyList;
    }

    public String getName() {
        return name;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public double getScale() {
        return scale;
    }

    public void setTerm(VelocityTerm term) {
        this.term = term;
    }

    public VelocityTerm getTerm() {
        return term;
    }
}
