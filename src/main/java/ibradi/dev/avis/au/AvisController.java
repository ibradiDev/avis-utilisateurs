package ibradi.dev.avis.au;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "avis")
public class AvisController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Avis> search() {
        return List.of(new Avis("Débuter avec Spring", "Ibradi", LocalDateTime.now()), new Avis("Formation sur Spring", "Ibradi.dev", LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Avis avis) {

    }

}
