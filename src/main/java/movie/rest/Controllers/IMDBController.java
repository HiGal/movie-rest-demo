package movie.rest.Controllers;


import org.springframework.beans.factory.annotation.Value;

public class IMDBController {

    private String url;

    IMDBController(@Value("${OMDB_API_KEY}") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
