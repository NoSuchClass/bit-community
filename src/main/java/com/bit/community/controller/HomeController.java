package com.bit.community.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bit.community.model.EntityType;
import com.bit.community.model.HostHolder;
import com.bit.community.model.Question;
import com.bit.community.model.User;
import com.bit.community.model.ViewObject;
import com.bit.community.service.CommentService;
import com.bit.community.service.FollowService;
import com.bit.community.service.QuestionService;
import com.bit.community.service.UserService;

@Controller
public class HomeController {
	@Autowired
	QuestionService questionService;
	@Autowired
	UserService userService;
	@Autowired
	FollowService followService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	CommentService commentService;

	@Autowired
	HostHolder host;

//	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping({ "/", "/index" })
	public String index(Model model) {
		List<Question> questions = questionService.getLatestQuestion(0, 10);
		List<ViewObject> viewObjects = new ArrayList<>();
		for (Question question : questions) {
			ViewObject viewObject = new ViewObject();
			viewObject.set("question", question);
			viewObject.set("user", userService.getUserById(question.getUserId()));
			viewObjects.add(viewObject);
		}
		model.addAttribute("vos", viewObjects);
		return "index";
	}

	@RequestMapping("/user/{userId}")
	public String showDetail(Model model, @PathVariable("userId") int userId) {
		model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUserById(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
	}
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestion(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userService.getUserById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
