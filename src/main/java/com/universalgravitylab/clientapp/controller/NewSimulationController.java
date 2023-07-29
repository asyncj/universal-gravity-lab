package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.Closable;
import com.universalgravitylab.clientapp.MainController;
import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewSimulationController implements Closable {

    @Autowired
    private MainController mainController;
    private Simulation simulation;

    @FXML
    private TableView<Body> bodyTable;

    @Override
    public void onClose(Event event) {

    }

    @FXML
    private void onNewBody(ActionEvent event) {
        mainController.onNewBody();
        System.out.println("onNewBody");
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void updateUI() {
        List<Body> bodyList = simulation.getBodyList();
        for (Body body: bodyList) {
            bodyTable.getItems().add(body);
        }
    }
}
