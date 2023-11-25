package test;

import model.AccountsDAO;
import model.AccountsDAO.TransactionStatus;
import model.User;

public class TransacctionStatusTest {

	public static void main(String[] args) {
		// DAOテスト
		testTransacctionStatusOK();
		//testTransacctionStatusNG();

	}
	
	public static void testTransacctionStatusOK() {
		//String action = null;
		String action = "done";
		
		AccountsDAO dao = new AccountsDAO();
		
		User user = new User("keitai12", "atra223123", "atra@example.com", "aとら2", 35);
		TransactionStatus result = dao.saveUserTransaction(user, action);
		dao.setFirstSaveSuccess(true);		
		dao.setSecondSaveSuccess(false);
		if (result == TransactionStatus.SUCCESS) {
			System.out.println("保存成功");
		} else if (result == TransactionStatus.PENDING) {
			System.out.println("保留中です");
		} else {
			System.out.println("失敗です");
		}
		
	}
		
}
