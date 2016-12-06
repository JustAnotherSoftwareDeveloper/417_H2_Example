package Examples.Examples;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

public class InMemoryDBExample {
	/*
	 * This is simply for an example, so I don't mind that I ripped code from a
	 * prior assignment where I had to write a bunch of stuff in pure sql
	 */
	private final String DB_USER;
	private final String DB_PASSWORD;

	public InMemoryDBExample(String usr, String passwd) {
		this.DB_PASSWORD = passwd;
		this.DB_USER = usr;
	}

	public void CreateTables() throws SQLException {
		Connection connection = this.getDBConnection();
		Statement createStuff;
		if (connection == null) {
			return;
		}
		try {
			createStuff = connection.createStatement();

			// Create Tables
			LinkedList<String> table_statements = new LinkedList<String>();
			// Students SQL
			table_statements.add("CREATE TABLE Students(" + "SNUM integer NOT NULL, " + "ssn integer NOT NULL, "
					+ "name varchar(10), " + "gender varchar(1), " + "dob datetime, " + "c_addr varchar(20), "
					+ "c_phone varchar(10), " + "p_addr  varchar(20), " + "p_phone varchar(10), "
					+ "PRIMARY KEY (ssn), " + "UNIQUE (SNUM))");
			// Departments SQL:
			table_statements.add("CREATE TABLE Departments(" + "department_code integer NOT NULL, "
					+ "name varchar(50) NOT NULL, " + "phone varchar(10), " + "college varchar(20), "
					+ "PRIMARY KEY (department_code), " + "UNIQUE(name))");
			// Degrees SQL
			table_statements
					.add("CREATE TABLE Degrees(" + "name VARCHAR(50) NOT NULL, " + "dlevel VARCHAR(5) NOT NULL, "
							+ "department_code integer NOT NULL, " + "PRIMARY KEY (name,dlevel), "
							+ "FOREIGN KEY (department_code) references Departments(department_code))");
			// Courses SQL
			table_statements.add("CREATE TABLE Courses(" + "Cnumber integer NOT NULL, " + "name varchar(50) NOT NULL, "
					+ "description varchar(50), " + "credithours integer, " + "clevel varchar(20), "
					+ "department_code integer NOT NULL, " + "PRIMARY KEY (Cnumber), " + "UNIQUE(name), "
					+ "FOREIGN KEY (department_code) references Departments(department_code))");
			// Register SQL
			table_statements.add("CREATE TABLE Register( " + "SNUM integer NOT NULL, "
					+ "course_number integer NOT NULL, " + "when_class varchar(20), " + "grade integer, "
					+ "PRIMARY KEY (SNUM,course_number), " + "FOREIGN KEY (SNUM) references Students(SNUM), "
					+ "FOREIGN KEY (course_number) references Courses(Cnumber) " + ")");
			// Major SQL
			table_statements.add("CREATE TABLE Major(" + "SNUM integer NOT NULL," + "name varchar(50) NOT NULL,"
					+ "mlevel varchar(5) NOT NULL," + "PRIMARY KEY (SNUM,name,mlevel),"
					+ "FOREIGN KEY (SNUM) references Students(SNUM),"
					+ "FOREIGN KEY (name,mlevel) references Degrees(name,dlevel)" + ")");
			// Minor SQL
			table_statements.add("CREATE TABLE Minor(" + "SNUM integer NOT NULL," + "name varchar(50) NOT NULL,"
					+ "mlevel varchar(5) NOT NULL," + "PRIMARY KEY (SNUM,name,mlevel),"
					+ "FOREIGN KEY (SNUM) references Students(SNUM),"
					+ "FOREIGN KEY (name,mlevel) references Degrees(name,dlevel)" + ")");
			for (Iterator<String> i = table_statements.iterator(); i.hasNext();) {
				createStuff.execute(i.next());
			}
			System.out.println("Done with Creation");
		} catch (SQLException e) {
			System.out.println("Error Creating DB/Tables");
			e.printStackTrace();
		}
	}

