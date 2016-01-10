CREATE TABLE IF NOT EXISTS "projects" (
    "id" serial,
    "name" text,
    "slug" text UNIQUE,
    "description" text,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "complaints" (
    "id" serial,
    "name" text,
    "description" text,
    "likes" bigint DEFAULT '0',
    "project_id" bigint NOT NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("project_id") REFERENCES "projects"("id")
);

INSERT INTO projects(id, name, slug, description) VALUES(1, 'PHP', 'php', 'The worst language in the history of computer science');
INSERT INTO projects(id, name, slug, description) VALUES(2, 'Recruiters', 'recruiters', 'Whores are higher up the social ladder');
INSERT INTO projects(id, name, slug, description) VALUES(3, 'Ziggo', 'ziggo', 'Bringing bundles to your doorstep');

INSERT INTO complaints(name, description, project_id) VALUES('array_split', 'Seriously, who made this, someone with bipolar disorder?', 1);
INSERT INTO complaints(name, description, project_id) VALUES('magic_quotes', 'It''s like genocide, but in computer science. Hitler would like it', 1);
INSERT INTO complaints(name, description, project_id) VALUES('internets', 'This thing sucks like a cat that didn''t drink for a week', 2);
INSERT INTO complaints(name, description, project_id) VALUES('they hire idiots', 'It''s anecdotal evidence, but still...', 2);
INSERT INTO complaints(name, description, project_id) VALUES('they are stupid', 'You think Donald Trump is stupid? Think again.', 3);

SELECT setval('projects_id_seq', (SELECT MAX(id) FROM projects));