package ibradi.dev.avis.au;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Avis {
    private String message;
    private String nom;
    private LocalDateTime creation;
}
