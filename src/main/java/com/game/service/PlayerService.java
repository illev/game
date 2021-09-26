package com.game.service;

import com.game.entity.Player;
import com.game.entity.PlayerSearchCriteria;
import org.springframework.data.domain.Page;


public interface PlayerService {
    Page<Player> findAll(PlayerSearchCriteria searchCriteria);
    Integer getCount(PlayerSearchCriteria searchCriteria);
    Player getPlayer(Long id);
    void deletePlayerById(Long id);
    Player savePlayer(Player player);
    Player updatePlayer(Long id, Player player);
}
