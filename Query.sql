SELECT DISTINCT p.*
FROM project p
         JOIN project_technologies pt ON p.id = pt.project_id
WHERE to_tsvector('english', p.title || ' ' || (
    SELECT string_agg(pt2.technology, ' ')
    FROM project_technologies pt2
    WHERE pt2.project_id = p.id
)) @@ to_tsquery('Flutter & Node.js');

