package com.nickvanhoof.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.nickvanhoof.dao.MessageDao;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class DynamoDBRepository {

    private static final String REGION = "eu-west-1";


    private AmazonDynamoDB amazonDynamoDBClient;
    private DynamoDBMapper dynamoDBMapper;

    public DynamoDBRepository(){
        amazonDynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(REGION)
                .build();
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
    }

    public Optional<List<MessageDao>> scanMessageTable(){
        List<MessageDao> scanResult = dynamoDBMapper.scan(MessageDao.class, new DynamoDBScanExpression().withLimit(100));
        return Optional.ofNullable(scanResult);
    }

    public Optional<List<MessageDao>> quickPickMessageTable(){
        List<MessageDao> scanResult = dynamoDBMapper.scan(MessageDao.class, new DynamoDBScanExpression().withLimit(1));
        return Optional.ofNullable(scanResult);
    }

    public void saveMessage(MessageDao messageDao){
        DynamoDBMapperConfig dynamoDBMapperConfig = new DynamoDBMapperConfig.Builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.PUT)
                .build();
        dynamoDBMapper.save(messageDao, dynamoDBMapperConfig);
    }

    public Optional<MessageDao> getOneMessageById(String id){
        MessageDao messageDao = new MessageDao.Builder().id(id).build();
        DynamoDBQueryExpression<MessageDao> queryExpression = new DynamoDBQueryExpression<MessageDao>()
                .withHashKeyValues(messageDao)
                .withConsistentRead(false)
                .withLimit(1);

        List<MessageDao> queryResult = dynamoDBMapper.query(MessageDao.class, queryExpression);
        if(queryResult.isEmpty()){
            log.info("No result found using inconsistent read");
            log.info("Trying a consistent read");
            queryExpression = new DynamoDBQueryExpression<MessageDao>()
                    .withHashKeyValues(messageDao)
                    .withConsistentRead(true)
                    .withLimit(1);
            queryResult = dynamoDBMapper.query(MessageDao.class, queryExpression);
        }
        return queryResult.stream().findAny();
    }
}