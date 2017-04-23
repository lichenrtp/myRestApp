package com.ibm.cio.rest.jersey;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("/cioapi")
public class CioApi {
	PreparedStatement psInsert;
	Statement s;
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String saveToDB(String jsonStr) {
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(jsonStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		//get DB connection
		Connection conn = connectToDB();
		
		//create or update a entity
		updateDB(conn, jObject);
				
		//get an entity from DB
		JSONObject json = getDB(conn, jObject);
		
		try {
			conn.close();
			psInsert.close();
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//return as a jsonStr
		return json.toString();
	}
	
	/*
	 * create a connection to Derby DB
	 */
	private Connection connectToDB() {
		//use embedded derby database
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		
		//for POC, DB (MyDBTest) and table (derbyDB) was created manually
		//url for connecting derby DB
		String connectionURL = "jdbc:derby:/Applications/Apache/db-derby-10.13.1.1-bin/bin/MyDBTest";
		Connection conn = null;
	    try {
	    	Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("Connected to database MYDbTest");
		} catch (Exception e) {
			System.out.println("Error occurred during connecting to DB MYDbTest");
			e.printStackTrace();
		}		        
        return conn;
	}
	
	/*
	 * update or insert a new entity
	 */
	private void updateDB(Connection conn, JSONObject jObject) {	    	    
	    try {
	    	//  Prepare insert statement 
			psInsert = conn.prepareStatement("insert into derbyDB(fname, lname, addr, company) values (?, ?, ?, ?)");
			psInsert.setString(1,jObject.getString("firstName"));
			psInsert.setString(2,jObject.getString("lastName"));
			psInsert.setString(3,jObject.getString("address"));
			psInsert.setString(4,jObject.getString("company"));
            psInsert.executeUpdate();
            
            System.out.println("Updated DB successfully");
		} catch (Exception e) {
			System.out.println("Error occurred during updating DB MYDbTest");
			e.printStackTrace();
		}
		
	}
	
	/*
	 * get the new created entity from DB
	 */
	private JSONObject getDB(Connection conn, JSONObject jObject) {
	    ResultSet results;	    
	    JSONObject jObj = new JSONObject();
	    
	    try {
	    	s = conn.createStatement();
	    	
	    	String lname = jObject.getString("lastName");
		    String fname = jObject.getString("firstName");
		    String selectSql = "select * from derbyDB where fname='" + fname + "' and lname='" + lname + "'";
		    System.out.println(selectSql);
		    
	    	results = s.executeQuery(selectSql);          
            System.out.println("get from DB successfully");
            while (results.next())
            {
            	jObj.append("firstName", results.getString(1));
            	jObj.append("lastName", results.getString(2));
            	jObj.append("address", results.getString(3));
            	jObj.append("company", results.getString(4));
            }
		} catch (Exception e) {
			System.out.println("Error occurred during getting the new created entity from DB MYDbTest");
			e.printStackTrace();
		}
		return jObj;
	}
	
}