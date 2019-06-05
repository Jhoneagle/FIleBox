package fi.omat.johneagle.filebox.domain.jsonmodels;

import fi.omat.johneagle.filebox.domain.enums.FileVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisibilityUpdate {
    @NotEmpty
    private FileVisibility fileVisibility;

    @NotEmpty
    private Long id;
}
