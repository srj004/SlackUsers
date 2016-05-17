package slackchallenge.example.com.slackusers;

import java.util.ArrayList;
import java.util.List;

/**
 * Person POJO
 */
class Person {
    String name;
    String email;
    String title;
    String photoURL;
    String phoneNumber;
    String userId;
    String userName;
    String colorCode;

    Person(String name, String title, String email,
           String photoURL, String phoneNumber,
           String userId, String userName, String hexColor) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.photoURL = photoURL;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.userName = userName;
        this.colorCode = hexColor;
    }
}
