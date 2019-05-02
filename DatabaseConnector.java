import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

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
						+ v.getTestName() + "'," + v.getThresholdMarks() + "," + v.getMaxMarks() + ",'"
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

	public TreeMap<String, ArrayList<TestBean>> getTests(int clas) {
		TreeMap<String, ArrayList<TestBean>> result = new TreeMap<>();

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
					ArrayList<TestBean> arr = new ArrayList<>();
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
					((ArrayList<TestBean>) mapElement.getValue()).add(test);

				}
			}

			s.close();
			con.close();
		} catch (Exception e) {
			return null;
		}

		return result;
	}

}
