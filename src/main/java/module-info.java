module silosgroup.projetosystem_silo {
    requires javafx.controls;
    requires javafx.fxml;


    opens silosgroup.projetosystem_silo to javafx.fxml;
    exports silosgroup.projetosystem_silo;
}