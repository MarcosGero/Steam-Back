package com.steamer.capas.business.service.impl;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidatorService implements Predicate<String> {
    @Override
    public boolean test(String s) {
//        TODO: regex pa validar email
        return true;
    }
}