	public void insertRecords() {
		Connection conn = this.getDBConnection();
		LinkedList<String> commands = new LinkedList<>();
		try {
			Statement insertion = conn.createStatement();
			// Insert Students
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1001','654651234','Randy','M','2000-12-01','301 E Hall','5152700988','121 Main','7083066321')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1002','123097834','Victor','M','2000-05-06','270 W Hall','5151234578','702 Walnut','7080366333')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1003','978023431','John','M','1999-07-08','201 W Hall','5154132805','888 University','5152012011')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1004','746897816','Seth','M','1998-12-30','199 N Hall','5158891504','21 Green','5154132907')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1005','186932894','Nicole','F','2001-04-01','178 S Hall','5158891155','13 Gray','5157162071')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1006','534218752','Becky','F','2001-05-06','12 N Hall','5157083698','189 Clark','2034367632')");
			commands.add("INSERT INTO Students (SNUM,ssn,name,gender,dob,c_addr,c_phone,p_addr,p_phone) "
					+ "VALUES ('1007','432609519','Kevin','M','2000-08-12','75 E Hall','5157082497','89 National','7182340772')");
			// Insert Departments
			commands.add("INSERT INTO Departments(department_code,name,phone,college) "
					+ "VALUES ('401','Computer Science','5152982801','LAS')");
			commands.add("INSERT INTO Departments(department_code,name,phone,college) "
					+ "VALUES ('402','Mathematics','5152982802','LAS') ");
			commands.add("INSERT INTO Departments(department_code,name,phone,college) "
					+ "VALUES ('403','Chemical Engineering','5152982803','Engineering')");
			commands.add("INSERT INTO Departments(department_code,name,phone,college) "
					+ "VALUES ('404','Landscape Architect','5152982804','Design')");
			// Insert Degreees
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Computer Science','BS', (SELECT department_code from Departments WHERE department_code='401'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Software Engineering','BS', (SELECT department_code from Departments WHERE department_code='401'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Computer Science','MS', (SELECT department_code from Departments WHERE department_code='401'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Applied Mathematics','MS', (SELECT department_code from Departments WHERE department_code='402'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Chemical Engineering','BS', (SELECT department_code from Departments WHERE department_code='403'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Landscape Architect','BS', (SELECT department_code from Departments WHERE department_code='404'))");
			commands.add("INSERT INTO Degrees (name,dlevel,department_code) "
					+ "VALUES ('Computer Science','PhD', (SELECT department_code from Departments WHERE department_code='401'))");
			// Insert Majors
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1001'),(SELECT name from Degrees WHERE name='Computer Science' AND dlevel='BS'),(SELECT dlevel from Degrees WHERE name='Computer Science' AND dlevel='BS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1002'),(SELECT name from Degrees WHERE name='Software Engineering' AND dlevel='BS'),(SELECT dlevel from Degrees WHERE name='Software Engineering' AND dlevel='BS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1003'),(SELECT name from Degrees WHERE name='Chemical Engineering' AND dlevel='BS'),(SELECT dlevel from Degrees WHERE name='Chemical Engineering' AND dlevel='BS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1004'),(SELECT name from Degrees WHERE name='Landscape Architect' AND dlevel='BS'),(SELECT dlevel from Degrees WHERE name='Landscape Architect' AND dlevel='BS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1005'),(SELECT name from Degrees WHERE name='Computer Science' AND dlevel='MS'),(SELECT dlevel from Degrees WHERE name='Computer Science' AND dlevel='MS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1006'),(SELECT name from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'),(SELECT dlevel from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'))");
			commands.add("INSERT INTO Major(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1007'),(SELECT name from Degrees WHERE name='Computer Science' AND dlevel='PhD'),(SELECT dlevel from Degrees WHERE name='Computer Science' AND dlevel='PhD'))");
			// Insert Minor
			commands.add("INSERT INTO Minor(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1007'),(SELECT name from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'),(SELECT dlevel from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'))");
			commands.add("INSERT INTO Minor(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1001'),(SELECT name from Degrees WHERE name='Software Engineering' AND dlevel='BS'),(SELECT dlevel from Degrees WHERE name='Software Engineering' AND dlevel='BS'))");
			commands.add("INSERT INTO Minor(SNUM,name,mlevel) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1005'),(SELECT name from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'),(SELECT dlevel from Degrees WHERE name='Applied Mathematics' AND dlevel='MS'))");
			// Courses
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '113','Spreadsheet','Microsoft Excel and Access','3','Undergraduate',(SELECT department_code from Departments where department_code='401'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '311','Algorithm','Design and Analysis','3','Undergraduate',(SELECT department_code from Departments where department_code='401'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '531','Theory of Computation','Theorem and Probability','3','Graduate',(SELECT department_code from Departments where department_code='401'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '363','Database','Design Principle','3','Undergraduate',(SELECT department_code from Departments where department_code='401'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '412','Water Management','Water Management','3','Undergraduate',(SELECT department_code from Departments where department_code='404'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '228','Special Topics','Interesting Topics about CE','3','Undergraduate',(SELECT department_code from Departments where department_code='403'))");
			commands.add("INSERT INTO Courses(Cnumber,name,description,credithours,clevel,department_code) "
					+ "VALUES ( '101','Calculus','Limit and Derivative','4','Undergraduate',(SELECT department_code from Departments where department_code='402'))");
			// Register
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1001'),(SELECT CNumber from Courses WHERE Cnumber='363'),'Fall2015','3')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1002'),(SELECT CNumber from Courses WHERE Cnumber='311'),'Fall2015','4')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1003'),(SELECT CNumber from Courses WHERE Cnumber='228'),'Fall2015','4')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1004'),(SELECT CNumber from Courses WHERE Cnumber='363'),'Spring2015','3')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1005'),(SELECT CNumber from Courses WHERE Cnumber='531'),'Spring2015','4')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1006'),(SELECT CNumber from Courses WHERE Cnumber='363'),'Fall2015','3')");
			commands.add("INSERT INTO Register(SNUM,course_number,when_class,grade) "
					+ "VALUES ( (SELECT SNUM from Students WHERE SNUM='1007'),(SELECT CNumber from Courses WHERE Cnumber='531'),'Spring2015','4')");
			// Execute Commands
			String c = "Inserting...";
			System.out.println(c);
			for (Iterator<String> i = commands.iterator(); i.hasNext();) {
				c = i.next();
				insertion.addBatch(c);
			}
			insertion.executeBatch();
			System.out.println("Done!");

		} catch (SQLException e) {
			System.out.println("Error Creating Statement");
			e.printStackTrace();
		}
	}

	public void queryDB() {
		Connection linkup = this.getDBConnection();
		try {
			Statement Query = linkup.createStatement();
			ResultSet qresults = null;
			String query = "";
			// Adding Queries.
			/*
			 * It is probably more logical to add these queries to a list and go
			 * with a loop from there. However, as some queries have a different
			 * number of return columns I am shaky on implementing that.
			 */
			/* 1 */
			// System.out.println("Problem #1");
			query = "SELECT SNUM,ssn FROM Students WHERE name='Becky'";
			qresults = Query.executeQuery(query);
			while (qresults.next()) {
				System.out.println(qresults.getString("SNUM") + "," + qresults.getString("ssn"));
			}
			/* 2 */
			// System.out.println("Problem #2");
			query = "SELECT Major.name,Major.mlevel FROM Major " + "INNER JOIN Students ON Major.SNUM=Students.SNUM "
					+ "WHERE Students.ssn='123097834'";
			qresults = Query.executeQuery(query);
			while (qresults.next()) {
				System.out.println(qresults.getString(1) + "," + qresults.getString(2));
			}
			/* 3 */
			// System.out.println("Problem #3");
			query = "SELECT name from Courses where department_code='401'";
			qresults = Query.executeQuery(query);
			while (qresults.next()) {
				System.out.println(qresults.getString(1));
			}
			// System.out.println("Problem #4");
			query = "SELECT name,dlevel from Degrees where department_code='401'";
			qresults = Query.executeQuery(query);
			while (qresults.next()) {
				System.out.println(qresults.getString("name") + "," + qresults.getString("dlevel"));
			}
			// System.out.println("Problem #5");
			query = "SELECT Students.name FROM Students INNER JOIN Minor ON Students.SNUM=Minor.SNUM";
			qresults = Query.executeQuery(query);
			while (qresults.next()) {
				System.out.println(qresults.getString(1));
			}
			// System.out.println("Complete!");

		} catch (SQLException e) {
			System.out.println("Error in Query");
			e.printStackTrace();
		}

	}

	/*
	 * Just Gets the Database connection
	 */
	public Connection getDBConnection() {

		Connection conn = null;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:mem:test", DB_USER, DB_PASSWORD);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
