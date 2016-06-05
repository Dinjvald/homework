package sample.objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class Service {

    private SimpleStringProperty serviceName = new SimpleStringProperty("");
    private SimpleStringProperty price = new SimpleStringProperty("");
    private Person person;

    public String getServiceName() {
        return serviceName.get();
    }

    public SimpleStringProperty serviceNameProperty() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName.set(serviceName);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName=" + serviceName +
                ", price=" + price +
                ", person=" + person +
                '}';
    }
}
