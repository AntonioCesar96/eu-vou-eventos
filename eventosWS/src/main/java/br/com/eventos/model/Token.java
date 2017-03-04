package br.com.eventos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.gson.annotations.SerializedName;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "token")
public class Token {

	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String scope;

	@SerializedName("account_id")
	@Column(name = "account_id")
	private Long accounId;

	@SerializedName("account_username")
	@Column(name = "account_username")
	private String accountUsername;

	@SerializedName("token_type")
	@Column(name = "token_type")
	private String tokenType;

	@SerializedName("expires_in")
	@Column(name = "expires_in")
	private Long expiresIn;

	@SerializedName("refresh_token")
	@Column(name = "refresh_token")
	private String refreshToken;

	@SerializedName("access_token")
	@Column(name = "access_token")
	private String accessToken;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getAccounId() {
		return accounId;
	}

	public void setAccounId(Long accounId) {
		this.accounId = accounId;
	}

	public String getAccountUsername() {
		return accountUsername;
	}

	public void setAccountUsername(String accountUsername) {
		this.accountUsername = accountUsername;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "ImgurAuthenticationObject [scope=" + scope + ", accounId=" + accounId + ", accountUsername="
				+ accountUsername + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn + ", refreshToken="
				+ refreshToken + ", accessToken=" + accessToken + "]";
	}

}
