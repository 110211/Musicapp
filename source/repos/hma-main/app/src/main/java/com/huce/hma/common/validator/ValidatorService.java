package com.huce.hma.common.validator;

import com.huce.hma.common.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ValidatorService {
    public static ArrayList<String> validate(Object obj, Map<String, String> map) {
        ArrayList<String> results = new ArrayList<>();
        for (Map.Entry<String, String> entry: map.entrySet()) {
            for (String field: entry.getValue().split("\\|")) {
                switch (field) {
                    case "email":
                        if (!isEmail(Objects.requireNonNull(Utils.getPropertyValue(obj, entry.getKey())).toString())) {
                            results.add(entry.getKey() + " is not valid email address");
                            return results;
                        }
                        break;
                    case "required":
                        if (Utils.getPropertyValue(obj, entry.getKey()) == null || Objects.requireNonNull(Utils.getPropertyValue(obj, entry.getKey())).toString().isEmpty()) {
                            results.add(entry.getKey() + " is required");
                            return results;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return results;
    }

    public static boolean isEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
