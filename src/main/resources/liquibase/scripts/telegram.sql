CREATE TABLE notification_task
(
    id      INTEGER PRIMARY KEY,
    chat_id INTEGER CHECK (chat_id > 0),
    message TEXT NOT NULL,
    datetime TIMESTAMP
);