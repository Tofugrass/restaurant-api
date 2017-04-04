

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RoundTwo
 */
public class RoundTwo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("optradio3")==null || request.getParameter("optradio4")==null) {String redirect = request.getRequestURL().toString();
		//if the user hasnt voted on 4 restaurants, he is redirected
		redirect = redirect.substring(0, redirect.lastIndexOf("/")+1);
		redirect+="index.jsp";
		response.sendRedirect(redirect);
		}else {
		 doPost(request, response);
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		for(int i = 1; i < 5; i++) {
			session.setAttribute("winner"+i, request.getParameter("optradio"+i));
			//we set an attribute called winner1-4, and we set that as the restaurant number that won the previous round
		}
		request.getRequestDispatcher("/bracket2.jsp").forward(request, response);
		//request.getRequestDispatcher("").forward(request, response);
	}
}
