//DAOクラス：ACCOUNTSテーブル担当

package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AccountsDAO {
	//クラス定数
	public static final String JDBC_URL;
	public static final String DB_USER;
	public static final String DB_PASS;
	
	//クラス定数を初期化
	static {
		//設定情報を読み込む
		Properties properties = new Properties();
		try (InputStream input = AccountsDAO.class.getClassLoader().getResourceAsStream("config.properties")){
			if (input != null) {
				properties.load(input);
				JDBC_URL = properties.getProperty("JDBC_URL");
				DB_USER = properties.getProperty("DB_USER");
				DB_PASS = properties.getProperty("DB_PASS");
			} else {
				System.out.println("config.propertiesファイルが見つかりません");
				JDBC_URL = null;
				DB_USER = null;
				DB_PASS = null;
			}
					
		} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Failed to load confiiguration properties");
		}
	}
	
	//JDBCドライバを読み込むメソッド//テストのためprotectedからpublicへ一時変更
	public void loadJDBCDriver() {
		try {
			Class.forName("org.h2.Driver");			
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException("JDBCドライバを読み込めませんでした");
		}
	}	
	
	//テーブルからログイン情報を検索するメソッド
	public Account findByLogin(Login login) {
		Account account = null;
			//JDBCドライバを読み込む
			loadJDBCDriver();
			//データベース接続
			try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
				//SQL文
				String sql = "select * from accounts where user_id = ? and pass = ?";			
				PreparedStatement pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, login.getUserId());
				pStmt.setString(2, login.getPass());
				//結果表を取得
				ResultSet rs = pStmt.executeQuery();
				
				if(rs.first()) {
					String userId = rs.getString("user_id");
					String pass = rs.getString("pass");
					String mail = rs.getString("mail");
					String name = rs.getString("name");
					int age = rs.getInt("age");
					account = new Account(userId,pass, mail, name, age);
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
		return account;
	}  
	
	//登録前のユーザーIDに重複が無いかをチェックするメソッド
	public boolean isUserIdUnique(User user) {
		//JDBCドライバを読み込む
		loadJDBCDriver();
		//データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			//SQL文（該当のユーザーIDを検索、存在するレコード数を求める）
			String sql = "select count(*) as count from accounts where user_id = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, user.getRUserId());
			
			//結果表(レコード数)を取得
			ResultSet rs = pStmt.executeQuery();
			
			//レコード数が1以上なら重複あり
			if(rs.next()) {		
				int count = 0;
				//countカラムの値を求める
				count = rs.getInt("count");
				if (count > 0) {
					System.out.println("ユーザーIDが重複しています");
					return false;
				}				
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	System.out.println("ユーザーIDは一意です");
	return true;
	}	
	
	//登録前にユーザーIDとパスワードだけをDBに保存するメソッド(１回目の保存)
	public boolean saveUserIdAndPass(User user){
		//JDBCドライバを読み込む
		loadJDBCDriver();
		//パスワードのハッシュ＆ソルト化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPass = encoder.encode(user.getRPass());
		
		//データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			//SQL文
			String sql = "insert into accounts(user_Id, pass, mail, name, age) values(?, ?, 'dummy@example.com', 'ダミー名前', 20)";			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, user.getRUserId());
			pStmt.setString(2, hashedPass);

			//変更した行数を取得
			int rowCount = pStmt.executeUpdate();			
			if(rowCount > 0) {
				System.out.println("ユーザーIDとパスワードをDBへ保存しました");
				return true;
			} else {
				System.out.println("ユーザーIDとパスワードを保存できませんでした");
				return false;
			}		
		} catch(SQLException e) {
			e.printStackTrace();
			return false; 		
		}
	} 
		 
		
	//登録後に全てのカラムへ保存するメソッド（２回目の保存）
	public boolean saveUser(User user){	
		//JDBCドライバを読み込む
		loadJDBCDriver();
		//データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) { 
			//SQL文
			String sql = "update accounts set mail=?, name=?, age=? where user_id = ?";			
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, user.getRMail());
			pStmt.setString(2, user.getRName());
			pStmt.setInt(3, user.getRAge());
			pStmt.setString(4, user.getRUserId());

			//変更した行数を取得
			int rowCount = pStmt.executeUpdate();
			
			if(rowCount > 0) {
				System.out.println("ユーザー情報を登録しました");
				return true;
			} else {
				System.out.println("ユーザー情報を登録できませんでした");
			  return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false; 
		}	
	}			
			
	//トランザクション用メソッド
	public boolean saveUserTransaction(User user) {
		//JDBCドライバを読み込む
		loadJDBCDriver();
		//データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
			//トランザクションの開始
			conn.setAutoCommit(false);
			
			//登録前にユーザーIDとパスワードだけをDBに保存する
			if (saveUserIdAndPass(user)) {
				//登録後に全てのカラムへ保存する
				if (saveUser(user)) {
					//トランザクションのコミット
					conn.commit();
					System.out.println("トランザクションが正常に完了しました");
					return true;
				}
			}				
			//トランザクションのロールバック
			conn.rollback();
			System.out.println("トランザクションをロールバックしました");					
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
