SELECT DISTINCT p.*
FROM project p
         JOIN project_technologies pt ON p.id = pt.project_id
WHERE to_tsvector('english', p.title || ' ' || (
    SELECT string_agg(pt2.technology, ' ')
    FROM project_technologies pt2
    WHERE pt2.project_id = p.id
)) @@ to_tsquery('Flutter & Node.js');


---------------------------------------
-- Create a function to update the search_vector column
CREATE OR REPLACE FUNCTION update_search_vector() RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector =
            setweight(to_tsvector('english', NEW.title), 'A') ||
            setweight(to_tsvector('english', array_to_string(NEW.technologies_used, ' ')), 'B');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger to call the function on INSERT or UPDATE
CREATE TRIGGER project_search_vector_update
    BEFORE INSERT OR UPDATE ON Project
    FOR EACH ROW EXECUTE FUNCTION update_search_vector();