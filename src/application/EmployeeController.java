package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeController {

    @FXML
    private TableView<ObservableList<String>> tableView;
    
    private ObservableMap<String, String> departmentMap = FXCollections.observableHashMap();

    
    @FXML
    private ComboBox<String> departmentComboBox;
    
    @FXML
    private ComboBox<String> genderComboBox;
    
    @FXML
    private TextField addEmployee_employee_id;
    
    @FXML
    private TextField addEmployee_salary;

    @FXML
    private TextField addEmployee_frst_name;

    @FXML
    private TextField addEmployee_last_name;

    private String imagePath = "images.png";

    @FXML
    private TextField txtSearch;
    
    @FXML
    private DatePicker addEmployee_dob;

    @FXML
    private TextField addEmployee_phone;

    @FXML
    private TextField addEmployee_address;

    @FXML
    private TextField addEmployee_position;

    @FXML
    private ImageView addEmployee_image;

    @FXML
    private DatePicker addEmployee_date_member;
 
    
    private Connection connection;
    
	
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
        	String url = "jdbc:sqlserver://DESKTOP-L8HCM9F\\HIEN1:1433; database=manage_employee;user=sa;password=123;encrypt=true;trustServerCertificate=true;intergratedSecurity=true;loginTimeout=30;" ;
        	connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }


    @FXML
    void initialize() {
    	callData();
    	clickTable();
    	dataDepartment();
    	selectGender();
    	handleComboBoxSelection();
    }
    
    void clickTable() {
    	tableView.setOnMouseClicked(event -> {
    	    if (event.getClickCount() == 1) { 
    	        if (tableView.getSelectionModel().getSelectedItem() != null) { 
    	            ObservableList<String> rowData = tableView.getSelectionModel().getSelectedItem();
    	            addEmployee_employee_id.setText(rowData.get(0));
    	            addEmployee_frst_name.setText(rowData.get(1)); 
    	            addEmployee_last_name.setText(rowData.get(2)); 
    	            genderComboBox.setValue(rowData.get(3));
    	            addEmployee_phone.setText(rowData.get(5));
    	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    	            LocalDate dateMember = LocalDate.parse(rowData.get(9), formatter); 
    	            addEmployee_date_member.setValue(dateMember); 
    	            addEmployee_address.setText(rowData.get(6));
    	            addEmployee_position.setText(rowData.get(7));
    	            LocalDate dateOfBirth = LocalDate.parse(rowData.get(4), formatter); 
    	            addEmployee_dob.setValue(dateOfBirth); 
    	            addEmployee_salary.setText(rowData.get(11));
    	            String path = rowData.get(8); 
    	            if (path != null && !path.isEmpty()) {
    	                File imageFile = new File(path);
    	                if (imageFile.exists()) {
    	                    Image image = new Image(imageFile.toURI().toString());
    	                    addEmployee_image.setImage(image);
    	                } else {
    	                   
    	                    addEmployee_image.setImage(null);
    	                }
    	            } else {
    	                addEmployee_image.setImage(null);
    	            
    	        }
    	    
    	            String departmentId = rowData.get(10);
    	            String departmentName = ""; 
    	            for (Map.Entry<String, String> entry : departmentMap.entrySet()) {
    	                if (entry.getValue().equals(departmentId)) {
    	                    departmentName = entry.getKey();
    	                    break;
    	                }
    	            }

    	           
    	            departmentComboBox.setValue(departmentName);
    	           
    	           
    	        }
    	    }
    	});

    }
    
    void callData() {
    	String query;
    	String name = txtSearch.getText();
    	if (name.isEmpty()) {
            query = "SELECT Employee.*, Salary.salary FROM Employee JOIN Salary ON Employee.employee_id = Salary.employee_id";
        } else {
            query = "SELECT Employee.*, Salary.salary FROM Employee JOIN Salary ON Employee.employee_id = Salary.employee_id WHERE first_name LIKE ? OR last_name LIKE ?";
        }
        try (Connection conn = getConnection()) {
            PreparedStatement statement = conn.prepareStatement(query);
            
            if (!name.isEmpty()) {
                statement.setString(1, "%" + name + "%");
                statement.setString(2, "%" + name + "%");
            }

            ResultSet resultSet = statement.executeQuery();

          
            tableView.getColumns().clear();
            tableView.getItems().clear();

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int index = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(param -> {
                    ObservableList<String> row = param.getValue();
                    return row == null || row.size() <= index ? null : new SimpleStringProperty(row.get(index));
                });
                tableView.getColumns().add(column);
            }

         
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                tableView.getItems().add(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    @FXML
    void handleComboBoxSelection() {
        String selectedDepartmentName = departmentComboBox.getValue();
        String selectedDepartmentId = departmentMap.get(selectedDepartmentName);
        
    }
    
    void dataDepartment() {
    	 try (Connection conn = getConnection()) {
             String query = "SELECT * FROM Department";
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query);

             while (resultSet.next()) {
                 String departmentId = resultSet.getString("department_id");
                 String departmentName = resultSet.getString("name");

                 departmentComboBox.getItems().add(departmentName);
                 departmentMap.put(departmentName, departmentId);
             }

         } catch (SQLException ex) {
             ex.printStackTrace();
         }
    }
 
    void selectGender() {
    	genderComboBox.getItems().addAll("Male", "Female");

       
    
    }
    
    @FXML
    private void searchEmloyee(MouseEvent event) {
    	callData();
    }
    
    
    @FXML
    private void AddEmloyee(MouseEvent event) {
    	
    	addEmployee();
    }
    
    @FXML
    private void UpdateEmloyee(MouseEvent event) {
    	updateEmployee();
    }
    
    @FXML
    private void clearField(MouseEvent event) {
    	resetemployee();
    }
    
    @FXML
    private void DeleteEmloyee(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Employee");
        alert.setContentText("Are you sure you want to delete this employee?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            
            deleteEmployee();
        } else {
           
            alert.close();
        }
    }

    
    public void addEmployee() {
        String employeeSql = "INSERT INTO employee (first_name, last_name, gender, date_of_birth, phone_number, address, position, image, date_member, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String salarySql = "INSERT INTO Salary (employee_id, salary) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            if (addEmployee_frst_name.getText().isEmpty()
                    || addEmployee_last_name.getText().isEmpty()
                    || genderComboBox.getSelectionModel().getSelectedItem() == null
                    || addEmployee_dob.getValue() == null
                    || addEmployee_salary.getText().isEmpty()
                    || addEmployee_phone.getText().isEmpty()
                    || addEmployee_address.getText().isEmpty()
                    || addEmployee_position.getText().isEmpty()
                    || addEmployee_date_member.getValue() == null
                    || departmentComboBox.getSelectionModel().getSelectedItem() == null) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blanks");
                alert.showAndWait();

            } else {
                if (addEmployee_employee_id.getText().isEmpty()) {
                    try {
                        conn.setAutoCommit(false); 

                      
                        PreparedStatement employeeStmt = conn.prepareStatement(employeeSql, Statement.RETURN_GENERATED_KEYS);
                        employeeStmt.setString(1, addEmployee_frst_name.getText());
                        employeeStmt.setString(2, addEmployee_last_name.getText());
                        employeeStmt.setString(3, genderComboBox.getSelectionModel().getSelectedItem());
                        employeeStmt.setString(4, addEmployee_dob.getValue().toString());
                        employeeStmt.setString(5, addEmployee_phone.getText());
                        employeeStmt.setString(6, addEmployee_address.getText());
                        employeeStmt.setString(7, addEmployee_position.getText());
                        employeeStmt.setString(8, imagePath);
                        employeeStmt.setString(9, addEmployee_date_member.getValue().toString());
                        employeeStmt.setString(10, departmentMap.get(departmentComboBox.getValue()));
                        employeeStmt.executeUpdate();

                       
                        ResultSet generatedKeys = employeeStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int employeeId = generatedKeys.getInt(1);

                           
                            PreparedStatement salaryStmt = conn.prepareStatement(salarySql);
                            salaryStmt.setInt(1, employeeId);
                            salaryStmt.setDouble(2, Double.parseDouble(addEmployee_salary.getText())); 
                            salaryStmt.executeUpdate();
                        }

                        conn.commit(); 
                        callData();
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Added!");
                        alert.showAndWait();
                        resetemployee();

                    } catch (SQLException ex) {
                        conn.rollback(); 
                        ex.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employees already exist");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateEmployee() {
        String updateEmployeeSql = "UPDATE employee SET first_name=?, last_name=?, gender=?, date_of_birth=?, phone_number=?, address=?, position=?, image=?, date_member=?, department_id=? WHERE employee_id=?";
        String updateSalarySql = "UPDATE salary SET salary=? WHERE employee_id=?";
        
        try (Connection conn = getConnection()) {
            if (addEmployee_frst_name.getText().isEmpty()
                    || addEmployee_last_name.getText().isEmpty()
                    || genderComboBox.getSelectionModel().getSelectedItem() == null
                    || addEmployee_dob.getValue() == null
                    || addEmployee_phone.getText().isEmpty()
                    || addEmployee_address.getText().isEmpty()
                    || addEmployee_position.getText().isEmpty()
                    || addEmployee_date_member.getValue() == null
                    || departmentComboBox.getSelectionModel().getSelectedItem() == null
                    || addEmployee_salary.getText().isEmpty()) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blanks");
                alert.showAndWait();

            } else {
               if(addEmployee_employee_id.getText().isEmpty()) {
                   Alert alert = new Alert(AlertType.ERROR);
                   alert.setTitle("Error Message");
                   alert.setHeaderText(null);
                   alert.setContentText("The employee does not exist to update");
                   alert.showAndWait();
               } else {
                   conn.setAutoCommit(false); 
                   
                   try (PreparedStatement updateEmployeeStmt = conn.prepareStatement(updateEmployeeSql);
                        PreparedStatement updateSalaryStmt = conn.prepareStatement(updateSalarySql)) {

                     
                       updateEmployeeStmt.setString(1, addEmployee_frst_name.getText());
                       updateEmployeeStmt.setString(2, addEmployee_last_name.getText());
                       updateEmployeeStmt.setString(3, genderComboBox.getSelectionModel().getSelectedItem());
                       updateEmployeeStmt.setString(4, addEmployee_dob.getValue().toString());
                       updateEmployeeStmt.setString(5, addEmployee_phone.getText());
                       updateEmployeeStmt.setString(6, addEmployee_address.getText());
                       updateEmployeeStmt.setString(7, addEmployee_position.getText()); 
                       updateEmployeeStmt.setString(8, imagePath); 
                       updateEmployeeStmt.setString(9, addEmployee_date_member.getValue().toString());
                       updateEmployeeStmt.setString(10, departmentMap.get(departmentComboBox.getValue()));
                       updateEmployeeStmt.setString(11, addEmployee_employee_id.getText()); 
                       updateEmployeeStmt.executeUpdate();

                     
                       updateSalaryStmt.setString(1, addEmployee_salary.getText());
                       updateSalaryStmt.setString(2, addEmployee_employee_id.getText());
                       updateSalaryStmt.executeUpdate();

                       conn.commit(); 
                       
                       Alert alert = new Alert(AlertType.INFORMATION);
                       alert.setTitle("Information Message");
                       alert.setHeaderText(null);
                       alert.setContentText("Successfully Updated!");
                       alert.showAndWait();
                       callData();
                           
                       resetemployee();
                   } catch (SQLException e) {
                       conn.rollback(); 
                       throw e;
                   } finally {
                       conn.setAutoCommit(true); 
                   }
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public void deleteEmployee() {
        String sqlDeleteSalary = "DELETE FROM salary WHERE employee_id = ?";
        String sqlDeleteEmployee = "DELETE FROM employee WHERE employee_id = ?";

        try (Connection conn = getConnection()) {
            if (addEmployee_employee_id.getText().isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("The employee does not exist to delete");
                alert.showAndWait();
            } else {
                conn.setAutoCommit(false); 

                try (PreparedStatement prepareDeleteSalary = conn.prepareStatement(sqlDeleteSalary);
                     PreparedStatement prepareDeleteEmployee = conn.prepareStatement(sqlDeleteEmployee)) {

                    prepareDeleteSalary.setString(1, addEmployee_employee_id.getText());
                    prepareDeleteSalary.executeUpdate();

                    prepareDeleteEmployee.setString(1, addEmployee_employee_id.getText());
                    prepareDeleteEmployee.executeUpdate();

                    conn.commit(); 

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee successfully deleted!");
                    alert.showAndWait();

                    resetemployee();
                    callData();
                } catch (SQLException e) {
                    conn.rollback(); 
                    throw e;
                } finally {
                    conn.setAutoCommit(true); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
       
        }
    }





public void resetemployee() {

    addEmployee_employee_id.setText("");
    addEmployee_frst_name.setText("");
    addEmployee_last_name.setText("");
    genderComboBox.getSelectionModel().clearSelection();
    addEmployee_dob.setValue(null);
    addEmployee_phone.setText("");
    addEmployee_address.setText("");
    addEmployee_position.setText("");
    addEmployee_date_member.setValue(null);
    departmentComboBox.getSelectionModel().clearSelection();
    imagePath = "";
    addEmployee_image.setImage(null);
}

@FXML
private void chooseImage(MouseEvent event) {
	   FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Choose Image File");
	    fileChooser.getExtensionFilters().addAll(
	        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
	    );
	    File selectedFile = fileChooser.showOpenDialog(null);
	    if (selectedFile != null) {
	        File destinationFolder = new File("src/images");
	        if (!destinationFolder.exists()) {
	            destinationFolder.mkdirs(); 
	        }
	        File newFile = new File(destinationFolder.getAbsolutePath() + File.separator + selectedFile.getName());
	        try {
	            Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	       
	         imagePath = newFile.getAbsolutePath();
	        Image image = new Image(newFile.toURI().toString());
	        addEmployee_image.setImage(image);
	    }
      
    }


}
    
  