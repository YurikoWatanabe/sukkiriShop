package test;

import model.AccountsDAO;
import model.User;

public class IsUserIdUniqueTest {

	public static void main(String[] args) {
		// ユーザーID一意性テスト
		testisUserIdUniqueOK();
		//testFindByLoginNG();

	}
	//
	public static void testisUserIdUniqueOK() {
		AccountsDAO dao = new AccountsDAO();
		User user = new User("fff", "test", "", "aaa@gmail.com", "クラウド", 50);
		boolean isUnique = dao.isUserIdUnique(user);
		if (isUnique) {
			System.out.println("てすと結果：ユーザーIDは一意です");
		} else {
			System.out.println("テスト結果：ユーザーIDが重複しています");
		}
		System.out.println("testisUserIdUniqueOKのテスト：成功しました。");
	}
}
