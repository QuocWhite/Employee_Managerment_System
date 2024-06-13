package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBBox;
import javafx.scene.layout.BorderPane;

public class Controller {
    @FXML
    private TableView<ObservableList<String>> table;
    
    private ObservableMap<String, String> department = FXCollections.observableHashMap();

    
    @FXML
    private ComboBox<String> departmentCbx;
    
    @FXML
    void initialize() {
    	dataDepartment();
    	filterEmployee();
    }
    
    
    @FXML
    private void Filter(MouseEvent event) {
    	filterEmployee();
    }


   @FXML
   private void handleButtonClick (AcctionEvent event)throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/Employeemanagement.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node)) event.getSource()).getScene().getWindow();
            window.setTitle("Employee Management System");
            window.setScene(scene);
            window.show();

    }


    
    void dataDepartment() {
   	 try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-L8HCM9F\\HIEN1:1433; database=manage_employee;user=sa;password=123;encrypt=true;trustServerCertificate=true;intergratedSecurity=true;loginTimeout=30;" )) {
            String query = "SELECT * FROM Department";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String departmentId = resultSet.getString("department_id");
                String departmentName = resultSet.getString("name");

                departmentCbx.getItems().add(departmentName);
                department.put(departmentName, departmentId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
   }
    
    @FXML
    void filterEmployee() {
        String selectedDepartmentName = departmentCbx.getValue();
        String selectedDepartmentId = department.get(selectedDepartmentName);
        call(selectedDepartmentId);
    }

    void call(String departmentId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-L8HCM9F\\HIEN1:1433; database=manage_employee;user=sa;password=123;encrypt=true;trustServerCertificate=true;intergratedSecurity=true;loginTimeout=30;" )) {
            String query;
            if (departmentId != null && !departmentId.isEmpty()) {
                query = "SELECT * FROM Employee WHERE department_id = '" + departmentId + "'";
            } else {
                query = "SELECT * FROM Employee";
            }
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

    
            table.getColumns().clear();

            table.getItems().clear();

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int index = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(param -> {
                    ObservableList<String> row = param.getValue();
                    return row == null || row.size() <= index ? null : new SimpleStringProperty(row.get(index));
                });
                table.getColumns().add(column);
            }

            // Fetch data
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                table.getItems().add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
