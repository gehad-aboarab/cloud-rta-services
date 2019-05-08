package RTA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/rta")
public class RTAWebServices {
	private static final DatabaseHelper database = new DatabaseHelper("cloud-computing");
	
	@GET
	@Path("renewal-fees/{license-no}/{registration-no}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRenewalFees(@PathParam("license-no") String license_no, 
			@PathParam("registration-no") String registration_no) {
		return database.getRenewalFees(license_no, registration_no).toString();
	}
	
	@GET
	@Path("renew-registration/{license-no}/{registration-no}/{credit-card}/{amount}")
	@Produces(MediaType.TEXT_PLAIN)
	public String renewRegistration(@PathParam("license-no") String license_no, 
			@PathParam("registration-no") String registration_no,
			@PathParam("credit-card") String credit_card, 
			@PathParam("amount") double amount) {
		return database.renewRegistration(license_no, registration_no, credit_card, amount).toString();
	}
	
//	@GET
//	@Path("payment/{license_no}/{registration_no}/{credit-card}/{amount}")
//	public JSONObject payment(@PathParam("license-no") String license_no, 
//			@PathParam("registration-no") String registration_no,
//			@PathParam("credit-card") String credit_card, 
//			@PathParam("amount") double amount) {
//		return database.payment(license_no, registration_no, credit_card, amount);
//	}
}
