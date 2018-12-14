package com.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-ioc.xml", "classpath:spring-mvc.xml", "classpath:mybatis-config.xml" })
public class TestCode {

	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;

	@Test
	public void testProperties() {
		String url = "https://ys.51asj.com/aihudong-duoping-web/login/checkToken";
		String params = "token=63287d93efef2fe9eed2b951604f52e3e9eda01bbc9fce68&tenant=90003&user=Testzhang&sign=bccd6f99141ace1e5bf81df5fdb77122054df0bf";
		String result = sendPost(url, params);
		System.out.println(result);
	}

	@Test
	public void testPost() throws UnsupportedEncodingException {
		String url = "http://itraining.cet.buct.edu.cn:8080/HuadaLdapService.asmx/checkTokenInterfaces";

		/*
		 * String param =
		 * "{\"tenant\":\"A\",\"method\":\"B\",\"data\":{\"token\":\"C\",\"tenant\":\"D\",\"sign\":\"E \"}}"
		 * ;
		 * 
		 * param = param.replaceAll("A", "90002"); param = param.replaceAll("B",
		 * "checkToken"); param = param.replaceAll("C",
		 * "248c140632610017d0b5cac64ae1e53bbbff48281cce538e"); param =
		 * param.replaceAll("D", "90002"); param = param.replaceAll("E",
		 * "cc92a9d27299cef28729a0f2395f5bb9c4146562");
		 */
		JSONObject params = new JSONObject();
		params.put("tenant", "90002");
		params.put("method", "checkToken");
		Map<String, String> data = new TreeMap<>();
		data.put("token", "248c140632610017d0b5cac64ae1e53bbbff48281cce538e");
		data.put("tenant", "90002");
		data.put("sign", "cc92a9d27299cef28729a0f2395f5bb9c4146562");
		params.put("data", data);

		System.out.println("Jsondata=" + params);
		String param = "Jsondata=" + URLEncoder.encode(params.toString(), "UTF-8");

		String sendPost = sendPost(url, param);
		System.out.println(sendPost);

	}
	@Test
	public void test11() throws Exception {
		String urlPath = new String("http://itraining.cet.buct.edu.cn:8080/HuadaLdapService.asmx/checkTokenInterfaces");

		JSONObject params = new JSONObject();
		params.put("tenant", "90002");
		params.put("method", "checkToken");
		Map<String, String> data = new TreeMap<>();
		data.put("token", "248c140632610017d0b5cac64ae1e53bbbff48281cce538e");
		data.put("tenant", "90002");
		data.put("sign", "cc92a9d27299cef28729a0f2395f5bb9c4146562");
		params.put("data", data);

		System.out.println("Jsondata=" + params);
		String param = "Jsondata=" + URLEncoder.encode(params.toString(), "UTF-8");
		
		/*String param = "Jsondata=" + URLEncoder.encode("{\"tenant\":\"90002\"," + "\"method\":\"checkToken\","
				+ "\"data\":{\"token\":\"248c140632610017d0b5cac64ae1e53bbbff48281cce538e\"," + "\"tenant\":\"90002\","
				+ "\"sign\":\"cc92a9d27299cef28729a0f2395f5bb9c4146562\"}}", "UTF-8");*/

		// 建立连接
		URL url = new URL(urlPath);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		// 设置参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接

		// 设置请求属性
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		httpConn.setRequestProperty("Charset", "UTF-8");

		// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
		httpConn.connect();

		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		dos.writeBytes(param);
		dos.flush();
		dos.close();

		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			StringBuffer sb = new StringBuffer();
			String readLine = new String();
			
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine).append("\n");
			}
			System.out.println(sb);
			responseReader.close();

		}
	}

	public static String sendPost(String url, String params) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// POST方法
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			if (params != null) {
				/*
				 * StringBuilder param = new StringBuilder(); for (Map.Entry<String, String>
				 * entry : params.entrySet()) { if(param.length()>0){ param.append("&"); }
				 * param.append(entry.getKey()); param.append("=");
				 * param.append(entry.getValue());
				 * //System.out.println(entry.getKey()+":"+entry.getValue()); }
				 */
				// System.out.println("param:"+param.toString());
				out.write(params);
			}
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

}
