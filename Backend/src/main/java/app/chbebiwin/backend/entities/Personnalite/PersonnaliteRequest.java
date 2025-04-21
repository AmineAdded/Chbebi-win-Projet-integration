package app.chbebiwin.backend.entities.Personnalite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonnaliteRequest {
    public Long type;
    public Long id;
}
