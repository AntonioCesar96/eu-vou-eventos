package br.com.eventos.test;

import java.io.IOException;

import com.tinify.Tinify;

public class CompressTiny {
	public static void main(String[] args) throws IOException {
		Tinify.setKey("2ss7CTzZgLWHbUAwqHpdQhARHddpM9hQ");
		Tinify.fromFile("/home/antonio/Imagens/1.png").toFile("/home/antonio/Imagens/optimized.png");

		try {
			Tinify.setKey("2ss7CTzZgLWHbUAwqHpdQhARHddpM9hQ");
			Tinify.validate();
			System.out.println(Tinify.compressionCount());
		} catch (java.lang.Exception e) {
			System.out.println("Validation of API key failed.");
		}

	}
}
