package sample.collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.objects.Person;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class CollectionPerson {

    private ObservableList<Person> personList = FXCollections.observableArrayList();

    public void add(Person person) {
        personList.add(person);
    }

    public ObservableList<Person> getPersonList() {
        return personList;
    }

    public int getPersonCount() {
        return personList.size();
    }
}
