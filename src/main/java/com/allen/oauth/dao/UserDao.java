package com.allen.oauth.dao;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.allen.oauth.common.dao.BaseDao;
import com.allen.oauth.model.User;

@Repository
public class UserDao extends BaseDao<User, ObjectId> {

	@Autowired
	private MongoTemplate mongoTemplate;

	public User findUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("openId").is(openId));
		return mongoTemplate.findOne(query, User.class, "users");
	}

	public void updateOpenName(User persistenceUser) {
		// TODO Auto-generated method stub
		Query query = new Query();
		query.addCriteria(Criteria.where("openId").is(persistenceUser.getOpenId()));
		Update update = new Update();
		update.set("nickname", persistenceUser.getName());
		mongoTemplate.updateFirst(query, update, "users");
	}

	public User saveOpenUser(User user) {
		// TODO Auto-generated method stub
		super.insert(user);
		return user;
	}

	@Autowired
	@Override
	public void setInit() {
		// TODO Auto-generated method stub
		setMongoTemplate(mongoTemplate);
		setTclass(User.class);
		setCollectionName("users");
	}

}
