package eu.europeana.api.commons_sb3.exception;

import eu.europeana.api.commons_sb3.definitions.oauth.KeyValidationResult;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public class EuropeanaClientRegistrationException extends ClientRegistrationException {

  KeyValidationResult result;

  public KeyValidationResult getResult() {
    return result;
  }

  public EuropeanaClientRegistrationException(String msg,KeyValidationResult result){
    super(msg);
    this.result = result;
  }

}