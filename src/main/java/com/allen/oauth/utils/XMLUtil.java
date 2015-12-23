package com.allen.oauth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.Doc;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLUtil {

	public static Map<String,String> doXMLParser(String strXml) throws JDOMException, IOException{
		if(null == strXml || "".equals(strXml)){
			return null;
		}
		
		Map<String,String> m = new HashMap<String,String>();
		InputStream in = HttpClientUtil.String2Inputstream(strXml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()){
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()){
				v = e.getTextNormalize();
			}else{
				v = XMLUtil.getChilrenText(children);
			}
			m.put(k, v);
		}
		in.close();
		return m;
		
	}
	
	public static String getChilrenText(List children){
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()){
			Iterator it = children.iterator();
			while(it.hasNext()){
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<"+name+">");
				if(!list.isEmpty()){
					sb.append(XMLUtil.getChilrenText(list));
				}
				sb.append(value);
				sb.append("</"+name+">");
			}
		}
		return sb.toString();
	}
	/**
	 * 获取XML编码字符集
	 * @param strXml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String getXMLEncoding(String strXml) throws JDOMException, IOException{
		InputStream in = HttpClientUtil.String2Inputstream(strXml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		in.close();
		return (String) doc.getProperty("encoding");
	}
}
