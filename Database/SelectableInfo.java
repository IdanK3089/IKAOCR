package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectableInfo {

    private final ObservableList<String> infoList = FXCollections.observableArrayList() ;

    private String selectedInfo ;

    public ObservableList<String> getInfoList() {
        return infoList ;
    }

    public String getSelectedInfo() {
        return selectedInfo ;
    }

    public void setSelectedInfo(String info) {
        selectedInfo = info ;
    }

}
