package com.game.entity;

import com.game.controller.PlayerOrder;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Date;

public class PlayerSearchCriteria {
    private String name;
    private String title;
    private Race race;
    private Profession profession;
    private Date after;
    private Date before;
    private Boolean banned;
    private Integer minExperience;
    private Integer maxExperience;
    private Integer minLevel;
    private Integer maxLevel;
    private PlayerOrder order;
    private Integer pageNumber;
    private Integer pageSize;
    private final Sort.Direction sortDirection = Sort.Direction.ASC;

    public PlayerSearchCriteria() {
    }

    public PlayerSearchCriteria(
            String name,
            String title,
            String race,
            String profession,
            Long after,
            Long before,
            String banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel,
            String order,
            Integer pageNumber,
            Integer pageSize
    ) {
        this.name = name;
        this.title = title;
        this.race = race != null ? Race.valueOf(race.toUpperCase()) : null;
        this.profession = profession != null ? Profession.valueOf(profession.toUpperCase()) : null;
        if (after != null) {
            Timestamp timestampAfter = new Timestamp(after);
            this.after = new Date(timestampAfter.getTime());
        } else this.after = null;
        if (before != null) {
            Timestamp timestampBefore = new Timestamp(before);
            this.before = new Date(timestampBefore.getTime());
        } else this.before = null;

        this.banned = banned != null ? Boolean.parseBoolean(banned) : null;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.order = order != null ? PlayerOrder.valueOf(order.toUpperCase()) : PlayerOrder.ID;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Race getRace() {
        return race;
    }

    public Profession getProfession() {
        return profession;
    }

    public PlayerOrder getOrder() {
        return order;
    }

    public Date getAfter() {
        return after;
    }

    public Date getBefore() {
        return before;
    }

    public Boolean getBanned() {
        return banned;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
