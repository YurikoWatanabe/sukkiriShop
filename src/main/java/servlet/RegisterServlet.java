package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ResisterConfirmLogic;
import model.User;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ユーザー登録画面へフォワードする		
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/registerForm.jsp");
		dispatcher.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//リクエストパラメータを取得(入力値)
		request.setCharacterEncoding("UTF-8");
		String rUserId = request.getParameter("rUserId");
		String rPass = request.getParameter("rPass");
		String rPass2 = request.getParameter("rPass2");
		String rMail = request.getParameter("rMail");
		String rName = request.getParameter("rName");		
		//rAgeはInteger型に変換する
		String rAgeStr = request.getParameter("rAge");
		Integer rAge = 0;
		if (rAgeStr != null && !rAgeStr.isEmpty()) {
			rAge = Integer.parseInt(rAgeStr);
		} 			
		
		//リクエストパラメータ（hidden）
		String action = request.getParameter("action");
		
		//BOクラス、Userクラスをインスタンス化
		ResisterConfirmLogic bo = new ResisterConfirmLogic();		
		User user = new User(rUserId, rPass, rPass2, rMail, rName, rAge);	
		
		//actionパラメータによって処理振り分け
		//actionパラメータがnullなら入力値チェック～セッションへ保存（rUserIdとrPassのみDBへ保存）
		if (action == null) {
			//入力値チェックを実行					
			boolean result = bo.confirm(user);
			
			//入力値を個別で保存（Userクラスにはパスワードも含まれているため）
			HttpSession session = request.getSession();
			session.setAttribute("rUserId", rUserId);
			session.setAttribute("rMail", rMail);
			session.setAttribute("rName", rName);
			session.setAttribute("rAge", rAge);
			
			//チェックの結果によって分岐
			if (result) {
				//resultがtrueなら確認画面へフォワード				
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/registerConfilm.jsp");
				dispatcher.forward(request, response);
			} else {
				//resultがfalseならリダイレクト
				response.sendRedirect("RegisterServlet");
			}	
		//actionパラメータがdoneなら、残りの入力値をDBへ保存、メイン画面へ
		} else if (action.equals("done")) {
			//DBへ保存（2回目）
			boolean result = bo.saveSecond(user);
			//トランザクション
			boolean result2 =  bo.Transaction(user);
			//メイン画面へフォワード
			if (result && result2) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginOK.jsp");
				dispatcher.forward(request, response);
			} else {
				System.out.println("失敗：action" + action);
				//resultとresult2がfalseならリダイレクト
				response.sendRedirect("RegisterServlet");
			}
			
		}
		
		
		
		
		
		
		
		
	}

}
