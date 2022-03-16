package sample;

import javafx.scene.control.ComboBox;

public class TableBean {

    String name;

    ComboBox<String> testType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComboBox<String> getTestType() {
        return testType;
    }

    public void setTestType(ComboBox<String> testType) {
        this.testType = testType;
    }
}
