package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.collections.CollectionPerson;
import sample.collections.CollectionService;
import sample.objects.Person;
import sample.objects.Service;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public static final int PERSON_REPRESENTS_ALL_INDEX = 1;

    private Stage mainStage;

    @FXML
    private javafx.scene.control.ListView<Person> namesList;
    @FXML
    private Button btnAddName;
    @FXML
    private TableView tableService;
    @FXML
    private TableColumn colService;
    @FXML
    private TableColumn colPrice;
    @FXML
    private Label lblPersonTotal;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblAverage;
    @FXML
    private TextArea txtareaTransactions;


    private Stage addPersonStage;
    private Stage addServiceStage;
    private Parent fxmlAdd;
    private Parent fxmlService;
    private FXMLLoader fxmlLoaderPerson = new FXMLLoader();
    private FXMLLoader fxmlLoaderService = new FXMLLoader();
    private AddNameController addNameController;
    private AddServiceController addServiceController;
    private CollectionPerson collectionPerson = new CollectionPerson();
    private CollectionService collectionService = new CollectionService();
    private StringBuilder resultString = new StringBuilder();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colService.setCellValueFactory(new PropertyValueFactory<Service, String>("serviceName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Service, String>("price"));
        initLoader();
        initListeners();
        collectionPerson.add(new Person("ALL"));
        namesList.setItems(collectionPerson.getPersonList());
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void initLoader() {
        try {
            fxmlLoaderPerson.setLocation(getClass().getResource("../fxml/AddName.fxml"));
            fxmlAdd = fxmlLoaderPerson.load();
            addNameController = fxmlLoaderPerson.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fxmlLoaderService.setLocation(getClass().getResource("../fxml/AddService.fxml"));
            fxmlService = fxmlLoaderService.load();
            addServiceController = fxmlLoaderService.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        collectionPerson.getPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> c) {
                addServiceController.setList(collectionPerson.getPersonList());
                addServiceController.updateChoiseBox();
                txtareaTransactions.clear();
                displayTotalAndAverageExpenses();
            }
        });

        namesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
            @Override
            public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
                if (newValue == null) {
                    fillTableWithData(oldValue);
                    countPersonTotalExpenses(oldValue);
                } else {
                    fillTableWithData(newValue);
                    countPersonTotalExpenses(newValue);
                }
            }
        });

        collectionService.getServiceList().addListener(new ListChangeListener<Service>() {
            @Override
            public void onChanged(Change<? extends Service> c) {
                displayTotalAndAverageExpenses();
            }
        });
    }

    private void displayTotalAndAverageExpenses() {
        double allExpenses = countAllExpenses();
        lblTotal.setText(roundDoubleTo2Dec(allExpenses));
        double averageExpenses = countAverageExpenses(allExpenses);
        lblAverage.setText(roundDoubleTo2Dec(averageExpenses));
    }

    private void showNameWindow() {

        if (addPersonStage == null) {
            addPersonStage = new Stage();
            addPersonStage.setTitle("Add a name");
            addPersonStage.setScene(new Scene(fxmlAdd));
            addPersonStage.initModality(Modality.WINDOW_MODAL);
            addPersonStage.initOwner(mainStage);
        }
        addPersonStage.showAndWait();
    }

    private void showServiceWindow() {
        if (addServiceStage == null) {
            addServiceStage = new Stage();
            addServiceStage.setTitle("Add service");
            addServiceStage.setScene(new Scene(fxmlService));
            addServiceStage.initModality(Modality.WINDOW_MODAL);
            addServiceStage.initOwner(mainStage);
        }
        addServiceStage.showAndWait();
    }

    @FXML
    private void actionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) return;

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnAddName":
                addNameController.setPerson(new Person());
                showNameWindow();
                Person person = addNameController.getPerson();
                if (!person.getName().equals("")) collectionPerson.add(person);
                namesList.setItems(collectionPerson.getPersonList());
                break;
            case "btnAddService":
                addServiceController.setService(new Service());
                showServiceWindow();
                Service service = addServiceController.getService();
                if (!service.getServiceName().equals("")) collectionService.add(service);
                namesList.getSelectionModel().clearSelection();
                namesList.getSelectionModel().select(service.getPerson());
                break;
            case "btnCalculate":
                levelExpenses();
        }
    }

    private void fillTableWithData(Person person) {
        ObservableList<Service> list = FXCollections.observableArrayList();
        if (person.getName().equals("ALL")) {
            tableService.setItems(collectionService.getServiceList());
            return;
        }

        for (Service service : collectionService.getServiceList()) {
            if (service.getPerson().equals(person)) {
                list.add(service);
            }
        }
        tableService.setItems(list);
    }

    private void countPersonTotalExpenses(Person person) {
        if (person.getName().equals("ALL")) {
            lblPersonTotal.setText("Total: " + doubleToString(countAllExpenses()));
            return;
        }
        ObservableList<Service> list = collectionService.getServiceList();
        double sum = 0;
        for (Service service : list) {
            if (service.getPerson().equals(person)) {
                sum += stringToDouble(service.getPrice());
            }
        }
        person.setTotalExpenses(sum);
        lblPersonTotal.setText(person.getName() + ": " + doubleToString(sum));
    }

    private double countAllExpenses() {
        ObservableList<Service> list = collectionService.getServiceList();
        double sum = 0;
        for (Service service : list) {
            sum += stringToDouble(service.getPrice());
        }
        return sum;
    }

    private double countAverageExpenses(double total) {
        int count = collectionPerson.getPersonCount() - PERSON_REPRESENTS_ALL_INDEX;
        if (count == 0) return 0.00;
        return total / count;
    }

    private void levelExpenses() {
        ObservableList<Person> personList = collectionPerson.getPersonList();
        double total = countAllExpenses();
        if (!canProceedFurther(total, personList)) return;
        double average = countAverageExpenses(total);

        txtareaTransactions.clear();
        evaluateCreditorsAndDebtors(personList, average);
        HashMap<String, Double> debtors = getDebtors(personList);
        HashMap<String, Double> creditors = getCreditors(personList);
        giveResult(debtors, creditors);

    }

    private void giveResult(HashMap<String, Double> debtors, HashMap<String, Double> creditors) {
        for (Map.Entry<String, Double> entryDebtors : debtors.entrySet()) {
            for (Map.Entry<String, Double> entryCreditors : creditors.entrySet()) {

                String creditorKey = entryCreditors.getKey();
                double creditorValue = entryCreditors.getValue();
                String debtorKey = entryDebtors.getKey();
                double debtorValue = entryDebtors.getValue();

                if (debtorValue <= creditorValue && debtorValue != 0) {
                    resultString
                            .append(debtorKey)
                            .append(" --> ")
                            .append(creditorKey)
                            .append(" ")
                            .append(roundDoubleTo2Dec(debtorValue))
                            .append("\n");
                    debtors.put(debtorKey, 0.00);
                    creditors.put(creditorKey, (creditorValue - debtorValue));
                }
            }
        }

        for (Map.Entry<String, Double> entryCreditors : creditors.entrySet()) {
            for (Map.Entry<String, Double> entryDebtors : debtors.entrySet()) {

                String creditorKey = entryCreditors.getKey();
                double creditorValue = entryCreditors.getValue();
                String debtorKey = entryDebtors.getKey();
                double debtorValue = entryDebtors.getValue();

                if (creditorValue <= debtorValue && creditorValue != 0) {
                    resultString
                            .append(debtorKey)
                            .append(" --> ")
                            .append(creditorKey)
                            .append(" ")
                            .append(roundDoubleTo2Dec(creditorValue))
                            .append("\n");
                    creditors.put(creditorKey, 0.00);
                    debtors.put(debtorKey, (debtorValue - creditorValue));
                }
            }
        }

        if (resultString.toString().equals("")) {
            txtareaTransactions.setText("All payed equally.");
        } else {
            txtareaTransactions.setText(resultString.toString());
        }
        resultString.setLength(0);
    }

    private HashMap<String, Double> getDebtors(ObservableList<Person> personList) {
        HashMap<String, Double> debtors = new HashMap<>();
        for (int x = PERSON_REPRESENTS_ALL_INDEX; x < personList.size(); x++) {
            Person person = personList.get(x);
            if (!person.isOverAveragePrice()) {
                debtors.put(person.getName(), person.getDifference());
            }
        }
        return debtors;
    }

    private HashMap<String, Double> getCreditors(ObservableList<Person> personList) {
        HashMap<String, Double> creditors = new HashMap<>();
        for (int x = PERSON_REPRESENTS_ALL_INDEX; x < personList.size(); x++) {
            Person person = personList.get(x);
            if (person.isOverAveragePrice()) {
                creditors.put(person.getName(), person.getDifference());
            }
        }
        return creditors;
    }

    private boolean canProceedFurther(double total, ObservableList<Person> list) {
        if (total == 0) {
            alertInformation("Alert", "Nothing to calculate");
            return false;
        }
        if (list.size() <= 2) {
            alertInformation("Alert", "There is only one person entered.");
            return false;
        }
        return true;
    }

    private void evaluateCreditorsAndDebtors(ObservableList<Person> personList, double average) {
        evaluateIfPersonExpensesAreOverAverage(personList, average);
        calculateTheDifference(personList, average);
    }

    private void evaluateIfPersonExpensesAreOverAverage(ObservableList<Person> personList, double average) {
        for (int x = PERSON_REPRESENTS_ALL_INDEX; x < personList.size(); x++) {
            Person person = personList.get(x);
            if (person.getTotalExpenses() > average) {
                person.setIsOverAveragePrice(true);
            } else {
                person.setIsOverAveragePrice(false);
            }
        }
    }

    private void calculateTheDifference(ObservableList<Person> personList, double average) {
        for (int x = PERSON_REPRESENTS_ALL_INDEX; x < personList.size(); x++) {
            Person person = personList.get(x);
            if (person.isOverAveragePrice()) {
                person.setDifference(person.getTotalExpenses() - average);
            } else {
                person.setDifference(average - person.getTotalExpenses());
            }
        }
    }

    private void alertInformation(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

    private double stringToDouble(String string) {
        return Double.parseDouble(string);
    }

    private String doubleToString(double x) {
        return String.valueOf(x);
    }

    private String roundDoubleTo2Dec(double number) {
        DecimalFormat f = new DecimalFormat("0.00");
        return f.format(number);
    }
}
