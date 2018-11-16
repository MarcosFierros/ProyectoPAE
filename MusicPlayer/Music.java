package MusicPlayer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class Music{
	
	
private Iterator<String> songIterator;
public void start(Stage primaryStage) {
    ObservableList<String> songs =FXCollections.observableArrayList("Stargazing.mp3","Me acuerdo.mp3");
    

    // start with first song
    songIterator = songs.iterator();
    Label songLabel = new Label(songIterator.next());
    ListView<String> lv = new ListView(songs);

    MultipleSelectionModel<String> selectionModel = lv.getSelectionModel();

}
}
   
        