package movie.rest.Repositories;

import movie.rest.Models.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MoviesRepository extends MongoRepository<Movie, String> {
    Movie findBy_id(ObjectId _id);

    Movie findByTitle(String title);
}
