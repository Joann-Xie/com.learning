package Learning.com.learning.objectResources;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import UserExceptions.InternalError;

import javax.xml.bind.DatatypeConverter;


import DBUtils.MemCacheDB;
import constants.Constants;

public class Token {
	private final int life = 1800000;
	private long ctime;
	private String token;
    private static MemCacheDB mcc;
	
    public Token(String token) throws SQLException{
    	mcc = MemCacheDB.getInstance();
    	this.token = token;
    }


	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public static String generateToken(String key) throws InternalError, SQLException {
		long curtime = System.currentTimeMillis();
		String token = "";
		//String token = genRandom() + curtime;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte[] bytes = md.digest();
			token = DatatypeConverter.printHexBinary(bytes) + curtime;
			refreshToken(key, token, curtime);
		} catch(NoSuchAlgorithmException e){
			//TODO Add log
			throw new InternalError("Internal error detected");
		}
		return token;
	}

	public static void refreshToken(String username, String token, long curtime) throws SQLException{
		String ctimesql = "select ctime from " + Constants.CRED_TABLE + " where username=\'" + username + "\'";
		String tokensql = "select token from " + Constants.CRED_TABLE + " where username=\'" + username + "\'";
		String sqlcmd = "update " + Constants.CRED_TABLE + " set ctime=" + curtime + ", token=\'" + token + "\' where username=\'" + username +"\'";
		if(mcc == null) {
			mcc = MemCacheDB.getInstance();
		}
		String ctimeKey = mcc.generateKey(ctimesql);
		String tokenKey = mcc.generateKey(tokensql);
		if(mcc.keyExist(ctimeKey)){
			mcc.setValue(ctimeKey, curtime);			
		}else{
			mcc.addValue(ctimeKey, curtime);
		}
		if(mcc.keyExist(tokenKey)){
			mcc.setValue(tokenKey, token);
		}else{
			mcc.addValue(tokenKey, token);
		}
		try{
		    mcc.getDbcon().update(sqlcmd);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
//	public boolean tokenExist() throws SQLException{
//		boolean exist = false;
//		String sqlcmd = "select token from " + Constants.CRED_TABLE + " where token=" + "\'" + this.token + "\'";
//		String tokenKey = mcc.generateKey(sqlcmd);
//		Object token = mcc.getValue(tokenKey);
//		if(token == null){
//			ResultSet rs = mcc.getDbcon().query(sqlcmd);
//			if(rs.next()){
//				exist = true;
//			}
//		} else {
//			exist = true;
//		}
//		return exist;
//	}
	
	public boolean tokenExpired() throws SQLException{
		boolean expired = true;
		long curtime = System.currentTimeMillis();
		String sqlcmd = "select ctime from " + Constants.CRED_TABLE + " where token=" + "\'" + this.token + "\'";
		String ctimeKey = mcc.generateKey(sqlcmd);
		Object tokenCtimeObj = mcc.getValue(ctimeKey);
		long tokenCtime;
		if(tokenCtimeObj == null){
			ResultSet rs = mcc.getDbcon().query(sqlcmd);
			if(rs.next()){
				tokenCtime = rs.getLong("ctime");
				if(curtime < tokenCtime + this.life) {					
					expired = false;
				} else {
					System.out.println("ctime is " + curtime);
					System.out.println("Token expired");
				}
			} 
		} else {
			tokenCtime = ((Number) tokenCtimeObj).longValue();
			if(curtime < tokenCtime + this.life){
				expired = false;
			}
		}
		return expired;
	}
	
//	private String genRandom(){
//		int len = 20;
//		Random random = new Random();
//		String str = "";
//		
//		for(int idx = 0; idx < len; idx++){
//			int r1 = random.nextInt(3);
//			int r2 = 0;
//			char cc = ' ';
//			switch(r1){
//			case 0:
//				r2 = random.nextInt(10);
//				cc = (char) (r2 + 48);
//				break;
//			case 1:
//				r2 = random.nextInt(26);
//				cc = (char) (r2 + 65);
//				break;
//			case 2:
//				r2 = random.nextInt(26);
//				cc = (char)(r2 + 97);
//				break;
//			}
//			str += String.valueOf(cc);
//		}
//		return str;
//	}
	public int getLife() {
		return life;
	}
}
