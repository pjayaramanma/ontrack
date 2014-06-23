package net.nemerosa.ontrack.extension.svn.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SVNChangeLogRevision {

    private final String path;
    private final int level;
    private final long revision;
    private final String author;
    private final LocalDateTime revisionDate;
    private final String message;
    private final String revisionUrl;
    private final String formattedMessage;

}
