package fi.omat.johneagle.filebox.domain.enums;

/**
 * Tells who can see the file.
 */
public enum FileVisibility {
    EVERYONE("everyone"),
    FOLLOWERS("followers"),
    RESTRICT("restrict"),
    ME("me");

    private final String displayName;

    FileVisibility (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
