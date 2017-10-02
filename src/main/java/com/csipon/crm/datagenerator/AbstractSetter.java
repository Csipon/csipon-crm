package com.csipon.crm.datagenerator;

import com.csipon.crm.service.security.RandomString;
import net.minidev.json.parser.JSONParser;

import java.util.List;
import java.util.Random;

/**
 * Created by Pasha on 05.05.2017.
 */
public abstract class AbstractSetter<T> {
    protected JSONParser parser = new JSONParser();
    protected RandomString randomString;
    protected Random random;

    public AbstractSetter() {
        randomString= new RandomString(50);
        random = new Random();
    }

    public abstract List<T> generate(int numbers);
    public abstract T generateObject();
}
