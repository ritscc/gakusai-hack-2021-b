package dev.abelab.gifttree.api.response;

import java.util.List;

import lombok.*;

/**
 * ランキングレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponse {

    /**
     * ユーザランク一覧
     */
    List<UserRankResponse> userRanks;

}
