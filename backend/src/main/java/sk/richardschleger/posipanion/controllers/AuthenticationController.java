package sk.richardschleger.posipanion.controllers;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.impl.DefaultClaims;
import sk.richardschleger.posipanion.configs.JwtTokenUtil;
import sk.richardschleger.posipanion.entities.FcmToken;
import sk.richardschleger.posipanion.entities.LoginCode;
import sk.richardschleger.posipanion.entities.User;
import sk.richardschleger.posipanion.models.FCMTokenModel;
import sk.richardschleger.posipanion.models.GoogleAuthenticationRequestModel;
import sk.richardschleger.posipanion.models.JwtRequest;
import sk.richardschleger.posipanion.models.JwtResponse;
import sk.richardschleger.posipanion.models.LoginCodeModel;
import sk.richardschleger.posipanion.models.RegistrationModel;
import sk.richardschleger.posipanion.services.FcmTokenService;
import sk.richardschleger.posipanion.services.GoogleAuthenticateService;
import sk.richardschleger.posipanion.services.JwtUserDetailsService;
import sk.richardschleger.posipanion.services.LoginCodeService;
import sk.richardschleger.posipanion.services.UserDetailsService;
import sk.richardschleger.posipanion.services.UserService;



@RestController
@CrossOrigin
public class AuthenticationController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
	private UserDetailsService myUserDetailsService;

	@Autowired
	private LoginCodeService loginCodeService;

	@Autowired
	private FcmTokenService fcmTokenService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		logger.info("Authenticating user: " + authenticationRequest.getUsername());

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		logger.info("User: " + authenticationRequest.getUsername() + " authenticated successfully");

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

		logger.info("Token refreshed successfully");

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

		logger.info("Authenticating user through Google");

		User user = googleAuthenticateService.authenticate(model.getIdToken());
		if(user != null){

			final UserDetails userDetails = userDetailsService
			.loadUserByUsername(user.getEmail());

			final String token = jwtTokenUtil.generateToken(userDetails);

			logger.info("User: " + user.getEmail() + " authenticated successfully");

			return ResponseEntity.ok(new JwtResponse(token));
		}else{

			return ResponseEntity.status(606).body("Invalid login");

		}
	}

	@PostMapping("/fcmtoken")
	public ResponseEntity<?> saveFCMToken(@RequestBody FCMTokenModel model){
		
		logger.info("Saving FCM token");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

		if(currentUser != null){

			FcmToken token = fcmTokenService.getFcmTokenByTokenAndUserId(model.getToken(), currentUser.getId());
			if(token != null){
				logger.info("Token already exists");
				return ResponseEntity.ok(new JwtResponse("OK"));
			}

			token = new FcmToken();
			token.setToken(model.getToken());
			token.setUser(currentUser);

			fcmTokenService.save(token);

			logger.info("Token saved successfully");
			return ResponseEntity.ok(new JwtResponse("OK"));
		}else{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtResponse(null));
		}
		
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationModel registrationModel){
		
		logger.info("Registering user: " + registrationModel.getEmail());

		User user = userService.getUserByEmail(registrationModel.getEmail());

		if(user != null){
			logger.info("User already exists");
			return ResponseEntity.status(601).body("User with this email already exists!");
		}

		user = new User();
		user.setEmail(registrationModel.getEmail());
		user.setPassword(passwordEncoder.encode(registrationModel.getPassword()));
		userService.save(user);

		sk.richardschleger.posipanion.entities.UserDetails details = new sk.richardschleger.posipanion.entities.UserDetails();
		details.setFirstName(registrationModel.getName());
		details.setSurname(registrationModel.getSurname());
		details.setUser(user);
		myUserDetailsService.saveUserDetails(details);

		logger.info("User registered successfully");

		return ResponseEntity.ok("OK");
	}

	@GetMapping("/generatecode")
	public ResponseEntity<?> generateCode(){
		
		logger.info("Generating code");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUserByEmail(authentication.getName());

		LoginCode loginCode = loginCodeService.getLoginCodeByUserId(currentUser.getId());
		if(loginCode != null){
			if(loginCode.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))){
				currentUser.setLoginCode(null);
				loginCodeService.removeLoginCode(loginCode);
			}else{
				logger.info("Code already exists");
			
				LoginCodeModel model = new LoginCodeModel();
				model.setCode(loginCode.getCode());
				model.setExpiresAt(loginCode.getExpiresAt());

				return ResponseEntity.ok(model);
			}
		}

		String code;
		do {
			code = ((int) (Math.random() * 1000000)) + "";
			while(code.length() < 6){
				code = "0" + code;
			}
		}while (loginCodeService.getLoginCodeByCode(code) != null);

		Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 3));

		loginCode = new LoginCode();
		loginCode.setCode(code);
		loginCode.setExpiresAt(expiresAt);
		loginCode.setUser(currentUser);
		loginCodeService.saveLoginCode(loginCode);

		logger.info("Code generated successfully");

		LoginCodeModel model = new LoginCodeModel();
		model.setCode(code);
		model.setExpiresAt(expiresAt);

		return ResponseEntity.ok(model);
	}

	@PostMapping("/verifycode")
	public ResponseEntity<?> verifyCode(@RequestBody LoginCodeModel loginCodeModel){
		
		logger.info("Verifying code");

		LoginCode loginCode = loginCodeService.getLoginCodeByCode(loginCodeModel.getCode());
		if(loginCode.getExpiresAt().after(new Timestamp(System.currentTimeMillis()))){
			
			final UserDetails userDetails = userDetailsService
			.loadUserByUsername(loginCode.getUser().getEmail());

			final String token = jwtTokenUtil.generateToken(userDetails);

			loginCode.getUser().setLoginCode(null);
			loginCodeService.removeLoginCode(loginCode);

			return ResponseEntity.ok(new JwtResponse(token));
		}else{
			loginCode.getUser().setLoginCode(null);
			loginCodeService.removeLoginCode(loginCode);
			return ResponseEntity.status(602).body("Code expired");
		}
		
		
	}

}