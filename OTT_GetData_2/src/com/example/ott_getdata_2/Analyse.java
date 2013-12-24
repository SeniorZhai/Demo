package com.example.ott_getdata_2;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Analyse extends DefaultHandler {
	int num = -1;
	String qName;
	SingleData data;
	public static void fun(String str,SingleData data) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new Analyse(data));
			reader.parse(new InputSource(new StringReader(str)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Analyse(SingleData data) {
		this.data = data;
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.qName = qName;
		if ("video".equals(qName)) {
			num++;
			data.dataList.add(new SmallData());
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length).trim();
		if ("title".equals(qName))
			data.dataList.get(num).setTitle(str);
		else if ("poster_url".equals(qName))
			data.dataList.get(num).setPoster_url(str);
		else if ("json_url".equals(qName))
			data.dataList.get(num).setJson_url(str);
	}
}
