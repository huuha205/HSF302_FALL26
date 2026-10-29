package fe.de201028.ch08javafx.b2;

import fe.de201028.ch08javafx.common.AlertUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginLayoutApp extends Application {

    private static final Map<String, String> ACCOUNTS = Map.of(
            "admin", "Admin@123",
            "student", "Student@123");

    private static final Border DEBUG_BORDER = new Border(new BorderStroke(
            Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(1)));

    private Scene scene;
    private String rememberedUser = "";
    private final List<Region> containers = new ArrayList<>();
    private boolean debugBorderEnabled = false;

    @Override
    public void start(Stage stage) {
        scene = new Scene(buildLoginView(), 680, 480);
        stage.setScene(scene);
        stage.setTitle("B2 - Layout & Form đăng nhập");
        stage.setMinWidth(480);
        stage.setMinHeight(400);
        stage.show();
    }

    // TODO 1: Khung chính — BorderPane 3 vùng top / center / bottom
    private Parent buildLoginView() {
        containers.clear();
        BorderPane root = new BorderPane();
        root.setTop(buildHeader("v1.0"));
        root.setCenter(buildCenterLogin());
        root.setBottom(buildFooter());
        applyDebugBorder();
        return track(root);
    }

    // TODO 2: Header — HBox: logo, tiêu đề, SPACER giãn, version bên phải
    private Node buildHeader(String rightText) {
        Label logo = new Label("HSF");
        logo.setStyle("-fx-background-color: #f27024; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 4 8;");
        Label title = new Label("Hệ thống quản lý sinh viên");
        title.setStyle("-fx-font-size: 16; -fx-text-fill: white;");
        Label version = new Label(rightText);
        version.setStyle("-fx-text-fill: #cfd8dc;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // spacer chiếm hết chỗ trống → đẩy version sang phải

        HBox header = new HBox(10, logo, title, spacer, version);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 16, 10, 16));
        header.setStyle("-fx-background-color: #263238;");
        return track(header);
    }

    // TODO 3: Center — GridPane (form) → VBox (card) → StackPane (căn giữa)
    private Node buildCenterLogin() {
        Label lblTitle = new Label("ĐĂNG NHẬP");
        lblTitle.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        TextField tfUser = new TextField(rememberedUser);
        tfUser.setPromptText("Username");
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");

        CheckBox chkRemember = new CheckBox("Ghi nhớ đăng nhập");
        chkRemember.setSelected(!rememberedUser.isEmpty());
        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #d93025;");

        Button btnLogin = new Button("Đăng nhập");
        btnLogin.setDefaultButton(true);
        Button btnExit = new Button("Thoát");
        btnExit.setCancelButton(true);

        HBox.setHgrow(btnLogin, Priority.ALWAYS);
        HBox.setHgrow(btnExit, Priority.ALWAYS);
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnExit.setMaxWidth(Double.MAX_VALUE);
        HBox btnBox = new HBox(10, btnLogin, btnExit);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Username:"), tfUser);
        grid.addRow(1, new Label("Password:"), pfPass);
        grid.add(chkRemember, 1, 2);

        VBox card = new VBox(15, lblTitle, grid, lblError, btnBox);
        card.setMaxWidth(360);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        btnLogin.setOnAction(e -> {
            String u = tfUser.getText().trim();
            String p = pfPass.getText();
            if (ACCOUNTS.containsKey(u) && ACCOUNTS.get(u).equals(p)) {
                rememberedUser = chkRemember.isSelected() ? u : "";
                scene.setRoot(buildHomeView(u));
            } else {
                lblError.setText("Sai tài khoản hoặc mật khẩu!");
            }
        });

        btnExit.setOnAction(e -> {
            if (AlertUtil.confirm("Bạn có chắc muốn thoát?")) {
                Platform.exit();
            }
        });

        tfUser.textProperty().addListener((obs, o, n) -> lblError.setText(""));
        pfPass.textProperty().addListener((obs, o, n) -> lblError.setText(""));

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(20));
        return track(center);
    }

    // TODO 4: Footer — HBox: CheckBox debug viền layout, spacer, copyright
    private Node buildFooter() {
        CheckBox chkDebug = new CheckBox("Hiện viền layout (debug)");
        chkDebug.setSelected(debugBorderEnabled);
        chkDebug.setOnAction(e -> {
            debugBorderEnabled = chkDebug.isSelected();
            applyDebugBorder();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label copy = new Label("© 2026 HSF302");

        HBox footer = new HBox(10, chkDebug, spacer, copy);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(8, 16, 8, 16));
        footer.setStyle("-fx-background-color: #eceff1;");
        return track(footer);
    }

    // TODO 5: Màn hình 2 — Trang chủ: header dùng lại, left menu có vSpacer, center lời chào
    private Parent buildHomeView(String username) {
        containers.clear();
        BorderPane root = new BorderPane();
        root.setTop(buildHeader("User: " + username));

        Button btnDash = new Button("Dashboard");
        Button btnStu = new Button("Sinh viên");
        Button btnCourse = new Button("Môn học");
        Button btnLogout = new Button("Đăng xuất");

        btnDash.setMaxWidth(Double.MAX_VALUE);
        btnStu.setMaxWidth(Double.MAX_VALUE);
        btnCourse.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setMaxWidth(Double.MAX_VALUE);

        Region vSpacer = new Region();
        VBox.setVgrow(vSpacer, Priority.ALWAYS); // đẩy nút Đăng xuất xuống đáy

        VBox left = new VBox(10, btnDash, btnStu, btnCourse, vSpacer, btnLogout);
        left.setPadding(new Insets(12));
        left.setPrefWidth(160);
        left.setStyle("-fx-background-color: #f5f5f5;");

        btnLogout.setOnAction(e -> scene.setRoot(buildLoginView()));

        Label welcome = new Label("Xin chào, " + username + "!");
        welcome.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");
        StackPane center = new StackPane(welcome);

        root.setLeft(track(left));
        root.setCenter(track(center));
        root.setBottom(buildFooter());

        applyDebugBorder();
        return track(root);
    }

    private <T extends Region> T track(T container) {
        containers.add(container);
        return container;
    }

    private void applyDebugBorder() {
        for (Region r : containers) {
            r.setBorder(debugBorderEnabled ? DEBUG_BORDER : Border.EMPTY);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
