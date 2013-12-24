package com.zoe.get;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class GetData {
	public static void fun(String str) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new MyContentHandler());
			reader.parse(new InputSource(new StringReader(str)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getData() {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL("http://192.168.1.62/dianying");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 返回一个
			// URLConnection对象，它表示到URL所引用的远程对象的连接。
			conn.setConnectTimeout(5000);
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "utf-8");// 返回从此打开的连接读取的输入流。
			BufferedReader br = new BufferedReader(isr);// 创建一个使用默认大小输入缓冲区的缓冲字符输入流。
			String temp;
			while ((temp = br.readLine()) != null) { // 按行读取输出流

				html.append(temp);
			}
			br.close(); // 关闭
			isr.close(); // 关闭

			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void main(String[] args) {
		System.out.println(getData());
		fun(getData());
	}
}