package eu.europeana.api.commons_sb3.definitions.search.enrich;

import java.util.Optional;

/**
 * Represents a query for enriching text data based on specific parameters.
 */
public record LanguageText (String text, String lang) {

    public LanguageText(String text, String lang) {
        this.text = text;
        this.lang = lang;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String lang() {
        return lang;
    }
}
