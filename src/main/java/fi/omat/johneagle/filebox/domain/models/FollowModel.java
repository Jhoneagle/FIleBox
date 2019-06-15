package fi.omat.johneagle.filebox.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowModel {
    private Long follows;
    private boolean followed;
}
