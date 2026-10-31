package com.example.ui.controller;

import com.example.data.exception.StudentNotFoundException;
import com.example.data.exception.ValidationException;
import com.example.data.pojo.Student;
import com.example.data.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

@Component
public class StudentController {

    @FXML private TableView<Student> tableView;
    @FXML private TextField searchField, idField, nameField, emailField, ageField;
    @FXML private Button btnUpdate, btnDelete;
    @FXML private Label messageLabel, statusLabel;

    private final StudentService studentService;
    private final ObservableList<Student> data = FXCollections.observableArrayList();

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML
    public void initialize() {
        tableView.setItems(data);
        tableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    if (newSel != null) fillForm(newSel);
                });
        var noSelection = tableView.getSelectionModel().selectedItemProperty().isNull();
        btnUpdate.disableProperty().bind(noSelection);
        btnDelete.disableProperty().bind(noSelection);
        loadData();
    }

    private void runAsync(Supplier<List<Student>> query, String prefix) {
        Task<List<Student>> task = new Task<>() {
            @Override
            protected List<Student> call() {
                return query.get();
            }
        };
        statusLabel.setText("Loading...");
        searchField.setDisable(true);
        task.setOnSucceeded(e -> {
            data.setAll(task.getValue());
            statusLabel.setText(prefix + " " + data.size() + " students");
            searchField.setDisable(false);
        });
        task.setOnFailed(e -> {
            showError("Cannot load data: " + task.getException().getMessage());
            statusLabel.setText("Database error");
            searchField.setDisable(false);
        });
        Thread t = new Thread(task, "db-loader");
        t.setDaemon(true);
        t.start();
    }

    private void loadData() {
        runAsync(studentService::findAll, "Loaded");
    }

    @FXML
    private void handleSearch() {
        String kw = searchField.getText();
        runAsync(() -> studentService.search(kw), "Found");
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadData();
    }

    private Student readForm() {
        String ageText = ageField.getText() == null ? "" : ageField.getText().trim();
        if (ageText.isEmpty()) throw new ValidationException("Age is required");
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            throw new ValidationException("Age must be a number");
        }
        return new Student(nameField.getText(), emailField.getText(), age);
    }

    @FXML
    private void handleAdd() {
        try {
            Student saved = studentService.create(readForm());
            showInfo("Added: " + saved.getName());
            handleClear();
            loadData();
        } catch (ValidationException | StudentNotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("System error: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a student");
            return;
        }
        try {
            Student edit = readForm();
            edit.setId(selected.getId());
            Student updated = studentService.update(edit);
            showInfo("Updated: " + updated.getName());
            handleClear();
            loadData();
        } catch (ValidationException | StudentNotFoundException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("System error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a student");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete student \"" + selected.getName() + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Confirm delete");
        confirm.initOwner(tableView.getScene().getWindow());
        confirm.showAndWait()
               .filter(rs -> rs == ButtonType.OK)
               .ifPresent(rs -> {
                   try {
                       studentService.delete(selected.getId());
                       showInfo("Deleted: " + selected.getName());
                       handleClear();
                       loadData();
                   } catch (StudentNotFoundException e) {
                       showError(e.getMessage());
                       loadData();
                   } catch (Exception e) {
                       showError("System error: " + e.getMessage());
                   }
               });
    }

    @FXML
    private void handleClear() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        ageField.clear();
        tableView.getSelectionModel().clearSelection();
        nameField.requestFocus();
    }

    @FXML
    private void handleExport() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("students.csv");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fc.showSaveDialog(tableView.getScene().getWindow());
        if (file == null) return;
        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {
            pw.print('\uFEFF');
            pw.println("ID,Name,Email,Age");
            for (Student s : data) {
                pw.printf("%d,%s,%s,%d%n", s.getId(), csv(s.getName()), csv(s.getEmail()), s.getAge());
            }
            showInfo("Exported " + data.size() + " rows to " + file.getName());
        } catch (IOException e) {
            showError("Export failed: " + e.getMessage());
        }
    }

    private static String csv(String v) {
        if (v == null) return "";
        return (v.contains(",") || v.contains("\"")) ? "\"" + v.replace("\"", "\"\"") + "\"" : v;
    }

    private void fillForm(Student s) {
        idField.setText(String.valueOf(s.getId()));
        nameField.setText(s.getName());
        emailField.setText(s.getEmail());
        ageField.setText(String.valueOf(s.getAge()));
    }

    private void showInfo(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: red;");
    }
}
