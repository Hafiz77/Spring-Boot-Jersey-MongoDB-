package com.test.services;

import com.test.dao.SpringDataDBUtils;
import com.test.entity.User;
import com.test.utility.AppConstant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hafiz on 4/19/2016.
 */

@Service
@Repository
public class UserService {


    public Object create(User user) throws Exception {
        SpringDataDBUtils.getMongoOperations().insert(user);

        return user;
    }

    public User get(String userId) throws Exception {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));

        return SpringDataDBUtils.getMongoOperations().findOne(query, User.class);
    }

    public User update(User user) throws Exception {

        SpringDataDBUtils.getMongoOperations().save(user);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(new ObjectId(user.getId())));
        User updatedUserInfo = SpringDataDBUtils.getMongoOperations().findOne(query, User.class);
        return updatedUserInfo;

    }

    public List<User> list(Integer offset, Integer limit) throws Exception {

        List<User> users = new ArrayList<User>();
        Query query = new Query();

        if (limit != null && offset != null) {
            limit = limit > 0 ? limit : AppConstant.RECORDS_LIMIT;
            offset = offset > 0 ? offset : AppConstant.RECORDS_OFFSET;
        }


        query.limit(limit);
        query.skip(offset);

        users = SpringDataDBUtils.getMongoOperations().find(query, User.class);

        return users;
    }


    public boolean delete(String userId) throws Exception {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        User u =  SpringDataDBUtils.getMongoOperations().findAndRemove(query, User.class);

        return (u != null);
    }
}
