package eu.europeana.api.commons_sb3.definitions.search;

import java.util.Map;

public interface FacetFieldView {

	public String getName();

	public Map<String, Long> getValueCountMap();
}