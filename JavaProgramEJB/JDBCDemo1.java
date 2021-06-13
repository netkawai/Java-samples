import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class JDBCDemo1 {

	public static void dispRecord(ResultSet result) {
		try {
			System.out.print(result.getInt("empno")+",");
			System.out.print(result.getString("empname")+",");
			System.out.print(result.getString("city")+",");
			System.out.println(result.getFloat("salary")+",");
		}
		catch(SQLException ex) {
			Logger.getLogger(JDBCDemo1.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String [] args)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input city:");
		String s_city = scan.next();
		System.out.println("City:[" + s_city + "]");
		
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:JAVADSN";
			Connection con = DriverManager.getConnection(url,"kawai","123");
			
			ResultSet result;
			PreparedStatement state;
			if(Character.isDigit(s_city.charAt(0))) {
				state = con.prepareStatement("select * from Employee",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				result = state.executeQuery();
			}
			else { 
				state = con.prepareStatement("select * from Employee where city = ?",ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				state.setString(1,s_city);
				result = state.executeQuery();
			}

			// ResultSet indicates the pointer toward Database.
			// ResultSet does not have the all data, Result can't get the number of Record directly.
			result.last();
			int numOfRow = result.getRow();
			result.beforeFirst();
			if(numOfRow!=0) 
			{
				while(result.next()) {
					dispRecord(result);
				}

				while(result.previous()) {
					dispRecord(result);
				}
			}
			else {
				System.out.println("Not found Data!!");
			}
		}
		catch(SQLException ex) {
			Logger.getLogger(JDBCDemo1.class.getName()).log(Level.SEVERE, null, ex);
		}
		catch(ClassNotFoundException ex) {
			Logger.getLogger(JDBCDemo1.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
