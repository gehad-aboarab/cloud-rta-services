package RTA;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseHelper {
	private static MongoClient mongoClient;
	private static MongoDatabase database;
	
	private static String RENEWAL_TABLE = "rta-renewal";
	
	public DatabaseHelper(String databaseName) {
		mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://Gehad:Aboarab97@cloud-computing-zqxty.mongodb.net/test?retryWrites=true"));
		database = mongoClient.getDatabase(databaseName);
	}
	
	// Returns the renewal fees given a license plate number and registration number
	public static JSONObject getRenewalFees(String license_no, String registration_no) {
		MongoCollection<Document> collection = database.getCollection(RENEWAL_TABLE);
		FindIterable<Document> documents = collection.find();
		
		try {
			for(Document document : documents) {
				if(document.getString("license-no").equals(license_no) && 
						document.getString("registration-no").equals(registration_no) && 
						(!document.getString("fee").equals("0"))) {
					document.remove("_id");
					JSONObject result = new JSONObject(document);
					return result;
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	
	// Returns the renewed registration
	public static JSONObject renewRegistration(String license_no, String registration_no, String credit_card, double amount) {
		MongoCollection<Document> collection = database.getCollection(RENEWAL_TABLE);
		FindIterable<Document> documents = collection.find();

		try {
			for(Document document : documents) {
				if(document.getString("license-no").equals(license_no) && 
						document.getString("registration-no").equals(registration_no) && 
						(!document.getString("fee").equals("0"))) {
					Bson filter = new Document("license-no", license_no);
					Bson fees = new Document("fee", "0");
					
					String pattern = "dd/MM/yyyy";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(new Date());
					Bson expiry = new Document("expiry", date); 
					
					Bson updateFees = new Document("$set", fees);
					collection.updateOne(filter, updateFees);

					Bson updateExpiry = new Document("$set", expiry);
					collection.updateOne(filter, updateExpiry);
				}
			}
			
			for(Document document : documents) {
				if(document.getString("license-no").equals(license_no) && 
						document.getString("registration-no").equals(registration_no) &&
						document.getString("fee").equals("0")) {
					document.remove("_id");
					return new JSONObject(document);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	
	// For testing
	public static void main(String[] args) {
		mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://Gehad:Aboarab97@cloud-computing-zqxty.mongodb.net/test?retryWrites=true"));
		database = mongoClient.getDatabase("cloud-computing");
		
		System.out.println(getRenewalFees("L11978", "987654"));
//		System.out.println(renewRegistration("L11978", "987654", "1234", 2000.0));
	}
}
