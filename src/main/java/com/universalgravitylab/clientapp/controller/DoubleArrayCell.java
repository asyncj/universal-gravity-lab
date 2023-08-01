package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.model.Body;
import javafx.scene.control.TableCell;

public class DoubleArrayCell extends TableCell<Body, double[]> {
    @Override
    protected void updateItem(double[] item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            String text = "x: " + item[0] + ", y: " + item[1] + ", z: " + item[2];
            setText(text);
        }
    }
}
