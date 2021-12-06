export type UserModel = {
  id: number;
  name: string;
  email: string;
  password: string;
};

export type UsersModel = {
  users: UserModel[];
};
