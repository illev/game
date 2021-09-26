package com.game.service.impl;

import com.game.entity.Player;
import com.game.entity.PlayerSearchCriteria;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadRequestException;
import com.game.exception.NotFoundException;
import com.game.repository.PlayerCriteriaRepository;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerCriteriaRepository playerCriteriaRepository;

    @Override
    public Page<Player> findAll(PlayerSearchCriteria searchCriteria) {
        return playerCriteriaRepository.findAllByFilter(searchCriteria);
    }

    @Override
    public void deletePlayerById(Long id) {
        Player playerFromDb = getPlayer(id);
        playerRepository.deleteById(id);
    }

    @Override
    public Integer getCount(PlayerSearchCriteria searchCriteria) {
        return playerCriteriaRepository.findPlayersCount(searchCriteria);
    }

    @Override
    public Player getPlayer(Long id) {
        idValidator(id);
        return playerRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Player savePlayer(Player player) {
        nameValidator(player.getName());
        titleValidator(player.getTitle());
        raceValidator(player.getRace());
        professionValidator(player.getProfession());
        expValidator(player.getExperience());
        birthdayValidator(player.getBirthday());
        if (player.getBanned() == null) player.setBanned(false);
        player.setLevel(findCurrentLevel(player.getExperience()));
        player.setUntilNextLevel(findUntilNextLevel(player.getLevel(), player.getExperience()));
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        Player playerFromDb = getPlayer(id);
        String name = player.getName();
        if (name != null) {
            nameValidator(name);
            playerFromDb.setName(name);
        }

        String title = player.getTitle();
        if (title != null) {
            titleValidator(title);
            playerFromDb.setTitle(title);
        }

        Race race = player.getRace();
        if (race != null) {
            raceValidator(race);
            playerFromDb.setRace(race);
        }

        Profession profession = player.getProfession();
        if (profession != null) {
            professionValidator(profession);
            playerFromDb.setProfession(profession);
        }

        Integer exp = player.getExperience();
        if (exp != null) {
            expValidator(exp);
            playerFromDb.setExperience(exp);
        }

        Date birthday = player.getBirthday();
        if (birthday != null) {
            birthdayValidator(birthday);
            playerFromDb.setBirthday(birthday);
        }

        Boolean banned = player.getBanned();
        if (banned != null) {
            playerFromDb.setBanned(banned);
        }

        playerFromDb.setLevel(findCurrentLevel(playerFromDb.getExperience()));
        playerFromDb.setUntilNextLevel(findUntilNextLevel(playerFromDb.getLevel(), playerFromDb.getExperience()));

        playerRepository.save(playerFromDb);
        return playerFromDb;
    }

    private void nameValidator(String name) {
        if (name == null || name.isEmpty() || name.length() > 12)
            throw new BadRequestException();
    }

    private void idValidator(Long id) {
        if (id <= 0) {
            throw new BadRequestException();
        }
    }

    private void titleValidator(String title) {
        if (title.isEmpty() || title.length() > 30)
            throw new BadRequestException();
    }

    private void raceValidator(Race race){
        if (race == null)
            throw new BadRequestException();
    }

    private void professionValidator(Profession profession){
        if (profession == null)
            throw new BadRequestException();
    }

    private void expValidator(Integer exp) {
        if (exp == null || exp < 0 || exp > 10000000)
            throw new BadRequestException();
    }

    private void birthdayValidator(Date birthday){
        if (birthday == null)
            throw new BadRequestException();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday.getTime());
        if (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000)
            throw new BadRequestException();
    }

    private int findCurrentLevel(int exp) {
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    private int findUntilNextLevel(int lvl, int exp) {
        int iValue = 50 * (lvl + 1) * (lvl + 2) - exp;
        return iValue;
    }

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository,
                             PlayerCriteriaRepository playerCriteriaRepository) {
        this.playerRepository = playerRepository;
        this.playerCriteriaRepository = playerCriteriaRepository;
    }
}
