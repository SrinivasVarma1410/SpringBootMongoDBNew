package com.example.springbootmongo.dal;

import com.example.springbootmongo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserDALLImpl implements UserDAL {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public User getUserById(String userId) {
        Query query =  new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query,  User.class);
    }

    @Override
    public User addNewUser(User user) {
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public Object getAllUserSettings(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        return null != user ? user.getUserSettings() : "User not found.";
    }

    @Override
    public String getUserSetting(String userId, String key) {
        Query query = new Query();
        query.fields().include("userSettings");
        query.addCriteria(Criteria.where("userId").is(userId).andOperator(Criteria.where("userSettings." + key).exists(true)));
        User user =  mongoTemplate.findOne(query, User.class);
        return null != user ? user.getUserSettings().get(key) : "User not found.";
    }

    @Override
    public String addUserSetting(String userId, String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        if (null != user) {
            user.getUserSettings().put(key, value);
            mongoTemplate.save(user);
            return "key value added.";
        } else {
            return "User not found.";
        }
    }
}
