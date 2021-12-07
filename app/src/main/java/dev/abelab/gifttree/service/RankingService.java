package dev.abelab.gifttree.service;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.response.RankingResponse;
import dev.abelab.gifttree.api.response.UserRankResponse;
import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.repository.NotificationRepository;
import dev.abelab.gifttree.db.entity.User;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    /**
     * ランキングを取得
     *
     * @param loginUser ログインユーザ
     *
     * @return ランキングレスポンス
     */
    @Transactional
    public RankingResponse getRanking(final User loginUser) {
        final var users = this.userRepository.selectAll();
        final var userRankResponses = users.stream().map(user -> {
            // NOTE: 通知の数がスコアになる
            final var notifications = this.notificationRepository.selectByUserId(user.getId());

            return UserRankResponse.builder() //
                .score(notifications.size()) //
                .user(this.modelMapper.map(user, UserResponse.class)) //
                .build();
        }) //
            .sorted(Comparator.comparing(UserRankResponse::getScore).reversed()) //
            .collect(Collectors.toList());

        IntStream.range(0, userRankResponses.size()).boxed().forEach(rank -> {
            userRankResponses.get(rank).setRank(rank + 1);
        });

        return new RankingResponse(userRankResponses);
    }

}
