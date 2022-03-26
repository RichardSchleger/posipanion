package sk.richardschleger.posipanion.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.impl.DefaultClaims;
import sk.richardschleger.posipanion.configs.JwtTokenUtil;
import sk.richardschleger.posipanion.entities.FcmToken;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.models.FCMTokenModel;
import sk.richardschleger.posipanion.models.GoogleAuthenticationRequestModel;
import sk.richardschleger.posipanion.models.JwtRequest;
import sk.richardschleger.posipanion.models.JwtResponse;
import sk.richardschleger.posipanion.services.FcmTokenService;
import sk.richardschleger.posipanion.services.GoogleAuthenticateService;
import sk.richardschleger.posipanion.services.JwtUserDetailsService;
import sk.richardschleger.posipanion.services.UserService;



@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private GoogleAuthenticateService googleAuthenticateService;

	@Autowired
	private UserService userService;

	@Autowired
	private FcmTokenService fcmTokenService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtTokenUtil.doGenerateToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	@PostMapping("/google/authenticate")
	public ResponseEntity<?> createAuthenticationTokenGoogle(@RequestBody GoogleAuthenticationRequestModel model){

		User user = googleAuthenticateService.authenticate(model.getIdToken());
		if(user != null){

			final UserDetails userDetails = userDetailsService
			.loadUserByUsername(user.getEmail());

			final String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseEntity.ok(new JwtResponse(token));
		}else{

			return ResponseEntity.status(606).body("Invalid login");

		}
	}

	@PostMapping("/fcmtoken")
	public ResponseEntity<?> saveFCMToken(@RequestBody FCMTokenModel model){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

		if(currentUser != null){

			FcmToken token = fcmTokenService.getFcmTokenByTokenAndUserId(model.getToken(), currentUser.getId());
			if(token != null){
				return ResponseEntity.ok(new JwtResponse("OK"));
			}

			token = new FcmToken();
			token.setToken(model.getToken());
			token.setUser(currentUser);

			fcmTokenService.save(token);

			return ResponseEntity.ok(new JwtResponse("OK"));
		}else{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtResponse(null));
		}
		
	}
}