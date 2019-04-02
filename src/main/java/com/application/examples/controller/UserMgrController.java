package com.application.examples.controller;
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
		List list=userMgrService.getUserList();
		String json= null;
		try {
			json = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return list;
	}
}
