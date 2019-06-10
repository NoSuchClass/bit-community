package com.bit.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.community.async.EventModel;
import com.bit.community.async.EventProducer;
import com.bit.community.async.EventType;
import com.bit.community.model.Comment;
import com.bit.community.model.EntityType;
import com.bit.community.model.HostHolder;
import com.bit.community.service.CommentService;
import com.bit.community.service.LikeService;
import com.bit.community.utils.CommunityUtils;

@Controller
public class LikeController {
	@Autowired
	LikeService likeService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	EventProducer eventProducer;
	@Autowired
	CommentService commentService;

	@RequestMapping(path = { "/like" }, method = { RequestMethod.POST })
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}
		Comment comment = commentService.getCommentById(commentId);
		if (comment.getUserId() == hostHolder.getUser().getId()) {
			return CommunityUtils.getJSON(666);
		}
		eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
				.setEntityId(commentId).setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
				.setExt("questionId", String.valueOf(comment.getEntityId())));
		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return CommunityUtils.getJSON(0, String.valueOf(likeCount));
	}

	@RequestMapping(path = { "/dislike" }, method = { RequestMethod.POST })
	@ResponseBody
	public String disLike(@RequestParam("commentId") int commentId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}
		Comment comment = commentService.getCommentById(commentId);
		if (comment.getUserId() == hostHolder.getUser().getId()) {
			return CommunityUtils.getJSON(333);
		}
		long disLikeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return CommunityUtils.getJSON(0, String.valueOf(disLikeCount));
	}
}
