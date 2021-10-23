module com.yurwar.thread.war.threadwar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.yurwar.thread.war.threadwar to javafx.fxml;
    exports com.yurwar.thread.war.threadwar;
}