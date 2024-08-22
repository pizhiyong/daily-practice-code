package com.pizhiyong.dailypractice.masksdk.service;

import com.pizhiyong.dailypractice.masksdk.annotation.SensitiveMaskField;
import com.pizhiyong.dailypractice.masksdk.dao.SensitiveMaskDao;
import com.pizhiyong.dailypractice.masksdk.enums.SensitiveMaskHandleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SensitiveMaskService {

    @Autowired
    private SensitiveMaskDao sensitiveMaskDao;

    public void encrypt(Object obj) {
        if (null == obj) {
            return;
        }

        List<Field> aLlFields = getALlFields(obj);
        aLlFields.forEach(field -> {
            if (field.isAnnotationPresent(SensitiveMaskField.class)) {
                SensitiveMaskField annotation = field.getAnnotation(SensitiveMaskField.class);
                SensitiveMaskHandleType handleType = annotation.type();
                switch (handleType) {
                    case MASK_ALL:
                        break;
                    case MASK_NONE:
                        break;
                    case MASK_PART:
                        break;
                    default:
                        break;
                }
            }
        });
        // 1、获取目标对象的所有脱敏字段
        // 2、获取脱敏字段的脱敏类型
        // 3、执行脱敏操作
    }

    private List<Field> getALlFields(Object obj) {
        List<Field> fields = new ArrayList<>();
        Class<?> aClass = obj.getClass();
        while (aClass != null) {
            Field[] declaredFields = aClass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            aClass = aClass.getSuperclass();
        }
        return fields;
    }
}
