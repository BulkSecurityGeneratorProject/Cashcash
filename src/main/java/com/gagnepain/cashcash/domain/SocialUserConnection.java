package com.gagnepain.cashcash.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Social user.
 */
@Entity
@Table(name = "jhi_social_user_connection")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialUserConnection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "user_id",
			length = 255,
			nullable = false)
	private String userId;

	@NotNull
	@Column(name = "provider_id",
			length = 255,
			nullable = false)
	private String providerId;

	@NotNull
	@Column(name = "provider_user_id",
			length = 255,
			nullable = false)
	private String providerUserId;

	@NotNull
	@Column(nullable = false)
	private Long rank;

	@Column(name = "display_name",
			length = 255)
	private String displayName;

	@Column(name = "profile_url",
			length = 255)
	private String profileURL;

	@Column(name = "image_url",
			length = 255)
	private String imageURL;

	@NotNull
	@Column(name = "access_token",
			length = 255,
			nullable = false)
	private String accessToken;

	@Column(length = 255)
	private String secret;

	@Column(name = "refresh_token",
			length = 255)
	private String refreshToken;

	@Column(name = "expire_time")
	private Long expireTime;

	public SocialUserConnection() {
	}

	public SocialUserConnection(final String userId, final String providerId, final String providerUserId, final Long rank,
			final String displayName, final String profileURL, final String imageURL, final String accessToken, final String secret,
			final String refreshToken, final Long expireTime) {
		this.userId = userId;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.rank = rank;
		this.displayName = displayName;
		this.profileURL = profileURL;
		this.imageURL = imageURL;
		this.accessToken = accessToken;
		this.secret = secret;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(final String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(final String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(final Long rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public String getProfileURL() {
		return profileURL;
	}

	public void setProfileURL(final String profileURL) {
		this.profileURL = profileURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(final String imageURL) {
		this.imageURL = imageURL;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(final String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(final String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(final String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(final Long expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SocialUserConnection user = (SocialUserConnection) o;

		if (!id.equals(user.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "SocialUserConnection{" + "id=" + id + ", userId=" + userId + ", providerId='" + providerId + '\'' + ", providerUserId='" +
				providerUserId + '\'' + ", rank=" + rank + ", displayName='" + displayName + '\'' + ", profileURL='" + profileURL + '\'' +
				", imageURL='" + imageURL + '\'' + ", accessToken='" + accessToken + '\'' + ", secret='" + secret + '\'' +
				", refreshToken='" + refreshToken + '\'' + ", expireTime=" + expireTime + '}';
	}
}
