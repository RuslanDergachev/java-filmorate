package ru.yandex.practicum.filmorate.model;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }

}
