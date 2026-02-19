package eu.europeana.api.commons_sb3.error;

import eu.europeana.api.commons_sb3.error.config.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class EuropeanaI18nApiException extends EuropeanaApiException {
  
  private static final long serialVersionUID = 97524780344752123L;
	
  private String i18nKey;
	
  private List<String> i18nParams;


    public EuropeanaI18nApiException(Class theClass, ErrorMessage errorMessage, List<String> i18nParams){
        super(null, String.format(errorMessage.getError(), theClass.getSimpleName()), errorMessage.getCode());
        this.i18nKey = errorMessage.getI18nKey();
        this.i18nParams = new ArrayList<>();
        this.i18nParams.add(theClass.getSimpleName());
        this.i18nParams.addAll(i18nParams);
    }

    public EuropeanaI18nApiException(ErrorMessage errorMessage, List<String> i18nParams){
        super(null, errorMessage.getError(), errorMessage.getCode());
        this.i18nKey = errorMessage.getI18nKey();
        this.i18nParams = i18nParams;
    }

    public EuropeanaI18nApiException(ErrorMessage errorMessage, List<String> i18nParams, HttpStatus responseStatus, Throwable th){
        super(null, errorMessage.getError(), errorMessage.getCode(), th);
        super.setResponseStatus(responseStatus);
        this.i18nKey = errorMessage.getI18nKey();
        this.i18nParams = i18nParams;
    }


    public EuropeanaI18nApiException(ErrorMessage errorMessage, List<String> i18nParams, HttpStatus responseStatus){
        super(null, errorMessage.getError(), errorMessage.getCode());
        super.setResponseStatus(responseStatus);
        this.i18nKey = errorMessage.getI18nKey();
        this.i18nParams = i18nParams;
    }

  public EuropeanaI18nApiException(String message, String errorCode, String error, String i18nKey, List<String> i18nParams){
	super(message, error, errorCode);
	this.i18nKey = i18nKey;
	this.i18nParams = i18nParams;
  }


  
  public EuropeanaI18nApiException(String message, String errorCode, String error, HttpStatus responseStatus, String i18nKey, List<String> i18nParams){
    super(message, error, errorCode);
    super.setResponseStatus(responseStatus);
    this.i18nKey = i18nKey;
    this.i18nParams = i18nParams;
  }
	
  public EuropeanaI18nApiException(String message, String errorCode, String error,HttpStatus responseStatus, String i18nKey, List<String> i18nParams, Throwable th){
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

  public List<String> getI18nParams() {
	return i18nParams;
  }

  void setI18nParams(List<String> i18nParams) {
	this.i18nParams = i18nParams;
  }
  
  @Override
  public String getErrorCode() {
    //fallback error code on internationalization key if not set explicitly 
    if (super.getErrorCode() != null)  {
        return super.getErrorCode();
    }
    // extract the code from i18nKey
    return i18nKey.contains(".") ? StringUtils.substringAfterLast(i18nKey, ".") : i18nKey;
  }
}
