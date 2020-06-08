package my.spring.siw.tud.security.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import my.spring.siw.tud.model.Credentials;
import my.spring.siw.tud.modelServices.CredentialsService;


@Component
public class CredentialsValidator implements Validator {
	
	private static final Integer USERNAME_MAX_LENGHT = 32;
	private static final Integer USERNAME_MIN_LENGHT = 2;
	private static final Integer PASS_MIN_LENGHT = 4;
	private static final Integer PASS_MAX_LENGHT = 32;
	
	@Autowired
	private CredentialsService credService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Credentials credentials = (Credentials) target;
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		
		if(isBlank(username))
			errors.rejectValue("username", "required");
		else if (username.length()<USERNAME_MIN_LENGHT || username.length()> USERNAME_MAX_LENGHT)
			errors.rejectValue("username", "size");
		else if(this.credService.retrieveCredentialsByUsername(username) != null)
			errors.rejectValue("username", "duplicato");
		
		if(isBlank(password))
			errors.rejectValue("password", "required");
		else if (password.length()<PASS_MIN_LENGHT || password.length()> PASS_MAX_LENGHT)
			errors.rejectValue("password", "size");		
	}
	
	private static boolean isBlank(String string) {
		boolean itIs = true;
		char[] s = string.toCharArray();
		char whiteSpace[] = " ".toCharArray();
		for(int i=0; i<s.length; i++) {
			if(!(s[i] == whiteSpace[0]))
		    itIs = false;	    
		}
		return itIs;
	 	}

}
