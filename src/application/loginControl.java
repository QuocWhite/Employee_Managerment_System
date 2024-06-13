package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class loginControl {
	@FXML
	private TextField txtUser;
	@FXML
	private TextField txtPass;
	
	public void initialize() {
		
//		txtUser.setText("Your Username here");
//		txtPass.setText("Input pass here");
	}
	@FXML
	private void LoginClick (MouseEvent event) throws IOException {
		
		String user = txtUser.getText();
		String pass = txtPass.getText();
		
		Alert alert = new Alert(AlertType.INFORMATION,"",ButtonType.OK);
		alert.setTitle("Thong bao");
		
		if(user.equals("dangkhoa") && (pass.equals("deptrai"))) {
			alert.setContentText("Welcome " + user);

			Parent root = FXMLLoader.load(getClass().getResource("/ui/dashboard.fxml"));
			Scene scene = new Scene(root);
			
			Stage primaryStage = new Stage();
			
			primaryStage.setTitle("Employee Management System");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		else
		alert.setContentText("Wrong Username or Password");
	
	alert.showAndWait();
	}
	@FXML
	 private void CancelClick(MouseEvent event) {
		
	    Stage stage = (Stage) txtUser.getScene().getWindow();
	    stage.close();
		        
	}
}
