module fe.de201028.ch08javafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens fe.de201028.ch08javafx to javafx.fxml;
    opens fe.de201028.ch08javafx.b4 to javafx.fxml;

    exports fe.de201028.ch08javafx;
    exports fe.de201028.ch08javafx.common;
    exports fe.de201028.ch08javafx.b1;
    exports fe.de201028.ch08javafx.b2;
    exports fe.de201028.ch08javafx.b3;
    exports fe.de201028.ch08javafx.b4;
}
