package shorteningURL;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBUtil;

public class ConvertUrl 
{
	private String domain; 
	private char useChars[];
	
	/**
	 * init
	 */
	public ConvertUrl(String dm)
	{
		setUseCharArr();
		domain = dm;
	}

	/**
	 * 단축 url 에서 사용할 문자배열 세팅
	 */
	private void setUseCharArr()
	{
		useChars = new char[62];
		
		for (int i = 0; i < 62; i++)
		{
			int j = 0;
			if (i < 10){ j = i + 48; }
			else if (i > 9 && i <= 35){j = i + 55;}
			else{j = i + 61;}
			useChars[i] = (char) j;
		}
	}
	
	/**
	 * 단축 url 생성
	 * @param longURL
	 * @return
	 * @throws SQLException 
	 */
	public String getShortenURL(String oUrl) throws SQLException
	{
		String sUrl = "";
		
		Connection conn = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm2 = null;
		PreparedStatement pstm3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		
		try {
			String query = "SELECT SHORTEN_URL FROM HS_URL_001 WHERE ORGN_URL = ?";

			conn = DBUtil.getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, oUrl);
			rs = pstm.executeQuery();
			
			if(rs.next())
			{
				sUrl = rs.getString("SHORTEN_URL");
			}
			else
			{
				do {
					//시퀀스가져오기
					query = "SELECT SEQ_URL.NEXTVAL NEXT_SEQ_NO FROM DUAL";
					pstm2 = conn.prepareStatement(query);
					rs2 = pstm2.executeQuery();
					rs2.next();
					
					oUrl = getRealURL(oUrl);
					sUrl =  domain + "/" +getShortUrl(oUrl, rs2.getInt("NEXT_SEQ_NO"));
					
				}
				while(checkDupShortUrl(conn, pstm3, rs3, sUrl));
				
				//저장
				query = "INSERT INTO HS_URL_001 VALUES  (?, SEQ_URL.NEXTVAL, ?, SYSTIMESTAMP)";
				pstm3 = conn.prepareStatement(query);
				pstm3.setString(1, oUrl);
				pstm3.setString(2, sUrl);
				
				pstm3.executeQuery();
			}
			
		}
		catch (SQLException e){
			System.out.println("SQL error : ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(rs	 != null) rs.close();
			if(rs2	 != null) rs2.close();
			if(rs3	 != null) rs3.close();
			if(pstm  != null) pstm.close();
			if(pstm2 != null) pstm2.close();
			if(pstm3 != null) pstm3.close();
			if(conn  != null) conn.close();
		}
		System.out.println(sUrl);
		
		return sUrl;
	}
	
	public String getOrgnURL(String sUrl) throws SQLException
	{
		String oUrl = "";
		Connection conn = null;
		PreparedStatement pstm = null;
		
		
		String query = "SELECT ORGN_URL FROM HS_URL_001 WHERE SHORTEN_URL = ?";
		conn = DBUtil.getConnection();
		pstm = conn.prepareStatement(query);
		pstm.setString(1, sUrl);
		ResultSet rs = pstm.executeQuery();
		
		if(rs.next()) oUrl = rs.getString("ORGN_URL");
		
		return oUrl;
	}
	
	/**
	 * 중복체크
	 * @param conn
	 * @param pstm
	 * @param rs
	 * @param sUrl
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDupShortUrl(Connection conn, PreparedStatement pstm, ResultSet rs,  String sUrl) throws SQLException
	{
		boolean dupUrlYn = false;
		
		String query = "SELECT 1 FROM HS_URL_001 where SHORTEN_URL = ?";
		pstm = conn.prepareStatement(query);
		pstm.setString(1, sUrl);
		rs = pstm.executeQuery();
		
		if(rs.next())
		{
			dupUrlYn = true;
		}
		
		return dupUrlYn;
	}
	
	
	/**
	 * 실제 url만 추출 (http, https, / 제거)
	 * @param url
	 * @return
	 */
	private String getRealURL(String url)
	{
		if(url.length() > 6)
		{
			if ("http://".equals(url.substring(0, 7))) url = url.substring(7);
			if ("https://".equals(url.substring(0, 8)))url = url.substring(8);
			if (url.charAt(url.length() - 1) == '/') url = url.substring(0, url.length() - 1);
		}
		return url;
	}
	
	/**
	 * 단축 url 생성
	 * @param url
	 * @param seq
	 * @return
	 */
	private String getShortUrl(String url, int seq)
	{
		String key = "";
		
		try{
			//sha-256 hash
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(url.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			key = hexString.toString().substring(0, 3);
			
			//seq_no 변환
			StringBuffer sb = new StringBuffer();
			
			while (seq > 0) {
				sb.append(useChars[seq%62]);
				seq /= 62;
			}
			
			key += sb.toString();
			
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
		
		return key;
	}
}
