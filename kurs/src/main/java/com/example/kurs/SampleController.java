package com.example.kurs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;

public class SampleController {
    @FXML
    private Button settingsButton;

    @FXML
    private Button ChangeColorAction;


    @FXML
    private TextField NumRecordsField;

    @FXML
    private Label ResultLabel;

    @FXML
    private ContextMenu settingsMenu;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField NameField;

    @FXML
    private TextField IDField;

    @FXML
    private TextField PositionField;

    @FXML
    private TextField HireDateField;

    @FXML
    private Label NumRecordsLabel;

    @FXML
    private Button AddToFileButton;

    @FXML
    private Button BufferButton;

    @FXML
    private GridPane DataGridPane;

    @FXML
    private TextField SearchField;

    @FXML
    private Button DeleteButton;

    @FXML
    private TextField DeleteEmployeeField;

    @FXML
    private Button UpdateButton;

    @FXML
    private TextField NewEmployeeField;

    @FXML
    private VBox optionsContainer;

    @FXML
    private Button ClearDataButton;

    private ArrayList<HRDepartment> addList = new ArrayList<>();
    private String db_path = "src/data.txt";

    @FXML
    private Label dateTimeLabel;

    private void updateDateTime(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        String currentDate = sdfDate.format(new Date());
        String currentTime = sdfTime.format(new Date());
        dateTimeLabel.setText("Date: " + currentDate + "    Time: " + currentTime);
        dateTimeLabel.setStyle("-fx-text-fill: white;");
    }
    @FXML
    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList (
                "ПІБ співробітника",
                "ID співробітника",
                "Посада",
                "Cтаж роботи"
        );
        comboBox.setItems(options);

        db_path = "HRdepart.txt";

        loadDataFromFile(db_path);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateDateTime()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void showOptions() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Налаштування");
        alert.setHeaderText("Виберіть дію:");

        Button infoButton = new Button("Інформація про програму");
        infoButton.setOnAction(event -> showInfo());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(infoButton);

        alert.getDialogPane().setContent(vbox);

