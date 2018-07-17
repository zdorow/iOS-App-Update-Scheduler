package com.app.update.scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/app_update_schedule.fxml"));
        root.setStyle("-fx-padding: 10;" +
        		"-fx-border-style: solid centered;" +
        		"-fx-border-width: 10;" +
        		"-fx-border-insets: 1;" +
        		"-fx-border-radius: 3;" +
        		"-fx-border-color: white;" +
        		"-fx-background-color: rgb(217, 219, 221);");
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("bootstrapfx.css");
 
        primaryStage.setResizable(false);
        primaryStage.setTitle("The App Update Scheduler");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}