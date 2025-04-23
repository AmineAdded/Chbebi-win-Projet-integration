package app.chbebiwin.backend.entities.AgendaPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaPageRequest {
    public Long id;
    public int pageNumber;
    public String leftContent;
    public String rightContent;
    public Long utilisateurId;
}
