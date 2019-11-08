package vn.pops.statsservive.service.mapper;

import vn.pops.statsservive.domain.*;
import vn.pops.statsservive.service.dto.UserStatsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserStats} and its DTO {@link UserStatsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UserStatsMapper extends EntityMapper<UserStatsDTO, UserStats> {



    default UserStats fromId(String id) {
        if (id == null) {
            return null;
        }
        UserStats userStats = new UserStats();
        userStats.setId(id);
        return userStats;
    }
}
