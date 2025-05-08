package eu.europeana.api.commons_sb3.error.config;

import static eu.europeana.api.commons_sb3.error.config.ErrorConfig.*;

public enum ErrorMessage {
  PARAM_INVALID_400("400_param_invalid","An invalid parameter was sent in the request",  INVALID_PARAM),
  PARAM_MISSING_400("400_param_missing","Mandatory parameter is missing in the request", MISSING_PARAM),
  BODY_INVALID_400 ("400_body",         "An invalid field was sent in the request body", INVALID_BODY),
  ID_INVALID_404   ("404_id_invalid",   "Invalid %s identifier",                         INVALID_ID),
  NOT_FOUND_404    ("404_not_found",    "%s not found!",                                NOT_FOUND);


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
