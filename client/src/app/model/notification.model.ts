export type NotificationModel = {
  id: number;
  title: string;
  createdAt: Date;
};

export type NotificationsModel = {
  notifications: NotificationModel[];
};
