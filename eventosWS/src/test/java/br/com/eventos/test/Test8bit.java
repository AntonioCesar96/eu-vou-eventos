package br.com.eventos.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.tinify.Tinify;

public class Test8bit {
	public static void main(String[] args) throws Exception {

		Tinify.setKey("2ss7CTzZgLWHbUAwqHpdQhARHddpM9hQ");

		byte[] sourceData = Files.readAllBytes(Paths.get("/home/antonio/Imagens/1.png"));
		byte[] resultData = Tinify.fromBuffer(sourceData).toBuffer();

		BufferedImage image = ImageIO.read(new ByteArrayInputStream(resultData));
		ImageIO.write(image, "png", new File("/home/antonio/Imagens/optimized.png"));
	}
}
