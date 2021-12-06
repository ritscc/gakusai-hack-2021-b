export type UserModel = {
  id: number;
  name: string;
  email: string;
  password: string;
  iconUrl: string;
};

export type UsersModel = {
  users: UserModel[];
};
