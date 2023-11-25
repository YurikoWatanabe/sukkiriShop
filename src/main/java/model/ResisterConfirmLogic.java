//BOクラス：ユーザー情報確認の処理とDBへの保存処理

package model;

import model.AccountsDAO.TransactionStatus;

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
	return true;
	}	
	//トランザクション
	public TransactionStatus firstSecondSave(User user, String action) {
		return dao.saveUserTransaction(user, action);		
	}	
}