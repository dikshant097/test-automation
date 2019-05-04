import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JProgressBar;

public class DatabaseConnector {
	Connection con;
	Statement s;
	ResultSet rs;
	URI dbUri;
	String username;
	String password;
	String dbUrl;
	Properties props;

	public DatabaseConnector() {
		super();
		try {
			dbUri = new URI(
					"postgres://tgajkqffcqbuuf:4b323c6ddf6734c206b808623fcb289e2260a8872689c3f458c21ef7cbff5213@ec2-54-197-234-117.compute-1.amazonaws.com:5432/dd546iqmle846b");
			Class.forName("org.postgresql.Driver");
			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
			dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
			props = new Properties();
			props.setProperty("user", username);
			props.setProperty("password", password);
			props.setProperty("ssl", "true");
			props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
			props.setProperty("sslmode", "verify-ca");

		} catch (Exception e) {
		}
	}

	public String writeMarks(HashMap<String, TestBean> marks) {
		String res = "", query = "";
		try {
			con = DriverManager.getConnection(dbUrl, props);
			con.setAutoCommit(false);
			s = con.createStatement();
			query = "SELECT nextval('test_no');";
			rs = s.executeQuery(query);
			int testNo;
			if (rs.next()) {
				testNo = rs.getInt(1);
			} else {
				s.close();
				con.close();
				return "Server Error. Please try later.";
			}
			marks.forEach((k, v) -> {
				k = k.toLowerCase();
				String q = "insert into test_details values(" + testNo + "," + v.getClas() + ",'" + k + "','"
						+ v.getTestName().toLowerCase() + "'," + v.getThresholdMarks() + "," + v.getMaxMarks() + ",'"
						+ v.getObtainedMarks() + "');";
				try {
					s.addBatch(q);
				} catch (Exception e) {

				}
			});
			s.executeBatch();
			con.commit();
			res = "Data saved Successfully";
			s.close();
			con.close();
		} catch (Exception e) {
			res = "Error Encountered.";
		}

		return res;
	}

	public TreeMap<String, TreeSet<TestBean>> getTests(int clas) {
		TreeMap<String, TreeSet<TestBean>> result = new TreeMap<>();

		String query = "";
		try {
			con = DriverManager.getConnection(dbUrl, props);
			s = con.createStatement();
			query = "select * from test_Details where clas=" + clas + ";";
			rs = s.executeQuery(query);
			while (rs.next()) {
				TestBean test = new TestBean();
				String name = (rs.getString(3)).toUpperCase();
				test.setId(rs.getInt(1));
				test.setTestName(rs.getString(4));
				test.setThresholdMarks(rs.getFloat(5));
				test.setMaxMarks(rs.getFloat(6));
				test.setObtainedMarks(rs.getString(7));
				if (result.containsKey(name)) {
					result.get(name).add(test);
				} else {
					TreeSet<TestBean> arr = new TreeSet<>();
					arr.add(test);
					result.put(name, arr);
				}
			}
			for (Map.Entry mapElement : result.entrySet()) {
				String name = (String) mapElement.getKey();
				query = "select distinct(sno),test_name from test_details where sno not in"
						+ " (select sno from test_details where clas=" + clas + " and student_name='"
						+ name.toLowerCase() + "');";
				rs = s.executeQuery(query);
				while (rs.next()) {
					TestBean test = new TestBean();
					test.setId(rs.getInt(1));
					test.setTestName(rs.getString(2));
					test.setObtainedMarks("na");
					((TreeSet<TestBean>) mapElement.getValue()).add(test);

				}
			}

			s.close();
			con.close();
		} catch (Exception e) {
			return null;
		}

		return result;
	}
	
	public HashMap<String,String> getListToEdit(int clas, String ID){
		
		HashMap<String,String> marks = new HashMap<>();
		String query = "";
		try {
			con = DriverManager.getConnection(dbUrl, props);
			s = con.createStatement();
			query = "select student_name, obtained from test_details where clas ="+clas+" and test_name='"+ID.toLowerCase()+"';";
			rs=s.executeQuery(query);
			while(rs.next())
			{
				marks.put(rs.getString(1), rs.getString(2));
			}
			s.close();
			con.close();
		}
		catch(Exception e)
		{
			return null;
		}
		return marks;
		
	}
	
	public  int updateMarks(HashMap<String,String> marks, int clas , String id)
	{
		if(marks == null || marks.size() == 0)
			return -1;
		
		try {
			con = DriverManager.getConnection(dbUrl, props);
			con.setAutoCommit(false);
			s = con.createStatement();
			
			marks.forEach((k, v) -> {
				String query = "update test_details set obtained='" + v + "' where student_name='" + k.toLowerCase() +"' and clas="+
						clas + " and test_name='" + id.toLowerCase() +"';";
				try {
					s.addBatch(query);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			s.executeBatch();
			con.commit();
			s.close();
			con.close();
			
		}catch(Exception e) 
		{
			return -1;
		}
		
		return 1;
	}
	
	public int startNewSession() {
	
		try {
			con = DriverManager.getConnection(dbUrl, props);
			con.setAutoCommit(false);
			s = con.createStatement();
			
			String query = "delete from test_details where clas=12 or clas=11;";
			s.addBatch(query);
			query = " ALTER SEQUENCE test_no RESTART WITH 1;";
			s.addBatch(query);
			
			s.executeBatch();
			con.commit();
			s.close();
			con.close();
		}
		catch(Exception e) {
			return -1;
		}
		
		return 1;
	}
}
