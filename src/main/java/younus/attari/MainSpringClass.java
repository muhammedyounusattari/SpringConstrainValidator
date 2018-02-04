package younus.attari;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
@Configuration
public class MainSpringClass {

	public static void main(String arg[]) {
		SpringApplication.run(MainSpringClass.class, arg);
	}

	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
}

@Controller
class ControllerClass {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getRegistrationForm(Model model) {
		model.addAttribute("register", new Register());// this is used to bind
		return "/register";
	}

	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String getRegistered(@ModelAttribute(value = "register") @Valid Register register,
			BindingResult result, Model mode) {

		if (result.hasErrors()) {
			return "/register";
		}
		
		return "redirect:/sucess";
		
	}
	
	@InitBinder("register")
	public void customValidtion(WebDataBinder binder){
		binder.addValidators(new CustomValidateDOBConstrain());
	}
}

class CustomValidateDOBConstrain implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		System.out.println("from supports...");
		return Register.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Register register = (Register) obj;
		if(register.getFname()==null || register.getFname()=="")	
			errors.rejectValue("fname", null,"Firstname cannot be null");
		if(register.getLname()==null || register.getLname()=="")	
			errors.rejectValue("lname", null,"Lastname cannot be null");
		if(register.getMname()==null || register.getFname()=="")	
			errors.rejectValue("mname",null, "Middlename cannot be null");
		if(register.getDateOfBirth()==null || register.getDateOfBirth()=="")
			errors.rejectValue("dateOfBirth", null, "Please select DOB from field");
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date userDate = sdf.parse(register.getDateOfBirth());
			Date currentDate = new Date(System.currentTimeMillis());
			currentDate.setYear(currentDate.getYear() - 18);
			if(userDate.compareTo(currentDate) > 0)
				errors.rejectValue("dateOfBirth",null, "You should be older than 18 years");
		} catch (ParseException e) {
			//e.printStackTrace();
		}
	}
	
}


class Register {

	
	private String fname;
	private String lname;
	private String mname;
	private String dateOfBirth;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}