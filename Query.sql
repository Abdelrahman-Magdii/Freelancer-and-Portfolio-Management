SELECT DISTINCT p.*
FROM projects p
         JOIN project_technologies pt ON p.id = pt.project_id
WHERE to_tsvector('english', p.title || ' ' || (
    SELECT string_agg(pt2.technology, ' ')
    FROM project_technologies pt2
    WHERE pt2.project_id = p.id
)) @@ to_tsquery('Flutter & Node.js');


---------------------------------------
ALTER TABLE Projects ADD COLUMN search_vector tsvector;

-- Index for faster full-text search
CREATE INDEX idx_project_search ON projects USING GIN (search_vector);

-- Function to update search_vector
CREATE OR REPLACE FUNCTION update_search_vector() RETURNS TRIGGER AS $$
DECLARE
    tech_text TEXT;
BEGIN
    -- Fetch technologies as a space-separated string
    SELECT array_to_string(ARRAY(
                                   SELECT technology FROM project_technologies WHERE project_id = NEW.id
                           ), ' ') INTO tech_text;

    -- Ensure technologies are not null
    tech_text := COALESCE(tech_text, '');

    -- Update search_vector with title (Weight 'A') and technologies (Weight 'B')
    NEW.search_vector =
            setweight(to_tsvector('english', COALESCE(NEW.title, '')), 'A') ||
            CASE
                WHEN tech_text <> '' THEN setweight(to_tsvector('english', tech_text), 'B')
                ELSE ''::tsvector
                END;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



-- Trigger to automatically update search_vector on insert/update

-- DROP TRIGGER IF EXISTS trg_update_search_vector ON projects;

CREATE TRIGGER trg_update_search_vector
    BEFORE INSERT OR UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION update_search_vector();