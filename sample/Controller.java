package sample;

import Database.MainTableModel;
import Database.TestDB;
import Database.TestModel;
import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.data.HeaderColorStyle;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.github.daytron.simpledialogfx.dialog.DialogType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller implements Initializable {

    String OpenImageFile;

    File reportFile = null;

    TextAlignment Pos;

    TestDB db = new TestDB();


@FXML
    TableColumn BRKClm;



    @FXML
    TableView<MainTableModel> MainTable;

    @FXML
    TextField TestBox;

    @FXML
    TextField PriceBox;

    @FXML
   TableColumn TestRadioClm;

    @FXML
    TableView<TestModel> TestTable;

    @FXML
            TableColumn TestTypeClmT;

    @FXML
    TableColumn TestCostClm;
    @FXML
    TableColumn TotalClm;

    @FXML
    TableColumn<MainTableModel,ComboBox> TestTypeClm;

    @FXML
            TableColumn AmountClm;

    ToggleGroup g = new ToggleGroup();

    ObservableList<MainTableModel> list = FXCollections.observableArrayList();

    String FileName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        setKeyEvents();
      db.CreateDb();
       // BRKClm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

   //     TestCostClm.setStyle( "-fx-alignment: CENTER");

        BRKClm.setCellValueFactory(
                new PropertyValueFactory<MainTableModel,String>("Lot")
        );
        BRKClm.setCellFactory(TextFieldTableCell.forTableColumn());

        BRKClm.setCellFactory(tc -> {
            TableCell<MainTableModel, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(TestTypeClmT.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });


        TotalClm.setCellValueFactory(
                new PropertyValueFactory<MainTableModel,String>("TotalCost")
        );
        TotalClm.setCellFactory(TextFieldTableCell.forTableColumn());

        TotalClm.setCellFactory(tc -> {
            TableCell<MainTableModel, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(TestTypeClmT.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });




        TestTypeClmT.setCellValueFactory(
                new PropertyValueFactory<TestModel,String>("TestName")
        );
        TestTypeClmT.setCellFactory(TextFieldTableCell.forTableColumn());

        TestTypeClmT.setCellFactory(tc -> {
            TableCell<TestModel, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(TestTypeClmT.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });


        TestCostClm.setCellValueFactory(
                new PropertyValueFactory<TestModel,String>("TestPrice")
        );
        TestCostClm.setCellFactory(TextFieldTableCell.forTableColumn());

        TestCostClm.setCellFactory(tc -> {
            TableCell<TestModel, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setTextAlignment(Pos.CENTER);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(TestCostClm.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });



   TestTypeClm.setCellValueFactory(new PropertyValueFactory<MainTableModel,ComboBox>("TestType"));
        AmountClm.setCellValueFactory(new PropertyValueFactory<MainTableModel,ComboBox>("Amount"));

        PopulateTable(TestTable,TestRadioClm,g);
    }

    public void FillCombox(ComboBox box){

      ArrayList<TestModel> M =  db.queryForReports();

        for(TestModel m:M){
            box.getItems().add(m.getTestName());
        }


    }

    @FXML
    public void ClearTbl(){

        MainTable.getItems().clear();

    }

@FXML
public void OpenFile(){

        Stage stage = new Stage();

    List<File> files = null;
    Preferences userPrefs = Preferences.userNodeForPackage(Main.class);

    String lastVisitedDirectory=userPrefs.get("OCR Path","");

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Tensile Data");

    if(!lastVisitedDirectory.equals(""))
        fileChooser.setInitialDirectory(new  File(lastVisitedDirectory));
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Png Image","*.png;*.jpeg"));

    try{

        files = fileChooser.showOpenMultipleDialog(stage);


    }catch (Exception e){

        String userDirectoryString = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new  File(userDirectoryString));
        files = fileChooser.showOpenMultipleDialog(stage);

    }


    lastVisitedDirectory=(files!=null && files.size()>=1)?files.get(0).getParent():System.getProperty("user.home");

    userPrefs.put("OCR Path", lastVisitedDirectory);

    reportFile = files.get(0);

     FileName = reportFile.getName();


}

@FXML
public void AddTest(){



    if(!TestBox.getText().equals("")){

            TestModel model = new TestModel();
            model.setId(db.FindHigestID());
            model.setTestName(TestBox.getText());

            if(PriceBox.getText().equals(""))
                 model.setTestPrice("0.00");
            else
                model.setTestPrice(PriceBox.getText());

             db.insertUser(model);

           //Refrash table here!
            PopulateTable(TestTable,TestRadioClm,g);

        }





}

    public ObservableList<TestModel> ReportToList(ArrayList<TestModel> List){

        ObservableList<TestModel> list = FXCollections.observableArrayList();

        //ToDo
        for(TestModel nd : List)
            list.add(nd);

        return list;


    }

    public void PopulateTable(TableView table,TableColumn clm,ToggleGroup g) {

       table.getItems().clear();

        ObservableList<TestModel> m = ReportToList(db.queryForReports());

        for (TestModel sd : m) {

            addRadioButtonToPrepTable(g,clm);

            table.getItems().add(sd);

        }



    }



    public void FillComboWithNumbers(ComboBox<String> box) {

      ArrayList<Integer> M = new ArrayList<>();

      for(int i=0;i<=30;i++){
          M.add(i);
      }

        for(Integer m:M){
            box.getItems().add(String.valueOf(m));
        }


    }

        private void addRadioButtonToPrepTable(ToggleGroup Group, TableColumn clm) {

        Callback<TableColumn<TestModel, String>, TableCell<TestModel, String>> cellFactory = new Callback<TableColumn<TestModel, String>, TableCell<TestModel, String>>() {

            public TableCell<TestModel, String> call(final TableColumn<TestModel, String> param) {
                final TableCell<TestModel, String> cell = new TableCell<TestModel, String>() {

                    private RadioButton btn = new RadioButton();

                    {

                        btn.setToggleGroup(Group);

                        btn.setOnAction((ActionEvent event) -> {


                            TestModel data = getTableView().getItems().get(getIndex());

                            //Here i choose what to do with the data, move to class variable

                        });
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                            btn.setSelected(false);
                        }
                    }
                };
                return cell;
            }
        };


        TestRadioClm.setCellFactory(cellFactory);


    }


@FXML
public void MakeData(){


    if(reportFile!=null){

        try {

            if(MainTable.getItems().size()!=0){
                Dialog dialog1 = new com.github.daytron.simpledialogfx.dialog.Dialog(
                        DialogType.CONFIRMATION,
                        "יש נתונים בטבלה",
                        "האם להמשיך ולמחוק את הנתונים הקיימים?",
                        "בחר YES כדי להמשיך");

                dialog1.setFontSize(22);
                dialog1.showAndWait();


                if (dialog1.getResponse() == DialogResponse.YES) {

                    StartOCR(reportFile);

                }

            } else
            StartOCR(reportFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }


    }


}

    public void StartOCR(File file) throws TesseractException {


        Tesseract tesseract = new Tesseract(); // JNA Interface Mapping

       // System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/win32-x86" : "lib/win32-x86-64");

        String p = System.getProperty("user.dir") + "/tessdata";
        System.load(Paths.get("").toAbsolutePath()+"\\"+"liblept1780.dll");
         System.load(Paths.get("").toAbsolutePath()+"\\"+"libtesseract410.dll");
        tesseract.setDatapath( System.getProperty("user.dir") + "/tessdata");
        tesseract.setLanguage("eng");
        String text = tesseract.doOCR(file);

        ArrayList<MainTableModel> T = new ArrayList<>();



        String[] g = text.split("\n");

                for(int i=0;i<g.length;i++){

                    if(g[i].contains("BRK")){
                        ComboBox<String> comboBox = new ComboBox<>();
                        ComboBox<String> comboBox1 = new ComboBox<>();
                        FillCombox(comboBox);
                        FillComboWithNumbers(comboBox1);

                        MainTableModel m = new MainTableModel(comboBox,comboBox1);
                comboBox.setPrefWidth(280);
                        comboBox1.setPrefWidth(100);
                m.setTestType(comboBox);
                m.setAmount(comboBox1);
                m.setLot("BRK" + g[i].split("BRK")[1]);
                T.add(m);

            }

        }



        ObservableList<MainTableModel> details = FXCollections.observableArrayList(T);

        MainTable.setItems(details);


    }


    @FXML
    public void CalculateCosts(){

       ArrayList<TestModel> m = db.queryForReports();

        for(MainTableModel T: MainTable.getItems()){

            for(TestModel f : m){

                if(f.getTestName().equals(T.getTestType().getSelectionModel().getSelectedItem())){


                    if((T.getAmount()!=null)&&(f.getTestName()!=null))


                    T.setTotalCost(String.valueOf(Double.parseDouble(f.getTestPrice())*Double.parseDouble(T.getAmount().getSelectionModel().getSelectedItem())));

                }


            }

            MainTable.refresh();
          //  MainTable.getItems().clear();
           // MainTable.setItems(details);



            }


        }




    public void setKeyEvents(){

        TestTable.setOnKeyReleased((KeyEvent t)-> {
            KeyCode key=t.getCode();
            if (key==KeyCode.DELETE){
                TestModel report =  TestTable.getSelectionModel().getSelectedItem();

                   Dialog dialog1 = new com.github.daytron.simpledialogfx.dialog.Dialog(
                        DialogType.CONFIRMATION,
                        "למחוק בדיקה",
                        "האם למחוק בדיקה זו מהרשימה?",
                        "בחר YES כדי למחוק בדיקה זו");

                    dialog1.setFontSize(22);
                    dialog1.showAndWait();


                if (dialog1.getResponse() == DialogResponse.YES) {

                    db.DeleteUser(report.getId());
                       PopulateTable(TestTable,TestRadioClm,g);
                }




            }
        });


    }
@FXML
    public void CreateWordFile(){

     Variables variables = new Variables();
     Docx docx;

     double TotalSum=0;
     int Counter = 1;
         if(FileName!=null){

             String path = Paths.get("").toAbsolutePath().toString() + "\\Template" + ".docx";

             docx  = new Docx(path);
             docx.setVariablePattern(new VariablePattern("#{", "}"));

             for(MainTableModel m: MainTable.getItems()){


                 variables.addTextVariable(new TextVariable("#{BRK-" + Counter + "}", m.getLot()));
                 if(m.getTestType().getSelectionModel().getSelectedItem()!=null)
                 variables.addTextVariable(new TextVariable("#{TestType-" + Counter + "}", m.getTestType().getSelectionModel().getSelectedItem()));
                else
                     variables.addTextVariable(new TextVariable("#{TestType-" + Counter + "}", ""));


                 if(m.getAmount().getSelectionModel().getSelectedItem()!=null)
                 variables.addTextVariable(new TextVariable("#{Count-" + Counter + "}",  m.getAmount().getSelectionModel().getSelectedItem()));
               else
                     variables.addTextVariable(new TextVariable("#{Count-" + Counter + "}",  ""));

                 variables.addTextVariable(new TextVariable("#{TotalPrice-" + Counter + "}", m.getTotalCost()));

                 if( m.getTotalCost()!=null)
                 TotalSum  += Double.parseDouble(m.getTotalCost());

        Counter++;

             }

             variables.addTextVariable(new TextVariable("#{Total}", String.valueOf(TotalSum)));
             docx.fillTemplate(variables);
            try{

                docx.save(Paths.get("").toAbsolutePath().toString() + "\\Files\\" + FileName.replace(".png","") + ".docx");

                Dialog dialog1 = new com.github.daytron.simpledialogfx.dialog.Dialog(
                        DialogType.CONFIRMATION,
                        "הקובץ מוכן",
                        "לפתוח את הקובץ?",
                        "בחר YES כדי לפתוח את הקובץ");

                dialog1.setFontSize(22);
                dialog1.showAndWait();


                if (dialog1.getResponse() == DialogResponse.YES) {

                    Desktop.getDesktop().open(new File(Paths.get("").toAbsolutePath().toString() + "\\" + FileName + ".docx"));

                }










            }catch (Exception E){
                Dialog dialog = new Dialog(
                        DialogType.ERROR,
                        "קובץ בעל שם זהה פתוח, נע לסגור את הקובץ וורד",
                        "נע לסגור את הקובץ וורד");
                dialog.setFontSize(22);
                dialog.setHeaderColorStyle(HeaderColorStyle.OPAQUE_RED);

                dialog.showAndWait();}

            }

         }





    }






