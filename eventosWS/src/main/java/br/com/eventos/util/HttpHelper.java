package br.com.eventos.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpHelper {
	public final int TIMEOUT_MILLIS = 15000;
	private String contentType;
	private String charsetToEncode;
	public static final String URL_UPLOAD = "https://api.imgur.com/3/image";

	public String uploadImage(String data, String tipoAutorizacao) throws IOException {

		URL u = new URL(URL_UPLOAD);
		HttpURLConnection conn = null;
		String s = null;
		try {
			conn = (HttpURLConnection) u.openConnection();

			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIMEOUT_MILLIS);
			conn.setReadTimeout(TIMEOUT_MILLIS);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Authorization", tipoAutorizacao);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.connect();

			if (data != null) {
				OutputStream out = conn.getOutputStream();
				OutputStreamWriter wr = new OutputStreamWriter(out);
				wr.write(data);
				wr.flush();
				out.close();
			}

			InputStream in = null;
			int status = conn.getResponseCode();
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
				in = conn.getErrorStream();
			} else {
				in = conn.getInputStream();
			}
			s = IOUtils.toString(in, "UTF-8");

			in.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return s;
	}

	public String doPost(String url, Map<String, String> params, String charset) throws IOException {
		String queryString = getQueryString(params);
		byte[] bytes = params != null ? queryString.getBytes(charset) : null;
		return doPost(url, bytes, charset);
	}

	public String doPost(String url, byte[] params, String charset) throws IOException {

		URL u = new URL(url);
		HttpURLConnection conn = null;
		String s = null;
		try {
			conn = (HttpURLConnection) u.openConnection();
			if (contentType != null) {
				conn.setRequestProperty("Content-Type", contentType);
			}
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIMEOUT_MILLIS);
			conn.setReadTimeout(TIMEOUT_MILLIS);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			if (params != null) {
				OutputStream out = conn.getOutputStream();
				out.write(params);
				out.flush();
				out.close();
			}
			InputStream in = null;
			int status = conn.getResponseCode();
			if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {

				in = conn.getErrorStream();
			} else {
				in = conn.getInputStream();
			}
			s = IOUtils.toString(in, charset);

			in.close();
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return s;
	}

	/**
	 * Retorna a QueryString para 'GET'
	 */
	public String getQueryString(Map<String, String> params) throws IOException {
		if (params == null || params.size() == 0) {
			return null;
		}
		String urlParams = null;
		for (String chave : params.keySet()) {
			Object objValor = params.get(chave);
			if (objValor != null) {
				String valor = objValor.toString();
				if (charsetToEncode != null) {
					valor = URLEncoder.encode(valor, charsetToEncode);
				}
				urlParams = urlParams == null ? "" : urlParams + "&";
				urlParams += chave + "=" + valor;
			}
		}
		return urlParams;

	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCharsetToEncode(String encode) {
		this.charsetToEncode = encode;
	}
}
