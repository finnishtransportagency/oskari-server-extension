CREATE TABLE oskari_announcements (
    id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    begin_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    active boolean NOT NULL
);