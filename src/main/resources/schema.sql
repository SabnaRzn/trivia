CREATE TABLE IF NOT EXISTS public."trivia" ( "trivia_id" SERIAL PRIMARY KEY NOT NULL, "answer_attempts" integer NOT NULL, "correct_answer" character varying NOT NULL, "question" character varying NOT NULL);
ALTER TABLE public."trivia" OWNER TO postgres;
