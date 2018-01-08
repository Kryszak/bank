package bsr.project.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final String APPLICATION_TITLE = "Klient";

    private static final String MAIN_LAYOUT_FXML = "/fxml/main.fxml";

    private static final int MIN_HEIGHT = 600;

    private static final int MIN_WIDTH = 800;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_LAYOUT_FXML));

        Parent root = loader.load();

        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);

        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
