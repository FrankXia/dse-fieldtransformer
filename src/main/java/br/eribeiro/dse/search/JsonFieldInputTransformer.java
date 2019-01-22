package br.eribeiro.dse.search;

import com.datastax.bdp.search.solr.FieldInputTransformer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.lucene.document.Document;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonFieldInputTransformer extends FieldInputTransformer
{
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonFieldInputTransformer.class);

  @Override
  public boolean evaluate(String field)
  {
    return field.equals("json");
  }

  @Override
  public void addFieldToDocument(SolrCore core,
                                 IndexSchema schema,
                                 String key,
                                 Document doc,
                                 SchemaField fieldInfo,
                                 String fieldValue,
                                 DocumentHelper helper)
      throws IOException
  {
    try
    {
      ObjectMapper mapper = new ObjectMapper();

      LOGGER.info("JsonFieldInputTransformer called");
      LOGGER.info("fieldValue: " + fieldValue);

      City city = mapper.readValue(fieldValue, City.class);
      SchemaField jsonCity = core.getLatestSchema().getFieldOrNull("city");
      SchemaField jsonState = core.getLatestSchema().getFieldOrNull("state");
      SchemaField jsonCountry = core.getLatestSchema().getFieldOrNull("country");

      helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, jsonCity, city.getCity());
      helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, jsonState, city.getState());
      helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, jsonCountry, city.getCountry());
    }
    catch (Exception ex)
    {
      LOGGER.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
  }
}
