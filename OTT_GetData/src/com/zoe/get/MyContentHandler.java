package com.zoe.get;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyContentHandler extends DefaultHandler {
	private String tagName, title, poster_url, json_url;

	@Override
	public void startDocument() throws SAXException {
		// ��ʼ����
	}

	@Override
	public void endDocument() throws SAXException {
		// ��ɽ���
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = qName;
//		System.out.println(tagName);
//		System.out.println(qName);
//		if (localName.equals("video")) {
//			// ��ȡ��ǩ����������
//			for (int i = 0; i < attributes.getLength(); i++) {
//				System.out.println(attributes.getLocalName(i) + "="
//						+ attributes.getValue(i));
//			}
//		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	//	printf();
	}


	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String string = new String(ch, start, length).trim();
	//	System.out.println(string);
		// ch��ȡ�������ݣ�start��ȡ�����ݴ�ch[start]��ʼ
		if("title".equals(tagName))
			System.out.println(string);
	}
}
