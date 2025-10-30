-- Sample data for testing service-status application
-- Run this in Supabase SQL Editor after Flyway migration completes

-- For LOCAL DEVELOPMENT (dev schema):
-- Copy and run this block in Supabase SQL Editor

INSERT INTO dev.services (name, url, check_type, current_status) 
VALUES 
  ('GitHub', 'https://github.com', 'HTTP', 'Unknown'),
  ('Google', 'https://www.google.com', 'HTTP', 'Unknown'),
  ('Stack Overflow', 'https://stackoverflow.com', 'HTTP', 'Unknown'),
  ('NPM Registry', 'https://registry.npmjs.org', 'HTTP', 'Unknown'),
  ('Invalid Service', 'https://thisdomaindoesnotexist12345.com', 'HTTP', 'Unknown')
ON CONFLICT (name) DO NOTHING;

-- Verify insertion:
SELECT * FROM dev.services;
