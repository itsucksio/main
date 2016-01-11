CREATE TABLE IF NOT EXISTS "projects" (
    "id" serial,
    "name" text,
    "slug" text UNIQUE,
    "description" text,
    PRIMARY KEY ("id")
);
--;;
CREATE TABLE IF NOT EXISTS "complaints" (
    "id" serial,
    "name" text,
    "description" text,
    "likes" bigint DEFAULT '0',
    "project_id" bigint NOT NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("project_id") REFERENCES "projects"("id")
);
--;;
INSERT INTO projects(id, name, slug, description) VALUES
    (1, 'PHP', 'php', 'The worst language in the history of computer science'),
    (2, 'Recruiters', 'recruiters', 'Whores are higher up the social ladder'),
    (3, 'Ziggo', 'ziggo', 'Bringing bundles to your doorstep');
--;;
INSERT INTO complaints(name, description, project_id) VALUES
    ('array_split', 'Seriously, who made this, someone with bipolar disorder?', 1),
    ('magic_quotes', 'It''s like genocide, but in computer science. Hitler would like it', 1),
    ('internets', 'This thing sucks like a cat that didn''t drink for a week', 2),
    ('they hire idiots', 'It''s anecdotal evidence, but still...', 2),
    ('they are stupid', 'You think Donald Trump is stupid? Think again.', 3);
--;;
DO '
BEGIN
  -- this is a workaround! https://github.com/yogthos/migratus/issues/21
  PERFORM setval(''projects_id_seq'', (SELECT MAX(id) FROM projects));
END; ';