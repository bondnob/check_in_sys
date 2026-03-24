ALTER TABLE sign_tasks
    CHANGE COLUMN status sign_type TINYINT NULL DEFAULT 1 COMMENT '1-二维码签到, 0-定位签到';

ALTER TABLE sign_tasks
    ADD COLUMN qr_code VARCHAR(128) NULL COMMENT '二维码签到口令' AFTER sign_type,
    ADD COLUMN latitude DECIMAL(10, 6) NULL COMMENT '签到中心纬度' AFTER qr_code,
    ADD COLUMN longitude DECIMAL(10, 6) NULL COMMENT '签到中心经度' AFTER latitude,
    ADD COLUMN radius INT NULL COMMENT '允许签到半径(米)' AFTER longitude,
    ADD COLUMN late_time DATETIME NULL COMMENT '迟到判定时间' AFTER radius;

ALTER TABLE sign_records
    ADD COLUMN latitude DECIMAL(10, 6) NULL COMMENT '签到时纬度' AFTER location,
    ADD COLUMN longitude DECIMAL(10, 6) NULL COMMENT '签到时经度' AFTER latitude;
