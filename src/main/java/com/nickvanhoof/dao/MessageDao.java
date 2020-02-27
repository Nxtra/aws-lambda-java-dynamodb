package com.nickvanhoof.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Builder;

import java.util.UUID;

@DynamoDBTable(tableName = "messageTable")
public class MessageDao {
    @DynamoDBHashKey
    String id;
    String message;
    String author;

    public MessageDao(String id, String message, String author) {
        this.id = id;
        this.message = message;
        this.author = author;
    }

    public MessageDao() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static class Builder{
        String id = UUID.randomUUID().toString();
        String message;
        String author;

        public Builder(){}

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Builder message(String message){
            this.message = message;
            return this;
        }

        public Builder author(String author){
            this.author = author;
            return this;
        }

        public MessageDao build(){
            return new MessageDao(this.id, this.message, this.author);
        }

    }

}
