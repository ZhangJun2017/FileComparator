package top.s0uths1de.function;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import top.s0uths1de.core.FileComparator;
import top.s0uths1de.entity.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetButton {
    public static void setInfo(Button button, Stage stage, FileEntity fe) {
        button.setText("读取信息文件");
        button.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fe.setInfo(fileChooser.showOpenDialog(stage));
        });

        setOnDragOver(button);

        button.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    if (file != null) {
                        fe.setInfo(file);
                        return;
                    }
                }
            }
        });
    }

    public static void setExplorer(Button button, Stage stage, FileEntity fe) {
        button.setText("读取作业");
        button.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folder = directoryChooser.showDialog(stage);
            if (folder != null) fe.setHomework(folder);
        });

        setOnDragOver(button);

        button.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                for (File file : db.getFiles()) {
                    if (file != null) {
                        fe.setHomework(file);
                        return;
                    }
                }
            }
        });
    }

    private static void setOnDragOver(Button explorerButton) {
        explorerButton.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) event.acceptTransferModes(TransferMode.ANY);
        });
    }

    public static void setStart(Button button, Stage stage, FileEntity fe) {
        button.setOnAction(actionEvent -> {
            if (fe.getInfo() == null || fe.getHomework() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                if (fe.getInfo() == null && fe.getHomework() == null) alert.setContentText("Info and Homework is null");
                else if (fe.getHomework() == null) alert.setContentText("HomeWork is null");
                else alert.setContentText("Info is null");
                alert.showAndWait();
                return;
            }
            FileComparator comparator = new FileComparator(fe.getInfo(), fe.getHomework());
            Map<String, String> infoMap = comparator.getInfoMap();
            List<String> directoryList = comparator.getDirectoryList();
            List<String> id = comparator.getId();
            List<String> name = comparator.getName();
            int mapSize = infoMap.size();
            int listSize = directoryList.size();
            List<String> unpaidList = new ArrayList<>();
            List<String> nameError = new ArrayList<>();
            List<String> idError = new ArrayList<>();

        });
    }
}