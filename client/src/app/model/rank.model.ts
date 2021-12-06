import { UserModel } from './user.model';

export type RankModel = {
  rank: number;
  score: number;
  user: UserModel;
};

export type RankingModel = {
  ranks: RankingModel[];
};
