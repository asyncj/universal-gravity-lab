package com.universalgravitylab.clientapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientappApplication extends Application {

    private ConfigurableApplicationContext applicationContext;


    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(ClientappApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/universalgravitylab/clientapp/MainView.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setRoot(new BorderPane());

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);



        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
