package com.universalgravitylab.clientapp;

import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainController {

    @Autowired
    private ApplicationContext applicationContext;

    public static final double AU = 149_597_870_700d;
    public static final int NUM_STEPS = 1_000;     // number of iterations
    public static final int ITERATIONS_PER_STEP = 10000;

    public static double M_SUN = 1.9891e30;   // mass of the Sun
    public static double M_EARTH = 5.9891e24;   // mass of the Sun

    @FXML
    private TreeView treeView;

    @FXML
    private Tab newSimulationTab;

    private List<Simulation> simulationList = new ArrayList<>();
    private Simulation simulation;
    private AnimationTimer timer;

    @FXML
    private Slider progressBar;

    @FXML
    private Canvas canvas;

    // Define initial conditions for planet
    double[] r0 = {1.496e11, 0, 0};        // initial position (m)
    double[] v0 = {0, 2.9783e4, 0};        // initial velocity (m/s)
    double[] a0 = {0, 0, 0};               // initial acceleration (m/s^2)
    private boolean play = true;
    private GraphicsContext gc;

    private int pos;


    @FXML
    private void initialize() {
        treeView.setRoot(new TreeItem("Sun/Earth"));
        progressBar.setMin(1);
        progressBar.setMax(NUM_STEPS);
        progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            pos = (int) progressBar.getValue() - 1;
            updateAnimation();
        });


        simulation = new Simulation(NUM_STEPS, ITERATIONS_PER_STEP);
        List<Body> bodyList = simulation.getBodyList();
        bodyList.add(new Body(M_SUN, NUM_STEPS, new double[]{0, 0, 0}, new double[]{0, 0, 0}, new double[]{0, 0, 0}, Color.YELLOW));
        bodyList.add(new Body(M_EARTH, NUM_STEPS, r0, v0, a0, Color.BLUE));

        gc = canvas.getGraphicsContext2D();

        simulation.runSimulation();

        timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            @Override
            public void handle(long now) {
                double elapsedTime = (now - lastTime) / 5_000_000.0;
                lastTime = now;

                updateAnimation();

                pos++;
                onUpdateSimulation(pos);
                if (pos >= NUM_STEPS) {
                    pos = 0;
                }

            }
        };
        timer.start();
    }

    private void updateAnimation() {
        gc.save();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < simulation.getBodyList().size(); i++) {
            Body body = simulation.getBodyList().get(i);

            gc.setFill(body.getColor());
            gc.fillOval(250 + body.getR()[pos][0] / AU * 100, 200 + body.getR()[pos][1] / AU * 100, 15, 15);
        }
        gc.restore();
    }

    private void onUpdateSimulation(int i) {
        progressBar.adjustValue(i + 1);
    }

    @FXML
    public void newSimulation(ActionEvent event) {
        event.consume();
        System.out.println("newSimulation");
    }

    @FXML
    public void onStopSimulation(ActionEvent event) {
        play = false;
        timer.stop();
        updateAnimation();
    }

    @FXML
    public void onChangeLeft(ActionEvent event) {
        pos -= 10;
        if (pos < 0) pos = 0;
        updateAnimation();
        progressBar.adjustValue(pos + 1);
    }

    @FXML
    public void onChangeRight(ActionEvent event) {
        pos += 10;
        if (pos >= NUM_STEPS) pos = NUM_STEPS - 1;
        updateAnimation();
        progressBar.adjustValue(pos + 1);
    }

    @FXML
    public void onPlayPause(ActionEvent event) {
        event.consume();
        play = !play;
        if (play) {
            timer.start();
        }
        else {
            timer.stop();
        }
    }

    @FXML
    private void onEditSimulation(ActionEvent event) {
        event.consume();
        System.out.println("onEditSimulation");
    }

    @FXML
    private void onDeleteSimulation(ActionEvent event) {
        event.consume();
        System.out.println("onDeleteSimulation");
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @FXML
    private void onClose(ActionEvent event) {
        int exitCode = SpringApplication.exit(applicationContext, () -> 0);
        System.exit(exitCode);
    }
}
