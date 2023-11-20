package test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import model.AccountsDAO;
import model.Login;

public class propertiesAccountsDAOTest {

	
		// propertiesDAOテスト
		//クラス定数
		public static final String JDBC_URL2;
		public static final String DB_USER2;
		public static final String DB_PASS2;
			
				
		//クラス定数を初期化
		static {
			//設定情報を読み込む			
			Properties properties = new Properties();
			try (InputStream input = propertiesAccountsDAOTest.class.getClassLoader().getResourceAsStream("config.properties")){
				if (input != null) {
					properties.load(input);
					JDBC_URL2 = properties.getProperty("JDBC_URL");
					DB_USER2 = properties.getProperty("DB_USER");
					DB_PASS2 = properties.getProperty("DB_PASS");
					
					
				} else {
					System.out.println("config.propertiesファイルが見つかりません");
					JDBC_URL2 = null;
					DB_USER2 = null;
					DB_PASS2 = null;
					
				}
						
			} catch (IOException e) {
			e.printStackTrace();
			System.out.println("失敗");
			throw new RuntimeException("Failed to load confiiguration properties");
			}
		}
		
		public static void main(String[] args) {
			
			AccountsDAO dao = new AccountsDAO();
			dao.loadJDBCDriver();
			Login login = new Login("minato", "1234");
			try (Connection conn = DriverManager.getConnection(JDBC_URL2, DB_USER2, DB_PASS2)) {
				System.out.println("JDBC_URL:" + JDBC_URL2); //"jdbc:h2:tcp://localhost/~/example"
				System.out.println("DB_USER:" + DB_USER2);	//"sa"
				System.out.println("DB_PASS:" + DB_PASS2);	//""
				
				//SQL文
				String sql = "select * from accounts where user_id = ? and pass = ?";			
				PreparedStatement pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, login.getUserId());
				pStmt.setString(2, login.getPass());
				//結果表を取得
				ResultSet rs = pStmt.executeQuery();
				
				System.out.println(rs);
				
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println("JDBC_URL:" + JDBC_URL2);
				System.out.println("DB_USER:" + DB_USER2);
				System.out.println("DB_PASS:" + DB_PASS2);
				
			}
		}
	}

	