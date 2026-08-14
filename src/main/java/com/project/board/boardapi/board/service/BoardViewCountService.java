package com.project.board.boardapi.board.service;

import com.project.board.boardapi.board.repository.BoardRepository;
import java.util.Map;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BoardViewCountService {
    private static final String INCREMENT_KEY = "board:view-count:increments";

    private final StringRedisTemplate redisTemplate;
    private final BoardRepository boardRepository;
    private final CacheManager cacheManager;

    public BoardViewCountService(StringRedisTemplate redisTemplate,
                                 BoardRepository boardRepository,
                                 CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.boardRepository = boardRepository;
        this.cacheManager = cacheManager;
    }

    public long increment(Long boardId, long databaseViewCount) {
        Long increment = redisTemplate.opsForHash()
                .increment(INCREMENT_KEY, String.valueOf(boardId), 1L);
        return databaseViewCount + (increment == null ? 0L : increment);
    }

    public long get(Long boardId, long databaseViewCount) {
        Object increment = redisTemplate.opsForHash()
                .get(INCREMENT_KEY, String.valueOf(boardId));
        return databaseViewCount + (increment == null ? 0L : Long.parseLong(increment.toString()));
    }

    public void delete(Long boardId) {
        redisTemplate.opsForHash().delete(INCREMENT_KEY, String.valueOf(boardId));
    }

    @Scheduled(fixedDelayString = "${board.view-count.sync-interval:1m}")
    public void syncToDatabase() {
        Map<Object, Object> increments = redisTemplate.opsForHash().entries(INCREMENT_KEY);
        if (increments.isEmpty()) return;

        for (Map.Entry<Object, Object> entry : increments.entrySet()) {
            String boardIdValue = entry.getKey().toString();
            long increment = Long.parseLong(entry.getValue().toString());
            if (increment <= 0L) continue;

            Long boardId = Long.valueOf(boardIdValue);
            try {
                redisTemplate.opsForHash()
                        .increment(INCREMENT_KEY, boardIdValue, -increment);
            } catch (RuntimeException exception) {
                continue;
            }

            try {
                int updatedRows = boardRepository.incrementViewCount(boardId, increment);
                if (updatedRows == 0) {
                    delete(boardId);
                    continue;
                }
            } catch (RuntimeException exception) {
                redisTemplate.opsForHash().increment(INCREMENT_KEY, boardIdValue, increment);
                continue;
            }

            evictBoardCache(boardId);
        }
    }

    private void evictBoardCache(Long boardId) {
        Cache cache = cacheManager.getCache("boardDetails");
        if (cache != null) cache.evict(boardId);
    }
}
