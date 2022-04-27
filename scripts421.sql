ALTER TABLE student ADD CONSTRAINT age_limit CHECK (age > 16);
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;
ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE(name);
ALTER TABLE student ALTER COLUMN name SET NOT NULL;
ALTER TABLE faculty ADD CONSTRAINT nameAndColor_unique UNIQUE(name, color);
