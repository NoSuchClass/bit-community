package com.bit.community.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.community.async.EventModel;
import com.bit.community.async.EventProducer;
import com.bit.community.async.EventType;
import com.bit.community.model.Comment;
import com.bit.community.model.EntityType;
import com.bit.community.model.HostHolder;
import com.bit.community.service.CommentService;

@Controller
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	HostHolder hostHolder;
	@Autowired
	EventProducer eventProducer;
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(CommentController.class);
	
	@RequestMapping(path="/addComment", method = {RequestMethod.POST})
	public String addComment(@RequestParam("questionId") int questionId,
							@RequestParam("content") String content, Model model) {
		try {
			Comment comment = new Comment();
			if (hostHolder.getUser() != null) {
				comment.setUserId(hostHolder.getUser().getId());
			}else {
				return "login";
			}
			comment.setStatus(0);
			comment.setContent(content);
			comment.setCreatedDate(new Date());
			comment.setEntityType(EntityType.ENTITY_QUESTION);
			comment.setEntityId(questionId );
			commentService.addComment(comment);
			
			eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(hostHolder.getUser().getId()).setEntityId(questionId));
			model.addAttribute("comment",comment);
		} catch (Exception e) { 
			log.error(e.getMessage());
		}
		return "redirect:/question/"+questionId;
	}
}
