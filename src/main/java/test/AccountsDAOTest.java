package test;

import model.Account;
import model.AccountsDAO;
import model.Login;

public class AccountsDAOTest {

	public static void main(String[] args) {
		// DAOテスト
		testFindByLoginOK();
		testFindByLoginNG();

	}
	//ユーザーが見つかった場合(mainメソッドから直で呼び出すのでstaticが必要)
	//ログイン自体の成功失敗だけでなく、情報の取得がうまくいっているかもテストする
	public static void testFindByLoginOK() {
		Login login = new Login("minato", "1234");
		AccountsDAO dao = new AccountsDAO();
		Account result = dao.findByLogin(login);
		if (result != null && 
				result.getUserId().equals("minato") &&
				result.getPass().equals("1234") &&
				result.getMail().equals("yusuke.minato@miyabilink.jp") &&
				result.getName().equals("湊　雄輔") &&
				result.getAge() == 23) {
			System.out.println("testFindByLoginOK:成功しました。");
		} else {
			System.out.println("testFindByLoginOK:失敗しました。");
		}
	}
	//ユーザーが見つからなかった場合
	public static void testFindByLoginNG() {
		Login login = new Login("minato", "12345");
		AccountsDAO dao = new AccountsDAO();
		Account result = dao.findByLogin(login);
		if (result == null) {
			System.out.println("testFindByLoginNG:成功しました。");
		} else {
			System.out.println("testFindByLoginNG:失敗しました。");
		}
	}
}
