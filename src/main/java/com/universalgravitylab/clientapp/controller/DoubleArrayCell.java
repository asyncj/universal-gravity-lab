package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.model.Body;
import javafx.scene.control.TableCell;

import java.text.DecimalFormat;

public class DoubleArrayCell extends TableCell<Body, double[]> {

    private DecimalFormat scientificFormat = new DecimalFormat("0.##E0");

    @Override
    protected void updateItem(double[] item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            String text = "x: " + scientificFormat.format(item[0])
                    + ", y: " + scientificFormat.format(item[1])
                    + ", z: " + scientificFormat.format(item[2]);
            setText(text);
        }
    }
}
