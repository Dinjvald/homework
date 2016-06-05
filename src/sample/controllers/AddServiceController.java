package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.objects.Person;
import sample.objects.Service;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class AddServiceController {

    public static final String NUMBERS_AFTER_DECIMAL_POINT = "00";

    @FXML private TextField txtServiceName;
    @FXML private TextField txtPrice;
    @FXML private ChoiceBox chboxChoosePerson;
    private Service service;

    private ObservableList<Person> list;

    public void setList(ObservableList<Person> list) {
        this.list = list;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        if (service == null) return;
        this.service = service;
        txtServiceName.setText(service.getServiceName());
        txtPrice.setText(service.getPrice());
        chboxChoosePerson.getSelectionModel().clearSelection();
    }

    public void updateChoiseBox() {
        ObservableList<Person> menu = FXCollections.observableArrayList();
        for (int x = MainController.PERSON_REPRESENTS_ALL_INDEX; x < list.size(); x++) {
            menu.add(list.get(x));
        }
        chboxChoosePerson.setItems(menu);
    }

    @FXML
    private void actionAddService(ActionEvent actionEvent) {
        service.setServiceName(txtServiceName.getText());
        service.setPrice(txtPrice.getText() + NUMBERS_AFTER_DECIMAL_POINT);
        service.setPerson((Person) chboxChoosePerson.getValue());
        if (!isServiceValid()){
            alertInformation("Alert", "Don't leave blank fiend.\n" + "Use only whole numbers.");
            return;
        }
        actionClose(actionEvent);
    }

    private Person getServicePerson(String name) {
        for (Person person : list) {
            if (person.getName().equals(name)) return person;
        }
        return new Person("Unknown");
    }

    @FXML
    private void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean isServiceValid() {
        service.getServiceName();
        if (service.getServiceName().equals("")) return false;
        try {
            Integer.parseInt(service.getPrice());
        } catch (NumberFormatException ex) {
            return false;
        }
        if (service.getPerson() == null) return false;
        return true;
    }

    private void alertInformation(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

}
