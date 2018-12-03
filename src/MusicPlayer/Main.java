package MusicPlayer;

import java.util.Locale;

import MusicPlayer.ui.Controller;
import MusicPlayer.ui.I18N;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

	@Override
	public void init() {
		I18N.setSupportedLocales(Locale.ENGLISH, new Locale("mx", "es"));
		I18N.setResourceBundlePath("resource.i18n.strings");
	}
    
    @Override
    public void start(Stage primaryStage) throws Exception{

    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/resource/sample.fxml"), I18N.getResources());
        StackPane root = (StackPane) fxmlloader.load();
        Controller controller = (Controller) fxmlloader.getController();
        controller.setStage(primaryStage);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(root, 1500, 900);
        controller.setScene(scene);
        controller.setFXMLLoader(fxmlloader);
        controller.setMain(this);
        primaryStage.setScene(scene);
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
