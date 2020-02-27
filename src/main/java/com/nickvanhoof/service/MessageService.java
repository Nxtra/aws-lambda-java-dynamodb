package com.nickvanhoof.service;

import com.nickvanhoof.dao.MessageDao;
import com.nickvanhoof.model.Message;
import com.nickvanhoof.repository.DynamoDBRepository;

public class MessageService {

    private static DynamoDBRepository dynamoDBRepository = new DynamoDBRepository();

    static {
        dynamoDBRepository.quickPickMessageTable();
    }

    public MessageDao handleCreateMessageRequest(Message message) {
        MessageDao messageDao = createMessageDao(message);
        dynamoDBRepository.saveMessage(messageDao);
        return messageDao;
    }

    public MessageDao createMessageDao(Message message) {
        MessageDao messageDao = new MessageDao.Builder()
                .author(message.getAuthor())
                .message(message.getMessage())
                .build();
        return  messageDao;
    }


}
