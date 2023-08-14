package com.universalgravitylab.clientapp.service;

import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimulationService implements InitializingBean {
    public static final int NUM_STEPS = 3_000;     // number of iterations

    public static final String SUN_EARTH = "Sun/Earth";
    public static final String DOUBLE_SUN_EARTH = "Double Sun/Earth";

    public static double M_SUN = 1.9891e30;   // mass of the Sun
    public static double M_EARTH = 5.9891e24;   // mass of the Sun
    public static final int ITERATIONS_PER_STEP = 10000;

    // Define initial conditions for planet
    double[] r0 = {1.496e11, 0, 0};        // initial position (m)
    double[] v0 = {0, 2.9783e4, 0};        // initial velocity (m/s)
    double[] a0 = {0, 0, 0};               // initial acceleration (m/s^2)

    private List<Simulation> simulationList = new ArrayList<>();

    public List<Simulation> getSimulationList() {
        return simulationList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Simulation simulation = new Simulation(SUN_EARTH, NUM_STEPS, ITERATIONS_PER_STEP);
        List<Body> bodyList = simulation.getBodyList();
        double sunRadius = 6.957E8;
        bodyList.add(new Body("Sun", M_SUN, sunRadius, NUM_STEPS, new double[]{0, 0, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.YELLOW, true, false));
        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, r0, v0, a0, Color.BLUE, false, false));
        simulationList.add(simulation);

        simulation = new Simulation(DOUBLE_SUN_EARTH, NUM_STEPS, ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        bodyList.add(new Body("Sun #1", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, -sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.YELLOW, true, true));
        bodyList.add(new Body("Sun #2", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.YELLOW, true, true));

        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, new double[]{1.496e11, 0, 0}, new double[]{0, 2.9783e4 * 0.8, 0}, new double[]{0, 0, 0}, Color.BLUE, false, false));
        simulationList.add(simulation);



    }
}
