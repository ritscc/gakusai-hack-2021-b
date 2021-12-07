import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { UserRankModel, RankingModel } from 'src/app/model/rank.model';

@Injectable({
  providedIn: 'root',
})
export class RankingService {
  constructor(private http: HttpClient) {}

  getRanking(): Observable<UserRankModel[]> {
    return this.http
      .get<RankingModel>(`${environment.API_BASE_URL}/api/ranking`)
      .pipe(map((response) => response.userRanks));
  }
}
