ALTER TABLE manga
MODIFY COLUMN description VARCHAR(5000) NOT NULL,
MODIFY COLUMN image VARCHAR(255) NOT NULL;