package process;

import java.util.*;

import model.CustomerInfo;

import java.io.*;
import utility.*;

public class CustomerManager {
	//Define Customer File Name;
	private static final String custfile = "customer.txt";
	//Define a Customer Info ArrayList
	private ArrayList<CustomerInfo> customers = new ArrayList<CustomerInfo>();
	
	public void viewAllCustomer() {
		//User Interface
		System.out.println("====================== Customer List ========================");
		System.out.printf("%-5s| %-20s| %-20s| %-12s%n","No.","Username","Name","ContactNo.");
		System.out.println("-------------------------------------------------------------");
		
		try {
		// Get all Customer Info from customer file 
			File file = new File("customer.txt");
			Scanner sc = new Scanner(file);
			
			int count = 0;
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.isEmpty()) {
					continue;
				}
				else {
					String[] userlst = line.split(",");
					
					if (userlst.length == 5) {
						CustomerInfo c = new CustomerInfo(
								userlst[0], //role
								userlst[1], // username
		                        userlst[2], // password
		                        userlst[3], // name
		                        userlst[4]  // contactNo
		                    );
						count++;
						//Display all customer Info
						System.out.printf("%-5s| %-20s| %-20s| %-12s%n",String.valueOf(count),c.getUsername(),c.getName(),c.getContactNo());
					}
				}
			}
			
			sc.close();
			//If customer file no record
			if (count == 0) {
				//Display no record message.
				System.out.println("No customer found in the file.");
			}
		} catch(FileNotFoundException error) {
			//Display error message If file not exist.
			System.out.println("File not found: " + "customer.txt");
		}
	}
	
	//Remove Customer Method
	public boolean removeCustomer(String filename,String usernamedeleted) {
		try {
			ArrayList<String> lines = new ArrayList<String>();
			Scanner sc = new Scanner(new File(filename));
			boolean removed = false;
				
			while(sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
			sc.close();
				
			//Read Customer File and Find the customer name to delete.
			for(int i=0; i< lines.size(); i++) {
				String[] parts = lines.get(i).split(",");
				if(parts[1].equalsIgnoreCase(usernamedeleted)) {
					lines.remove(i);
					removed = true;
					break;
				}
			}
				
			Collections.sort(lines);
			//Rewrite the Customer File For Sorting	
			PrintWriter writefile = new PrintWriter(new FileWriter(filename));
			for(String line: lines) {
				writefile.println(line);
			}
			writefile.close();
				
			return removed;
		}catch(Exception error) {
			//Removed User Error
			System.out.println("Error occured. Please try again." + error.getMessage());
			return false;
		}
	}
	
	//Read Customer Detail and save into a list for future use
	public void loadCustomers()
	{
		customers.clear();
		try(BufferedReader br = new BufferedReader(new FileReader(custfile)))
		{
			String line;
			while((line=br.readLine())!=null)
			{
				String[] parts = line.split(",");
				if (parts.length == 5)
				{
					customers.add(new CustomerInfo(parts[0],parts[1],parts[2],parts[3],parts[4]));
					
				}
			}
		}catch (IOException e)
		{
			System.out.println("Error reading customers: "+ e.getMessage());
		}
	}
	
	//Save customer info into customer text file
	public void saveCustomers()
	{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(custfile)))
		{
			for (CustomerInfo c : customers)
			{
				bw.write(c.toFileString());
				bw.newLine();
			}
		}catch (IOException e)
		{
			System.out.println("Error saving customers: " + e.getMessage());
		}
	}
	
	//Update customer account password with passing customer username and new account password
	public void updatePassword(String username, String newPassword)
	{
		loadCustomers();
		for (CustomerInfo c : customers)
		{
			if(c.getUsername().equals(username))
			{
				c.setPassword(newPassword);
				saveCustomers();
				System.out.println("Password updated for " + username);
				return;
			}
		}
		System.out.println("Username not found");
	}
	
	//Update customer contact number with passing customer username and new customer contact number
	public void updateContact(String username,String newContact)
	{
		loadCustomers();
		for (CustomerInfo c : customers)
		{
			if (c.getUsername().equals(username))
			{
				c.setContactNum(newContact);
				saveCustomers();
				System.out.println("Contact number updated for" + username);
				return;
			}
		}
		System.out.println("Username not found.");
	}
}