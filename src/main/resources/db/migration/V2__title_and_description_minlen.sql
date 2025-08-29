
ALTER TABLE courses
  ADD CONSTRAINT ck_courses_title_min_len
  CHECK (char_length(trim(title)) >= 3);


ALTER TABLE courses
  ADD CONSTRAINT ck_courses_description_min_len
  CHECK (description IS NULL OR char_length(trim(description)) >= 3);