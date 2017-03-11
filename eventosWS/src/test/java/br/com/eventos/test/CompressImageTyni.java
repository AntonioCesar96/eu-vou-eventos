package br.com.eventos.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import javax.imageio.ImageIO;

public class CompressImageTyni {

	public static void main(String[] args) throws Exception {

		String chave = Base64.getEncoder().encodeToString("api:2ss7CTzZgLWHbUAwqHpdQhARHddpM9hQ".getBytes());
		
		String naoSei = compress(chave);
		
		System.out.println(naoSei);
	}

	public static String compress(String clientID) throws Exception {
		URL url;
		url = new URL("https://api.tinify.com/shrink");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// create base64 image
		BufferedImage image = null;
		File file = new File("/home/antonio/Imagens/769.png");
		// read image
		image = ImageIO.read(file);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ImageIO.write(image, "png", byteArray);
		byte[] byteImage = byteArray.toByteArray();

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Basic " + clientID);
		conn.setRequestProperty("Content-Type", "application/octet-stream");

		conn.connect();
		StringBuilder stb = new StringBuilder();
		OutputStream wr = conn.getOutputStream();
		wr.write(byteImage);
		wr.flush();
		wr.close();
		
		InputStream in = null;
		int status = conn.getResponseCode();
		if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
			in = conn.getErrorStream();
		} else {
			in = conn.getInputStream();
		}

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = rd.readLine()) != null) {
			stb.append(line).append("\n");
		}
		wr.close();
		rd.close();

		return stb.toString();
	}
}
