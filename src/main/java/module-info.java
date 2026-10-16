module uqvirutal.edu.co.parcialdosp2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens uqvirutal.edu.co.parcialdosp2 to javafx.fxml;
    opens uqvirutal.edu.co.parcialdosp2.viewController to javafx.fxml;
    opens uqvirutal.edu.co.parcialdosp2.model;
    opens uqvirutal.edu.co.parcialdosp2.controller;

    exports uqvirutal.edu.co.parcialdosp2;
    exports uqvirutal.edu.co.parcialdosp2.model;
    exports uqvirutal.edu.co.parcialdosp2.servicios;
    exports uqvirutal.edu.co.parcialdosp2.controller;
}