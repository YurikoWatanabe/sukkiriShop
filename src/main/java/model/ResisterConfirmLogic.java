//BOクラス：ユーザー情報確認の処理

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ResisterConfirmLogic {
	AccountsDAO dao = new AccountsDAO();
	
	//バリデーション
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
	
	//DBへの保存（1回目）
	dao.saveUserIdAndPass(user);
	
	//DBへの保存（2回目）
	
	
}