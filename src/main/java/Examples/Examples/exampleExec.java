package Examples.Examples;

import java.sql.SQLException;

public class exampleExec {

	public static void main(String[] args) throws SQLException {
		InMemoryDBExample db=new InMemoryDBExample("","");
		db.CreateTables();
		db.insertRecords();
		db.queryDB();
		
	}

}
