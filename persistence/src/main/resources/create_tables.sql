create table USERS(
  ID BIGSERIAL PRIMARY KEY,
  USERNAME varchar(20) UNIQUE NOT NULL,
  EMAIL varchar(254) UNIQUE NOT NULL,
  PASSWORD varchar(50) NOT NULL
);

create table QUESTIONS (
  ID BIGSERIAL PRIMARY KEY,
  AUTHOR_ID BIGINT REFERENCES USERS(ID) NOT NULL,
  CONTENT VARCHAR(150) NOT NULL,
  CATEGORY VARCHAR(50) NOT NULL,
  QUESTION_TYPE VARCHAR(20) NOT NULL
);

create table ANSWERS (
  ID BIGSERIAL PRIMARY KEY,
  QUESTION_ID BIGINT NOT NULL ,
  CONTENT VARCHAR(60) NOT NULL,
  IS_CORRECT BOOLEAN NOT NULL
)