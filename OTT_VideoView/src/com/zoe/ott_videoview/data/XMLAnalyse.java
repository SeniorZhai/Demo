package com.zoe.ott_videoview.data;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XMLAnalyse extends DefaultHandler {
	int num = -1;
	private static final String TAG = "---Debug-TAG---";
	String qName;
	MovieData data;

	public static void fun(String str, MovieData data) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(new XMLAnalyse(data));
			reader.parse(new InputSource(new StringReader(str)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public XMLAnalyse(MovieData data) {
		this.data = data;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.qName = qName;
		if ("video".equals(qName)) {
			num++;
			data.dataList.add(new SingleMovieData());
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String str = new String(ch, start, length).trim();
		if ("title".equals(qName)) {
			data.dataList.get(num).setTitle(str);
//			Log.d(TAG, qName + "\t:\t" + str);
		} else if ("poster_url".equals(qName)) {
			data.dataList.get(num).setPoster_url(str);
//			Log.d(TAG, qName + "\t:\t" + str);
		}
		else if ("json_url".equals(qName)) {
			data.dataList.get(num).setJson_url(str);
//			Log.d(TAG, qName + "\t:\t" + str);
		}
	}
}
