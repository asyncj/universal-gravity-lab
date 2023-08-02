package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.Closable;
import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

@Component
public class SimulationController implements Closable {

    public static final double AU = 149_597_870_700d;

    @FXML
    private Slider progressBar;

    @FXML
    private Canvas canvas;

    private AnimationTimer timer;

    private Simulation simulation;

    private int pos;
    private int numSteps;

    private boolean play = true;
    private GraphicsContext gc;

    @FXML
    private void initialize() {
        progressBar.setMin(1);
        progressBar.setMax(numSteps);
        progressBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            pos = newValue.intValue() - 1;
            if (pos < 0) {
                pos = 0;
            }
            updateAnimation();
        });

        gc = canvas.getGraphicsContext2D();
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void startAnimation() {

        timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            @Override
            public void handle(long now) {
                lastTime = now;

                updateAnimation();

                pos++;
                onUpdateSimulation(pos);
                if (pos >= numSteps) {
                    pos = 0;
                }

            }
        };
        timer.start();
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
        progressBar.setMax(numSteps);
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
        if (pos >= numSteps) pos = numSteps - 1;
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

    private void onUpdateSimulation(int i) {
        progressBar.adjustValue(i + 1);
    }


    @Override
    public void onClose(Event event) {
        onStopSimulation(null);
    }
}
