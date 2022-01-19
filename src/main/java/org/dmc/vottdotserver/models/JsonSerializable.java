package org.dmc.vottdotserver.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = "stackTrace", ignoreUnknown = true)
public interface JsonSerializable {
}
