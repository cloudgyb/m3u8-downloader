CREATE TABLE IF NOT EXISTS "task"
(
    "id"                   integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "url"                  text    NOT NULL,
    "create_time"          DATE,
    "finished_time"        DATE,
    "total_media_segment"  integer,
    "finish_media_segment" integer,
    "download_duration"    integer,
    "file_path"            TEXT,
    "resolution"           TEXT,
    "max_thread_count"     integer,
    "stage"                TEXT,
    "status"               TEXT,
    "save_filename"        TEXT
);

CREATE TABLE IF NOT EXISTS "media_segment"
(
    "id"                INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "task_id"           INTEGER NOT NULL,
    "url"               TEXT    NOT NULL,
    "finished"          integer,
    "duration"          integer,
    "download_duration" integer,
    "file_path"         TEXT
);

CREATE TABLE IF NOT EXISTS "system_config"
(
    "id"                   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "download_dir"         TEXT,
    "default_thread_count" TEXT
);