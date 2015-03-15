package com.example.assignment;
import java.io.IOException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class AnagramServlet extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		String word =null;
		word=req.getParameter("search_word");
		
		
		if(word==null){
			String emptyValue = "No value has been entered";
			System.out.println(emptyValue);
			req.setAttribute("message", emptyValue);
			resp.sendRedirect("/");
		}else{
			try{
				UserService us = UserServiceFactory.getUserService();
				User u = us.getCurrentUser();
				PersistenceManager pm = null; 
				Key user_key = KeyFactory.createKey("WordList", u.getUserId());
				WordList user_words;
				pm =PMF.get().getPersistenceManager();
				user_words = pm.getObjectById(WordList.class, user_key);
				String[] anagram_words =null;
				
			for(int j=0;j<user_words.wordCount(); j++){
				if(word.length()==user_words.getWord(j).length()){
					char[] userWord = word.toCharArray();
					char[] listWord = user_words.getWord(j).toCharArray();
					Arrays.sort(userWord);
					Arrays.sort(listWord);
					if(Arrays.equals(userWord,listWord)){
						anagram_words = new String[user_words.wordCount()];
						anagram_words[j]=user_words.getWord(j);	
						
					}
				}	
			}
			if(anagram_words!=null){
				String matches ="Here are the anagram of your word";
				System.out.println(matches);
				String[] wordArray = new String[anagram_words.length];
				for(int i=0; i<anagram_words.length; i++){
					if(anagram_words[i]!=null){
						wordArray[i]=anagram_words[i];
						System.out.println(wordArray[i]);
					}
				}
				req.setAttribute("word_array", wordArray);
				req.setAttribute("message", matches);
				resp.sendRedirect("/");
			}else{
				String no_matches="There were no anagrams of this word";
				System.out.println(no_matches);				
				req.setAttribute("message", no_matches);
				resp.sendRedirect("/");
			}
			}catch(Exception e){
				//Will only fail if datastore goes down	
			}	
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
			//Add word
		String word = null;
		word = req.getParameter("add_word");
		if(word==null){
			String emptyValue = "No value has been entered";
			System.out.println(emptyValue);
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
					String success ="This word has been added";
					System.out.println(success);
					req.setAttribute("message", success);
					resp.sendRedirect("/");
				}
				else{
					
					String fail = "This word already exists";
					System.out.println(fail);
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
