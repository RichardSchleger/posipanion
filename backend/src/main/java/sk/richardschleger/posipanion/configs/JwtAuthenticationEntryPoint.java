package sk.richardschleger.posipanion.configs;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
	
			Exception exception = (Exception) request.getAttribute("exception");
			ExpiredJwtException expiredJwtException = (ExpiredJwtException) request.getAttribute("expiredexception");
	
			String message;
	
			if (exception != null) {
	
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);

				byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));
	
				response.getOutputStream().write(body);
	
			} else if (expiredJwtException != null) {
				
				response.setStatus(606);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", expiredJwtException.toString()));

				response.getOutputStream().write(body);

			}else {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	
				if (authException.getCause() != null) {
					message = authException.getCause().toString() + " " + authException.getMessage();
				} else {
					message = authException.getMessage();
				}
	
				byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));
	
				response.getOutputStream().write(body);
			}
	}
}