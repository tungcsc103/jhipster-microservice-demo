
CREATE TABLE IF NOT EXISTS viewLog (
    id uuid,
    userId text,
    entityType text,
    entityId text,
    date tuple<timestamp,varchar>,
    PRIMARY KEY(id)
);