        alert.showAndWait();
    }

    @FXML
    public void showInfo() {
        Alert infoAlert = new Alert(AlertType.INFORMATION);
        infoAlert.setTitle("Інформація про програму");
        infoAlert.setHeaderText("Про програму");
        infoAlert.setContentText("desktop-застосунок з графічним  інтерфейсом для автоматизації діяльності співробітника відділу кадрів");
        infoAlert.showAndWait();
    }

    @FXML
    void saveToFile(ActionEvent event) {
        if (NameField.getText().isEmpty() ||
                IDField.getText().isEmpty() ||
                PositionField.getText().isEmpty() ||
                HireDateField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            Stage stage = (Stage) dialogPane.getScene().getWindow();

            alert.setTitle("Помилка введення");
            alert.setHeaderText("Заповніть усі поля!");
            alert.setContentText(null);
            alert.showAndWait();
            return;

        }

        String name = NameField.getText();
        int id = Integer.parseInt(IDField.getText());
        String position = PositionField.getText();
        String hireDate = HireDateField.getText();

        HRDepartment employee = new HRDepartment(name, id, position, hireDate);

        addList.add(employee);

        NumRecordsLabel.setText("Кількість записів у буфері: " + addList.size());

        int j = 0;
        File file = new File(db_path);
        if (!file.exists()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Файл відсутній. Оберіть файл для запису.");
            return;
        }

        if (addList.size() == 0) {
            showAlert(Alert.AlertType.ERROR, "Записи відсутні!");
        } else {
            try (PrintWriter out = new PrintWriter(new FileWriter(db_path, true))) {
                Iterator<HRDepartment> al = addList.iterator();
                while (al.hasNext()) {
                    HRDepartment element = al.next();
                    element.writeData(out);
                    j = j + 1;
                }

                NumRecordsLabel.setText("Кількість записів у буфері: " + j);

                addList.clear();

                showAlert(Alert.AlertType.INFORMATION, "Співробітник успішно доданий в буфер!");
            } catch (IOException exception) {
                exception.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Сталася помилка при записі в файл!");
            }
        }

        clearFields();

    }

    @FXML
    private void searchEmployee(ActionEvent event) {
        String keyword = SearchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Будь ласка, введіть ПІБ співробітника");
            return;
        }

        DataGridPane.getChildren().clear();
        int row = 0;

        boolean found = false;
        for (HRDepartment employee : addList) {
            if (employee.getName().toLowerCase().contains(keyword)) {
                addGridCell("ПІБ співробітника: " + employee.getName(), 0, row++);
                addGridCell("ID співробітника: " + employee.getId(), 0, row++);
                addGridCell("Посада: " + employee.getPosition(), 0, row++);
                addGridCell("Cтаж роботи: " + employee.getHireDate(), 0, row++);

                addGridCell("=======================", 0, row++);

                found = true;

            }
        }
        if (!found) {
            showAlert(Alert.AlertType.INFORMATION, "Нічого не знайдено!");
        }
    }

    @FXML
    private void changeEmployee(ActionEvent event) {
        String name = SearchField.getText().trim();
        String newEmployee = NewEmployeeField.getText().trim();
        String selectedField = comboBox.getEmployee();

        if (name.isEmpty() || newEmployee.isEmpty() || selectedField == null) {
            showAlert(Alert.AlertType.ERROR, "Заповніть всі поля!");
            return;
        }

        boolean found = false;
        for (HRDepartment employee : addList) {
            if (employee.getName().equalsIgnoreCase(name)) {
                try {
                    switch (selectedField) {
                        case "ID":
                            employee.setId(Integer.parseInt(newEmployee));
                            break;
                        case "Посада":
                            employee.setPosition(newEmployee);
                            break;
                        case "Cтаж роботи":
                            employee.setHireDate(newEmployee);
                            break;
                        default:
                            showAlert(Alert.AlertType.ERROR, "Невірне поле для зміни!");
                            return;
                    }
                    found = true;
                    showAlert(Alert.AlertType.INFORMATION, "Співробітник оновлен!");
                    addToBuffer();
                    break;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Невірний формат для нового значення!");
                    return;
                }
            }
        }
        if (!found) {
            showAlert(Alert.AlertType.ERROR, "Співробітник не знайден!");
        }
    }

    @FXML
    void addToBuffer() {
        if (NameField.getText().isEmpty() ||
                IDField.getText().isEmpty() ||
                PositionField.getText().isEmpty() ||
                HireDateField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            Stage stage = (Stage) dialogPane.getScene().getWindow();

            alert.setTitle("Помилка введення");
            alert.setHeaderText("Заповніть всі поля!");
            alert.setContentText(null);
            alert.showAndWait();
            return;
        }

        try {
            String name = NameField.getText();
            int id = Integer.parseInt(IDField.getText());
            String position = PositionField.getText();
            String hireDate = HireDateField.getText();

            HRDepartment employee = new HRDepartment(name, id, position, hireDate);
            addList.add(employee);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            alert.setTitle("Дані збережені");
            alert.setHeaderText("Дані успішно збережені!");
            alert.setContentText(null);
            alert.showAndWait();
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            DialogPane dialogPane = alert.getDialogPane();
            Stage stage = (Stage) dialogPane.getScene().getWindow();

            alert.setTitle("Помилка введеня");
            alert.setHeaderText("Невірний формат!");
            alert.setContentText(null);
            alert.showAndWait();
        }

        NameField.clear();
        IDField.clear();
        PositionField.clear();
        HireDateField.clear();
        NumRecordsField.setText("Кількість у буфері: " + addList.size());
    }

    @FXML
    void showData() {
        DataGridPane.getChildren().clear();
        int row = 0;
        for (HRDepartment employee : addList) {
            addGridCell("ПІБ співробітника:", 0, row);
            addGridCell(employee.getName(), 1, row++);
            addGridCell("ID співробітника:", 0, row);
            addGridCell(employee.getId(), 1, row++);
            addGridCell("Посада:", 0, row);
            addGridCell(employee.getPosition(), 1, row++);
            addGridCell("Cтаж роботи:", 0, row);
            addGridCell(employee.getHireDate(), 1, row++);

            addGridCell("=======================", 0, row++);


        }

        NumRecordsLabel.setText("Кількість записів: " + addList.size());
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        String name = DeleteEmployeeField.getText().trim();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Будь ласка, введіть ПІБ співробітника для видалення!");
            return;
        }

        boolean found = false;
        Iterator<HRDepartment> iterator = addList.iterator();
        while (iterator.hasNext()) {
            HRDepartment employee = iterator.next();
            if (employee.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            showAlert(Alert.AlertType.INFORMATION, "Співробітник успішно видален!");
            addToBuffer();
            showData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Співробітник не знайдений!");

    }
        DeleteEmployeeField.clear();

}
    private void loadDataFromFile(String filePath) {
        addList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String name = parts[0].split(":")[1].trim();
                    int id = Integer.parseInt(parts[1].split(":")[1].trim());
                    String position = parts[2].split(":")[1].trim();
                    String hireDate = parts[3].split(":")[1].trim();

                    addList.add(new HRDepartment(name, id, position, hireDate));
                }
            }

            NumRecordsLabel.setText("Кількість записів у буфері: " + addList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
}

    private void addGridCell(String text, int col, int row) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setTextFill(textColor);
        DataGridPane.add(label, col, row);
    }

    @FXML
    private void updateRecordCount() {
        int numRecords = addList.size();
        ResultLabel.setText("Кількість записів: " + numRecords);
    }

    @FXML
    private void openFileAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Відкрити файл");

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            db_path = selectedFile.getAbsolutePath();
            loadDataFromFile(db_path);
        }
    }

    private Color textColor = Color.BLACK;

    @FXML
    private void changeColorAction(ActionEvent event) {
        Stage window = new Stage();
        window.setTitle("Оберіть колір");
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #111111");
        pane.setBackground(Background.EMPTY);

        ColorPicker cp = new ColorPicker();
        cp.setPrefWidth(130);
        cp.setPrefHeight(70);
        cp.setStyle("-fx-font-size: 18px;");

        Button btn = new Button();
        btn.setPrefSize(130, 70);
        btn.setText("Обрати");
        btn.setStyle("-fx-font-size: 18px;");

        btn.setOnAction(e -> {
            textColor = cp.getValue();
            window.close();
        });

        pane.setRight(cp);
        pane.setLeft(btn);
        window.setScene(new Scene(pane, 400, 70));
        window.setResizable(false);
        window.show();
    }

    @FXML
    private void clearFields() {
        DataGridPane.getChildren().clear();
        DeleteEmployeeField.clear();
        NumRecordsField.clear();
        ResultLabel.setText("");
    }
}