package movie.rest.Controllers;

import movie.rest.Models.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private MoviesController moviesController;


    @GetMapping("/")
    public String home(){
        return "hello";
    }

    @PostMapping("/search")
    public ModelAndView search(@RequestParam("search") String title, Map<String,Object> model){
        Movie result = null;
        try {
            result = moviesController.getMovieByTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.toString().equals("movie.rest.Exceptions.ResourceNotFoundException")){
                model.put("exception","404 NOT FOUND");
                return new ModelAndView("exception",model);
            }
        }
        model.put("result",result);
        return new ModelAndView("search",model);
    }

}
