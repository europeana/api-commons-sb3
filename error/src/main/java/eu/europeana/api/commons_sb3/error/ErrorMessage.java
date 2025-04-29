package eu.europeana.api.commons_sb3.error;

public enum ErrorMessage {
  PARAM_INVALID_400("400_param_invalid","An invalid parameter was sent in the request",  CommonExceptionI18nConstants.INVALID_PARAM);

  private final String code;
  private final String error;
  private final String i18nKey;

  ErrorMessage(String code, String error, String i18nKey) {
    this.code = code;
    this.error = error;
    this.i18nKey = i18nKey;
  }
  public String getCode() {return code;}
  public String getError() {return error; }
  public String getI18nKey() {return i18nKey;}

}
