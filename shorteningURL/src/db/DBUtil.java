package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil
{
	//접속정보
	private static String dbId = "WB"; //유저
	private static String dbPw = "WB"; //비밀번호
	private static String dbUrl = "jdbc:oracle:thin:@localhost:1521:orcl"; //접속url
	
	private static String dbDriver = "oracle.jdbc.driver.OracleDriver"; 
	
	public static Connection getConnection() 
	{
		Connection conn = null;
		
		try
		{
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("DB Driver loading fail : ");
			System.out.println(e.getMessage());
		}
		catch(SQLException e)
		{
			System.out.println("DB Connection fail : ");
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			System.out.println("error : ");
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
}
