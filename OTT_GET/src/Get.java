import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class Get {
	public static void main(String[] args) {
		System.out.println(sendGet("http://192.168.1.162/dianying", "page=2"));
	}

	private static String sendGet(String url, String params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlname = url + "?" + params;
			URL realUrl = new URL(urlname);
			URLConnection conn = realUrl.openConnection();
		//	conn.setRequestProperty("page", "2");
			conn.connect();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	private void fun() {
		URL url;
		try {
			url = new URL("http://192.168.1.162/dianying");
			StringBuffer html = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("page", "2");
			String str = conn.getResponseMessage();
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp);
			}
			br.close(); // �ر�
			isr.close(); // �ر�
			System.out.println(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
