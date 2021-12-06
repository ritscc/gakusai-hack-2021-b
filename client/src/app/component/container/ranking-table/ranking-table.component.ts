import { Component, OnInit } from '@angular/core';
import { RankingService } from 'src/app/shared/service/ranking.service';
import { RankModel } from 'src/app/model/rank.model';

@Component({
  selector: 'app-ranking-table',
  templateUrl: './ranking-table.component.html',
  styleUrls: ['./ranking-table.component.css'],
})
export class RankingTableComponent implements OnInit {
  firstRank!: RankModel[];
  secondRank!: RankModel[];
  thirdRank!: RankModel[];
  loginUserRank!: RankModel[];

  constructor(private rankingService: RankingService) {}

  ngOnInit(): void {}
}
