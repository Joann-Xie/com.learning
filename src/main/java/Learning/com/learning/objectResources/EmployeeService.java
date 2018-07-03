package Learning.com.learning.objectResources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import DBUtils.MemCacheDB;
import constants.Constants;
import constants.ReturnCode;

@Path("employee")
public class EmployeeService {

	@Path("instances")
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getEmployee(@Context HttpHeaders httpHeader) throws SQLException, JSONException{
		String token = httpHeader.getHeaderString(Constants.TOKEN_KEY);
		Token tokenInst = new Token(token);
		if(tokenInst.tokenExpired()) {
			return Response.status(ReturnCode.AUTHENTICATION_FAILURE).build();
		} else {
			ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		    //JSONObject json = new  JSONObject();
		    String sqlcmd = "select username,company,level from " + Constants.EMPLOYEE_TABLE;
		    MemCacheDB mcc = MemCacheDB.getInstance();
		    String mccKey = mcc.generateKey(sqlcmd);
		    JSONObject resp = new JSONObject();
		    if(mcc.keyExist(mccKey)) {
		    	Object result = mcc.getValue(mccKey);
		    	System.out.println(result.toString());
		    	resp.put("employee", result);
		    } else {
		    	ResultSet rs = mcc.getDbcon().query(sqlcmd);		    	
		    	while(rs.next()){
		    		JSONObject json = new JSONObject();
		    		json.put("username", rs.getString("username"));
		    		json.put("company", rs.getString("company"));
		    		json.put("level", rs.getInt("level"));	
		    		arr.add(json);
		    	}		    	
		    	resp.put("employees", arr);

		    	//put the result from db into memcache
		    	mcc.addValue(mccKey, arr);
		    }
		    return Response.ok(resp.toString()).build();
		}
	}
	
	@Path("instance/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployeeWithID(@Context HttpHeaders httpHeader, @PathParam("id") String instId) throws SQLException, JSONException{
		String token = httpHeader.getHeaderString(Constants.TOKEN_KEY);
		Token tokenInst = new Token(token);
		if(tokenInst.tokenExpired()){
			return Response.status(ReturnCode.AUTHENTICATION_FAILURE).build();
		}else{
			JSONObject json = new JSONObject();
			String sqlcmd = "select username, company. level from " + Constants.EMPLOYEE_TABLE + " where id=" + instId;
			MemCacheDB mcc = MemCacheDB.getInstance();
			String mccKey = mcc.generateKey(sqlcmd);
			if(mcc.keyExist(mccKey)) {
				Object result = mcc.getValue(mccKey);
				System.out.println(result.toString());
				json.put("employee", result);
			} else {
				ResultSet rs = mcc.getDbcon().query(sqlcmd);
				if(rs.next()) {
					json.put("username", rs.getShort("username"));
					json.put("company", rs.getString("company"));
					json.put("level", rs.getInt("level"));
				}
				//put the query result into memcache
				mcc.addValue(mccKey, json);
			}
			return Response.ok(json.toString()).build();
		}
	}
}
