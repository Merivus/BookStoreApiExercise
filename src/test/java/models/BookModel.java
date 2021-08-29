package models;

import io.restassured.RestAssured;
import lombok.*;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class BookModel {
    int id;
    String author;
    String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
