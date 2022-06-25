package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;

@Data
public class Genre {
    private int id;
    private String name;


}

