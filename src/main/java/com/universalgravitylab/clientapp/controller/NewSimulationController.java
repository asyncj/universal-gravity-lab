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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TextField bodyTextField;

    @FXML
    private TableView<Body> bodyTable;

    @FXML
    private TableColumn<Body, String> nameColumn;

    @FXML
    private TableColumn<Body, Double> massColumn;

    @FXML
    private TableColumn<Body, Color> colorColumn;

    @FXML
    private TableColumn<Body, double[]> positionColumn;

    @FXML
    private TableColumn<Body, double[]> velocityColumn;

    @FXML
    private Button newBodyButton;

    @FXML
    private Button editBodyButton;

    @FXML
    private Button deleteBodyButton;

    @FXML
    private Button createButton;

    @Override
    public void onClose(Event event) {

    }

    @FXML
    private void onNewBody(ActionEvent event) {
        mainController.onNewBody();
        System.out.println("onNewBody");
    }

    @FXML
    private void onEditBody(ActionEvent event) {
        Body body = bodyTable.getSelectionModel().getSelectedItem();
        mainController.onEditBody(body);
        System.out.println("onEditBody");
    }

    @FXML
    private void onCancelButton(ActionEvent event) {
        mainController.onCancelTab("Edit Simulation");
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void updateUI() {
        if (simulation == null) {
            return;
        }
        bodyTextField.setText(simulation.getName());
        List<Body> bodyList = simulation.getBodyList();
        ObservableList<Body> bodies = FXCollections.observableArrayList(bodyList);
        bodyTable.setItems(bodies);
        createButton.setText("Save");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        massColumn.setCellValueFactory(new PropertyValueFactory<>("mass"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(factory -> new ColoredTableCell());
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("rInit"));
        positionColumn.setCellFactory(param -> new DoubleArrayCell());
        velocityColumn.setCellValueFactory(new PropertyValueFactory<>("vInit"));
        velocityColumn.setCellFactory(param -> new DoubleArrayCell());

        bodyTable.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> updateButtons()
        );
    }

    private void updateButtons() {
        Body selectedItem = bodyTable.getSelectionModel().getSelectedItem();
        editBodyButton.setDisable(selectedItem == null);
        deleteBodyButton.setDisable(selectedItem == null);
    }
}
