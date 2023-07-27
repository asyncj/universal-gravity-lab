package com.universalgravitylab.clientapp;

import com.universalgravitylab.clientapp.model.Simulation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ArrayList;
import java.util.List;

/*
--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml
 */
@Component
public class MainController {

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    private TreeView treeView;

    @FXML
    private Tab newSimulationTab;

    @FXML
    private TabPane tabPane;

    private List<Simulation> simulationList = new ArrayList<>();

    @FXML
    private void initialize() {
        treeView.setRoot(new TreeItem("Sun/Earth"));

        loadCurrentTab();

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> loadCurrentTab()
        );
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                createTab("simulation");
            }
        });
    }

    private void createTab(String id) {
        Object selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        TreeItem<String> selectedTreeItem = (TreeItem<String>) selectedItem;
        boolean nonMatch = tabPane.getTabs().stream().noneMatch(tab -> tab.getText().equals(selectedTreeItem.getValue()));
        if (nonMatch) {
            Tab tab = new Tab(selectedTreeItem.getValue());
            tab.setId(id);
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
                currentTab.setOnClosed(event -> closable.onClose(event));
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
        createTab("simulation");
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
