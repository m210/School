SELECT s.name as name, f.name as faculty FROM student as s RIGHT JOIN faculty as f on f.id = s.faculty_id;
SELECT s.name as name FROM student as s JOIN avatar as a on a.student_id = s.id;