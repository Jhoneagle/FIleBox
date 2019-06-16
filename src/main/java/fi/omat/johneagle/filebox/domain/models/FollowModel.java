package fi.omat.johneagle.filebox.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to indicate if person is been followed or not and how many follows he generally has.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowModel {
    private Long follows;
    private boolean followed;
}
