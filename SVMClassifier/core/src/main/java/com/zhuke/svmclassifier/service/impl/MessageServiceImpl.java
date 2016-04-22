package com.zhuke.svmclassifier.service.impl;

import com.zhuke.svmclassifier.entity.Message;
import com.zhuke.svmclassifier.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ZHUKE on 2016/4/22.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Transactional
    public void addMessage(Message message) {
        hibernateTemplate.save(message);
    }
}
