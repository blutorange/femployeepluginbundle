package de.xima.fc.fuerth.employees;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("de.xima.fc.dateFormatValidator")
public class DateFormatValidator implements Validator {

	@Override
	public void validate(final FacesContext fc, final UIComponent ui, final Object value) throws ValidatorException {
		final FacesMessage msg = check(value, fc.getExternalContext().getRequestLocale());
		if (msg != null)
			throw new ValidatorException(msg);
	}

	public static FacesMessage check(final Object value, final Locale locale) {
		if (value == null)
			return null;
		final String dateFormat = String.valueOf(value);
		if (dateFormat.isEmpty())
			return null;
		try {
			@SuppressWarnings("unused")
			final Object o = new SimpleDateFormat(dateFormat, Locale.ROOT);
			return null;
		}
		catch (final IllegalArgumentException e) {
			final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid date format.", e.getMessage());
			return msg;
		}
	}
}
