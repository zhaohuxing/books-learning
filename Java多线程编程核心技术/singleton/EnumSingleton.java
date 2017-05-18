import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class EnumSingleton {
	public static void main(String[] args) {
		MyThread t1 = new MyThread();
		MyThread t2 = new MyThread();
		MyThread t3 = new MyThread();
		t1.start();
		t2.start();
		t3.start();
	}	
}

class MyObject {
	//遵循职责单一原则
	public enum MyEnumSingleton {
		connectionFactory;
		private Connection connection;
		private MyEnumSingleton() {
			try {
				System.out.println("创建MyObject对象");
				String url = "jdbc:mysql//localhost:3306/struts";
				String username = "root";
				String password = "123456";
				String driverName = "com.mysql.jdbc.Driver";
				Class.forName(driverName);
				connection = DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public Connection getConnection() {
			return connection;
		}
	}

	public static Connection getConnection() {
		return MyEnumSingleton.connectionFactory.getConnection();
	}
}

class MyThread extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println(MyObject.getConnection().hashCode());
		}
	}
}






















