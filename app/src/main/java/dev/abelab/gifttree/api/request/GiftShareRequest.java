package dev.abelab.gifttree.api.request;

import javax.validation.constraints.NotNull;

import lombok.*;

/**
 * ギフトおすそわけリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftShareRequest {

    /**
     * ギフトID
     */
    @NotNull
    Integer giftId;

}
