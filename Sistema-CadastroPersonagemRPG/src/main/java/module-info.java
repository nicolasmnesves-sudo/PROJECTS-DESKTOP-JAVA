module nicolasmneves.sistemacadastropersonagemrpg {
    requires javafx.controls;
    requires javafx.fxml;


    opens nicolasmneves.sistemacadastropersonagemrpg to javafx.fxml;
    exports nicolasmneves.sistemacadastropersonagemrpg;
}