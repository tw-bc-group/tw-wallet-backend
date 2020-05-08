ALTER TABLE tbl_healthy_verification_claim DROP COLUMN IF EXISTS owner;

ALTER TABLE tbl_healthy_verification_claim ADD owner VARCHAR(64) GENERATED ALWAYS AS (json_extract_path_text(sub, 'id')) stored;