package sample.collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.objects.Service;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class CollectionService {

    private ObservableList<Service> serviceList = FXCollections.observableArrayList();

    public void add(Service service) {
        serviceList.add(service);
    }

    public ObservableList<Service> getServiceList() {
        return serviceList;
    }
}
