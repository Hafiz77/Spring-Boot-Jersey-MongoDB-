package com.test.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.test.utility.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Hafiz on 4/19/2016.
 */
public class SpringDataDBUtils {


    private static MongoOperations mongoOperation;
    private final static Properties properties = new Properties();
    private final static Logger logger     = LoggerFactory.getLogger(SpringDataDBUtils.class);


    public static MongoOperations getMongoOperations() throws Exception {

        if( mongoOperation==null){

            logger.info("Connecting to db ... ");

            MongoClientURI uri = new MongoClientURI(getDatabaseURI()+getDatabaseName());
            MongoClient client = new MongoClient(uri);

            mongoOperation = new MongoTemplate(client, getDatabaseName());
            logger.info("Connected to db : "+  getDatabaseName());

        }
        return mongoOperation;

    }

    protected static String getDatabaseName() {

        try {
            InputStream inputStream = SpringDataDBUtils.class.getClassLoader()
                    .getResourceAsStream(AppConstant.PROPERTIES_FILE);
            properties.load(inputStream);

        } catch (IOException e) {

            logger.error("Error:"+e.getMessage());
        }

        return properties.getProperty(AppConstant.PROPERTIES_DB_NAME);
    }


    protected static  String getDatabaseURI() {

        try {
            InputStream inputStream = SpringDataDBUtils.class.getClassLoader().getResourceAsStream(AppConstant.PROPERTIES_FILE);
            properties.load(inputStream);

        } catch (IOException e) {
            logger.error("Error:"+e.getMessage());
        }

        String dbURI = "mongodb://"+  properties.getProperty(AppConstant.PROPERTIES_DB_USER) +
                ":" + properties.getProperty(AppConstant.PROPERTIES_DB_PASSWORD)   +
                "@" + properties.getProperty(AppConstant.PROPERTIES_DB_IP)      +
                ":" + properties.getProperty(AppConstant.PROPERTIES_DB_PORT)      + "/";

        logger.info(dbURI);

        return dbURI;
    }

}
