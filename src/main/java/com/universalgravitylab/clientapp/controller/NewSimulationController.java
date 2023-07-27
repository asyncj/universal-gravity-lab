package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.Closable;
import com.universalgravitylab.clientapp.MainController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewSimulationController implements Closable {

    @Autowired
    private MainController mainController;

    @Override
    public void onClose(Event event) {

    }

    @FXML
    private void onNewBody(ActionEvent event) {
        mainController.onNewBody();
        System.out.println("onNewBody");
    }
}
