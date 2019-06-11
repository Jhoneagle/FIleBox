package fi.omat.johneagle.filebox.domain.jsonmodels;

import javax.validation.constraints.NotEmpty;
import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * For updating files visibility.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisibilityUpdate {
    @NotEmpty
    private FileVisibility fileVisibility;

    @NotEmpty
    private Long id;
}
