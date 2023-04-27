package ibf2022.batch2.paf.server.models;


import java.util.Date;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// Do not change this file
public class Comment {

	private String restaurantId;
	private String name;
	private long date = 0l;
	private String comment;
	private int rating;

	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setComment(String comment) { this.comment = comment; }
	public String getComment() { return this.comment; }

	public void setDate(long date) { this.date = date; }
	public long getDate() { return this.date; }

	public void setRating(int rating) { this.rating = rating; }
	public int getRating() { return this.rating; }

	@Override
	public String toString() {
		return "Comment{restaurantId=%s, name=%s, date=%d, comment=%s, rating=%d"
				.formatted(restaurantId, name, date, comment, rating);
	}

	public JsonObject toJson(){
		return Json.createObjectBuilder()
				.add("restaurantId", this.getRestaurantId())
				.add("name", this.getName())
				.add("date", this.getDate())
				.add("comment", this.getComment())
				.add("rating", this.getRating())
				.build();
	}

	public static Comment convertFromDocument(Document d){
		Comment c = new Comment();
		c.setRestaurantId(d.getString("restaurantId"));
		c.setName(d.getString("name"));
		c.setDate(d.getLong("date"));
		c.setComment(d.getString("comment"));
		c.setRating(d.getInteger("rating"));
		return c;
	}
	
	

	//Convert Java Object to Document
	 public Document toDocumentInsert(){
		Document d = new Document();
		d.append("restaurantId", this.getRestaurantId());
		d.append("name", this.getName());
		d.append("rating", this.getRating());
		d.append("comment", this.getComment());
		d.append("date", this.getDate());

	return d;
	}

}
