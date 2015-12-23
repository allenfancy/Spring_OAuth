package com.allen.oauth.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.ssl.HttpsURLConnection;

public class HttpClientUtil {

	public static final String SunX509 = "SunX509";
	public static final String JKS = "JKS";
	public static final String PKCS12 = "PKCS12";
	public static final String TLS = "TLS";
	
	
	public static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException{
		URL url = new URL(strUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		return httpURLConnection;
	}
	
	public static HttpURLConnection getHttpsURLConnection(String strUrl) throws IOException{
		URL url = new URL(strUrl);
		HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
		return httpURLConnection;
	}
	
	public static String getURL(String strUrl){
		if(null != strUrl){
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf){
				return strUrl.substring(0,indexOf);
			}
			return strUrl;
		}
		return strUrl;
	}
	
	public static String getQueryString(String strUrl){
		if(null != strUrl){
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf){
				return strUrl.substring(indexOf+1,strUrl.length());
			}
			return "";
		}
		return strUrl;
	}
	
	public static Map queryString2Map(String queryString){
		if(null == queryString || "".equals(queryString)){
			return null;
		}
		Map m = new HashMap();
		String[] strArray = queryString.split("&");
		for(int index = 0 ; index < strArray.length;index++){
			String pair = strArray[index];
			HttpClientUtil.putMapByPair(pair,m);
		}
		return m;
	}
	
	public static void putMapByPair(String pair,Map m){
		if(null == pair || "".equals(pair)){
			return;
		}
		int indexOf = pair.indexOf("=");
		if(-1 != indexOf){
			String k = pair.substring(0, indexOf);
			String v = pair.substring(indexOf+1,pair.length());
			if(null != k && !"".equals(k)){
				m.put(k, v);
			}
		}else{
			m.put("pair","");
		}
	}
	/**
	 * 将BufferedReader转换为String
	 * 注意：处理流关闭的处理
	 * @param reader
	 * @return
	 * @throws IOException 
	 */
	public static String bufferedReader2String(BufferedReader reader) throws IOException{
		StringBuffer buf = new StringBuffer();
		String line = null;
		while((line = reader.readLine())!=null){
			buf.append(line);
			buf.append("\r\n");
		}
		reader.close();
		return buf.toString();
	}
	/**
	 * 处理输出
	 * @param out
	 * @param data
	 * @param len
	 * @throws IOException
	 */
	public static void doOutPut(OutputStream out,byte[] data,int len) throws IOException{
		int dataLen = data.length;
		int off = 0;
		while(off < dataLen){
			if(len >= dataLen){
				out.write(data,off,dataLen);
			}else{
				out.write(data,off,len);
			}
			//刷新缓冲区
			out.flush();
			off += len;
			dataLen -= len;
		}
	}
	
	public static SSLContext getSLContext(FileInputStream trustFileInputStream,String trustPasswd,
			FileInputStream keyFileInputStream,String keyPasswd) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException{
		//ca
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
		KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.PKCS12);
		trustKeyStore.load(trustFileInputStream, HttpClientUtil.str2CharArray(trustPasswd));
		tmf.init(trustKeyStore);
		
		final char[]kp = HttpClientUtil.str2CharArray(keyPasswd);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SunX509);
		KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
		ks.load(keyFileInputStream, kp);
		kmf.init(ks, kp);
		
		SecureRandom rand = new SecureRandom();
		SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);
		
		return ctx;
		
		
	}
	/**
	 * 获取CA证书信息
	 * @param cafile	cafile CA证书文件
	 * @return 获取CA证书信息
	 * @throws CertificateException 
	 * @throws IOException 
	 */
	public static Certificate getCertificate(File cafile) throws CertificateException, IOException{
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(cafile);
		Certificate cert = cf.generateCertificate(in);
		in.close();
		return cert;
	}
	
	/**
     * 字符串转换成char数组
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if(null == str) return null;
         
        return str.toCharArray();
    }
    /**
     * 存储ca证书成JKS格式
     * @param cert
     * @param alias
     * @param password
     * @param out
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static void storeCACert(Certificate cert,String alias,String password,OutputStream out) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
    	KeyStore ks = KeyStore.getInstance(HttpClientUtil.JKS);
    	ks.load(null, null);
    	ks.setCertificateEntry(alias, cert);
    	ks.store(out, HttpClientUtil.str2CharArray(password));
    }
    
    public static InputStream String2Inputstream(String str){
    	return new ByteArrayInputStream(str.getBytes());
    }
}
