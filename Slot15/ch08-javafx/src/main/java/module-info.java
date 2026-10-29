module fe.de201028.ch08javafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens fe.de201028.ch08javafx to javafx.fxml;
    exports fe.de201028.ch08javafx;
    exports fe.de201028.ch08javafx.common;
    exports fe.de201028.ch08javafx.b1;
}
