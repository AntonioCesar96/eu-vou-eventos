package br.com.eventos.service;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.imgur.Authentication;
import br.com.eventos.util.HttpHelper;

@Component
public class UploadService {

	@Autowired
	private Authentication authenticationUtil;

	public String upload(String base64) throws Exception {
		if (base64 == null) {
			throw new IllegalArgumentException("Parâmetros inválidos");
		}

		String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base64, "UTF-8");

		HttpHelper http = new HttpHelper();

		// Tenta fazer upload usando a minha conta
		String tipoAutorizacao = "Bearer " + authenticationUtil.getToken().getAccessToken();
		String retorno = http.uploadImage(data, tipoAutorizacao);

		JSONObject object = new JSONObject(retorno);
		JSONObject dataJson = object.getJSONObject("data");

		// Se der erro tenta fazer upload de forma anonima
		if (!object.getBoolean("success")) {
			tipoAutorizacao = "Client-ID " + Authentication.client_id;
			try {
				retorno = http.uploadImage(data, tipoAutorizacao);

				object = new JSONObject(retorno);
				dataJson = object.getJSONObject("data");

				if (!object.getBoolean("success")) {
					JSONObject error = dataJson.getJSONObject("error");
					throw new RuntimeException(error.getString("message"));
				}

				return dataJson.getString("link");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return dataJson.getString("link");
	}
}
