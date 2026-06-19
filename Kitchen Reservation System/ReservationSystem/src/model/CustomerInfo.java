package model;

public class CustomerInfo extends User{
	public CustomerInfo(String role, String username, String password, String name, String contactNo){
		super(role,username,password,name,contactNo);
	}
	
	public String toFileString() //Format Customer and Save into File 
	{
		return getRole() + "," + getUsername() + "," + getPassword() + "," + getName() + "," + getContactNo();
	}
	
	@Override
	public String toString() //Show Customer Info with a template.
	{
		return "Username: " + getUsername() + "| Name:" + getName() + "| Contact: " + getContactNo() + "| Password: " + getPassword();
	}
}