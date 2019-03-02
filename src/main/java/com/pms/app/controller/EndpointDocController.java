package com.pms.app.controller;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import  org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
public class EndpointDocController {
 private final RequestMappingHandlerMapping handlerMapping;
 
 @Autowired
 public EndpointDocController(RequestMappingHandlerMapping handlerMapping) {
  this.handlerMapping = handlerMapping;
 }
  
 @RequestMapping(value="/endpointdoc", method=RequestMethod.GET)
 public String show(Model model) {
  model.addAttribute("handlerMethods", this.handlerMapping.getHandlerMethods());
  Map<RequestMappingInfo,HandlerMethod> apiMap = this.handlerMapping.getHandlerMethods();
  for (Entry<RequestMappingInfo,HandlerMethod> entry : apiMap.entrySet())  {
	RequestMappingInfo key= entry.getKey() ;
	System.out.println(key.getPatternsCondition().getPatterns());
	String str=key.getPatternsCondition().getPatterns().toString();
	str = str.replaceAll("\\[", "").replaceAll("\\]","");
	System.out.println(str);
	HandlerMethod val=entry.getValue();
	System.out.println(key.getMethodsCondition().getMethods().toString());
	String method=key.getMethodsCondition().getMethods().toString();
	method = method.replaceAll("\\[", "").replaceAll("\\]","");
    System.out.println("Key = " + str + ", Value = " +method); 
    Parameter[] parameters = val.getMethod().getParameters();
    List<String> parameterNames = new ArrayList<>();
	    for (Parameter parameter : parameters) {
	        if(parameter.getDeclaringExecutable().getName()==null) {
	            //throw new IllegalArgumentException("Parameter names are not present!");
	        }
	        else{
	        String parameterName = parameter.getType().getSimpleName();
	        parameterNames.add(parameterName);
	        }
	    }
	   
	    System.out.println(parameterNames);
  	} 
  return "api-list";
 }
}