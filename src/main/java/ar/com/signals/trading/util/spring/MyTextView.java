package ar.com.signals.trading.util.spring;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyTextView extends AbstractView{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();		
		out.print(mapper.writeValueAsString(model));
		out.flush();
	}

}
