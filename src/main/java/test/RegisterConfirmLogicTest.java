package test;

import model.ResisterConfirmLogic;
import model.User;

public class RegisterConfirmLogicTest {

	public static void main(String[] args) {
		// LoginLogicテスト
		testConfirmOK(); 
		testConfirmNG();
	}
	
	public static void testConfirmOK() {
		User user = new User("晶", "watanabe12341234", "watanabe12341234", "y@gmail.com", "nabe", 25);
		ResisterConfirmLogic confirm = new ResisterConfirmLogic();
		boolean result = confirm.confirm(user);
				
		if (result) {
			System.out.println("testExecuteOK:成功しました。");
		} else {
			System.out.println("testExecuteOK:失敗しました。");
		}
	}
	public static void testConfirmNG() {
		User user = new User("晶", "5678watanabe12341234", "567watanabe12341234", "y@gmail.com", "nabe", 25);
		ResisterConfirmLogic confirm = new ResisterConfirmLogic();
		boolean result = confirm.confirm(user);
		
		if (!result) {
			System.out.println("testExecuteNG:成功しました。");
		} else {
			System.out.println("testExecuteNG:失敗しました。");
		}		
	}
}
