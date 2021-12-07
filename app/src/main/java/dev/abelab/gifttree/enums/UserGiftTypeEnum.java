package dev.abelab.gifttree.enums;

import lombok.*;

/**
 * The enum user gift type
 */
@Getter
@AllArgsConstructor
public enum UserGiftTypeEnum {

    /**
     * 店舗受け取り
     */
    STORE(1),

    /**
     * 友人
     */
    FRIEND(2);

    private final int id;

}
