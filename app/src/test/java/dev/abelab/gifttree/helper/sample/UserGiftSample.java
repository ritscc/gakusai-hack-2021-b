package dev.abelab.gifttree.helper.sample;

import java.util.Date;

import dev.abelab.gifttree.db.entity.UserGift;

/**
 * UserGift Sample Builder
 */
public class UserGiftSample extends AbstractSample {

	public static UserGiftSampleBuilder builder() {
		return new UserGiftSampleBuilder();
	}

	public static class UserGiftSampleBuilder {

		private Integer userId = SAMPLE_INT;
		private Integer giftId = SAMPLE_INT;
		private Integer quantity = SAMPLE_INT;
		private Integer type = SAMPLE_INT;
		private Date receivedAt = SAMPLE_DATE;
		private Integer receivedBy = SAMPLE_INT;

		public UserGiftSampleBuilder userId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public UserGiftSampleBuilder giftId(Integer giftId) {
			this.giftId = giftId;
			return this;
		}

		public UserGiftSampleBuilder quantity(Integer quantity) {
			this.quantity = quantity;
			return this;
		}

		public UserGiftSampleBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public UserGiftSampleBuilder receivedAt(Date receivedAt) {
			this.receivedAt = receivedAt;
			return this;
		}

		public UserGiftSampleBuilder receivedBy(Integer receivedBy) {
			this.receivedBy = receivedBy;
			return this;
		}

		public UserGift build() {
			return UserGift.builder() //
				.userId(this.userId) //
				.giftId(this.giftId) //
				.quantity(this.quantity) //
				.type(this.type) //
				.receivedAt(this.receivedAt) //
				.receivedBy(this.receivedBy) //
				.build();
		}

	}

}
