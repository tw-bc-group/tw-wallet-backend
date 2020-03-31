package com.thoughtworks.wallet.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }
}
