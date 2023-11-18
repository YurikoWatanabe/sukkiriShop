package test;

import model.AccountsDAO;
import model.User;

public class SaveUserIdPassDAOTest {

	public static void main(String[] args) {
		// DAOテスト(ユーザーIDとパスワードの保存)
		saveUserIdAndPassOK();
		saveUserIdAndPassNG();
	}
	//
	public static void saveUserIdAndPassOK() {
		
		User user = new User("test003", "fff", "fff", "aaa@gmail.com", "あべ", 50);
		AccountsDAO dao = new AccountsDAO();
		
		boolean result = dao.saveUserIdAndPass(user);
		
		if (result) {
			System.out.println("saveUserIdAndPassOKのテスト:成功しました");
		} else {
			System.out.println("saveUserIdAndPassOKのテスト:失敗しました");
		}
	}
		public static void saveUserIdAndPassNG() {			
			
			User user = new User("bbbccc2", "", "", "bbb@gmail.com", "吉田", 100);
			AccountsDAO dao = new AccountsDAO();
			
			boolean result = dao.saveUserIdAndPass(user);
			
			if (result == false) {
				System.out.println("saveUserIdAndPassNGのテスト:成功しました");
			} else {
				System.out.println("saveUserIdAndPassNGのテスト:失敗しました");
			}
		}
		
		
		
		
	

}
