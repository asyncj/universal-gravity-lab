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
import javafx.scene.effect.Glow;
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

    private Glow glow = new Glow();

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
        glow.setLevel(0.75);
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        int k = 100;
        for (int i = 0; i < simulation.getBodyList().size(); i++) {
            Body body = simulation.getBodyList().get(i);

            for (int j = pos - k; j < pos; j++) {
                if (j > 0) {
                    Color color = body.getColor();
                    double opacity = (pos - j) / (double)k;
                    gc.setFill(color.deriveColor(0, 1, 1, 1 - opacity));
                    gc.fillOval(250 + body.getR()[j][0] / AU * 100 - 1, 200 + body.getR()[j][1] / AU * 100 - 1, 2, 2);
                }
            }
            gc.setFill(body.getColor());
            double h = 16 + 8 * (Math.log10(body.getRadius()) - Math.log10(6371000));
            double x = 250 + body.getR()[pos][0] / AU * 100 - h / 2.0;
            double y = 200 + body.getR()[pos][1] / AU * 100 - h / 2.0;
            gc.fillOval(x, y, h, h);
            if (body.isStar()) {
                glow.setLevel(0.75);
                gc.applyEffect(glow);
            }
            String name = body.getName();
            gc.fillText(name, x + 18, y - 6);
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
