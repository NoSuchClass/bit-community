package com.bit.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.bit.community.dao.CommentDao;
import com.bit.community.model.Comment;

@Service
public class CommentService {
	@Autowired
	CommentDao commentDao;
	@Autowired
	WordFilterService wordFilterService;
	
	public List<Comment> getCommentByEntity(int entityId, int entityType) {
		return commentDao.selectCommentById(entityId, entityType);
	}
	
	public boolean deleteComment(int entityId, int status) {
		return commentDao.deleteComment(entityId, status);
	}
	
	public int addComment(Comment comment) {
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		comment.setContent(wordFilterService.filter(comment.getContent()));
		return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
	}
	
	public int getCommentCount(int entityId, int entityType) {
		return commentDao.getCommentCount(entityId, entityType);
	}
	
	public Comment getCommentById(int id) {
		return commentDao.getCommentById(id);
	}

	public int getUserCommentCount(Integer id) {
		return commentDao.getUserCommentCount(id);
	}
}
