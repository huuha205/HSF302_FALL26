package fe.de201028.ch08javafx.b4;

import fe.de201028.ch08javafx.common.AlertUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class LoginController {

    @FXML private TextField tfUsername, tfPasswordVisible;
    @FXML private PasswordField pfPassword;
    @FXML private CheckBox chkShow;
    @FXML private ComboBox<String> cbRole;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    private static final Map<String, String[]> ACCOUNTS = Map.of(
            "student", new String[]{"Student@123", "Sinh viên"},
            "teacher", new String[]{"Teacher@123", "Giảng viên"});

    @FXML
    public void initialize() {
        // Đồng bộ dữ liệu 2 chiều giữa TextField mật khẩu hiện và PasswordField
        tfPasswordVisible.textProperty().bindBidirectional(pfPassword.textProperty());

        // Hiện/Ẩn mật khẩu bằng visible & managed
        tfPasswordVisible.visibleProperty().bind(chkShow.selectedProperty());
        tfPasswordVisible.managedProperty().bind(chkShow.selectedProperty());
        pfPassword.visibleProperty().bind(chkShow.selectedProperty().not());
        pfPassword.managedProperty().bind(chkShow.selectedProperty().not());

        // Tự disable nút Login nếu để trống username, password hoặc role
        btnLogin.disableProperty().bind(
                tfUsername.textProperty().isEmpty()
                .or(pfPassword.textProperty().isEmpty())
                .or(cbRole.valueProperty().isNull())
        );

        Platform.runLater(tfUsername::requestFocus);
    }

    @FXML
    private void handleLogin(ActionEvent e) {
        String user = tfUsername.getText().trim().toLowerCase();
        String pass = pfPassword.getText();
        String role = cbRole.getValue();

        if (ACCOUNTS.containsKey(user)) {
            String[] acc = ACCOUNTS.get(user);
            if (acc[0].equals(pass) && acc[1].equals(role)) {
                openWelcomeScreen(e, user, role);
                return;
            }
        }
        lblError.setText("Sai tài khoản, mật khẩu hoặc vai trò!");
    }

    private void openWelcomeScreen(ActionEvent event, String user, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome-view.fxml"));
            Parent root = loader.load();

            WelcomeController controller = loader.getController();
            controller.setUser(user, role);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 400, 250));
            stage.setTitle("Chào mừng");
        } catch (IOException ex) {
            AlertUtil.error("Lỗi khi tải màn hình: " + ex.getMessage());
        }
    }

    @FXML
    private void handleForgot(ActionEvent event) {
        AlertUtil.info("Vui lòng liên hệ quản trị viên (admin@fpt.edu.vn) để lấy lại mật khẩu!");
    }
}
