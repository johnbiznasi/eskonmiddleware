CREATE TABLE temple (
  id SERIAL PRIMARY KEY,
  country TEXT,
  region TEXT,
  name TEXT,
  longitude DOUBLE PRECISION,
  latitude DOUBLE PRECISION,
  town TEXT,
  road TEXT
);

CREATE TABLE devotee (
  id SERIAL PRIMARY KEY,
  created_on timestamp
  status TEXT
);

CREATE TABLE mentor (
  id SERIAL PRIMARY KEY,
  first_name TEXT,
  last_name TEXT,
  status TEXT,
  region TEXT,
  temple TEXT,
  age INTEGER
);

CREATE TABLE mentor_login (
  id SERIAL PRIMARY KEY,
  username TEXT,
  password TEXT,
  status TEXT
);

CREATE TABLE mentor_sessions (
  id SERIAL PRIMARY KEY,
  mentor_id  integer
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  status TEXT
);

CREATE TABLE platform_users (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
CREATE INDEX idx_platform_users_username
ON public.platform_users (username);

CREATE TABLE platform_roles (
    roleid SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE,
    role_description TEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create   table  public_keys(account   int ,key text,id   serial   primary key, created_on  timestamp)




