package movie.rest.Controllers;


import com.google.gson.Gson;
import movie.rest.Exceptions.ResourceNotFoundException;
import movie.rest.Models.Movie;
import movie.rest.Repositories.MoviesRepository;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesController.class);

    @Autowired
    private MoviesRepository moviesRepository;

    @GetMapping("/")
    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") ObjectId id) {
        LOGGER.info("Fetching film by id");
        return moviesRepository.findBy_id(id);
    }

    @GetMapping("/title/{title}")
    public Movie getMovieByTitle(@PathVariable("title") String title) throws Exception {
        LOGGER.info("Fetching film data by title \"{}\" from our database", title);
        Movie result = moviesRepository.findMovieByTitle(title);
        title=title.replace(" ", "%20");
        if (result == null) {
            LOGGER.info("\"{}\" is not found in our database.", title);
            LOGGER.info("Referring to OMDBApi");
            String url = "http://www.omdbapi.com/?i=tt3896198&apikey=79e137c&t=" + title;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            if(jsonResponse.has("Error")){
                LOGGER.error("\"{}\" is not found in the referring API", title);
                throw new ResourceNotFoundException();
            }
            jsonResponse.put("title", jsonResponse.get("Title"));
            jsonResponse.remove("Title");
            if ((result = moviesRepository.findMovieByTitle(jsonResponse.get("title").toString())) == null) {
                LOGGER.info("\"{}\" was found in API",title);
                Gson gson = new Gson();
                result = gson.fromJson(jsonResponse.toString(), Movie.class);
                result.set_id(ObjectId.get());
                LOGGER.info("Writing data about film in our database");
                moviesRepository.save(result);
                LOGGER.info("Film was successfully written in database");
            }
        }
        return result;
    }

    @PutMapping("/{id}")
    public void modifyMovieById(@PathVariable("id") ObjectId id, @Valid
    @RequestBody Movie movie) {
        movie.set_id(id);
        moviesRepository.save(movie);
        LOGGER.info("Film with id \"{}\" has been modified", id);
    }

    @PutMapping("/title/{title}")
    public void modifyMovieByTitle(@PathVariable("title") String title, @Valid @RequestBody Movie movie){
        movie.setTitle(title);
        moviesRepository.save(movie);
        LOGGER.info("Film with title \"{}\" has been modified", title);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieById(@PathVariable("id") ObjectId id) {
        moviesRepository.delete(moviesRepository.findBy_id(id));
        LOGGER.info("Film with id \"{}\" has been deleted", id);
    }

    @DeleteMapping("/title/{title}")
    public void deleteMovieByTitle(@PathVariable("title") String title){
        moviesRepository.delete(moviesRepository.findMovieByTitle(title));
        LOGGER.info("Film with title \"{}\" has been deleted", title);
    }

    @PostMapping("/")
    public Movie addMovie(@Valid @RequestBody Movie movie) {
        ObjectId id = ObjectId.get();
        movie.set_id(id);
        moviesRepository.save(movie);
        LOGGER.info("Film with id \"{}\" has been added", id);

        return movie;
    }
}
