package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.model.Body;
import javafx.scene.paint.Color;

public class ColoredTableCell extends javafx.scene.control.TableCell<com.universalgravitylab.clientapp.model.Body, javafx.scene.paint.Color> {
    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null) {
            setText(null);
            setGraphic(null);
        } else {
            Body body = getTableRow().getItem();
            if (body != null) {
                String colorAsString = getColorAsString(body.getColor());
                setText(colorAsString);
                setStyle("-fx-background-color: " + colorAsString);
            }
        }
    }

    public String getColorAsString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
