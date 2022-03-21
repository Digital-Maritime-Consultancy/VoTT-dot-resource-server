package org.dmc.vottdotserver.models.domain.vottdot;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Connection implements Serializable {
    private String name;
    private String providerType;
    private ConnectionProviderOption providerOptions;
}
