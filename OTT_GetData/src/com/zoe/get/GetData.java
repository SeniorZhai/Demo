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
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����һ��
			// URLConnection��������ʾ��URL�����õ�Զ�̶�������ӡ�
			conn.setConnectTimeout(5000);
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "utf-8");// ���شӴ˴򿪵����Ӷ�ȡ����������
			BufferedReader br = new BufferedReader(isr);// ����һ��ʹ��Ĭ�ϴ�С���뻺�����Ļ����ַ���������
			String temp;
			while ((temp = br.readLine()) != null) { // ���ж�ȡ�����

				html.append(temp);
			}
			br.close(); // �ر�
			isr.close(); // �ر�

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