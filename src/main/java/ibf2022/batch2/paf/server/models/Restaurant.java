package ibf2022.batch2.paf.server.models;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

// Do not change this file
public class Restaurant {

	private String restaurantId;
	private String name;
	private String address;
	private String cuisine;
	private List<Comment> comments = new LinkedList<>();

	public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
	public String getRestaurantId() { return this.restaurantId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setAddress(String address) { this.address = address; }
	public String getAddress() { return this.address; }

	public void setCuisine(String cuisine) { this.cuisine = cuisine; }
	public String getCuisine() { return this.cuisine; }

	public void setComments(List<Comment> comments) { this.comments = comments; }
	public List<Comment> getComments() { return this.comments; }
	public void addComment(Comment comment) { this.comments.add(comment); }

	@Override
	public String toString() {
		return "Restaurant{restaurantId=%s, name=%s, address=%s, cuisine=%s, comments=%s"
				.formatted(restaurantId, name, address, cuisine, comments);
	}

	//Dont need convert all fields from Document to Java Object. Just need convert the required ones
	public static Restaurant convertFromDocumentForNameAndId(Document d){
		
		Restaurant r = new Restaurant();
		r.setName(d.getString("name"));
		r.setRestaurantId(d.getString("restaurant_id"));

		return r;
	}
	
	//Dont need convert all fields from Java Object to Json. Just need convert the required ones
	public JsonObject toJsonForNameAndId(){
		return Json.createObjectBuilder()
					.add("restaurantId",this.getRestaurantId())
					.add("name", this.getName())
					.build();
	}

	public static Restaurant convertFromDocument(Document d){
		Restaurant r = new Restaurant();

		r.setName(d.getString("name"));
		r.setRestaurantId(d.getString("restaurant_id"));
		r.setCuisine(d.getString("cuisine"));
		r.setAddress(d.getString("address"));
		
		//cannot cast as List<Comment> directly as we retrieve results from aggregation in the form of Document.class 
		List<Document> comments = d.getList("comments", Document.class);
		//(List<Document>) d.get("comments");
		r.setComments(comments.stream().map(doc -> Comment.convertFromDocument(doc)).toList());
		return r;
	}

	public JsonObject toJson(){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		
		for(Comment c : comments){
			JsonObject commentObj = c.toJson();
			jab.add(commentObj);
		}

		return Json.createObjectBuilder()
				.add("restaurant_id", this.getRestaurantId())
				.add("name", this.getName())
				.add("cuisine", this.getCuisine())
				.add("address", this.getAddress())
				.add("comments", jab)
				.build();
	}
}
