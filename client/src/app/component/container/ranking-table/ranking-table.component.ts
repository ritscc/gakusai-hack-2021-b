import { Component, OnInit } from '@angular/core';
import { RankingService } from 'src/app/shared/service/ranking.service';
import { UserService } from 'src/app/shared/service/user.service';
import { AlertService } from 'src/app/shared/service/alert.service';
import { UserRankModel } from 'src/app/model/rank.model';

export type Trophy = {
  color: string;
  userRank: UserRankModel | undefined;
};

@Component({
  selector: 'app-ranking-table',
  templateUrl: './ranking-table.component.html',
  styleUrls: ['./ranking-table.component.css'],
})
export class RankingTableComponent implements OnInit {
  userRanks: UserRankModel[] = [];
  loginUserRank!: UserRankModel;
  trophies: Trophy[] = [];

  constructor(
    private rankingService: RankingService,
    private userService: UserService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.rankingService.getRanking().subscribe(
      (userRanks) => {
        this.userRanks = userRanks;

        this.trophies = [
          {
            color: 'trophy-gold',
            userRank: this.userRanks[0],
          },
          {
            color: 'trophy-silver',
            userRank: this.userRanks[1],
          },

          {
            color: 'trophy-copper',
            userRank: this.userRanks[2],
          },
        ];

        this.userService.getLoginUser().subscribe((loginUser) => {
          userRanks.forEach((userRank) => {
            if (userRank.user.id === loginUser.id) {
              this.loginUserRank = userRank;
            }
          });
        });
      },
      (error) => {
        this.alertService.openSnackBar(error, 'ERROR');
      }
    );
  }
}
