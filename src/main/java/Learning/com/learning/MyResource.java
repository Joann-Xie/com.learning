package Learning.com.learning;

import java.sql.ResultSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import DBUtils.DBConnection;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	//@Path("myresource")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	
    	String result = null;
    	try{
    		DBConnection dbcon = DBConnection.getInstance();
    	    ResultSet rs = dbcon.query("select * from users");
    	    while(rs.next()){
    	    	result += "\ninstance: ";
    	    	String name = rs.getString("name");
    	    	int age = rs.getInt("age");
    	    	result += name;
    	    	result += age;
    	    }
    	}catch(Exception e){
    	    e.printStackTrace();
    	}
    	return result;
        //return "Got it!";
    }
    
	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test(){
		return "test";
	}
	
	@Path("myresource")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String doIt(){
    	JSONObject res = new JSONObject();
    	try{
    		res.put("name", "jerry");
    		res.put("age", 31);
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	
    	return res.toString();
    }
}
