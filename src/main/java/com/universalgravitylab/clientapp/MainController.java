package com.universalgravitylab.clientapp;

import com.universalgravitylab.clientapp.controller.NewSimulationController;
import com.universalgravitylab.clientapp.controller.SimulationController;
import com.universalgravitylab.clientapp.model.Simulation;
import com.universalgravitylab.clientapp.service.SimulationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
    private TreeView treeView;

    @FXML
    private TabPane tabPane;

    private Map<String, Simulation> simulationMap = new LinkedHashMap<>();

    @FXML
    private void initialize() {
        List<Simulation> simulationList = simulationService.getSimulationList();

        for (Simulation simulation: simulationList) {
            treeView.setRoot(new TreeItem(simulation.getName()));
            simulation.runSimulation();
            simulationMap.put(simulation.getName(), simulation);
        }
        tabPane.getTabs().get(0).setUserData(treeView.getRoot().getValue());

        loadCurrentTab();

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> loadCurrentTab()
        );
        treeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> createTab("simulation")
        );
    }

    private void createTab(String id) {
        Object selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        TreeItem<String> selectedTreeItem = (TreeItem<String>) selectedItem;
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(selectedTreeItem.getValue()));
        if (nonMatch) {
            Tab tab = new Tab(selectedTreeItem.getValue());
            tab.setId(id);
            tab.setUserData(selectedTreeItem.getValue());
            tabPane.getTabs().add(tab);
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
                    Simulation simulation = simulationMap.get((String)currentTab.getUserData());
                    controller.setNumSteps(simulation.getNumSteps());
                    controller.setSimulation(simulation);

                    controller.startAnimation();
                }
                if (fxmlLoader.getController() instanceof NewSimulationController) {
                    NewSimulationController controller = fxmlLoader.getController();
                    Simulation simulation = simulationMap.get((String)currentTab.getUserData());
                    controller.setSimulation(simulation);
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
        }
    }

    @FXML
    private void onEditSimulation(ActionEvent event) {
        event.consume();
        String newSimulationName = "Edit Simulation";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(newSimulationName));
        if (nonMatch) {
            Tab tab = new Tab(newSimulationName);
            TreeItem<String>  selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();

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

    public void onNewBody() {
        String newBodyName = "New Body";
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(newBodyName));
        if (nonMatch) {
            Tab tab = new Tab(newBodyName);
            tab.setId("newBody");
            tabPane.getTabs().add(tab);
        }
    }
}
