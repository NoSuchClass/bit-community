package com.bit.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.bit.community.dao.QuestionDao;
import com.bit.community.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDao questionDao;
	@Autowired
	WordFilterService wordFilterService;

	public List<Question> getLatestQuestion(int offset, int limit) {
		return questionDao.selectLatestQuestion(offset, limit);
	}

	public List<Question> getLatestQuestion(int userId, int offset, int limit) {
		return questionDao.selectLatestQuestionById(userId, offset, limit);
	}
	
	public int addQuestion(Question question) {
		// 对html标签进行转义
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		question.setContent(wordFilterService.filter(question.getContent()));
		question.setTitle(wordFilterService.filter(question.getTitle()));
		return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
	}

	public Question getQuestionDetail(int qid) {
		return questionDao.selectQuestionById(qid);
	}
	
	public boolean updateCommentCount(int id, int count) {
		return questionDao.updateCommentCount(id, count);
	}	
	
	public Question getQuestionById(int id) {
		return questionDao.selectQuestionById(id);
	}
}
