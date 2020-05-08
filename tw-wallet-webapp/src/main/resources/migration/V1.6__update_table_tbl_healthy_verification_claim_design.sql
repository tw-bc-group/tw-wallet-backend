ALTER TABLE tbl_healthy_verification_claim ALTER COLUMN sub TYPE JSON USING sub::json;

ALTER TABLE tbl_healthy_verification_claim ADD owner VARCHAR(64) GENERATED ALWAYS AS (json_extract_path(sub, '$.id')) stored;