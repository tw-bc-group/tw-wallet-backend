ALTER TABLE tbl_healthy_verification_claim DROP COLUMN IF EXISTS signature;

ALTER TABLE tbl_healthy_verification_claim ADD signature VARCHAR(255);