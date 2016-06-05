package sample.objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dinjvald on 04/06/16.
 */
public class Person {

    private SimpleStringProperty name = new SimpleStringProperty("");
    private boolean isOverAveragePrice;
    private double totalExpenses;
    private double difference;

    public Person() {
    }

    public Person(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public boolean isOverAveragePrice() {
        return isOverAveragePrice;
    }

    public void setIsOverAveragePrice(boolean isOverAveragePrice) {
        this.isOverAveragePrice = isOverAveragePrice;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
