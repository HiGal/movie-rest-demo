package movie.rest.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no such a film")
public class ResourceNotFoundException extends RuntimeException{

}
