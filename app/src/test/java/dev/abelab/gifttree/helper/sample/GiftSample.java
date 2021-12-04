package dev.abelab.gifttree.helper.sample;

import java.util.Date;

import dev.abelab.gifttree.db.entity.Gift;

/**
 * Gift Sample Builder
 */
public class GiftSample extends AbstractSample {

	public static GiftSampleBuilder builder() {
		return new GiftSampleBuilder();
	}

	public static class GiftSampleBuilder {

		private Integer id = SAMPLE_INT;
		private String name = SAMPLE_STR;
		private String description = SAMPLE_STR;
		private Integer quantity = SAMPLE_INT;
		private String url = SAMPLE_STR;
		private Date createdAt = SAMPLE_DATE;
		private Date updatedAt = SAMPLE_DATE;

		public GiftSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public GiftSampleBuilder name(String name) {
			this.name = name;
			return this;
		}

		public GiftSampleBuilder description(String description) {
			this.description = description;
			return this;
		}

		public GiftSampleBuilder quantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public GiftSampleBuilder url(String url) {
			this.url = url;
			return this;
		}

		public GiftSampleBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public GiftSampleBuilder updatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public Gift build() {
			return Gift.builder() //
				.id(this.id) //
				.name(this.name) //
				.description(this.description) //
				.quantity(this.quantity) //
				.url(this.url) //
				.createdAt(this.createdAt) //
				.updatedAt(this.updatedAt) //
				.build();
		}

	}

}
