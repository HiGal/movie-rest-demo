package movie.rest.Controllers;

import movie.rest.Exceptions.ResourceNotFoundException;
import movie.rest.Models.Movie;
import movie.rest.Repositories.MoviesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private MoviesRepository moviesRepository;

    @GetMapping("/")
    public List<Movie> getAllPets() {
        return moviesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") ObjectId id) {
        return moviesRepository.findBy_id(id);
    }

    @GetMapping("/title/{title}")
    public Movie getMovieByTitle(@PathVariable("title") String title) throws Exception {
        Movie result = moviesRepository.findByTitle(title);
//        if(result == null){
//
//        }
        return moviesRepository.findByTitle(title);
    }

    @PutMapping("/{id}")
    public void modifyMovieById(@PathVariable("id") ObjectId id, @Valid
    @RequestBody Movie movie) {
        movie.set_id(id);
        moviesRepository.save(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieById(@PathVariable("id") ObjectId id){
        moviesRepository.delete(moviesRepository.findBy_id(id));
    }

    @PostMapping("/")
    public Movie addMovie(@Valid @RequestBody Movie movie){
        movie.set_id(ObjectId.get());
        moviesRepository.save(movie);
        return movie;
    }
}
