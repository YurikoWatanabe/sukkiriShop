package test;

import model.User;
import model.UserValidation;

public class UserValidationTest {

	public static void main(String[] args) {
		// UserValidationTestテスト
		testValidationOK(); 
		testValidationNG();
	}
	
	public static void testValidationOK() {
		User user = new User("yuri", "5678watanabe12341234", "5678watanabe12341234", "y@gmail.com", "綿", 25);
		UserValidation v = new UserValidation();
		boolean result = v.validation(user);
		
		
		if (result) {
			System.out.println("testValidationOK:成功しました。");
		} else {
			System.out.println("testValidationOK:失敗しました。");
		}
	}
	public static void testValidationNG() {
		User user = new User(null, null, null, null, null, 125);
		UserValidation v = new UserValidation();
		boolean result = v.validation(user);
		
		if (!result) {
			System.out.println("testValidationNG:成功しました。");
		} else {
			System.out.println("testValidationNG:失敗しました。");
		}		
	}
}
