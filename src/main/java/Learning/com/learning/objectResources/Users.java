package Learning.com.learning.objectResources;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;

import UserExceptions.InternalError;

@Path("login")
public class Users {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Credential cred) throws SQLException, InternalError{
		String username = cred.getUsername();
		String passwd = cred.getPassword();
		UserService userinst = new UserService(username, passwd);
		return userinst.login();
	}
}
