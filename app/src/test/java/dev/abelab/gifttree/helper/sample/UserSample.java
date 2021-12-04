package dev.abelab.gifttree.helper.sample;

import java.util.Date;

import dev.abelab.gifttree.db.entity.User;

/**
 * User Sample Builder
 */
public class UserSample extends AbstractSample {

	public static UserSampleBuilder builder() {
		return new UserSampleBuilder();
	}

	public static class UserSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;
		private String email = SAMPLE_STR;
		private String password = SAMPLE_STR;
		private String iconUrl = SAMPLE_STR;
		private Date createdAt = SAMPLE_DATE;
		private Date updatedAt = SAMPLE_DATE;

		public UserSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public UserSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public UserSampleBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserSampleBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserSampleBuilder iconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
			return this;
		}

		public UserSampleBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserSampleBuilder updatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public User build() {
			return User.builder() //
				.id(this.id) //
				.name(this.name) //
				.email(this.email) //
				.password(this.password) //
				.iconUrl(this.iconUrl) //
				.createdAt(this.createdAt) //
				.updatedAt(this.updatedAt) //
				.build();
		}

	}

}
