//BOクラス：ユーザー情報確認の処理とDBへの保存処理

package model;

public class ResisterConfirmLogic {
	AccountsDAO dao = new AccountsDAO();
	
	//ビジネスロジック・バリデーション
	public boolean confirm(User user) {
		//入力値チェック
		UserValidation valid = new UserValidation();		
		boolean validResolt = valid.validation(user);
		if (validResolt == false) {
			return false;
		}
		//ユーザーIDの重複チェック
		boolean userIdUniqueResolt = dao.isUserIdUnique(user);
		if (userIdUniqueResolt == false) {
			return false;
		}	
	//DBへの保存（1回目）
	dao.saveUserIdAndPass(user);
	return true;
	}	
	
	//ビジネスロジック・DBへの保存（2回目）
	public boolean saveSecond(User user) {
		//2回目の保存
		return dao.saveUser(user);		
	}
	
	//ビジネスロジック・トランザクション
	public boolean Transaction(User user) {
		//トランザクション
		return dao.saveUserTransaction(user);
	}
	
	
}