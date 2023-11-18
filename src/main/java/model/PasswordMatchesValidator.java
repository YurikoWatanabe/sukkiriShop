package model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jboss.logging.Logger;

//カスタムバリデーションを実装するクラス

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
	//ロガーを取得
	private static final Logger logger = Logger.getLogger(PasswordMatchesValidator.class.getName());
	
	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
		//何も処理しない
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		//ログ出力
		logger.info("value: " + value);
		
		//バリテーション対象がnullの場合、無条件にバリテーション成功
		if (value == null) {
			return true;
		}
		if (value instanceof User) {
			User user = (User) value;
			logger.info("RPass: " + user.getRPass());
			logger.info("RPass2: " + user.getRPass2());
			return user.getRPass().equals(user.getRPass2());
		}
		//Userクラスでない場合、バリテーション失敗
		logger.info("Userクラスではありません");
		return false;
	}
}