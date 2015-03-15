package com.example.assignment;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class WordList {
	
	@PrimaryKey
	@Persistent
	private Key id;
	@Persistent
	private List<String> my_words = new ArrayList<String>();
	
	public void setID(final Key id) { this.id=id;}
	public void createWordList(){ my_words =new ArrayList<String>();}
	
	
	public int wordCount(){
		int num_words = my_words.size(); 
		return num_words;
	}
	public String getWord(final int index){
		String word; 
		word = my_words.get(index);
		return word;
	}
	public boolean addWord(final String word){
		if(!my_words.contains(word)){
			my_words.add(word);
			return true;
		}else{
			return false;
		}
	}

}
