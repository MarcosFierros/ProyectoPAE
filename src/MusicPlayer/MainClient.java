package MusicPlayer;

import MusicPlayer.ui.ControllerClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainClient extends Application {

	 private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/resource/client.fxml"));
        StackPane root = (StackPane) fxmlloader.load();
        ControllerClient controller = (ControllerClient) fxmlloader.getController();
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root, 1028, 200);
        controller.setScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
