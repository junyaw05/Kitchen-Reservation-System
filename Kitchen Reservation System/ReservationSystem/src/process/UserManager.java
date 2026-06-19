package process;

import java.io.*;
import java.util.*;
import model.User;

public class UserManager {
	private ArrayList<User> users;
	private static User currentUsername;
	
	//Accessor
	public static User getCurrentUsername() {
		return currentUsername;
	}
	
	//LogOut Method
	public void logout() {
		currentUsername = null;
		System.out.println("Logout Successful");
	}

    public UserManager() {
        users = new ArrayList<>(); //create list and read the file
        readFiles();
    }

    //register method
    public void register(String role, String username, String password, String name, String contactNo) {
    	if(!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("customer")) {
            System.out.println("Invalid role, please choose admin or customer.");
            return;
        }
    	
        if (findUser(username) != null) { // if have the name(not null)
            System.out.println("Username already exists.");
            return;
        }
        
        if(role.equalsIgnoreCase("customer")) {
        	if(!isValidPhoneNum(contactNo)) {
        		System.out.println("Invalid phone number, must exactly 10 digit only!");
        		return;
        	}
        	name = formatName(name);
        }
        User newUser = new User(role,username, password,name,contactNo); //create object and save inside list that been readed before
        users.add(newUser);
        
        if(role.equalsIgnoreCase("admin")){
        	saveFile("admin.txt",newUser);
        }else {
        	saveFile("customer.txt",newUser);
        }
        System.out.println("User registered successfull.");
    }

    //Login method
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUsername = user;
            	return user; //success and pass back to completeLog
            }
        }
        return null; //fail and print invalid input
    }

    //Validate the user is exist or not
    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
            	//return user if user exist
                return user; 
            }
        }
        return null;
    }

    private void saveFile(String filename,User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename,true))) { //after register save inside file
        	writer.println(user.toString());  //overriden it the format ,call user to store in format
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void readFiles() {
    	readFile("admin.txt");
    	readFile("customer.txt");
    }
    
    private void readFile(String filename) {
        File file = new File(filename); //read user role name and password
        if (!file.exists()) {
        	return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                String[] data = readLine.split(","); // split it out 
                if (data.length == 5) {
                    String role = data[0];
                    String username = data[1];
                    String password = data[2];
                    String name = data[3];
                    String contactNo = data[4];
                    users.add(new User(role, username, password, name, contactNo));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //validate the phone number is valid or not valid
    private boolean isValidPhoneNum(String phoneNum) {
        if (phoneNum == null) {
        	return false;
        }
        String validPhoneNum = phoneNum.replaceAll("[^0-9]", "");
        return validPhoneNum.matches("\\d{10}");
    }
    
    //format customer name in a constant pattern
    private String formatName(String customerName) {
        String name = customerName.replaceAll("[^a-zA-Z\\s]", "").replaceAll("\\s+", " ").trim();
        return name.toUpperCase();
    } 
}