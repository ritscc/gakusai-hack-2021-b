import { UserModel } from './user.model';

export type UserRankModel = {
  rank: number;
  score: number;
  user: UserModel;
};

export type RankingModel = {
  userRanks: UserRankModel[];
};
