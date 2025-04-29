package eu.europeana.api.commons_sb3.error;

import org.springframework.http.HttpStatus;

public class EuropeanaI18nApiException extends EuropeanaApiException {
  
  private static final long serialVersionUID = 97524780344752123L;
	
  private String i18nKey;
	
  private String[] i18nParams;

    public EuropeanaI18nApiException(ErrorMessage errorMessage, String[] i18nParams){
        super(null, errorMessage.getError(), errorMessage.getCode());
        this.i18nKey = errorMessage.getI18nKey();
        this.i18nParams = i18nParams;
    }

  public EuropeanaI18nApiException(String message, String errorCode, String error,  String i18nKey, String[] i18nParams){
	super(message, error, errorCode);
	this.i18nKey = i18nKey;
	this.i18nParams = i18nParams;
  }


  
  public EuropeanaI18nApiException(String message, String errorCode, String error, HttpStatus responseStatus, String i18nKey, String[] i18nParams){
    super(message, error, errorCode);
    super.setResponseStatus(responseStatus);
    this.i18nKey = i18nKey;
    this.i18nParams = i18nParams;
  }
	
  public EuropeanaI18nApiException(String message, String errorCode, String error,HttpStatus responseStatus, String i18nKey, String[] i18nParams, Throwable th){
	super(message, error, errorCode, th);
	super.setResponseStatus(responseStatus);
	this.i18nKey = i18nKey;
	this.i18nParams = i18nParams;
  }

  public String getI18nKey() {
    return i18nKey;
  }

  void setI18nKey(String i18nKey) {
	this.i18nKey = i18nKey;
  }

  public String[] getI18nParams() {
	return i18nParams;
  }

  void setI18nParams(String[] i18nParams) {
	this.i18nParams = i18nParams;
  }
  
  @Override
  public String getErrorCode() {
    //fallback error code on internationalization key if not set explicitly 
    return super.getErrorCode() != null ? super.getErrorCode() : getI18nKey() ;
  }
}
