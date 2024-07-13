package com.reader.manga.service;

import java.util.function.Consumer;

public class Utils {
    protected static <T> void updateField(T fieldValue, Consumer<T> setter) {
        if (fieldValue != null) {
            setter.accept(fieldValue);
        }
    }
}
