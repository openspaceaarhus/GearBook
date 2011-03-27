package gb.osaa.dk;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class NewGearHandler extends AbstractHandler {

	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		if (!request.getPathInfo().equals("/blarg.xml")) {
			return;
		}

		response.setContentType("text/xml;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);
	    baseRequest.setHandled(true);
	    
	    //response.getWriter().println("<voters>");
	}

}
