package movie.rest.Models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class Movie {
    @Id
    public ObjectId _id;

    public String title;
    public String description;
    public float rating;
    public Date release_date;


    public String get_id(){
        return _id.toHexString();
    }
}
