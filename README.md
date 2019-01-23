# dse-fieldtransformer
Field input transformer for substring indexing

In the samples folder, we have included 2 XML files for configuring a FAA table with components required for supporting the field input transformer. 

In the solrconfig.xml file, a new entry of "fieldInputTransformer" is added that points to the field input transformer class. Then we can update
the solr configuraiton with following command/request, 

`curl http://localhost:8983/solr/resource/esri_ds_data.faa/solrconfig.xml --data-binary @solrconfig.xml -H 'Content-type:text/xml; charset=utf-8'`

In the schema.xml file, all substring fields are added with docValues set to true since we will need each sub-field for terms faceting. Then we can update
the solr index schema with following command/request,  

`curl http://localhost:8983/solr/resource/esri_ds_data.faa/schema.xml --data-binary @schema.xml -H 'Content-type:text/xml; charset=utf-8'`