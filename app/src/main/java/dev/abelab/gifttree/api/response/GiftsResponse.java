package dev.abelab.gifttree.api.response;

import java.util.List;

import lombok.*;

/**
 * ギフト一覧レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftsResponse {

    List<GiftResponse> gifts;

}
