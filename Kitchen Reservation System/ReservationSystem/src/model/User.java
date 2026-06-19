package model;

public class User {
	private String role;
	private String username,name;
	private String password,contactNo;
	
	//User Constructor
	public User(String role, String username, String password,String name,String contactNo) {
		 this.role = role;
	       this.username = username;
	       this.password = password;
	       this.name = name;
	       this.contactNo = contactNo;
	}
	
	//Accessor
	public String getRole() { 
		return role;
	}
    public String getUsername() { 
    	return username; 
    }
    public String getPassword() { 
    	return password; 
    }
    public String getName() {
    	return name;
    }
    public String getContactNo() {
    	return contactNo;
    }
    
    //Mutator
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public void setContactNum(String contactNo)
	{
		this.contactNo = contactNo;
	}
    
    @Override
    //Return a String represent to user object.
    public String toString() {
    	return role + "," + username + "," + password + "," + name + "," + contactNo;
    }
}
