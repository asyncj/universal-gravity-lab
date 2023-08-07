package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.Closable;
import com.universalgravitylab.clientapp.model.Body;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class NewBodyController implements Closable, Initializable {
    private Body body;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField bodyNameTextField;

    @FXML
    private TextField bodyMassTextField;

    @FXML
    private TextField posXTextField;

    @FXML
    private TextField posYTextField;

    @FXML
    private TextField posZTextField;

    @FXML
    private TextField velXTextField;

    @FXML
    private TextField velYTextField;

    @FXML
    private TextField velZTextField;

    @FXML
    private ColorPicker bodyColorPicker;

    @Override
    public void onClose(Event event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void updateUI() {
        if (body == null) {
            return;
        }
        bodyNameTextField.setText(body.getName());
        bodyMassTextField.setText(String.valueOf(body.getMass()));
        double[] rInit = body.getRInit();
        posXTextField.setText(String.valueOf(rInit[0]));
        posYTextField.setText(String.valueOf(rInit[1]));
        posZTextField.setText(String.valueOf(rInit[2]));
        double[] vInit = body.getVInit();
        velXTextField.setText(String.valueOf(vInit[0]));
        velYTextField.setText(String.valueOf(vInit[1]));
        velZTextField.setText(String.valueOf(vInit[2]));

        bodyColorPicker.setValue(body.getColor());
        titleLabel.setText("Edit Body");
    }
}
