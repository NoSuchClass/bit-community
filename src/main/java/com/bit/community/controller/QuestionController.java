package com.bit.community.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.community.model.Comment;
import com.bit.community.model.EntityType;
import com.bit.community.model.HostHolder;
import com.bit.community.model.Question;
import com.bit.community.model.User;
import com.bit.community.model.ViewObject;
import com.bit.community.service.CommentService;
import com.bit.community.service.LikeService;
import com.bit.community.service.QuestionService;
import com.bit.community.service.UserService;
import com.bit.community.utils.CommunityUtils;

@Controller
public class QuestionController {
	@Autowired
	QuestionService questionService;
	@Autowired
	CommentService commentService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	UserService userService;
	@Autowired
	LikeService likeService;

	@RequestMapping(value = { "/question/add" }, method = { RequestMethod.POST })
	@ResponseBody
	public String addQuestion(@Param("title") String title, @Param("content") String content) {
		try {
			Question question = new Question();
			question.setContent(content);
			question.setTitle(title);
			question.setCommentCount(0);
			question.setCreatedDate(new Date());
			if (hostHolder.getUser() == null) {
				return CommunityUtils.getJSON(999);
			} else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if (questionService.addQuestion(question) > 0) {
				return CommunityUtils.getJSON(0, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommunityUtils.getJSON(1, "失败");
	}

	@RequestMapping(value = { "/question/{qid}" })
	public String getQuestionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.getQuestionDetail(qid);
		model.addAttribute("question", question);
		List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> comments = new ArrayList<>();
		User user = hostHolder.getUser();
		for (Comment comment : commentList) {
			ViewObject vo = new ViewObject();
			if (user == null) {
				vo.set("liked", 0);
			}else {
				int likeStatus = likeService.getLikeStatus(user.getId(), EntityType.ENTITY_COMMENT, comment.getId());
				vo.set("liked", likeStatus);
			}
			vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			
//			vo.set("liked", 0);
//			vo.set("likeCount", 2);
			vo.set("comment", comment);
			vo.set("user", userService.getUserById(comment.getUserId()));
			comments.add(vo);
		}
		// 这儿可以添加一个事务
		int count = commentService.getCommentCount(qid, EntityType.ENTITY_QUESTION);
		questionService.updateCommentCount(qid, count);
		model.addAttribute("comments", comments);
		return "detail";
	}
}
