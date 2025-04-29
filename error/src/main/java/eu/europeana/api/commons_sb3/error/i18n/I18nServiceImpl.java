package eu.europeana.api.commons_sb3.error.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class I18nServiceImpl implements I18nService {
	
	@Autowired
	private MessageSource messageSource;
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public String getMessage(String key, String args[]) throws NoSuchMessageException {
			return messageSource.getMessage(key, args, key, Locale.ENGLISH);
	}

	@Override
	public String getMessage(String key) throws NoSuchMessageException {
		return getMessage(key, null);
	}
}
