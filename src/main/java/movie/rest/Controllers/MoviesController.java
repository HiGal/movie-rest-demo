package movie.rest.Controllers;


import com.google.gson.Gson;
import movie.rest.Exceptions.ResourceNotFoundException;
import movie.rest.Models.Movie;
import movie.rest.Repositories.MoviesRepository;
import org.bson.types.ObjectId;
import org.json.JSONObject;
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

    @Autowired
    private MoviesRepository moviesRepository;

    @GetMapping("/")
    public List<Movie> getAllMovies() {
        return moviesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") ObjectId id) {
        return moviesRepository.findBy_id(id);
    }

    @GetMapping("/title/{title}")
    public Movie getMovieByTitle(@PathVariable("title") String title) throws Exception {
        title=title.replace(" ", "%20");
        Movie result = moviesRepository.findMovieByTitle(title);
        if (result == null) {
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
                throw new ResourceNotFoundException();
            }
            jsonResponse.put("title", jsonResponse.get("Title"));
            jsonResponse.remove("Title");
            if ((result = moviesRepository.findMovieByTitle(jsonResponse.get("title").toString())) == null) {
                Gson gson = new Gson();
                result = gson.fromJson(jsonResponse.toString(), Movie.class);
                result.set_id(ObjectId.get());
                moviesRepository.save(result);
            }
        }
        return result;
    }

    @PutMapping("/{id}")
    public void modifyMovieById(@PathVariable("id") ObjectId id, @Valid
    @RequestBody Movie movie) {
        movie.set_id(id);
        moviesRepository.save(movie);
    }

    @PutMapping("/title/{title}")
    public void modifyMovieByTitle(@PathVariable("title") String title, @Valid @RequestBody Movie movie){
        movie.setTitle(title);
        moviesRepository.save(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovieById(@PathVariable("id") ObjectId id) {
        moviesRepository.delete(moviesRepository.findBy_id(id));
    }

    @DeleteMapping("/title/{title}")
    public void deleteMovieByTitle(@PathVariable("title") String title){
        moviesRepository.delete(moviesRepository.findMovieByTitle(title));
    }

    @PostMapping("/")
    public Movie addMovie(@Valid @RequestBody Movie movie) {
        movie.set_id(ObjectId.get());
        moviesRepository.save(movie);
        return movie;
    }
}
