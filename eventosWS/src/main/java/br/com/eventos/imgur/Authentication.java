package br.com.eventos.imgur;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.eventos.dao.TokenDAO;
import br.com.eventos.model.Token;
import br.com.eventos.util.HttpHelper;

@Component
public class Authentication {

	private static final String TOKEN_URL = "https://api.imgur.com/oauth2/token";
	public static String client_id = "6d52de9f810ab22";
	public static String client_secret = "f334743d6d616472d34e086303fe0afcce7d27af";

	@Autowired
	private TokenDAO tokenDAO;

	public Token getToken() throws Exception {

		Token token = tokenDAO.getToken();

		if (token != null && token.getAccessToken() != null) {
			// Check token validity
			if (currentTimeSeconds() > token.getExpiresIn()) {

				System.out.println("Token expirado, solicitando novo token...");

				Token newToken = requestNewAccessToken(token);
				newToken.setId(token.getId());

				tokenDAO.update(newToken);

				return newToken;
			}
			return token;

		}
		throw new RuntimeException("Token nulo");
	}

	public Token requestNewAccessToken(Token oldToken) throws Exception {

		String refreshToken = oldToken.getRefreshToken();

		if (refreshToken == null) {
			throw new RuntimeException("Nenhum refresh token encontrado.");
		}

		Map<String, String> data = new HashMap<>();
		data.put("refresh_token", refreshToken);
		data.put("client_id", client_id);
		data.put("client_secret", client_secret);
		data.put("grant_type", "refresh_token");

		HttpHelper helper = new HttpHelper();
		String response = helper.doPost(TOKEN_URL, data, "UTF-8");

		try {
			Gson gson = new Gson();
			Token newToken = gson.fromJson(response, Token.class);

			// expires_in = seconds, update the counter
			long expireTime = newToken.getExpiresIn();

			newToken.setExpiresIn(currentTimeSeconds() + expireTime);

			return newToken;
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível atualizar o token de acesso do Imgur!", e);
		}
	}

	public long currentTimeSeconds() {
		return (System.currentTimeMillis() / 1000);
	}

	/*
	 * public static void main(String[] args) throws Exception {
	 * ClassPathXmlApplicationContext context = new
	 * ClassPathXmlApplicationContext("applicationContext.xml");
	 * AuthenticationUtil util = context.getBean(AuthenticationUtil.class);
	 * 
	 * 
	 * Token token = util.getToken();
	 * 
	 * System.out.println(token); }
	 */
}
