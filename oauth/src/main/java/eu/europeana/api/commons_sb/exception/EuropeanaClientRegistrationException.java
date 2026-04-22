package eu.europeana.api.commons_sb.exception;

import eu.europeana.api.commons_sb.definitions.oauth.KeyValidationResult;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class EuropeanaClientRegistrationException extends ClientRegistrationException {

  KeyValidationResult result;

  public KeyValidationResult getResult() {
    return result;
  }

  public EuropeanaClientRegistrationException(String msg, KeyValidationResult result){
    super(msg);
    this.result = result;
  }

}