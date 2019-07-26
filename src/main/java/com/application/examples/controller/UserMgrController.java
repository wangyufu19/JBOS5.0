package com.application.examples.controller;
import com.application.examples.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbosframework.beans.annotation.Autowired;
import com.jbosframework.web.mvc.annotation.Controller;
import com.jbosframework.web.mvc.annotation.RequestMapping;
import java.util.List;
import com.application.examples.service.UserMgrService;
@Controller
public class UserMgrController {
	@Autowired
	private UserMgrService userMgrService;
	
	@RequestMapping("/user/getUserList")
	public List getUserList() {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("*******userMgrService: "+userMgrService);
		List list=userMgrService.getUserList();
		String json= null;
		try {
			json = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return list;
	}
	@RequestMapping("/user/getUserInfo")
	public User getUserInfo(String id) {
		ObjectMapper mapper = new ObjectMapper();
		User user=userMgrService.getUserById(id);
		String json= null;
		try {
			json = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return user;
	}
}
