package org.dmc.vottdotserver.models.domain.vottdot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveLearningSetting {
    private boolean autoDetect;
    private boolean predictTag;
    private String modelPathType;
}
