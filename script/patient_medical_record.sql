ALTER TABLE patient_medical_record
    ADD PRIMARY KEY (emr_id, appointment_date);

-- Step 3: Convert table to partitioned (by month)
ALTER TABLE patient_medical_record
    PARTITION BY RANGE COLUMNS (appointment_date) (
    PARTITION p2025_01 VALUES LESS THAN ('2025-02-01'),
    PARTITION p2025_02 VALUES LESS THAN ('2025-03-01'),
    PARTITION p2025_03 VALUES LESS THAN ('2025-04-01'),
    PARTITION p2025_04 VALUES LESS THAN ('2025-05-01'),
    PARTITION p2025_05 VALUES LESS THAN ('2025-06-01'),
    PARTITION p2025_06 VALUES LESS THAN ('2025-07-01'),
    PARTITION p2025_07 VALUES LESS THAN ('2025-08-01'),
    PARTITION p2025_08 VALUES LESS THAN ('2025-09-01'),
    PARTITION p2025_09 VALUES LESS THAN ('2025-10-01'),
    PARTITION p2025_10 VALUES LESS THAN ('2025-11-01'),
    PARTITION p2025_11 VALUES LESS THAN ('2025-12-01'),
    PARTITION p2025_12 VALUES LESS THAN ('2026-01-01')
    );
