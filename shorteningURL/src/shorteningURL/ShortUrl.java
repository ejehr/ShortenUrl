package shorteningURL;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShortUrl
 */
@WebServlet("/")
public class ShortUrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String domain = "localhost:8080";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShortUrl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rUrl = domain+request.getRequestURI();
		String dUrl = "";
		try {
			ConvertUrl cu = new ConvertUrl(domain);
			dUrl = cu.getOrgnURL(rUrl);
			if("".equals(dUrl)){
				response.sendRedirect("/");
			}
			else
			{
				response.sendRedirect("http://"+dUrl);
			}
			
		} catch (SQLException e) {
			System.out.println("error : " + e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String oUrl = request.getParameter("O_URL");
		String sUrl = oUrl;
		
		ConvertUrl cu = new ConvertUrl(domain);
		try {
			sUrl = cu.getShortenURL(oUrl);
			
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			request.setAttribute("O_URL", oUrl);
			request.setAttribute("S_URL", sUrl);
			rd.forward(request, response);
		} catch (SQLException e) {
			System.out.println("error : " + e.getMessage());
		}
	}

}
