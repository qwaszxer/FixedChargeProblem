package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    private static File selectedDirectory;

    private static String choice;

    @FXML
    private Button referenceButton1;

    @FXML
    private Button referenceButton2;

    @FXML
    private Button referenceButton3;

    @FXML
    private Button referenceButton4;

    @FXML
    private Button solveButton;

    @FXML
    private Button directoryButton;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private void initialize() {

        choiceBox.setItems(FXCollections.observableArrayList("Автоматически", "Из файла"));

        solveButton.setOnAction((event) -> {
            if ((choice != null) && (selectedDirectory != null)) {
                Balinski balinski = new Balinski();
                outputTextArea.appendText(balinski.solve(choice, selectedDirectory));
            }
            else
                outputTextArea.appendText("Заданы не все исходные данные или параметры. \n\n");
        });

        directoryButton.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            selectedDirectory = fileChooser.showOpenDialog(null);
            if (selectedDirectory != null)
                outputTextArea.appendText("Выбран файл: " + selectedDirectory.getAbsolutePath() + "\n\n");
            else
                outputTextArea.appendText("Файл не выбран. \n\n");
        });

        choiceBox.setOnAction((event) -> {
            choice = choiceBox.getSelectionModel().getSelectedItem().toString();
            if (("Автоматически").equals(choice)) {
                outputTextArea.appendText("Верхняя граница М будет рассчитана автоматически. \n\n");
            }
            else {
                outputTextArea.appendText("Верхняя граница М будет задана из файла. \n\n");
            }
        });

        referenceButton1.setOnAction((event) -> {

        });

        referenceButton2.setOnAction((event) -> {

        });

        referenceButton3.setOnAction((event) -> {

        });

        referenceButton4.setOnAction((event) -> {

        });
    }
}
