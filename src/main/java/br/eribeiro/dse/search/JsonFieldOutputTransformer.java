package br.eribeiro.dse.search;

import com.datastax.bdp.search.solr.FieldOutputTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.StoredFieldVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonFieldOutputTransformer extends FieldOutputTransformer
{

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonFieldOutputTransformer.class);

  @Override
  public void stringField(FieldInfo fieldInfo,
                          String value,
                          StoredFieldVisitor visitor,
                          DocumentHelper helper) throws IOException
  {

    ObjectMapper mapper = new ObjectMapper();

    LOGGER.info("name: " + fieldInfo.name + ", value: " + value);
    try
    {
      City city = mapper.readValue(value.getBytes(), City.class);

      FieldInfo json_city_fi = helper.getFieldInfo("city");
      FieldInfo json_state_fi = helper.getFieldInfo("state");
      FieldInfo json_country_fi = helper.getFieldInfo("country");

      if (city.getCity() != null)
      {
        visitor.stringField(json_city_fi, city.getCity().getBytes());
      }
      if (city.getState() != null)
      {
        visitor.stringField(json_state_fi, city.getState().getBytes());
      }
      if (city.getCountry() != null)
      {
        visitor.stringField(json_country_fi, city.getCountry().getBytes());
      }
    }
    catch (IOException e)
    {
      LOGGER.error(fieldInfo.name + " " + e.getMessage());
      throw e;
    }
  }
}
