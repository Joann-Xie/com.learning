package DBUtils;

import java.sql.SQLException;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MemCacheDB {
	private MemCachedClient memclient;
	private static DBConnection dbcon;

	public static MemCacheDB inst;
	public static MemCacheDB getInstance() throws SQLException{
		if(inst == null) {
			inst = new MemCacheDB();
		}
		return inst;
	}
	
	private MemCacheDB() throws SQLException{
		String[] servers = { "127.0.0.1:10000" };

	       SockIOPool pool = SockIOPool.getInstance();

	       pool.setServers(servers);

	       pool.setFailover(true);

	       pool.setInitConn(10);

	       pool.setMinConn(5);

	       pool.setMaxConn(250);

	       pool.setMaintSleep(30);

	       pool.setNagle(false);

	       pool.setSocketTO(3000);

	       pool.setAliveCheck(true);

	       pool.initialize();
	       memclient = new MemCachedClient();
	       dbcon = DBConnection.getInstance();
	}
	public void setValue(String key, Object value){
		memclient.add(key, value);
	}
	
	public Object getValue(String key){
		return memclient.get(key);
	}
	
	public void deleteValue(String key){
		memclient.delete(key);
	}
	
	public void addValue(String key, Object value){
		memclient.add(key, value);
	}
	
	public DBConnection getDbcon() {
		return dbcon;
	}
	
	public boolean keyExist(String key){
		return memclient.keyExists(key);
	}
	
	public String generateKey(String key){
		StringBuffer sb = new StringBuffer();
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte [] bytes = md.digest();
			
			for(int i = 0; i < bytes.length; i++){
				String hex=Integer.toHexString(0xff & bytes[i]);
				if(hex.length()==1) sb.append('0');
				sb.append(hex);
			}
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}	
		
		return sb.toString();
	}

}
