package sample;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Stage stage;
    Scene scene;

    @FXML
    private JFXTreeTableView<Song> treeView;

    @FXML
    private void closeButtonAction(ActionEvent event) {
        scene = ((Button)event.getSource()).getScene();
        stage = (Stage) scene.getWindow();
        stage.close();
    }

    @FXML
    private void minimizeButtonAction(ActionEvent event) {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizeButtonAction(ActionEvent event) {
        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        if(!stage.isMaximized())
            stage.setMaximized(true);
        else
            stage.setMaximized(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<Song, String> songTitle = new JFXTreeTableColumn<>("TÍTULO");
        songTitle.setPrefWidth(500);

        JFXTreeTableColumn<Song, String> songArtist = new JFXTreeTableColumn<>("ARTISTA");
        songArtist.setPrefWidth(200);

        JFXTreeTableColumn<Song, String> songAlbum = new JFXTreeTableColumn<>("ÁLBUM");
        songAlbum.setPrefWidth(150);

        JFXTreeTableColumn<Song, String> songDuration = new JFXTreeTableColumn<>("DURACIÓN");
        songDuration.setPrefWidth(150);

        ObservableList<Song> songs = FXCollections.observableArrayList();

        final TreeItem<Song> root = new RecursiveTreeItem<>(songs, RecursiveTreeObject::getChildren);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setEditable(true);

        treeView.getColumns().setAll(songTitle, songArtist, songAlbum, songDuration);

    }
}
