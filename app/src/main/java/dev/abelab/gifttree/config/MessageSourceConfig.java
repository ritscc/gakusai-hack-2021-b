package dev.abelab.gifttree.config;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSourceConfig {

	@Bean
	public MessageSource messageSource(@Value("${spring.messages.basename}") String basename,
		@Value("${spring.messages.encoding}") String encoding) {

		final var messageSource = new YamlMessageSource();

		messageSource.setBasenames(basename);
		messageSource.setDefaultEncoding(encoding);

		return messageSource;
	}

	class YamlMessageSource extends ResourceBundleMessageSource {

		@Override
		protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
			return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
		}

	}

}
