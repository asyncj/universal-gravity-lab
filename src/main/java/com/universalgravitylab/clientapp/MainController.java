package com.universalgravitylab.clientapp;

import com.universalgravitylab.clientapp.controller.NewBodyController;
import com.universalgravitylab.clientapp.controller.NewSimulationController;
import com.universalgravitylab.clientapp.controller.SimulationController;
import com.universalgravitylab.clientapp.model.Body;
import com.universalgravitylab.clientapp.model.Simulation;
import com.universalgravitylab.clientapp.service.SimulationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.universalgravitylab.clientapp.service.SimulationService.SUN_EARTH;

/*
--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
 */
@Component
public class MainController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SimulationService simulationService;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private MenuItem editSimulationItem;

    @FXML
    private MenuItem deleteSimulationItem;

    private Map<String, Simulation> simulationMap = new LinkedHashMap<>();

    @FXML
    private void initialize() {
        List<Simulation> simulationList = simulationService.getSimulationList();

        TreeItem<String> simulationsItem = new TreeItem<>("Simulations");
        treeView.setRoot(simulationsItem);
        for (Simulation simulation: simulationList) {
            simulationsItem.getChildren().add(new TreeItem<>(simulation.getName()));
            //simulation.runSimulation();
            simulationMap.put(simulation.getName(), simulation);
        }
        TreeItem<String> root = treeView.getRoot();
        root.setExpanded(true);

        tabPane.getTabs().get(0).setUserData(simulationsItem.getChildren().get(0).getValue());

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> loadCurrentTab()
        );
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> createTab("simulation")
        );

        treeView.getSelectionModel().select(4);
    }

    @FXML
    private void onMouseClicked(MouseEvent event) {
        Object selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && event.getButton() == MouseButton.PRIMARY) {
            createTab("simulation");
        }
    }

    private void createTab(String id) {
        Object selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        TreeItem<String> selectedTreeItem = (TreeItem<String>) selectedItem;
        String treeItemName = selectedTreeItem.getValue();
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(treeItemName));
        if (nonMatch && !treeItemName.equals("Simulations")) {
            Tab tab = new Tab(treeItemName);
            tab.setId(id);
            tab.setUserData(treeItemName);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }

    private void loadCurrentTab() {
        try {
            Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
            if (currentTab == null) return;
            String id = currentTab.getId();
            String fileName = StringUtils.capitalize(id) + ".fxml";

            if (currentTab.getContent() == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/universalgravitylab/clientapp/" + fileName));
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                Parent root = fxmlLoader.load();

                currentTab.setContent(root);
                Closable closable = fxmlLoader.getController();
                currentTab.setOnClosed(closable::onClose);
                if (fxmlLoader.getController() instanceof SimulationController) {
                    SimulationController controller = fxmlLoader.getController();
                    Simulation simulation = simulationMap.get(currentTab.getUserData());
                    controller.setNumSteps(simulation.getNumSteps());
                    controller.setSimulation(simulation);
                    simulation.runSimulation();

                    controller.startAnimation();
                }
                if (fxmlLoader.getController() instanceof NewSimulationController) {
                    NewSimulationController controller = fxmlLoader.getController();
                    Simulation simulation = simulationMap.get((String)currentTab.getUserData());
                    controller.setSimulation(simulation);
                    controller.updateUI();
                }
                if (fxmlLoader.getController() instanceof NewBodyController) {
                    NewBodyController controller = fxmlLoader.getController();
                    Body body = (Body) currentTab.getUserData();
                    controller.setBody(body);
                    controller.updateUI();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void newSimulation(ActionEvent event) {
        event.consume();
        String newSimulationName = "New Simulation";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(newSimulationName));
        if (nonMatch) {
            Tab tab = new Tab(newSimulationName);
            tab.setId("newSimulation");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }

    @FXML
    private void onEditSimulation(ActionEvent event) {
        event.consume();
        String newSimulationName = "Edit Simulation";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(newSimulationName));
        if (nonMatch) {
            Tab tab = new Tab(newSimulationName);
            TreeItem<String>  selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            tab.setUserData(selectedItem.getValue());
            tab.setId("newSimulation");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }

    @FXML
    private void onDeleteSimulation(ActionEvent event) {
        event.consume();
        System.out.println("onDeleteSimulation");
    }

    @FXML
    private void onClose(ActionEvent event) {
        int exitCode = SpringApplication.exit(applicationContext, () -> 0);
        System.exit(exitCode);
    }


    @FXML
    public void onTreeViewContextMenuShowing(WindowEvent event) {
        TreeItem<String>  selectedItem = treeView.getSelectionModel().getSelectedItem();
        editSimulationItem.setDisable(selectedItem == null);
        deleteSimulationItem.setDisable(selectedItem == null);
    }


    public void onNewBody() {
        String newBodyName = "New Body";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(newBodyName));
        if (nonMatch) {
            Tab tab = new Tab(newBodyName);
            tab.setId("newBody");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }

    public void onCancelTab(String tabName) {
        tabPane.getTabs().removeIf(tab -> tab.getText().equalsIgnoreCase(tabName));
    }

    public void onEditBody(Body body) {
        String editBodyName = "Edit Body";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(editBodyName));
        if (nonMatch) {
            Tab tab = new Tab(editBodyName);
            tab.setUserData(body);
            tab.setId("newBody");
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }
}
