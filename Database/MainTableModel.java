package Database;

import javafx.scene.control.ComboBox;

public class MainTableModel {

String Lot;

ComboBox<String> TestType;

    ComboBox<String> Amount;

    public MainTableModel(ComboBox<String> testType, ComboBox<String> amount) {
        TestType = testType;
        Amount = amount;
    }

    public ComboBox<String> getTestType() {
        return TestType;
    }

    public void setTestType(ComboBox<String> testType) {
        TestType = testType;
    }

    String TotalCost;

    public String getLot() {
        return Lot;
    }

    public void setLot(String lot) {
        Lot = lot;
    }

    public ComboBox<String> getAmount() {
        return Amount;
    }

    public void setAmount(ComboBox<String> amount) {
        Amount = amount;
    }

    public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String totalCost) {
        TotalCost = totalCost;
    }



}
