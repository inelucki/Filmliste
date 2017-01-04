package main.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException ,ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("WWW-Authenticate", "Basic realm="+getRealmName()+"");
		
		JSONObject obj = new JSONObject();
		obj.put("statusOK", false);
		obj.put("errormessage", "HTTP Status 401 : "+authException.getMessage());
		obj.write(response.getWriter());
	}
	
	@Override
	public void afterPropertiesSet() throws Exception{
		setRealmName("MAIN");
		super.afterPropertiesSet();
	}
}
