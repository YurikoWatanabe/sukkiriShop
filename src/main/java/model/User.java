package model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

//Entityクラス：ユーザー登録情報

@PasswordMatches(message = "パスワードが一致していません")
public class User {
	//登録画面で入力される情報
		//※入力チェックが十分かは最終的に確認する
		@NotNull(message = "ユーザーIDを入力してください")
		@Size(max = 10, message = "ユーザーIDは10文字以内で入力してください" )
		private String rUserId;
		
		@NotNull(message = "パスワードを入力してください")
		@Size(min = 8, max = 20, message = "パスワードは8文字以上20文字以内で入力してください")
		
		private String rPass;
		private String rPass2;
		
		@NotNull(message = "メールアドレスを入力してください")
		@Email(message = "正しいメールアドレスを入力してください")
		private String rMail;
		
		@NotNull(message = "名前を入力してください")
		@Size(max = 40, message = "名前が長すぎます")
		private String rName;
		
		@NotNull(message = "年齢を入力してください")
		@Min(value = 0, message = "0以上の数値を選択してください")
		@Max(value = 120, message = "120以下の数値を選択してください")
		private Integer rAge;
		
		//コンストラクタ
		public User() {}
		
		//オーバーロード
		public User(String rUserId, String rPass, String rPass2, String rMail, String rName, Integer rAge) {
			this.rUserId = rUserId;
			this.rPass = rPass;
			this.rPass2 = rPass2;
			this.rMail = rMail;
			this.rName = rName;
			this.rAge = rAge;
		}
		//オーバーロード
		public User(String rUserId, String rPass, String rMail, String rName, Integer rAge) {
			this.rUserId = rUserId;
			this.rPass = rPass;
			this.rMail = rMail;
			this.rName = rName;
			this.rAge = rAge;
		}
		
		//ゲッター
		public String getRUserId() {
			return rUserId;
		}
		public String getRPass() {
			return rPass;
		}
		public String getRPass2() {
			return rPass2;
		}	
		public String getRMail() {
			return rMail;
		}
		public String getRName() {
			return rName;
		}
		public Integer getRAge() {
			return rAge;
		}
}
