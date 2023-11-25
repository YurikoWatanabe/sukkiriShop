package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.AccountsDAO.TransactionStatus;
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
		//actionパラメータがnullなら入力値チェック～セッションへ保存とrUserIdとrPassのみDBへ保存（トランザクション）
		if (action == null) {
			//入力値チェックと1回目のDB保存実行					
			boolean confirmResult = bo.confirm(user);
			
			//入力値を個別で保存（Userクラスにはパスワードも含まれているため）
			HttpSession session = request.getSession();
			session.setAttribute("rUserId", rUserId);
			session.setAttribute("rMail", rMail);
			session.setAttribute("rName", rName);
			session.setAttribute("rAge", rAge);
			
			//入力値チェックがtrueならトランザクション実行
			if (confirmResult == true) {
				TransactionStatus traResult = bo.firstSecondSave(user, action);
				//トランザクションの結果によって分岐/PENDINGなら確認画面へフォワード
				if (traResult == TransactionStatus.PENDING) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/registerConfilm.jsp");
					dispatcher.forward(request, response);				
				//FAILUREなら入力画面へリダイレクト
				} else if (traResult == TransactionStatus.FAILURE) {
					response.sendRedirect("RegisterServlet");
				}
			//入力値チェックがtrueでないなら入力画面へリダイレクト
			} else {
				response.sendRedirect("RegisterServlet");
			}	
		//actionパラメータがdoneなら2回目のトランザクション実行
		} else if (action.equals("done")) {
			TransactionStatus traResult2 = bo.firstSecondSave(user, action);
			//トランザクションの結果によって分岐/SUCCESSならトップ画面へフォワード
			if (traResult2 == TransactionStatus.SUCCESS) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginOK.jsp");
				dispatcher.forward(request, response);
				//SUCCESS以外なら入力画面へリダイレクト
			} else {
				response.sendRedirect("RegisterServlet");
				System.out.println("2回目のトランザクションが失敗しました");
			}
			
		}		
	}
}


