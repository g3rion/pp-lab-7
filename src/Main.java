import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
            resultArea.setText("Please provide a directory path.");
            return;
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            resultArea.setText("The provided path is not a directory.");
            return;
        }

        StringBuilder results = new StringBuilder();
        searchInDirectory(directory, searchPhrase, results);
        resultArea.setText(results.toString());
    }

    private boolean containsPhrase(File file, String searchPhrase) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchPhrase)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void searchInDirectory(File directory, String searchPhrase, StringBuilder results) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && containsPhrase(file, searchPhrase)) {
                    results.append(file.getAbsolutePath()).append("\n");
                } else if (file.isDirectory()) {
                    searchInDirectory(file, searchPhrase, results);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
