CREATE TYPE user_role AS ENUM (
    'ADMIN',
    'PREMIUM',
    'USER'
);

ALTER TABLE users
ADD COLUMN role user_role;

UPDATE users
SET role = 'USER'
WHERE role IS NULL;

ALTER TABLE users
ALTER COLUMN role SET NOT NULL;