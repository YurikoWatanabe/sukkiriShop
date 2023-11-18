//Entity（Userクラス）をバリデーションするクラス

package model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class UserValidation {
	public boolean validation(User user) {
		//validatorを取得
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		//バリデーションを実行
		Set<ConstraintViolation<User>> result = validator.validate(user);
		
		//結果の確認
		if (result.size() > 0) {
			for (ConstraintViolation<User> violation : result) {
				System.out.println("Validation Error: " + violation.getPropertyPath() + violation.getMessage());
			}
			return false;
		}
		return true;
	}
	
}

