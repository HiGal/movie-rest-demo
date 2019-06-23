package movie.rest.Models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Movie {
    @Id
    public ObjectId _id;

    public String title;
    public String Year;
    public String Rated;
    public String Released;
    public String Runtime;
    String Language;
    String Genre;
    String Director;
    String Writer;
    String Actors;
    String Plot;
    String Country;
    String Awards;
    String Poster;
    List<HashMap<String, String>> Ratings;
    String Metascore;
    String imdbRating;
    String imdbVotes;
    String imdbID;
    String Type;
    String DVD;
    String BoxOffice;
    String Production;
    String Website;
    String Response;


    public String get_id() {
        return _id.toHexString();
    }
}
