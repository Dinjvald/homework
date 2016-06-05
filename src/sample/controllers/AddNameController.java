package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.objects.Person;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class AddNameController {

    private Person person;

    @FXML private TextField nameField;
    @FXML private Button btnCancelName;
    @FXML private Button btnSubmitName;

    public void setPerson(Person person) {
        if (person == null) return;
        this.person = person;
        nameField.setText(person.getName());
    }

    public Person getPerson() {
        return person;
    }

    @FXML
    private void actionAddName(ActionEvent actionEvent) {
        //System.out.println(person);
        person.setName(nameField.getText());
        actionClose(actionEvent);
    }

    @FXML
    private void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
