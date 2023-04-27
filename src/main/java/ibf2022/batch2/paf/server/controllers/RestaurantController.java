package ibf2022.batch2.paf.server.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;
import ibf2022.batch2.paf.server.services.RestaurantService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

	@Autowired
	RestaurantService svc;

	// TODO: Task 2 - request handler
	@GetMapping(path="/cuisines")
	public ResponseEntity<String> getCuisines(){
		List<String> cuisines = svc.getCuisines();

		if(cuisines.isEmpty()){
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
								.add("Error", "No cuisines available!")
								.build().toString());
		}

		JsonArrayBuilder jab = Json.createArrayBuilder();

		for(String cuisine : cuisines){
			jab.add(cuisine);
		}

		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(jab.build().toString());
	}

	// TODO: Task 3 - request handler
	@GetMapping(path="/restaurants/{cuisine}")
	public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine){
		
		List<Restaurant> restaurants = svc.getRestaurantsByCuisine(cuisine);
		JsonArrayBuilder jab = Json.createArrayBuilder();

		for(Restaurant r : restaurants){
			jab.add(r.toJsonForNameAndId());
		}

		if(restaurants.isEmpty()){
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
								.add("Error", "No restaurant selling %s cuisine!".formatted(cuisine))
								.build().toString());
		}

		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(jab.build().toString());
	}


	// TODO: Task 4 - request handler
	@GetMapping(path="/restaurant/{restaurant_id}")
	public ResponseEntity<String> getRestaurantById(@PathVariable String restaurant_id){
		
		Optional<Restaurant> r = svc.getRestaurantById(restaurant_id);
		if(r.isEmpty()){
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder()
								.add("Error", "Missing %s".formatted(restaurant_id))
								.build().toString());
		}

		return ResponseEntity
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(r.get().toJson().toString());
	}

	// TODO: Task 5 - request handler
	@PostMapping(path="/restaurant/comment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> postRestaurantComment(@ModelAttribute Comment comment){
		
		Date date = new Date();
		long epoch = date.getTime();
		comment.setDate(epoch);

		
		svc.postRestaurantComment(comment);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body("{ }");
	}
}
