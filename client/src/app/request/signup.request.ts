export type SignUpRequest = {
  name: string;
  email: string;
  password: string;
  // NOTE: Base64でエンコードした画像
  icon: string;
};
