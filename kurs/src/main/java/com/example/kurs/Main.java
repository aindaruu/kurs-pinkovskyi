package com.exaple.kurs;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.io.IOException;

public class Main extends Application {
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlLocation = getClass().getResource("/com/example/kurs/Sample.fxml");

            if (fxmlLocation == null) {
                throw new IOException("FXML файл 'Sample.fxml' не знайден по шляху '/com/example/kurs/'. Перевірте структуру проекту.");
            }
            loader.setLocation(fxmlLocation);
            GridPane root = loader.load();

            Scene scene = new Scene(root, 1359, 753);

            URL cssLocation = getClass().getResource("/com/example/kurs/application.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            } else {
                System.err.println("CSS файл 'application.css' не знайден. Інтерфейс буде без стилів.");
            }
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);

            URL iconLocation = getClass().getResource("/HRDepartment.png");
            if (iconLocation != null) {
                primaryStage.getIcons().add(new Image(iconLocation.toExternalForm()));
            } else {
                System.err.println("Іконка 'HRDepartment.png' не знайдена. Використовується стандартна іконка.");
            }
            primaryStage.setTitle("Автоматизація діяльності співробітника відділу кадрів");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Помилка під час завантаження інтерфейсу: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Непередбачена помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}