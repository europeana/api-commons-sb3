package eu.europeana.api.commons_sb3.definitions.oauth;

import java.io.Serializable;

public class KeyValidationResult implements Serializable {

  private int httpStatusCode;
  private KeyValidationError validationError;

  public KeyValidationResult(int httpStatusCode, KeyValidationError validationError) {
    this.httpStatusCode = httpStatusCode;
    this.validationError = validationError;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(int httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public KeyValidationError getValidationError() {
    return validationError;
  }

  public void setValidationError(KeyValidationError validationError) {
    this.validationError = validationError;
  }
}