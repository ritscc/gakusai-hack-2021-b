package dev.abelab.gifttree.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.Gift;
import dev.abelab.gifttree.db.entity.GiftExample;
import dev.abelab.gifttree.db.mapper.GiftMapper;

@RequiredArgsConstructor
@Repository
public class GiftRepository {

    private final GiftMapper giftMapper;

}
