package com.bit.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit.community.async.EventModel;
import com.bit.community.async.EventProducer;
import com.bit.community.async.EventType;
import com.bit.community.model.EntityType;
import com.bit.community.model.HostHolder;
import com.bit.community.model.Question;
import com.bit.community.model.User;
import com.bit.community.model.ViewObject;
import com.bit.community.service.CommentService;
import com.bit.community.service.FollowService;
import com.bit.community.service.QuestionService;
import com.bit.community.service.UserService;
import com.bit.community.utils.CommunityUtils;

@Controller
public class FollowController {
	@Autowired
	HostHolder hostHolder;
	@Autowired
	FollowService followService;
	@Autowired
	UserService userService;
	@Autowired
	EventProducer eventProducer;
	@Autowired
	QuestionService questionService;
	@Autowired
	CommentService commentService;

	@RequestMapping(path = { "/followUser" }, method = { RequestMethod.POST })
	@ResponseBody
	public String followUser(@RequestParam("userId") int userId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}
		boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
		System.out.println( CommunityUtils.getJSON(ret ? 0 : 1,
				String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())))
);
		return CommunityUtils.getJSON(ret ? 0 : 1,
				String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
	}

	@RequestMapping(path = { "/unfollowUser" }, method = { RequestMethod.POST })
	@ResponseBody
	public String unFollowUser(@RequestParam("userId") int userId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}
		boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
		return CommunityUtils.getJSON(ret ? 0 : 1,
				String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
	}

	@RequestMapping(path = { "/followQuestion" }, method = { RequestMethod.POST })
	@ResponseBody
	public String followQuestion(@RequestParam("questionId") int questionId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}
		Question question = questionService.getQuestionById(questionId);
		if (question == null) {
			return CommunityUtils.getJSON(1, "问题不存在");
		}
		boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(
				new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
						.setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

		Map<String, Object> info = new HashMap<>();

		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));

		return CommunityUtils.getJSON(ret ? 0 : 1,
				String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
	}

	@RequestMapping(path = { "/unfollowQuestion" }, method = { RequestMethod.POST })
	@ResponseBody
	public String unfollowQuestion(@RequestParam("questionId") int questionId) {
		if (hostHolder.getUser() == null) {
			return CommunityUtils.getJSON(999);
		}

		Question question = questionService.getQuestionById(questionId);
		if (question == null) {
			return CommunityUtils.getJSON(1, "问题不存在");
		}

		boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

		eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));
		Map<String, Object> info = new HashMap<>();

		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followService.getFolloweeCount(EntityType.ENTITY_QUESTION, questionId));
		return CommunityUtils.getJSON(ret ? 0 : 1,
				String.valueOf(followService.getFolloweeCount(EntityType.ENTITY_USER, hostHolder.getUser().getId())));
	}

	@RequestMapping(path = { "/user/{uid}/followees" }, method = { RequestMethod.GET })
	public String followees(@PathVariable("uid") int userId, Model model) {
		List<Integer> followeeIds = followService.getFollowees(EntityType.ENTITY_USER, userId, 0, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
		} else {
			model.addAttribute("followees", getUsersInfo(0, followeeIds));
		}
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUserById(userId));
		return "followees";
	}

	@RequestMapping(path = { "/user/{uid}/followers" }, method = { RequestMethod.GET })
	public String followers(@PathVariable("uid") int userId, Model model) {
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
		} else {
			model.addAttribute("followers", getUsersInfo(0, followerIds));
		}
        model.addAttribute("followerCount", followService.getFollowerCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUserById(userId));
		return "followers";
	}

	private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
		List<ViewObject> userInfos = new ArrayList<>();
		for (Integer id : userIds) {
			User user = userService.getUserById(id);
			if (user == null) {
				continue;
			}
			ViewObject vObject = new ViewObject();
			vObject.set("user", user);
			vObject.set("commentCount", commentService.getUserCommentCount(id));
			vObject.set("followerCount", followService.getFolloweeCount(EntityType.ENTITY_USER, id));
			vObject.set("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, id));
			if (localUserId != 0) {
				vObject.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, id));
			} else {
				vObject.set("followed", false);
			}
			userInfos.add(vObject);
		}
		return userInfos;
	}
}
