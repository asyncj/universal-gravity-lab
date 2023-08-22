package com.universalgravitylab.clientapp.service;

import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.universalgravitylab.clientapp.controller.SimulationController.AU;

@Component
public class SimulationService implements InitializingBean {
    public static final int NUM_STEPS = 3_000;     // number of iterations

    public static final String SUN_EARTH = "Sun/Earth";
    public static final String DOUBLE_SUN_EARTH = "Double Sun/Earth";
    public static final String DOUBLE_SUN_EARTH_VENUS = "Double Sun/Earth/Venus";
    public static final String DOUBLE_SUN_EARTH_MARS = "Double Sun/Earth/Mars";
    public static final String EARTH_MOON = "Earth/Moon";
    public static final String EARTH_MOON_SPIRAL = "Earth/Moon/Spiral";

    public static double M_SUN = 1.9891e30;   // mass of the Sun
    public static double M_EARTH = 5.9891e24;   // mass of Earth
    public static double M_MOON = 7.342e22;   // mass of Moon
    public static double M_VENUS = 4.867e24;   // mass of Venus
    public static double M_MARS = 6.39e23;   // mass of Mars
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
        Simulation simulation = new Simulation(SUN_EARTH, NUM_STEPS, ITERATIONS_PER_STEP, AU / 100.0, 24 * 60 * 60d / ITERATIONS_PER_STEP);
        List<Body> bodyList = simulation.getBodyList();
        double sunRadius = 6.957E8;
        String sunColor = "#FDB813";
        String earthColor = "#006994";
        String moonColor = "#F7EAC6";
        String venusColor = "#8B7D82";
        String marsColor = "#AD6242";

        bodyList.add(new Body("Sun", M_SUN, sunRadius, NUM_STEPS, new double[]{0, 0, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, false));
        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, r0, v0, a0, Color.valueOf(earthColor), false, false));
        simulationList.add(simulation);

        simulation = new Simulation(DOUBLE_SUN_EARTH, NUM_STEPS, ITERATIONS_PER_STEP, AU / 100.0, 24 * 60 * 60d / ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        bodyList.add(new Body("Sun #1", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, -sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, true));
        bodyList.add(new Body("Sun #2", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, true));

        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, new double[]{1.496e11, 0, 0}, new double[]{0, 2.9783e4 * 1.0, 0}, new double[]{0, 0, 0}, Color.valueOf(earthColor), false, false));
        simulationList.add(simulation);

        simulation = new Simulation(DOUBLE_SUN_EARTH_VENUS, NUM_STEPS, ITERATIONS_PER_STEP, AU / 100.0, 24 * 60 * 60d / ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        bodyList.add(new Body("Sun #1", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, -sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, true));
        bodyList.add(new Body("Sun #2", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, sunRadius * 50, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, true));

        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, new double[]{1.496e11, 0, 0}, new double[]{0, 2.9783e4 * 0.99, 0}, new double[]{0, 0, 0}, Color.valueOf(earthColor), false, false));
        bodyList.add(new Body("Venus", M_VENUS, 6051800, NUM_STEPS, new double[]{-1.082e11, 0, 0}, new double[]{0, -3.5023e4 * 0.98, 0}, new double[]{0, 0, 0}, Color.valueOf(venusColor), false, false));
        simulationList.add(simulation);

        simulation = new Simulation(DOUBLE_SUN_EARTH_MARS, NUM_STEPS, ITERATIONS_PER_STEP, AU / 100.0, 24 * 60 * 60d / ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        bodyList.add(new Body("Sun #1", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, -sunRadius * 50, 0}, new double[]{2.9E4 / 2.5, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, false));
        bodyList.add(new Body("Sun #2", M_SUN / 2.0, sunRadius, NUM_STEPS, new double[]{0, sunRadius * 50, 0}, new double[]{-2.9E4 / 2.5, 0, 0}, new double[]{0, 0, 0}, Color.valueOf(sunColor), true, false));

        bodyList.add(new Body("Venus", M_VENUS, 6051800, NUM_STEPS, new double[]{-1.082e11, 0, 0}, new double[]{0, -3.5023e4 * 0.98, 0}, new double[]{0, 0, 0}, Color.valueOf(venusColor), false, false));
        bodyList.add(new Body("Earth", M_EARTH, 6371000, NUM_STEPS, new double[]{1.496e11, 0, 0}, new double[]{0, 2.9783e4 * 0.99, 0}, new double[]{0, 0, 0}, Color.valueOf(earthColor), false, false));
        bodyList.add(new Body("Mars", M_MARS, 6051800, NUM_STEPS, new double[]{-2.272e11, 0, 0}, new double[]{0, -2.4023e4 * 0.98, 0}, new double[]{0, 0, 0}, Color.valueOf(marsColor), false, false));
        simulationList.add(simulation);

        double r = 362_600_000;
        int numSteps = NUM_STEPS * 5;
        simulation = new Simulation(EARTH_MOON, numSteps, ITERATIONS_PER_STEP, r / 150.0, 24 * 60 * 1d / ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        double totalMass = M_EARTH + M_MOON;
        double rCenter = (M_EARTH * 0 + M_MOON * r) / totalMass;
        r = r - rCenter;
        bodyList.add(new Body("Earth", M_EARTH, 6371000, numSteps, new double[]{-rCenter, 0, 0}, new double[]{0, 13, 0}, new double[]{0, 0, 0}, Color.valueOf(earthColor), false, false));
        bodyList.add(new Body("Moon", M_MOON, 1737400, numSteps, new double[]{r, 0, 0}, new double[]{0, -1080, 0}, new double[]{0, 0, 0}, Color.valueOf(moonColor), false, false));
        simulationList.add(simulation);

        r = 362_600_000;
        numSteps = NUM_STEPS * 10;
        simulation = new Simulation(EARTH_MOON_SPIRAL, numSteps, ITERATIONS_PER_STEP, r / 75.0, 24 * 60 * 5d / ITERATIONS_PER_STEP);
        bodyList = simulation.getBodyList();
        simulation.setTerm(new VelocityTerm());
        totalMass = M_EARTH + M_MOON;
        rCenter = (M_EARTH * 0 + M_MOON * r) / totalMass;
        r = r - rCenter;
        bodyList.add(new Body("Earth", M_EARTH, 6371000, numSteps, new double[]{-rCenter, 0, 0}, new double[]{0, 13, 0}, new double[]{0, 0, 0}, Color.valueOf(earthColor), false, false));
        bodyList.add(new Body("Moon", M_MOON, 1737400, numSteps, new double[]{r, 0, 0}, new double[]{0, -1080, 0}, new double[]{0, 0, 0}, Color.valueOf(moonColor), false, false));
        simulationList.add(simulation);
    }
}
