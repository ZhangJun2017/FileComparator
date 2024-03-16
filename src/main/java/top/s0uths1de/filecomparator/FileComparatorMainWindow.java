package top.s0uths1de.filecomparator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import top.s0uths1de.core.FileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileComparatorMainWindow extends Application {
    private static final String TITLE = "FileComparator bate:0.1.4";
    private static final int WIDTH = 854;
    private static final int HEIGHT = 480;
    private static int clickCount = 0;
    private static int count = 0;
    private final ListView<String> unsubmittedListView;
    private final ListView<String> wrongNameListView;
    private List<String> unsubmittedCsvList;
    private List<String> wrongNameListViewDirectoryFiles;
    private List<String> csvList;
    private List<String> directoryFiles;
    private String csvFilePath;
    private String directoryPath;
    private final HBox hBox;

    public FileComparatorMainWindow() {
        this.hBox = new HBox();
        this.unsubmittedListView = new ListView<>();
        this.wrongNameListView = new ListView<>();
        this.unsubmittedListView.getItems().add("未交作业的同学");
        this.unsubmittedListView.setStyle("-fx-background-color: transparent");
        this.wrongNameListView.getItems().add("名字错误的同学");
        this.wrongNameListView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");
        this.unsubmittedCsvList = new ArrayList<>();
        this.wrongNameListViewDirectoryFiles = new ArrayList<>();
        this.csvList = new ArrayList<>();
        this.directoryFiles = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) {

        Button infoButton = setInfo(stage);
        Button explorerButton = setExplorer(stage);
        Button beginButton = setBegin();
        Media media = new Media(Objects.requireNonNull(this.getClass().getResource("/top/s0uths1de/filecomparator/assets/sound/music.mp3")).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));

        hBox.getChildren().addAll(infoButton, explorerButton, beginButton);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        setButtonStyle(infoButton);
        setButtonStyle(explorerButton);
        setButtonStyle(beginButton);

        Image image = new Image(Objects.requireNonNull(this.getClass().getResource("/top/s0uths1de/filecomparator/assets/query.png")).toString());
        Scene scene = new Scene(hBox, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE)
                stage.close();
        });
        stage.getIcons().add(image);
        scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/top/s0uths1de/filecomparator/assets/css/main.css")).toExternalForm());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    private Button setInfo(Stage stage) {
        Button infoButton = new Button();
        infoButton.setText("读取信息文件");
        infoButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null)
                csvFilePath = file.getPath();
        });
        return infoButton;
    }

    private Button setExplorer(Stage stage) {
        Button explorerButton = new Button();
        explorerButton.setText("读取作业");
        explorerButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folder = directoryChooser.showDialog(stage);
            if (folder != null)
                directoryPath = folder.getPath();
        });
        return explorerButton;
    }

    private void setButtonStyle(Button button){
        String buttonStyle = Objects.requireNonNull(this.getClass().getResource("/top/s0uths1de/filecomparator/assets/css/button.css")).toExternalForm();
        button.getStylesheets().add(buttonStyle);
    }

    private Button setBegin() {
        Button beginButton = new Button();
        if (beginButton.getText().isEmpty())
            beginButton.setText("开始");
        beginButton.setOnAction(actionEvent -> {
            if (csvFilePath == null || directoryPath == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                if (csvFilePath == null)
                    alert.setContentText("csvFilePath 为空");
                else
                    alert.setContentText("directoryPath  为空");
                alert.showAndWait();
                return;
            }
            FileComparator comparator = new FileComparator(csvFilePath, directoryPath);

            this.csvList = comparator.getCsvList();
            this.directoryFiles = comparator.getDirectoryList();

            for (String file : csvList)
                if (!directoryFiles.contains(file))
                    unsubmittedCsvList.add(file);
            for (String file : directoryFiles)
                if (!csvList.contains(file))
                    wrongNameListViewDirectoryFiles.add(file);
            for (String file : unsubmittedCsvList)
                unsubmittedListView.getItems().add(file);
            for (String file : wrongNameListViewDirectoryFiles)
                wrongNameListView.getItems().add(file);

            if (count == 0)
                this.hBox.getChildren().addAll(unsubmittedListView, wrongNameListView);
            if (clickCount == 1) {
                unsubmittedListView.getItems().clear();
                wrongNameListView.getItems().clear();
                unsubmittedCsvList = new ArrayList<>();
                wrongNameListViewDirectoryFiles = new ArrayList<>();
                clickCount = 0;
                csvFilePath = null;
                directoryFiles = null;
            }
            clickCount++;
            count++;
            if (beginButton.getText().equals("开始"))
                beginButton.setText("重新选择");
            else
                beginButton.setText("开始");
        });
        return beginButton;
    }

    public static void main(String[] args) {
        launch();
    }
}