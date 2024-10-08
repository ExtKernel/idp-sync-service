package com.iliauni.idpsyncservice.idp;

import com.iliauni.idpsyncservice.model.Usergroup;
import com.iliauni.idpsyncservice.service.UsergroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A class implementing {@link UsergroupDbSyncHandler} for synchronizing user groups
 * in a local database context.
 */
@Component
public class GenericUsergroupDbSyncHandler implements UsergroupDbSyncHandler {
    private final UsergroupService usergroupService;

    @Autowired
    public GenericUsergroupDbSyncHandler(UsergroupService usergroupService) {
        this.usergroupService = usergroupService;
    }

    @Override
    public void sync(Map<String, List<Usergroup>> differenceMap) {
        buildSyncFlagsMap().forEach((key, flags) -> {
            Boolean isNew = flags[0];
            Boolean isAltered = flags[1];

            differenceMap.getOrDefault(key, new ArrayList<>()).forEach(usergroup -> {
                if (isNew) {
                    usergroupService.save(Optional.of(usergroup));
                } else if (isAltered) {
                    usergroupService.update(Optional.of(usergroup));
                } else {
                    usergroupService.deleteById(usergroup.getName());
                }
            });
        });
    }

    /**
     * Builds a map containing boolean representations of flags,
     * which are supposed to be passed to synchronization methods.
     * Such as "new", "altered", "missing", etc.
     * The keys are meant to match the keys of difference map, generated by DifferenceCalculator.
     * This exists for the sake of reducing boilerplate and adding some scalability.
     *
     * @return a map of boolean representations of DifferenceCalculator map's flags
     */
    private Map<String, Boolean[]> buildSyncFlagsMap() {
        return Map.of(
                "new", new Boolean[]{true, false},
                "altered", new Boolean[]{false, true},
                "missing", new Boolean[]{false, false}
        );
    }
}
