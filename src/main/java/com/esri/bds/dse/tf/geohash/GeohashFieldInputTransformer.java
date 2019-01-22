package com.esri.bds.dse.tf.geohash;

import com.datastax.bdp.search.solr.FieldInputTransformer;
import org.apache.lucene.document.Document;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GeohashFieldInputTransformer extends FieldInputTransformer
{
  private static final Logger LOGGER = LoggerFactory.getLogger(GeohashFieldInputTransformer.class);

  @Override
  public boolean evaluate(String field)
  {
    return field.equals("esri_geohash_square_102100_30");
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
      LOGGER.info("GeohashFieldInputTransformer called");
      LOGGER.info("fieldValue: " + fieldValue);

      String fieldName = fieldInfo.getName();
      String fieldNamePrfix = fieldName.substring(0, fieldName.lastIndexOf("_"));
      int length = fieldValue.length();
      for (int index = 1; index < length; index++) {
        String subGeohash = fieldValue.substring(0, index);
        SchemaField subGeohashField = core.getLatestSchema().getFieldOrNull(fieldNamePrfix+"_" + index);
        helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, subGeohashField, subGeohash);
      }
      helper.addFieldToDocument(core, core.getLatestSchema(), key, doc, fieldInfo, fieldValue);
    }
    catch (Exception ex)
    {
      LOGGER.error(ex.getMessage());
      throw new RuntimeException(ex);
    }
  }
}
