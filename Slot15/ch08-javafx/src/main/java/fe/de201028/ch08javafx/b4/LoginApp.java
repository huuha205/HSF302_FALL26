package fe.de201028.ch08javafx.b4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 440, 320));
        stage.setTitle("B4 - Form đăng nhập FXML");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
