ALTER TABLE IF EXISTS todo
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN updated_at TIMESTAMP DEFAULT null;