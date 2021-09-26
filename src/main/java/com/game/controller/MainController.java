package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerSearchCriteria;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class MainController {
    private final PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getPlayersList(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> race,
            @RequestParam Optional<String> profession,
            @RequestParam Optional<Long> after,
            @RequestParam Optional<Long> before,
            @RequestParam Optional<String> banned,
            @RequestParam Optional<Integer> minExperience,
            @RequestParam Optional<Integer> maxExperience,
            @RequestParam Optional<Integer> minLevel,
            @RequestParam Optional<Integer> maxLevel,
            @RequestParam Optional<String> order,
            @RequestParam Optional<Integer> pageNumber,
            @RequestParam Optional<Integer> pageSize
    ) {

        PlayerSearchCriteria searchCriteria = new PlayerSearchCriteria(
                name.orElse(null),
                title.orElse(null),
                race.orElse(null),
                profession.orElse(null),
                after.orElse(null),
                before.orElse(null),
                banned.orElse(null),
                minExperience.orElse(null),
                maxExperience.orElse(null),
                minLevel.orElse(null),
                maxLevel.orElse(null),
                order.orElse(null),
                pageNumber.orElse(0),
                pageSize.orElse(3)

        );

        return playerService.findAll(searchCriteria).getContent();
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> race,
            @RequestParam Optional<String> profession,
            @RequestParam Optional<Long> after,
            @RequestParam Optional<Long> before,
            @RequestParam Optional<String> banned,
            @RequestParam Optional<Integer> minExperience,
            @RequestParam Optional<Integer> maxExperience,
            @RequestParam Optional<Integer> minLevel,
            @RequestParam Optional<Integer> maxLevel
    ) {

        PlayerSearchCriteria searchCriteria = new PlayerSearchCriteria(
                name.orElse(null),
                title.orElse(null),
                race.orElse(null),
                profession.orElse(null),
                after.orElse(null),
                before.orElse(null),
                banned.orElse(null),
                minExperience.orElse(null),
                maxExperience.orElse(null),
                minLevel.orElse(null),
                maxLevel.orElse(null),
                null,
                null,
                null
        );

        return playerService.getCount(searchCriteria);
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Long id) {
        Player playerFromDb = playerService.getPlayer(id);
        return playerFromDb;
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayerById(id);
    }

    @PostMapping("/players")
    public Player createPlayer(
            @RequestBody Player player
    ) {
        return playerService.savePlayer(player);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable Long id,
                               @RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(id, player);
        return updatedPlayer;
    }

    @Autowired
    public MainController(PlayerService playerService) {
        this.playerService = playerService;
    }
}
