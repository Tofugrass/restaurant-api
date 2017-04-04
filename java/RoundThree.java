

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RoundThree
 */
public class RoundThree extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoundThree() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		if(request.getParameter("optradio1")==null || request.getParameter("optradio2")==null) {
			//again we make sure the user is tunneled correctly
			String redirect = request.getRequestURL().toString();
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
		for(int i = 1; i < 3; i++) {
			session.setAttribute("winner"+i, request.getParameter("optradio"+i));
			//we set an attribute called winner1-2, and we set that as the restaurant number that won the previous round
		}
		request.getRequestDispatcher("/bracket3.jsp").forward(request, response);
		//request.getRequestDispatcher("").forward(request, response);
	}

}
