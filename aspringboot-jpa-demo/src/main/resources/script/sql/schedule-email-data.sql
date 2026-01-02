-- data.sql
-- Insert initial reference data
INSERT INTO audit_logs (user_id, action, resource_type, resource_id, status, ip_address, timestamp, details, created_by, updated_by)
VALUES 
('system.admin', 'SYSTEM_STARTUP', 'Application', 'AUDIT_SYSTEM', 'SUCCESS', '127.0.0.1', CURRENT_TIMESTAMP, 'Application startup completed', 'SYSTEM', 'SYSTEM'),
('system.admin', 'DATABASE_INIT', 'Database', 'AUDIT_DB', 'SUCCESS', '127.0.0.1', CURRENT_TIMESTAMP, 'Database schema initialized', 'SYSTEM', 'SYSTEM');

-- Insert sample daily report data
INSERT INTO audit_daily_reports (report_date, total_operations, successful_operations, failed_operations, top_users_json, operations_by_type_json, generated_at, status, created_by, updated_by)
VALUES 
('2024-01-15', 150, 145, 5, '{"admin.user": 50, "john.doe": 30, "jane.smith": 25}', '{"LOGIN": 70, "VIEW_ACCOUNT": 45, "TRANSACTION": 35}', CURRENT_TIMESTAMP, 'GENERATED', 'SYSTEM', 'SYSTEM');