package util;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;

public final class AnnotationValidator {
    private AnnotationValidator() {}

    public static void validate(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Positive.class)) {
                    if (field.getType() == long.class || field.getType() == int.class) {
                        long val = field.getLong(obj);
                        if (val < 0) {
                            throw new IllegalArgumentException("Field " + field.getName() + " must be positive.");
                        }
                    }
                }
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength ml = field.getAnnotation(MaxLength.class);
                    Object val = field.get(obj);
                    if (val instanceof String s && s.length() > ml.value()) {
                        throw new IllegalArgumentException("Field " + field.getName() + " exceeds max length: " + ml.value());
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}