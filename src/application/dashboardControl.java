package application;

import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class dashboardControl {

	void Connect()
	{
		Connection conn = null;
		try {
			String url = "jdbc:sqlserver://DESKTOP-L8HCM9F\\HIEN1:1433; database=manage_employee;user=sa;password=123;encrypt=true;trustServerCertificate=true;intergratedSecurity=true;loginTimeout=30;" ; 
			conn = DriverManager.getConnection(url);
	
			if (conn != null) {
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out.println("Product version: " + dm.getDatabaseProductVersion());
				  conn.close();
				} 
		}
		catch (SQLException ex) { 
		System.out.println("error connections " + ex);
			//ex.printStackTrace();
		}

	}
	

	@FXML
	TableView tbaView;

	public void initialize() {
		Connect();
		tbaView.setEditable(true);
		tbaView.getColumns().clear();

		TableColumn Employee = new TableColumn("Employee");
		Employee.setCellValueFactory(new PropertyValueFactory<>("Employee"));

		TableColumn Firstname = new TableColumn("Firstname");
		Firstname.setCellValueFactory(new PropertyValueFactory<>("Firstname"));

		TableColumn Lastname = new TableColumn("Lastname");
		Lastname.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
		
		TableColumn Position = new TableColumn("Position");
		Position.setCellValueFactory(new PropertyValueFactory<>("Position"));

		TableColumn Department = new TableColumn("Department");
		Department.setCellValueFactory(new PropertyValueFactory<>("Department"));

		tbaView.getColumns().addAll(Employee, Firstname, Lastname,  Position, Department);

		employee k = new employee("01", "John", "Doe", "Manager", "HR");
		tbaView.getItems().add(k);

		k = new employee("01", "Jane", "Doe", "Analyst", "Finance");
		tbaView.getItems().add(k);

		k = new employee("02", "Jim", "Beam", "Developer", "IT");
		tbaView.getItems().add(k);

		k = new employee("03", "Jack", "Daniel", "Designer", "Marketing");
		tbaView.getItems().add(k);
		
		k = new employee("04", "Jill", "Valentine", "Sale Executive", "Sales");
		tbaView.getItems().add(k);


		}
	}


