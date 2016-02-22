package com.card;

public class Card_rawYouJi {
	
	private String title = "";
	private String content = "";
	private String author = "";
	private String authorFrom = "";
	private String time = "";
	private int up = -1;
	private int collect = -1;
	private int comment = -1;
	private int share = -1;
	private String webFrom = "";
	
	public Card_rawYouJi(String title, String content, String author, String authorFrom, String time,
			int up, int collect, int comment, int share, String webFrom){
		this.title = title;
		this.content = content;
		this.author = author;
		this.authorFrom = authorFrom;
		this.time = time;
		this.up = up;
		this.collect = collect;
		this.comment = comment;
		this.share = share;
		this.webFrom = webFrom;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getAuthor(){
		return author;
	}

	public String getAuthorFrom(){
		return authorFrom;
	}
	
	public String getTime(){
		return time;
	}
	
	public int getUp(){
		return up;
	}
	
	public int getCollect(){
		return collect;
	}
	
	public int getComment(){
		return comment;
	}
	
	public int getShare(){
		return share;
	}
	
	public String getWebFrom(){
		return webFrom;
	}
}
