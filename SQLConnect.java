import java.sql.*;
import java.util.Scanner;
public class SQLConnect {
	public static String password =  																																																																															"SimEx1598";                                                                
	public static int currentUserID;
	public static boolean patient = true;
	public static ResultSetMetaData rsmd;
	@SuppressWarnings("resource")
	public static Scanner kin = new Scanner(System.in);

	public static void main(String[] args) {



		/*
		String sql	="CREATE TABLE userAll "
                + "(UID INT NOT NULL,"	
                + "NAME VARCHAR(45) NOT NULL,"
                + "DOB DATE NOT NULL,"
                + "EMAIL VARCHAR(45) NOT NULL,"
                + "PRIMARY KEY (UID))";
		// Prepare Statement
		String insert="insert into users (id, username) values(36,'Ola')";
		String updateRecord="update users  set username= 'Sam2' where id>1";
		String updateRecord1="update users  set password= username where id<100";
		String selectSt="Select * from users where id<100";
		PreparedStatement updateUserName = null;
		PreparedStatement updateTable=null;
		PreparedStatement updateTable1=null;
		 */

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String insert = "";//insert into users (userid, pass, privilege) values (666, 'pass', 'Admin')";
			//@SuppressWarnings("unused")
			int inserted = 0;//statement.executeUpdate(insert);

			/*
			insert = "insert into users (userid, pass, privilege) values (1, 'pass', 'Patient')";
			inserted = statement.executeUpdate(insert);
			insert = "insert into Patient (patientid) values (1)";
			inserted = statement.executeUpdate(insert);
			insert = "insert into users (userid, pass, privilege) values (667, 'pass', 'Admin')";
			inserted = statement.executeUpdate(insert);
			insert = "insert into users (userid, pass, privilege) values (668, 'pass', 'Admin')";
			inserted = statement.executeUpdate(insert);
			//

			insert = "insert into employee (empid) values (666)";
			inserted = statement.executeUpdate(insert);
			insert = "insert into employee (empid) values (667)";
			inserted = statement.executeUpdate(insert);
			insert = "insert into employee (empid) values (668)";
			inserted = statement.executeUpdate(insert);

			insert = "insert into bill values (1, 1, 10, 5, 15, 1, 'go fuck yourself')";
			inserted = statement.executeUpdate(insert);
			 */
			System.out.println("Connected to PostgreSQL database!");

			System.out.println("Login as 1. Patient or 2. Employee? ");
			int login = Integer.parseInt(kin.nextLine());

			System.out.println("Please input userID:");
			int userID = Integer.parseInt(kin.nextLine());
			System.out.println('\n'+"Please input password:");
			String pass = kin.nextLine();

			String userPrivilegeQuery = "Select privilege from users where userid="+userID+" and pass='"+pass+"'";
			ResultSet resultSet = statement.executeQuery(userPrivilegeQuery);
			// Fetch the results
			//while (resultSet.next()) {
			//	Array z = resultSet.getArray(")
			//}
			while (!resultSet.next()) {
				System.out.println("Sorry wrong Username or Password");
				System.out.println("Please input username:");
				userID = Integer.parseInt(kin.nextLine());
				System.out.println('\n'+"Please input password:");
				pass = kin.nextLine();

				userPrivilegeQuery = "Select privilege from users where userid='"+userID+"' and pass='"+pass+"'";
				resultSet = statement.executeQuery(userPrivilegeQuery);
			}
			String userPrivilege = resultSet.getString("privilege");
			currentUserID = userID;
			if (login == 1) {
				userPrivilege = "Patient";
				patient = true;
			} else patient = false;
			Boolean working = true;
			do {
				if (userPrivilege.equalsIgnoreCase("Patient")) {
					String out = "Patients can \n";
					out += '\t' + "0. Exit\n";
					out += '\t' + "1. View Orders\n";
					out += '\t' + "2. View Bills\n";
					out += '\t' + "3. View Record\n";
					System.out.println(out+="Please enter 1 or 2 or 3");
					int choice = Integer.parseInt(kin.nextLine());
					switch(choice) {
					case 0:
						working = false;
						break;
					case 1: 
						viewOrders();
						break;
					case 2:
						viewBills();
						break;
					case 3:
						viewRecords();
						break;
					}
				} else if (userPrivilege.equalsIgnoreCase("Medical Staff")) {
					String out = "Staff can \n";
					out += '\t' + "0. Exit\n";
					out += '\t' + "1. View Patient Record\n";
					out += '\t' + "2. Update Patient Record\n";
					out += '\t' + "3. Create an Order\n";
					out += '\t' + "4. View Calendar\n";
					out += '\t' + "5. Schedule Appointment\n";

					System.out.println(out+="Please enter 1, 2, 3, or 4");
					int choice = Integer.parseInt(kin.nextLine());
					switch(choice) {
					case 0:
						working = false;
						break;
					case 1: //View and Update Patient Records
						viewRecords();
						break;
					case 2: //View and Update Patient Records
						updateRecord();
						break;
					case 3: //Create an order
						createOrder();
						break;
					case 4: //View Calendar
						viewCalendar();
						break;
					case 5: //Schedule Appointment
						schedule();
						break;
					}
				} else if (userPrivilege.equalsIgnoreCase("Scheduler")) {
					String out = "Schedulers can \n";
					out += '\t' + "0. Exit\n";
					out += '\t' + "1. View Orders\n";
					out += '\t' + "2. View Bills\n";
					out += '\t' + "3. View Calendar";
					out += '\t' + "4. Schedule Appointment";
					System.out.println(out+="Please enter 1, 2, 3, or 4");
					int choice = Integer.parseInt(kin.nextLine());
					switch(choice) {
					case 0:
						working = false;
						break;
					case 1: 
						viewOrders();
						break;
					case 2:
						viewBills();
						break;
					case 3:
						viewCalendar();
						break;
					case 4: 
						schedule();
						break;
					}
				} else if(userPrivilege.equalsIgnoreCase("Admin")) {
					String out = "";
					out +="Admins can"+'\n';
					out += '\t' + "0. Exit\n";
					out += '\t' + "1. Schedule an appointment" + '\n';
					out += '\t' + "2. Create new patient" + '\n';
					out += '\t' + "3. Create a new user account" + '\n';
					out += '\t' + "4. Access the business reporting" + '\n';
					System.out.println(out+="Please enter 1,2,3, or 4");
					int choice = Integer.parseInt(kin.nextLine());
					switch(choice) {
					case 0:
						working = false;
						break;
					case 1: //Schedul
						schedule();
						break;
					case 2: //Create Patient
						createPatient();
						break;
					case 3: //Create New User
						createEmployee();
						break;
					case 4: //Business Reporting
						busReporting();
						break;
					}

				} 
			}while(working);
			/*
            connection.setAutoCommit(false) ;  
    		Statement stmt = connection.createStatement();
    		//stmt.executeUpdate(insert);
    		updateUserName=connection.prepareStatement(insert);
    		updateTable=connection.prepareStatement(updateRecord );
    		updateTable1=connection.prepareStatement(updateRecord1 );
    		updateTable.executeUpdate();
    		updateTable1.executeUpdate();

            System.out.println("Reading car records...");
            System.out.printf("%-30.30s  %-30.30s%n", "Model", "Price");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cars");
            while (resultSet.next()) {
                System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("model"), resultSet.getString("price"));
            }

            ResultSet rset = stmt.executeQuery(selectSt);
    		// Fetch the results
    		while (rset.next())
    		{
    			System.out.println( rset.getString(2)+" "+rset.getString(3));
    		}
			 */

			
			statement.close();
			connection.close();
		} /*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        }*/ catch (SQLException e) {
        	System.out.println("Connection failure.");
        	e.printStackTrace();
        }
	}
	@SuppressWarnings("resource")
	public static void schedule()
	{

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();
			System.out.println('\n'+"Please input Time:");
			int time = Integer.parseInt(kin.nextLine());
			System.out.println('\n'+"Please input day:");
			int day = Integer.parseInt(kin.nextLine());    				
			System.out.println('\n'+"Please input month:");
			int month = Integer.parseInt(kin.nextLine());
			System.out.println('\n'+"Please input year:");
			int year = Integer.parseInt(kin.nextLine());
			System.out.println('\n'+"Please input doctorID:"); //we don't check to see if staff id is valid
			int staffID = Integer.parseInt(kin.nextLine());

			String takenQuery = "Select assignedstaffid from calendar where time="+time+" and day="+day+" and month="+month+" and year="+year+" and assignedstaffid="+staffID;
			ResultSet takenResultSet = statement.executeQuery(takenQuery);					

			while (takenResultSet.next()) {
				System.out.println("Date Taken, try again!");
				System.out.println('\n'+"Please input Time:");
				time = Integer.parseInt(kin.nextLine());
				System.out.println('\n'+"Please input day:");
				day = Integer.parseInt(kin.nextLine());   				
				System.out.println('\n'+"Please input month:");
				month = Integer.parseInt(kin.nextLine());
				System.out.println('\n'+"Please input year:");
				year = Integer.parseInt(kin.nextLine());
				System.out.println('\n'+"Please input doctorID:"); //we don't check to see if staff id is valid
				staffID = Integer.parseInt(kin.nextLine());

				takenQuery = "Select assignedstaffid from calendar where time="+time+" and day="+day+" and month="+month+" and year="+year+" and assignedstaffid="+staffID;
				takenResultSet = statement.executeQuery(takenQuery);
			}
			System.out.println('\n'+"Please input your ID");
			int schedulerid = Integer.parseInt(kin.nextLine()); //does not yet check if it is a valid scheduler id
			System.out.println('\n'+"Please input Patient ID");
			int patientid = Integer.parseInt(kin.nextLine());
			String insert = "insert into Calendar values("+time+", "+day+", "+month+", "+year+", "+patientid+", "+schedulerid+", "+staffID+")";
			int inserted = statement.executeUpdate(insert);
			if (inserted == 0)
				System.out.println("Failed to update");
			else
				System.out.println("Updated!");

			
			statement.close();
			connection.close();
		} /*catch (ClassNotFoundException e) {
	            System.out.println("PostgreSQL JDBC driver not found.");
	            e.printStackTrace();
			        }*/ catch (SQLException e) {
			        	System.out.println("Connection failure.");
			        	e.printStackTrace();
			        }
		//break;
	}

	public static void createPatient()
	{

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String idQu = "Select max(userid) from users";
			ResultSet rsid = statement.executeQuery(idQu);
			rsid.next();
			int patientid = rsid.getInt(1) + 1;

			//Patient Information
			System.out.println('\n'+"First Name:");
			String fname = kin.nextLine();
			System.out.println('\n'+"Last Name:");
			String lname = kin.nextLine();  				
			System.out.println('\n'+"Address:");
			String address = kin.nextLine();
			//

			//User Information
			System.out.println('\n'+"Password:");
			String pass = kin.nextLine();
			//

			String takenQuery = "Select firstname, lastname, address from patient where firstname='"+fname+"' and lastname='"+lname+"' and address='"+address+"'";
			ResultSet takenResultSet = statement.executeQuery(takenQuery);					

			if (!takenResultSet.next()) {
				String insert = "insert into patient values('"+lname+"', '"+fname+"', '"+address+"', "+patientid+")";
				String userinsert = "insert into users (userid, pass, privilege) values("+patientid+", '"+pass+"', 'Patient')";
				int userinserted = statement.executeUpdate(userinsert);
				int inserted = statement.executeUpdate(insert);
				if (userinserted == 0 || inserted == 0)
					System.out.println("Failed to update");
				else
					System.out.println("Updated!");
				System.out.println("Your ID: " + patientid);
			}

			
			statement.close();
			connection.close();
		} /*catch (ClassNotFoundException e) {
	            System.out.println("PostgreSQL JDBC driver not found.");
	            e.printStackTrace();
			        }*/ catch (SQLException e) {
			        	System.out.println("Connection failure.");
			        	e.printStackTrace();
			        }		
	}

	public static void createEmployee()
	{

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String idQu = "Select max(userid) from users";
			ResultSet rsid = statement.executeQuery(idQu);
			rsid.next();
			int empid = rsid.getInt(1) + 1;

			//Employee Information
			System.out.println('\n'+"First Name:");
			String fname = kin.nextLine();
			System.out.println('\n'+"Last Name:");
			String lname = kin.nextLine();  				
			System.out.println('\n'+"Job Type (Admin, Medical Staff, Scheduler): ");
			String job = kin.nextLine();
			//

			//User Information
			System.out.println('\n'+"Password:");
			String pass = kin.nextLine();
			//

			String takenQuery = "Select firstname, lastname, jobType from Employee where firstname='"+fname+"' and lastname='"+lname+"' and jobType='"+job+"'";
			ResultSet takenResultSet = statement.executeQuery(takenQuery);					

			if (!takenResultSet.next()) {
				String inserts = "";
				String insert = "insert into Employee values('"+fname+"', '"+lname+"', "+empid+", '"+job+"')";
				String userinsert = "insert into users (userid, pass, privilege) values("+empid+", '"+pass+"', '"+job+"')";
				//if job == Admin insert into Admin
				while(true) {
					if (job.equalsIgnoreCase("Admin")) {
						inserts = "insert into admins values ("+empid+")";
						break;}
					//else if job == Staff insert into Staff
					else if (job.equalsIgnoreCase("Medical staff")) {
						inserts = "insert into staff values ("+empid+")";
						break;}
					//else if job == Scheduler insert into Scheduler
					else if (job.equalsIgnoreCase("scheduler")) {
						inserts = "insert into scheduler values ("+empid+")";
						break;}
					//else print incorrect job title
					else {
						System.out.println("Incorrect Job Title, Please Try Again.");
						System.out.println('\n'+"Job Type (Admin, Staff, Scheduler): ");
						job = kin.nextLine();
					}
				}
				int userinserted = statement.executeUpdate(userinsert);
				int empinserted = statement.executeUpdate(insert);
				int assid = statement.executeUpdate(inserts);
				if (userinserted == 0 || empinserted == 0 || assid == 0)
					System.out.println("Failed to update");
				else
					System.out.println("Updated!");
				System.out.println("Your ID: " + empid);
			}

			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}		
	}
	public static void printQuery(String query) {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();
			ResultSet temp = statement.executeQuery(query);
			rsmd = temp.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (temp.next()) {
				System.out.println();
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  "+ '\t');
					String columnValue = temp.getString(i);
					System.out.print(columnValue );
				}
			}
			statement.close();
			connection.close();
		} 
		catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
		System.out.println();

	}
	public static ResultSet callQuery(String query) {
		ResultSet output = null;
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();
			ResultSet temp = statement.executeQuery(query);
			rsmd = temp.getMetaData();
			output = temp;
			statement.close();
			connection.close();
		} 
		catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
		return output;
	}
	public static void printResult(ResultSet results) {
		try {
			//ResultSetMetaData rsmd = results.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (results.next()) {
				System.out.println();
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  "+ '\t');
					String columnValue = results.getString(i);
					System.out.print(columnValue );
				}
			}
		}catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
		System.out.println();

	}
	public static void viewBills() {
		String out = "";
		out+="Viewing Bills";
		System.out.println(out);
		String query = "";
		if (patient) {
			query = "SELECT * FROM bill WHERE patientid = '"+currentUserID+"'";
		} else {
			boolean temp = true;
			while(temp) {
				System.out.println("Search by 1. Patient ID or 2. Bill ID? ");
				int searchBy = Integer.parseInt(kin.nextLine());

				System.out.println("Please enter ID, use -1 to return all records:");
				int recordID = Integer.parseInt(kin.nextLine());

				if (recordID == -1) {
					query =  "SELECT * FROM bill";
					temp = false;
				} else if (recordID > -1) {
					if(searchBy == 1) {
						query =  "SELECT * FROM bill WHERE patientid = '"+recordID+"'";
						temp = false;
					}else if (searchBy == 2) {
						query =  "SELECT * FROM bill WHERE billid = '"+recordID+"'";
						temp = false;
					}
				}
			}
		}
		printQuery(query);

		
	}

	public static void viewOrders()
	{
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String out = "";
			out+="Viewing Orders";
			System.out.println(out);
			String query = "";
			if (patient) {
				query = "SELECT * FROM orders WHERE patientid = '"+currentUserID+"'";
			} else {
				boolean temp = true;
				while(temp) {
					System.out.println("Search by 1. Patient ID or 2. Order ID? ");
					int searchBy = Integer.parseInt(kin.nextLine());

					System.out.println("Please enterID, use -1 to return all records:");
					int recordID = Integer.parseInt(kin.nextLine());

					if(searchBy == 1) {
						if (recordID > -1) {
							query =  "SELECT * FROM orders WHERE patientid = '"+recordID+"'";
							temp = false;
						}else if (recordID == -1) {
							query =  "SELECT * FROM orders";
							temp = false;
						}
					}else if (searchBy == 2) {
						if (recordID > -1) {
							query =  "SELECT * FROM orders WHERE orderid = '"+recordID+"'";
							temp = false;
						}else if (recordID == -1) {
							query =  "SELECT * FROM orders";
							temp = false;
						}
					}
				}
			}
			ResultSet orders = statement.executeQuery(query);
			ResultSetMetaData rsmd = orders.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (orders.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  "+ '\t');
					String columnValue = orders.getString(i);
					System.out.print(columnValue );
				}
				System.out.println("");
			}

			
			statement.close();
			connection.close();
		} 
		/*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
		        }*/ catch (SQLException e) {
		        	System.out.println("Connection failure.");
		        	e.printStackTrace();
		        }
	}
	public static void busReporting()
	{

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String out = "";
			out+="Business Report";
			System.out.println(out);

			String query = "SELECT * FROM bill";
			ResultSet billing = statement.executeQuery(query);
			ResultSetMetaData rsmd = billing.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			int visitChargeTotal = 0;
			int diagnosticTotal = 0;
			int totalRevenue = 0;
			while (billing.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  "+ '\t');
					String columnValue = billing.getString(i);
					System.out.print(columnValue );
				}
				System.out.println("");
				visitChargeTotal += billing.getInt("visitCharge");
				diagnosticTotal += billing.getInt("diagnosticCharge");
				totalRevenue += billing.getInt("totalCost");
			}

			System.out.println("\nVisist Charge Total: " +visitChargeTotal);
			System.out.println("Diagnostic Total: " + diagnosticTotal);
			System.out.println("Total Revenue: " + totalRevenue);




			
			statement.close();
			connection.close();
		} /*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
		        }*/ catch (SQLException e) {
		        	System.out.println("Connection failure.");
		        	e.printStackTrace();
		        }
	}

	public static void createOrder() {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			Statement statement = connection.createStatement();

			String idQu = "Select max(orderid) from orders";
			ResultSet rsid = statement.executeQuery(idQu);
			rsid.next();
			int orderid = rsid.getInt(1) + 1;

			//Order Information
			System.out.println('\n'+"Patient ID: ");
			int patientid = Integer.parseInt(kin.nextLine());
			System.out.println('\n'+"Staff ID:");
			int staffid = Integer.parseInt(kin.nextLine());  				
			System.out.println('\n'+"Diagnostic ID:");
			int diagnosticid = Integer.parseInt(kin.nextLine()); 
			System.out.println('\n'+"Results:");
			String resul = kin.nextLine();
			//

			String insert = "insert into orders values("+orderid+", "+patientid+", "+staffid+", "+diagnosticid+", '"+resul+"')";
			int inserted = statement.executeUpdate(insert);

			if (inserted == 0)
				System.out.println("Failed to update");
			else
				System.out.println("Updated!");
			System.out.println("Your Order ID: " + orderid);

			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}	
	}
	public static void viewCalendar() {
		String out = "";
		out+="Viewing Calendar";
		System.out.println(out);
		String query = "";
		Boolean temp = true;

		while(temp){
			System.out.println("Search by 1. Date or 2. Assigned Staff? ");
			int searchBy = Integer.parseInt(kin.nextLine());
			switch(searchBy) {
			case 1: //Date
				System.out.println("Please enter Day, or -1 for all days");
				int day = Integer.parseInt(kin.nextLine());
				System.out.println("Please enter Month, or -1 for all months");
				int month = Integer.parseInt(kin.nextLine());
				System.out.println("Please enter Year, or -1 for all years");
				int year = Integer.parseInt(kin.nextLine());
				query =  "SELECT * FROM calendar";
				if(-1 != day||-1 != month||-1 != year){
					query +=" WHERE ";
					if(day != -1){
						query +="day = '"+day+"' ";
						if(-1 != month||-1 != year){
							query +=" AND ";
						}
					}
					if(month != -1){
						query +="month = '"+month+"' ";
						if(-1 != year){
							query +=" AND ";
						}
					}
					if(year != -1){
						query +="year = '"+year+"' ";
					}

				}
				break;
			case 2: //Staff
				System.out.println("Please enter Staff ID");
				int staffID = Integer.parseInt(kin.nextLine());
				query =  "SELECT * FROM calendar WHERE assignedstaffid = '"+staffID+"'";
				temp = false;
				break;
			}		

		}
		printQuery(query);
		
	}
	public static void viewRecords() {
		String out = "";
		out+="Viewing Records";
		System.out.println(out);
		String query = "";
		System.out.println("Please enter Patient ID");
		int searchBy = Integer.parseInt(kin.nextLine());
		query =  "SELECT * FROM records WHERE patientid = '"+searchBy+"'";
		printQuery(query);
		
	}
	public static void updateRecord() {
		String out = "";
		out+="Updating Records";
		System.out.println(out);
		String query = "";
		System.out.println("Please enter Patient ID");
		int searchBy = Integer.parseInt(kin.nextLine());
		System.out.println("Please enter Patient info");
		String input = kin.nextLine();

		query ="update records set patientinfo= '"+input+"' where patientid = "+searchBy;

		updateQuery(query);
		System.out.println("Query updated");
		
	}
	public static void  updateQuery(String query) {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", password)) {
			String updateRecord=query;
			PreparedStatement updateTable=null;
			updateTable=connection.prepareStatement(updateRecord );
			updateTable.executeUpdate();
			connection.close();
		} 
		catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
	}
}













