package fi.omat.johneagle.filebox.domain.jsonmodels;

import javax.validation.constraints.Positive;
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
    private FileVisibility fileVisibility;

    @Positive
    private Long id;
}
