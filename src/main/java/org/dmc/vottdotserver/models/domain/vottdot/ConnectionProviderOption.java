package org.dmc.vottdotserver.models.domain.vottdot;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ConnectionProviderOption implements Serializable {
    private String url;
    private String taskId;
    private String taskServerUrl;
}
