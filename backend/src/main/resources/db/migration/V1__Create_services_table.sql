-- Services table: stores monitored service configurations
-- Uses schema specified in spring.flyway.default-schema property

CREATE TABLE IF NOT EXISTS services (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    url VARCHAR(512) NOT NULL,
    check_type VARCHAR(50) DEFAULT 'HTTP',
    current_status VARCHAR(50) DEFAULT 'Unknown',
    last_checked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
); 

CREATE INDEX IF NOT EXISTS idx_services_name ON services(name);