import { Component, OnInit } from '@angular/core';
import { RankingService } from 'src/app/shared/service/ranking.service';
import { UserRankModel } from 'src/app/model/rank.model';

@Component({
  selector: 'app-ranking-table',
  templateUrl: './ranking-table.component.html',
  styleUrls: ['./ranking-table.component.css'],
})
export class RankingTableComponent implements OnInit {
  firstRank!: UserRankModel[];
  secondRank!: UserRankModel[];
  thirdRank!: UserRankModel[];
  loginUserRank!: UserRankModel[];

  constructor(private rankingService: RankingService) {}

  ngOnInit(): void {}
}
