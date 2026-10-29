package fe.de201028.ch08javafx.b3;

import fe.de201028.ch08javafx.common.AlertUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class RegisterApp extends Application {

    private static final String STRONG_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$";
    private static final String[] LEVEL_TEXT = {"Rất yếu", "Yếu", "Trung bình", "Khá", "Mạnh"};
    private static final String[] LEVEL_COLOR = {"#d93025", "#d93025", "#f29900", "#1a73e8", "#188038"};

    private Scene scene;
    private Parent registerView;

    // Fields Form
    private final TextField tfFullName = new TextField();
    private final TextField tfEmail = new TextField();
    private final TextField tfUser = new TextField();
    private final PasswordField pfPass = new PasswordField();
    private final PasswordField pfConfirm = new PasswordField();
    private final ToggleGroup tgGender = new ToggleGroup();
    private final RadioButton rbMale = new RadioButton("Nam");
    private final RadioButton rbFemale = new RadioButton("Nữ");
    private final RadioButton rbOther = new RadioButton("Khác");
    private final DatePicker dpDob = new DatePicker();
    private final ComboBox<String> cbMajor = new ComboBox<>();
    private final CheckBox chkMusic = new CheckBox("Âm nhạc");
    private final CheckBox chkSport = new CheckBox("Thể thao");
    private final CheckBox chkRead = new CheckBox("Đọc sách");
    private final CheckBox chkTravel = new CheckBox("Du lịch");
    private final CheckBox chkAgree = new CheckBox("Tôi đồng ý với các điều khoản dịch vụ");
    private final Button btnRegister = new Button("Đăng ký");
    private final Button btnReset = new Button("Làm lại");

    // Inline errors & Strength indicator
    private final Label errName = errLabel(), errEmail = errLabel(), errUser = errLabel();
    private final Label errPass = errLabel(), errConfirm = errLabel(), errGender = errLabel();
    private final Label errDob = errLabel(), errMajor = errLabel();
    private final ProgressBar pbStrength = new ProgressBar(0);
    private final Label lblStrength = new Label();

    private static Label errLabel() {
        Label l = new Label();
        l.setStyle("-fx-text-fill: #d93025; -fx-font-size: 11;");
        return l;
    }

    @Override
    public void start(Stage stage) {
        setupControls();
        registerView = buildRegisterView();
        scene = new Scene(registerView, 560, 680);
        stage.setScene(scene);
        stage.setTitle("B3 - Form Đăng ký tài khoản");
        stage.show();
    }

    private void setupControls() {
        rbMale.setToggleGroup(tgGender);
        rbFemale.setToggleGroup(tgGender);
        rbOther.setToggleGroup(tgGender);
        cbMajor.getItems().addAll("Kỹ thuật phần mềm", "An toàn thông tin", "Trí tuệ nhân tạo", "Thiết kế đồ họa");
        cbMajor.setMaxWidth(Double.MAX_VALUE);

        pbStrength.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setDefaultButton(true);

        // Binding: Chưa tích checkbox -> nút Đăng ký bị vô hiệu hóa
        btnRegister.disableProperty().bind(chkAgree.selectedProperty().not());

        // Password strength meter
        pfPass.textProperty().addListener((obs, o, n) -> {
            int score = calculatePasswordScore(n);
            pbStrength.setProgress(score / 4.0);
            lblStrength.setText(LEVEL_TEXT[score]);
            lblStrength.setStyle("-fx-text-fill: " + LEVEL_COLOR[score] + "; -fx-font-weight: bold;");
        });

        // Event
        btnRegister.setOnAction(e -> handleRegister());
        btnReset.setOnAction(e -> resetForm());
    }

    private Parent buildRegisterView() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));

        int r = 0;
        grid.addRow(r++, new Label("Họ và tên *:"), new VBox(2, tfFullName, errName));
        grid.addRow(r++, new Label("Email *:"), new VBox(2, tfEmail, errEmail));
        grid.addRow(r++, new Label("Username *:"), new VBox(2, tfUser, errUser));
        grid.addRow(r++, new Label("Mật khẩu *:"), new VBox(2, pfPass, pbStrength, lblStrength, errPass));
        grid.addRow(r++, new Label("Xác nhận MK *:"), new VBox(2, pfConfirm, errConfirm));
        grid.addRow(r++, new Label("Giới tính *:"), new VBox(2, new HBox(15, rbMale, rbFemale, rbOther), errGender));
        grid.addRow(r++, new Label("Ngày sinh *:"), new VBox(2, dpDob, errDob));
        grid.addRow(r++, new Label("Ngành học *:"), new VBox(2, cbMajor, errMajor));
        grid.addRow(r++, new Label("Sở thích:"), new FlowPane(10, 10, chkMusic, chkSport, chkRead, chkTravel));
        grid.add(chkAgree, 1, r++);

        Hyperlink hlLogin = new Hyperlink("Đã có tài khoản? Đăng nhập ngay");
        hlLogin.setOnAction(e -> scene.setRoot(buildLoginView("")));

        HBox btnBox = new HBox(10, btnRegister, btnReset, hlLogin);
        btnBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(btnBox, 1, r++);

        Label title = new Label("ĐĂNG KÝ TÀI KHOẢN");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1a73e8;");

        VBox box = new VBox(15, title, grid);
        box.setPadding(new Insets(10));
        return new ScrollPane(box);
    }

    private void handleRegister() {
        boolean valid = true;
        String name = tfFullName.getText().trim();
        if (name.length() < 3 || !name.matches("^[\\p{L} ]+$")) {
            errName.setText("Họ tên từ 3-50 ký tự, chỉ chứa chữ cái");
            valid = false;
        } else errName.setText("");

        String email = tfEmail.getText().trim();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errEmail.setText("Email không hợp lệ");
            valid = false;
        } else errEmail.setText("");

        String username = tfUser.getText().trim().toLowerCase();
        if (!username.matches("^[a-z0-9_]{3,20}$")) {
            errUser.setText("Username 3-20 ký tự (chữ thường, số, _)");
            valid = false;
        } else if (UserStore.exists(username)) {
            errUser.setText("Username đã được sử dụng!");
            valid = false;
        } else errUser.setText("");

        String pass = pfPass.getText();
        if (!pass.matches(STRONG_PASSWORD)) {
            errPass.setText("MK tối thiểu 8 ký tự, gồm chữ hoa, thường, số, ký tự đặc biệt");
            valid = false;
        } else errPass.setText("");

        if (!pfConfirm.getText().equals(pass)) {
            errConfirm.setText("Mật khẩu xác nhận không trùng khớp");
            valid = false;
        } else errConfirm.setText("");

        if (tgGender.getSelectedToggle() == null) {
            errGender.setText("Vui lòng chọn giới tính");
            valid = false;
        } else errGender.setText("");

        LocalDate dob = dpDob.getValue();
        if (dob == null || dob.isAfter(LocalDate.now()) || Period.between(dob, LocalDate.now()).getYears() < 16) {
            errDob.setText("Ngày sinh không hợp lệ (phải đủ 16 tuổi)");
            valid = false;
        } else errDob.setText("");

        if (cbMajor.getValue() == null) {
            errMajor.setText("Vui lòng chọn ngành học");
            valid = false;
        } else errMajor.setText("");

        if (!valid) return;

        List<String> hobbies = new ArrayList<>();
        if (chkMusic.isSelected()) hobbies.add("Âm nhạc");
        if (chkSport.isSelected()) hobbies.add("Thể thao");
        if (chkRead.isSelected()) hobbies.add("Đọc sách");
        if (chkTravel.isSelected()) hobbies.add("Du lịch");

        RadioButton selGender = (RadioButton) tgGender.getSelectedToggle();
        User newUser = new User(name, email, username, pass, selGender.getText(), dob, cbMajor.getValue(), hobbies);
        UserStore.add(newUser);

        AlertUtil.info("Đăng ký thành công tài khoản: " + username);
        scene.setRoot(buildLoginView(username));
    }

    private Parent buildLoginView(String defaultUser) {
        TextField tfLoginUser = new TextField(defaultUser);
        PasswordField pfLoginPass = new PasswordField();
        Button btnDoLogin = new Button("Đăng nhập");
        Hyperlink hlBack = new Hyperlink("Quay lại Đăng ký");

        btnDoLogin.setOnAction(e -> {
            var uOpt = UserStore.login(tfLoginUser.getText(), pfLoginPass.getText());
            if (uOpt.isPresent()) {
                AlertUtil.info("Xin chào: " + uOpt.get().fullName());
            } else {
                AlertUtil.error("Sai username hoặc password!");
            }
        });
        hlBack.setOnAction(e -> scene.setRoot(registerView));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Username:"), tfLoginUser);
        form.addRow(1, new Label("Password:"), pfLoginPass);
        form.addRow(2, btnDoLogin, hlBack);

        VBox box = new VBox(20, new Label("ĐĂNG NHẬP HỆ THỐNG"), form);
        box.setPadding(new Insets(30));
        return box;
    }

    private void resetForm() {
        tfFullName.clear(); tfEmail.clear(); tfUser.clear();
        pfPass.clear(); pfConfirm.clear();
        tgGender.selectToggle(null);
        dpDob.setValue(null);
        cbMajor.setValue(null);
        chkMusic.setSelected(false); chkSport.setSelected(false);
        chkRead.setSelected(false); chkTravel.setSelected(false);
        chkAgree.setSelected(false);
    }

    private int calculatePasswordScore(String pass) {
        if (pass == null || pass.isEmpty()) return 0;
        int score = 0;
        if (pass.length() >= 8) score++;
        if (pass.matches(".*[A-Z].*") && pass.matches(".*[a-z].*")) score++;
        if (pass.matches(".*\\d.*")) score++;
        if (pass.matches(".*[^\\w\\s].*")) score++;
        return score;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
