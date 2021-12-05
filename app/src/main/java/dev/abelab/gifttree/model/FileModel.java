package dev.abelab.gifttree.model;

import java.util.UUID;

import lombok.*;

/**
 * ファイルモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileModel {

    /**
     * ファイル名
     */
    @Builder.Default
    String name = UUID.randomUUID().toString();

    /**
     * ファイルのバイナリ
     */
    byte[] content;

}
