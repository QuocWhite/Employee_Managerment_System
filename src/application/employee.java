package application;

public class employee {
	
	private String employee;
	private String firstname;
	private String department;
	private String lastname;
	private String position;
	
	public employee (String Employee, String Firstname, String Lastname, String Position, String Department) {
		this.employee = Employee;
		this.firstname = Firstname;
		this.lastname = Lastname;
		this.position = Position;
		this.department = Department;
	}
	
	public String getEmployee() {
		return employee;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getTastname() {
		return lastname;
	}
	public String getPosition() {
		return position;
	}
	public String getDepartment() {
		return department;
	}
}
