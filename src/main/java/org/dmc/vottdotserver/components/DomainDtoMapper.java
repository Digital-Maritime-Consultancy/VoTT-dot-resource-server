package org.dmc.vottdotserver.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

@Component
public class DomainDtoMapper <F, T> {

    /**
     * The Model Mapper.
     */
    @Autowired
    ModelMapper modelMapper;

    /**
     * Returns the model mapper.
     *
     * @return the model mapper
     */
    public ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    /**
     * Map the FROM object into a TO object.
     *
     * @param fromObj           The FROM object
     * @param clazz             The class to map the FROM object to
     * @return the mapped TO object
     */
    @Transactional
    public T convertTo(F fromObj, Class<T> clazz) {
        return this.modelMapper.map(fromObj, clazz);
    }

}
