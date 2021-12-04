package dev.abelab.gifttree.helper.sample;

import java.util.Date;

import dev.abelab.gifttree.db.entity.Notification;

/**
 * Notification Sample Builder
 */
public class NotificationSample extends AbstractSample {

	public static NotificationSampleBuilder builder() {
		return new NotificationSampleBuilder();
	}

	public static class NotificationSampleBuilder {

		private Integer id = SAMPLE_INT;
		private Integer userId = SAMPLE_INT;
		private String title = SAMPLE_STR;
		private String description = SAMPLE_STR;
		private Date createdAt = SAMPLE_DATE;

		public NotificationSampleBuilder id(Integer id) {
			this.id = id;
			return this;
		}

		public NotificationSampleBuilder userId(Integer userId) {
			this.userId = userId;
			return this;
		}

		public NotificationSampleBuilder title(String title) {
			this.title = title;
			return this;
		}

		public NotificationSampleBuilder description(String description) {
			this.description = description;
			return this;
		}

		public NotificationSampleBuilder createdAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Notification build() {
			return Notification.builder() //
				.id(this.id) //
				.userId(this.userId) //
				.title(this.title) //
				.description(this.description) //
				.createdAt(this.createdAt) //
				.build();
		}

	}

}
