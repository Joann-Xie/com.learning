package Learning.com.learning.objectResources;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

//import org.glassfish.grizzly.http.server.Response;

import DBUtils.MemCacheDB;
import UserExceptions.InternalError;
import constants.ReturnCode;
import constants.Constants;

public class UserService {
	private String username;
	private String passwd;
	private MemCacheDB mcc;
	public UserService(String username, String password) throws SQLException{
		this.username = username;
		this.passwd = password;
		mcc = MemCacheDB.getInstance();
	}
	
	public boolean isUserExist() throws SQLException{
		String sqlcmd = "select username from " + Constants.CRED_TABLE + " where username=\'" + this.username + "\'";
		MemCacheDB mcc = MemCacheDB.getInstance();
		String mccKey = mcc.generateKey(sqlcmd);
		boolean exist = false;
		try{
			if(mcc.keyExist(mccKey)){
				return true;
			} else {
				ResultSet rs = mcc.getDbcon().query(sqlcmd);
				if(rs.next()){
			    	exist = true;
			    }
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return exist;
	}
	
	public boolean authenticate(){
		boolean valid = false;
		String sqlcmd = "select password from " + Constants.CRED_TABLE + " where username=\'" + this.username + "\'";
		String userKey = this.username + "_password";

		try{
			if(mcc.keyExist(userKey)){
				String passwd = mcc.getValue(userKey).toString();
				if(passwd.equals(this.passwd)){
					valid = true;
				}
			}else {
			    ResultSet rs = mcc.getDbcon().query(sqlcmd);
			    if(rs.next()){
				    String passwd = rs.getString("password");
				    if(passwd.equals(this.passwd)){
					    valid = true;
				    }
			    }
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		return valid;
	}

	public Response login() throws InternalError, SQLException{
		try{
			if(authenticate()){
				String token = Token.generateToken(username);
				JSONObject resp = new JSONObject();
				resp.put("test-token", token);
				//return Response.ok(resp.toString()).build();
				return Response.ok().header(Constants.TOKEN_KEY, token).build();
			} else {
				return Response.status(ReturnCode.INVALID_USER).build();
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return Response.status(ReturnCode.GENERAL_ERR).build();
		
	}

}
