package br.com.eventos.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;

public class Download {
	public static void main(String[] args) {

		String urlUrl = "http://web.detran.ms.gov.br/sgdv_homo/download/SGDV_COM_JRE_1.5.2_x86.exe";

		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			// Esperado o cod. de resposta igual a 200 ou seja conexão OK
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println(
						"Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
			// Comprimento do conteudo em bytes
			double totalSize = connection.getContentLength();
			// download the file
			input = connection.getInputStream();
			output = new FileOutputStream("C:\\arquivos_para_instalar\\SGDV_COM_JRE_1.5.2_x86.exe");
			byte buffer[] = new byte[1];
			double downloadedSize = 0;
			int bufferLength;

			long time = (System.currentTimeMillis() / 1000) + 1;

			while ((bufferLength = input.read(buffer)) != -1) {

				downloadedSize += bufferLength;
				output.write(buffer, 0, bufferLength);
				if ((System.currentTimeMillis() / 1000) == time) {
					publishProgress(downloadedSize, totalSize);
					time = (System.currentTimeMillis() / 1000) + 1;
				}
			}
		} catch (Exception e) {

		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}
			if (connection != null)
				connection.disconnect();
		}
	}

	private static void publishProgress(double downloadedSize, double totalSize) {

		BigDecimal a = new BigDecimal((downloadedSize / 1024) / 1024);
		BigDecimal b = new BigDecimal((totalSize / 1024) / 1024);

		System.out.println((int) ((downloadedSize * 100) / totalSize) + "%");
		System.out.println("Baixado: " + a.setScale(3, RoundingMode.CEILING) + " MB | Total: "
				+ b.setScale(3, RoundingMode.CEILING) + " MB");
	}
}
