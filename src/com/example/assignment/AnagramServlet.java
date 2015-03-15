package com.example.assignment;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
	
@SuppressWarnings("serial")
public class AnagramServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/plain");
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");
		String message = "Test";
		PersistenceManager pm = null; 
		WordList user_words = null;
		if(u!=null){
			Key user_key = KeyFactory.createKey("WordList", u.getUserId());
			pm = PMF.get().getPersistenceManager();
			
			try{
				user_words = pm.getObjectById(WordList.class, user_key);
			} catch(Exception e){
				user_words = new WordList();
				user_words.setID(user_key);
				user_words.createWordList();
				pm.makePersistent(user_words);
			}
			pm.close();
		}
		
		
		
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);
		req.setAttribute("logout_url", logout_url);
		req.setAttribute("message", message);
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		//Adding a word 
		RequestDispatcher rd = req.getRequestDispatcher("/");
		String word = null;
		word = req.getParameter("add_word");
		if(word==null){
			String emptyValue = "No value has been entered";
			req.setAttribute("message", emptyValue);
			resp.sendRedirect("/");
		}else{
			UserService us = UserServiceFactory.getUserService();
			User u = us.getCurrentUser();
			PersistenceManager pm = null; 
			Key user_key = KeyFactory.createKey("WordList", u.getUserId());
			WordList user_words;
			try{
				pm = PMF.get().getPersistenceManager();
				user_words = pm.getObjectById(WordList.class, user_key);
				if(user_words.addWord(word)==true){
					pm.makePersistent(user_words);
					String success ="This words has been added";
					req.setAttribute("message", success);
					rd.forward(req, resp);	
				}
				else{
					String fail = "This word already exists";
					req.setAttribute("message", fail);
					pm.close();
					resp.sendRedirect("/");
										
				}
			}catch(Exception e){
				//Only if datastore fails 
			}
		}	
	}
}
