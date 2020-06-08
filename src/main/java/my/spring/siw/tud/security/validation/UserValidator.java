package my.spring.siw.tud.security.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import my.spring.siw.tud.model.Utente;

@Component
public class UserValidator implements Validator{

	private static final Integer NAMES_MAX_LENGHT = 32;
	private static final Integer NAMES_MIN_LENGHT = 2;
	//private static final Integer PASS_MIN_LENGHT = 4;
	//private static final Integer PASS_MAX_LENGHT = 32;


	@Override
	public void validate(Object o, Errors errors) {
		Utente user = (Utente) o;
		String name = user.getName();
		String surname = user.getSurname();
		
		if(isBlank(name))
			errors.rejectValue("name", "required");
		else if(name.length()<NAMES_MIN_LENGHT || name.length()> NAMES_MAX_LENGHT)
			errors.rejectValue("name", "size");
		
		if(isBlank(surname))
			errors.rejectValue("surname", "required");
		else if(surname.length()<NAMES_MIN_LENGHT || surname.length()> NAMES_MAX_LENGHT)
			errors.rejectValue("surname", "size");
	}


	@Override
	public boolean supports(Class<?> clazz) {
		return Utente.class.equals(clazz);
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
