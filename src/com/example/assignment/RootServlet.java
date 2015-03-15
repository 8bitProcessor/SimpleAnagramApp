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
public class RootServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		//I coudln't get the message to print it out on the screen 
		//Everything else works 
		//StudentNo: 2813365
		//Name : Conor Mckernan
		//Assignment 01
		
		resp.setContentType("text/plain");
		UserService us = UserServiceFactory.getUserService();
		User u = us.getCurrentUser();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");
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
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);
	}
	
}
