package eu.europeana.api.commons_sb3.definitions.search.result;

import eu.europeana.api.commons_sb3.definitions.search.FacetFieldView;
import eu.europeana.api.commons_sb3.definitions.vocabulary.CommonApiConstants;
import org.apache.stanbol.commons.jsonld.JsonLd;
import org.apache.stanbol.commons.jsonld.JsonLdProperty;
import org.apache.stanbol.commons.jsonld.JsonLdPropertyValue;
import org.apache.stanbol.commons.jsonld.JsonLdResource;

import java.util.Map;
import java.util.TreeMap;
import static eu.europeana.api.commons_sb3.definitions.vocabulary.CommonLdConstants.*;

public abstract class ResultsPageSerializer<T> extends JsonLd {

	ResultsPage<T> resultsPage;
	String contextValue;
	String type;

	public ResultsPageSerializer(ResultsPage<T> resPage) {
		this(resPage, null, null);
	}

	public ResultsPageSerializer(ResultsPage<T> resPage, String contextValue, String type) {
		this.resultsPage = resPage;
		this.contextValue = contextValue;
		this.type = type;
		registerContainerProperty(CommonApiConstants.SEARCH_RESP_FACETS);
		registerContainerProperty(CommonApiConstants.SEARCH_RESP_FACETS_VALUES);
	}

	public ResultsPage<T> getResultsPage() {
		return resultsPage;
	}

	public void setResultsPage(ResultsPage<T> resultsPage) {
		this.resultsPage = resultsPage;
	}

	public void setContextValue(String contextValue) {
		this.contextValue = contextValue;
	}

	protected String getContextValue() {
		return this.contextValue;
	}

	public String serialize(String profile) {

		setUseTypeCoercion(false);
		setUseCuries(false);
		setApplyNamespaces(false);
		// if(isApplyNamespaces())
		// setUsedNamespaces(namespacePrefixMap);

		JsonLdResource jsonLdResource = new JsonLdResource();
		jsonLdResource.setSubject("");
		jsonLdResource.putProperty(context, getContextValue());
		// annotation page
		if (resultsPage.getCurrentPageUri() != null)
			jsonLdResource.putProperty(ID, resultsPage.getCurrentPageUri());
		jsonLdResource.putProperty(TYPE, getType());
		jsonLdResource.putProperty(TOTAL, resultsPage.getTotalInPage());

		// collection
		if (resultsPage.getCurrentPageUri() != null) {
			JsonLdProperty collectionProp = new JsonLdProperty(PART_OF);
			JsonLdPropertyValue collectionPropValue = new JsonLdPropertyValue();
			collectionPropValue.putProperty(new JsonLdProperty(ID, resultsPage.getCollectionUri()));
			collectionPropValue.putProperty(new JsonLdProperty(TYPE, RESULT_LIST));
			collectionPropValue
					.putProperty(new JsonLdProperty(TOTAL, resultsPage.getTotalInCollection()));
			collectionProp.addValue(collectionPropValue);
			jsonLdResource.putProperty(collectionProp);
		}

		// items
		serializeItems(jsonLdResource, profile);
		serializeFacets(jsonLdResource, profile);

		// nagivation
		if (resultsPage.getPrevPageUri() != null)
			jsonLdResource.putProperty(PREV, resultsPage.getPrevPageUri());
		if (resultsPage.getNextPageUri() != null)
			jsonLdResource.putProperty(NEXT, resultsPage.getNextPageUri());

		put(jsonLdResource);

		return toString(4);
	}

	protected void serializeFacets(JsonLdResource jsonLdResource, String profile) {
		if (getResultsPage() == null || getResultsPage().getFacetFields() == null
				|| getResultsPage().getFacetFields().isEmpty())
			return;

		JsonLdProperty facetsProperty = new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS);

		for (FacetFieldView view : getResultsPage().getFacetFields())
			facetsProperty.addValue(buildFacetPropertyValue(view));

		if (facetsProperty.getValues() != null && !facetsProperty.getValues().isEmpty())
			jsonLdResource.putProperty(facetsProperty);

	}

	JsonLdPropertyValue buildFacetPropertyValue(FacetFieldView view) {

		JsonLdPropertyValue facetViewEntry = new JsonLdPropertyValue();

		facetViewEntry.putProperty(new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS_FIELD, view.getName()));

        JsonLdProperty values = new JsonLdProperty(CommonApiConstants.SEARCH_RESP_FACETS_VALUES);
		// only if values for facet count are available
		if (view.getValueCountMap() != null && !view.getValueCountMap().isEmpty()) {
			JsonLdPropertyValue labelCountValue;
			Map<String, String> valueMap;

			for (Map.Entry<String, Long> valueCount : view.getValueCountMap().entrySet()) {
				labelCountValue = new JsonLdPropertyValue();
				valueMap = new TreeMap<String, String>();
				valueMap.put(CommonApiConstants.SEARCH_RESP_FACETS_LABEL, valueCount.getKey());
				valueMap.put(CommonApiConstants.SEARCH_RESP_FACETS_COUNT, valueCount.getValue().toString());
				labelCountValue.setValues(valueMap);

				values.addValue(labelCountValue);
			}
		}
        facetViewEntry.putProperty(values);

		return facetViewEntry;
	}

	protected abstract void serializeItems(JsonLdResource jsonLdResource, String profile);

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
