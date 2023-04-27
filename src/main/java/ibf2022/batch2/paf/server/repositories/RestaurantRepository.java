package ibf2022.batch2.paf.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.paf.server.models.Comment;
import ibf2022.batch2.paf.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	MongoTemplate template;
	// TODO: Task 2 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//	db.restaurants.distinct("cuisine").sort({cuisine:1})

	public List<String> getCuisines() {
		
		Query q = new Query();
		q.with(Sort.by(Sort.Direction.ASC, "cuisine"));

		//template.findDistinct method will return empty list if the 
		//collection or attribute does not exist or if the query is not satisfied
		List<String> cuisines = template.findDistinct(q, "cuisine", "restaurants", String.class);
		return cuisines;
	}

	// TODO: Task 3 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	//	db.restaurants.find({cuisine:/African/i}).sort({name:1})

	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		
		Query q = new Query().addCriteria(Criteria.where("cuisine").is(cuisine));
		q.with(Sort.by(Sort.Direction.ASC,"name"));

		//template.find method will return empty list if the if the query is not satisfied
		List<Document> restaurantDocs = template.find(q,Document.class,"restaurants");

		List<Restaurant> restaurants = restaurantDocs.stream()
													.map(d -> Restaurant.convertFromDocumentForNameAndId(d))
													.toList();

		return restaurants;
	}
	
	// TODO: Task 4 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below
	/* 	db.restaurants.aggregate([
		{
			$match:{restaurant_id:"40368021"}
		},
		{
			$lookup: {
				from:"comments",
				foreignField:"restaurantId",
				localField:"restaurant_id",
				as:"comments"
			}
		},
		{
			$project: {
				restaurant_id:1, name:1, cuisine:1,
				address:{
					$concat:["$address.building", ",", "$address.street", ",", "$address.zipcode", ",", "$borough"]
				}, comments:1
			}
		}
	]); */
	public Optional<Restaurant> getRestaurantById(String id) {
		
		MatchOperation mOp = Aggregation.match(
			Criteria.where("restaurant_id").is(id)
		);

		LookupOperation lOp = Aggregation.lookup(
			"comments", "restaurant_id", "restaurantId", "commentsDoc"
		);

		ProjectionOperation pOp = Aggregation.project("restaurant_id", "name", "cuisine", "address")
									.and("commentsDoc").as("comments")
									.and(
										StringOperators.Concat.valueOf("address.building").concat(",")
										.concatValueOf("address.street").concat(",")
										.concatValueOf("address.zipcode").concat(",")
										.concatValueOf("borough")
									).as("address");

		Aggregation pipeline = Aggregation.newAggregation(mOp, lOp, pOp);

		AggregationResults<Document> results = template.aggregate(pipeline, "restaurants", Document.class);

		List<Document> docs = results.getMappedResults();

		if(docs.isEmpty())
		return Optional.empty();

		Restaurant r = Restaurant.convertFromDocument(docs.get(0));
		
		return Optional.of(r);
	}

	// TODO: Task 5 
	// Do not change the method's signature
	// Write the MongoDB query for this method in the comments below

	//for insert
	/*db.comments.insertOne({
		restaurant_id: "40356442", name: "SH", rating: 5,
		comment: "A", date: new Date(Date.now())
	});*/

	//for update
	/*	db.restaurants.updateOne({restaurant_id: "40356442"},
		{$push: {grades: { date: new Date(Date.now()) , grade: "A", score: 5 }}
		 });
	 */
	public void insertRestaurantComment(Comment comment) {
		//Query q = new Query(Criteria.where("restaurant_id").is(comment.getRestaurantId()));

		Document newDoc = template.insert(comment.toDocumentInsert(), "comments");
		System.out.println("Document inserted with Object_id:"+newDoc.getObjectId("_id"));
	}
}
