package eu.europeana.api.commons_sb3.definitions.oauth.exception;

public class DateParsingException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = 4290381096959914992L;

  public DateParsingException(Throwable th) {
    super("Date parsing error occured!", th);
  }
}