package br.com.eventos.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import javax.imageio.ImageIO;

public class PostImageImgur {

	public static void main(String[] args) throws Exception {

		
		String naoSei = getImgurContent("a01db5ceedb69cf9d9c4263ea7e3e4ce7b6d1d0f");
		
		System.out.println(naoSei);
	}

	public static String getImgurContent(String clientID) throws Exception {
		URL url;
		url = new URL("https://api.imgur.com/3/image");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// create base64 image
		BufferedImage image = null;
		File file = new File("/home/antonio/Imagens/769.png");
		// read image
		image = ImageIO.read(file);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ImageIO.write(image, "png", byteArray);
		byte[] byteImage = byteArray.toByteArray();
		String dataImage = Base64.getEncoder().encodeToString(byteImage);
		String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Bearer " + clientID);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		conn.connect();
		StringBuilder stb = new StringBuilder();
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			stb.append(line).append("\n");
		}
		wr.close();
		rd.close();

		return stb.toString();
	}
}
