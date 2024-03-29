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
	//クラス定数（DB設定情報）
	public static final String JDBC_URL;
	public static final String DB_USER;
	public static final String DB_PASS;
	
	//クラス定数（DB設定情報）を初期化
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
	
	//トランザクションの結果を格納する変数の初期化
	static Boolean preSaveSuccess = null;
	static Boolean SaveSuccess = null;
	
	
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
				String sql = "select * from accounts where user_id = ?";			
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, login.getUserId());
				//結果表を取得
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.first()) {
					//passカラムの値を取得
					String hashedPass = rs.getString("pass");
					//パスワードを比較
					BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
					if (encoder.matches(login.getPass(), hashedPass)) {						
						String userId = rs.getString("user_id");
						String pass = hashedPass;
						String mail = rs.getString("mail");
						String name = rs.getString("name");
						int age = rs.getInt("age");
						account = new Account(userId,pass, mail, name, age);
						System.out.println("データがヒットしました");						
					} else {
						System.out.println("パスうわーどが一致しません");
					}					
				} else {
					System.out.println("ユーザーIDが見つかりません");
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println("データがヒットしませんでした");
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
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getRUserId());
			
			//結果表(レコード数)を取得
			ResultSet rs = pstmt.executeQuery();
			
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
	
	//ユーザー情報を一時テーブルに仮保存するメソッド
	public boolean preSaveUser(Connection conn, User user){
		//パスワードのハッシュ＆ソルト化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPass = encoder.encode(user.getRPass());
		//SQL文（一時テーブルを作成）
		String preSQL = "create temporary table temporary_table (user_id varchar(10) primary key, pass varchar(255), mail varchar(100), name varchar(40), age integer)";
		try (PreparedStatement prepstmt = conn.prepareStatement(preSQL)) {
			prepstmt.executeUpdate();
			System.out.println("一時テーブルを作成しました");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		//SQL文（一時保存）
		String sql = "insert into temporary_table (user_Id, pass, mail, name, age) values(?, ?, ?, ?, ?)";	
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {			
			pstmt.setString(1, user.getRUserId());
			pstmt.setString(2, hashedPass);
			pstmt.setString(3, user.getRMail());
			pstmt.setString(4, user.getRName());
			pstmt.setInt(5, user.getRAge());
			
			//変更した行数を取得
			int rowCount = pstmt.executeUpdate();			
			if(rowCount > 0) {
				System.out.println("ユーザー情報を一時テーブルへ仮保存しました");
				return true;
			} else {
				System.out.println("ユーザー情報を一時テーブルへ仮保存できませんでした");
				return false;
			}		
		} catch(SQLException e) {
			e.printStackTrace();
			return false; 		
		}
	} 
	
	//一時テーブルのユーザー情報をaccountsテーブルへ移動するメソッド
	public boolean saveUser(Connection conn) {
		//SQL文
		String sql = "insert into accounts (user_id, pass, mail, name, age) select user_id, pass, mail, name, age from temporary_table";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.executeUpdate();
			return true;
		} catch(SQLException e) {
			e.printStackTrace();
			return false; 		
		}
	}
	
	//一時テーブルのデータを削除するメソッド
	public boolean deletePreSaveUser () {
		//JDBCドライバを読み込む
		loadJDBCDriver();		
		//データベース接続
		try (Connection deleteConn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {		
			//SQL文
			String sql = "delete from temporary_table";
			try (PreparedStatement pstmt = deleteConn.prepareStatement(sql)) {
				 pstmt.executeUpdate();
				 System.out.println("一時テーブルのデータを削除しました");
				 return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}

	//トランザクションの戻り値を定義
	public enum TransactionStatus {
		SUCCESS,
		PENDING,
		FAILURE,
	}
	
	//トランザクション用メソッド
	public TransactionStatus saveUserTransaction(User user, String action) {
		//JDBCドライバを読み込む
		loadJDBCDriver();		
		//データベース接続
		try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {			
					
			try {
				//actionの値が「null」かつ仮保存がまだなら一時テーブルへ保存
				if (action == null && preSaveSuccess == null) {
					preSaveSuccess = preSaveUser(conn, user);						
					//一時テーブルへの保存が成功かつaccountsテーブルへの保存は未確定の場合
					if (preSaveSuccess && SaveSuccess == null) {
						System.out.println("登録未確定のため、トランザクションを保留にしました");
						return TransactionStatus.PENDING;
						//仮保存が失敗した場合ロールバック
					} else {
						conn.rollback();
						System.out.println("一時テーブルへの保存が失敗したため、トランザクションをロールバックしました");
						return TransactionStatus.FAILURE;
					}	
				}//actionの値が「done」かつpreSaveSuccessがtrueなら登録確定（accountsテーブルへ保存＆コミット）
				else if (action.equals("done") && preSaveSuccess) {
					//トランザクションの開始
					conn.setAutoCommit(false);		
					SaveSuccess = saveUser(conn);						
					//コミット
					conn.commit();
					SaveSuccess = deletePreSaveUser();
					System.out.println("トランザクションが正常に完了しました。一時保存データを削除し、accountsテーブルへ保存しなおしました");
					return TransactionStatus.SUCCESS;
				} else {
						//不明なアクションのためロールバック
						conn.rollback();
						System.out.println("不明なアクションのため、トランザクションをロールバックしました");
						return TransactionStatus.FAILURE;
				} 						
			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
				System.out.println("トランザクションをロールバックしました");
				return TransactionStatus.FAILURE;
			} 			
		} catch (SQLException e) {
			e.printStackTrace();
			return TransactionStatus.FAILURE;
								
		} 
	}

}

