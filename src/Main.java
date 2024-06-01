import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Main extends Application {
    private TextField directoryPathField;
    private TextField searchField;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Browser and Search");

        directoryPathField = new TextField();
        directoryPathField.setPromptText("Enter directory path");

        searchField = new TextField();
        searchField.setPromptText("Enter search phrase");

        resultArea = new TextArea();
        resultArea.setPrefHeight(400);
        resultArea.setEditable(false);

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(event -> browseDirectory());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> searchFiles());

        HBox hBox = new HBox(10, directoryPathField, browseButton);

        VBox vBox = new VBox(10, hBox, searchField, searchButton, resultArea);

        Scene scene = new Scene(vBox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void searchFiles() {
        String directoryPath = directoryPathField.getText();
        String searchPhrase = searchField.getText();
        if (directoryPath == null || directoryPath.isEmpty()) {
            resultArea.setText("Please select a directory.");
            return;
        }
        if (searchPhrase == null || searchPhrase.isEmpty()) {
            resultArea.setText("Please enter a search phrase.");
            return;
        }

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.contains(searchPhrase));
            if (files != null && files.length > 0) {
                StringBuilder results = new StringBuilder("Search results:\n");
                Arrays.stream(files).forEach(file -> results.append(file.getAbsolutePath()).append("\n"));
                resultArea.setText(results.toString());
            } else {
                resultArea.setText("No files found.");
            }
        } else {
            resultArea.setText("Invalid directory.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
