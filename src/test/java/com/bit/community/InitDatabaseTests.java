package com.bit.community;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bit.community.dao.QuestionDao;
import com.bit.community.dao.UserDao;
import com.bit.community.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTests {
	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;
	@Test
	public void initDatabase() {
		Random random = new Random();
		for (int i = 2201; i < 1000020; i++) {
			User user = new User();
			user.setId(i);
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("User %d", i));
			user.setPassword("xx");
			user.setSalt("");
			System.out.println(userDao.addUser(user));
//			Question question = new Question();
//			question.setId(i);
//			question.setContent("dsefasedgas"+i);
//			question.setCommentCount(i*23);
//			question.setTitle("aasdga"+i);
//			question.setContent("asedgaedga");
//			Date date = new Date();
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
//			simpleDateFormat.format(date);
//			question.setCreatedDate(date);
//			question.setUserId(i);
//			questionDao.addQuestion(question);
		}
	}
	
}
