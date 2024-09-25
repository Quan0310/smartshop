package com.quanle.quan.service;

import com.quanle.quan.models.entity.OptionType;
import com.quanle.quan.models.entity.OptionValue;
import com.quanle.quan.models.request.OptionValueRequest;
import com.quanle.quan.repository.OptionTypeRepository;
import com.quanle.quan.repository.OptionValueRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
public class ProductOptionService {
    @Autowired
    OptionValueRepository optionValueRepository;

    @Autowired
    OptionTypeRepository optionTypeRepository;

    @Transactional
    public OptionType addOptionValue(OptionValueRequest optionValueRequest, String id){
        OptionValue optionValue = new OptionValue();

        OptionType optionType = optionTypeRepository.findById(id).get();

        optionValue.setValue(optionValueRequest.getValue());
        optionValue.setId(UUID.randomUUID().toString());

        optionValue.setOptionType(optionType);

        optionType.getOptionValues().add(optionValue);

        optionTypeRepository.save(optionType);

        return optionType;
    }
}
