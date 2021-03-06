# dse-fieldtransformer
Field input transformer for substring indexing

1. Create a table with DseClientTester program with default FAA schema. 

2. Update the table's Solr index schema and configuration with 2 XML files included in this project.

    In the samples folder, we have included 2 XML files for configuring a FAA table with components required for supporting the field input transformer. 
    The two XML files can be dowloanded from Solr HTTP ending point at http://localhost:8983/solr/#/esri_ds_data.faa/files?file=solrconfig.xml and 
    http://localhost:8983/solr/#/esri_ds_data.faa/files?file=schema.xml  

    In the solrconfig.xml file, a new entry of "fieldInputTransformer" is added that points to the field input transformer class. We can update
the solr configuration with following command/request, 

    `curl http://dse1:8983/solr/resource/esri_ds_data.faa/solrconfig.xml --data-binary @solrconfig.xml -H 'Content-type:text/xml; charset=utf-8'`

    In the schema.xml file, all substring fields are added with docValues set to true since we will need each sub-field for terms faceting. We can update
the solr index schema with following command/request,  

    `curl http://dse1:8983/solr/resource/esri_ds_data.faa/schema.xml --data-binary @schema.xml -H 'Content-type:text/xml; charset=utf-8'`

    The reload solr index schema via CQL shell (not sure if this is necessary step though)
    
3. To manually install the transformer for each DSE node. SSH into each node and then issue following commands (
you need to install the `git` and `maven` programs if you haven't done it yet)

    - install required libs to the local maven repo
    
    `mvn install:install-file -Dfile=/usr/share/dse/dse-search-6.7.0.jar -DgroupId=com.datastax -DartifactId=dse-search -Dversion=6.7.0 -Dpackaging=jar`
    
    `mvn install:install-file -Dfile=/usr/share/dse/solr/lib/solr-uber-with-auth_2.1-6.0.1.2.2356.jar -DgroupId=com.datastax -DartifactId=dse-solr -Dversion=6.7.0 -Dpackaging=jar`

    - download the project and build it

    `git clone https://github.com/FrankXia/dse-fieldtransformer.git`
    
    `mvn clean instal`
 
    - copy the jar file to the Solr lib folder 

    `sudo cp target/datastore-dse-field-transformer-0.10.16.jar /usr/share/dse/solr/lib/`

4. Follow the instructions from Spark testing program at https://github.com/david618/sparktest to load the simulated FAA flight data with a command similar to this one

    `java -cp target/sparktest.jar org.jennings.estest.SendKafkaTopicCassandraPlanesHashGlobalObjectIds local[8] 1000 a4:9092 faa faa 1 dse1 1 false true true false esri_ds_data faa`

