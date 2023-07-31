package com.universalgravitylab.clientapp.controller;

import com.universalgravitylab.clientapp.Closable;
import com.universalgravitylab.clientapp.MainController;
import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class NewSimulationController implements Closable, Initializable {

    @Autowired
    private MainController mainController;
    private Simulation simulation;

    @FXML
    private TableView<Body> bodyTable;

    @FXML
    private TableColumn<Body, String> nameColumn;

    @FXML
    private TableColumn<Body, Double> massColumn;

    @FXML
    private TableColumn<Body, Color> colorColumn;

    @FXML
    private TableColumn<Body, String> positionColumn;

    @FXML
    private TableColumn<Body, String> velocityColumn;

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
        ObservableList<Body> bodies = FXCollections.observableArrayList(bodyList);
        bodyTable.setItems(bodies);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        massColumn.setCellValueFactory(new PropertyValueFactory<>("mass"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(factory -> new ColoredTableCell());
    }
}
