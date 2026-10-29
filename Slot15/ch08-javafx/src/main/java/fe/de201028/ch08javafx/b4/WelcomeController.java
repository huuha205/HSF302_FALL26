package fe.de201028.ch08javafx.b4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {
    @FXML private Label lblWelcome;

    public void setUser(String user, String role) {
        lblWelcome.setText("Xin chào %s! Vai trò: %s".formatted(user, role));
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 440, 320));
        stage.setTitle("B4 - Form đăng nhập FXML");
    }
}
